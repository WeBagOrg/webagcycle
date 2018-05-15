package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CheckTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	private String field;
	private Object fieldValues;//string，array，list三种情况
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Object fieldValues) {
		this.fieldValues = fieldValues;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		String html="";
		if(isChecked()){
			html="checked";
		}
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 判断是否选中
	 * @param val
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isChecked(){
		boolean flag=false;
		List<String> listValues=null;
		String[] arrayValues=null;
		String value=null;
		if(null!=fieldValues&&fieldValues instanceof String){
			value=(String) fieldValues;
			if(value.contains(",")&&value.contains(field)){//以逗号分隔的数组
				flag=true;
			}
			else if(value!=null&&value.equals(field)){
				flag=true;
			}
		}else if(null!=fieldValues&&fieldValues instanceof List){
			listValues=(List<String>) fieldValues;
			if(null!=listValues&&listValues.contains(field)){//是否选中
				flag=true;
			}
		}
		else if(null!=fieldValues&&fieldValues instanceof String[]){
			arrayValues=(String[]) fieldValues;
			for (String temp:arrayValues) {
				if(temp.equalsIgnoreCase(field))
					return true;
			}
		}
		return flag;
	}
}
