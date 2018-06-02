
package com.hotent.webag.dao.weChatInfo;

import java.util.List;

import com.hotent.webag.model.wechatInfo.WebagWechatuserInfo;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;



@Repository
public class WebagWechatuserInfoDao extends BaseDao<WebagWechatuserInfo>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagWechatuserInfo.class;
	}

}