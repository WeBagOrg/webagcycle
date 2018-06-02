package com.hotent.webag.controller.bagInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hotent.core.web.controller.BaseController;
import com.hotent.platform.annotion.Action;
import com.hotent.webag.service.bagInfo.WebagService;
import com.hotent.webag.until.IpAddressUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fengqian
 * date 2018/5/23 8:52
 */
@Controller
@RequestMapping("/webag/")
public class WebagController extends BaseController {

    private static Logger logger = Logger.getLogger(WebagController.class);

    @Resource
    private WebagService webagService;

    /**
     * 获取用户sessionkey和openid
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getSessionKeyOropenid",method= RequestMethod.POST)
    @Action(description = "获取用户sessionkey和openid")
    @ResponseBody
    public JSONObject getSessionKeyOropenid(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = null;
        try {
            String code = request.getParameter("code");
            jsonObject = webagService.getSessionKeyOropenid(code);
            logger.info("getSessionKeyOropenid返回结果："+jsonObject);
            //对返回结果进行解析，调用getUserInfo
            if(jsonObject!=null){
                String openId=jsonObject.getString("openid");
                String sessionKey=jsonObject.getString("session_key");
                String encryptedData = request.getParameter("encryptedData");
                String iv = request.getParameter("iv");
                jsonObject = webagService.getUserInfo(encryptedData,sessionKey,iv);
                logger.info("getUserInfo返回结果："+jsonObject);
            }
        } catch (Exception e) {
            logger.info("获取用户信息失败");
            logger.error(e.getMessage(),e);
        }
        return jsonObject;
    }
    /**
     * 获取用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getUserInfo",method= RequestMethod.POST)
    @Action(description = "获取用户信息")
    @ResponseBody
    public JSONObject getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = null;
        try {
            String encryptedData = request.getParameter("encryptedData");
            String sessionKey = request.getParameter("sessionKey");
            String iv = request.getParameter("iv");
            jsonObject = webagService.getUserInfo(encryptedData,sessionKey,iv);
            logger.info("getUserInfo返回结果："+jsonObject);
        } catch (Exception e) {
            logger.info("获取用户信息失败");
            logger.error(e.getMessage(),e);
        }
        return jsonObject;
    }
    /**
     * 企业付款
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "appletPay",method= RequestMethod.POST)
    @Action(description = "企业付款")
    @ResponseBody
    public JSONObject appletPay(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = null;
        try {
            String userId = request.getParameter("userId");//用户ID，
            Integer amount = Integer.parseInt(request.getParameter("amount"));//金额，以分为单位
            Map<String, String> map = webagService.appletPay(userId,amount, IpAddressUtil.getIpAddress(request));
            logger.info("getUserInfo返回结果："+map);
            jsonObject= JSON.parseObject(map.toString());
            logger.info("转换之后的json返回："+jsonObject);
        } catch (Exception e) {
            logger.info("获取用户信息失败");
            logger.error(e.getMessage(),e);
        }
        return jsonObject;
    }
}
