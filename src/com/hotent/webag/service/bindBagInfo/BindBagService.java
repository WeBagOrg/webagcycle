package com.hotent.webag.service.bindBagInfo;

import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;


import com.hotent.webag.dao.bindBagInfo.BindBagDao;
import com.hotent.webag.model.bindBagInfo.BindBag;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class BindBagService extends BaseService<BindBag>
{
	@Resource
	private BindBagDao dao;



	public BindBagService()
	{
	}
	
	@Override
	protected IEntityDao<BindBag,Long> getEntityDao()
	{
		return dao;
	}
	

	/**
	 * 保存 绑定信息
	 * @param bindBag
	 */

	public void save(BindBag bindBag) throws Exception{
		this.save(bindBag);
	}
	/**
	 * 取得回收袋绑定信息
	 * @param wechatId
	 * @param wechatId
	 * @return
	 * @throws Exception
	 */
	public List<BindBag> getByWechatId(String wechatId,String size) {
		return dao.getByWechatId(wechatId,size);
	}

	/**
	 * 根据袋子编号获取被绑定用户信息
	 * @param bagNo
	 * @return
	 */
    public String getByBagNo(String bagNo) {
    	return dao.getByBagNo(bagNo);
    }
}
