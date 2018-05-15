package com.hotent.platform.ip.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author ipph
 *
 */
public class AttachmentUtil {
	/**
	 * 解析文件上传域的字符串，构造出文件上传表sys_file表的主键集合
	 * @param fileIds
	 * @param attachment
	 */
	public static void parseAttachment(List<Long> fileIds,String attachment){
		//对attachment进行格式化，如果有两个'则取得一个
		Long[] fileIdArray=parseAttachment(attachment);
		if(null!=fileIdArray&&fileIdArray.length>0){
			for(int i=0;i<fileIdArray.length;i++){
				fileIds.add(fileIdArray[i]);
			}
		}
	}
	/**
	 * 获取FileId数组
	 * @param fileIds
	 * @param attachment
	 * @return
	 */
	public static Long[] parseAttachment(String attachment){
		//对attachment进行格式化，如果有两个'则取得一个
		Long[] fileIds=null;
		JSONArray array=getJSONArrayFromAttachment(attachment);
		if(array!=null){
			if(attachment.startsWith("[")){
				if(attachment.indexOf("fileId")!=-1){
					fileIds=getFileIds(array,"fileId");
					return fileIds;
				}
			}
			if(attachment.indexOf("attachs")==-1){
				fileIds=getFileIds(array,"id");
			}
			else{
				fileIds=getFileIds(array,"fileId");
			}
		}
		return fileIds;
	}
	/**
	 * 从attachment中解析出JSONArray对象
	 * @param attachment
	 * @return
	 */
	private static JSONArray getJSONArrayFromAttachment(String attachment){
		String tempAttachment=attachment.replaceAll("''", "'");
		JSONArray array=null;
		if(tempAttachment.startsWith("[")){//构造新的json对象
			//tempAttachment="{attachs:"+tempAttachment+"}";
			array=JSONArray.fromObject(attachment);
			return array;
		}
		if(attachment.indexOf("attachs")==-1){
			array=JSONArray.fromObject(tempAttachment);
		}
		else{
			JSONObject jsonObject=JSONObject.fromObject(tempAttachment);
			if(null!=jsonObject){
				array=jsonObject.getJSONArray("attachs");
			}
		}
		
		return array;
	}
	/**
	 * 获取json数组的fileId数组
	 * @param array
	 * @param field
	 * @return
	 */
	private static Long[] getFileIds(JSONArray array,String field){
		Long[] fileIds=new Long[array.size()];
		for(int i=0;i<array.size();i++){
			JSONObject object=array.getJSONObject(i);
			String fileId=object.getString(field);
			if(StringUtils.isNotEmpty(fileId))
				fileIds[i]=Long.parseLong(fileId);
		}
		return fileIds;
	}
	/**
	 * 判断是否没有上传任何文件
	 * @return
	 */
	public static boolean isFileEmpty(String attachment){
		Long[] fileIds=parseAttachment(attachment);
		if(null==fileIds||fileIds.length==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 转换流程表附件格式至管理表附件格式
	 *
	 */
	public static String checkAttachment(String oldAttachment) {
		String attachment = "";
		if (!oldAttachment.isEmpty()) {
			attachment = oldAttachment.replaceAll("\"id\"", "\'fileId\'").replaceAll("\"name\"", "\'fileName\'").replace('\"', '\'');
		}
		attachment = "{'attachs':" + attachment + "}";
		return attachment;
	}
	//判断是否是html上传控件
	public static boolean isHtmlUploader(String attachment){
		boolean flag=true;
		if(attachment.indexOf("fileName")!=-1){
			flag=false;
		}
		return flag;
	}
}
