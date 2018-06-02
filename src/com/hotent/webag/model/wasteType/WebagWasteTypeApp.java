package com.hotent.webag.model.wasteType;

import com.hotent.core.model.WfBaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 对象功能:webag_waste_type Model对象
 */
public class WebagWasteTypeApp extends WfBaseModel
{

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

   	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof WebagWasteTypeApp))
		{
			return false;
		}
		WebagWasteTypeApp rhs = (WebagWasteTypeApp) object;
		return new EqualsBuilder()
		.append(this.wasteTypeNo, rhs.wasteTypeNo)
		.append(this.wasteTypeName, rhs.wasteTypeName)
		.append(this.wasteTypePrice, rhs.wasteTypePrice)
		.isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.wasteTypeNo)
		.append(this.wasteTypeName) 
		.append(this.wasteTypePrice) 
		.toHashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("wasteTypeNo", this.wasteTypeNo)
		.append("wasteTypeName", this.wasteTypeName) 
		.append("wasteTypePrice", this.wasteTypePrice) 
		.toString();
	}

}