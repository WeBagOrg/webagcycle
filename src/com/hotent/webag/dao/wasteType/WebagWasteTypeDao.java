
package com.hotent.webag.dao.wasteType;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.WfBaseDao;

import com.hotent.webag.model.wasteType.WebagWasteType;

@Repository
public class WebagWasteTypeDao extends WfBaseDao<WebagWasteType>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagWasteType.class;
	}

}
