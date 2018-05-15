package com.hotent.demo.model.demopackage;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:ypxxdemo Model对象
 */
public class Ypxxdemo extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *样品编号
	 */
	protected String  ypbh;
	/**
	 *样品名称
	 */
	protected String  ypmc;
	/**
	 *采样人
	 */
	protected String  cyr;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setYpbh(String ypbh) 
	{
		this.ypbh = ypbh;
	}
	/**
	 * 返回 样品编号
	 * @return
	 */
	public String getYpbh() 
	{
		return this.ypbh;
	}
	public void setYpmc(String ypmc) 
	{
		this.ypmc = ypmc;
	}
	/**
	 * 返回 样品名称
	 * @return
	 */
	public String getYpmc() 
	{
		return this.ypmc;
	}
	public void setCyr(String cyr) 
	{
		this.cyr = cyr;
	}
	/**
	 * 返回 采样人
	 * @return
	 */
	public String getCyr() 
	{
		return this.cyr;
	}
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Ypxxdemo)) 
		{
			return false;
		}
		Ypxxdemo rhs = (Ypxxdemo) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.ypbh, rhs.ypbh)
		.append(this.ypmc, rhs.ypmc)
		.append(this.cyr, rhs.cyr)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.ypbh) 
		.append(this.ypmc) 
		.append(this.cyr) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("ypbh", this.ypbh) 
		.append("ypmc", this.ypmc) 
		.append("cyr", this.cyr) 
		.toString();
	}

}