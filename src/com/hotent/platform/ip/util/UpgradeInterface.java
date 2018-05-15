package com.hotent.platform.ip.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

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

public class UpgradeInterface {
	public static String UPGRADEINTERFACEURL=AppConfigUtil.get("upgrade.package");//"http://192.168.3.120:8080/upgrade/package.do";
	public static String CONNECTION=AppConfigUtil.get("upgrade.connection");//"http://192.168.3.120:8080/upgrade/connection.do";
	public static String DOWNLAODURL=AppConfigUtil.get("upgrade.downloadUrl");//"http://192.168.3.120:8080/upgrade/download.do";
	public static String FILEEXISTSURL=AppConfigUtil.get("upgrade.fileExistsUrl");//"http://192.168.3.120:8080/upgrade/exists.do";
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
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(UpgradeInterface.CONNECTION);  
        httppost.setHeader("ContentType", UpgradeInterface._jsonApplication);  
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
				if(resultJson!=null&&resultJson.containsKey("message")&&resultJson.getString("message").equals(UpgradeInterface._status)){
					flag=true;
					resultJson.clear();
				}
				httpclient.getConnectionManager().shutdown();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 查询获得更新包的信息
	 * @return
	 */
	public static JSONObject getUpgradePackge(){
		JSONObject resultJson=null;
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(UpgradeInterface.UPGRADEINTERFACEURL);  
        httppost.setHeader("ContentType", UpgradeInterface._jsonApplication);  
        HttpResponse response;
		try {
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
					EntityUtils.consume(entity);
				}
				httpclient.getConnectionManager().shutdown();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultJson;
	}
	/**
	 * 下载更新包
	 * 将获取的内容接入到一个输入流，然后再接到输出流上。
	 */
	public static void downloadPackageOld(int id,ServletOutputStream out){
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(UpgradeInterface.DOWNLAODURL);  
        HttpResponse response;
        InputStream in=null;
        //封装参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", id+""));
		try {
			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, "UTF-8"); 
			httppost.setEntity(entry);
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					//将内容放入到一个输入流中，输入流读入缓存，然后输出到输出流中
					in=entity.getContent();
					BufferedInputStream bis=new BufferedInputStream(in);
					byte[] buffer=new byte[1024];
					int len=0;
					while((len=bis.read(buffer))!=-1){
						out.write(buffer, 0, len);
					}
					out.flush();
					EntityUtils.consume(entity);
				}
				httpclient.getConnectionManager().shutdown();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			//关闭流
			try {
				if(null!=in)in.close();
				if(null!=out) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 下载文档，返回下载的输入流
	 * @param id
	 * @return
	 */
	@Deprecated
	public static InputStream downloadPackage(int id){
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(UpgradeInterface.DOWNLAODURL);  
        HttpResponse response;
        InputStream in=null;
        //封装参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", id+""));
		try {
			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, "UTF-8"); 
			httppost.setEntity(entry);
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					//返回inputStream无法使用，这里已经将链接断掉了。
					in=entity.getContent();
					EntityUtils.consume(entity);
				}
				httpclient.getConnectionManager().shutdown();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	public static boolean isExists(int id){
		boolean flag=false;
		DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(UpgradeInterface.FILEEXISTSURL);  
        httppost.setHeader("ContentType", UpgradeInterface._jsonApplication);  
        HttpResponse response;
        //封装参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("id", id+""));
		try {
			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, "UTF-8");
			httppost.setEntity(entry);
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode()==200){
				JSONObject resultJson=null;
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
					EntityUtils.consume(entity);
				}
				if(resultJson!=null&&resultJson.containsKey("result")&&resultJson.getString("result").equals(UpgradeInterface._status)){
					flag=true;
					resultJson.clear();
				}
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
