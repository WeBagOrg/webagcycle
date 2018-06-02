package com.hotent.webag.service.userAccount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;

import com.hotent.webag.dao.userAccount.WebagUserAccountDao;
import com.hotent.webag.model.userAccount.WebagUserAccount;
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
public class WebagUserAccountService extends BaseService<WebagUserAccount>
{
	@Resource
	private WebagUserAccountDao dao;
	
	public WebagUserAccountService()
	{
	}
	
	@Override
	protected IEntityDao<WebagUserAccount,Long> getEntityDao() 
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
			WebagUserAccount webagUserAccount=getWebagUserAccount(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagUserAccount.setId(genId);
				this.add(webagUserAccount);
			}else{
				webagUserAccount.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagUserAccount);
			}
			cmd.setBusinessKey(webagUserAccount.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagUserAccount对象
	 * @param json
	 * @return
	 */
	public WebagUserAccount getWebagUserAccount(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagUserAccount webagUserAccount = (WebagUserAccount)JSONObject.toBean(obj, WebagUserAccount.class);
		return webagUserAccount;
	}
	/**
	 * 保存 用户余额表 信息
	 * @param webagUserAccount
	 */

	public void save(WebagUserAccount webagUserAccount) throws Exception{
		Long id=webagUserAccount.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagUserAccount.setId(id);
		    this.add(webagUserAccount);
		}
		else{
		   WebagUserAccount webagUserAccountTemd = getById(webagUserAccount.getId());
		   if(webagUserAccountTemd==null || webagUserAccountTemd.equals("")){
		       this.add(webagUserAccount);
		   }else{
		       this.update(webagUserAccount);
		   }
		}
	}
}
