package com.hotent.platform.ip.util;

import java.util.List;

import org.dom4j.DocumentException;

import com.hotent.platform.model.ip.HighchartsModel;
import com.hotent.platform.model.ip.IpPatentAnalysis;

import net.sf.json.JSONObject;

/**
 * 构造highchart显示报表的数据
 * 使用的highchart版本为：Highcharts-3.0.10
 * @author ipph
 *
 */
public class HighchartsUtil {
	/**
	 * 查看API可以直接使用chart.series=[]数组
	 * xAxis构造横轴的坐标显示,chart.xAxis[0].setCategories
	 * @param models
	 * @return
	 * @throws DocumentException 
	 */
	public JSONObject getHighchartsData(List<IpPatentAnalysis> models,String highchartsDataType,boolean isTrend,List<Object> trendData) {
		JSONObject jsonObject=null;
		if(HighchartsModel.HIGHCHARTSLINE.equals(highchartsDataType)){//折线图
			jsonObject=getHighchartsLineData(models,isTrend,trendData);
		}
		else if(HighchartsModel.HIGHCHARTSCOLUMN.equals(highchartsDataType)){//柱状图
			jsonObject=getHighchartsColumnData(models);
		}
		else {//饼状图
			jsonObject=getHighchartsPieData(models);
		}
		return jsonObject;
	}
	/**
	 * 折线图
	 * @param models
	 * @param highchartsDataType
	 * @param filed
	 * @return
	 */
	private JSONObject getHighchartsLineData(List<IpPatentAnalysis> models,boolean isTrend,List<Object> trendData){
		HighchartsModel highchartsModel=new HighchartsModel();
		HighchartsDataParse dataParse=new HighchartsDataParse();
		dataParse.getHighchartsLineData(models,highchartsModel,isTrend,trendData);
		return JSONObject.fromObject(highchartsModel);
	}
	/**
	 * 获取柱状图模型统计数据
	 * @param models
	 * @param filed 统计的字段
	 * @return
	 */
	private  JSONObject getHighchartsColumnData(List<IpPatentAnalysis> models){
		HighchartsModel highchartsModel=new HighchartsModel();
		HighchartsDataParse dataParse=new HighchartsDataParse();
		dataParse.getHighchartsColumnData(models,highchartsModel);
		return JSONObject.fromObject(highchartsModel);
	}
	/**
	 * 获取饼状图模型统计数据 drilldown
	 * @param models
	 * @return
	 */
	private  JSONObject getHighchartsPieData(List<IpPatentAnalysis> models){
		HighchartsModel highchartsModel=new HighchartsModel();
		HighchartsDataParse dataParse=new HighchartsDataParse();
		dataParse.getHighchartsPieData(models,highchartsModel);
		return JSONObject.fromObject(highchartsModel);
	}
}
