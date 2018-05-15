package com.hotent.platform.ip.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TableDataAnalysis {
	/**
	 * 为分析图表下面的表格使用，参数jsonobject为统计分析线图对象
	 * 
	 */
	public boolean getTableDataAnalysis(JSONObject jsonObject, List<String[]> columnList, List<Map<String,String>> tableList,boolean isTotal) {
		boolean result = false;
		try {
			//获得所有列表头
			JSONArray jaSeries = (JSONArray)jsonObject.get("series");
			//获得所有行
			JSONArray jaXAxis = (JSONArray)jsonObject.get("xAxis");
			if (jaSeries != null) {//列标题
				for (int i = 0;i < jaSeries.size(); i++) {
					//列标题内容
					if ((JSONObject)jaSeries.get(i) != null && ((JSONObject)jaSeries.get(i)).get("name") != null) {
						String[] temp=new String[2];
						temp[0]="seriesName"+i;
						temp[1]=((JSONObject)jaSeries.get(i)).get("name").toString();
						columnList.add(temp);
					}
				}
				if(isTotal){
					//1)合计字段
					String[] temp=new String[2];
					temp[0]="seriesName";
					temp[1]=("总量");
					columnList.add(temp);
				}
			}
			//table内容
			int[] sumColumn=new int[jaSeries.size()];
			if (jaXAxis != null) {//行数据
				for (int i = 0; i < jaXAxis.size(); i++) {
					//行内容
					Map<String, String> rowMap = new HashMap<String, String>();
					//第一列的内容,一般为标题
					String rowName = "";
					if (jaXAxis.get(i) != null) {
						rowName = jaXAxis.get(i).toString();
					}
					rowMap.put("title", rowName);
					
					//获得真正的内容
					int sum=0;//2)合计
					for (int j = 0; j < jaSeries.size() && j < columnList.size(); j++) {
						JSONArray ja = (JSONArray)((JSONObject)jaSeries.get(j)).get("data");
						if (ja.size() > i && ja.get(i) != null) {
							rowMap.put(columnList.get(j)[0], ja.get(i).toString());
							//3)合计
							sumColumn[j]+=Integer.parseInt(ja.get(i).toString());
							sum+=Integer.parseInt(ja.get(i).toString());
						} else {
							rowMap.put(columnList.get(j)[0], "0");
						}
					}
					if(isTotal){
						//4)合计
						rowMap.put(columnList.get(columnList.size()-1)[0], sum+"");
					}
					tableList.add(rowMap);
				}
				if(isTotal){
					//5)行位置的合计
					Map<String, String> rowMap = new HashMap<String, String>();
					rowMap.put("title", "合计");
					for(int i=0;i<sumColumn.length;i++){
						rowMap.put(columnList.get(i)[0], sumColumn[i]+"");
					}
					rowMap.put(columnList.get(columnList.size()-1)[0],"");
					tableList.add(rowMap);
				}
			}
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 为分析图表下面的表格使用，参数jsonobject为统计分析线图对象
	 * 
	 */
	public boolean getTableDataAnalysisReverse(JSONObject jsonObject, List<String[]> columnList, List<Map<String,String>> tableList,boolean isTotal) {
		boolean result = false;
		try {
			//获得所有行
			JSONArray jaSeries = (JSONArray)jsonObject.get("series");
			//获得所有标题
			JSONArray jaXAxis = (JSONArray)jsonObject.get("xAxis");
			if (jaXAxis != null) {
				for (int i = 0;i < jaXAxis.size(); i++) {
					//列标题内容
					if (jaXAxis.get(i) != null) {
						String[] temp=new String[2];
						temp[0]="xAxis"+i;
						temp[1]=(jaXAxis.get(i).toString());
						columnList.add(temp);
					}
				}
				//合计字段
				if(isTotal){
					String[] temp=new String[2];
					temp[0]="xAxis";
					temp[1]=("总量");
					columnList.add(temp);
				}
			}
			int[] sumColumn=new int[jaXAxis.size()];
			//table内容
			if (jaSeries != null) {//行
				for (int i = 0; i < jaSeries.size(); i++) {
					//行内容
					Map<String, String> rowMap = new HashMap<String, String>();
					//第一列的内容,一般为标题
					String rowName = "";
					if (jaSeries.get(i) != null&& ((JSONObject)jaSeries.get(i)).get("name") != null) {
						rowName = ((JSONObject)jaSeries.get(i)).get("name").toString();
					}
					rowMap.put("title", rowName);
					
					//获得真正的内容
					int sum=0;
					for (int j = 0; j < jaXAxis.size() && j < columnList.size(); j++) {//循环列，为每一列赋值
						JSONArray ja = (JSONArray)((JSONObject)jaSeries.get(i)).get("data");
						if (ja.size() > j && ja.get(j) != null) {
							rowMap.put(columnList.get(j)[0], ja.get(j).toString());
							sumColumn[j]+=Integer.parseInt(ja.get(j).toString());
							sum+=Integer.parseInt(ja.get(j).toString());
						} else {
							rowMap.put(columnList.get(j)[0], "0");
						}
					}
					//合计值
					if(isTotal){
						rowMap.put(columnList.get(columnList.size()-1)[0], sum+"");
					}
					tableList.add(rowMap);
				}
				if(isTotal){
					//行位置的合计
					Map<String, String> rowMap = new HashMap<String, String>();
					rowMap.put("title", "合计");
					for(int i=0;i<sumColumn.length;i++){
						rowMap.put(columnList.get(i)[0], sumColumn[i]+"");
					}
					rowMap.put(columnList.get(columnList.size()-1)[0],"");
					tableList.add(rowMap);
				}
			}
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
