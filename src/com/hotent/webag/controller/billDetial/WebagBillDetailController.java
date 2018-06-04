

package com.hotent.webag.controller.billDetial;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.webag.model.billDetial.WebagBillDetail;
import com.hotent.webag.service.billDetial.WebagBillDetailService;
import com.hotent.webag.until.GetWeChatOpenId;
import com.hotent.webag.until.getWechatId;
import install.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.platform.annotion.Action;
import org.springframework.web.bind.annotation.ResponseBody;
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
 * 对象功能:用户流水明细表 控制器类
 */
@Controller
@RequestMapping("/webag/billDetail/webagBillDetail/")
public class WebagBillDetailController extends BaseController
{
	@Resource
	private WebagBillDetailService webagBillDetailService;
	getWechatId getWechatId = new getWechatId();
	/**
	 * 添加或更新用户流水明细表。
	 * @param request
	 * @param response
	 * @param webagBillDetail 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新用户流水明细表")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagBillDetail webagBillDetail) throws Exception
	{
		String resultMsg=null;		
		try{
			if(webagBillDetail.getId()==null){
				webagBillDetailService.save(webagBillDetail);
				resultMsg=getText("添加","用户流水明细表");
			}else{
			    webagBillDetailService.save(webagBillDetail);
				resultMsg=getText("更新","用户流水明细表");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得用户流水明细表分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看用户流水明细表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<WebagBillDetail> list=webagBillDetailService.getAll(new QueryFilter(request,"webagBillDetailItem"));
		ModelAndView mv=this.getAutoView().addObject("webagBillDetailList",list);
		return mv;
	}
	
	/**
	 * 删除用户流水明细表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除用户流水明细表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagBillDetailService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除用户流水明细表成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑用户流水明细表
	 * @param request
	 *
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑用户流水明细表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		WebagBillDetail webagBillDetail=webagBillDetailService.getById(id);
		
		return getAutoView().addObject("webagBillDetail",webagBillDetail)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得用户流水明细表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看用户流水明细表明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagBillDetail webagBillDetail=webagBillDetailService.getById(id);
		return getAutoView().addObject("webagBillDetail", webagBillDetail);
	}

	/**
	 * 取得用户流水明细表明细(微信)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getByWechatId")
	@Action(description="查看用户流水明细表明细")
	public void getByWechatId(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");
		String wxCode = request.getParameter("code");
		String size = request.getParameter("length");
		//String userOpenId = getWechatId.getOpenId(wxCode);
		//String wechatid=RequestUtil.getString(request,"wechatid");
		List<WebagBillDetail> webagBillDetails=webagBillDetailService.getByWechatId("1",size);
		for (WebagBillDetail webagBillDetail:webagBillDetails) {
			String dateStr = myFmt.format(webagBillDetail.getCreatTime());
			System.out.print(dateStr);
			webagBillDetail.setShowDate(dateStr);
		}

		writeResultMessage(response.getWriter(), JsonUtils.toJson(webagBillDetails), ResultMessage.Success);
	}
	
}