package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 年费展示标签
 * @author Administrator
 *
 */
public class AnnualFeeTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	private String patType;
	private Date appDate;
	private Date authDate;
	private int num;//每行显示的列数
	private String colors;
	private Map<String,String> colorsMap;
	private int annualFeeNum;//年费已交至

	public int getAnnualFeeNum() {
		return annualFeeNum;
	}
	public void setAnnualFeeNum(int annualFeeNum) {
		this.annualFeeNum = annualFeeNum;
	}
	public String getPatType() {
		return patType;
	}
	public void setPatType(String patType) {
		this.patType = patType;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public Date getAuthDate() {
		return authDate;
	}
	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		initColorMap();
		String html=getAnnualFeeHtml();
		//初始化化颜色map
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 获取已交费的年份
	 * @return
	 */
	private String getAnnualFeeHtml(){
		if(null==appDate||null==authDate) return "";
		StringBuffer sb=new StringBuffer();
		//根据专利类型获取专利的有效年限
		int feeNum=20;
		if(patType.matches("[a-z]{4}"))
			feeNum=patType.equals("fmzl")?20:10;
		else{
			feeNum=patType.equals("发明专利")?20:10;
		}
		Calendar app=Calendar.getInstance();
		app.setTime(appDate);
		Calendar auth=Calendar.getInstance();
		auth.setTime(authDate);
		Calendar dueDate=Calendar.getInstance();
		dueDate.setTime(appDate);
		int left=feeNum%num;
		for(int i=1;i<=feeNum;i++){
			if((i-1)%num==0){
				if(i!=1)
					sb.append("</tr>");
				sb.append("<tr>");
			}
			app.add(Calendar.YEAR, 1);
			if(app.before(auth)){
				sb.append("<td style=\"background:"+colorsMap.get("unpayment")+";text-align: center;height:30px;width:25%\">第"+i+"年</td>");
			}
			else{
				if(annualFeeNum==0) annualFeeNum=i;
				
				if(annualFeeNum>=i)
					sb.append("<td style=\"background:"+colorsMap.get("payment")+";text-align: center;height:30px;width:25%\">第"+i+"年</td>");
				else{
					if(annualFeeNum+1==i&&annualFeeNum!=0){
						
						dueDate.add(Calendar.YEAR, annualFeeNum);
						
						if(dueDate.after(Calendar.getInstance()))//非超期
						{
							sb.append("<td style=\"background:"+colorsMap.get("due")+";text-align: center;height:30px;width:25% \" ><a href='#' onclick='annualFee("+i+")'>第"+i+"年 "+new SimpleDateFormat("yyyy-MM-dd").format(dueDate.getTime())+"之前</a></td>");
					    }
						else
						{  
						  dueDate.add(Calendar.MONTH, 6);
						  if(dueDate.after(Calendar.getInstance()))
							  sb.append("<td style=\"background:"+colorsMap.get("out")+";text-align: center;height:30px;width:25% \" ><a href='#' onclick='annualFee("+i+")'>第"+i+"年 "+new SimpleDateFormat("yyyy-MM-dd").format(dueDate.getTime())+"截止</a></td>");
						  else{
							  sb.append("<td style=\"background:"+colorsMap.get("out")+";text-align: center;height:30px;width:25% \">第"+i+"年，未缴费已终止</td>");
						  }
						}
					
					}
					else{
						sb.append("<td style=\"text-align: center;height:30px;width:25% \">第"+i+"年</td>");
					}
					
				}
			}
		}
		if(left!=0){
			for(int i=left;i<num;i++){
				sb.append("<td></td>");
			}
		}
		return sb.toString();
	}
	/**
	 * 初始化颜色的map集合
	 */
	private void initColorMap(){
		String[] temp=colors.split(";");
		colorsMap=new HashMap<String, String>();
		for(int i=0;i<temp.length;i++){
			colorsMap.put(temp[i].split(":")[0], temp[i].split(":")[1]);
		}
	}
}
