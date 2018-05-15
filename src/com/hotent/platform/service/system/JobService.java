package com.hotent.platform.service.system;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.core.db.IEntityDao;
import com.hotent.core.service.BaseService;
import com.hotent.platform.dao.system.JobDao;
import com.hotent.platform.model.system.Job;

/**
 *<pre>
 * 对象功能:职务表 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2013-11-28 16:17:48
 *</pre>
 */
@Service
public class JobService extends BaseService<Job>
{
	@Resource
	private JobDao dao;
	@Resource 
	private UserPositionService userPositionService;
	@Resource
	private PositionService positionService;
	
	public JobService()
	{
	}
	
	@Override
	protected IEntityDao<Job, Long> getEntityDao() 
	{
		return dao;
	}
	
	
	/**
	 * 删除职务，实际是修改标志位
	 * 同时修改sys_user_pos用户岗位关系表、sys_pos岗位表
	 * @author hjx
	 * @version 创建时间：2013-12-4  上午10:50:27
	 * @param posId
	 */
	public void deleteByUpdateFlag(Long id){
		  dao.deleteByUpdateFlag(id);
		  userPositionService.delByJobId(id);
		  positionService.deleByJobId(id);
	}

	/**
	 * 判断职务名称是否存在
	 * @param jobname
	 * @return
	 */
	public boolean isExistJobCode(String jobCode) {
		return dao.isExistJobCode(jobCode);
	}
	
	
	public boolean isExistJobCodeForUpd(String jobCode,Long jobId) {
		return dao.isExistJobCodeForUpd(jobCode,jobId);
	}
	/**
	 * 通过职务代码获取职务
	 * @param jobCode
	 * @return
	 */
	public Job getByJobCode(String jobCode){
		return dao.getByJobCode(jobCode);
	}
}
