

package com.hotent.webag.controller.userAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.webag.model.userAccount.WebagUserAccount;
import com.hotent.webag.service.userAccount.WebagUserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.platform.annotion.Action;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.util.BeanUtils;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.page.PageList;
import com.hotent.platform.model.system.SysUser;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.hotent.core.bpm.util.BpmUtil;
import net.sf.json.JSONObject;
import com.hotent.core.util.MapUtil;


import com.hotent.core.web.ResultMessage;
/**
 * 对象功能:用户余额表 控制器类
 */
@Controller
@RequestMapping("/webag/usesrAccount/webagUserAccount/")
public class WebagUserAccountController extends BaseController
{
	@Resource
	private WebagUserAccountService webagUserAccountService;
	
	/**
	 * 添加或更新用户余额表。
	 * @param request
	 * @param response
	 * @param webagUserAccount 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新用户余额表")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagUserAccount webagUserAccount) throws Exception
	{
		String resultMsg=null;		
		try{
			if(webagUserAccount.getId()==null){
				webagUserAccountService.save(webagUserAccount);
				resultMsg=getText("添加","用户余额表");
			}else{
			    webagUserAccountService.save(webagUserAccount);
				resultMsg=getText("更新","用户余额表");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得用户余额表分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看用户余额表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<WebagUserAccount> list=webagUserAccountService.getAll(new QueryFilter(request,"webagUserAccountItem"));
		ModelAndView mv=this.getAutoView().addObject("webagUserAccountList",list);
		return mv;
	}
	
	/**
	 * 删除用户余额表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除用户余额表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagUserAccountService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除用户余额表成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑用户余额表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑用户余额表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		WebagUserAccount webagUserAccount=webagUserAccountService.getById(id);
		
		return getAutoView().addObject("webagUserAccount",webagUserAccount)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得用户余额表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看用户余额表明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagUserAccount webagUserAccount=webagUserAccountService.getById(id);
		return getAutoView().addObject("webagUserAccount", webagUserAccount);
	}
	
}