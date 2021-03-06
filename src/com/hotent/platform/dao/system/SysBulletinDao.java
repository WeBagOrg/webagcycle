
package com.hotent.platform.dao.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.pdfbox.examples.util.RemoveAllText;
import org.springframework.stereotype.Repository;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.db.BaseDao;
import com.hotent.core.page.PageBean;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.system.SysBulletin;

@Repository
public class SysBulletinDao extends BaseDao<SysBulletin>
{
	@Resource 
	SysReadRecodeDao readRecodeDao;
	
	@Override
	public Class<?> getEntityClass()
	{
		return SysBulletin.class;
	}
	
	@Override
	public int delById(Long id) {
		readRecodeDao.deleteByParam(null, id, null);
		return super.delById(id);
	}
	
	public void delByColumnId(Long columnId) {
		readRecodeDao.deleteByParam(columnId, null, null);
		this.delBySqlKey("delByColumnId", columnId)  ;
	}

	public List<SysBulletin> getAllByAlias(String alias, PageBean pb) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", ContextUtil.getCurrentUserId());
		map.put("alias", alias);
		return getBySqlKey("getAllByAlias",map,pb);
	}

	public List<SysBulletin> getAllByAlias(QueryFilter queryFilter) {
		return getBySqlKey("getAllByAlias",queryFilter);
	}
	
	/***修改公告状态**/
	public void updateStatus(Long id, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		this.update("updateStatus",map);
	}

	/**
	 * 桌面根据当前用户的公司id拿到公告列表
	 * @param tenantId
	 * @param alias
	 * @param pb
	 * @return
	 */
	/*public List<SysBulletin> getListForDesktop(Long tenantId, String alias,
			PageBean pb) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tenantId", tenantId);
		map.put("alias", alias);
		return getBySqlKey("getDesktopList",map,pb);
	}*/

	/**
	 * 桌面拿到所有的公告
	 * @param alias
	 * @param pb
	 * @return
	 */
	/*public  List<SysBulletin> getAllListForDesktop(String alias, PageBean pb) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alias", alias);
		return getBySqlKey("allListForDesktop",map,pb);
	}*/
}