package com.hotent.platform.ip.util;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlToDocument{

	/*
	 * xml解析成Document对象
	 */
	public static Document xmlToDocument (String realpath)  throws Exception{
		File file = new File(realpath);
		if(file.exists()){
			StringBuilder sb = new StringBuilder();
			FileInputStream fis=null;
			BufferedReader bf=null;
			fis = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			String str="";
			int i=0;
			while((str=bf.readLine())!=null){
				sb.append(str);
			}		
			return getDocument(sb.toString());
		}
		return null;
	}
		  
	
	 /**
	  * 获取document对象
	  * @param xml
	  * @return
	  * @throws Exception
	  * 去掉dtd校验文件,使用正则方式替换字符串
	  * xml=xml.replaceFirst("<!DOCTYPE.+dtd\">", "");
	  * sax.setValidation(false);，虽然设置了不校验dtd，还是会加载dtd文件
	  * if(xml.charAt(0)==65279){//跳过第一字符
	  *  xml=xml.substring(1);
	  * }
	  */
	 private static Document getDocument(String xml)throws Exception{
		 SAXReader sax=new SAXReader();
		 sax.setEntityResolver(new IgnoreDTDEntityResolver());
		 InputStream in=new ByteArrayInputStream(xml.getBytes("UTF-8"));//传递文本的编码方式
	 	 UnicodeReader reader=new UnicodeReader(in, "UTF-8");
	 	 Document doc=sax.read(reader);
	 	 return doc;
	 } 
}
