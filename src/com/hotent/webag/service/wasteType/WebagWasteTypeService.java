package com.hotent.webag.service.wasteType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.GenericService;
import com.hotent.core.util.BeanUtils;
import com.hotent.webag.model.wasteType.WebagWasteType;
import com.hotent.webag.dao.wasteType.WebagWasteTypeDao;
import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;


import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.core.service.WfBaseService;
import com.hotent.core.annotion.WorkFlow;
import com.hotent.platform.service.bpm.util.BpmAspectUtil;
import com.hotent.core.bpm.BpmResult;


@Service
public class WebagWasteTypeService extends WfBaseService<WebagWasteType>
{
	@Resource
	private WebagWasteTypeDao dao;
	
	@Resource
	private ProcessRunService processRunService;
	public WebagWasteTypeService()
	{
	}
	
	@Override
	protected IEntityDao<WebagWasteType,Long> getEntityDao() 
	{
		return dao;
	}

	/**
	 * 重写getAll方法绑定流程runId
	 */
	public List<WebagWasteType> getAllApp(){
		List<WebagWasteType> webagWasteTypeList=super.getAll();
		return webagWasteTypeList;
	}

	/**
	 * 重写getAll方法绑定流程runId
	 * @param queryFilter
	 */
	public List<WebagWasteType> getAll(QueryFilter queryFilter){
		List<WebagWasteType> webagWasteTypeList=super.getAll(queryFilter);
		List<WebagWasteType> webagWasteTypes=new ArrayList<WebagWasteType>();
		for(WebagWasteType webagWasteType:webagWasteTypeList){
			ProcessRun processRun=processRunService.getByBusinessKey(webagWasteType.getId().toString());
			if(BeanUtils.isNotEmpty(processRun)){
				webagWasteType.setRunId(processRun.getRunId());
			}
			webagWasteTypes.add(webagWasteType);
		}
		return webagWasteTypes;
	}
	

		
	
	/**
	 * 流程处理器方法 用于处理业务数据
	 * @param cmd
	 * @throws Exception
	 */
	public void processHandler(ProcessCmd cmd)throws Exception{
		Map data=cmd.getFormDataMap();
		if(BeanUtils.isNotEmpty(data)){
			String json=data.get("json").toString();
			WebagWasteType webagWasteType=getWebagWasteType(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagWasteType.setId(genId);
				this.add(webagWasteType);
			}else{
				webagWasteType.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagWasteType);
			}
			cmd.setBusinessKey(webagWasteType.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagWasteType对象
	 * @param json
	 * @return
	 */
	public WebagWasteType getWebagWasteType(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagWasteType webagWasteType = (WebagWasteType)JSONObject.toBean(obj, WebagWasteType.class);
		return webagWasteType;
	}
	/**
	 * 保存 webag_waste_type 信息
	 * @param webagWasteType
	 */

	@WorkFlow(flowKey="undefined",tableName="webag_waste_type")
	public void save(WebagWasteType webagWasteType) throws Exception{
		Long id=webagWasteType.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagWasteType.setId(id);
		    this.add(webagWasteType);
		}
		else{
		   WebagWasteType webagWasteTypeTemd = getById(webagWasteType.getId());
		   if(webagWasteTypeTemd==null || webagWasteTypeTemd.equals("")){
		       this.add(webagWasteType);
		   }else{
		       this.update(webagWasteType);
		   }
		}
		BpmResult result=new BpmResult();
		//添加流程变量
//		result.addVariable("", "");
		result.setBusinessKey(id.toString());
		BpmAspectUtil.setBpmResult(result);
	}
}
