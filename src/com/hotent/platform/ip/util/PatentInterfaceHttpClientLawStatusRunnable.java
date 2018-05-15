package com.hotent.platform.ip.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import com.hotent.core.util.AppUtil;
import com.hotent.platform.service.ip.IpPatentLawStatusService;

/**
 * 调用专利接口查询数据
 * @author Administrator
 *
 */
@Deprecated
public class PatentInterfaceHttpClientLawStatusRunnable implements Runnable{
	private String appNumber;
	private Date lawDate;
	private Date maxDate;
	private String[] appNumbers;
	private Date[] lawDates;
	
	public String[] getAppNumbers() {
		return appNumbers;
	}
	public void setAppNumbers(String[] appNumbers) {
		this.appNumbers = appNumbers;
	}
	public Date[] getLawDates() {
		return lawDates;
	}
	public void setLawDates(Date[] lawDates) {
		this.lawDates = lawDates;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public String getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}
	public Date getLawDate() {
		return lawDate;
	}
	public void setLawDate(Date lawDate) {
		this.lawDate = lawDate;
	}
	public PatentInterfaceHttpClientLawStatusRunnable(){
		
	}
	public PatentInterfaceHttpClientLawStatusRunnable(String appNumber,Date lawDate){
		this.appNumber=appNumber;
		this.lawDate=lawDate;
	}
	public PatentInterfaceHttpClientLawStatusRunnable(String[] appNumbers,Date[] lawDates){
		this.appNumbers=appNumbers;
		this.lawDates=lawDates;
	}
	@Override
	public void run() {
		IpPatentLawStatusService lawStatusService=(IpPatentLawStatusService) AppUtil.getBean("ipPatentLawStatusService");
		if(StringUtils.isNotEmpty(appNumber)){
			getLawstatus(appNumber,lawDate,lawStatusService);
		}
		else if(null!=appNumbers&&appNumbers.length>0){
			for(int i=0;i<appNumbers.length;i++){
				if(StringUtils.isNotEmpty(appNumbers[i])){
					getLawstatus(appNumbers[i],lawDates[i],lawStatusService);
				}
			}
		}
	}
	/**
	 * 执行查询
	 */
	private void getLawstatus(String appNumber,Date lawDate,IpPatentLawStatusService lawStatusService){
		try {
			lawStatusService.lawsasyn(appNumber, lawDate);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
