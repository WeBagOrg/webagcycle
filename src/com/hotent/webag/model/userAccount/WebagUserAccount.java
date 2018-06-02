package com.hotent.webag.model.userAccount;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:用户余额表 Model对象
 */
public class WebagUserAccount extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *用户微信OpenId
	 */
	protected String  userWechatId;
	/**
	 *账号余额
	 */
	protected Long  account;
	/**
	 *用户昵称
	 */
	protected String  nickName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUserWechatId(String userWechatId) 
	{
		this.userWechatId = userWechatId;
	}
	/**
	 * 返回 用户微信OpenId
	 * @return
	 */
	public String getUserWechatId() 
	{
		return this.userWechatId;
	}
	public void setAccount(Long account) 
	{
		this.account = account;
	}
	/**
	 * 返回 账号余额
	 * @return
	 */
	public Long getAccount() 
	{
		return this.account;
	}
	public void setNickName(String nickName) 
	{
		this.nickName = nickName;
	}
	/**
	 * 返回 用户昵称
	 * @return
	 */
	public String getNickName() 
	{
		return this.nickName;
	}
   	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof WebagUserAccount)) 
		{
			return false;
		}
		WebagUserAccount rhs = (WebagUserAccount) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.userWechatId, rhs.userWechatId)
		.append(this.account, rhs.account)
		.append(this.nickName, rhs.nickName)
		.isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.userWechatId) 
		.append(this.account) 
		.append(this.nickName) 
		.toHashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("userWechatId", this.userWechatId) 
		.append("account", this.account) 
		.append("nickName", this.nickName) 
		.toString();
	}

}