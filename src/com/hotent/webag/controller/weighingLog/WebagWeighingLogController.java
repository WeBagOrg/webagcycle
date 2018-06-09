

package com.hotent.webag.controller.weighingLog;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.webag.model.billDetial.WebagBillDetail;
import com.hotent.webag.model.userAccount.WebagUserAccount;
import com.hotent.webag.service.billDetial.WebagBillDetailService;
import com.hotent.webag.service.bindBagInfo.BindBagService;
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

import com.hotent.webag.model.weighingLog.WebagWeighingLog;
import com.hotent.webag.service.weighingLog.WebagWeighingLogService;
import com.hotent.core.web.ResultMessage;
import com.hotent.platform.service.system.IdentityService;
/**
 * 对象功能:webag_weighing_log 控制器类
 */
@Controller
@RequestMapping("/webag/weighingLog/webagWeighingLog/")
public class WebagWeighingLogController extends BaseController
{
	@Resource
	private WebagWeighingLogService webagWeighingLogService;
	@Resource
	private IdentityService identityService;
	@Resource
	private BindBagService bindBagService;
	@Resource
	private WebagBillDetailService webagBillDetailService;
	@Resource
	private WebagUserAccountService webagUserAccountService;
	@RequestMapping("test")
    @Action(description = "测试")
	public void test() {
        System.out.println("测试.................................");
    }

	/**
	 * 添加或更新webag_weighing_log。
	 * @param request
	 * @param response
	 * @param webagWeighingLog 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveWeighingLog")
	@Action(description="添加或更新webag_weighing_log")
	public void saveWeighingLog(HttpServletRequest request, HttpServletResponse response,WebagWeighingLog webagWeighingLog) throws Exception
	{
		String resultMsg=null;
		try{
			String wasteTypeNo = request.getParameter("wasteTypeNo");
			String bagNoUrl = request.getParameter("bagNo");
			String thePriceOfTheTime = request.getParameter("thePriceOfTheTime");
			String weight = request.getParameter("weight");
			String amountOfRebate = request.getParameter("amountOfRebate");
			//取得袋子编号
			String[] bagNo = bagNoUrl.split("=");
			webagWeighingLog.setWasteTypeNo(wasteTypeNo);
			webagWeighingLog.setBagNo(bagNo[1]);
			webagWeighingLog.setThePriceOfTheTime(thePriceOfTheTime);
			webagWeighingLog.setWeight(weight);
			webagWeighingLog.setAmountOfRebate(amountOfRebate);
			webagWeighingLog.setWeighingLogNo("weighingLog"+UUID.randomUUID().toString().substring(0, 10));
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(8);
			String dateString = formatter.format(currentTime);
			webagWeighingLog.setCreateDate(formatter.parse(dateString, pos));
			webagWeighingLogService.save(webagWeighingLog);

			//流水明细表添加信息
			WebagBillDetail webagBillDetail = new WebagBillDetail();
			webagBillDetail.setBagNo(bagNo[1]);
			webagBillDetail.setTotalPrice(Long.valueOf(amountOfRebate));
			webagBillDetail.setWasteType(wasteTypeNo);
			webagBillDetail.setUnitPrice(Long.valueOf(thePriceOfTheTime));
			webagBillDetail.setWeight(Long.valueOf(weight));
			webagBillDetail.setCreatTime(currentTime);
			//根据bagNo获取被绑的用户id
			String weChatId = bindBagService.getByBagNo(bagNo[1]);
			webagBillDetail.setUserWeChatID(weChatId);
			webagBillDetailService.save(webagBillDetail);
			//向公众号发消息
			//更新用户余额
			WebagUserAccount webagUserAccount = webagUserAccountService.getByWeChatId(weChatId);
			if (webagUserAccount==null){
				webagUserAccount = new WebagUserAccount();
				webagUserAccount.setAccount(Long.valueOf(amountOfRebate));
				webagUserAccount.setUserWechatId(weChatId);
				webagUserAccountService.save(webagUserAccount);
			}else {
				Long newAccount = webagUserAccount.getAccount()+Long.valueOf(amountOfRebate);
				webagUserAccount.setAccount(newAccount);
				webagUserAccountService.updateByWechatId(webagUserAccount);
			}

			writeResultMessage(response.getWriter(),"",ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),"",ResultMessage.Fail);
		}
	}

	/**
	 * 添加或更新webag_weighing_log。
	 * @param request
	 * @param response
	 * @param webagWeighingLog 添加或更新的实体
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新webag_weighing_log")
	public void save(HttpServletRequest request, HttpServletResponse response,WebagWeighingLog webagWeighingLog) throws Exception
	{
		String resultMsg=null;
		try{
			if(webagWeighingLog.getId()==null){
				webagWeighingLogService.save(webagWeighingLog);
				resultMsg=getText("添加","webag_weighing_log");
			}else{
			    webagWeighingLogService.save(webagWeighingLog);
				resultMsg=getText("更新","webag_weighing_log");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}

	/**
	 * 取得webag_weighing_log分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看webag_weighing_log分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		List<WebagWeighingLog> list=webagWeighingLogService.getAll(new QueryFilter(request,"webagWeighingLogItem"));
		ModelAndView mv=this.getAutoView().addObject("webagWeighingLogList",list);
		return mv;
	}

	/**
	 * 删除webag_weighing_log
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除webag_weighing_log")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[]  lAryId=RequestUtil.getLongAryByStr(request,"id");
			webagWeighingLogService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, "删除webag_weighing_log成功!");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 	编辑webag_weighing_log
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑webag_weighing_log")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");

		String returnUrl=RequestUtil.getPrePage(request);
		WebagWeighingLog webagWeighingLog=webagWeighingLogService.getById(id);
		if(BeanUtils.isEmpty(webagWeighingLog)){
			webagWeighingLog=new WebagWeighingLog();
			String weighingLogNo_id=identityService.nextId("czrzbh");
			webagWeighingLog.setWeighingLogNo(weighingLogNo_id);
		}

		return getAutoView().addObject("webagWeighingLog",webagWeighingLog)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * 取得webag_weighing_log明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看webag_weighing_log明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		WebagWeighingLog webagWeighingLog=webagWeighingLogService.getById(id);
		return getAutoView().addObject("webagWeighingLog", webagWeighingLog);
	}
	
}