package com.hotent.webag.service.weighingLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.GenericService;
import com.hotent.core.util.BeanUtils;
import com.hotent.webag.model.weighingLog.WebagWeighingLog;
import com.hotent.webag.dao.weighingLog.WebagWeighingLogDao;
import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;


import com.hotent.core.service.BaseService;


@Service
public class WebagWeighingLogService extends BaseService<WebagWeighingLog>
{
	@Resource
	private WebagWeighingLogDao dao;
	
	public WebagWeighingLogService()
	{
	}
	
	@Override
	protected IEntityDao<WebagWeighingLog,Long> getEntityDao() 
	{
		return dao;
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
			WebagWeighingLog webagWeighingLog=getWebagWeighingLog(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagWeighingLog.setId(genId);
				this.add(webagWeighingLog);
			}else{
				webagWeighingLog.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagWeighingLog);
			}
			cmd.setBusinessKey(webagWeighingLog.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagWeighingLog对象
	 * @param json
	 * @return
	 */
	public WebagWeighingLog getWebagWeighingLog(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagWeighingLog webagWeighingLog = (WebagWeighingLog)JSONObject.toBean(obj, WebagWeighingLog.class);
		return webagWeighingLog;
	}
	/**
	 * 保存 webag_weighing_log 信息
	 * @param webagWeighingLog
	 */

	public void save(WebagWeighingLog webagWeighingLog) throws Exception{
		Long id=webagWeighingLog.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagWeighingLog.setId(id);
		    this.add(webagWeighingLog);
		}
		else{
		   WebagWeighingLog webagWeighingLogTemd = getById(webagWeighingLog.getId());
		   if(webagWeighingLogTemd==null || webagWeighingLogTemd.equals("")){
		       this.add(webagWeighingLog);
		   }else{
		       this.update(webagWeighingLog);
		   }
		}
	}
}
