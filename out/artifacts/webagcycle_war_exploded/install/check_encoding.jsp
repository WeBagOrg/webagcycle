<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@include file="common.jsp"%>
<%!
	private static final String DEST_CHARACTER = "一";
%>
<%
	String character = request.getParameter("character");
	Map<String, Boolean> jsonMap = new HashMap<String, Boolean>();
	if (DEST_CHARACTER.equals(character)) {
		jsonMap.put("isUTF8", true);
	} else {
		jsonMap.put("isUTF8", false);
	}
	ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getWriter(), jsonMap);
%>