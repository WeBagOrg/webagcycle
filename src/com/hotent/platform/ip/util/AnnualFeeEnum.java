package com.hotent.platform.ip.util;
/**
 * 年费的四种枚举状态值
 * @author ipph
 * 2014/09/13 石建强
 */
public enum AnnualFeeEnum {
	AHEAD(1),INMONTH(2),OVERDUE(3),TIP(4);
	private Integer value;
	private AnnualFeeEnum(Integer value){
		this.value=value;
	}
	public Integer getValue(){
		return this.value;
	}
}
