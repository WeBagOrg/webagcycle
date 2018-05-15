package com.hotent.platform.ip.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class PatentValidationUtil {
	/**
	 * appNumber="CN201420424716.X";实用新型
	 * appNumber="CN89105613.0";发明专利
	 * appNumber="CN85100613";发明专利
	 * 根据申请号返回查询的数据库
	 * @param appNumber
	 * @return
	 */
	public static String getPatentInterfaceDbName(String appNumber){
		String number="";
		String[] zlNumberArr=getZlNumber(appNumber);
		if(null!=zlNumberArr&&StringUtils.isNotEmpty(zlNumberArr[0]))
		{
			String zlNumber=zlNumberArr[0];
			Pattern p=Pattern.compile("^(85|86|87|88)(1|2|3|8|9)\\d+");
			Matcher m=p.matcher(zlNumber);
			if(m.matches()){
				number=zlNumber.substring(2,3);
			}
			else if(zlNumber.length()==8){
				p=Pattern.compile("^(89|90|91|92|93|94|95|96|97|98|99|00|01|02|03)(1|2|3|8|9)\\d+");
				m=p.matcher(zlNumber);
				if(m.matches()){
					number=zlNumber.substring(2,3);
				}
			}
			else{
				number=zlNumber.substring(4,5);
			}
		}
			
		if(number.equals("1")||number.equals("8")){
			return "fmzl";
		}
		else if(number.equals("2")||number.equals("9")){
			return "syxx";
		}
		else if(number.equals("3")){
			return "wgzl";
		}
		else{
			return "";
		}
	}
	/**
	 * 判断是否是合法的申请号
	 * @param appNumber
	 * @return
	 */
	public static boolean isValidAppNumber(String appNumber){
		String[] zlNumber=getZlNumber(appNumber);
		if(null!=zlNumber&&StringUtils.isNotEmpty(zlNumber[0])){
			return validation(zlNumber[0],zlNumber[1]);
		}
		return false;
	}
	
	private static String[] getZlNumber(String appNumber){
		String[] zlNumber=new String[2];//第1个位置存放专利号，第二个wiz存放校验位
		if(StringUtils.isNotEmpty(appNumber)){
			appNumber=appNumber.trim();
			Pattern p=Pattern.compile("^([a-zA-Z]{2})?+\\d{8}(\\d{4})?+(\\.[\\d|X|x])?+");
			Matcher m=p.matcher(appNumber);
			if(m.matches()){
				p=Pattern.compile("^[a-zA-Z]{2}(\\d+?)(\\.[\\d|X|x])?");
				m=p.matcher(appNumber);
				if(m.matches()){
					zlNumber[0]=m.group(1);
					if(appNumber.indexOf(".")!=-1){
						zlNumber[1]=appNumber.substring(appNumber.lastIndexOf(".")+1);
					}
				}
				else if(appNumber.indexOf(".")!=-1){
					zlNumber[0]=appNumber.substring(0,appNumber.lastIndexOf("."));
					zlNumber[1]=appNumber.substring(appNumber.lastIndexOf(".")+1);
				}
				else{
					zlNumber[0]=appNumber;
				}
			}
		}
		return zlNumber;
	}
	/**
	 * 根据校验位校验专利是否正确
	 * @param zlNumber
	 * @param validation
	 * @return
	 */
	private static boolean validation(String zlNumber,String validation){
		Pattern p=Pattern.compile("^(85|86|87|88)(1|2|3|8|9)\\d+");
		Matcher m=p.matcher(zlNumber);
		if("".equals(validation)){
			if(m.matches())
				return true;
		}
		if(zlNumber.length()==8){
			p=Pattern.compile("^(89|90|91|92|93|94|95|96|97|98|99|00|01|02|03)(1|2|3|8|9)\\d+");
			m=p.matcher(zlNumber);
			if(!m.matches())
				return false;
		}
		int[] numbers=new int[zlNumber.length()];
		int sum=0;
		for(int i=0, mul=2;i<numbers.length;i++,mul++){
			if(mul>9) mul=2;
			sum+=Integer.parseInt(zlNumber.substring(i,i+1))*mul;
		}
		int flag=sum%11;
		if(flag==10&&validation.equalsIgnoreCase("x")){
			return true;
		}
		else{
			return validation.equals(flag+"");
		}
	}
	
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
