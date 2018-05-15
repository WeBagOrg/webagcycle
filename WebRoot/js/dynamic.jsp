<%@page import="com.hotent.core.web.controller.BaseController"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="org.springframework.security.core.context.SecurityContextImpl"%>
<%@page import="com.hotent.platform.model.system.SysUser"%>
//设置ContextPath	
var __ctx='<%=request.getContextPath()%>';

var __jsessionId='<%=session.getId() %>';

<% SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");%>
var userFullName = '<%=((SysUser)securityContextImpl.getAuthentication().getPrincipal()).getFullname()%>'
var userId = '<%=((SysUser)securityContextImpl.getAuthentication().getPrincipal()).getUserId()%>'
