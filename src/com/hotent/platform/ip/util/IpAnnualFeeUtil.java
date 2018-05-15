package com.hotent.platform.ip.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.util.DateFormatUtil;
import com.hotent.platform.constants.PatentConstants;
import com.hotent.platform.model.ip.IpExpStandard;
import com.hotent.platform.model.ip.IpPatent;

/**
 * 专利年费工具类
 * @author ipph
 * 2014/9/11 石建强
 */
public class IpAnnualFeeUtil {
	//年费监控的提示信息,使用国际化信息
	public static String EXPIRED="annualFee.expired";
	public static String UNAUTHORIZED="annualFee.unauthorized";
	public static String INVALID="annualFee.invalid";
	/**
	 * 设置专利的提示信息，失效或申请中时，根据专利状态来判断
	 * @param patentStatus 专利状态
	 * @return
	 */
	public static String patentAnnualFeeTip(IpPatent pm,int aheadMonth){
		AnnualFeeEnum feeEnum=AnnualFeeEnum.AHEAD;
		Calendar tempc=Calendar.getInstance();
		if(isExpiredPatent(pm.getAppDate(),pm.getPatType(),validYear(pm.getPatType()))){
			return ContextUtil.getMessages(INVALID);
		}
		else if(pm.getPatStatus().equals(PatentStatusEnum.INVALID.getIndex() + "")){
			return ContextUtil.getMessages(INVALID);
		}
		if(null==pm.getAuthDate()||null==pm.getAppDate())
			return "";
		if(pm.getPatType().equals(PatentConstants.PATENT_INVENTION)){//发明专利
			//判断是否由于未缴纳费用，而超期失效
			if(isExpireForNoPay(pm.getAuthDate(), pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
				return ContextUtil.getMessages(EXPIRED);
			}
			//专利应缴费时间
			tempc=getAnnualFeeDate(pm.getAuthDate(), pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee());
			//获取专利所处的缴费阶段
			feeEnum=annualFeePhrase(pm.getAuthDate(),pm.getAppDate(),null==pm.getAnnualFee()?0:pm.getAnnualFee(),aheadMonth);
		}
		else{
			if(isExpireForNoPay(pm.getAuthDate(), pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
				return ContextUtil.getMessages(EXPIRED);
			}
			tempc=getAnnualFeeDate(pm.getAuthDate(), pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee());
			feeEnum=annualFeePhrase(pm.getAuthDate(),pm.getAppDate(),null==pm.getAnnualFee()?0:pm.getAnnualFee(),aheadMonth);
		}
		//根据专利所处的缴费阶段给出提示信息
		if(feeEnum==AnnualFeeEnum.AHEAD){
			return ContextUtil.getMessages("annualFee.ahead", new Object[]{DateFormatUtil.format(tempc.getTime()),(null==pm.getAnnualFee()?0:pm.getAnnualFee())+1});
		}
		else if(feeEnum==AnnualFeeEnum.INMONTH){
			tempc.add(Calendar.MONTH, 1);
			tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));
			return ContextUtil.getMessages("annualFee.inMonth", new Object[]{DateFormatUtil.format(tempc.getTime()),(null==pm.getAnnualFee()?0:pm.getAnnualFee())+1});
		}
		else if(feeEnum==AnnualFeeEnum.OVERDUE){
			int due=getOverdueMonth(tempc);
			tempc.add(Calendar.MONTH,6);
			tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));
			return ContextUtil.getMessages("annualFee.overdue", new Object[]{DateFormatUtil.format(tempc.getTime()),(null==pm.getAnnualFee()?0:pm.getAnnualFee())+1,due});
		}
		return "";
	}
	/**
	 * 专利费用提醒
	 * @param pm 专利实例
	 * @param aheadMonth 提前月数
	 * 判断条件是否需要提醒，是否需要年费
	 */
	public static String remindAnuualFee(IpPatent pm,int aheadMonth){
		//判断条件非空,申请号不能空
		if(null==pm.getAppNumber())
			return "";
		//判断是否已到提醒时间
		if(isToRemind(pm,aheadMonth)&&isNeedAnnualFee(pm)&&pm.getPatStatus().equals(PatentStatusEnum.AUTHORIZATION.getIndex() + "")){
			//设置年费相关的提醒信息
			return patentAnnualFeeTip(pm, aheadMonth);
		}
		else{
			return patentAnnualFeeTip(pm,aheadMonth);
		}
	}
	
	/**
	 * 判断年费是否到提醒时间
	 * @param pm 专利实体类
	 * @param aheadMonth 提前几个月提醒
	 * @return
	 */
	public static boolean isToRemind(IpPatent pm,int aheadMonth){
		boolean flag=false;
		if(pm.getPatStatus().equals(PatentStatusEnum.AUTHORIZATION.getIndex() + "")){
			if(pm.getAppDate()!=null&&pm.getAuthDate()!=null){
				if(pm.getPatType().equals(PatentConstants.PATENT_INVENTION)&&pm.getAuthDate()!=null){//发明专利
					flag=isDueToRemind(pm.getAuthDate(),pm.getAppDate(),null==pm.getAnnualFee()?0:pm.getAnnualFee(),aheadMonth,AnnualFeeEnum.TIP);
				}
				else{
					flag=isDueToRemind(pm.getAuthDate(),pm.getAppDate(),null==pm.getAnnualFee()?0:pm.getAnnualFee(),aheadMonth,AnnualFeeEnum.TIP);
				}
			}
		}
		return flag;
	}
	
	/**
	 * 是否需要年费，
	 * 发明未授权的专利不需要年费计算,专利期限为20年
	 * 实用新型和外观设计，专利期限为10年，从公开后开始计算
	 * 判断条件：专利状态为已授权，申请号不空，专利是否超过有效期（未），今年是否已缴纳年费（未）
	 */
	public static boolean isNeedAnnualFee(IpPatent pm){
		boolean flag=false;
		//判断专利状态必须是已授权的专利
		if(StringUtils.isNotEmpty(pm.getPatStatus())&&!pm.getPatStatus().equals(PatentStatusEnum.AUTHORIZATION.getIndex() + ""))
			return flag;
		if(pm!=null&&StringUtils.isNotEmpty(pm.getAppNumber())){//专利实体类,并且专利申请号不空
			if(StringUtils.isNotEmpty(pm.getPatType())&&PatentConstants.PATENT_INVENTION.equals(pm.getPatType())){//如果是发明专利
				if(!isExpiredPatent(pm.getAppDate(), PatentConstants.PATENT_INVENTION,null==pm.getAnnualFee()?0:pm.getAnnualFee())
					&&!isPayAnnualFee(pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
					flag=true;
				}
			}
			else if(StringUtils.isNotEmpty(pm.getPatType())){//使用新型和外观设计
				if(!isExpiredPatent(pm.getAppDate(), PatentConstants.NEW_TYPE,null==pm.getAnnualFee()?0:pm.getAnnualFee())
						&&!isPayAnnualFee(pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
						flag=true;
					}
			}
		}
		return flag;
	}
	/**
	 * 流程中的年费，可缴纳本年的所有专利
	 * @param pm
	 * @return
	 */
	public static boolean isNeedAnnualFeeForFlow(IpPatent pm){
		boolean flag=false;
		//判断专利状态必须是已授权的专利
		if(StringUtils.isNotEmpty(pm.getPatStatus())&&!pm.getPatStatus().equals(PatentStatusEnum.AUTHORIZATION.getIndex() + ""))
			return flag;
		if(pm!=null&&StringUtils.isNotEmpty(pm.getAppNumber())){//专利实体类,并且专利申请号不空
			if(StringUtils.isNotEmpty(pm.getPatType())&&PatentConstants.PATENT_INVENTION.equals(pm.getPatType())){//如果是发明专利
				if(!isExpiredPatent(pm.getAppDate(), PatentConstants.PATENT_INVENTION,null==pm.getAnnualFee()?0:pm.getAnnualFee())
					&&isPatentPayYear(pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
					flag=true;
				}
			}
			else if(StringUtils.isNotEmpty(pm.getPatType())){//使用新型和外观设计
				if(!isExpiredPatent(pm.getAppDate(), PatentConstants.NEW_TYPE,null==pm.getAnnualFee()?0:pm.getAnnualFee())
						&&isPatentPayYear(pm.getAppDate(), null==pm.getAnnualFee()?0:pm.getAnnualFee())){
						flag=true;
					}
			}
		}
		return flag;
	}
	/**
	 * 判断专利是否已经超过有效年限
	 * @param applyDate 专利申请日
	 * @param patentType 专利类型
	 */
	public static boolean isExpiredPatent(Date applyDate,String patentType,int validYear){
		boolean flag=false;
		if(null!=applyDate){
			if(validYear==0){
				if(patentType.equals(PatentConstants.PATENT_INVENTION)){//发明专利，有效期限20年
					if(validYear==0&&DateExtUtil.addYear(applyDate, 20).before(new Date())){
						flag=true;
					}
				}
				else if(validYear==0&&DateExtUtil.addYear(applyDate, 10).before(new Date())){
					flag=true;
				}
			}
			else {
				Date d=DateExtUtil.addYear(applyDate, validYear);
				d=DateExtUtil.addMonth(d,6);
				if(d.before(new Date())){//如果有效年限比当前日期大，则专利有效
					flag=true;
				}
			}
		}
		return flag;
	}
	/**
	 * 判断今年是否已经缴纳年费
	 * @param authorizedDate
	 * @param payYear
	 * @return
	 */
	/*public static boolean isPayAnnualFee(Date authorizedDate,int payYear){
		boolean flag=false;
		if(authorizedDate!=null&&DateExtUtil.addYear(authorizedDate, payYear).after(new Date())){
			flag=true;
		}
		return flag;
	}*/
	public static boolean isPayAnnualFee(Date appDate,int payYear){
		boolean flag=false;
		if(appDate!=null&&DateExtUtil.addYear(appDate, payYear).after(new Date())){
			flag=true;
		}
		return flag;
	}
	/**
	 * 判断是否今年需要缴纳年费
	 * @param appDate
	 * @param payYear
	 * @return
	 */
	public static boolean isPatentPayYear(Date appDate,int payYear){
		boolean flag=false;
		if(appDate!=null){
			Calendar c=Calendar.getInstance();
			int year=c.get(Calendar.YEAR);
			c.setTime(DateExtUtil.addYear(appDate, payYear));
			if(year==c.get(Calendar.YEAR)){
				flag=true;
			}
		}
		return flag;
	}
	/**
	 * 是否到来提醒时间
	 * @param authorizedDate 授权日期（专利），公开日期（外观，新型）
	 * @param payYears 已经缴纳的年费
	 * @param aheadMonth 提前多少个月
	 * @return
	 * 提醒的时间可划分为三个阶段：提前，缴费月内，超期（有滞纳金）三阶段
	 */
	private static boolean isDueToRemind(Date authorizedDate,Date applyDate,int payYears,int aheadMonth,AnnualFeeEnum feeEnum){
		Calendar tempc=getAnnualFeeDate(authorizedDate,applyDate,payYears);
		Calendar now=Calendar.getInstance();
		clearHMSMi(now);
		if(feeEnum==AnnualFeeEnum.TIP){
			now.add(Calendar.MONTH, aheadMonth);//是否到了提醒阶段
			return now.compareTo(tempc)>=0;
		}
		else if(feeEnum==AnnualFeeEnum.AHEAD){//提前
			Calendar c=Calendar.getInstance();
			c.setTime(now.getTime());
			c.add(Calendar.MONTH, aheadMonth);
			return tempc.compareTo(now)>=0&&tempc.compareTo(c)<=0;
			
		
		}
		else if(feeEnum==AnnualFeeEnum.INMONTH){//缴费月份内
			Calendar c=Calendar.getInstance();
			c.setTime(tempc.getTime());
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DAY_OF_MONTH, postponeHoliday(c));//推迟假期
			return now.compareTo(tempc)>=0&&now.compareTo(c)<=0;
		}
		else{//超期，有滞纳金
			Calendar c=Calendar.getInstance();
			c.setTime(tempc.getTime());
			c.add(Calendar.MONTH, 6);
			c.add(Calendar.DAY_OF_MONTH, postponeHoliday(c));//推迟假期
			return now.compareTo(tempc)>0&&now.compareTo(c)<=0;
		}
	}
	/**
	 * 获取应交年费的日期
	 * @param authorizedDate 授权日
	 * @param applyDate 申请日
	 * @param payYears 已交年费
	 * @return
	 * 授权日+已缴费年为已缴费的年的截止年份，申请日的月为授权日的截止月份，申请日的天为授权日的截止天。
	 * 年费的截止即使下一年的开始
	 */
	private static Calendar getAnnualFeeDate(Date authorizedDate,Date applyDate,int payYears){
		Calendar tempc=Calendar.getInstance();
		tempc.setTime(authorizedDate);
		Calendar temp=Calendar.getInstance();
		temp.setTime(applyDate);
		tempc.set(Calendar.YEAR, tempc.get(Calendar.YEAR)+payYears);
		if(isLessThanApplyMonth(authorizedDate,applyDate)){
			tempc.add(Calendar.YEAR, -1);
		}
		tempc.set(Calendar.MONTH,temp.get(Calendar.MONTH));
		tempc.set(Calendar.DAY_OF_MONTH,temp.get(Calendar.DAY_OF_MONTH));
		return tempc;
	}
	/**
	 * 获取滞纳月数
	 * @param annualFeeDate 应交年费时间
	 * @return
	 */
	private static int getOverdueMonth(Calendar annualFeeDate){
		Calendar now=Calendar.getInstance();
		clearHMSMi(now);
		Calendar tempc=Calendar.getInstance();
		tempc.setTime(annualFeeDate.getTime());
		int temp=0;
		for(int i=0;i<6;){
			tempc.add(Calendar.MONTH, 1);
			tempc.set(Calendar.DAY_OF_MONTH, annualFeeDate.get(Calendar.DAY_OF_MONTH));//重新设置会原来的天
			tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));//添加推迟的日期
			if(now.after(tempc)){//相等算做当月，即
				temp++;
			}else{
				break;
			}
		}
		return temp;
	}
	/**
	 * 判断是否比申请日期月份和天数要小
	 * @param authorizedDate 授权日（发明专利），公开日（外观和实用新型）
	 * @param applyDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static boolean isLessThanApplyMonth(Date authorizedDate,Date applyDate){
		Calendar tempc=Calendar.getInstance();
		tempc.setTime(authorizedDate);
		tempc.set(Calendar.MONTH, applyDate.getMonth());
		tempc.set(Calendar.DAY_OF_MONTH, applyDate.getDate());
		if(authorizedDate.before(tempc.getTime())){
			return true;
		}
		return false;
	}
	/** 是否由于未交费用而使专利失效
	 * @param authorizedDate 授权日（专利发明），公开日（外观，新型）
	 * @param applyDate 申请日
	 * @param payYears 已交年费年数
	 * @return
	 */
	private static boolean isExpireForNoPay(Date authorizedDate,Date applyDate,int payYears){
		Calendar tempc=getAnnualFeeDate(authorizedDate,applyDate,payYears);
		tempc.add(Calendar.MONTH, 6);
		Calendar now=Calendar.getInstance();
		clearHMSMi(now);
		if(now.after(tempc)){
			return true;
		}
		return false;
	}
	/**
	 * 清空一个Calendar的时，分，秒，毫秒数据
	 * @param c
	 * @return
	 */
	private static void clearHMSMi(Calendar c){
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}
	/** 年费所处阶段
	 * @param authorizedDate
	 * @param applyDate
	 * @param payYears
	 * @param aheadMonth
	 * @return
	 */
	private static AnnualFeeEnum annualFeePhrase(Date authorizedDate,Date applyDate,int payYears,int aheadMonth){
		//判断是否提前
		if(isDueToRemind(authorizedDate,applyDate,payYears,aheadMonth,AnnualFeeEnum.AHEAD)){
			return AnnualFeeEnum.AHEAD;
		}
		//判断是否当月
		else if(isDueToRemind(authorizedDate,applyDate,payYears,aheadMonth,AnnualFeeEnum.INMONTH)){
			return AnnualFeeEnum.INMONTH;
		}
		//判断是否有滞纳金
		else if(isDueToRemind(authorizedDate,applyDate,payYears,aheadMonth,AnnualFeeEnum.OVERDUE)){
			return AnnualFeeEnum.OVERDUE;
		}
		return AnnualFeeEnum.AHEAD;
	}
	
	/**
	 * 计算滞纳金时，如果遇到假期需要向后延期处理
	 * @param c 滞纳金计算的结束日期
	 * @return
	 */
	private static int postponeHoliday(Calendar c){
		//如果是星期6，则推出2天，到星期1
		if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
			return 2;
		}
		//如果是星期天，则推出1天
		else if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			return 1;
		}
		return 0;
	}
	
	
	public static Calendar patentAnnualFee(IpPatent pm,int aheadMonth){
		AnnualFeeEnum feeEnum=AnnualFeeEnum.AHEAD;
		Calendar tempc=Calendar.getInstance();
		if(isExpiredPatent(pm.getAppDate(),pm.getPatType(),pm.getAnnualFee())){
			return null;
		}
		else if(pm.getPatStatus().equals(PatentStatusEnum.INVALID.getIndex() + "")){
			return null;
		}
		if(null==pm.getAuthDate()||null==pm.getAppDate())
			return null;
		if(pm.getPatType().equals(PatentConstants.PATENT_INVENTION)){//发明专利
			//判断是否由于未缴纳费用，而超期失效
			if(isExpireForNoPay(pm.getAuthDate(), pm.getAppDate(), pm.getAnnualFee())){
				return null;
			}
			//专利应缴费时间
			tempc=getAnnualFeeDate(pm.getAuthDate(), pm.getAppDate(),pm.getAnnualFee());
			//获取专利所处的缴费阶段
			feeEnum=annualFeePhrase(pm.getAuthDate(),pm.getAppDate(),pm.getAnnualFee(),aheadMonth);
		}
		else{
			if(isExpireForNoPay(pm.getAuthDate(), pm.getAppDate(),pm.getAnnualFee())){
				return null;
			}
			
			tempc=getAnnualFeeDate(pm.getAuthDate(), pm.getAppDate(),pm.getAnnualFee());
			feeEnum=annualFeePhrase(pm.getAuthDate(),pm.getAppDate(),pm.getAnnualFee(),aheadMonth);
		}
		//根据专利所处的缴费阶段给出提示信息
		if(feeEnum==AnnualFeeEnum.AHEAD){
			//return ContextUtil.getMessages("annualFee.ahead", new Object[]{DateFormatUtil.format(tempc.getTime()),(StringUtils.isNotEmpty(pm.getPatentAnnurity())?Integer.parseInt(pm.getPatentAnnurity()):0)+1});
			Calendar now=Calendar.getInstance();
			Calendar c=Calendar.getInstance();
			c.setTime(tempc.getTime());
			c.add(Calendar.MONTH, -aheadMonth);//提前提醒
		   if(tempc.compareTo(now)>=0&&now.compareTo(c)>=0)
			{  tempc.add(Calendar.MONTH,6);//无论是否超期，都按最后的时间提醒
			   tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));
			   return tempc;
			}
		   else return null;
		}
		else if(feeEnum==AnnualFeeEnum.INMONTH){
			//tempc.add(Calendar.MONTH, 1);
			tempc.add(Calendar.MONTH,6);//无论是否超期，都按最后的时间提醒
			tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));
		//	return ContextUtil.getMessages("annualFee.inMonth", new Object[]{DateFormatUtil.format(tempc.getTime()),(StringUtils.isNotEmpty(pm.getPatentAnnurity())?Integer.parseInt(pm.getPatentAnnurity()):0)+1});
		    return tempc;
		}
		else if(feeEnum==AnnualFeeEnum.OVERDUE){
			int due=getOverdueMonth(tempc);
			tempc.add(Calendar.MONTH,6);
			tempc.add(Calendar.DAY_OF_MONTH, postponeHoliday(tempc));
		//	return ContextUtil.getMessages("annualFee.overdue", new Object[]{DateFormatUtil.format(tempc.getTime()),(StringUtils.isNotEmpty(pm.getPatentAnnurity())?Integer.parseInt(pm.getPatentAnnurity()):0)+1,due});
		    return tempc;
		}
		//return "";
		return null;
	}
	/**
	 * 获取专利的有效年限
	 * @param patType
	 * @return
	 */
	public static int validYear(String patType){
		if(PatentConstants.PATENT_INVENTION.equals(patType)){
			return 20;
		}
		else{
			return 10;
		}
	}
	/**
	 * 获取应已缴纳的年费年数
	 * @param appDate
	 * @param AuthDate
	 * @return
	 */
	public static int getAnnualFeeNum(Date appDate){
		Calendar app=Calendar.getInstance();
		app.setTime(appDate);
		Calendar now=Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND, 0);
		int feeNumber=0;
		while(app.before(now)){
			feeNumber++;
			app.add(Calendar.YEAR, 1);
		}
		return feeNumber;
	}
	/**
	 * 获取已缴纳的年费年数
	 * @param appDate
	 * @param lastDate
	 * @return
	 */
	public static int getAnnualFeeNum(Date appDate,Date lastDate){
		Calendar app=Calendar.getInstance();
		app.setTime(appDate);
		Calendar now=Calendar.getInstance();
		now.setTime(lastDate);
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND, 0);
		int feeNumber=0;
		while(app.before(now)){
			feeNumber++;
			app.add(Calendar.YEAR, 1);
		}
		return feeNumber;
	}
	/**
	 * 获取专利年费对应的金额
	 * @param annualFeeStandards
	 * @return
	 */
	public static Map<String,Object[]> getAnnualFee(List<IpExpStandard> annualFeeStandards){
		if(null!=annualFeeStandards&&annualFeeStandards.size()>0){
			Map<String,Object[]> annualFee=new HashMap<String, Object[]>();
			for(IpExpStandard standard:annualFeeStandards){
				Object[] temp=new Object[5];
				temp[0]=standard.getAmount();
				temp[1]=standard.getCompanyReduceRate();
				temp[2]=standard.getPersonalReduceRate();
				temp[3]=standard.getId();
				temp[4]=standard.getStandardName();
				annualFee.put(standard.getPk(),temp);
			}
			return annualFee;
		}
		return null;
	}
}
