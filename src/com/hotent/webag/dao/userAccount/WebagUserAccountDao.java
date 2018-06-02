
package com.hotent.webag.dao.userAccount;

import java.util.List;

import com.hotent.webag.model.userAccount.WebagUserAccount;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;



@Repository
public class WebagUserAccountDao extends BaseDao<WebagUserAccount>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagUserAccount.class;
	}

}