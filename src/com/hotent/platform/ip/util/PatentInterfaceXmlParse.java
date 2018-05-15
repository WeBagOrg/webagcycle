package com.hotent.platform.ip.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.fusesource.hawtbuf.ByteArrayInputStream;
/**
 * 解析接口返回的xml文件
 * @author Administrator
 *
 */
public class PatentInterfaceXmlParse {
	/**
	  * 解析出专利接口中的附图名称
	  * @param xml
	  * @param path
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 public static List<String> parseXml2PatentInterfacePicName(String xml,String path,String tag) { 
		 if(StringUtils.isEmpty(xml)) return null;
		 List<String> fileNames = new ArrayList<String>();  
	     Document doc = null;  
	     try{  
	    	doc=getDocument(xml);
	 		List<Element> figures=doc.selectNodes(path);
	        if(null!=figures&&figures.size()>0){
	        	for(Element e:figures){
	        		Element element=e.element(tag);
	        		if(null!=element){
	        			fileNames.add(element.attributeValue("file").replace("TIF", "gif").replace("tif", "gif"));
	        		}
	        	}
	        }
	     } catch (DocumentException e) {  
	    	 e.printStackTrace();  
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     }  
	     return fileNames;  
	 }
	 /**
	  * 解析出说明书附图的描述信息
	  */
	 @SuppressWarnings("unchecked")
	 public static List<String> parseXml2PatentInterfacePicDescription(String xml,String path,String tag) {  
		 List<String> picDescriptions = new ArrayList<String>();  
	     try{  
	    	Document doc=getDocument(xml);
	 		List<Element> drawings=doc.selectNodes(path);//获取到description-of-drawings节点
	        if(null!=drawings&&drawings.size()>0){
	        	List<Element> descriptions=drawings.get(0).selectNodes(tag);
	        	if(null!=descriptions&&descriptions.size()>0){//忽略掉前两个p节点
	        		for(int i=0;i<descriptions.size();i++){
		        		Element element=descriptions.get(i);
		        		if(null!=element&&isPictureDescription(element.getTextTrim())){
		        			picDescriptions.add(element.getTextTrim().replaceAll("<br/>", "").replaceAll("\n\r", "").replaceAll("\n", ""));
		        		}
		        	}
	        	}
	        }
	     } catch (DocumentException e) {  
	    	 e.printStackTrace();  
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     }  
	     return picDescriptions;  
	 }
	 /**
	  * 解析说明书的内容
	  * @param xml
	  * @param path
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 @Deprecated
	 public static Map<String,String> parseXml2PatentInterfaceSpecification(String xml,String path){
		 Map<String,String> map=new HashMap<String,String>();
		 try{  
		    	Document doc=getDocument(xml);
		 		List<Element> description=doc.selectNodes(path);//获取到description-of-drawings节点
		        if(null!=description&&description.size()>0){
		        	//获取invention-title节点
		        	Element title=description.get(0).element("invention-title");
		        	if(title!=null){
		        		map.put("invention-title", title.getTextTrim());
		        	}
		        }
		 } catch (DocumentException e) {  
			 e.printStackTrace();  
		 } catch (Exception e) {  
			 e.printStackTrace();  
		 }
		 return map;
	 }
	 /**
	  * 保存xml数据到本地
	  * @param xml
	  * @param realPath
	  */
	 public static void saveXml(String xml,String realPath){
		 FileOutputStream fos=null;
		 BufferedOutputStream bos=null;
		 try{  
			 if(StringUtils.isNotEmpty(realPath)){
				 String directoryPath=realPath.substring(0,realPath.lastIndexOf(File.separator));
				 File directory=new File(directoryPath);
				 if(!directory.exists()){
					 directory.mkdirs();
				 }
				 /*Document doc=getDocument(xml);
				 Writer writer=new FileWriter(realPath);
				 doc.write(writer);*/
				 fos=new FileOutputStream(realPath);
				 bos=new BufferedOutputStream(fos);
				 bos.write(xml.getBytes("utf-8"));
			 } 
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 finally{
			try {
				 if(null!=bos){
					 bos.close();
				 } 
				 if(null!=fos){
					 fos.close();
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
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
	 public static Document getDocument(String xml)throws Exception{
		 SAXReader sax=new SAXReader();
		 sax.setEntityResolver(new IgnoreDTDEntityResolver());
		 InputStream in=new ByteArrayInputStream(xml.getBytes("UTF-8"));//传递文本的编码方式
	 	 UnicodeReader reader=new UnicodeReader(in, null);
	 	 Document doc=sax.read(reader);
	 	 return doc;
	 } 
	 /**
	  * 判断是否是说明书附图描述信息
	  * @param text
	  * @return
	  */
	 private static boolean isPictureDescription(String text){
		 Pattern p=Pattern.compile("^图\\d+");
		 Matcher m=p.matcher(text);
		 return m.find();
	 }
	 /**
	  * 判断是否是PcT申请
	  * @param text
	  * @return
	  */
	 public static boolean isPctCountry(String text){
		 Pattern p=Pattern.compile("[A-Z]{2}");
		 Matcher m=p.matcher(text.split(";")[1]);
		 return m.matches();
	 }
}
