
package com.hotent.webag.dao.bagInfo;

import java.util.List;

import com.hotent.webag.model.bagInfo.WebagBaginfo;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.WfBaseDao;



@Repository
public class WebagBaginfoDao extends WfBaseDao<WebagBaginfo>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagBaginfo.class;
	}

}