package com.hotent.core.web.security;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class ServicePassCipher {
	public static final String PRIVATE_KEY="tdmarco";
	
	public static String[] getMacAddress()throws Exception{
		String[] addr=new String[2];
		InetAddress address=InetAddress.getLocalHost();
		//主机名
		addr[0]=address.getHostName();
		
		byte[] mac=NetworkInterface.getByInetAddress(address).getHardwareAddress();
		//输出字符串
		StringBuffer sb = new StringBuffer("");

		for(int i=0; i<mac.length; i++) {
			//字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if(str.length()==1) {//格式化显示的位数
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
			sb.append("-");
		}
		addr[1]=sb.substring(0,sb.length()-1).toUpperCase();
		return addr;
	}
	/**
	 * 获取服务器端mac地址和计算机名
	 * 使用下划线分隔两个值
	 * @return
	 */
	public static String getMachineCode(){
		String code="";
		try {
			String[] temp=getMacAddress();
			if(null!=temp[0]&&null!=temp[1]){
				code=temp[0]+"_"+temp[1];
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return code;
	}
}
