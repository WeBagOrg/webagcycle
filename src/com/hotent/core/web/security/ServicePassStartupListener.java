package com.hotent.core.web.security;

import javax.servlet.ServletContextEvent;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.context.ContextLoaderListener;

public class ServicePassStartupListener extends ContextLoaderListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		//判断服务端的密码
		try {
			String[] addr=ServicePassCipher.getMacAddress();
			String cipher=DigestUtils.md5Hex(addr[0]+addr[1]+ServicePassCipher.PRIVATE_KEY);
			if(!"5d4a6a8a2bc98611d3a4ad90c28c7505".equals(cipher)){
				throw new Exception("The Service password is valid!!!");
			}
		} catch (Exception e) {
			//throw new Exception("The Service password is valid!!!");
			System.out.println("The Service password is valid!!!");
			super.contextDestroyed(event);
		}
	}
}
