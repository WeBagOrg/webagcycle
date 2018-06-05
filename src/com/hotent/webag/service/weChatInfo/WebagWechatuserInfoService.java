package com.hotent.webag.service.weChatInfo;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.hotent.webag.dao.weChatInfo.WebagWechatuserInfoDao;
import com.hotent.webag.model.wechatInfo.WebagWechatuserInfo;
import com.hotent.webag.until.GetWeChatOpenId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;
import org.springframework.stereotype.Service;
import com.hotent.core.db.IEntityDao;
import com.hotent.core.util.BeanUtils;

import com.hotent.core.util.UniqueIdUtil;

import net.sf.json.util.JSONUtils;
import net.sf.ezmorph.object.DateMorpher;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.core.util.StringUtil;
import net.sf.json.JSONObject;


import com.hotent.core.service.BaseService;


@Service
public class WebagWechatuserInfoService extends BaseService<WebagWechatuserInfo>
{
	private static ResourceBundle resource = ResourceBundle.getBundle("com/hotent/webag/config/weixin");

	@Resource
	private WebagWechatuserInfoDao dao;
	
	public WebagWechatuserInfoService()
	{
	}

	/**
	 * 获取微信小程序 session_key 和 openid
	 *
	 * @param code 小程序调用wx.login返回的code
	 * @return
	 */
	public JSONObject getSessionKeyOropenid(String code) throws Exception{
		String requestUrl = resource.getString("url");
		Map<String, String> requestUrlParam = new HashMap<String, String>();
		requestUrlParam.put("appid", resource.getString("appId"));
		logger.info("appid："+resource.getString("appId"));
		requestUrlParam.put("secret", resource.getString("appSecret"));
		logger.info("secret："+resource.getString("appSecret"));
		requestUrlParam.put("js_code", code);
		logger.info("js_code："+code);
		requestUrlParam.put("grant_type", resource.getString("grant_type")); //默认参数
		logger.info("grant_type："+resource.getString("grant_type"));
		//发送post请求获取openid
		JSONObject jsonObject = JSONObject.fromObject(GetWeChatOpenId.sendPost(requestUrl, requestUrlParam));
		//保存openid
		WebagWechatuserInfo userInfo=new WebagWechatuserInfo();
		userInfo.setOpenId(jsonObject.getString("openid"));
		userInfo.setCreateTime(new Date());
		this.save(userInfo);
		return jsonObject;
	}

	/**
	 * 解密用户敏感数据获取用户信息
	 *
	 * @param sessionKey    数据进行加密签名的密钥
	 * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
	 * @param iv            加密算法的初始向量
	 * @return
	 */
	public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);
		// 偏移量
		byte[] ivByte = Base64.decode(iv);
		try {
			// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			} // 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return JSONObject.fromObject(result);
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidParameterSpecException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e.getMessage(), e);
		} catch (BadPaddingException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	@Override
	protected IEntityDao<WebagWechatuserInfo,Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 流程处理器方法 用于处理业务数据
	 * @param cmd
	 * @throws Exception
	 */
	public void processHandler(ProcessCmd cmd)throws Exception{
		Map data=cmd.getFormDataMap();
		if(BeanUtils.isNotEmpty(data)){
			String json=data.get("json").toString();
			WebagWechatuserInfo webagWechatuserInfo=getWebagWechatuserInfo(json);
			if(StringUtil.isEmpty(cmd.getBusinessKey())){
				Long genId=UniqueIdUtil.genId();
				webagWechatuserInfo.setId(genId);
				this.add(webagWechatuserInfo);
			}else{
				webagWechatuserInfo.setId(Long.parseLong(cmd.getBusinessKey()));
				this.update(webagWechatuserInfo);
			}
			cmd.setBusinessKey(webagWechatuserInfo.getId().toString());
		}
	}
	
	/**
	 * 根据json字符串获取WebagWechatuserInfo对象
	 * @param json
	 * @return
	 */
	public WebagWechatuserInfo getWebagWechatuserInfo(String json){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
		if(StringUtil.isEmpty(json))return null;
		JSONObject obj = JSONObject.fromObject(json);
		WebagWechatuserInfo webagWechatuserInfo = (WebagWechatuserInfo)JSONObject.toBean(obj, WebagWechatuserInfo.class);
		return webagWechatuserInfo;
	}
	/**
	 * 保存 微信用户表 信息
	 * @param webagWechatuserInfo
	 */

	public void save(WebagWechatuserInfo webagWechatuserInfo) throws Exception{
		Long id=webagWechatuserInfo.getId();
		if(id==null || id==0){
			id=UniqueIdUtil.genId();
			webagWechatuserInfo.setId(id);
		    this.add(webagWechatuserInfo);
		}
		else{
		   WebagWechatuserInfo webagWechatuserInfoTemd = getById(webagWechatuserInfo.getId());
		   if(webagWechatuserInfoTemd==null || webagWechatuserInfoTemd.equals("")){
		       this.add(webagWechatuserInfo);
		   }else{
		       this.update(webagWechatuserInfo);
		   }
		}
	}
}
