<%@page import="com.hotent.core.api.util.PropertyUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  %>
<%@include file="/commons/include/html_doctype.html"%>
<%
	String appName=PropertyUtil.getByAlias("appName");
%>
<head>
	<title><%=appName%></title>
	<%@include file="/commons/include/home.jsp"%>
</head>
	
</head>
<body>
	<div class="index-page">${html }</div>
</html>