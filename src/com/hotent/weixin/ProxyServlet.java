package com.hotent.weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.api.util.PropertyUtil;
import com.hotent.core.util.HttpUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.SecurityUtil;
import com.hotent.weixin.api.TokenUtil;
import com.hotent.weixin.api.WeixinConsts;

import net.sf.json.JSONObject;

/**
 * 实现微信OATH2.0协议，微信跳转到应用的页面。
 * 
 * <pre>
 * 
 * 1.判定用户是否登录，如果已经登录，那么直接跳转到指定的页面。
 * 
 * 2.没有登录的情况。
 * 	首先获取from 是否为 "wx"。
 * 	1.是则跳转到微信服务器页面，获取会话code。
 * 	2.获取code后，微信服务器再次跳转回此代理页面。
 * 	3.代理页面根据code和token提交数据到微信服务器获取，当前用户帐号。
 * 	4.获取帐号后，系统让此账户自动登录。
 * 	5.在跳转回指定页面，页面验证成功。
 * 
 * 在微信菜单URL做如下配置：
 * 
 * http://平台域名/bpmx3/proxy?from=wx&redirect=需要跳转到的页面地址。
 * 
 * </pre>
 * 
 * @author ray
 *
 */
public class ProxyServlet  extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8382507359838493519L;
	
	protected Logger log = LoggerFactory.getLogger(ProxyServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String from=req.getParameter("from");
		String redirect=req.getParameter("redirect");
		SysUser sysUser=(SysUser) ContextUtil.getCurrentUser();
		
		//没有登录
		if(sysUser==null){
			String code=req.getParameter("code");
			if("wx".equals(from)){
				String corpId=PropertyUtil.getByAlias("corpId");
				String redirectUrl=WeixinConsts.getWxAuthorize(corpId, redirect);
				resp.sendRedirect(redirectUrl);
			}
			else if(StringUtil.isNotEmpty(code)){
				String token=TokenUtil.getWxToken();
				if("-1".equals(token ) || "-2".equals(token )){
					resp.getWriter().println("获取token失败");
				}
				else{
					String userUrl=WeixinConsts.getWxUserInfo(token, code);
					try{
						String json=HttpUtil.sendHttpsRequest(userUrl, "", WeixinConsts.METHOD_GET);
						JSONObject jsonObj=JSONObject.fromObject(json);
						String userId=jsonObj.getString("UserId");
						//让系统登录
						SecurityUtil.login(req, userId, "", true);
					}
					catch(Exception ex){
						log.error(ex.getMessage());
					}
				}
				
			}
		}
		else{
			resp.sendRedirect(redirect);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

}
