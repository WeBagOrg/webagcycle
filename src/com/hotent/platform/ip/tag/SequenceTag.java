package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.hotent.platform.ip.util.HtmlTypeEnum;

/**
 * 通过页面传递的参数获取生成顺序标签
 * @author Ipph
 *
 */
public class SequenceTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	private String styleClass;//应用页面样式
	private HtmlTypeEnum styleType;//显示类型，可以显示
	private String fieldName;//字段名称
	private Object fieldValue;//字段值
	private boolean needAdditional;//是否需要附加项，如请选择，只针对select使用
	private String additionalOption;//附加项的名称
	private int min;//序列的开始位置
	private int max;//序列的结束位置
	private int step=1;//序列的步长
	private String prefix;//序列的前缀
	private String suffix;//序列的后置
	private String lastAppendOptions;
	public String getLastAppendOptions() {
		return lastAppendOptions;
	}
	public void setLastAppendOptions(String lastAppendOptions) {
		this.lastAppendOptions = lastAppendOptions;
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
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * 重写doStartTag标签
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out=this.pageContext.getOut();
		String html="";
		html=getHtmlByStyleType();
		//输出
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 根据指定的类型，获取相应的html代码
	 * @param obj
	 * @return
	 */
	private String getHtmlByStyleType(){
		if(null!=styleType){
			if(styleType.equals(HtmlTypeEnum.checkbox)){
				return getCheckboxHtml();
			}
			else
				return getSelectHtml();
		}
		return "";
	}
	
	/**
	 * 获取checkbox的html代码
	 * @param obj
	 * @return
	 */
	private String getCheckboxHtml(){
		StringBuffer checkboxHtml=new StringBuffer();
		if(min>=0&&min<=max){
			for(int index=min;index<=max;index=index+step){
				checkboxHtml.append("<input name='"+fieldName+"' type='checkbox' value='");
				checkboxHtml.append(index);//值
				if(isCheckedOrSelected(index+"")){//是否选中
					checkboxHtml.append(" checked ");
				}
				setStyle2Html(checkboxHtml);//设置样式
				checkboxHtml.append("/>");
				getTextValue(checkboxHtml,index);//数据项
			}
			
		}
		return checkboxHtml.toString();
	}
	/**
	 * 获取select的html代码
	 * @param obj
	 * @return
	 */
	private String getSelectHtml(){
		StringBuffer selectHtml=new StringBuffer();
		if(min>=0&&min<=max){
			selectHtml.append("<select name='"+fieldName+"'");
			setStyle2Html(selectHtml);
			selectHtml.append(">");
			if(needAdditional){
				selectHtml.append("<option value=''>"+additionalOption+"</option>");
			}
			for(int index=min;index<=max;index=index+step){
				selectHtml.append("<option value='"+index+"'");
				if(isCheckedOrSelected(index+"")){//是否选中
					selectHtml.append(" selected ");
				}
				selectHtml.append(">");
				getTextValue(selectHtml,index);//数据项
				selectHtml.append("</option>");
			}
			if(StringUtils.isNotEmpty(lastAppendOptions)){
				String[] temp=lastAppendOptions.split(",");
				for(String opt:temp){
					selectHtml.append("<option value='"+opt+"'>");
					getTextValue(selectHtml,Integer.parseInt(opt));
					selectHtml.append("</option>");
				}
			}
			selectHtml.append("</select>");
		}
		return selectHtml.toString();
	}
	
	/**
	 * 判断是否选中
	 * @param val
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isCheckedOrSelected(String val){
		boolean flag=false;
		List<String> listValues=null;
		String value=null;
		if(null!=fieldValue&&fieldValue instanceof String){
			value=(String) fieldValue;
			if(value!=null&&value.equals(val)){
				flag=true;
			}
		}
		if(null!=fieldValue&&fieldValue instanceof Integer){
			value= fieldValue.toString();
			if(value!=null&&value.equals(val)){
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
	 * 设置样式
	 * @param styleHtml
	 */
	private void setStyle2Html(StringBuffer styleHtml){
		if(null!=styleClass){
			styleHtml.append(" class='"+styleClass+"' ");
		}
	}
	/**
	 * 获取显示的文本值
	 * @param value
	 */
	private void getTextValue(StringBuffer html,int value){
		if(prefix!=null){
			html.append(prefix);
		}
		html.append(value);
		if(suffix!=null){
			html.append(suffix);
		}
	}
}
