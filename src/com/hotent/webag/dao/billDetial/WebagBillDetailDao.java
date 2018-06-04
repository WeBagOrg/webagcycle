
package com.hotent.webag.dao.billDetial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.webag.model.billDetial.WebagBillDetail;
import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;

import com.hotent.core.db.BaseDao;


@Repository
public class WebagBillDetailDao extends BaseDao<WebagBillDetail>
{
	@Override
	public Class<?> getEntityClass()
	{
		return WebagBillDetail.class;
	}

    public List<WebagBillDetail> getByWechatId(String wechatid,String size) {
		Map map = new HashMap();
		map.put("wechatid",wechatid);
		map.put("pageSize",Long.valueOf(size));
		return this.getBySqlKey("getByWechatId",map);
    }
}