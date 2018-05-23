<%@page import="install.util.ResourceUtil"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.io.FileSystemUtils"%>
<%@include file="common.jsp"%>
<%
	//判断用户是否同意许可条件
	String isAgreeAgreement = request.getParameter("isAgreeAgreement");
	if ("true".equals(isAgreeAgreement)) {
		session.setAttribute("isAgreeAgreement", true);
	} else {
		response.sendRedirect("index.jsp");
		return;
	}
	//待修改的属性文件路径
	String proertiesFilePath=ResourceUtil.getPropertyValue("propertyFile.path")+ResourceUtil.getPropertyValue("db.propertyFile");
	boolean isPropertiesFileCanWrite = isCanWrite(rootPath +proertiesFilePath);
	
	String freeSpaceInfo = "-";
	long diskNeedSpace=Long.parseLong(ResourceUtil.getPropertyValue("disk.freespace"));//安装需要的磁盘空间
	long freeSpace;
	try {
		freeSpace = FileSystemUtils.freeSpaceKb(rootPath);//项目的部署磁盘空间是否足够
	} catch (Exception e) {
		freeSpace = 0;
	}
	if (freeSpace != 0&&diskNeedSpace>0) {
		if (freeSpace < 1024) {//小于1MB
			freeSpaceInfo = "<span class=\"red\">" + freeSpace + "KB</span>";
		} else if (freeSpace <= 1024 && freeSpace < 51200) {//1MB--50MB
			freeSpaceInfo = "<span class=\"red\">" + (freeSpace / 1024) + "MB</span>";
		} else if (freeSpace >= (diskNeedSpace+200)*1024) {//大于所需空间+200MB的磁盘空间（200MB用于扩展）
			freeSpaceInfo = "<span class=\"green\">" + (freeSpace / 1024) + "MB</span>";
		} 
	}
	
	String maxMemoryInfo = "-";
	long memNeedSpace=Long.parseLong(ResourceUtil.getPropertyValue("mem.freespace"));//安装需要的内存空间
	double maxMemory;
	try {
		maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;//检查内存空间是否足够使用,单位MB
	} catch (Exception e) {
		maxMemory = 0;
	}
	if (maxMemory != 0) {
		if (maxMemory > memNeedSpace) {
			maxMemoryInfo = "<span class=\"green\">" + maxMemory + "MB</span>";
		} else {
			maxMemoryInfo = "<span class=\"red\">" + maxMemory + "MB</span>";
		}
	}
	
	String isPropertiesFileInfo = null;
	if (isPropertiesFileCanWrite) {
		isPropertiesFileInfo = "<span class=\"green\">√</span>";
	} else {
		isPropertiesFileInfo = "<span class=\"red\">×</span>";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>检查安装环境 </title>
<link href="css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
$().ready( function() {
	//初始化页面按钮
	var $previous = $("#previous");
	var $next = $("#next");

	//判断页面的编码方式是否为UTF-8
	var $encodingInfo = $("#encodingInfo");
	var hasWarn = false;
	$.ajax({
		type: "GET",
		cache: false,
		url: "check_encoding.jsp",
		data: {character: "一"},
		dataType: "json",
		beforeSend: function(data) {
			$encodingInfo.text("-");
		},
		success: function(data) {
			if (data.isUTF8) {
				$encodingInfo.html('<span class="green">√<\/span>');
			} else {
				$encodingInfo.html('<span class="red">×<\/span>');
				hasWarn = true;
			}
		}
	});
	
	<%if ((freeSpace != 0 && freeSpace < (diskNeedSpace+200)*1024 ) || (maxMemory != 0 && maxMemory < memNeedSpace )  || !isPropertiesFileCanWrite  ) { %>
		hasWarn = true;
	<% } %>
	
	$previous.click( function() {
		location.href = "index.jsp";
	});
	
	$next.click( function() {
		if (hasWarn && confirm("您的安装环境有误,可能导致系统安装或运行错误,您确定继续吗?") == false) {
			return false;
		} else {
			location.href = "setting.jsp";
		}
	});

});
</script>
</head>
<body>
	<div class="header">
		<div class="title">安装程序 - 环境检测</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<div class="bodyLeft">
			<ul class="step">
				<li>许可协议</li>
				<li class="current">环境检测</li>
				<li>系统配置</li>
				<li>系统安装</li>
				<li>完成安装</li>
			</ul>
		</div>
		<div class="bodyRight">
			<table>
				<tr>
					<th>&nbsp;</th>
					<th>基本环境</th>
					<th>推荐环境</th>
					<th>当前环境</th>
				</tr>
				<tr>
					<td>
						<strong>操作系统:</strong>
					</td>
					<td>
						Linux/Unix/Windows ...
					</td>
					<td>
						Linux/Unix/Windows
					</td>
					<td>
						<%=System.getProperty("os.name")%> (<%=System.getProperty("os.arch")%>)
					</td>
				</tr>
				<tr>
					<td>
						<strong>JDK版本:</strong>
					</td>
					<td>
						JDK 1.5 +
					</td>
					<td>
						JDK 1.6
					</td>
					<td>
						<%=System.getProperty("java.version")%>
					</td>
				</tr>
				<tr>
					<td>
						<strong>WEB服务器:</strong>
					</td>
					<td>
						Tomcat
					</td>
					<td>
						Tomcat 6.0
					</td>
					<td>
						<span title="<%=application.getServerInfo()%>">
							<%=application.getServerInfo()%>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<strong>数据库:</strong>
					</td>
					<td>
						MySQL/Oracle/SQL Server
					</td>
					<td>
						MySQL 5.5
					</td>
					<td>
						-
					</td>
				</tr>
				<tr>
					<td>
						<strong>磁盘空间:</strong>
					</td>
					<td>
						500MB +
					</td>
					<td>
						<%=(diskNeedSpace+200) %>MB +
					</td>
					<td>
						<%=freeSpaceInfo%>
					</td>
				</tr>
				<tr>
					<td>
						<strong>可用内存:</strong>
					</td>
					<td>
						256MB +
					</td>
					<td>
						<%=memNeedSpace %> MB +
					</td>
					<td>
						<%=maxMemoryInfo%>
					</td>
				</tr>
				<tr>
					<td>
						<strong>字符集编码:</strong>
					</td>
					<td>
						UTF-8
					</td>
					<td>
						UTF-8
					</td>
					<td>
						<span id="encodingInfo"></span>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<th width="344">
						文件目录
					</th>
					<th width="170">
						所需状态
					</th>
					<th>
						当前状态
					</th>
				</tr>
				<tr>
					<td>
						<%=proertiesFilePath %>
					</td>
					<td>
						可写
					</td>
					<td>
						<%=isPropertiesFileInfo%>
					</td>
				</tr>
			</table>
		</div>
		<div class="buttonArea">
			<input type="button" id="previous" class="button" value="上 一 步" />&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" id="next" class="button" value="下 一 步" />
		</div>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2005-2013 IPPH All Rights Reserved.</p>
	</div>
</body>
</html>