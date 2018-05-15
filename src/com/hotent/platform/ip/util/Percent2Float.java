package com.hotent.platform.ip.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
/**
 * /^(100|[1-9]?\d)%$/      // 0% 到 100% 不能有小数
 * /^(100|[1-9]?\d(\.\d\d?)?)%$/    // 0% 到 100% 可有小数 (5.2%  55.55%) 
 * @author Administrator
 *
 */
public class Percent2Float implements Converter<String, Float>{
	Pattern pattern = Pattern.compile("^(100|[1-9]?\\d)%$");  
	@Override
	public Float convert(String percent) {
		if(StringUtils.isNotEmpty(percent)){
			Matcher matcher = pattern.matcher(percent);  
	        if(matcher.matches()) {  
	            //如果匹配 进行转换  
	            Float f=new Float(matcher.group(1));
	            return f/100;
	        } else {  
	            //如果不匹配 转换失败  
	            //throw new IllegalArgumentException(String.format("类型转换失败，需要格式[75\\%]，但格式是[%s]", percent));
	        	return Float.parseFloat(percent);
	        }  
		}
		return null;
	}

}
