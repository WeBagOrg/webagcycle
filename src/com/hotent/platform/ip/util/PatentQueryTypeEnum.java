package com.hotent.platform.ip.util;

/**
 *	专利查询类型
 * @author ipph
 * 根据不同类型，拼接不同查询条件使用，目前只有专利年费申请
 */
public enum PatentQueryTypeEnum {
	ZLNFSQ("专利年费申请","zlnfsq");
	private String name;
	private String index;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	//无参构造方法
	private PatentQueryTypeEnum(){}
	// 构造方法
    private PatentQueryTypeEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }
	// 普通方法
    public static String getName(String index) {
        for (PatentQueryTypeEnum c : PatentQueryTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}