package com.hotent.platform.ip.util;
/**
 * 专利申请类别
 * @author changyouli
 *
 */
public enum PatentTypeEnum {
	fmzl("发明专利","fmzl"),syxx("实用新型","syxx"),wgzl("外观设计","wgzl");
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
	private PatentTypeEnum(){}
	// 构造方法
    private PatentTypeEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }
	// 普通方法
    public static String getName(String index) {
        for (PatentTypeEnum c : PatentTypeEnum.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }
}
