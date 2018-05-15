package com.hotent.platform.ip.util;

import java.util.Calendar;
import java.util.Date;

import com.hotent.core.util.DateFormatUtil;
import com.hotent.core.util.DateUtil;
/**
 * 日期工具类
 * @author ipph
 * 2014/9/12 
 */
public class DateExtUtil extends DateUtil{
	/**
	 * 添加年
	 * @param date 待追加的日期
	 * @param year 待追加的年数
	 * @return
	 */
	public static Date addYear(Date date,int year){
		if(year<0)
			return date;
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		return c.getTime();
	}
	/**
	 * 添加月
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date,int month){
		if(month<0)
			return date;
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}
	/**
	 * 获取当年的第一天
	 * @return
	 */
	public static String firstDayOfYear(){
		Calendar c=Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return DateFormatUtil.format(c.getTime(), "yyyy-MM-dd");
	}
	/**
	 * 获取当年的最后一天
	 * @return
	 */
	public static String lastDayOfYear(){
		Calendar c=Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);
		return DateFormatUtil.format(c.getTime(), "yyyy-MM-dd");
	}
}
