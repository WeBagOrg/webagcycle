package com.hotent.platform.dao.bpm;

import org.springframework.stereotype.Repository;

import com.hotent.core.db.BaseDao;
import com.hotent.platform.model.bpm.FormDefTree;
/**
 *<pre>
 * 对象功能:form_def_tree Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:liyj
 * 创建时间:2015-05-12 14:46:20
 *</pre>
 */
@Repository
public class FormDefTreeDao extends BaseDao<FormDefTree>
{
	@Override
	public Class<?> getEntityClass()
	{
		return FormDefTree.class;
	}
	
	/**
	 * 根据formKey获取FormDefTree对象。
	 * @param formKey
	 * @return
	 */
	public FormDefTree getByFormKey(String formKey){
		FormDefTree defTree=this.getUnique("getByFormKey", formKey);
		return defTree;
	}
	
	public void delByFormDefKey(String formKey){
		this.delBySqlKey("delByFormDefKey", formKey);
	}
	
}