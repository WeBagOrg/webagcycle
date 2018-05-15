package com.hotent.platform.ip.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hotent.core.util.AppUtil;
import com.hotent.platform.dao.system.DictionaryDao;
import com.hotent.platform.model.system.Dictionary;

public class FormatFromDictionaryUtil {
	/**
	 * 格式化对象属性
	 * @param modelClass 模型对象的Class
	 * @param obj 具体的对象
	 * @param dicType 查询的数据字典的Type
	 * @param methodStr 转换的属性名称（转成get和set方法）
	 */
	public static void parseObject(Class<?> modelClass,Object obj,String nodeKey,String field){
		List<Dictionary> dictionarys=getDictionaryByNodeKey(nodeKey);
		setFieldFromDictionary(modelClass,dictionarys,field,obj);
	}
	/**
	 * 解析一个对象中的多个值
	 * @param modelClass 模型对象的Class
	 * @param object 具体的对象实例
	 * @param nodeKeyMap map的key为nodeKey,value为对象的filed
	 */
	public static void parseObject(Class<?> modelClass,Object object,Map<String,String> nodeKeyMap){
		if(nodeKeyMap!=null&&nodeKeyMap.size()>0){
			for(String key:nodeKeyMap.keySet())
				parseObject(modelClass,object,key,nodeKeyMap.get(key));
		}
	}
	/**
	 * 解析list中的对象的属性
	 * @param modelClass list中的对象的Class
	 * @param list 放置对象的集合
	 * @param nodeKey 数据字典的nodeKey
	 * @param filed 集合中对象的属性
	 */
	public static void parseList(Class<?> modelClass,List<Object> list,String nodeKey,String field){
		List<Dictionary> dictionarys=getDictionaryByNodeKey(nodeKey);
		if(null!=list&&list.size()>0){
			for(Object obj:list){
				setFieldFromDictionary(modelClass,dictionarys,field,obj);
			}
		}
	}
	/**
	 * 解析list中对象的属性，并格式化数据字典的字段
	 * @param modelClass List中的对象的Class
	 * @param list 放置对象的集合
	 * @param nodeKeyMap 数据字典的nodeKey和对象属性的map对象
	 */
	public static void parseList(Class<?> modelClass,List<Object> list,Map<String,String> nodeKeyMap){
		if(nodeKeyMap!=null&&nodeKeyMap.size()>0){
			for(String key:nodeKeyMap.keySet())
				parseList(modelClass,list,key,nodeKeyMap.get(key));
		}
	}
	
	/**
	 * 查询数据字典，获取数据字典对应的值
	 * @param nodeKey
	 * @return
	 */
	private static List<Dictionary> getDictionaryByNodeKey(String nodeKey){
		DictionaryDao dictionaryDao=(DictionaryDao) AppUtil.getBean(DictionaryDao.class);//查询数据字典的dao
		//查询出数据字典的值
		Map<String, String> queryMap=new HashMap<String, String>();
		queryMap.put("catKey", "DIC");
		queryMap.put("nodeKey",nodeKey.toUpperCase());
		return dictionaryDao.getDictionaryByNodeKey(queryMap);
	}
	/**
	 * 将字典名封装到对象中
	 * @param modelClass
	 * @param dictionarys
	 * @param field
	 * @param obj
	 */
	private static void setFieldFromDictionary(Class<?> modelClass,List<Dictionary> dictionarys,String field,Object obj){
		if(dictionarys!=null&&dictionarys.size()>0){
			try {
				String getMethodStr="get"+field.substring(0,1).toUpperCase()+field.substring(1);
				String setMethodStr="set"+field.substring(0,1).toUpperCase()+field.substring(1);
				Method getMethod=modelClass.getMethod(getMethodStr);
				Method setMethod=modelClass.getMethod(setMethodStr, new Class[]{String.class});
				for(Dictionary dictionary:dictionarys){
					String value=(String) getMethod.invoke(obj, null);
					if(StringUtils.isNotEmpty(value)&&value.equalsIgnoreCase(dictionary.getItemValue())){
						setMethod.invoke(obj, dictionary.getItemName());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}
