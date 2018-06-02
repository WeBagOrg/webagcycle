package com.hotent.webag.service.weChatInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;

import com.hotent.webag.dao.weChatInfo.WebagWechatuserInfoDao;
import com.hotent.webag.model.wechatInfo.WebagWechatuserInfo;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.GenericService;
import com.hotent.core.util.BeanUtils;

import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;


import com.hotent.core.service.BaseService;


@Service
public class WebagWechatuserInfoService extends BaseService<WebagWechatuserInfo>
{
	@Resource
	private WebagWechatuserInfoDao dao;
	
	public WebagWechatuserInfoService()
	{
	}
	
	@Override
	protected IEntityDao<WebagWechatuserInfo,Long> getEntityDao() 
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
			WebagWechatuserInfo webagWechatuserInfo=getWebagWechatuserInfo(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagWechatuserInfo.setId(genId);
				this.add(webagWechatuserInfo);
			}else{
				webagWechatuserInfo.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagWechatuserInfo);
			}
			cmd.setBusinessKey(webagWechatuserInfo.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagWechatuserInfo对象
	 * @param json
	 * @return
	 */
	public WebagWechatuserInfo getWebagWechatuserInfo(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagWechatuserInfo webagWechatuserInfo = (WebagWechatuserInfo)JSONObject.toBean(obj, WebagWechatuserInfo.class);
		return webagWechatuserInfo;
	}
	/**
	 * 保存 微信用户表 信息
	 * @param webagWechatuserInfo
	 */

	public void save(WebagWechatuserInfo webagWechatuserInfo) throws Exception{
		Long id=webagWechatuserInfo.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagWechatuserInfo.setId(id);
		    this.add(webagWechatuserInfo);
		}
		else{
		   WebagWechatuserInfo webagWechatuserInfoTemd = getById(webagWechatuserInfo.getId());
		   if(webagWechatuserInfoTemd==null || webagWechatuserInfoTemd.equals("")){
		       this.add(webagWechatuserInfo);
		   }else{
		       this.update(webagWechatuserInfo);
		   }
		}
	}
}
