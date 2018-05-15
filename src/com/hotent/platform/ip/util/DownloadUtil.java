package com.hotent.platform.ip.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class DownloadUtil {
	/**
	 * 用于向客户端下载模板文件
	 * @param file
	 * @param out
	 */
	public static void downLoadFile(File file,OutputStream out){
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file)); 
	        byte[] buffer = new byte[fis.available()]; 
	        fis.read(buffer); 
	          
	        toClient = new BufferedOutputStream(out); 
	        toClient.write(buffer); 
	        toClient.flush(); 
	        toClient.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (toClient != null) {
					toClient.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void downLoadFile(InputStream in,OutputStream out){
		try {
			BufferedInputStream bis=new BufferedInputStream(in);
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=bis.read(buffer))!=-1){
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 批量导出数据
	 * @return
	 * @throws IOException
	 */
	public static void batchDownload(java.util.List<File> files,OutputStream out){
		ZipOutputStream zos = new ZipOutputStream(out);
		FileInputStream fis = null;
		try{
			for (File file : files) {
				fis = new FileInputStream(file);
				zos.putNextEntry(new ZipEntry(file.getName()));
				byte[] buffer = new byte[1024];
				int r;
				while ((r = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				fis.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	
	/**
	 * 批量导出数据更改文件名
	 * @return
	 * @throws IOException
	 */
	public static void batchDownloadChangeNameComm(List<File> files,OutputStream out,List<String> filesAlias){
		ZipOutputStream zos = new ZipOutputStream(out);
		FileInputStream fis = null;
		try{
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				String fileAlias = filesAlias.get(i);
				fis = new FileInputStream(file);
				zos.putNextEntry(new ZipEntry(fileAlias));
				byte[] buffer = new byte[1024];
				int r;
				while ((r = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				fis.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 批量导出数据更改文件名
	 * @return
	 * @throws IOException
	 */
	public static void batchDownloadChangeName(List<File> files,OutputStream out,List<String> filesAlias){
		ZipOutputStream zos = new ZipOutputStream(out);
		FileInputStream fis = null;
		try{
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				String fileAlias = filesAlias.get(i);
				fis = new FileInputStream(file);
				zos.putNextEntry(new ZipEntry(fileAlias + ".jpg"));
				byte[] buffer = new byte[1024];
				int r;
				while ((r = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				fis.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				fis.close();
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置下载的header头，特别是针对有汉字的文件下载
	 * @return
	 */
	public static String setDownloadHeader(String browser,String fileName)throws Exception{
        if(StringUtils.contains(browser, "MSIE")){//IE浏览器
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        }else if(StringUtils.contains(browser, "FIREFOX")){//google,火狐浏览器
            fileName = "=?UTF-8?B?" + (new String (Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";  //火狐文件名空格被截断问题
        }else{
            fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        }
        return fileName;
	}

}
