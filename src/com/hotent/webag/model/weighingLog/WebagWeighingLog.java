package com.hotent.webag.model.weighingLog;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.BaseModel;
/**
 * 对象功能:webag_weighing_log Model对象
 */
public class WebagWeighingLog extends BaseModel
{
	//主键
	protected Long id;
	/**
	 *称重日志编号
	 */
	protected String  weighingLogNo;
	/**
	 *废品类型编号
	 */
	protected String  wasteTypeNo;
	/**
	 *回收袋编号
	 */
	protected String  bagNo;
	/**
	 * 当时的价格
	 */
	protected String thePriceOfTheTime;
	/**
	 * 重量
	 */
	protected String weight;
	/**
	 *返利金额
	 */
	protected String  amountOfRebate;
	/**
	 *创建时间
	 */
	protected java.util.Date  createDate;
	/**
	 *创建者id
	 */
	protected Long  creatorId;
	protected Long  runId=0L;

	public String getWeight() {return weight;}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getThePriceOfTheTime() {return thePriceOfTheTime;}
	public void setThePriceOfTheTime(String thePriceOfTheTime) {
		this.thePriceOfTheTime = thePriceOfTheTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setWeighingLogNo(String weighingLogNo) 
	{
		this.weighingLogNo = weighingLogNo;
	}
	/**
	 * 返回 称重日志编号
	 * @return
	 */
	public String getWeighingLogNo() 
	{
		return this.weighingLogNo;
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
	public void setAmountOfRebate(String amountOfRebate)
	{
		this.amountOfRebate = amountOfRebate;
	}
	/**
	 * 返回 返利金额
	 * @return
	 */
	public String getAmountOfRebate()
	{
		return this.amountOfRebate;
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
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	/**
	 * 返回 创建者id
	 * @return
	 */
	public Long getCreatorId()
	{
		return this.creatorId;
	}

   	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof WebagWeighingLog)) 
		{
			return false;
		}
		WebagWeighingLog rhs = (WebagWeighingLog) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.weighingLogNo, rhs.weighingLogNo)
		.append(this.wasteTypeNo, rhs.wasteTypeNo)
		.append(this.bagNo, rhs.bagNo)
				.append(this.thePriceOfTheTime, rhs.thePriceOfTheTime)
				.append(this.weight, rhs.weight)
		.append(this.amountOfRebate, rhs.amountOfRebate)
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
		.append(this.weighingLogNo) 
		.append(this.wasteTypeNo) 
		.append(this.bagNo) 
		.append(this.amountOfRebate) 
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
		.append("weighingLogNo", this.weighingLogNo) 
		.append("wasteTypeNo", this.wasteTypeNo) 
		.append("bagNo", this.bagNo) 
		.append("amountOfRebate", this.amountOfRebate) 
		.append("createDate", this.createDate) 
		.append("creatorId", this.creatorId) 
		.toString();
	}

}