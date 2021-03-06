<%@page import="com.hotent.core.api.util.PropertyUtil"%>
<%@page import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="com.hotent.core.api.util.PropertyUtil"%>
<%@page import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="org.springframework.security.authentication.BadCredentialsException"%>
<%@page import="java.util.Enumeration"%>
<%@ page pageEncoding="UTF-8" %>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	String appName=PropertyUtil.getByAlias("appName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>企业管理平台</title>
		<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
		<link rel="stylesheet" href="${ctx}/styles/webagcycle/reset.css" />
		<link rel="stylesheet" href="${ctx}/styles/webagcycle/common.css" />
		<script type="text/javascript">
		if(top!=this){//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			  top.location='${ctx}/login.jsp';
		}
		
		function reload(){
			var url="${ctx}/servlet/ValidCode?rand=" +new Date().getTime();
			document.getElementById("validImg").src=url;
		}
		
		$(function(){
			$('.main_login').find('input').keydown(function(event){
				if(event.keyCode==13){
					$('#form-login').submit();
				}
			});
			//检查office控件是否正常
			//officeInit();
		});
		
		function officeInit(){
			TANGER_OCX_OBJ = document.getElementById("TANGER_OCX");
			try {  
				var ax = new ActiveXObject("NTKO.OfficeControlQiYe");  
			} catch(e) {  
				$("#officeSpan").append("不能装载文档控件,"+e.message+"。请点击<a href='${ctx}/media/office/NtkoOfficeControlSetup.msi'>下载控件</a>")
				return 0;
			}  
			try{
				TANGER_OCX_OBJ.GetOfficeVer();
			}
			catch(e)
			{
				$("#officeSpan").append("office控件加载出现出错,"+e.message+"。请点击<a href='${ctx}/media/office/NtkoOfficeControlSetup.msi'>下载控件</a>")
				return 0;
			}

		}
		function changeCheckbox() {
			     var mylabel = document.getElementById('myCheckBox');
			     if (mylabel.innerHTML == "√")
			        mylabel.innerHTML = "";
			     else
			        mylabel.innerHTML = "√";
			 }		
</script>
	</head>
	<body>
		<div class="wrap login_wrap">
			<div class="content">
				<div class="logo"></div>
				<div class="login_box">
					<div class="login_form">
						<div class="login_title">
							企业管理平台
						</div>
						<form id="form-login" action="${ctx}/login.ht" method="post">
							<div class="form_text_ipt">
								<input name="username" type="text" placeholder="员工编号">
							</div>
							<div class="ececk_warning"><span>员工编号不能为空</span></div>
							<div class="form_text_ipt">
								<input name="password" type="password" placeholder="密码">
							</div>
							<div class="ececk_warning"><span>密码不能为空</span></div>
							<div class="form_check_ipt">
								<div class="left check_left">
									<label><input name="" type="checkbox"> 记住密码</label>
								</div>
							</div>
							<div class="form_btn">
								<button type="button"  class="left" onclick="document.getElementById('form-login').submit();">登录</button>
							</div>
							<%
				Object loginError=(Object)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				
				if(loginError!=null ){
					String msg="";
					if(loginError instanceof BadCredentialsException){
						msg="密码输入错误";
					}
					else if(loginError instanceof AuthenticationServiceException){
						AuthenticationServiceException ex=(AuthenticationServiceException)loginError;
						msg=ex.getMessage();
					}
					else{
						msg=loginError.toString();
					}
				%>
				<div class="confirm"><span style="color:#ff0000;"><%=msg %></span></div>
				<%
				request.getSession().removeAttribute("validCodeEnabled");//删除需要验证码的SESSION
				request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);//删除登录失败信息
				}
				%>
						</form>
					</div>
				</div>
			</div>
			
		</div>
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<script type="text/javascript" src="js/common.js" ></script>
		<div style="text-align:center;">
		
</div>
	</body>
</html>
