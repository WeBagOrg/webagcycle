package com.haiya.produce.script;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hotent.core.engine.IScript;
import com.hotent.core.util.BeanUtils;

/**
 * 
 * @author miao
 * 数据转换格式，处理特殊字段
 */
public class JsonDataTranslate{
	
	/**处理业务数据*/
	public static String translateNormalJsonData(String jsonData,int operation){
		JSONObject json = handelJson(jsonData);
		switch (operation) {
			// 某表特殊字段转子表处理，添加合同号
			case 100000001:
				me_zltksbbJsonHandle(json);
			//	HTH_Handle(json);
				break;
			
			case 1000001:
				
				break;
		}
		
		return json.toString();
	}
	//处理合同号
	public static void HTH_Handle(JSONObject jsonData){
		
	}

	//某表处理
	public static void me_zltksbbJsonHandle(JSONObject jsonData){
		
	}
	
	
	
	
	
	
	
	/** 处理表单里面的json数据*/
	public static String handleFormDataToSend(String json,int operation){
		JSONObject formDataJson = JSONObject.parseObject(json);
		JSONObject reaultJson = formDataTranslator(formDataJson);
		// 处理特殊表单
		switch (operation) {
		case 100000001:
			break;
		case 1000001:
			break;
		}
		
		return reaultJson.toJSONString();
	}
	
	
	
	
	
	
	
	
	
	
	
	/**公共统一处理json格式*/
	private static JSONObject handelJson(String jsonDataStr){
		JSONObject originalJsonData = JSONObject.parseObject(jsonDataStr);
		JSONObject jsonData = originalJsonData.getJSONObject("main");
		//测试模式
		if(jsonData.containsKey("fields"))  { return formDataTranslator(originalJsonData); }
		
		JSONObject subTables = originalJsonData.getJSONObject("sub");
		if(subTables != null)
		for (String subTableName : subTables.keySet()) {
			JSONObject subTable = subTables.getJSONObject(subTableName);
			JSONArray subTableFields = handelSubTableFields(subTable.getJSONArray("dataList"));
			jsonData.put(subTableName, subTableFields);
		}
		return jsonData;
	}
	
	
	/**表单json 转换*/
	private static JSONObject formDataTranslator(JSONObject json) {
		JSONObject jsonData = json.getJSONObject("main").getJSONObject("fields");

		JSONArray subTables = json.getJSONArray("sub");
		for (int i = 0; i < subTables.size(); i++) {
			JSONObject subTable = subTables.getJSONObject(i);
			String subTableName = subTable.getString("tableName");
			JSONArray subTableFields = handelSubTableFields(subTable.getJSONArray("fields"));
			jsonData.put(subTableName, subTableFields);
		}
		return jsonData;
	}
	/**添加上序号*/
	private static JSONArray handelSubTableFields(JSONArray subTableFields){
		for (int i = 0; i < subTableFields.size(); i++) {
			JSONObject	subTableField = subTableFields.getJSONObject(i);
			if(subTableField.containsKey("xh")){
				subTableField.put("idx",subTableField.get("xh"));
			}else{
				subTableField.put("idx", i+1+"");
			}
		}
		return subTableFields;
	}

}
