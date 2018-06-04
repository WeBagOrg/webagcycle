package com.hotent.webag.service.bagInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hotent.webag.until.GetWeChatOpenId;
import com.hotent.webag.until.PayUtil;
import com.hotent.webag.until.XMLUtil;
import com.hotent.webag.vo.AppletPayVo;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fengqian
 * date 2018/5/17 15:34
 */
@Service
public class WebagService {

    private static Logger log = Logger.getLogger(WebagService.class);
    private static ResourceBundle resource = ResourceBundle.getBundle("com/hotent/webag/config/weixin");


    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 小程序调用wx.login返回的code
     * @return
     */
    public JSONObject getSessionKeyOropenid(String code) {
        String requestUrl = resource.getString("url");
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", resource.getString("appId"));
        log.info("appid："+resource.getString("appId"));
        requestUrlParam.put("secret", resource.getString("appSecret"));
        log.info("secret："+resource.getString("appSecret"));
        requestUrlParam.put("js_code", code);
        log.info("js_code："+code);
        requestUrlParam.put("grant_type", resource.getString("grant_type")); //默认参数
        log.info("grant_type："+resource.getString("grant_type"));
        //发送post请求获取openid
        JSONObject jsonObject = JSON.parseObject(GetWeChatOpenId.sendPost(requestUrl, requestUrlParam));
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
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 企业付款
     * @param userId
     * @param amount
     * @param ip
     * @return
     * @throws Exception
     *
     */
    public Map<String, String> appletPay(String userId, Integer amount,String ip) throws Exception{

        AppletPayVo appletPayVo=new AppletPayVo();
        //根据userId获取openid
        String openId="";
        appletPayVo.setOpenid(openId);
        appletPayVo.setAmount(amount.intValue());
        appletPayVo.setSpbill_create_ip(ip);
        appletPayVo.setMch_appid(resource.getString("appId"));// 自己的公众账号
        appletPayVo.setMchid(resource.getString("partnerId"));//自己的 商户号
        appletPayVo.setNonce_str(getRandomStr());// 随机字符串
        appletPayVo.setCheck_name("NO_CHECK");// 校验用户姓名选项
        appletPayVo.setDesc("微信企业付款");// 企业付款描述信息
        appletPayVo.setPartner_trade_no(getPartnerTradeNo());// 商户订单号

        String sign = PayUtil.createOrderSign(appletPayVo);
        appletPayVo.setSign(sign);

        XMLUtil xmlUtil=new XMLUtil();
        xmlUtil.xstream().alias("xml", appletPayVo.getClass());
        String xml = xmlUtil.xstream().toXML(appletPayVo);
        String response = PayUtil.ssl(resource.getString("appletPayUrl"),xml);
        Map<String, String> responseMap = xmlUtil.parseXml(response);
        return responseMap;
    }

    /**
     * 生成随机字符串
     * */
    private String getRandomStr(){
        return UUID.randomUUID().toString().substring(0, 30);
    }

    /**
     * 生成商户订单号
     * */
    private String getPartnerTradeNo(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        Random ne=new Random();
        int guid=ne.nextInt(9999-1000+1)+1000;
        return resource.getString("partnerId")+time+guid;
    }

}

