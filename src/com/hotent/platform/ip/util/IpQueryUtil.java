package com.hotent.platform.ip.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hotent.core.web.query.QueryFilter;

/**
 * 解析查询条件的工具类
 * 1、由于在处理复选框的查询条件时，queryFilter组织查询参数到mybatis查询使用foreach时无法获取数据，因此将数组解析成字符串
 * 2、实现查询文本框的智能解析，如文本框中输入申请号，根据申请号的信息查询，输入日期：则根据申请日或授权日查询，输入名称：则根据专利名称查询
 * @author Administrator
 *
 */
public class IpQueryUtil {
	/**
	 * 
	 * @param queryFilter 查询参数的集合体
	 * @param param 待转换的列表,map的第一个参数指定待转换的参数名称，第二个参数TypeEnum定义出待转换的类型
	 * 由string转换成array是以逗号分隔，而由
	 * @return
	 */
	public static void parseSearchCondition(Map<String,Object> queryMap,Map<String,TypeEnum> param){
		Iterator<String> keys=param.keySet().iterator();
		while(keys.hasNext()){
			String key=keys.next();
			if(queryMap.containsKey(key)){
				/*if(queryMap.get(key) instanceof Object[]){
					switch (param.get(key)) {
						case string:
						
						break;
						case merge:
						break;
						
						case clear:
						
						break;
						default:
						break;
					}
				}*/
				if(param.get(key)==TypeEnum.string&&queryMap.get(key) instanceof Object[]){//转换成string
					if(key.equals("tradeNumber"))//数字需要特殊处理
					{queryMap.put(key,0);}
					else
						{
						 queryMap.put(key, "("+parseArray2String((Object[])queryMap.get(key),null)+")");}
				}				
				else if(param.get(key)==TypeEnum.merge&&queryMap.get(key) instanceof Object[]){//多选情况
					queryMap.put(key, "("+parseArray2String((Object[])queryMap.get(key),"merge")+")");
				}
				else if(queryMap.get(key) instanceof String){
					if(key.equals("tradeNumber")||key.equals("fundWay"))//需要特殊处理的字段
					{queryMap.put(key,queryMap.get(key));}				
					else
					{	queryMap.put(key, "('"+queryMap.get(key)+"')");}
				}
			}
		}
	}
	/**
	 * 将数组转化成sql字符串
	 * @param temp
	 * @return
	 */
	private static String parseArray2String(Object[] temp,String type){
		 String tempStr="";
		 String merge="";
		 for(int i=0;i<temp.length;i++){
			 if(tempStr.equals("")){
				 tempStr= "'" +temp[i]+ "'";
				 if("merge".equals(type)){
					 merge="'"+temp[i];
				 }
			 }
			 else{
				 tempStr=tempStr+ ",'" +temp[i]+ "'";
				 if("merge".equals(type)){
					 if(i==temp.length-1)
						 merge+=","+temp[i]+"'";
					 else
						 merge=","+temp[i];
				 }
			 }
			 
		 }
		 if("merge".equals(type)){
			 tempStr+=","+merge;
		 }
		 return tempStr;
	 }
	/**
	 * 封装查询条件，排序和主键字段
	 * @param primaryKey
	 * @param queryFilter
	 * @return
	 */
	public Map<String,Object> queryPrimaryKeys(List<Object> primaryKey,QueryFilter queryFilter){
		Map<String,Object> map=null;
		if(primaryKey!=null&&primaryKey.size()>0){
			map=new HashMap<String, Object>();
			map.put("orderField", queryFilter.getFilters().get("orderField"));
			map.put("orderSeq", queryFilter.getFilters().get("orderSeq"));
			map.put("ids", primaryKey);
		}
		return map;
	}
}
