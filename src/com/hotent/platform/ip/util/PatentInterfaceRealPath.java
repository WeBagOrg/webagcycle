package com.hotent.platform.ip.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.hotent.core.util.AppUtil;

public class PatentInterfaceRealPath {
	/**
	 * 获取专利图片本地存储路径
	 * @param urls
	 * @param appNumber
	 * @return
	 * 年/类型/申请号/图片文件
	 * url地址使用/来查找文件名
	 */
	public static List<String> getPictureRealPath(List<String> urls,String appNumber,String appDate,String dbName){
		List<String> realPath=new ArrayList<String>();
		//String ctxPath=AppUtil.getRealPath("/");
		StringBuffer sb=null;
		for(String fileName:urls){
			if(null==fileName||StringUtil.isEmpty(fileName)) continue;
			sb=new StringBuffer();
			//sb.append(ctxPath);
			sb.append("patent_pic").append(File.separator).append(appDate.substring(0,4)).append(File.separator).append(dbName).append(File.separator).append(appNumber).append(File.separator);
			if(fileName.contains("ThumbnailImage")){
				sb.append("thumbnail").append(File.separator);
			}
			else if(fileName.contains("BOOKS")){
				sb.append("BOOKS").append(File.separator);
				fileName=fileName.replace(".tif", ".png");
			}
			else if(isAbsPic(fileName)){
				sb.append("abstract").append(File.separator);//摘要附图
			}
			else{
				sb.append("drawing").append(File.separator);//说明书附图
			}
			sb.append(fileName.substring(fileName.lastIndexOf("/")+1));//文件的名称
			if(null!=sb&&sb.length()>0)
				realPath.add(sb.toString());
		}
		return realPath;
	}
	
	/**
	 * 判断是否是摘要附图
	 * @param path
	 * @param appNumber
	 * @return
	 */
	private static boolean isAbsPic(String path){
		String[] separatePath=path.split("/");
		if(null==separatePath||separatePath.length<2) return false;
		String fileName=separatePath[separatePath.length-1];
		fileName=fileName.substring(0,fileName.lastIndexOf("."));
		if(separatePath[separatePath.length-2].contains(fileName))
			return true;
		return false;
	}
	/**
	 * 获取xml的存储路径
	 * @return
	 * 年/类型/申请号/xml文件
	 * 文件名使用"申请号.xml"
	 */
	public static String constructXmlRealPath(String appNumber,String appDate,String dbName,boolean claims){
		String ctxPath=AppUtil.getRealPath("/");
		StringBuffer sb=new StringBuffer();
		sb.append(ctxPath).append("patent_pic").append(File.separator).append(appDate.substring(0,4)).append(File.separator).append(dbName).append(File.separator)
				.append(appNumber).append(File.separator);
		if(claims){
			sb.append("claims");
		}
		return sb.append(appNumber).append(".xml").toString();
	}
	
	/**
	 * 获取图片的url路径
	 * @param patentJson
	 * @return
	 */
	public static List<String> getPictureUrl(JSONObject patentJson){
		List<String> urls=new ArrayList<String>();
		//调用方法返回摘要图的图片路径
		if(patentJson.containsKey("draws")&&!"".equals(patentJson.getString("draws").trim())){
			urls.add(PatentInterfaceNetworkParse.getAbstractPic(patentJson.getString("draws")));
   	 	}
   	 	//外观专利的附图为tifDistributePath
   	 	//if(patentJson.getString("dbName").equalsIgnoreCase("WGZL")){
		//说明书的tif文件
 		if(patentJson.containsKey("tifDistributePath")&&!"".equals(patentJson.getString("tifDistributePath").trim())){
   			List<String> tifSpecificationPics=PatentInterfaceNetworkParse.getSpecificationPics(patentJson.getString("tifDistributePath"),patentJson.getInt("pages"),patentJson.getString("dbName"));
   			if(null!=tifSpecificationPics&&tifSpecificationPics.size()>0){
   				urls.addAll(tifSpecificationPics);
   				tifSpecificationPics.clear();
   			}
 		}
   	 	//}
   	 	//调用方法返回说明书附图的路径
   	 	if(patentJson.containsKey("instrTif")&&!"".equals(patentJson.getString("instrTif").trim())){
   	 		String picPrefixPath=PatentInterfaceNetworkParse.getSpecificationPicPrefixPath(patentJson.getString("dbName"), patentJson.getString("pubDate"), patentJson.getString("appNumber"));
   	 		List<String> specificationPics=PatentInterfaceXmlParse.parseXml2PatentInterfacePicName(patentJson.getString("instrTif"),"cn-patent-document/application-body/drawings/figure","img");
   	 		if(null!=specificationPics&&specificationPics.size()>0){
   	 			//说明书附图的描述内容
   	 			for(String path:specificationPics){
   	 				urls.add(picPrefixPath+path);//拼接成实际的网络路径
   	 			}
   	 			specificationPics.clear();
   	 		}
   	 	}
   	 	return urls;
	}
}
