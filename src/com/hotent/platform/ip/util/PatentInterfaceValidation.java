package com.hotent.platform.ip.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 专利接口数据校验
 * @author Administrator
 *
 */
@Deprecated
public class PatentInterfaceValidation {
	/**
	 * 判断是否以字母开头
	 * @param str
	 */
	public static boolean isStartWithCharacter(String str){
		Pattern p=Pattern.compile("^[A-Za-z]+.*");
		Matcher m=p.matcher(str);
		if(m.matches()){
			return true;
		}
		return false;
	}
	/**
	 * 判断给定的字符串是否是合法的日期
	 * @param strDate
	 */
	public static boolean isValidDate(String strDate){
		String regex="^("
				+ "(((1[6-9]|[2-9]\\d)\\d{2})([-./])(0?[13578]|1[02])[-./](0?[1-9]|[12]\\d|3[01]))"//存在31天的月份
				+ "|(((1[6-9]|[2-9]\\d)\\d{2})([-./])(0?[13456789]|1[012])[-./](0?[1-9]|[12]\\d|30))"//不包含二月
				+ "|(((1[6-9]|[2-9]\\d)\\d{2})([-./])0?2[-./](0?[1-9]|1\\d|2[0-8]))"//二月
				+ "|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))([-./])0?2[-./]29)"//闰月
				+ ")$";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(strDate);
		if(m.matches()){
			return true;
		}else{
			return false;
		}
	}
	
}
