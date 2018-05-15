package com.hotent.platform.ip.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.hotent.core.util.AppUtil;
import com.hotent.platform.dao.system.DictionaryDao;
import com.hotent.platform.model.system.Dictionary;
/**
 * 根据数据字典的值获取数据字典的名称
 * @author ipph
 *
 */
public class DicNameTag extends TagSupport{
	private String nodeKey;//字典的key
	private String dicValue;//值
	private List<String> listValue;//多值
	private boolean isStrArray;//是否是数组，以逗号分隔的字符串
	public String getNodeKey() {
		return nodeKey;
	}
	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}
	public String getDicValue() {
		return dicValue;
	}
	public void setDicValue(String dicValue) {
		this.dicValue = dicValue;
	}
	public List<String> getListValue() {
		return listValue;
	}
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}
	public boolean isStrArray() {
		return isStrArray;
	}
	public void setStrArray(boolean isStrArray) {
		this.isStrArray = isStrArray;
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		String html=getDicName();
		try {
			out.print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return TagSupport.SKIP_BODY;
	}
	/**
	 * 获取数据字典对应的名称
	 * @return
	 */
	private String getDicName(){
		DictionaryDao dictionaryDao=(DictionaryDao) AppUtil.getBean(DictionaryDao.class);
		if(null!=dictionaryDao){
			Map<String, String> queryMap=new HashMap<String, String>();
			queryMap.put("catKey", "DIC");
			queryMap.put("nodeKey",nodeKey.toUpperCase());
			List<Dictionary> dictionarys=dictionaryDao.getDictionaryByNodeKey(queryMap);
			if(null!=dictionarys&&dictionarys.size()>0){
				if(StringUtils.isNotEmpty(dicValue)){
					if(isStrArray&&dicValue.contains(",")){
						return getDicNameByArrayString(dictionarys);
					}
					else{
						return getDicNameByValue(dictionarys);
					}
				}
				else if(listValue!=null&&listValue.size()>0){
					return getDicNameByList(dictionarys);
				}
			}
		}
		return "";
	}
	/**
	 * 通过单值获取显示名称
	 * @param dictionaryDao
	 * @return
	 */
	private String getDicNameByValue(List<Dictionary> dictionarys){
		for(Dictionary dictionary:dictionarys){
			if(StringUtils.isNotEmpty(dicValue)&&dictionary.getItemValue().equals(dicValue)){
				return dictionary.getItemName();
			}
		}
		return "";
	}
	
	/**
	 * 通过List<String>对象获取显示的名称
	 * @param dictionaryDao
	 * @return
	 */
	private String getDicNameByList(List<Dictionary> dictionarys){
		StringBuffer sb=new StringBuffer();
		for(Dictionary dictionary:dictionarys){
			for(String value:listValue){
				if(StringUtils.isNotEmpty(value)&&dictionary.getItemValue().equals(value)){
					sb.append(dictionary.getItemName()+",");
				}
			}
		}
		if(sb.length()>0){
			return sb.substring(0,sb.length()-1);
		}
		return "";
	}
	/**
	 * 
	 * @param dictionarys
	 * @return
	 */
	private String getDicNameByArrayString(List<Dictionary> dictionarys){
		StringBuffer sb=new StringBuffer();
		String[] temp=dicValue.split(",");
		if(null!=temp&&temp.length>0){
			for(Dictionary dictionary:dictionarys){
				for(String value:temp){
					if(StringUtils.isNotEmpty(value)&&dictionary.getItemValue().equals(value)){
						sb.append(dictionary.getItemName()+",");
					}
				}
			}
		}
		if(sb.length()>0){
			return sb.substring(0,sb.length()-1);
		}
		return "";
	}
}
