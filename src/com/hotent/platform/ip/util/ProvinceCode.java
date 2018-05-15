package com.hotent.platform.ip.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 专利国省代码
 * @author Administrator
 *
 */
public class ProvinceCode {
	private static Map<String,String> proCode=new HashMap<String,String>();
	
	public ProvinceCode(){
		proCode.put("11", "北京");proCode.put("12", "天津");proCode.put("13", "河北");
		
	}
}
