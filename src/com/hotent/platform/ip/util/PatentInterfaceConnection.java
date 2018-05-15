package com.hotent.platform.ip.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import com.hotent.core.util.AppConfigUtil;
@Deprecated
public class PatentInterfaceConnection {
	public static String _contentType="application/x-www-form-urlencoded";
	public static String _encode="UTF-8";
	public static String _application="application/x-www-form-urlencoded;charset=UTF-8";
	public static String REDIRECT_URI=AppConfigUtil.get("REDIRECT_URI");
	public static String CLIENT_ID=AppConfigUtil.get("CLIENT_ID");
	public static String CLIENT_SECRET=AppConfigUtil.get("CLIENT_SECRET").trim();
	public static String ACCESS_URL=AppConfigUtil.get("ACCESS_URL");
	public static String PICURL=AppConfigUtil.get("PICURL");
	public static String ACCESS_TOKEN=AppConfigUtil.get("ACCESS_TOKEN");
	public static String REFRESH_TOKEN=AppConfigUtil.get("REFRESH_TOKEN");
	public static String AUTHRIZE_URL=AppConfigUtil.get("AUTHRIZE_URL").trim();
	public static String OPENID=AppConfigUtil.get("OPENID");
	public static String SF1=AppConfigUtil.get("ACCESS_URL")+"sf1/";//专利信息概览检索接口{client_id}
	public static String SF2=AppConfigUtil.get("ACCESS_URL")+"sf2/";//专利信息细览检索接口{client_id}?pid={pid}
	public static String SF3=AppConfigUtil.get("ACCESS_URL")+"sf3/";//专利的法律状态概览检索接口{client_id}
	public static String SF4=AppConfigUtil.get("ACCESS_URL")+"sf4/";//专利的法律状态细览检索接口{client_id}
	public static String SF9=AppConfigUtil.get("ACCESS_URL")+"sf9/";//专利转让检索接口{client_id}
	public static String SF10=AppConfigUtil.get("ACCESS_URL")+"sf10/";//专利质押审查检索接口{client_id}
	public static String SF11=AppConfigUtil.get("ACCESS_URL")+"sf11/";//专利实施许可合同备案检索{client_id}
	public static String IF1=AppConfigUtil.get("ACCESS_URL")+"if1/";//所有的根分类号检索接口{client_id}
	public static String IF2=AppConfigUtil.get("ACCESS_URL")+"if2/";//检索某分类号的下级分类号接口{client_id}
	public static String IF3=AppConfigUtil.get("ACCESS_URL")+"if3/";//通过分类号关键词，检索分类号接口{client_id}
	public static String IF4=AppConfigUtil.get("ACCESS_URL")+"if4/";//通过分类内容关键词，检索分类号接口{client_id}
	//访问系统的步骤
	//1、https://open.cnipr.com/oauth2/authorize?client_id=7D9B57DB84EF88779C4345B26B05756D&response_type=code&redirect_uri=http://open.cnipr.com/oauth2
	//地址栏被重定向，取出code代码值
	//2、https://open.cnipr.com/oauth2/access_token?client_id=7D9B57DB84EF88779C4345B26B05756D&client_secret=5E8A434FD469F41DA9F2F760BB3B94D6&redirect_uri=http://open.cnipr.com/oauth2&grant_type=authorization_code&code=871885cc8e2be1d61b25ecf868ad7cfb
	//返回数据：{"status":0,"message":"SUCCESS","expires_in":2592000,"refresh_token":"b9d8e8d67d201dd702e60c4eda7157c3","access_token":"a7a9d9d0a4a73af8422314059ecb0059"}
	/**
	 * 判断是否可用链接
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean isConnected() throws UnsupportedEncodingException{
		boolean flag=false;
		//访问服务器的一个地址，看能不能正常获取链接
		String url=PatentInterfaceConnection.SF1+PatentInterfaceConnection.CLIENT_ID;
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(url);  
        httppost.setHeader("ContentType", PatentInterfaceConnection._contentType);  
        //封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("order", "申请号"));  
        nvps.add(new BasicNameValuePair("option", "2")); //按字检索 
        nvps.add(new BasicNameValuePair("from", "0"));  
        nvps.add(new BasicNameValuePair("to", "50"));  
        nvps.add(new BasicNameValuePair("displayCols", "AppNumber,AppDate,PubNumber,PubDate")); //支持多个字段用逗号连接；为空返回所有字段
        nvps.add(new BasicNameValuePair("openid", PatentInterfaceConnection.OPENID));
        nvps.add(new BasicNameValuePair("access_token", PatentInterfaceConnection.ACCESS_TOKEN));
        nvps.add(new BasicNameValuePair("dbs", "FMZL"));
        nvps.add(new BasicNameValuePair("exp", "申请号=CN201210105520.X%"));
        // 设置表单提交编码为UTF-8  
        UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, PatentInterfaceConnection._encode);  
        entry.setContentType(PatentInterfaceConnection._application);  
        httppost.setEntity(entry);  
        HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			JSONObject resultJson=null;
			if(entity!=null){
				resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
				EntityUtils.consume(entity);
			}
			if(response.getStatusLine().getStatusCode()==200&&resultJson!=null
					&&resultJson.containsKey("status")&&resultJson.getString("status").equals("0")){
				//网络错误异常！!!!
				flag=true;
				resultJson.clear();
			}
			nvps.clear();
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 动态刷新获取新的AccessToken
	 * https://open.cnipr.com/oauth2/access_token?
	 * client_id=7D9B57DB84EF88779C4345B26B05756D
	 * &client_secret=5E8A434FD469F41DA9F2F760BB3B94D6
	 * &redirect_uri=http://open.cnipr.com/oauth2
	 * &grant_type=refresh_token
	 * &refresh_token=b9d8e8d67d201dd702e60c4eda7157c3
	 * @return
	 */
	public static boolean refreshAccessToken(){
		boolean flag=false;
		String url=PatentInterfaceConnection.AUTHRIZE_URL+"/oauth2/access_token?client_id="+PatentInterfaceConnection.CLIENT_ID+"&client_secret="+PatentInterfaceConnection.CLIENT_SECRET+"&redirect_uri="+PatentInterfaceConnection.REDIRECT_URI+"&grant_type=refresh_token&refresh_token="+PatentInterfaceConnection.REFRESH_TOKEN;
		//使用httpGet执行链接，获取重定向信息
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpGet httpGet = new HttpGet(url);
        try {
			HttpResponse response=httpclient.execute(httpGet);
            //获取实际的请求对象的URI,即重定向之后的"/blog/admin/login.jsp"
			HttpEntity entity = response.getEntity();
			if(null != entity){
                JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
		        if(null!=resultJson){
		        	/*if(resultJson.containsKey("refresh_token")){
		        		PatentInterfaceConnection.REFRESH_TOKEN=resultJson.getString("refresh_token");
		        		SetProperties("REFRESH_TOKEN", resultJson.getString("refresh_token"));
		        	}*/
		        	if(resultJson.containsKey("access_token")){
		        		PatentInterfaceConnection.ACCESS_TOKEN=resultJson.getString("access_token");
		        		SetProperties("ACCESS_TOKEN", resultJson.getString("access_token"));
		        	}
		        	flag=true;
		        	resultJson.clear();
		        }
                EntityUtils.consume(entity);
            }
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return flag;
	}
	/**
	 * 授权Access_Token
	 * //访问系统的步骤
	//1、https://open.cnipr.com/oauth2/authorize?client_id=7D9B57DB84EF88779C4345B26B05756D&response_type=code&redirect_uri=http://open.cnipr.com/oauth2
	//地址栏被重定向，取出code代码值
	 * http://open.cnipr.com/oauth2/?openkey=b225c1fddc87a676ad13261a25da425f&openid=b914deb987bee3cf163391ff8453880b&code=3da5d5c42b4af31bc71702c59ca320ed
	//2、https://open.cnipr.com/oauth2/access_token?client_id=7D9B57DB84EF88779C4345B26B05756D&client_secret=5E8A434FD469F41DA9F2F760BB3B94D6&redirect_uri=http://open.cnipr.com/oauth2&grant_type=authorization_code&code=871885cc8e2be1d61b25ecf868ad7cfb
	//返回数据：{"status":0,"message":"SUCCESS","expires_in":2592000,"refresh_token":"b9d8e8d67d201dd702e60c4eda7157c3","access_token":"a7a9d9d0a4a73af8422314059ecb0059"}
	 */
	public static String authorizeAccessToken(){
		//获取授权链接
		String url=PatentInterfaceConnection.AUTHRIZE_URL+"/oauth2/authorize?client_id="+PatentInterfaceConnection.CLIENT_ID+"&response_type=code&redirect_uri="+PatentInterfaceConnection.REDIRECT_URI;
		//使用httpGet执行链接，获取重定向信息
		GetMethod getMethod = new GetMethod(url);
		HttpClient httpClient=new HttpClient();
		String token="";
        String code="";
        try {
        	int statusCode = httpClient.executeMethod(getMethod);
        	getMethod.releaseConnection();
        	//int statusCode=response.getStatusLine().getStatusCode();
        	 if(statusCode==HttpStatus.SC_OK){
        		 //将cookie值和用户名，密码输入,action的提交地址/oauth2/authorize/
        		 //Cookie[] cookies=httpClient.getState().getCookies();
        		 //调用登录方法
        		 code=authorizeLogin(httpClient);
        	 }
        	 //调用授权方法
    		 if(StringUtils.isNotEmpty(code)){
    			 token=getAccessToken(code);
    		 }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return token;
	}
	/**
	 * 接口的登录
	 * @param httpClient
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String authorizeLogin(HttpClient httpClient) throws HttpException, IOException{
		 String url=PatentInterfaceConnection.AUTHRIZE_URL+"/oauth2/authorize";
		 PostMethod postMethod=new PostMethod(url);
		 org.apache.commons.httpclient.NameValuePair account = new org.apache.commons.httpclient.NameValuePair("account", "bpmx");
		 org.apache.commons.httpclient.NameValuePair password = new org.apache.commons.httpclient.NameValuePair("password", "iloveipph");
		 postMethod.setRequestBody(new org.apache.commons.httpclient.NameValuePair[]{account,password});
		 int statusCode = httpClient.executeMethod(postMethod);
		 postMethod.releaseConnection();
		 String code="";
		 if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY) ||(statusCode == HttpStatus.SC_MOVED_PERMANENTLY) ||
   	     (statusCode == HttpStatus.SC_SEE_OTHER) ||(statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
   		 Header header=postMethod.getResponseHeader("location");
   		 //获取code值
	   		 if(null!=header){
	   			 String temp=header.getValue();
	   			 if(StringUtils.isNotEmpty(temp)){
	   				 String[] parameters=temp.split("&");
	   				 if(null!=parameters&&parameters.length>0){
	   					 for(String s:parameters){
	   						 if(StringUtils.isNotEmpty(s)){
	   							 String[] param=s.split("=");
	   							 if(null!=param&&param.length==2){
	   								 if("code".equals(param[0])){
	   									 code=param[1];
	   									 break;
	   								 }
	   							 }
	   						 }
	   					 }
	   				 }
	   			 }
	   		}
        }
		 return code;
	}
	/**
	 * 获取access_token和refresh_token
	 * @param code
	 */
	private static String getAccessToken(String code){
		String url=PatentInterfaceConnection.AUTHRIZE_URL+"/oauth2/access_token?client_id="+PatentInterfaceConnection.CLIENT_ID+
			"&client_secret="+PatentInterfaceConnection.CLIENT_SECRET+"&redirect_uri="+PatentInterfaceConnection.REDIRECT_URI+
			"&grant_type=authorization_code&code="+code;
		//使用httpGet执行链接，获取重定向信息
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpGet httpGet = new HttpGet(url);
        StringBuffer token=new StringBuffer();
        try {
			HttpResponse response=httpclient.execute(httpGet);
            //获取实际的请求对象的URI,即重定向之后的"/blog/admin/login.jsp"
			HttpEntity entity = response.getEntity();
			if(null != entity){
                JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
		        if(null!=resultJson){
		        	if(resultJson.containsKey("refresh_token")){
		        		PatentInterfaceConnection.REFRESH_TOKEN=resultJson.getString("refresh_token");
		        		SetProperties("REFRESH_TOKEN", resultJson.getString("refresh_token"));
		        		token.append("REFRESH_TOKEN="+resultJson.getString("refresh_token")+";");
		        	}
		        	if(resultJson.containsKey("access_token")){
		        		PatentInterfaceConnection.ACCESS_TOKEN=resultJson.getString("access_token");
		        		SetProperties("ACCESS_TOKEN", resultJson.getString("access_token"));
		        		token.append("ACCESS_TOKEN="+resultJson.getString("access_token"));
		        	}
		        	resultJson.clear();
		        }
                EntityUtils.consume(entity);
            }
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return token.toString();
	}
	/**
	 * 将属性写回属性文件
	 */
	public static void SetProperties(String key,String value){
		//属性文件的位置
		String path=PatentInterfaceConnection.class.getResource("/").getPath()+File.separator+"conf/app.properties";
		AppConfigUtil.set(key, value);
		try {
			AppConfigUtil.getPropertiesFile().store(new FileOutputStream(new File(path)), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
