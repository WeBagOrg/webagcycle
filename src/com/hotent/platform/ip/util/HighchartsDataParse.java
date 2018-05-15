package com.hotent.platform.ip.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hotent.platform.model.ip.HighchartsModel;
import com.hotent.platform.model.ip.IpPatentAnalysis;
/**
 * 折线图只提供一种基本模式的图像
 * 柱状图提供两种模式图：基本的和stacked,两种数据的封装形式相同，只是页面js不同
 * 饼状图提供两种：基本的和drilldown
 * @author Administrator
 *
 */
public class HighchartsDataParse {
	/**
	 * 获取折线图数据
	 * @param models
	 * @param highchartsModel
	 * 基本折线图：xAxis: {categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']}
	 * series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]	
	 * 
	 * 
	 */
	public  void getHighchartsLineData(List<IpPatentAnalysis> models,HighchartsModel highchartsModel,boolean isTrend,List<Object> trendData){
		Set<String> seriesNames=getHighchartsSeriesNameSet(models,HighchartsModel.HIGHCHARTSLINE);
		List<Map<String,Object>> series=new ArrayList<Map<String,Object>>();
		String[] xAxis=getHighchartsxAxis(models,isTrend,trendData);//横坐标，数据是按照横坐标对应放置的
		highchartsModel.setxAxis(xAxis);
		for(String name:seriesNames){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", name);
			int[] seriesData=getLineData(models,name,xAxis);
			map.put("data", seriesData);
			series.add(map);
		}
		highchartsModel.setSeries(series);
	}
	/**
	 * 获取柱状图数据
	 * @param models
	 * @param highchartsModel
	 * 基本柱状图：xAxis: {categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']}
	 * series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]	
	 * 
	 */
	public void getHighchartsColumnData(List<IpPatentAnalysis> models,HighchartsModel highchartsModel){
		Set<String> set=getHighchartsSeriesNameSet(models,HighchartsModel.HIGHCHARTSCOLUMN);
		List<Map<String,Object>> series=new ArrayList<Map<String,Object>>();
		String[] xAxis=getHighchartsxAxis(models,false,null);
		highchartsModel.setxAxis(xAxis);
		for(String name:set){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", name);
			int[] data=getLineData(models,name,xAxis);
			map.put("data", data);
			series.add(map);
		}
		highchartsModel.setSeries(series);
	}
	/**
	 * 获取饼状图数据
	 * @param models
	 * @param highchartsModel
	 */
	public void getHighchartsPieData(List<IpPatentAnalysis> models,HighchartsModel highchartsModel){
		Map<String,Object> series=getHighchartsPieDataBasic(models);
		highchartsModel.setPieSeries(series);
	}
	/**
	 * 获取饼状图数据
	 * 基本的饼状图：注意，饼状图是没有横坐标的,只需要数据的xAxis和seriesData
	 * 其中data的name值为原来的xAxis
	 * series: [{type: 'pie',
            name: 'Browser share',
            data: [ ['Firefox',   45.0],['IE',       26.8],{ name: 'Chrome', y: 12.8, sliced: true,  selected: true},['Safari',    8.5], ['Opera',     6.2], ['Others',   0.7]
            ]
        }]
        { name: 'Chrome', y: 12.8, sliced: true,  selected: true}表示选择该饼状图的该区域，sliced表示分离，y是数据量
	 * @param models
	 */
	private Map<String,Object> getHighchartsPieDataBasic(List<IpPatentAnalysis> models){
		Map<String,Object> seriesMap=new HashMap<String,Object>();
		//将输入封装成data返回
		List<Object[]> data=new ArrayList<Object[]>();
		Set<String> seriesNames=getHighchartsSeriesNameSet(models,HighchartsModel.HIGHCHARTSPIE);
		if(null!=seriesNames&&seriesNames.size()>0){
			for(String name:seriesNames){
				int number=0;
				for(int i=0;i<models.size();i++){
					IpPatentAnalysis ipPatentAnalysis=models.get(i);
					if(StringUtils.isNotEmpty(ipPatentAnalysis.getxAxis())&&name.equals(ipPatentAnalysis.getxAxis())){
						number+=ipPatentAnalysis.getSeriesData();
					}
				}
				if(number>0){
					Object[] temp=new Object[2];
					//temp[0]=(String) c.getMethod("getSeriesName", null).invoke(ipPatentAnalysis, null);
					temp[0]=name;
					temp[1]=number;
					data.add(temp);
				}
			}
		}
		seriesMap.put("type", "pie");
		seriesMap.put("data", data);
		return seriesMap;
	}
	/**
	 * 饼状图数据解析
	 * 
	 * @param models
	 * @param highchartsModel
	 */
	public void getHighchartsPieDataDrilldown(List<IpPatentAnalysis> models,HighchartsModel highchartsModel){
		//需要封装两个data数据，name指定发明类型，y指定数量，drilldown指定下级名称，也使用发明类型
		/*data: [{'name':'Microsoft Internet Explorer','y':53.61,'drilldown':'Microsoft Internet Explorer'},
		       {'name':'Chrome','y':18.73,'drilldown':'Chrome'},
		       {'name':'Firefox','y':19.899999999999995,'drilldown':'Firefox'},
		       {'name':'Safari','y':4.64,'drilldown':'Safari'},{'name':'Opera','y':0.29,'drilldown':'Opera'}]*/
		Set<String> set=getHighchartsSeriesNameSet(models,HighchartsModel.HIGHCHARTSPIE);
		List<Map<String,Object>> series=new ArrayList<Map<String,Object>>();
		if(null!=set&&set.size()>0){
			for(String name:set){
				Map<String,Object> map=new HashMap<String,Object>();
				int number=0;
				for(int i=0;i<models.size();i++){
					IpPatentAnalysis ipPatentAnalysis=models.get(i);
					if(StringUtils.isNotEmpty(ipPatentAnalysis.getxAxis())&&name.equals(ipPatentAnalysis.getxAxis())){
						number+=ipPatentAnalysis.getSeriesData();
					}
				}
				if(number>0){
					map.put("name", name);
					map.put("drilldown", name);
					map.put("y",number);
					series.add(map);
				}
			}
		}
		highchartsModel.setSeries(series);
		highchartsModel.setDrilldownSeries(getHighchartsPieDataDetail(models));
	}
	
	/**
	 * 解析饼状图的报表数据
	 * @param models
	 * @return
	 */
	public  List<Map<String,Object>> getHighchartsPieDataDetail(List<IpPatentAnalysis> models){
		/*series: [{'name':'Microsoft Internet Explorer','id':'Microsoft Internet Explorer',
			'data':[['v8.0',26.61],['v9.0',16.96],['v6.0',6.4]]},
			{'name':'Chrome','id':'Chrome','data':[['v18.0',8.01],['v19.0',7.73],['v17.0',1.13]]},
			{'name':'Firefox','id':'Firefox','data':[['v18.0',8.0],['v19.0',7.73],['v17.0',1.13]]},
			{'name':'Safari','id':'Safari','data':[['v18.0',8.01],['v19.0',7.73],['v17.0',1.13]]},
			{'name':'Opera','id':'Opera','data':[['v18.0',8.01],['v19.0',7.73],['v17.0',1.13]]}]*/
		Set<String> set=getHighchartsSeriesNameSet(models,HighchartsModel.HIGHCHARTSPIE);
		List<Map<String,Object>> drilldownSeries=new ArrayList<Map<String,Object>>();
		try {
			//Class<?> c=IpPatentAnalysis.class;
			if(null!=set&&set.size()>0){
				for(String name:set){
					Map<String,Object> map=new HashMap<String,Object>();
					List<Object[]> data=new ArrayList<Object[]>();
					for(int i=0;i<models.size();i++){
						IpPatentAnalysis ipPatentAnalysis=models.get(i);
						if(name.equals(ipPatentAnalysis.getxAxis())){
							Object[] temp=new Object[2];
							//temp[0]=(String) c.getMethod("getSeriesName", null).invoke(ipPatentAnalysis, null);
							temp[0]=ipPatentAnalysis.getSeriesName();
							temp[1]=ipPatentAnalysis.getSeriesData();
							data.add(temp);
						}
					}
					if(data.size()>0){
						map.put("name", name);
						map.put("id", name);
						map.put("data",data);
						drilldownSeries.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drilldownSeries;
	}
	
	/**
	 * 解析折线图数据
	 * @param models
	 * @param name
	 * @return
	 */
	public int[] getLineData(List<IpPatentAnalysis> models,String name,String[] xAxis){
		int[] data=new int[xAxis.length];
		//String flag="";
		for(int i=0;i<models.size();i++){
			IpPatentAnalysis ipPatentAnalysis=models.get(i);
			//String temp=ipPatentAnalysis.getxAxis();
			/*if(!flag.equals(temp)){
				flag=temp;
				j++;
			}*/
			for(int j=0;j<xAxis.length;j++){
				if(StringUtils.isNotEmpty(ipPatentAnalysis.getSeriesName())&&ipPatentAnalysis.getSeriesName().equals(name)&&ipPatentAnalysis.getxAxis()!=null&&ipPatentAnalysis.getxAxis().equals(xAxis[j])){
					data[j]=ipPatentAnalysis.getSeriesData();
					break;
				}
			}
			
		}
		return data;
	}
	/**
	 * 获取统计的name字段，如发明类型中的发明、实用新型、外观设计
	 * @param models
	 * @return
	 */
	public Set<String> getHighchartsSeriesNameSet(List<IpPatentAnalysis> models,String type){
		Set<String> set=new HashSet<String>();//存放专利统计名称
		if(HighchartsModel.HIGHCHARTSPIE.equals(type)){
			for(int i=0;i<models.size();i++){
				IpPatentAnalysis ipPatentAnalysis=models.get(i);
				if(null!=ipPatentAnalysis.getxAxis())
					set.add(ipPatentAnalysis.getxAxis());
			}
		}
		else{
			for(int i=0;i<models.size();i++){
				IpPatentAnalysis ipPatentAnalysis=models.get(i);
				if(null!=ipPatentAnalysis.getSeriesName())
					set.add(ipPatentAnalysis.getSeriesName());
			}
		}
		return set;
	}
	/**
	 * 获取横坐标
	 * xAixs数据解析
	 * @param models
	 * @return
	 * trendData中存放开始统计的值，最大长度，以及后缀4个元素
	 */
	private String[] getHighchartsxAxis(List<IpPatentAnalysis > models,boolean isTrend,List<Object> trendData){
		String[] temp=null;
		if(isTrend&&trendData!=null&&trendData.size()==4){
			temp=new String[Integer.parseInt(trendData.get(1).toString())];
			int startNumber=Integer.parseInt(trendData.get(0).toString());
			boolean flag=(Boolean) trendData.get(3);
			//boolean flag=false;
			String suffix=trendData.get(2).toString();
			if(flag){
				for(int i=0,j=1;i<temp.length;i++,j++){
					temp[i]=j+trendData.get(2).toString();
				}
			}else{
				for(int i=0,j=startNumber;i<temp.length;i++,j++){
					temp[i]=j+trendData.get(2).toString();
				}
			}
			for(IpPatentAnalysis ipPatentAnalysis:models){
				if(StringUtils.isNotEmpty(ipPatentAnalysis.getxAxis()))
					if(flag){
						ipPatentAnalysis.setxAxis(Integer.parseInt(ipPatentAnalysis.getxAxis())-startNumber+1+suffix);
					}
					else{
						ipPatentAnalysis.setxAxis(Integer.parseInt(ipPatentAnalysis.getxAxis())+suffix);
					}
			}
		}
		else{
			List<String> categories=new ArrayList<String>();
			try {
				for(IpPatentAnalysis ipPatentAnalysis:models){
					String xAxis=ipPatentAnalysis.getxAxis();
					if(!categories.contains(xAxis)&&StringUtils.isNotEmpty(xAxis)){
						categories.add(xAxis);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			temp=new String[categories.size()];
			categories.toArray(temp);
			categories.clear();
		}
		return temp;
	}
}
