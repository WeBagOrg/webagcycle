package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.hotent.platform.ip.util.HtmlTypeEnum;


/**
 * 
 * @author ipph
 *
 */
public class EnumTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Class<?> enumClass;
	private String styleClass;//应用页面样式
	private HtmlTypeEnum styleType;//显示类型，可以显示
	private String fieldName;//字段名称
	private Object fieldValue;//字段值
	private boolean needAdditional;//是否需要附加项，如请选择，只针对select使用
	private String additionalOption;//附加项的名称
	private String cssclass;//label文字样式
	private int limitCount;//多少个一换行
	
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	public Class<?> getEnumClass() {
		return enumClass;
	}
	public void setEnumClass(Class<?> enumClass) {
		this.enumClass = enumClass;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public HtmlTypeEnum getStyleType() {
		return styleType;
	}
	public void setStyleType(HtmlTypeEnum styleType) {
		this.styleType = styleType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	public boolean isNeedAdditional() {
		return needAdditional;
	}
	public void setNeedAdditional(boolean needAdditional) {
		this.needAdditional = needAdditional;
	}
	public String getAdditionalOption() {
		return additionalOption;
	}
	public void setAdditionalOption(String additionalOption) {
		this.additionalOption = additionalOption;
	}
	/**
	 * 实现标签，输出到页面html代码
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out=this.pageContext.getOut();
		String html="";
		if(null!=enumClass&&enumClass.isEnum()){
			html=getHtmlByStyleType(enumClass);
		}
		//输出
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 获取html代码
	 * @param Object
	 * @return
	 */
	private String getHtmlByStyleType(Class<?> obj){
		if(null!=styleType){
			if(styleType.equals(HtmlTypeEnum.checkbox)){
				return getCheckboxHtml(obj);
			}
			
			else
				{if(styleType.equals(HtmlTypeEnum.select)){
					return getSelectHtml(obj);
				}
				else return getRadioHtml(obj);}
		}
		return "";
	}
	/**
	 * 获取checkbox的html代码
	 * @param obj
	 * @return
	 */
	private String getCheckboxHtml(Class<?> obj){
		StringBuffer checkboxHtml=new StringBuffer();
		Enum<?>[] enums=(Enum<?>[]) obj.getEnumConstants();
		int i=enums.length;
		int j=i;
		int n=1;
		if(enums!=null){
			for(Enum<?> e:enums){
			
				checkboxHtml.append("<input name='"+fieldName+"' type='checkbox' value='");
				int value=(Integer) getValueFromEnum("getIndex",e);
				checkboxHtml.append(value+"'");//值
				if(isCheckedOrSelected(value)){//是否选中
					checkboxHtml.append(" checked ");
				}
				setStyle2Html(checkboxHtml);//设置样式
				checkboxHtml.append("/>");
				/*checkboxHtml.append((String)getValueFromEnum("getName",e));//数据项
				*/	
				checkboxHtml.append("<span class='"+cssclass+"'>"+(String)getValueFromEnum("getName",e)+"</span>");//更改样式
				//每limitCount个复选 换行
				if(enums.length>limitCount)
				{				
					j--;
					if(j==i-limitCount*n){			
						checkboxHtml.append("</br>");//自动换行
						checkboxHtml.append("<span class='label'></span>");//更改样式
						n++;
						}
					
				}
			}
		}
		return checkboxHtml.toString();
	}
	/**
	 * 获取select的html代码
	 * @param obj
	 * @return
	 */
	private String getSelectHtml(Class<?> obj){
		StringBuffer selectHtml=new StringBuffer();
		Enum<?>[] enums=(Enum<?>[]) obj.getEnumConstants();
		if(enums!=null){
			selectHtml.append("<select name='"+fieldName+"'");
			setStyle2Html(selectHtml);
			selectHtml.append(">");
			if(needAdditional){
				selectHtml.append("<option value=''>"+additionalOption+"</option>");
			}
			for(Enum<?> e:enums){
				int value=(Integer) getValueFromEnum("getIndex",e);
				selectHtml.append("<option value='"+value+"'");
				if(isCheckedOrSelected(value)){//是否选中
					selectHtml.append(" selected ");
				}
				selectHtml.append(">");
				selectHtml.append(getValueFromEnum("getName",e));	
				selectHtml.append("</option>");
			}
			selectHtml.append("</select>");
		}
		return selectHtml.toString();
	}
	
	
	/**
	 * 获取radio的html代码
	 * @param obj
	 * @return
	 */
	private String getRadioHtml(Class<?> obj){
		StringBuffer RadioHtml=new StringBuffer();
		Enum<?>[] enums=(Enum<?>[]) obj.getEnumConstants();
		int i=enums.length;
		int j=i;
		int n=1;
		if(enums!=null){
			for(Enum<?> e:enums){
				
				RadioHtml.append("<input name='"+fieldName+"' type='radio' value='");
				int value=(Integer) getValueFromEnum("getIndex",e);
				RadioHtml.append(value+"'");//值
				if(isCheckedOrSelected(value)){//是否选中
					RadioHtml.append(" checked ");
				}
				setStyle2Html(RadioHtml);//设置样式
				RadioHtml.append("/>");
				/*RadioHtml.append((String)getValueFromEnum("getName",e));//数据项
				*/	
				RadioHtml.append("<span class='"+cssclass+"'>"+(String)getValueFromEnum("getName",e)+"</span>");//更改样式
				//每5个复选 换行
				if(enums.length>5)
				{				
					j--;
					if(j==i-5*n){			
						RadioHtml.append("</br>");//自动换行
						RadioHtml.append("<span class='label'></span>");//更改样式
						n++;
						}
					
				}
			}
		}
		return RadioHtml.toString();
	}
	
	
	/**
	 * 设置样式
	 * @param styleHtml
	 */
	private void setStyle2Html(StringBuffer styleHtml){
		if(null!=styleClass){
			styleHtml.append(" class='"+styleClass+"' ");
		}
	}
	/**
	 * 判断是否选中
	 * @param val
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isCheckedOrSelected(int val){
		boolean flag=false;
		List<String> listValues=null;
		String value;
		if(null!=fieldValue&& fieldValue instanceof String ){
			value= (String) fieldValue;
			if(StringUtils.isNotEmpty(value)&&value.contains(",")){
				String[] values=value.split(",");
				for(int i=0;i<values.length;i++)
					if(Integer.parseInt(values[i])==val){
						flag=true;
					}
			}
			else if(StringUtils.isNotEmpty(value)&&Integer.parseInt(value)==val){
				flag=true;
			}
		}else if(null!=fieldValue&&fieldValue instanceof List){
			listValues=(List<String>) fieldValue;
			if(null!=listValues&&listValues.contains(val)){//是否选中
				flag=true;
			}
		}
		return flag;
	}
	/**
	 * 获取枚举值
	 * @param methodStr
	 * @param e
	 * @return
	 */
	private Object getValueFromEnum(String methodStr,Enum<?> e){
		try {
			Method method=enumClass.getMethod(methodStr,new Class<?>[]{});//get方法无需传递参数
			return method.invoke(e, null);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	public String getCssclass() {
		return cssclass;
	}
	public void setCssclass(String cssclass) {
		this.cssclass = cssclass;
	}
}
