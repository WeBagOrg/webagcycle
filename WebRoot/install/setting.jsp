<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>
<%
	Boolean isAgreeAgreement = (Boolean) session.getAttribute("isAgreeAgreement");
	if (isAgreeAgreement == null || !isAgreeAgreement) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>系统配置</title>
<link href="css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
$().ready( function() {
	//页面表单及按钮
	var $settingForm = $("#settingForm");//form表单
	var $databaseType = $("#databaseType");//数据库类型
	var $databaseHost = $("#databaseHost");//数据主机信息
	var $databasePort = $("#databasePort");//数据库端口号
	var $databaseUsername = $("#databaseUsername");//数据库用户名
	var $databasePassword = $("#databasePassword");//数据库密码
	var $databaseName = $("#databaseName");//数据库名，新建的数据名称
	var $isCreateDatabase = $("#isCreateDatabase");//是否新建数据库
	var $previous = $("#previous");
	
	//数据库类型，及数据库默认端口设置
	$databaseType.change( function() {
		if ($databaseType.val() == "mysql") {
			$databasePort.val("3306");
			$isCreateDatabase.prop("disabled", false);
			$isCreateDatabase.prop("checked", true);
		} else if ($databaseType.val() == "sqlserver") {
			$databasePort.val("1433");
			$isCreateDatabase.prop("checked", false);
			$isCreateDatabase.prop("disabled", true);
		} else if ($databaseType.val() == "oracle") {
			$databasePort.val("1521");
			$isCreateDatabase.prop("checked", false);
			$isCreateDatabase.prop("disabled", true);
		}
	});
	
	//表单提交时，校验表单内容
	$settingForm.submit( function() {
		if ($databaseType.val() == "") {
			alert("请选择数据库类型!");
			$databaseType.focus();
			return false;
		}
		if ($.trim($databaseHost.val()) == "") {
			alert("请填写数据库主机!");
			$databaseHost.focus();
			return false;
		}
		if ($.trim($databasePort.val()) == "") {
			alert("请填写数据库端口!");
			$databasePort.focus();
			return false;
		}
		if ($.trim($databaseUsername.val()) == "") {
			alert("请填写数据库用户名!");
			$databaseUsername.focus();
			return false;
		}
		if ($.trim($databaseName.val()) == "") {
			alert("请填写数据库名称!");
			$databaseName.focus();
			return false;
		}
	});
	
	$previous.click( function() {
		location.href = "check.jsp?isAgreeAgreement=true";
	});

});
</script>
</head>
<body>
	<div class="header">
		<div class="title">安装程序 - 系统配置</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<form id="settingForm" action="<%=ctx %>/install/install.jsp" method="post">
			<div class="bodyLeft">
				<ul class="step">
					<li>许可协议</li>
					<li>环境检测</li>
					<li class="current">系统配置</li>
					<li>系统安装</li>
					<li>完成安装</li>
				</ul>
			</div>
			<div class="bodyRight">
				<table>
					<tr>
						<th colspan="2">数据库设置</th>
					</tr>
					<tr>
						<td width="120">数据库类型:</td>
						<td>
							<select id="databaseType" name="databaseType">
								<option value="">请选择...</option>
								<option value="mysql">MySQL</option>
								<option value="sqlserver">SQL Server</option>
								<option value="oracle">Oracle</option>
							</select>
							<span class="requireField">*</span>
						</td>
					</tr>
					<tr>
						<td>数据库主机:</td>
						<td>
							<input type="text" id="databaseHost" name="databaseHost" class="text" value="localhost" maxlength="200" />
							<span class="requireField">*</span>
						</td>
					</tr>
					<tr>
						<td>数据库端口:</td>
						<td>
							<input type="text" id="databasePort" name="databasePort" class="text" value="3306" maxlength="200" />
							<span class="requireField">*</span>
						</td>
					</tr>
					<tr>
						<td>数据库用户名:</td>
						<td>
							<input type="text" id="databaseUsername" name="databaseUsername" class="text" maxlength="200" />
							<span class="requireField">*</span>
						</td>
					</tr>
					<tr>
						<td>数据库密码:</td>
						<td>
							<input type="password" id="databasePassword" name="databasePassword" class="text" maxlength="200" />
						</td>
					</tr>
					<tr>
						<td>数据库名称:</td>
						<td>
							<input type="text" id="databaseName" name="databaseName" class="text" value="" maxlength="200" />
							<span class="requireField">*</span>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<th colspan="2">其它设置</th>
					</tr>
					<tr>
						<td width="120">自动创建数据库:</td>
						<td>
							<input type="checkbox" id="isCreateDatabase" name="isCreateDatabase" value="true" checked="checked" />
						</td>
					</tr>
				</table>
			</div>
			<div class="buttonArea">
				<input type="button" id="previous" class="button" value="上 一 步" />&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" class="button" value="立即安装" />
			</div>
		</form>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2005-2013 IPPH All Rights Reserved.</p>
	</div>
</body>
</html>