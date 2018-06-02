

package com.hotent.webag.controller.wasteType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.webag.model.wasteType.WebagWasteTypeApp;
import install.util.JsonUtils;
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

import com.hotent.webag.model.wasteType.WebagWasteType;
import com.hotent.webag.service.wasteType.WebagWasteTypeService;
import com.hotent.core.web.ResultMessage;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.platform.service.system.IdentityService;
import com.hotent.core.util.StringUtil;
import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.service.bpm.ProcessRunService;
/**
 * 对象功能:webag_waste_type 控制器类
 */
@Controller
@RequestMapping("/webag/wasteType/webagWasteType/")
public class WebagWasteTypeController extends BaseController
{
	@Resource
	private WebagWasteTypeService webagWasteTypeService;
	@Resource
	private GroovyScriptEngine engine;
	@Resource
	private IdentityService identityService;
	@Resource
	private ProcessRunService processRunService;
	
	/**
	 * 添加或更新webag_waste_type。
	 * @param request
	 * @param response
	 * @param webagWasteType 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新webag_waste_type")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagWasteType webagWasteType) throws Exception
	{
		String resultMsg=null;		
		try{
			if(webagWasteType.getId()==null){
				webagWasteTypeService.save(webagWasteType);
				resultMsg=getText("添加","webag_waste_type");
			}else{
			    webagWasteTypeService.save(webagWasteType);
				resultMsg=getText("更新","webag_waste_type");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}

	/**
	 * 取得webag_waste_type列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllWasteTypeApp")
	@Action(description="查看webag_waste_type分页列表")
	public void getAllWasteTypeApp(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		List<WebagWasteType> list=webagWasteTypeService.getAll();
		List<WebagWasteTypeApp> newList= new ArrayList<WebagWasteTypeApp>();
		for (WebagWasteType e: list) {
			WebagWasteTypeApp webagWasteTypeApp = new WebagWasteTypeApp();
			webagWasteTypeApp.setWasteTypeNo(e.getWasteTypeNo());
			webagWasteTypeApp.setWasteTypeName(e.getWasteTypeName());
			webagWasteTypeApp.setWasteTypePrice(e.getWasteTypePrice());
			newList.add(webagWasteTypeApp);
		}
		writeResultMessage(response.getWriter(), JsonUtils.toJson(newList), ResultMessage.Success);
	}
	
	/**
	 * 取得webag_waste_type分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看webag_waste_type分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<WebagWasteType> list=webagWasteTypeService.getAll(new QueryFilter(request,"webagWasteTypeItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWasteTypeList",list);
		return mv;
	}
	
	/**
	 * 删除webag_waste_type
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除webag_waste_type")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagWasteTypeService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除webag_waste_type成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑webag_waste_type
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑webag_waste_type")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		Long runId=0L;
		ProcessRun processRun=processRunService.getByBusinessKey(id.toString());
		if(BeanUtils.isNotEmpty(processRun)){
			runId=processRun.getRunId();
		}
		String returnUrl=RequestUtil.getPrePage(request);
		WebagWasteType webagWasteType=webagWasteTypeService.getById(id);
		if(BeanUtils.isEmpty(webagWasteType)){
			webagWasteType=new WebagWasteType();
			String wasteTypeNo_id=identityService.nextId("fplxbh");
			webagWasteType.setWasteTypeNo(wasteTypeNo_id);
			String creatorId_script="return scriptImpl.getCurrentUserId();";
			webagWasteType.setCreatorId((Long) engine.executeObject(creatorId_script, null));
		}
		
		return getAutoView().addObject("webagWasteType",webagWasteType)
							.addObject("runId", runId)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得webag_waste_type明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看webag_waste_type明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagWasteType webagWasteType=webagWasteTypeService.getById(id);
		Long runId=0L;
		ProcessRun processRun=processRunService.getByBusinessKey(id.toString());
		if(BeanUtils.isNotEmpty(processRun)){
			runId=processRun.getRunId();
		}
		return getAutoView().addObject("webagWasteType", webagWasteType).addObject("runId", runId);
	}
	
	/**
	 * 流程url表单 绑定的表单明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detail")
	@Action(description="表单明细")
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagWasteType webagWasteType=webagWasteTypeService.getById(id);
		return getAutoView().addObject("webagWasteType", webagWasteType);
	}
	
	/**
	 * 流程url表单 绑定的表单编辑页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("modify")
	public ModelAndView modify(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagWasteType webagWasteType=webagWasteTypeService.getById(id);
		return getAutoView().addObject("webagWasteType", webagWasteType);
	}
	
	
	
	/**
	 * 启动流程
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("run")
	@Action(description="启动流程")
	public void run(HttpServletRequest request, HttpServletResponse response,WebagWasteType webagWasteType) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		Integer isList=RequestUtil.getInt(request, "isList",0);
		ProcessCmd processCmd=new ProcessCmd();
		//添加表单数据，当人员为流程变量的时候用到,先注释，因为会引起流程提交报错
		//if(isList==0){
		//	processCmd = BpmUtil.getProcessCmd(request);
		//}else{
		//	WebagWasteType webagWasteTypeTemd = webagWasteTypeService.getById(webagWasteType.getId()); 
		//	Map<String,Object> map = MapUtil.transBean2Map(webagWasteTypeTemd);
		//	JSONObject jsonObject = JSONObject.fromObject(map);
		//	String str = "{'main':{'fields':"+jsonObject+"},'sub':[],'opinion':[]}";
		//	JSONObject formData = JSONObject.fromObject(str); 
		//	processCmd.setFormData(formData.toString());
		//}
		processCmd.setFlowKey("undefined");
		processCmd.setUserAccount(ContextUtil.getCurrentUser().getAccount());
		try {
			if(id!=0L){
				if(isList==1){
				webagWasteType=webagWasteTypeService.getById(id);
				}
				processCmd.setBusinessKey(id.toString());
				processRunService.startProcess(processCmd);
				webagWasteTypeService.save(webagWasteType);
			}else{
				Long genId=UniqueIdUtil.genId();
				processCmd.setBusinessKey(genId.toString());
				webagWasteType.setId(genId);
				processRunService.startProcess(processCmd);
				webagWasteTypeService.save(webagWasteType);
			}
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, "启动流程成功"));
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, ExceptionUtils.getRootCauseMessage(e)));
		}
	}


    @RequestMapping("getMyTodoTask")
	@Action(description="查看webag_waste_type任务分页列表")
	public ModelAndView getMyTodoTask(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagWasteType> list=webagWasteTypeService.getMyTodoTask(userId, new QueryFilter(request,"webagWasteTypeItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWasteTypeList",list);
		return mv;
	}
	
	@RequestMapping("getMyDraft")
	@Action(description="查看webag_waste_type草稿")
	public ModelAndView getMyDraft(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagWasteType> list=webagWasteTypeService.getMyDraft(userId, new QueryFilter(request,"webagWasteTypeItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWasteTypeList",list);
		return mv;
	}
	
	@RequestMapping("getMyEnd")
	@Action(description="查看我结束的webag_waste_type实例")
	public ModelAndView getMyEnd(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagWasteType> list=webagWasteTypeService.getMyEnd(userId, new QueryFilter(request,"webagWasteTypeItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWasteTypeList",list);
		return mv;
	}
}