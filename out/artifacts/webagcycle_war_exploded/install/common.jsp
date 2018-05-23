<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%!
	//加载资源文件
	public static final String INSTALL_INIT_CONFIG_PATH = "/install_init.conf";
	public static final String INSTALL_LOCK_CONFIG_PATH = "/install_lock.conf";
	public static final String INDEX_JSP_PATH = "/install/index.jsp";
	public static final String DATABASE_ENCODING = "UTF-8";
	public static final String WEB_XML_INSTALL_PATH = "/WEB-INF/web.xml";
	public static final String WEB_XML_PATH = "/install/web/web.xml";
	
	String stackToString(Exception exception) {
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			exception.printStackTrace(printWriter);
			return stringWriter.toString().replaceAll("\\r?\\n", "</br>");
		} catch (Exception e) {
			return "stackToString error";
		}
	}
	
	public boolean isCanWrite(String dirPath) {
		File file = new File(dirPath);
		if(!file.exists()) {
			file.mkdir();
		}
		if(file.canWrite()) {
			return true;
		} else{
			return false;
		}
	}
%>
<%
	String ctx=request.getContextPath();
	response.setHeader("progma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0);
	
	String rootPath = application.getRealPath("/");
	File installLockConfigFile = new File(rootPath + INSTALL_LOCK_CONFIG_PATH);
	if(installLockConfigFile.exists()) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>系统提示 </title>
<link href="css/install.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<fieldset>
		<legend>系统提示</legend>
		<p>您无此访问权限！若您需要重新安装程序，请删除install_lock.conf文件！</p>
		<p>
			<strong>提示: 基于安全考虑请在安装成功后卸载install项目</strong>
		</p>
	</fieldset>
</body>
</html>
<%
		return;
	}
%>