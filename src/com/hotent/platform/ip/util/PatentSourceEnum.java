package com.hotent.platform.ip.util;
/**
 * 数据来源
 * @author ipph
 *
 */
public enum PatentSourceEnum {
	PROPOSAL("提案",0),PATENT("专利申请",1);
	private String name;
	private int index;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	//无参构造方法
	private PatentSourceEnum(){}
	// 构造方法
    private PatentSourceEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
	// 普通方法
    public static String getName(int index) {
        for (PatentSourceEnum c : PatentSourceEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
