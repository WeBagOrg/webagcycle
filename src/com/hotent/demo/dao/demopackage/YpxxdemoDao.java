
package com.hotent.demo.dao.demopackage;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;

import com.hotent.demo.model.demopackage.Ypxxdemo;

@Repository
public class YpxxdemoDao extends BaseDao<Ypxxdemo>
{
	@Override
	public Class<?> getEntityClass()
	{
		return Ypxxdemo.class;
	}

}