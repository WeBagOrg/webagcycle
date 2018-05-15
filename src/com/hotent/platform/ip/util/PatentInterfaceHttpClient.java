package com.hotent.platform.ip.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.hotent.core.util.AppConfigUtil;
import com.hotent.core.util.AppUtil;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
/**
 * 调用专利接口查询数据
 * @author Administrator
 *
 */
@Deprecated
public class PatentInterfaceHttpClient {
	/**
	 * 获取专利的概要信息，将公开和授权信息存放到map中，并返回pid集合
	 * @param url
	 * @param nvps
	 * @return
	 * @throws Exception
	 */
	public List<String> patentInterfaceForImport(List<String> appNumbers,Map<String,String> patentMap) throws Exception { 
		List<String> pidList=new ArrayList<String>();
		//封装请求参数
		if(appNumbers!=null&&appNumbers.size()>0){
			BasicNameValuePair paramter=getApplycodeParameters(appNumbers);
	        JSONObject resultJson=getContentFromPatentInterfaceByPost(paramter);
			if(null!=resultJson&&resultJson.get("message").equals("SUCCESS")){//查询成功时
				 JSONArray jsonArray=(JSONArray) resultJson.get("results");
			     if(null!=jsonArray&&jsonArray.size()>0){
			    	 JSONObject patentJson=null;
			 	   	for(int index=0;index<jsonArray.size();index++){
			 			patentJson=jsonArray.getJSONObject(index);
			 			if(patentJson.containsKey("pid")){//向map中设置值
			 				pidList.add(patentJson.getString("pid")+";"+patentJson.getString("dbName"));
			 				patentMap.put(patentJson.getString("appNumber")+";"+patentJson.getString("dbName"),
			 						patentJson.getString("pubNumber")+";"+patentJson.getString("pubDate")+";"+patentJson.getString("pid"));
			 			}
			 	     }
			 	   	patentJson.clear();
			     }
			     jsonArray.clear();
			}
			resultJson.clear();
		}
        return pidList;
    }
	/**
	 * 根据申请号查询出相关的pid和查询库名，返回map对象
	 * @param appNumbers
	 * @param patentMap
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> patentInterfaceForDisplay(String appNumber) throws Exception { 
		Map<String,String> interfaceMap=new HashMap<String,String>();
		//封装请求参数
        JSONObject resultJson=getContentFromPatentInterfaceByPost(new BasicNameValuePair("exp", "申请号="+appNumber+"%"));
		if(resultJson.get("message").equals("SUCCESS")){//查询成功时
			 JSONArray jsonArray=(JSONArray) resultJson.get("results");
		     if(null!=jsonArray&&jsonArray.size()>0){
		 	   	for(int index=0;index<jsonArray.size();index++){
		 			JSONObject patentJson=jsonArray.getJSONObject(index);
		 			if(patentJson.containsKey("pid")){//向map中设置值
		 				interfaceMap.put(patentJson.getString("appNumber")+";"+patentJson.getString("dbName"),patentJson.getString("pid")+";"+patentJson.getString("dbName"));
		 			}
		 	     }
		     }
		}
		resultJson.clear();
        return interfaceMap;
    }
	/**
	 * 获取专利的概要信息
	 * @param nvps
	 */
	private JSONObject getContentFromPatentInterfaceByPost(BasicNameValuePair parameter)throws Exception{
		String url=PatentInterfaceConnection.SF1+AppConfigUtil.get("CLIENT_ID");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
        //读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        HttpPost httppost = new HttpPost(url);  
        httppost.setHeader("ContentType", PatentInterfaceConnection._contentType);
        
        //封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        getCommonParameters(nvps);
        nvps.add(parameter);
        // 设置表单提交编码为UTF-8  
        UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, PatentInterfaceConnection._encode);  
        entry.setContentType(PatentInterfaceConnection._application);  
        httppost.setEntity(entry);  
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity, "UTF-8"));
        nvps.clear();
        httppost.abort();//是否资源
        EntityUtils.consume(entity);//销毁资源  
        httpClient.getConnectionManager().closeExpiredConnections();
        httpClient.getConnectionManager().shutdown();
        return resultJson;
	}
	/**
	 * 通过pid获取专利信息
	 * @param pid
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	public JSONObject patentInterfaceByPid(String pid,String dbName)throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		 //请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
        //读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		//封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
		getPidCommonParameters(nvps);
		nvps.add(new BasicNameValuePair("dbs", dbName));
		nvps.add(new BasicNameValuePair("pid", pid));
		String url=PatentInterfaceConnection.SF2+AppConfigUtil.get("CLIENT_ID");
        HttpGet httpGet = new HttpGet(url);  
        httpGet.addHeader("Content-Type", "application/json;charset=utf-8");
        httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + EntityUtils.toString(new UrlEncodedFormEntity(nvps,"UTF-8"))));
        HttpResponse response = httpClient.execute(httpGet);  
        HttpEntity entity = response.getEntity();
        JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity,"UTF-8"));
        nvps.clear();
        httpGet.abort();//释放资源
        EntityUtils.consume(entity);//销毁资源  
        httpClient.getConnectionManager().shutdown();
        if(null!=resultJson&&resultJson.get("message").equals("SUCCESS")){
			return resultJson;
		}
        return null;
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
		String url=PatentInterfaceConnection.SF3+AppConfigUtil.get("CLIENT_ID");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		 //请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
        //读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		HttpPost httppost = new HttpPost(url);
		//封装请求参数
        List<NameValuePair> nvps=new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("from", to+""));
        nvps.add(new BasicNameValuePair("to", to+size+""));
        getLawCommonParameters(nvps);
		nvps.add(new BasicNameValuePair("exp", "申请号=%"+appNumber+"%"));
        // 设置表单提交编码为UTF-8  
        UrlEncodedFormEntity entry = new UrlEncodedFormEntity(nvps, "UTF-8");  
        entry.setContentType("application/x-www-form-urlencoded;charset=UTF-8");  
        httppost.setEntity(entry);
        HttpResponse response = httpClient.execute(httppost);  
        HttpEntity entity = response.getEntity(); 
        JSONObject resultJson=JSONObject.fromObject(EntityUtils.toString(entity,"UTF-8"));
        nvps.clear();
        httppost.abort();//释放资源
        EntityUtils.consume(entity);//销毁资源  
        httpClient.getConnectionManager().shutdown();
        if(null!=resultJson&&resultJson.get("message").equals("SUCCESS")){
			return resultJson;
		}
        return null;
	}
	/**
	 * 获取同用的请求参数
	 * @return
	 */
	private void getCommonParameters(List<NameValuePair> parameters){
		parameters.add(new BasicNameValuePair("order", "申请号"));  
		parameters.add(new BasicNameValuePair("option", "2")); //按字检索 
		parameters.add(new BasicNameValuePair("from", "0"));  
		parameters.add(new BasicNameValuePair("to", "50"));  
		parameters.add(new BasicNameValuePair("displayCols", "AppNumber,AppDate,PubNumber,PubDate")); //支持多个字段用逗号连接；为空返回所有字段
		parameters.add(new BasicNameValuePair("openid", AppConfigUtil.get("OPENID")));
		parameters.add(new BasicNameValuePair("access_token", AppConfigUtil.get("ACCESS_TOKEN")));
		parameters.add(new BasicNameValuePair("dbs", "FMZL"));
		parameters.add(new BasicNameValuePair("dbs", "FMSQ"));
		parameters.add(new BasicNameValuePair("dbs", "SYXX"));
		parameters.add(new BasicNameValuePair("dbs", "WGZL"));
	}
	/**
	 * 获取pid的请求参数
	 * @return
	 */
	private void getPidCommonParameters(List<NameValuePair> parameters){
		parameters.add(new BasicNameValuePair("option", "2")); //按字检索 
		parameters.add(new BasicNameValuePair("from", "0"));  
		parameters.add(new BasicNameValuePair("to", "1"));  
		//parameters.add(new BasicNameValuePair("displayCols", "statusCode,proCode,mainIpc,abs,address,agencyName,agentName,appDate,applicantName,appNumber,den,family,iapp,inventroName,ipc,ipub,patType,priority,pubDate,pubNumber,claimsPath,cipPath,title,gazettePath,gazettePage,gazetteCount,patentWords,autoAbs,instrPath,instrTif,censor,refDoc,pages,tifDistributePath,draws,priorityDate,issueDate")); //支持多个字段用逗号连接；为空返回所有字段 
		parameters.add(new BasicNameValuePair("openid", AppConfigUtil.get("OPENID")));
		parameters.add(new BasicNameValuePair("access_token", AppConfigUtil.get("ACCESS_TOKEN")));
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
	private BasicNameValuePair getApplycodeParameters(List<String> appNumbers){
		String appcode="";
		if(appNumbers.size()>1){
			StringBuffer sb=new StringBuffer();
    		for(String code:appNumbers){
    			if(StringUtils.isNotEmpty(code))
    				sb.append("or 申请号=(%"+code+"%) ");
    		}
    		appcode=sb.substring(3);
    	}
    	else{
    		appcode="申请号=%"+appNumbers.get(0)+"%";
    	}
		return new BasicNameValuePair("exp", appcode);
	}
	/**
	 * 下载图片
	 * @param webPath
	 * @param realPath
	 */
	public void patentInterfaceDownloadPic(String webPath,String realPath){
		String filepath=AppUtil.getRealPath("/")+realPath;
		directionBuilder(filepath);
        File file=new File(filepath);
        if(!file.exists()){
    		HttpURLConnection httpConn = null;  
    	    BufferedInputStream bis=null;
    	    BufferedOutputStream bos=null;
    	    InputStream in=null;
    	    FileOutputStream out=null;
    	    String picUrl=AppConfigUtil.get("PICURL");
    	    if(null==picUrl) picUrl="http://pic.cnipr.com:8080/";
    	    try {  
    	        URL url = new URL(picUrl+webPath);  
    	        httpConn = (HttpURLConnection) url.openConnection();  
    	        httpConn.setRequestProperty("Use-Contrl", "LIPTIFACTIVEX");
    	        httpConn.setRequestProperty("Use-Agent", "LIDOWN");
    	        httpConn.setRequestProperty("Check-Code", "1234");
    	        httpConn.setRequestProperty("Connection", "Keep-Alive");
    	        httpConn.setRequestProperty("Cache-Control", "no-cache");
    	        //httpConn.setConnectTimeout(5*1000);//设置连接超时时间为5s
    	        in = httpConn.getInputStream();
    	        out = new FileOutputStream(file);
    	        if(webPath.indexOf("tif")!=-1||webPath.indexOf("TIF")!=-1){
    		        ImageDecoder decoder = ImageCodec.createImageDecoder("tiff",in,null);
    		        ImageEncoder encoder = ImageCodec.createImageEncoder("png",out,null);
    		        encoder.encode( decoder.decodeAsRenderedImage() );
    	        }
    	        else{
        	        bis=new BufferedInputStream(in);
        	        byte[] buf=new byte[1024];
        	        bos=new BufferedOutputStream(out);
        	        int len=0;
        	        while((len=bis.read(buf))>0){
        	        	bos.write(buf,0,len);
        	        }
    	        }
    	    } catch (Exception ex) {  
    	        ex.printStackTrace();
    	    } finally {  
    	    	if(null!=httpConn)
    	    		httpConn.disconnect();//关闭连接
    	        try {
    		        if(null!=bis){
    		        	bis.close();
    		        }
    		        if(null!=in){
    		        	in.close();
    		        }
    		        if(null!=bos){
    		        	bos.close();
    		        }
    		        if(null!=out){
    		        	out.close();
    		        }
    	        } catch (IOException e) {
    				e.printStackTrace();
    			}
    	    }  
        }
	}
	/**
	 * 构造文件夹路径
	 * @param path
	 */
	 private void directionBuilder(String path){
	    	String realPath=path.substring(0,path.lastIndexOf(File.separator));
	    	File file=new File(realPath);
	    	if(!file.exists())
	    		file.mkdirs();
	    }
}
