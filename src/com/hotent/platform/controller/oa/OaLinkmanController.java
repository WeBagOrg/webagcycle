package com.hotent.platform.controller.oa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.annotion.Action;
import com.hotent.platform.model.oa.OaLinkman;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.oa.OaLinkmanService;
import com.hotent.platform.service.system.SysUserService;
/**
 *<pre>
 * 对象功能:联系人 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:ray
 * 创建时间:2015-07-14 09:13:58
 *</pre>
 */
@Controller
@RequestMapping("/platform/oa/oaLinkman/")
public class OaLinkmanController extends BaseController
{
	@Resource
	private OaLinkmanService oaLinkmanService;
	@Resource
	private SysUserService sysUserservice;
	
	/**
	 * 添加或更新联系人。
	 * @param request
	 * @param response
	 * @param oaLinkman 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更联系人表")
	public void save(HttpServletRequest request, HttpServletResponse response,OaLinkman oaLinkman) throws Exception
	{
		String resultMsg=null;		
		try{
			if(oaLinkman.getId()==null||oaLinkman.getId()==0){
				oaLinkmanService.save(oaLinkman);
				resultMsg=getText("添加","添加联系人成功");
			}else{
			    oaLinkmanService.save(oaLinkman);
				resultMsg=getText("更新","更新联系人成功");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得联系人分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看联系人分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		Long id = ContextUtil.getCurrentUserId();
		List<OaLinkman> list=oaLinkmanService.getByUserId(new QueryFilter(request,"oaLinkmanItem"),id);
		ModelAndView mv=this.getAutoView().addObject("oaLinkmanList",list);
		return mv;
	}
	
	/**
	 * 删除联系人
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除联系人")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			oaLinkmanService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除联系人成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑联系人
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑联系人")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		OaLinkman oaLinkman=oaLinkmanService.getById(id);
		
		return getAutoView().addObject("oaLinkman",oaLinkman)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得联系人明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看联系人明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		OaLinkman oaLinkman = oaLinkmanService.getById(id);	
		return getAutoView().addObject("oaLinkman", oaLinkman);
	}
	/**
	 * 取得联系人明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	@Action(description="联系人选择框")
	public ModelAndView selector(HttpServletRequest request, HttpServletResponse response) throws Exception
	{  
		Long id = ContextUtil.getCurrentUserId();
		List<OaLinkman> list =  oaLinkmanService.getSelectorList(new QueryFilter(request,"oaLinkmanItem"),id);
		ModelAndView mv=this.getAutoView().addObject("oaLinkmanList",list);
		return mv;
	}
	
	/**
	 * 判断是否存在该联系人
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("checkLinkMan")
	@Action(description="判断是否存在该联系人")
	@ResponseBody
	public String checkLinkMan(HttpServletRequest request, HttpServletResponse response) throws Exception
	{  
		Long userId = ContextUtil.getCurrentUserId();
		Map<String,String> linkManMap = oaLinkmanService.getLinkManMap(userId);
		String addressData = RequestUtil.getString(request,"addressData");
		if(addressData == null || addressData.equals("")){
			return null;
		}
		List<OaLinkman> linkManList = new ArrayList<OaLinkman>();
		String[] arryEmailData = null;
		if(addressData.indexOf(",")> -1){
			arryEmailData = addressData.split(",");
			for(int i = 0 ; i < arryEmailData.length ; i++){
				List<SysUser> userList =sysUserservice.findLinkMan((arryEmailData[i]),userId);
				if(!(linkManMap.containsValue(arryEmailData[i])) && userList.size() == 0){
					OaLinkman linkMan = new OaLinkman();
					linkMan.setEmail(arryEmailData[i]);
					linkManList.add(linkMan);
				}
			}
		}else{
			List<SysUser> userList =sysUserservice.findLinkMan(addressData,userId);
			if(!(linkManMap.containsValue(addressData)) && userList.size() == 0){
				OaLinkman linkMan = new OaLinkman();
				linkMan.setEmail(addressData);
				linkManList.add(linkMan);
			}
		}
		String jsonData = JSONArray.fromObject(linkManList).toString(); 
		return jsonData;
	}
	/**
	 * 添加新联系人
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addNew")
	@Action(description="添加新联系人")
	@ResponseBody
	public void addNew(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String linkManData=RequestUtil.getString(request,"linkManData");
		String arryStr[] = linkManData.split(",");
		for(int i = 0 ; i < arryStr.length ; i++){
			OaLinkman oaLinkMan = new OaLinkman();
			oaLinkMan.setEmail(arryStr[i]);
			oaLinkMan.setName("新建联系人"+arryStr[i]);
			oaLinkMan.setUserid(ContextUtil.getCurrentUserId());
			oaLinkMan.setStatus(1);
			oaLinkmanService.save(oaLinkMan);
		}
	}

}
