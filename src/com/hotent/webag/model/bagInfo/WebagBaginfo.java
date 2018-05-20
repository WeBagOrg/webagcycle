package com.hotent.webag.model.bagInfo;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.hotent.core.model.WfBaseModel;
/**
 * 对象功能:回收袋信息 Model对象
 */
public class WebagBaginfo extends WfBaseModel
{
	//主键
	protected Long id;
	/**
	 *袋子编号
	 */
	protected String  bagNo;
	/**
	 *添加时间
	 */
	protected java.util.Date  createDate;
	/**
	 *创建者
	 */
	protected String  creatorName;
	/**
	 *创建者id
	 */
	protected Long  creatorId;
	/**
	 *使用次数
	 */
	protected Long  useTime;
	/**
	 *袋子是否启用
	 */
	protected String  bagStatus;



	/**
	 *是否产生二维码
	 */
	protected String  isHaveQR;
	/**
	 *二维码存放路径
	 */
	protected String  QRUrl;

	protected Long  runId=0L;
	public String getIsHaveQR() {
		return isHaveQR;
	}

	public void setIsHaveQR(String isHaveQR) {
		this.isHaveQR = isHaveQR;
	}

	public String getQRUrl() {
		return QRUrl;
	}

	public void setQRUrl(String QRUrl) {
		this.QRUrl = QRUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setBagNo(String bagNo) 
	{
		this.bagNo = bagNo;
	}
	/**
	 * 返回 袋子编号
	 * @return
	 */
	public String getBagNo() 
	{
		return this.bagNo;
	}
	public void setCreateDate(java.util.Date createDate) 
	{
		this.createDate = createDate;
	}
	/**
	 * 返回 添加时间
	 * @return
	 */
	public java.util.Date getCreateDate() 
	{
		return this.createDate;
	}
	public void setCreatorName(String creatorName) 
	{
		this.creatorName = creatorName;
	}
	/**
	 * 返回 创建者
	 * @return
	 */
	public String getCreatorName() 
	{
		return this.creatorName;
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
	public void setUseTime(Long useTime) 
	{
		this.useTime = useTime;
	}
	/**
	 * 返回 使用次数
	 * @return
	 */
	public Long getUseTime() 
	{
		return this.useTime;
	}
	public void setBagStatus(String bagStatus) 
	{
		this.bagStatus = bagStatus;
	}
	/**
	 * 返回 袋子是否启用
	 * @return
	 */
	public String getBagStatus() 
	{
		return this.bagStatus;
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
		if (!(object instanceof WebagBaginfo)) 
		{
			return false;
		}
		WebagBaginfo rhs = (WebagBaginfo) object;
		return new EqualsBuilder()
		.append(this.id,rhs.id)
		.append(this.bagNo, rhs.bagNo)
		.append(this.createDate, rhs.createDate)
		.append(this.creatorName, rhs.creatorName)
		.append(this.creatorId, rhs.creatorId)
		.append(this.useTime, rhs.useTime)
		.append(this.bagStatus, rhs.bagStatus)
		.isEquals();
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.bagNo) 
		.append(this.createDate) 
		.append(this.creatorName) 
		.append(this.creatorId) 
		.append(this.useTime) 
		.append(this.bagStatus) 
		.toHashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id",this.id)
		.append("bagNo", this.bagNo) 
		.append("createDate", this.createDate) 
		.append("creatorName", this.creatorName) 
		.append("creatorId", this.creatorId) 
		.append("useTime", this.useTime) 
		.append("bagStatus", this.bagStatus) 
		.toString();
	}

}