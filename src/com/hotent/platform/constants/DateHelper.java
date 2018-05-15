package com.hotent.platform.constants;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	//计算出两个日期相差的天数
	public static long getBetweenDays(Date maxdate,Date mindate){
		long l=maxdate.getTime()-mindate.getTime();
		return l/1000/60/60/24;
	}
	/**
	 * 计算出两个日期相差的月数
	 * @param maxdate
	 * @param mindate
	 * @return
	 */
	public static int getBetweenMonth(Date maxdate,Date mindate){
		Calendar c1=Calendar.getInstance();
		c1.setTime(maxdate);
		Calendar c2=Calendar.getInstance();
		c2.setTime(mindate);
		int years=c1.get(Calendar.YEAR)-c2.get(Calendar.YEAR);
		int month=0;
		if(c1.after(c2)){//c1的日期大
			//int year=c2.get(Calendar.YEAR);
			if(c1.get(Calendar.MONTH)>c2.get(Calendar.MONTH)){//c1的月份比c2的月份大，在专利计算中5月5号与6月1号，一个月内，按一个月计算
				c2.set(Calendar.YEAR,c1.get(Calendar.YEAR));//先转换成同一年
				for(int i=1;i<12;i++){
					c2.add(Calendar.MONTH, 1);
					if(c2.compareTo(c1)>=0){
						month=i;
						break;
					}
				}
			}
			else if(c1.get(Calendar.MONTH)<c2.get(Calendar.MONTH)){//跨年计算
				//int c1Year=c1.get(Calendar.YEAR);
				c1.set(Calendar.YEAR,c2.get(Calendar.YEAR)+1);//即c2的下一年
				for(int i=12;i>0;i--){
					c2.add(Calendar.MONTH, 1);
					if(c2.compareTo(c1)>=0){
						i--;//循环体执行了，但i没有自减
						month=-i;
						break;
					}
				}
			}
			else{//同月
				c2.set(Calendar.YEAR,c1.get(Calendar.YEAR));//先转换成同一年
				if(c1.after(c2)){//排除同月同日和小于当前天
					month=1;
				}
			}
		}
		return years*12+month;
	}
}
