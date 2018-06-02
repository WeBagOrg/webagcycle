package com.hotent.webag.model.wechatInfo;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:微信用户表 Model对象
 */
public class WebagWechatuserInfo extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *昵称
	 */
	protected String  nickName;
	/**
	 *微信openId
	 */
	protected String  openId;
	/**
	 *省份
	 */
	protected String  province;
	/**
	 *城市
	 */
	protected String  city;
	/**
	 *注册时间
	 */
	protected java.util.Date  createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setNickName(String nickName) 
	{
		this.nickName = nickName;
	}
	/**
	 * 返回 昵称
	 * @return
	 */
	public String getNickName() 
	{
		return this.nickName;
	}
	public void setOpenId(String openId) 
	{
		this.openId = openId;
	}
	/**
	 * 返回 微信openId
	 * @return
	 */
	public String getOpenId() 
	{
		return this.openId;
	}
	public void setProvince(String province) 
	{
		this.province = province;
	}
	/**
	 * 返回 省份
	 * @return
	 */
	public String getProvince() 
	{
		return this.province;
	}
	public void setCity(String city) 
	{
		this.city = city;
	}
	/**
	 * 返回 城市
	 * @return
	 */
	public String getCity() 
	{
		return this.city;
	}
	public void setCreateTime(java.util.Date createTime) 
	{
		this.createTime = createTime;
	}
	/**
	 * 返回 注册时间
	 * @return
	 */
	public java.util.Date getCreateTime() 
	{
		return this.createTime;
	}
   	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof WebagWechatuserInfo)) 
		{
			return false;
		}
		WebagWechatuserInfo rhs = (WebagWechatuserInfo) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.nickName, rhs.nickName)
		.append(this.openId, rhs.openId)
		.append(this.province, rhs.province)
		.append(this.city, rhs.city)
		.append(this.createTime, rhs.createTime)
		.isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.nickName) 
		.append(this.openId) 
		.append(this.province) 
		.append(this.city) 
		.append(this.createTime) 
		.toHashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("nickName", this.nickName) 
		.append("openId", this.openId) 
		.append("province", this.province) 
		.append("city", this.city) 
		.append("createTime", this.createTime) 
		.toString();
	}

}