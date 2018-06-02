package com.hotent.webag.model.wasteType;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.WfBaseModel;
/**
 * 对象功能:webag_waste_type Model对象
 */
public class WebagWasteType extends WfBaseModel
{
	//主键
	protected Long id;
	/**
	 *废品类型编号
	 */
	protected String  wasteTypeNo;
	/**
	 *废品类型名称
	 */
	protected String  wasteTypeName;
	/**
	 *废品类型单价
	 */
	protected String wasteTypePrice;
	/**
	 *废品类型描述信息
	 */
	protected String  wasteTypeDescribe;
	/**
	 *创建时间
	 */
	protected java.util.Date  createDate;
	/**
	 *创建者id
	 */
	protected Long  creatorId;
	protected Long  runId=0L;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setWasteTypeNo(String wasteTypeNo) 
	{
		this.wasteTypeNo = wasteTypeNo;
	}
	/**
	 * 返回 废品类型编号
	 * @return
	 */
	public String getWasteTypeNo() 
	{
		return this.wasteTypeNo;
	}
	public void setWasteTypeName(String wasteTypeName) 
	{
		this.wasteTypeName = wasteTypeName;
	}
	/**
	 * 返回 废品类型名称
	 * @return
	 */
	public String getWasteTypeName() 
	{
		return this.wasteTypeName;
	}
	public void setWasteTypePrice(String wasteTypePrice)
	{
		this.wasteTypePrice = wasteTypePrice;
	}
	/**
	 * 返回 废品类型单价
	 * @return
	 */
	public String getWasteTypePrice()
	{
		return this.wasteTypePrice;
	}
	public void setWasteTypeDescribe(String wasteTypeDescribe) 
	{
		this.wasteTypeDescribe = wasteTypeDescribe;
	}
	/**
	 * 返回 废品类型描述信息
	 * @return
	 */
	public String getWasteTypeDescribe() 
	{
		return this.wasteTypeDescribe;
	}
	public void setCreateDate(java.util.Date createDate) 
	{
		this.createDate = createDate;
	}
	/**
	 * 返回 创建时间
	 * @return
	 */
	public java.util.Date getCreateDate() 
	{
		return this.createDate;
	}
	public void setCreatorId(Long creatorId) 
	{
		this.creatorId = creatorId;
	}
	/**
	 * 返回 创建者id
	 * @return
	 */
	public Long getCreatorId() 
	{
		return this.creatorId;
	}
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
   	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof WebagWasteType)) 
		{
			return false;
		}
		WebagWasteType rhs = (WebagWasteType) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.wasteTypeNo, rhs.wasteTypeNo)
		.append(this.wasteTypeName, rhs.wasteTypeName)
		.append(this.wasteTypePrice, rhs.wasteTypePrice)
		.append(this.wasteTypeDescribe, rhs.wasteTypeDescribe)
		.append(this.createDate, rhs.createDate)
		.append(this.creatorId, rhs.creatorId)
		.isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.wasteTypeNo) 
		.append(this.wasteTypeName) 
		.append(this.wasteTypePrice) 
		.append(this.wasteTypeDescribe) 
		.append(this.createDate) 
		.append(this.creatorId) 
		.toHashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("wasteTypeNo", this.wasteTypeNo) 
		.append("wasteTypeName", this.wasteTypeName) 
		.append("wasteTypePrice", this.wasteTypePrice) 
		.append("wasteTypeDescribe", this.wasteTypeDescribe) 
		.append("createDate", this.createDate) 
		.append("creatorId", this.creatorId) 
		.toString();
	}

}