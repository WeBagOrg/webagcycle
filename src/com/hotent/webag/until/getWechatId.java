package com.hotent.webag.until;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class getWechatId {
    public String getOpenId(String code){
        String requestUrl ="https://api.weixin.qq.com/sns/jscode2session";//请求地址 https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", "wx9750eb4c1a8b4073");//开发者设置中的appId
        requestUrlParam.put("secret", "67e39634565bf07bb2b411ff03ef4f7a"); //开发者设置中的appSecret
        requestUrlParam.put("js_code", code); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");//默认参数 authorization_code
        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject =JSONObject.fromObject(GetWeChatOpenId.sendPost(requestUrl, requestUrlParam)) ;
        //将用户微信id和袋子做绑定
        String userOpenId = jsonObject.get("openId").toString();
        return userOpenId;
    }
}
