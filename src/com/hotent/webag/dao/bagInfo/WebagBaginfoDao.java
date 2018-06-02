
package com.hotent.webag.dao.bagInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * 导出二维码
	 */
	public List<WebagBaginfo> getBagsListByIds(String ids){
		return this.getBySqlKey("getBagsListByIds",ids);
	}

    public int setuserBindQR(String userOpenId, String bagNo) {
		Map param = new HashMap();
		param.put("userOpenId",userOpenId);
		param.put("bagNo",bagNo);
		param.put("bindTime", new Date());
		param.put("bindStauts",1);
		return this.insert("setUserBindQR",param);
    }
}