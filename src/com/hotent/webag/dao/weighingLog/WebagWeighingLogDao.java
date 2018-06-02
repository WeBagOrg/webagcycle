
package com.hotent.webag.dao.weighingLog;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;

import com.hotent.webag.model.weighingLog.WebagWeighingLog;

@Repository
public class WebagWeighingLogDao extends BaseDao<WebagWeighingLog>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagWeighingLog.class;
	}

}