<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@include file="common.jsp"%>
<%
	Boolean isAgreeAgreement = (Boolean) session.getAttribute("isAgreeAgreement");
	if (isAgreeAgreement == null || !isAgreeAgreement) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	//获取set.jsp页面提交的数据库配置参数
	String databaseType = StringUtils.trim(request.getParameter("databaseType"));
	String databaseHost = StringUtils.trim(request.getParameter("databaseHost"));
	String databasePort = StringUtils.trim(request.getParameter("databasePort"));
	String databaseUsername = StringUtils.trim(request.getParameter("databaseUsername"));
	String databasePassword = StringUtils.trim(request.getParameter("databasePassword"));
	String databaseName = StringUtils.trim(request.getParameter("databaseName"));
	String isCreateDatabase = StringUtils.trim(request.getParameter("isCreateDatabase"));
	//后端校验提交参数
	if (StringUtils.equalsIgnoreCase(isCreateDatabase, "true")) {
		isCreateDatabase = "true";
	} else {
		isCreateDatabase = "false";
	}
	
	//设置校验的结果信息
	String status = "success";
	String message = "";
	String exception = "";
	
	if (StringUtils.isEmpty(databaseType)) {
		status = "error";
		message = "数据库类型不允许为空!";
	} else if (StringUtils.isEmpty(databaseHost)) {
		status = "error";
		message = "数据库主机不允许为空!";
	} else if (StringUtils.isEmpty(databasePort)) {
		status = "error";
		message = "数据库端口不允许为空!";
	} else if (StringUtils.isEmpty(databaseUsername)) {
		status = "error";
		message = "数据库用户名不允许为空!";
	} else if (StringUtils.isEmpty(databaseName)) {
		status = "error";
		message = "数据库名称不允许为空!";
	} 
	//将页面配置信息放置到session中，从而可以在多个页面中共享数据
	if (status.equals("success")) {
		session.setAttribute("databaseType", databaseType);
		session.setAttribute("databaseHost", databaseHost);
		session.setAttribute("databasePort", databasePort);
		session.setAttribute("databaseUsername", databaseUsername);
		session.setAttribute("databasePassword", databasePassword);
		session.setAttribute("databaseName", databaseName);
		session.setAttribute("isCreateDatabase", isCreateDatabase);
	}
	
	Connection connection = null;
	
	if (status.equals("success")) {
		if (databaseType.equalsIgnoreCase("mysql")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (Exception e) {
				status = "error";
				message = "MySQL JDBC驱动加载失败,请检查JDBC驱动!";
				exception = stackToString(e);
			}
			
			if (status.equals("success")) {
				if (StringUtils.equals(isCreateDatabase, "true")) {
					List<String> urls = new ArrayList<String> ();
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true&characterEncoding=" + DATABASE_ENCODING);
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true");
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/mysql?useUnicode=true&characterEncoding=" + DATABASE_ENCODING);
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/mysql?useUnicode=true");
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/test?useUnicode=true&characterEncoding=" + DATABASE_ENCODING);
					urls.add("jdbc:mysql://" + databaseHost + ":" + databasePort + "/test?useUnicode=true");
					//循环数据库访问url，只要有一个能够获取connection对象，就break出循环
					for (String url : urls) {
						try {
							connection = DriverManager.getConnection(url, databaseUsername, databasePassword);
						} catch (SQLException e) {
							connection = null;
							exception = stackToString(e);
							continue;
						}
						exception = "";
						break;
					}
					
					if (connection != null) {
						Integer databaseMajorVersion = null;
						Integer databaseMinorVersion = null;
						try {
							DatabaseMetaData databaseMetaData = connection.getMetaData();
							databaseMajorVersion = databaseMetaData.getDatabaseMajorVersion();
							databaseMinorVersion = databaseMetaData.getDatabaseMinorVersion();
						} catch (SQLException e) {
							status = "error";
							message = "JDBC执行错误!";
							exception = stackToString(e);
							try {
								if (connection != null) {
									connection.close();
									connection = null;
								}
							} catch (SQLException e1) {
							}
						}
						//创建数据库信息
						if (status.equals("success")) {
							Statement statement = connection.createStatement();
							try {
								if (databaseMajorVersion >= 5 || (databaseMajorVersion == 4 && databaseMinorVersion > 0)) {
									if (StringUtils.equalsIgnoreCase(DATABASE_ENCODING, "utf-8")) {
										statement.executeUpdate("create database if not exists `" + databaseName + "` default character set utf8");
									} else {
										statement.executeUpdate("create database if not exists `" + databaseName + "` default character set " + DATABASE_ENCODING);
									}
								} else {
									statement.executeUpdate("create database if not exists `" + databaseName + "`");
								}
							} catch (SQLException e) {
								status = "error";
								message = "自动创建数据库失败，请手动创建数据库!";
								exception = stackToString(e);
							} finally {
								try {
									if(statement != null) {
										statement.close();
										statement = null;
									}
									if(connection != null) {
										connection.close();
										connection = null;
									}
								} catch (SQLException e) {
									status = "error";
									message = "自动创建数据库失败，请手动创建数据库!";
									exception = stackToString(e);
								}
							}
						}
					} else {
						status = "error";
						message = "自动创建数据库失败，请手动创建数据库!";
					}
				} else {//如果不自动创建数据库，则校验需要的数据库是否存在
					try {
						String jdbcUrl = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true&characterEncoding=" + DATABASE_ENCODING;
						connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
					} catch (SQLException e0) {
						try {
							String jdbcUrl = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true";
							connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
						} catch (SQLException e1) {
							status = "error";
							message = "数据库连接失败,请检查数据库用户名、密码等配置信息!";
							exception = stackToString(e1);
						}
					} finally {
						try {
							if(connection != null) {
								connection.close();
								connection = null;
							}
						} catch (SQLException e) {
							status = "error";
							message = "数据库创建失败!";
							exception = stackToString(e);
						}
					}
				}
			}
		} else if (databaseType.equalsIgnoreCase("sqlserver")) {//sqlserver和oracle不能通过程序创建数据库，自能手抖创建数据库
			String jdbcUrl = "jdbc:sqlserver://" + databaseHost + ":" + databasePort + ";databasename=" + databaseName;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (Exception e) {
				status = "error";
				message = "SQL Server JDBC驱动加载失败,请检查JDBC驱动!";
				exception = stackToString(e);
			}
			
			if (status.equals("success")) {
				try {
					connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
				} catch (SQLException e) {
					status = "error";
					message = "数据库连接失败,请检查数据库用户名、密码等配置信息!";
					exception = stackToString(e);
				} finally {
					try {
						if(connection != null) {
							connection.close();
							connection = null;
						}
					} catch (SQLException e) {
						status = "error";
						message = "数据库创建失败!";
						exception = stackToString(e);
					}
				}
			}
		} else if (databaseType.equalsIgnoreCase("oracle")) {
			String jdbcUrl = "jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":" + databaseName;
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (Exception e) {
				status = "error";
				message = "Oracle JDBC驱动加载失败,请检查JDBC驱动!";
				exception = stackToString(e);
			}
			
			if (status.equals("success")) {
				try {
					connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
				} catch (SQLException e) {
					status = "error";
					message = "数据库连接失败,请检查数据库用户名、密码等配置信息!";
					exception = stackToString(e);
				} finally {
					try {
						if(connection != null) {
							connection.close();
							connection = null;
						}
					} catch (SQLException e) {
						status = "error";
						message = "数据库创建失败!";
						exception = stackToString(e);
					}
				}
			}
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>安装程序</title>
<link href="css/install.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
</head>
<body>
	<div class="header">
		<div class="title">安装程序</div>
		<div class="banner"></div>
	</div>
	<div class="body">
		<div class="bodyLeft">
			<ul class="step">
				<li>许可协议</li>
				<li>环境检测</li>
				<li>系统配置</li>
				<li id="installStep" class="current">系统安装</li>
				<li id="completeStep">完成安装</li>
			</ul>
		</div>
		<div class="bodyRight">
			<%
				if (StringUtils.equals(status, "error")) {
			%>
			<table>
				<tr>
					<th>
						系统提示
					</th>
				</tr>
				<tr>
					<td>
						<strong class="message"><%=message%></strong>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<th>
						异常信息
					</th>
				</tr>
				<tr>
					<td style="padding: 0px;">
						<div class="exception">
							<%=exception%>
						</div>
					</td>
				</tr>
			</table>
			<%
				} else {
			%>
			<table>
				<tr>
					<th>
						安装提示
					</th>
				</tr>
				<tr>
					<td>
						<div id="installMessage">正在检测数据库环境...</div>
						<div id="installLoading" class="loadingBar">&nbsp;</div>
					</td>
				</tr>
			</table>
			<table id="installExceptionTable" style="display: none;">
				<tr>
					<th>
						异常信息
					</th>
				</tr>
				<tr>
					<td style="padding: 0px;">
						<div id="installException" class="exception">
							<%=exception%>
						</div>
					</td>
				</tr>
			</table>
			<%
				}
			%>
		</div>
		<div class="buttonArea">
			<input type="button" id="previous" class="button" value="上 一 步" onclick="location.href='setting.jsp'" />&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" id="complete" class="button" value="完成安装" disabled="disabled" />
		</div>
	</div>
	<div class="footer">
		<p class="copyright">Copyright © 2005-2013 IPPH All Rights Reserved.</p>
	</div>
	<%
		if (StringUtils.equals(status, "success")) {
	%>
	<script type="text/javascript">
		$().ready(function() {
			//页面元素
			var $installStep = $("#installStep");
			var $completeStep = $("#completeStep");
			var $installMessage = $("#installMessage");
			var $installLoading = $("#installLoading");
			var $installExceptionTable = $("#installExceptionTable");
			var $installException = $("#installException");
			var $installUrl = $("#installUrl");
			var $previous = $("#previous");
			var $complete = $("#complete");
			
			installInit();
			//初始化数据库
			function installInit() {
				$.ajax({
					url: "install_init.jsp",
					type: "POST",
					dataType: "json",
					cache: false,
					beforeSend: function(data) {
						$installMessage.html("正在初始化数据库结构...");
					},
					success: function(data) {
						if (data.status == "success") {
							installConfig();
						} else {
							$installLoading.hide();
							$installMessage.html(data.message);
							$installException.html(data.exception);
							$installExceptionTable.show();
						}
					}
				});
			}
			
			function installConfig() {
				$.ajax({
					url: "install_config.jsp",
					type: "POST",
					dataType: "json",
					cache: false,
					beforeSend: function(data) {
						$installMessage.html("正在初始化系统配置...");
					},
					success: function(data) {
						if (data.status == "success") {
							$installStep.removeClass("current");
							$completeStep.addClass("current");
							$installLoading.hide();
							$previous.prop("disabled", true);
							$complete.prop("disabled", false);
							$installMessage.html('<div id="installSuccess" class="installSuccess"><strong>恭喜您，SHOP++系统安装成功，请重新启动WEB服务器！<\/strong><span>基于安全考虑，请在安装成功后删除install目录<\/span><\/div>');
							$("#installSuccess").fadeIn(2000);
							$installUrl.fadeIn(2000);
						} else {
							$installLoading.hide();
							$installMessage.html(data.message);
							$installException.html(data.exception);
							$installExceptionTable.show();
						}
					}
				});
			}
			
			$complete.click( function() {
				alert("恭喜您,系统安装成功，请重新启动WEB服务器！");
			});
			
		})
	</script>
	<%
		}
	%>
</body>
</html>