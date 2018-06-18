package com.hotent.webag.until;

import com.hotent.webag.vo.AppletPayVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.util.ResourceBundle;

/**
 * @author fengqian
 * date 2018/5/17 15:00
 */
public class PayUtil {

    private static ResourceBundle resource = ResourceBundle.getBundle("com/hotent/webag/config/weixin");

    public static String createOrderSign(AppletPayVo appletPayVo){

        StringBuffer sign = new StringBuffer();
        sign.append("amount=").append(appletPayVo.getAmount());
        sign.append("&check_name=").append(appletPayVo.getCheck_name());
        sign.append("&desc=").append(appletPayVo.getDesc());
        sign.append("&mch_appid=").append(appletPayVo.getMch_appid());
        sign.append("&mchid=").append(appletPayVo.getMchid());
        sign.append("&nonce_str=").append(appletPayVo.getNonce_str());
        sign.append("&openid=").append(appletPayVo.getOpenid());
        sign.append("&partner_trade_no=").append(appletPayVo.getPartner_trade_no());
        sign.append("&spbill_create_ip=").append(appletPayVo.getSpbill_create_ip());
        sign.append("&key=").append(resource.getString("partnerKey"));
        return  DigestUtils.md5Hex(sign.toString()).toUpperCase();
    }

    public static String ssl(String url,String data){
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File("D:/cert/apiclient_cert.p12"));
         //   keyStore.load(instream, resource.getString("partnerId").toCharArray());
            keyStore.load(instream, "1501967551".toCharArray());
            // Trust own CA and all self-signed certs
//            SSLContext sslcontext = SSLContexts.custom()
//                    .loadKeyMaterial(keyStore, resource.getString("partnerId").toCharArray())
//                    .build();
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore,"1501967551".toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpost = new HttpPost(url);

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            //System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                //System.out.println("----------------------------------------");
                //System.out.println(response.getStatusLine());
                if (entity != null) {
                    //System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return message.toString();
    }

}
