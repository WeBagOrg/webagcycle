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
    <title><%=appName %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/styles/login/login.css"></link>
	<link rel="icon" href="favicon.ico" mce_href="favicon.ico" type="image/x-icon"/>
	
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
<div class="form_background">
<form id="form-login" action="${ctx}/login.ht" method="post">
	<span id="officeSpan" style="color:red" width="100%";height="50px"></span>
	<center>
		<div class="main_login">
			<span class="bpmlogo_login"></span>
			<div class="content_login">
			<div class="column">
			<!-- <span class="logo"> -->
			<span><img id="logoImg" src="${ctx}/commons/image/logo2.png" />			
			</span>&nbsp;&nbsp;&nbsp;<font style="font-size:12pt ;font-weight:bold">知识产权管理平台</font>
			</div>	
				<div class="column">
					<span style="font-size:15px;">用户名&nbsp;:</span>
			        <input class="username" type="text" name="username" class="login" /><br>	
				</div>	
				<div class="column">
					<span style="font-size:15px;">密&nbsp;&nbsp;&nbsp;&nbsp;码&nbsp;:</span>
					<input class="password" type="password" name="password"/>
				</div>	
				<c:if test="${sessionScope.validCodeEnabled=='true'}">
					<div class="vcode column">
					 	<div>
							<span>验证码:</span><br>
						
							<input type="text" name="validCode"  />
						</div>
						<div class="vcode_img">
							<img id="validImg" src="${ctx}/servlet/ValidCode" /><br>
							<input type="hidden" name="validCodeEnabled" value="true"/>
							<a onclick="reload()">看不清，换一张</a>
						</div>
					</div>
				</c:if>
				<div class="confirm">
					<input id="checkbox" type="checkbox" name="_spring_security_remember_me" value="1"/>
					<span><img src="styles/login/images/5c5d5f.png" alt="" /></span>
					<a class="reset_btn btn r" onclick="document.getElementById('form-login').reset();">重置</a>
					<a class="login_btn btn r" onclick="document.getElementById('form-login').submit();">登录</a>
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
			</div>
		</div>
	</center>
	
</form>
</div>
</body>
</html>
