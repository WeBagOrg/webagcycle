package com.hotent.platform.service.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.util.AppUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.platform.model.system.ResourcesUrlExt;
import com.hotent.platform.model.system.SysRole;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.model.system.SystemConst;
import com.hotent.platform.web.security.CustomPwdEncoder;

/**
 * 主要用于系统权限资源缓存。
 * <pre>
 * 	1.系统的url和角色映射。
 *  2.系统的url和参数列表进行映射。
 * 	4.系统和角色进行映射。
 *  3.系统的功能和角色映射。
 * </pre>
 * @author ray
 *
 */
public class SecurityUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	

	
	/**
	 * 将系统 的功能和角色列表加入到映射中。
	 * @param systemId		系统id。
	 * @param funcRoleList	功能和角色映射列表。
	 */
	private static FunctionRights getFuncRoleList(String sysAlias,String function){
		
		SysRoleService sysRoleService=(SysRoleService)AppUtil.getBean(SysRoleService.class);
		List<ResourcesUrlExt> funcRoleList=sysRoleService.getFunctionRoleList(sysAlias,function);
		boolean hasFunction=false;
		if(funcRoleList.size()>0){
			hasFunction=true;
		}
		
		Collection<ConfigAttribute> collectoin=new HashSet<ConfigAttribute>();
		for(ResourcesUrlExt table:funcRoleList){
			String role=(String)table.getRole();
			if(StringUtil.isEmpty(role)) continue;
			collectoin.add(new SecurityConfig(role));
		}
		SecurityUtil util=new SecurityUtil();
		FunctionRights rights=util.new FunctionRights(hasFunction, collectoin);
		return rights;

	}
	
	
	
	
	
	/**
	 * 添加系统和角色的关系映射。
	 * 系统id ： 角色set集合。
	 * @param systemId
	 */
	public static Set<String> getSystemRole(Long systemId){
		SysRoleService sysRoleService=(SysRoleService)AppUtil.getBean(SysRoleService.class);
		List<SysRole> listRole= sysRoleService.getBySystemId(systemId);
//		ICache iCache = (ICache) AppUtil.getBean(ICache.class);
//		String systemRoleKey=SystemRoleMap + systemId;
		//URL 和角色列表映射。
		Set<String> roleSet=new HashSet<String>();
		for(SysRole role: listRole){
			roleSet.add(role.getAlias());
		}
//		iCache.add(systemRoleKey, roleSet);
		return roleSet;
	}
	

	
	
	
	/**
	 * 根据系统和功能别名判断是否有权限访问。
	 * @param systemId
	 * @param function
	 * @return
	 */
	public static boolean hasFuncPermission(String systemAlias, String function){
		
		FunctionRights functionRights= getFuncRoleList(systemAlias, function);
		
		
		
		SysUser currentUser= (SysUser) ContextUtil.getCurrentUser();
		//超级管理员
		if(currentUser.getAuthorities().contains(SystemConst.ROLE_GRANT_SUPER)){
			return true;
		}
		//当功能在系统功能表中，匹配当前用户的角色是否在功能的角色列表中。
		else {
			if(!functionRights.isHasFunction()) return true ;
			Collection<ConfigAttribute> functionRole=functionRights.getRoles();
			if(functionRole.size()==0) return false;
			
			for(GrantedAuthority hadRole:currentUser.getAuthorities()){
				if(functionRole.contains(new SecurityConfig(hadRole.getAuthority()))){  
	                return true;
	            }
	        }
			return false;
	    }
    }
	
	
	public class FunctionRights{
		
		private boolean hasFunction=false;
		
		private Collection<ConfigAttribute> roles=new ArrayList<ConfigAttribute>();
		
		public FunctionRights(boolean hasFunction,Collection<ConfigAttribute> roles){
			this.hasFunction=hasFunction;
			this.roles=roles;
		}

		public boolean isHasFunction() {
			return hasFunction;
		}

		public void setHasFunction(boolean hasFunction) {
			this.hasFunction = hasFunction;
		}

		public Collection<ConfigAttribute> getRoles() {
			return roles;
		}

		public void setRoles(Collection<ConfigAttribute> roles) {
			this.roles = roles;
		}
		
	}
	
	/**
	 * 登录系统让系统实现登录。
	 * @param request
	 * @param userName		用户名
	 * @param pwd			密码
	 * @param isIgnorePwd	是否忽略密码
	 * @return 
	 */
	public static Authentication login(HttpServletRequest request,String userName,String pwd,boolean isIgnorePwd){
		AuthenticationManager authenticationManager =(AuthenticationManager) AppUtil.getBean("authenticationManager");
		CustomPwdEncoder.setIngore(isIgnorePwd);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, pwd);
		authRequest.setDetails(new WebAuthenticationDetails(request));
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = authenticationManager.authenticate(authRequest);
		securityContext.setAuthentication(auth);
		return auth;
	}
	
	
	

}



 
