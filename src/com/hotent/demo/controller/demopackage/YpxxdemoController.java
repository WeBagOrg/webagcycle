

package com.hotent.demo.controller.demopackage;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.hotent.demo.model.demopackage.Ypxxdemo;
import com.hotent.demo.service.demopackage.YpxxdemoService;
import com.hotent.core.web.ResultMessage;
/**
 * 对象功能:ypxxdemo 控制器类
 */
@Controller
@RequestMapping("/demo/demopackage/ypxxdemo/")
public class YpxxdemoController extends BaseController
{
	@Resource
	private YpxxdemoService ypxxdemoService;
	
	/**
	 * 添加或更新ypxxdemo。
	 * @param request
	 * @param response
	 * @param ypxxdemo 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新ypxxdemo")
	public void save(HttpServletRequest request, HttpServletResponse response,Ypxxdemo ypxxdemo) throws Exception
	{
		String resultMsg=null;		
		try{
			if(ypxxdemo.getId()==null){
				ypxxdemoService.save(ypxxdemo);
				resultMsg=getText("添加","ypxxdemo");
			}else{
			    ypxxdemoService.save(ypxxdemo);
				resultMsg=getText("更新","ypxxdemo");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得ypxxdemo分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看ypxxdemo分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<Ypxxdemo> list=ypxxdemoService.getAll(new QueryFilter(request,"ypxxdemoItem"));
		ModelAndView mv=this.getAutoView().addObject("ypxxdemoList",list);
		return mv;
	}
	
	/**
	 * 删除ypxxdemo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除ypxxdemo")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			ypxxdemoService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除ypxxdemo成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑ypxxdemo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑ypxxdemo")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		String returnUrl=RequestUtil.getPrePage(request);
		Ypxxdemo ypxxdemo=ypxxdemoService.getById(id);
		
		return getAutoView().addObject("ypxxdemo",ypxxdemo)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得ypxxdemo明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看ypxxdemo明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		Ypxxdemo ypxxdemo=ypxxdemoService.getById(id);
		return getAutoView().addObject("ypxxdemo", ypxxdemo);
	}
	
}