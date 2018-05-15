package com.hotent.platform.service.oa;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.service.bpm.ProcessRunService;
import com.hotent.platform.model.bpm.ProcessRun;
import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;
import com.hotent.core.api.util.ContextUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.platform.model.oa.OaLinkman;
import com.hotent.platform.dao.oa.OaLinkmanDao;
import com.hotent.core.service.BaseService;

/**
 *<pre>
 * 对象功能:联系人 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2015-07-14 09:13:57
 *</pre>
 */
@Service
public class OaLinkmanService extends  BaseService<OaLinkman>
{
	@Resource
	private OaLinkmanDao dao;
	
	
	
	public OaLinkmanService()
	{
	}
	
	@Override
	protected IEntityDao<OaLinkman, Long> getEntityDao() 
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
			OaLinkman oaLinkman=getOaLinkman(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				oaLinkman.setId(genId);
				this.add(oaLinkman);
			}else{
				oaLinkman.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(oaLinkman);
			}
			cmd.setBusinessKey(oaLinkman.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取OaLinkman对象
	 * @param json
	 * @return
	 */
	public OaLinkman getOaLinkman(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		OaLinkman oaLinkman = (OaLinkman)JSONObject.toBean(obj, OaLinkman.class);
		return oaLinkman;
	}
	
	/**
	 * 保存 联系人 信息
	 * @param oaLinkman
	 */
	public void save(OaLinkman oaLinkman){
		Long id=oaLinkman.getId();
		if(id==null || id==0){
			Long userid = ContextUtil.getCurrentUserId();
			id=UniqueIdUtil.genId();
			oaLinkman.setId(id);
			oaLinkman.setUserid(userid);
			this.add(oaLinkman);
		}
		else{
			this.update(oaLinkman);
		}
	}

	public List<OaLinkman> getByUserId(QueryFilter queryFilter, Long userId) {
		queryFilter.addFilter("userId", userId);
		List<OaLinkman> list = dao.getBySqlKey("getByUserId", queryFilter);
		return list;
	}

	public List<OaLinkman> getSelectorList(QueryFilter queryFilter, Long userId) {
		queryFilter.addFilter("userId", userId);
		List<OaLinkman> list = dao.getBySqlKey("getSelectorList", queryFilter);
		return list;
	}
	/**
	 * 检查最近联系人
	 * @param userId
	 * @return
	 */
	public List<OaLinkman> getMailLinkMan(Long userId) {
		List<OaLinkman> list = dao.getBySqlKey("getMailLinkMan", userId);
		return list;
	}

	/**
	 * 拿到联系人map
	 * @param userId
	 * @return
	 */
	public Map<String, String> getLinkManMap(Long userId) {
		List<OaLinkman> linkManList = this.getMailLinkMan(userId);
		Map<String,String> linkManMap = new HashMap<String,String>();
		for(OaLinkman linkMan:linkManList){
			linkManMap.put(linkMan.getName(),linkMan.getEmail());
		}
		return linkManMap;
	}

	
}
