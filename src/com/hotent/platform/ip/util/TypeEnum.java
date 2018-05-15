package com.hotent.platform.ip.util;
/**
 * 定义待转换的类型
 * @author ipph
 *
 */
public enum TypeEnum {
	string(1), merge(2),clear(3);
	
	private Integer value;

	private TypeEnum(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
}
