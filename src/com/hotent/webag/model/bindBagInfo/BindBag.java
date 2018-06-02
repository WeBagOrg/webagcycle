package com.hotent.webag.model.bindBagInfo;

import com.hotent.core.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * 对象功能:用户流水明细表 Model对象
 */
public class BindBag extends BaseModel
{
	/*
	 *用户微信openID
	 */
	protected String  userWeChatID;
	/**
	 *回收袋编号
	 */
	protected String  bagNo;
	/**
	 *绑定时间
	 */
	protected java.util.Date  bindTime;
	/**
	 *解绑时间
	 */
	protected java.util.Date  unBindTime;
	/*
	 *绑定状态
	 */
	protected   Integer bindStauts;

	public Integer getBindStauts() {
		return bindStauts;
	}

	public void setBindStauts(Integer bindStauts) {
		this.bindStauts = bindStauts;
	}

	public void setUserWeChatID(String userWeChatID)
	{
		this.userWeChatID = userWeChatID;
	}
	/**
	 * 返回 用户微信openID
	 * @return
	 */
	public String getUserWeChatID() 
	{
		return this.userWeChatID;
	}
	public void setBagNo(String bagNo) 
	{
		this.bagNo = bagNo;
	}
	/**
	 * 返回 回收袋编号
	 * @return
	 */
	public String getBagNo() 
	{
		return this.bagNo;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Date getUnBindTime() {
		return unBindTime;
	}

	public void setUnBindTime(Date unBindTime) {
		this.unBindTime = unBindTime;
	}
}