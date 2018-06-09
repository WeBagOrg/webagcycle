
package com.hotent.webag.dao.bindBagInfo;

import com.hotent.core.db.BaseDao;

import com.hotent.webag.model.bindBagInfo.BindBag;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class BindBagDao extends BaseDao<BindBag>
{
	@Override
	public Class<?> getEntityClass()
	{
		return BindBag.class;
	}

    public List<BindBag> getByWechatId(String wechatId,String size) {
		Map map = new HashMap();
		map.put("wechatid",wechatId);
		map.put("pageSize",Long.valueOf(size));
		return getBySqlKey("getByWechatId",map);
    }
	/**
	 * 根据袋子编号获取被绑定用户信息
	 * @param bagNo
	 * @return
	 */
	public String getByBagNo(String bagNo) {
		List<BindBag> list = getBySqlKey("getByBagNo",bagNo);
		if (list.isEmpty()){
			return null;
		}else{
			return list.get(0).getUserWeChatID();
		}

	}
}