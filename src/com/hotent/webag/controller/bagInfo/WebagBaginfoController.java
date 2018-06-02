

package com.hotent.webag.controller.bagInfo;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.core.util.*;
import com.hotent.webag.model.bagInfo.WebagBaginfo;
import com.hotent.webag.model.bindBagInfo.BindBag;
import com.hotent.webag.service.bagInfo.WebagBaginfoService;
import com.hotent.webag.service.bindBagInfo.BindBagService;
import com.hotent.webag.until.GetWeChatOpenId;
import com.hotent.webag.until.produceQRcodeTool;
import net.sf.json.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hotent.platform.annotion.Action;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.core.page.PageList;
import com.hotent.platform.model.system.SysUser;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.hotent.core.bpm.util.BpmUtil;
import net.sf.json.JSONObject;


import com.hotent.core.web.ResultMessage;
import com.hotent.core.engine.GroovyScriptEngine;
import com.hotent.platform.service.system.IdentityService;
import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.bpm.model.ProcessCmd;
import com.hotent.platform.model.bpm.ProcessRun;
import com.hotent.platform.service.bpm.ProcessRunService;
/**
 * 对象功能:回收袋信息 控制器类
 */
@Controller
@RequestMapping("/webag/bagInfo/webagBaginfo/")
public class WebagBaginfoController extends BaseController
{
	@Resource
	private WebagBaginfoService webagBaginfoService;
	@Resource
	private GroovyScriptEngine engine;
	@Resource
	private IdentityService identityService;
	@Resource
	private ProcessRunService processRunService;
	@Resource
	private BindBagService bindBagService;
	/**
	 * 添加或更新回收袋信息。
	 * @param request
	 * @param response
	 * @param webagBaginfo 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新回收袋信息")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagBaginfo webagBaginfo) throws Exception
	{
		String resultMsg=null;		
		try{
			if(webagBaginfo.getId()==null){
				webagBaginfoService.save(webagBaginfo);
				resultMsg=getText("添加","回收袋信息");
			}else{
			    webagBaginfoService.save(webagBaginfo);
				resultMsg=getText("更新","回收袋信息");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得回收袋信息分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看回收袋信息分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<WebagBaginfo> list=webagBaginfoService.getAll(new QueryFilter(request,"webagBaginfoItem"));
		ModelAndView mv=this.getAutoView().addObject("webagBaginfoList",list);
		return mv;
	}
	
	/**
	 * 删除回收袋信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除回收袋信息")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagBaginfoService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除回收袋信息成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑回收袋信息
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑回收袋信息")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		
		Long runId=0L;
		ProcessRun processRun=processRunService.getByBusinessKey(id.toString());
		if(BeanUtils.isNotEmpty(processRun)){
			runId=processRun.getRunId();
		}
		String returnUrl=RequestUtil.getPrePage(request);
		WebagBaginfo webagBaginfo=webagBaginfoService.getById(id);
		if(BeanUtils.isEmpty(webagBaginfo)){
			webagBaginfo=new WebagBaginfo();
			String bagNo_id=identityService.nextId("bagNo");
			webagBaginfo.setBagNo(bagNo_id);
			String creatorName_script="return scriptImpl.getCurrentName();";
			webagBaginfo.setCreatorName(engine.executeObject(creatorName_script, null).toString());
			String creatorId_script="return scriptImpl.getCurrentUserId();";
			webagBaginfo.setCreatorId((Long) engine.executeObject(creatorId_script, null));
		}
		
		return getAutoView().addObject("webagBaginfo",webagBaginfo)
							.addObject("runId", runId)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得回收袋信息明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看回收袋信息明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagBaginfo webagBaginfo=webagBaginfoService.getById(id);
		Long runId=0L;
		ProcessRun processRun=processRunService.getByBusinessKey(id.toString());
		if(BeanUtils.isNotEmpty(processRun)){
			runId=processRun.getRunId();
		}
		return getAutoView().addObject("webagBaginfo", webagBaginfo).addObject("runId", runId);
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
		WebagBaginfo webagBaginfo=webagBaginfoService.getById(id);
		return getAutoView().addObject("webagBaginfo", webagBaginfo);
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
		WebagBaginfo webagBaginfo=webagBaginfoService.getById(id);
		return getAutoView().addObject("webagBaginfo", webagBaginfo);
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
	public void run(HttpServletRequest request, HttpServletResponse response,WebagBaginfo webagBaginfo) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		Integer isList=RequestUtil.getInt(request, "isList",0);
		ProcessCmd processCmd=new ProcessCmd();
		//添加表单数据，当人员为流程变量的时候用到,先注释，因为会引起流程提交报错
		//if(isList==0){
		//	processCmd = BpmUtil.getProcessCmd(request);
		//}else{
		//	WebagBaginfo webagBaginfoTemd = webagBaginfoService.getById(webagBaginfo.getId()); 
		//	Map<String,Object> map = MapUtil.transBean2Map(webagBaginfoTemd);
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
				webagBaginfo=webagBaginfoService.getById(id);
				}
				processCmd.setBusinessKey(id.toString());
				processRunService.startProcess(processCmd);
				webagBaginfoService.save(webagBaginfo);
			}else{
				Long genId=UniqueIdUtil.genId();
				processCmd.setBusinessKey(genId.toString());
				webagBaginfo.setId(genId);
				processRunService.startProcess(processCmd);
				webagBaginfoService.save(webagBaginfo);
			}
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, "启动流程成功"));
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, ExceptionUtils.getRootCauseMessage(e)));
		}
	}


    @RequestMapping("getMyTodoTask")
	@Action(description="查看回收袋信息任务分页列表")
	public ModelAndView getMyTodoTask(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagBaginfo> list=webagBaginfoService.getMyTodoTask(userId, new QueryFilter(request,"webagBaginfoItem"));
		ModelAndView mv=this.getAutoView().addObject("webagBaginfoList",list);
		return mv;
	}
	
	@RequestMapping("getMyDraft")
	@Action(description="查看回收袋信息草稿")
	public ModelAndView getMyDraft(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagBaginfo> list=webagBaginfoService.getMyDraft(userId, new QueryFilter(request,"webagBaginfoItem"));
		ModelAndView mv=this.getAutoView().addObject("webagBaginfoList",list);
		return mv;
	}
	
	@RequestMapping("getMyEnd")
	@Action(description="查看我结束的回收袋信息实例")
	public ModelAndView getMyEnd(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		SysUser sysUser=(SysUser)ContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		List<WebagBaginfo> list=webagBaginfoService.getMyEnd(userId, new QueryFilter(request,"webagBaginfoItem"));
		ModelAndView mv=this.getAutoView().addObject("webagBaginfoList",list);
		return mv;
	}
	/**
	 * 为回收袋绑定二维码。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addQR")
	@Action(description="为回收袋绑定二维码")
	public void addQR(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		String resultMsg=null;
		Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
        ResultMessage message=null;
		//根据id获取回收袋信息
		for(int i=0; i<lAryId.length;i++){
			WebagBaginfo webagBaginfo = webagBaginfoService.getById(lAryId[i]);
			File logoFile = new File("../usr/local/webag/logo.png");
			String fileName = "../usr/local/webag"+File.separator+webagBaginfo.getBagNo()+".png";
			File QrCodeFile = new File(fileName);
			String url = "https://www.webagcycle.com/webagcycle_war/webag/bagInfo/webagBaginfo/userBindQR.ht?bagNo="+webagBaginfo.getBagNo();
			String note = "NO."+webagBaginfo.getBagNo();

				try {
					produceQRcodeTool.drawLogoQRCode(logoFile, QrCodeFile, url, note);
				}catch (Exception e){
                    message=new ResultMessage(ResultMessage.Fail, "生成二维码失败!");
					//writeResultMessage(response.getWriter(),"生成二维码失败",ResultMessage.Fail);
				}
				webagBaginfo.setIsHaveQR("已生成");
				webagBaginfo.setQRUrl(fileName);
				webagBaginfoService.update(webagBaginfo);
			}
        message=new ResultMessage(ResultMessage.Success, "生成完毕!");
		//writeResultMessage(response.getWriter(),"生成完毕",ResultMessage.Success);

        addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 用户扫码绑定二维码。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("userBindQR")
	@ResponseBody
	@Action(description="用户扫码绑定二维码")
	public Integer userBindQR(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String wxCode = request.getParameter("code");
		String bagNo = request.getParameter("bagNo");
        Locale locale = new Locale("en", "US");
		String requestUrl ="https://api.config.qq.com/sns/jscode2session";//请求地址 https://api.weixin.qq.com/sns/jscode2session
   		Map<String, String> requestUrlParam = new HashMap<String, String>();
		requestUrlParam.put("appid", "wx9750eb4c1a8b4073");//开发者设置中的appId
		requestUrlParam.put("secret", "67e39634565bf07bb2b411ff03ef4f7a"); //开发者设置中的appSecret
		requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
		requestUrlParam.put("grant_type", "authorization_code");//默认参数 authorization_code
		//发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
		JSONObject jsonObject =JSONObject.fromObject(GetWeChatOpenId.sendPost(requestUrl, requestUrlParam)) ;
		//将用户微信id和袋子做绑定
		String userOpenId = jsonObject.get("openId").toString();
		int res = webagBaginfoService.setuserBindQR(userOpenId,bagNo);
		//System.out.print(jsonObject.toString());
		return res;
		}
    /**
     * 取得二维码信息分页列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("QRCode")
    @Action(description="查看回收袋信息分页列表")
    public ModelAndView QRCode(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
        List<WebagBaginfo> list=webagBaginfoService.getAll(new QueryFilter(request,"webagBaginfoItem"));
        ModelAndView mv=this.getAutoView().addObject("webagBaginfoList",list);
        return mv;
    }
	/**
	 * 导出二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("downLoadZip")
	@Action(description="导出二维码")
	public ModelAndView downLoadZip(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String ids=RequestUtil.getString(request,"ids");
		//根据ids的值，获取对应的二维码，以List<WebagBaginfo>存储
		List<WebagBaginfo> webagBaginfoList = webagBaginfoService.getBagsListByIds(ids);
		//将打包好的zip文件，先放入一个临时文件夹中，如 C:/zip/用户名_时间
		SysUser sysUser=(SysUser) ContextUtil.getCurrentUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//设置临时文件下载地址
		String dir=AppConfigUtil.get("zipPath")!=null?AppConfigUtil.get("zipPath"):"C:/zip/";
		String fileDir=dir+sysUser.getUsername()+"_"+df.format(new Date());
		File file =new File(fileDir);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}

		webagBaginfoService.discloseTechnical(webagBaginfoList,fileDir);

		webagBaginfoService.exportZip(fileDir, request, response);
		return null;
	}
	/**
	 * 取得回收袋绑定信息(微信)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getbindInfo")
	@ResponseBody
	@Action(description="取得回收袋绑定信息")
	public JSONObject getbindInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String wechatId=RequestUtil.getString(request,"wechatId");
		List<BindBag> list = bindBagService.getByWechatId(wechatId);
		JSONObject jsonObject = JSONObject.fromObject(list);
		return jsonObject;
	}
}