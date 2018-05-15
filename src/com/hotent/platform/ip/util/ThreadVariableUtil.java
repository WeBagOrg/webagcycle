package com.hotent.platform.ip.util;

import java.util.Map;

public class ThreadVariableUtil {
	private static ThreadLocal<Map<String,Object>> threadVariableMap=new ThreadLocal<Map<String,Object>>();

	public static Map<String, Object> getThreadVariableMap() {
		return threadVariableMap.get();
	}

	public static void setThreadVariableMap(Map<String, Object> map) {
		threadVariableMap.set(map);
	}
	
	public static void cleanVariable(){
		threadVariableMap.remove();
	}
}
