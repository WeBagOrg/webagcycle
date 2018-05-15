package com.hotent.platform.ip.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.hotent.platform.ip.util.PatentTypeEnum;


/**
 * 解析枚举类型的值，获取枚举值对应的参数
 * @author Administrator
 *
 */
public class EnumNameTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Class<?> enumClass;
	private String enumValue;
	public Class<?> getEnumClass() {
		return enumClass;
	}
	public void setEnumClass(Class<?> enumClass) {
		this.enumClass = enumClass;
	}
	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out=this.pageContext.getOut();
		String html="";
		if(null!=enumClass&&enumClass.equals(PatentTypeEnum.class)&&StringUtils.isNotEmpty(enumValue)){
			html=PatentTypeEnum.getName(enumValue);
		}
		/*if(null!=enumClass&&enumClass.isEnum()&&StringUtils.isNotEmpty(enumValue)){
			if(enumClass.equals(PatentStatusEnum.class)){
				html=PatentStatusEnum.getName(Integer.parseInt(enumValue));
			}
			else{
			if(enumClass.equals(PatentTypeEnum.class)){
				html=PatentTypeEnum.getTagName(enumValue);
			}}
		}*/
		//输出
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
}
