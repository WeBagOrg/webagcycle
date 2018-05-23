<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>
<%@page import="install.util.FreemarkerUtils"%>
<%@page import="install.util.JsonUtils"%>
<%@include file="common.jsp"%>
<%
	Boolean isAgreeAgreement = (Boolean) session.getAttribute("isAgreeAgreement");
	if (isAgreeAgreement == null || !isAgreeAgreement) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	//从session中获取页面配置的数据库信息
	String databaseType = (String) session.getAttribute("databaseType");
	String databaseHost = (String) session.getAttribute("databaseHost");
	String databasePort = (String) session.getAttribute("databasePort");
	String databaseUsername = (String) session.getAttribute("databaseUsername");
	String databasePassword = (String) session.getAttribute("databasePassword");
	String databaseName = (String) session.getAttribute("databaseName");
	
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

	String jdbcUrl = null;
	File sqlFile = null;
	Integer databaseMajorVersion = null;
	Integer databaseMinorVersion = null;

	if (status.equals("success")) {
		if (databaseType.equalsIgnoreCase("mysql")) {
			Connection connection = null;
			try {
				jdbcUrl = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true&characterEncoding=" + DATABASE_ENCODING;
				connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
			} catch (SQLException e) {
				jdbcUrl = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true";
			} finally {
				try {
					if (connection != null) {
						connection.close();
						connection = null;
					}
				} catch (SQLException e) {
				}
			}
			sqlFile = new File(rootPath + "/install/data/mysql/init.sql");
		} else if (databaseType.equalsIgnoreCase("sqlserver")) {
			jdbcUrl = "jdbc:sqlserver://" + databaseHost + ":" + databasePort + ";DatabaseName=" + databaseName;
			sqlFile = new File(rootPath + "/install/data/sqlserver/init.sql");
		} else if (databaseType.equalsIgnoreCase("oracle")) {
			jdbcUrl = "jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":" + databaseName;
			sqlFile = new File(rootPath + "/install/data/oracle/init.sql");
		} else {
			status = "error";
			message = "参数错误!";
		}
	}
	
	//判断执行的sql文件是否存在
	if (status.equals("success") && (sqlFile == null || !sqlFile.exists())) {
		status = "error";
		message = "init.sql文件不存在!";
	}
	
	//判断数据库是否能正常访问
	if (status.equals("success")) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			databaseMajorVersion = databaseMetaData.getDatabaseMajorVersion();
			databaseMinorVersion = databaseMetaData.getDatabaseMinorVersion();
		} catch (SQLException e) {
			status = "error";
			message = "JDBC执行错误!";
			exception = stackToString(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				status = "error";
				message = "JDBC执行错误!";
				exception = stackToString(e);
			}
		}
	}
	
	//获取数据库连接，执行sql
	if (status.equals("success")) {
		Connection connection = null;
		Statement statement = null;
		String currentSQL = null;

		try {
			connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			//使用freemarker，如果sql中有freemarker的占位符，这里可以动态的传递参数，生成相应的sql语句
			Map<String, Object> model = new HashMap<String, Object>();
			for (String line : FileUtils.readLines(sqlFile, "utf-8")) {
				if (StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, "--")) {//忽略sql语句的注释内容
					currentSQL = line;//FreemarkerUtils.process(line, model);
					statement.executeUpdate(currentSQL);
				}
			}
			connection.commit();
			currentSQL = null;
		} catch (SQLException e) {
			status = "error";
			message = "JDBC执行错误!";
			exception = stackToString(e);
			if (currentSQL != null) {
				exception = "SQL: " + currentSQL + "<br />" + exception;
			}
		} catch (IOException e) {
			status = "error";
			message = "init.sql文件读取失败!";
			exception = stackToString(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
					statement = null;
				}
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				status = "error";
				message = "JDBC执行错误!";
				exception = stackToString(e);
			}
		}
	}

	Map<String, String> data = new HashMap<String, String>();
	data.put("status", status);
	data.put("message", message);
	data.put("exception", exception);
	JsonUtils.writeValue(response.getWriter(), data);
%>