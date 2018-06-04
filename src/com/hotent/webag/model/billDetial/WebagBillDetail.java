package com.hotent.webag.model.billDetial;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:用户流水明细表 Model对象
 */
public class WebagBillDetail extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *用户微信openID
	 */
	protected String  userWeChatID;
	/**
	 *回收袋编号
	 */
	protected String  bagNo;
	/**
	 *创建时间
	 */
	protected java.util.Date  creatTime;
	/**
	 *操作员id
	 */
	protected Long  staffId;
	/**
	 *职工姓名
	 */
	protected String  staffName;
	/**
	 *废品类别
	 */
	protected String  wasteType;
	/**
	 *称重重量
	 */
	protected Long  weight;
	/**
	 *单价
	 */
	protected Long  unitPrice;
	/**
	 *总价
	 */
	protected Long  totalPrice;
	/**
	 *是否给用户发送消息
	 */
	protected String  isSendMsg;
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
	public void setCreatTime(java.util.Date creatTime) 
	{
		this.creatTime = creatTime;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public java.util.Date getCreatTime() 
	{
		return this.creatTime;
	}
	public void setStaffId(Long staffId) 
	{
		this.staffId = staffId;
	}
	/**
	 * 返回 操作员id
	 * @return
	 */
	public Long getStaffId() 
	{
		return this.staffId;
	}
	public void setStaffName(String staffName) 
	{
		this.staffName = staffName;
	}
	/**
	 * 返回 职工姓名
	 * @return
	 */
	public String getStaffName() 
	{
		return this.staffName;
	}
	public void setWasteType(String wasteType) 
	{
		this.wasteType = wasteType;
	}
	/**
	 * 返回 废品类别
	 * @return
	 */
	public String getWasteType() 
	{
		return this.wasteType;
	}
	public void setWeight(Long weight) 
	{
		this.weight = weight;
	}
	/**
	 * 返回 称重重量
	 * @return
	 */
	public Long getWeight() 
	{
		return this.weight;
	}
	public void setUnitPrice(Long unitPrice) 
	{
		this.unitPrice = unitPrice;
	}
	/**
	 * 返回 单价
	 * @return
	 */
	public Long getUnitPrice() 
	{
		return this.unitPrice;
	}
	public void setTotalPrice(Long totalPrice) 
	{
		this.totalPrice = totalPrice;
	}
	/**
	 * 返回 总价
	 * @return
	 */
	public Long getTotalPrice() 
	{
		return this.totalPrice;
	}
	public void setIsSendMsg(String isSendMsg) 
	{
		this.isSendMsg = isSendMsg;
	}
	/**
	 * 返回 是否给用户发送消息
	 * @return
	 */
	public String getIsSendMsg() 
	{
		return this.isSendMsg;
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
		if (!(object instanceof WebagBillDetail)) 
		{
			return false;
		}
		WebagBillDetail rhs = (WebagBillDetail) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.userWeChatID, rhs.userWeChatID)
		.append(this.bagNo, rhs.bagNo)
		.append(this.creatTime, rhs.creatTime)
		.append(this.staffId, rhs.staffId)
		.append(this.staffName, rhs.staffName)
		.append(this.wasteType, rhs.wasteType)
		.append(this.weight, rhs.weight)
		.append(this.unitPrice, rhs.unitPrice)
		.append(this.totalPrice, rhs.totalPrice)
		.append(this.isSendMsg, rhs.isSendMsg)
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
		.append(this.userWeChatID) 
		.append(this.bagNo) 
		.append(this.creatTime) 
		.append(this.staffId) 
		.append(this.staffName) 
		.append(this.wasteType) 
		.append(this.weight) 
		.append(this.unitPrice) 
		.append(this.totalPrice) 
		.append(this.isSendMsg) 
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
		.append("userWeChatID", this.userWeChatID) 
		.append("bagNo", this.bagNo) 
		.append("creatTime", this.creatTime) 
		.append("staffId", this.staffId) 
		.append("staffName", this.staffName) 
		.append("wasteType", this.wasteType) 
		.append("weight", this.weight) 
		.append("unitPrice", this.unitPrice) 
		.append("totalPrice", this.totalPrice) 
		.append("isSendMsg", this.isSendMsg) 
		.append("nickName", this.nickName) 
		.toString();
	}
	//微信显示使用
	protected String showDate;

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
}