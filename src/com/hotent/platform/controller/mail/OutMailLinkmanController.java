package com.hotent.platform.controller.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.query.QueryFilter;
import com.hotent.platform.model.mail.OutMailLinkman;
import com.hotent.platform.model.oa.OaLinkman;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.mail.OutMailLinkmanService;
import com.hotent.platform.service.oa.OaLinkmanService;
import com.hotent.platform.service.system.SysUserService;
/**
 * 对象功能:最近联系人 控制器类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2012-04-13 11:11:56
 */
@Controller
@RequestMapping("/platform/mail/outMailLinkman/")
public class OutMailLinkmanController extends BaseController
{
	@Resource
	private OutMailLinkmanService outMailLinkmanService;
	@Resource
	private OaLinkmanService oaLinkmanService;
	@Resource
	private SysUserService sysUserService;
	/**
	 * 最近联系人树形列表的json数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getOutMailLinkmanData")
	@ResponseBody
	public List<OutMailLinkman> getOutMailLinkmanData(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		List<OutMailLinkman> list=outMailLinkmanService.getAllByUserId(ContextUtil.getCurrentUserId());
		List<OutMailLinkman> listTemp=new ArrayList<OutMailLinkman>();
		Long userId = ContextUtil.getCurrentUserId();
		Map<String,String> linkManMap = oaLinkmanService.getLinkManMap(userId);
		for(OutMailLinkman beanTemp:list){
			List<SysUser> userList = new ArrayList<SysUser>();
			userList = sysUserService.findLinkMan(beanTemp.getLinkAddress(), userId);
			OutMailLinkman omus=new OutMailLinkman();
	    	//String currentName=ContextUtil.getCurrentUser().getFullname();
	    	//String linkName=beanTemp.getLinkName();
	    	if(linkManMap.containsValue(beanTemp.getLinkAddress())){
	    		Set set=linkManMap.entrySet();
	    		Iterator it=set.iterator();
	    		String linkName = "";
	    		while(it.hasNext()){
	    			Map.Entry entry=(Map.Entry)it.next();
	    			if(entry.getValue().equals(beanTemp.getLinkAddress())) {
	    				linkName = linkName+entry.getKey();
	    			}
	    		}
	    		omus.setLinkAddress(linkName+"("+beanTemp.getLinkAddress()+")");
	    	}
	    	else if(!(linkManMap.containsValue(beanTemp.getLinkAddress())) && userList.size()!=0){//联系人列表没有，但是用户列表有
	    		String strName="";
	    		if(userList.size() >1){
	    			for(int i = 0 ; i<userList.size() ; i++){
		    			SysUser sysUser = userList.get(i);
		    			strName=strName+sysUser.getFullname();
		    		}
	    		}else{
	    			strName = userList.get(0).getFullname();
	    		}
	    		omus.setLinkAddress(strName+"("+beanTemp.getLinkAddress()+")");
	    	}else{
	    		omus.setLinkAddress("陌生人("+beanTemp.getLinkAddress()+")");
	    	}
		    listTemp.add(omus);
		}
		return listTemp;
	}
}
