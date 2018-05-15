package com.hotent.platform.ip.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 用于解析出接口中的图片路径
 * @author Administrator
 *
 */
/**
 * 该类的主要功能是获取数据在网络上的地址
 * @author Administrator
 *
 */
public class PatentInterfaceNetworkParse {
	/**
	 * 获取摘要附图的路径
	 * @param draws
	 * @return
	 */
	public static String getAbstractPic(String draws){
		if("".equals(draws.trim())) return null;
		if(StringUtils.isNotEmpty(draws)&&draws.indexOf(".")>0){
			draws=draws.replaceAll("\\\\", "/");
			String fileName=draws.substring(draws.lastIndexOf("/"),draws.indexOf(".")+1)+"gif";
			if(draws.indexOf("XmlData")==-1){
				return "XmlData/"+draws+fileName;
			}
			//return AppConfigUtil.get("PICURL")+draws+fileName;
			return draws+fileName;
		}
		return null;
	}
	/**
	 * 获取说明书的路径前缀
	 * @return
	 * 以公开号为CN102845169A的专利为例:此专利所在发明专利库-fm,公告日为2012.01.02,申请号为CN201210362158.4,file的值如上instrTif内容，
	 * http://pic.cnipr.com:8080/XmlData/+库名简称+/+专利公开日(以yyyymmdd格式)+/+专利申请号（去掉前头的国别代码）+/真实的file值
	 * 文件后缀不是.tif，而是.gif图片
	 * 以/结尾
	 * 外观专利没有说明书附图
	 */
	public static String getSpecificationPicPrefixPath(String dbName,String pubDate,String appNumber){
		if(StringUtils.isNotEmpty(dbName)&&!dbName.equalsIgnoreCase("wgzl")&&StringUtils.isNotEmpty(pubDate)&&StringUtils.isNotEmpty(appNumber)){
			StringBuffer path=new StringBuffer();
			//path.append(AppConfigUtil.get("PICURL"));
			path.append("XmlData").append("/");
			path.append(getDbName(dbName)).append("/");
			path.append(pubDate.replaceAll("\\.", "")).append("/");
			Pattern p=Pattern.compile("^[A-Z]{2}.+");
			Matcher m=p.matcher(appNumber);
			if(m.matches()){
				path.append(appNumber.substring(2)).append("/");//去掉开头的英文字母
			}
			else{
				path.append(appNumber).append("/");
			}
			return path.toString();
		}
		return null;
	}
	/**
	 * 获取图片名集合
	 * @param drawsXml
	 * @return
	 */
	@Deprecated
	public static List<String> getSpecificationPics(String drawsXml){
		if(StringUtils.isNotEmpty(drawsXml)){
			List<String> fileNames=parseXml2PatentInterfacePicName(drawsXml);
			List<String> files=new ArrayList<String>();
			if(null!=fileNames&&fileNames.size()>0){
				for(String file:fileNames){
					files.add(file.replace("TIF", "gif"));
				}
			}
			return files;
		}
		return null;
	}
	/**
	 * 获取图片的库名
	 * @param dbName
	 * @return
	 */
	private static String getDbName(String dbName){
		if(dbName.equalsIgnoreCase("fmzl"))
			return "fm";
		else if(dbName.equalsIgnoreCase("syxx"))
			return "xx";
		else {
			return "sq";
		}
	}
	/**
	 * 从字符串中解析出图片名称
	 * @param drawsXml
	 * @return
	 */
	@Deprecated
	private static List<String> parseXml2PatentInterfacePicName(String drawsXml){
		List<String> fileNames=new ArrayList<String>();
		Pattern p=Pattern.compile("file=\"(.*?)\"");
		Matcher m=p.matcher(drawsXml);
		String file="";
		while(m.find()){
			file=m.group();
			fileNames.add(file.substring(6,file.length()-1));
		}
		return fileNames;
	}
	/**
	 * 获取文档说明书图片的路径
	 * @return
	 */
	public static List<String> getSpecificationPics(String distributePath,int pages,String dbName){
		if(StringUtils.isNotEmpty(distributePath)&&pages>0){
			//String clpPath=AppConfigUtil.get("PICURL")+"ThumbnailImage"+distributePath.substring(5)+"/";
			//String picPath=AppConfigUtil.get("PICURL")+distributePath+"/";
			String clpPath="ThumbnailImage"+distributePath.substring(5)+"/";
			String picPath=distributePath+"/";
			List<String> fileNames=new ArrayList<String>();
			for(int i=1;i<=pages;i++){
				if(dbName.equalsIgnoreCase("wgzl")){
					fileNames.add(clpPath+String.format("%06d", i)+".jpg");//缩略图路径
					fileNames.add(picPath+String.format("%06d", i)+".jpg");
				}
				else{
					fileNames.add(clpPath+String.format("%06d", i)+".gif");
					fileNames.add(picPath+String.format("%06d", i)+".tif");
					//fileNames.add(picPath+String.format("%06d", i)+".png");
				}
			}
			return fileNames;
		}
		return null;
	}
}
