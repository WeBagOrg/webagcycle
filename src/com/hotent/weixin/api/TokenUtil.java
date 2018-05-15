package com.hotent.weixin.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.core.api.util.PropertyUtil;
import com.hotent.core.util.HttpUtil;
import com.hotent.weixin.model.TokenModel;

import net.sf.json.JSONObject;


/**
 * 获取token工具类。
 * @author ray
 *
 */
public class TokenUtil {
	
	protected static Logger log = LoggerFactory.getLogger(TokenUtil.class);
	
	private static TokenModel model=new TokenModel();
	
	/**
	 * 获取企业token。
	 * @param corpId		企业ID
	 * @param secret		管理组密码
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	public static String getWxToken(String corpId,String secret) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException{
		if(!model.isInit()){
			String token=getToken(corpId,secret);
			return token;
		}
		else{
			if(model.isExpire()){
				String token=getToken(corpId,secret);
				return token;
			}
			else{
				return model.getToken();
			}
		}
	}
	
	/**
	 * 获取token
	 * @return
	 */
	public static String getWxToken() {
		try{
			String corpId=PropertyUtil.getByAlias("corpId");
			String secret=PropertyUtil.getByAlias("secret");
			return getWxToken(corpId, secret);
		}
		catch(Exception ex){
			log.error(ex.getMessage());
			return "-2";
		}
		
	}
	
	/**
	 * 获取token。
	 * @param corpId
	 * @param secret
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	private static String getToken(String corpId,String secret) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException{
		String url=WeixinConsts.getWxToken(corpId, secret);
		String rtn=HttpUtil.sendHttpsRequest(url, "", WeixinConsts.METHOD_GET);
		//取到了
		if(rtn.indexOf("errcode")==-1){
			JSONObject jsonObj=JSONObject.fromObject(rtn);
			String token=jsonObj.getString("access_token");
			int expireIn=jsonObj.getInt("expires_in");
			model.setToken(token, expireIn);
			return token;
		}
		//获取失败
		else{
			JSONObject jsonObj=JSONObject.fromObject(rtn);
			String errMsg=jsonObj.getString("errmsg");
			log.error(errMsg);
			return "-1";
		}
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String str=getWxToken("wxc6e093e6b81b98d1","a7UyXEeLTIZw6Dp65uVIRZAjv6UKQ_2mKq3E7YHNHn-A_aeeRJ2osmOsHSbSFTVE");
		System.out.println(str);
	}
}
