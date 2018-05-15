package com.hotent.platform.ip.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.hotent.core.util.AppConfigUtil;
/**
 * 调用专利接口查询数据
 * @author Administrator
 *
 */
public class NewPatentInterfaceHttpClient{
	/**
	 * 通过pid获取专利信息
	 * @param pid
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	public JSONObject patentInterfaceByAppNumber(String appNumber,String dbName)throws Exception{
		//封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("order", "申请号"));  
        nvps.add(new BasicNameValuePair("from", "0"));  
        nvps.add(new BasicNameValuePair("to", "2"));  
		nvps.add(new BasicNameValuePair("dbs", dbName));
		if(appNumber.contains("'"))
			nvps.add(new BasicNameValuePair("exp","申请号="+appNumber));
		else{
			nvps.add(new BasicNameValuePair("exp","申请号='"+appNumber+"'"));
		}
		if(null==NewPatentInterfaceConnection.INTERFACEURL||"".equals(NewPatentInterfaceConnection.INTERFACEURL)){
			//http\://api.souips.com\:8080/ipms-pi/pat
			NewPatentInterfaceConnection.INTERFACEURL="http://api.souips.com:8080/ipms/pat";
		}
		return getByHttpClient(nvps, NewPatentInterfaceConnection.INTERFACEURL);
	}
	/**
	 * 获取专利法律状态信息
	 * @param url
	 * @param pids
	 * @param nvps
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 */
	public JSONObject PatentInterfaceForLawStatus(String appNumber,Date lawDate,int to,int size) throws Exception{
		//封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", to+""));
        nvps.add(new BasicNameValuePair("to", to+size+""));
		nvps.add(new BasicNameValuePair("exp", "申请号=%"+appNumber+"%"));
		if(null==NewPatentInterfaceConnection.INTERFACEPRS||"".equals(NewPatentInterfaceConnection.INTERFACEPRS)){
			NewPatentInterfaceConnection.INTERFACEPRS="http://api.souips.com:8080/ipms/prs";
		}
		return getByHttpClient(nvps, NewPatentInterfaceConnection.INTERFACEPRS);
	}
	/**
	 * 获取同用的请求参数
	 * @return
	 */
	private void getCommonParameters(List<NameValuePair> parameters){
		parameters.add(new BasicNameValuePair("order", "申请号"));
		parameters.add(new BasicNameValuePair("from", "0"));
		parameters.add(new BasicNameValuePair("to", "2"));
	}
	/**
	 * 获取同用的请求参数
	 * @return
	 */
	private void getLawCommonParameters(List<NameValuePair> parameters){
		parameters.add(new BasicNameValuePair("option", "2")); //按字检索 
		parameters.add(new BasicNameValuePair("openid", AppConfigUtil.get("OPENID")));
		parameters.add(new BasicNameValuePair("access_token", AppConfigUtil.get("ACCESS_TOKEN")));
	}
	/**
	 * 选择库
	 * @param nvps
	 * @param dbName
	 */
	@SuppressWarnings("unused")
	private void setPatentInterfaceDb(List<NameValuePair> nvps,String dbName){
    	if(null!=dbName){
    		if(dbName.equalsIgnoreCase("fmzl")){
    			nvps.add(new BasicNameValuePair("dbs", "FMZL"));
    			nvps.add(new BasicNameValuePair("dbs", "FMSQ"));
    		}
    		else if(dbName.equalsIgnoreCase("syxx")){
    			nvps.add(new BasicNameValuePair("dbs", "SYXX"));  
    		}
    		else{
    			nvps.add(new BasicNameValuePair("dbs", "WGZL"));
    		}
    	}
	}
	/**
	 * 封装申请号
	 * @param nvpsList
	 */
	@SuppressWarnings("unused")
	private BasicNameValuePair getApplycodeParameters(List<String> appNumbers){
		String appcode="";
		if(appNumbers.size()>1){
			StringBuffer sb=new StringBuffer();
			sb.append("申请号=( " );
    		for(String code:appNumbers){
    			if(StringUtils.isNotEmpty(code))
    				sb.append("'"+code+"',");
    		}
    		appcode=sb.substring(0, sb.length()-1);
    		appcode+=")";
    	}
		return new BasicNameValuePair("exp", appcode);
	}
	 /**
	  * 检索接口获取专利的公开/授权信息
	  * @param appNumber
	  * @param dbName
	  * @param patentMap
	  */
	 public void patentInterfacePubInfo(String appNumber,Map<String,String> patentMap)throws Exception{
			//封装请求参数
	        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
	        nvps.add(new BasicNameValuePair("order", "申请号"));
	        nvps.add(new BasicNameValuePair("from", "0"));
	        nvps.add(new BasicNameValuePair("to", "2"));
			nvps.add(new BasicNameValuePair("dbs", "FMZL,FMSQ"));
			nvps.add(new BasicNameValuePair("exp","申请号='"+appNumber+"'"));
			nvps.add(new BasicNameValuePair("displayCols","appNumber,pubNumber,pubDate,dbName"));
			if(null==NewPatentInterfaceConnection.INTERFACEURL||"".equals(NewPatentInterfaceConnection.INTERFACEURL)){
				NewPatentInterfaceConnection.INTERFACEURL="http://api.souips.com:8080/ipms-pi/pat";
			}
			JSONObject resultJson=getByHttpClient(nvps, NewPatentInterfaceConnection.INTERFACEURL);
			if(null!=resultJson&&resultJson.get("message").equals("SUCCESS")){
            	JSONArray jsonArray=(JSONArray) resultJson.get("results");
		     	if(null!=jsonArray&&jsonArray.size()>0){
		     		for(int i=0;i<jsonArray.size();i++){
		     		JSONObject patentJson=jsonArray.getJSONObject(i);
		     		patentMap.put(patentJson.getString("appNumber")+";"+patentJson.getString("dbName"),
					patentJson.getString("pubNumber")+";"+patentJson.getString("pubDate"));
		     		patentJson.clear();
		     		}
		     	}
		     	jsonArray.clear();
		     	resultJson.clear();
    		}	           
	 }
	 
	 /**
		 * 通过HttpClient获取网络数据
		 * @return
		 */
		private JSONObject getByHttpClient(List<NameValuePair> nvps,String url)throws Exception{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			//请求超时
		    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,200000);
		    //读取超时
		    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		    //内容编码
		    httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		    HttpPost httpPost = new HttpPost(url);
		    //服务密码
		    //nvps.add(new BasicNameValuePair("serverPassword", PatentInterfaceConnection.SERVERPASSWORD));
		    nvps.add(new BasicNameValuePair("machineCode", NewPatentInterfaceConnection.MACHINECODE));
		    //服务器加密时间戳
	  		Map<String,String> passParam=MD5Util.getEncodeParam();
	  		if(passParam.size()>0){
	  			for(String key:passParam.keySet()){
	  				nvps.add(new BasicNameValuePair(key, passParam.get(key)));
	  			}
	  		}
		    // 设置表单提交编码为UTF-8  
		    httpPost.setHeader("ContentType", "application/json");
		    UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, NewPatentInterfaceConnection._encode);  
		    entry.setContentType(NewPatentInterfaceConnection._application);  
		    httpPost.setEntity(entry);
		    //发送post请求
		    HttpResponse response = httpClient.execute(httpPost);  
		    if(response.getStatusLine().getStatusCode()==200){
		    	HttpEntity entity = response.getEntity();
				JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity,"UTF-8"));
				//System.out.println(resultJson.toString());
				nvps.clear();
				httpPost.abort();//释放资源
				EntityUtils.consume(entity);//销毁资源  
				httpClient.getConnectionManager().shutdown();
				if(null!=resultJson&&resultJson.get("message").equals("SUCCESS")){
				 	return resultJson;
				}
		    }
		    return null;
		}
}
