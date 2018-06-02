package com.hotent.webag.service.billDetial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;

import com.hotent.webag.dao.billDetial.WebagBillDetailDao;
import com.hotent.webag.model.billDetial.WebagBillDetail;
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
public class WebagBillDetailService extends BaseService<WebagBillDetail>
{
	@Resource
	private WebagBillDetailDao dao;
	
	public WebagBillDetailService()
	{
	}
	
	@Override
	protected IEntityDao<WebagBillDetail,Long> getEntityDao() 
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
			WebagBillDetail webagBillDetail=getWebagBillDetail(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagBillDetail.setId(genId);
				this.add(webagBillDetail);
			}else{
				webagBillDetail.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagBillDetail);
			}
			cmd.setBusinessKey(webagBillDetail.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagBillDetail对象
	 * @param json
	 * @return
	 */
	public WebagBillDetail getWebagBillDetail(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagBillDetail webagBillDetail = (WebagBillDetail)JSONObject.toBean(obj, WebagBillDetail.class);
		return webagBillDetail;
	}
	/**
	 * 保存 用户流水明细表 信息
	 * @param webagBillDetail
	 */

	public void save(WebagBillDetail webagBillDetail) throws Exception{
		Long id=webagBillDetail.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagBillDetail.setId(id);
		    this.add(webagBillDetail);
		}
		else{
		   WebagBillDetail webagBillDetailTemd = getById(webagBillDetail.getId());
		   if(webagBillDetailTemd==null || webagBillDetailTemd.equals("")){
		       this.add(webagBillDetail);
		   }else{
		       this.update(webagBillDetail);
		   }
		}
	}
	/**
	 * 取得用户流水明细表明细(微信)
	 * @param wechatid
	 * @param wechatid
	 * @return
	 * @throws Exception
	 */
    public List<WebagBillDetail> getByWechatId(String wechatid) {
    	return dao.getByWechatId(wechatid);
    }
}
