
package com.hotent.webag.dao.bindBagInfo;

import com.hotent.core.db.BaseDao;

import com.hotent.webag.model.bindBagInfo.BindBag;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BindBagDao extends BaseDao<BindBag>
{
	@Override
	public Class<?> getEntityClass()
	{
		return BindBag.class;
	}

    public List<BindBag> getByWechatId(String wechatId) {
		return getBySqlKey("getByWechatId","wechatId");
    }
}