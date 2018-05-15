package com.haiya.produce.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.jaxen.dom4j.Dom4jXPath;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hotent.core.engine.IScript;
import com.hotent.core.exception.BusDataException;
import com.hotent.core.util.Dom4jUtil;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.service.bpm.WebserviceHelper;
import com.hotent.platform.service.form.BpmFormHandlerService;

/**
 *<pre>
 * 对象功能:海雅专用Script
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ouxb
 * 创建时间:2015-6-8 14:30:51
 *</pre>
 */
@Component 
public class HaiyaScript implements IScript {
	@Resource
	BpmFormHandlerService formHandlerService;
	
	/**
	 * 商友接口数据对接公共处理方法
	 * @return
	 * @throws Exception 
	 */
	public String webserviceScript(String alias,Map<String,Object> map) throws Exception{

		String xml = WebserviceHelper.executeXml(alias, map);
		Document doc= Dom4jUtil.loadXml(xml);
		Dom4jXPath path = new Dom4jXPath("//ns:CYDataPorcessResult");
		path.addNamespace("ns", "http://tempuri.org/");
		List<Node> nodes = path.selectNodes(doc);
		String resultStr = nodes.get(0).getText();
		JSONObject resultJson = JSONObject.parseObject(resultStr);
		
		String resultType = resultJson.getString("ResType");
		String message = resultJson.getString("Message");
		if("-1".equals(resultType)) {
			throw new BusDataException(message);
		}
		
		return message;
	}
	
	/**测试传输 **/
	public String TransNormalDataToWebService(int operation,String json) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		String jsonData = JsonDataTranslate.translateNormalJsonData(json,operation);
		
		params.put("operation",operation);
		params.put("jsonstring",jsonData);
		
		return webserviceScript("xiaoWei",params);
	}
	
	/**
	 * 获取流程业务数据转换并传输
	 * @param operation
	 * @param businessKey
	 * @throws Exception
	 */

	public void getFormDataToSend(int operation,String id,long tableId) throws Exception{
		try {
			String fromDataJson = formHandlerService.getFormDataJson(id, tableId);
			String jsonData = JsonDataTranslate.translateNormalJsonData(fromDataJson,operation);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("operation",operation);
			params.put("jsonstring",jsonData);
			
			webserviceScript("ShangYouWebService",params);
		} catch (Exception e) {
			throw new BusDataException("业务数据传输异常："+ e.getMessage());
		}
	}
	
	/**
	 * 获取流程业务数据转换并传输
	 * @param operation
	 * @param businessKey
	 * @throws Exception
	 */

	public void getFlowDataToSend(int operation,String businessKey) throws Exception{
		try {
			String fromDataJson = formHandlerService.getBpmFormDataJson(null, businessKey, null);
			String jsonData = JsonDataTranslate.translateNormalJsonData(fromDataJson,operation);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("operation",operation);
			params.put("jsonstring",jsonData);
			
			webserviceScript("ShangYouWebService",params);
		} catch (Exception e) {
			throw new BusDataException("业务数据传输异常："+ e.getMessage());
		}
	}
	/**
	 * 业务表单数据，直接从表单获取的json数据传输，比如基础数据维护
	 * @param operation
	 * @param businessKey
	 * @throws Exception
	 */
	public void transFormDataToSend(int operation,String json) throws Exception{
		try {
			String jsonData = JsonDataTranslate.handleFormDataToSend(json,operation);
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("operation",operation);
			params.put("jsonstring",jsonData);
			
			webserviceScript("ShangYouWebService",params);
		} catch (Exception e) {
			throw new BusDataException("业务数据传输异常："+ e.getMessage());
		}
	}
	
	
}
