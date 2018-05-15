package com.hotent.platform.ip.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.web.security.ServicePassCipher;

public class NewPatentInterfaceConnection {
	public static String INTERFACEURL=AppConfigUtil.get("interfaceUrl");//专利检索接口
	public static String INTERFACEPRS=AppConfigUtil.get("interfacePrs");//专利法律状态接口
	public static String SERVERPASSWORD=AppConfigUtil.get("service.pass");//密码
	public static String MACHINECODE=ServicePassCipher.getMachineCode();//计算机名_mac地址,使用下划线分隔
	public static String _contentType="application/x-www-form-urlencoded";
	public static String _encode="UTF-8";
	public static String _application="application/x-www-form-urlencoded;charset=UTF-8";
	public static String _jsonApplication="application/json";
	public static String _status="SUCCESS";
	/**
	 * 判断是否可用链接
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean isConnected() {
		boolean flag=false;
		//访问服务器的一个地址，看能不能正常获取链接
		if(null==NewPatentInterfaceConnection.INTERFACEURL){
			NewPatentInterfaceConnection.INTERFACEURL="http://api.souips.com:8080/ipms-pi/pat";//"http://59.151.99.154:8080/ipms-pi/pat";
		}
		if(null==NewPatentInterfaceConnection.INTERFACEPRS){
			NewPatentInterfaceConnection.INTERFACEPRS="http://api.souips.com:8080/ipms-pi/prs";//"http://59.151.99.154:8080/ipms-pi/prs";
		}
		
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(NewPatentInterfaceConnection.INTERFACEURL);  
        httppost.setHeader("ContentType", NewPatentInterfaceConnection._jsonApplication);  
        //封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("order", "申请号"));  
        nvps.add(new BasicNameValuePair("displayCols", "appNumber,appDate")); //支持多个字段用逗号连接；为空返回所有字段
        nvps.add(new BasicNameValuePair("dbs", "FMZL"));
        nvps.add(new BasicNameValuePair("exp", "申请号=('CN201210105520.X')"));
        nvps.add(new BasicNameValuePair("from", "0")); //支持多个字段用逗号连接；为空返回所有字段
        nvps.add(new BasicNameValuePair("to", "1"));
        //服务密码
        //nvps.add(new BasicNameValuePair("serverPassword", NewPatentInterfaceConnection.SERVERPASSWORD));  
        nvps.add(new BasicNameValuePair("machineCode", NewPatentInterfaceConnection.MACHINECODE));
        //服务器加密时间戳
		Map<String,String> passParam=MD5Util.getEncodeParam();
		if(passParam.size()>0){
			for(String key:passParam.keySet()){
				nvps.add(new BasicNameValuePair(key, passParam.get(key)));
			}
		}
        // 设置表单提交编码为UTF-8  
        UrlEncodedFormEntity entry;
		try {
			entry = new UrlEncodedFormEntity(nvps, NewPatentInterfaceConnection._encode);
			entry.setContentType(NewPatentInterfaceConnection._application);  
	        httppost.setEntity(entry);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
        HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()==200){
				JSONObject resultJson=null;
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
					EntityUtils.consume(entity);
				}
				if(resultJson!=null&&resultJson.containsKey("message")&&resultJson.getString("message").equals(NewPatentInterfaceConnection._status)){
					flag=true;
					resultJson.clear();
				}
				nvps.clear();
				httpclient.getConnectionManager().shutdown();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
