package com.hotent.platform.ip.util;
/**
 * 
 * @author ipph
 * 获取显示的类型
 */
public enum HtmlTypeEnum {
	checkbox(0),select(1),radio(2);
	private Integer value;

	private HtmlTypeEnum(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
}
