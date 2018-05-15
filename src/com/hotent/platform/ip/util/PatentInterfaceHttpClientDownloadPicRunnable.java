package com.hotent.platform.ip.util;

import java.io.BufferedInputStream;

import com.hotent.core.util.AppConfigUtil;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 调用专利接口查询数据
 * @author Administrator
 *
 */
public class PatentInterfaceHttpClientDownloadPicRunnable implements Runnable{
	private String webAddress = "";  
    private String destFile = "";  
      
    public void setWebAddress(String webAddress){  
    	this.webAddress = webAddress;  
    }  
      
    public void setDestFile (String destFile){  
    	this.destFile = destFile;  
    }
    public PatentInterfaceHttpClientDownloadPicRunnable(String address){
    	this.webAddress=address.split(";")[0];
    	this.destFile=address.split(";")[1];
    }
    /**
     * 实现图片的下载
     */
	@Override
	public void run() {
		//判断文档路径是否存在，如果不存在则新建
        directionBuilder(destFile);
        File file=new File(destFile);
        if(!file.exists()){
    		HttpURLConnection httpConn = null;  
    	    BufferedInputStream bis=null;
    	    BufferedOutputStream bos=null;
    	    InputStream in=null;
    	    FileOutputStream out=null;
    	    String picUrl=AppConfigUtil.get("PICURL");
    	    if(null==picUrl) picUrl="http://pic.cnipr.com:8080/";
    	    try {  
    	    	
    	        URL url = new URL(picUrl+webAddress);  
    	        httpConn = (HttpURLConnection) url.openConnection();  
    	        httpConn.setRequestProperty("Use-Contrl", "LIPTIFACTIVEX");
    	        httpConn.setRequestProperty("Use-Agent", "LIDOWN");
    	        httpConn.setRequestProperty("Check-Code", "1234");
    	        httpConn.setRequestProperty("Connection", "Keep-Alive");
    	        httpConn.setRequestProperty("Cache-Control", "no-cache");
    	        //httpConn.setConnectTimeout(5*1000);//设置连接超时时间为5s
    	        in = httpConn.getInputStream();
    	        out = new FileOutputStream(file);
    	        if(webAddress.indexOf("tif")!=-1||webAddress.indexOf("TIF")!=-1){
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
     * 判断路径是否存在，如果不存在，则创建
     * @param path
     */
    private void directionBuilder(String path){
    	String realPath=null;
    	if(path.indexOf(File.separator)!=-1){
    		realPath=path.substring(0,path.lastIndexOf(File.separator));
    	}else{
    		realPath=path.substring(0,path.lastIndexOf("/"));
    	}
    	File file=new File(realPath);
    	if(!file.exists())
    		file.mkdirs();
    }
}
