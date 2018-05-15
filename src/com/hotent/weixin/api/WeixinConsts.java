package com.hotent.weixin.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.hotent.core.util.StringUtil;

public class WeixinConsts {
	
	public final static String METHOD_GET="GET";
	
	public final static String METHOD_POST="POST";

	/**
	 * 获取微信地址。
	 * @param corpId
	 * @param secret
	 * @return
	 */
	public static String getWxToken(String corpId,String secret){
		//https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
		return "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + secret;
		
	}
	
	//https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	
	/**
	 * 获取微信验证地址。
	 * @param appId
	 * @param redirectUrl
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getWxAuthorize(String appId,String redirectUrl) throws UnsupportedEncodingException{
		String redirect=URLEncoder.encode(redirectUrl, "utf-8");
		String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+redirect+"&response_type=code&scope=SCOPE&state=hotent#wechat_redirect";
		return url;
	}
	
	//
	/**
	 * 根据code获取用户信息。
	 * @param accessToken
	 * @param code
	 * @return
	 */
	public static String getWxUserInfo(String accessToken,String code){
		String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code=" + code;
		return url;
	}
	
	/**
	 * 当前url跳转地址。
	 * @param domain
	 * @param port
	 * @param appName
	 * @return
	 */
	public static String getAppUrl(String domain,String port,String  appName){
		String url="";
		if(StringUtil.isEmpty(appName)){
			if("80".equals(port)){
				url="http://" + domain + "/proxy";
			}
			else{
				url="http://" + domain +":" + port + "/proxy";
			}
		}
		else{
			if("80".equals(port)){
				url="http://" + domain + "/" + appName + "/proxy";
			}
			else{
				url="http://" + domain +":" + port + "/" + appName +"/proxy";
			}
		}
		return url;
		
	}
}
