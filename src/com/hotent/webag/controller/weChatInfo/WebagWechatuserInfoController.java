

package com.hotent.webag.controller.weChatInfo;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import install.util.JsonUtils;
import net.sf.json.JSONObject;
import com.hotent.webag.model.wechatInfo.WebagWechatuserInfo;
import com.hotent.webag.service.weChatInfo.WebagWechatuserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.platform.annotion.Action;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;


import com.hotent.core.web.ResultMessage;
/**
 * 对象功能:微信用户表 控制器类
 */
@Controller
@RequestMapping("/webag/wechatuserInfo/")
public class WebagWechatuserInfoController extends BaseController
{
	@Resource
	private WebagWechatuserInfoService webagWechatuserInfoService;

	/**
	 * 获取用户sessionkey和openid
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getSessionKeyOropenid",method= RequestMethod.POST)
	@Action(description = "获取用户sessionkey和openid")
	public void getSessionKeyOropenid(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String resultMsg=null;
		JSONObject jsonObject = null;
		try {
			String code = request.getParameter("code");
			jsonObject = webagWechatuserInfoService.getSessionKeyOropenid(code);
			System.out.println(jsonObject);
			logger.info("getSessionKeyOropenid返回结果："+jsonObject);
			//对返回结果进行解析，调用getUserInfo
//			if(jsonObject!=null&&jsonObject.get("session_key")!=null){
//				String openId=jsonObject.getString("openid");
//				String sessionKey=jsonObject.getString("session_key");
//				String encryptedData = request.getParameter("encryptedData");
//				String iv = request.getParameter("iv");
//				jsonObject = webagWechatuserInfoService.getUserInfo(encryptedData,sessionKey,iv);
//				logger.info("getUserInfo返回结果："+jsonObject);
//			}else{
//				logger.info("获取用户code失败");
//			}
			writeResultMessage(response.getWriter(), JsonUtils.toJson(jsonObject), ResultMessage.Success);
		} catch (Exception e) {
			logger.info("获取用户信息失败");
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 添加或更新微信用户表。
	 * @param request
	 * @param response
	 * @param webagWechatuserInfo 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新微信用户表")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagWechatuserInfo webagWechatuserInfo) throws Exception
	{
		String resultMsg=null;		
		try{
			if(webagWechatuserInfo.getId()==null){
				webagWechatuserInfoService.save(webagWechatuserInfo);
				resultMsg=getText("添加","微信用户表");
			}else{
			    webagWechatuserInfoService.save(webagWechatuserInfo);
				resultMsg=getText("更新","微信用户表");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得微信用户表分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看微信用户表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<WebagWechatuserInfo> list=webagWechatuserInfoService.getAll(new QueryFilter(request,"webagWechatuserInfoItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWechatuserInfoList",list);
		return mv;
	}
	
	/**
	 * 删除微信用户表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除微信用户表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagWechatuserInfoService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除微信用户表成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑微信用户表
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑微信用户表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		WebagWechatuserInfo webagWechatuserInfo=webagWechatuserInfoService.getById(id);
		
		return getAutoView().addObject("webagWechatuserInfo",webagWechatuserInfo)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得微信用户表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看微信用户表明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagWechatuserInfo webagWechatuserInfo=webagWechatuserInfoService.getById(id);
		return getAutoView().addObject("webagWechatuserInfo", webagWechatuserInfo);
	}

	
}