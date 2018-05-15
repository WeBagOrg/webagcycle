package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.hotent.core.util.AppUtil;
import com.hotent.platform.dao.system.DictionaryDao;
import com.hotent.platform.ip.util.HtmlTypeEnum;
import com.hotent.platform.model.system.Dictionary;

public class DictionaryTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	private String nodeKey;//查询的主键
	private String styleClass;//应用页面样式
	private HtmlTypeEnum styleType;//显示类型，可以显示
	private String fieldName;//字段名称
	private Object fieldValue;//字段值
	private boolean needAdditional;//是否需要附加项，如请选择，只针对select使用
	private String additionalOption;//附加项的名称
	private String cssclass;//label文字样式
	private int limitCount;//多少个一换行
	private boolean isIndent=true;//是否缩进，默认缩进
	private String defaultValue;

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isIndent() {
		return isIndent;
	}
	public boolean getIsIndent() {
		return isIndent;
	}
	public void setIndent(boolean isIndent) {
		this.isIndent = isIndent;
	}
	
	public void setIsIndent(boolean isIndent) {
		this.isIndent = isIndent;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public String getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
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

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		if(fieldValue==null||"".equals(fieldValue)){
			fieldValue=defaultValue;
		}
		//获取jdbcDao用来查询数据库字段，并构造出
		/*JdbcDao jdbcDao=(JdbcDao) AppUtil.getBean(JdbcDao.class);
		String sql=""*/
		DictionaryDao dictionary=(DictionaryDao) AppUtil.getBean(DictionaryDao.class);
		String html="";
		if(null!=dictionary){
			Map<String, String> queryMap=new HashMap<String, String>();
			queryMap.put("catKey", "DIC");
			queryMap.put("nodeKey",nodeKey.toUpperCase());
			List<Dictionary> dictionarys=dictionary.getDictionaryByNodeKey(queryMap);
			if(null!=dictionarys&&dictionarys.size()>0){
				html=getHtmlByStyleType(dictionarys);
			}
		}
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 获取数据字典组成的html代码
	 * @param dictionarys
	 * @return
	 */
	private String getHtmlByStyleType(List<Dictionary> dictionarys){
		if(null!=styleType){
			if(styleType.equals(HtmlTypeEnum.checkbox)){
				return getCheckboxHtml(dictionarys);
			}
			else
			{  if(styleType.equals(HtmlTypeEnum.select)){
				return getSelectHtml(dictionarys);
			}
				return getRadioHtml(dictionarys);
			}
		}
		return "";
	}
	/**
	 * 获取checkbox形式的html代码
	 * @param dictionarys
	 * @return
	 */
	private String getCheckboxHtml(List<Dictionary> dictionarys){
		StringBuffer checkboxHtml=new StringBuffer();
		//不勾选任何一个时候就相当于不限了，是否需要添加这个选项有待商议
		/*if(needAdditional){
			checkboxHtml.append("<input name='"+fieldName+"' type='checkbox' value='' checked='checked'/>");
			checkboxHtml.append("<span class='label'>"+additionalOption+"</span>");
		}	*/
		Dictionary dictionary=null;
		for(int index=0;index<dictionarys.size();index++){
			dictionary=dictionarys.get(index);
			checkboxHtml.append("<input name='"+fieldName+"' type='checkbox' value='");
			checkboxHtml.append(dictionary.getItemValue()+"'");
			if(isCheckedOrSelected(dictionary.getItemValue())){//是否选中
				checkboxHtml.append(" checked ");
			}				
			setStyle2Html(checkboxHtml);//设置样式
			checkboxHtml.append("/>");			
			//添加样式
			checkboxHtml.append("<span class='"+cssclass+"'>"+dictionary.getItemName()+"</span>");
			if(limitCount>0&&dictionarys.size()>limitCount&&index!=0&&(index+1)%limitCount==0)
			{
				breakLine(checkboxHtml);
			}
		}
		return checkboxHtml.toString();
	}
	/**
	 * 获取select形式的html代码
	 * @param dictionarys
	 * @return
	 */
	private String getSelectHtml(List<Dictionary> dictionarys){
		StringBuffer selectHtml=new StringBuffer();
		selectHtml.append("<select name='"+fieldName+"'");
		setStyle2Html(selectHtml);
		selectHtml.append(">");
		if(needAdditional){
			selectHtml.append("<option value=''>"+additionalOption+"</option>");
		}
		for(Dictionary dictionary:dictionarys){
			selectHtml.append("<option value='"+dictionary.getItemValue()+"'");
			if(isCheckedOrSelected(dictionary.getItemValue())){//是否选中
				selectHtml.append(" selected ");
			}
			selectHtml.append(">");
			selectHtml.append(dictionary.getItemName());	
			selectHtml.append("</option>");
		}
		selectHtml.append("</select>");
		return selectHtml.toString();
	}
	
	/**
	 * 获取radio的html代码
	 * @param obj
	 * @return
	 */
	private String getRadioHtml(List<Dictionary> dictionarys){
		StringBuffer radioHtml=new StringBuffer();
		//不勾选任何一个时候就相当于不限了，是否需要添加这个选项有待商议
		/*if(needAdditional){
			checkboxHtml.append("<input name='"+fieldName+"' type='checkbox' value='' checked='checked'/>");
			checkboxHtml.append("<span class='label'>"+additionalOption+"</span>");
		}	*/
		boolean flag=true;
		Dictionary dictionary=null;
		for(int index=0;index<dictionarys.size();index++){
			dictionary=dictionarys.get(index);
			radioHtml.append("<input name='"+fieldName+"' type='radio' value='");
			radioHtml.append(dictionary.getItemValue()+"'");
			if(flag&&isCheckedOrSelected(dictionary.getItemValue())){//是否选中
				radioHtml.append(" checked ");
				flag=false;
			}				
			setStyle2Html(radioHtml);//设置样式
			radioHtml.append("/>");			
			//添加样式
			radioHtml.append("<span class='"+cssclass+"'>"+dictionary.getItemName()+"</span>");
			if(limitCount>0&&dictionarys.size()>limitCount&&index!=0&&(index+1)%limitCount==0)
			{
				breakLine(radioHtml);
			}
		}
		return radioHtml.toString();
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
	private boolean isCheckedOrSelected(String val){
		boolean flag=false;
		List<String> listValues=null;
		String[] arrayValues=null;
		String value=null;
		if(null!=fieldValue&&fieldValue instanceof String){
			value=(String) fieldValue;
			if(value.contains(",")&&value.contains(val)){//以逗号分隔的数组
				flag=true;
			}
			else if(value!=null&&value.equalsIgnoreCase(val)){
				flag=true;
			}
		}else if(null!=fieldValue&&fieldValue instanceof List){
			listValues=(List<String>) fieldValue;
			if(null!=listValues&&listValues.contains(val)){//是否选中
				flag=true;
			}
		}
		else if(null!=fieldValue&&fieldValue instanceof String[]){
			arrayValues=(String[]) fieldValue;
			for (String temp:arrayValues) {
				if(temp.equalsIgnoreCase(val))
					return true;
			}
		}
		return flag;
	}

	public String getCssclass() {
		return cssclass;
	}

	public void setCssclass(String cssclass) {
		this.cssclass = cssclass;
	}
	/**
	 * 输出回车
	 * @param sb
	 */
	public void breakLine(StringBuffer sb){
		sb.append("<br/>");
		/*if(isIndent){
			sb.append("<span class='label'></span>");
		}*/
	}
}
