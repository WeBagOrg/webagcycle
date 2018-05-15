package com.hotent.platform.ip.util;
/**
 *	专利状态 
 * @author ipph
 * 不可配置在数据字典中，因为在使用选择专利对话框时，有限定条件：已公开和已授权，如果在数据字典中改变了则无法检索到相应的字段。
 */
public enum PatentStatusEnum {
	APPLICATION("已申请",0),WITHDRAW("申请后主动撤回",1),NORESPONSE("申请后视为撤回",2),REJECT("申请后驳回",3),
	PUBLIC("已公开",4),EXAMINE("实审中",5),NORESPONSEAFTERPUBLIC("公开后视为撤回",6),REJECTAFTERPUBLIC("公开后驳回",7),
	AUTHORIZATION("有效",10),INVALID("失效",20);
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
	private PatentStatusEnum(){}
	// 构造方法
    private PatentStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
	// 普通方法
    public static String getName(int index) {
        for (PatentStatusEnum c : PatentStatusEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
