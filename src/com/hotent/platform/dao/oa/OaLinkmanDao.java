package com.hotent.platform.dao.oa;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.oa.OaLinkman;
/**
 *<pre>
 * 对象功能:OA_LINKMAN Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2015-07-14 09:13:57
 *</pre>
 */
@Repository
public class OaLinkmanDao extends BaseDao<OaLinkman>
{
	@Override
	public Class<?> getEntityClass()
	{
		return OaLinkman.class;
	}

	
	
	
	
}