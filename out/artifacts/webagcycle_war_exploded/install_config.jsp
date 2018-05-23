<%@page import="install.util.ResourceUtil"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.springframework.core.io.Resource"%>
<%@page import="org.springframework.core.io.ClassPathResource"%>
<%@page import="org.springframework.core.io.support.PropertiesLoaderUtils"%>
<%@page import="install.constants.CommonAttributes"%>
<%@page import="install.util.JsonUtils"%>
<%@include file="common.jsp"%>
<%
	Boolean isAgreeAgreement = (Boolean) session.getAttribute("isAgreeAgreement");
	if (isAgreeAgreement == null || !isAgreeAgreement) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	//获取session中页面设置的数据库信息
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

	String jdbcDriver = null;
	String jdbcUrl = null;
	Integer databaseMajorVersion = null;
	Integer databaseMinorVersion = null;
	String hibernateDialect = null;

	if (status.equals("success")) {
		if (databaseType.equalsIgnoreCase("mysql")) {
			jdbcDriver = "com.mysql.jdbc.Driver";
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
		} else if (databaseType.equalsIgnoreCase("sqlserver")) {
			jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			jdbcUrl = "jdbc:sqlserver://" + databaseHost + ":" + databasePort + ";databasename=" + databaseName;
		} else if (databaseType.equalsIgnoreCase("oracle")) {
			jdbcDriver = "oracle.jdbc.OracleDriver";
			jdbcUrl = "jdbc:oracle:thin:@" + databaseHost + ":" + databasePort + ":" + databaseName;
		} else {
			status = "error";
			message = "参数错误!";
		}
	}

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

	if (status.equals("success")) {
		
		try {
			//待修改的属性文件路径
			String proertiesFilePath=ResourceUtil.getPropertyValue("propertyFile.path")+ResourceUtil.getPropertyValue("db.propertyFile");
			Properties properties=new Properties();
			properties.load(new FileInputStream(rootPath +proertiesFilePath));
			properties.setProperty("jdbc.driverClassName", jdbcDriver);
			properties.setProperty("jdbc.url", jdbcUrl);
			properties.setProperty("jdbc.username", databaseUsername);
			properties.setProperty("jdbc.password", databasePassword);
			OutputStream outputStream = new FileOutputStream(new File(rootPath +proertiesFilePath));
			properties.store(outputStream, " PROPERTIES");
			outputStream.close();
		} catch (IOException e) {
			status = "error";
			message = "属性文件写入失败!";
			exception = stackToString(e);
		}
	}

	//覆盖web。xml
	if (status.equals("success")) {
		try {
			File webXmlFile = new File(rootPath + WEB_XML_PATH);
			File webXmlInstallFile = new File(rootPath + WEB_XML_INSTALL_PATH);
			String webXmlString = FileUtils.readFileToString(webXmlFile,"utf-8");
			FileUtils.writeStringToFile(webXmlInstallFile, webXmlString,"utf-8");
		} catch (IOException e) {
			status = "error";
			message = "WEB.XML文件写入失败!";
			exception = stackToString(e);
		}
	}
	
	if (status.equals("success")) {
		try {
			FileUtils.writeStringToFile(new File(rootPath + INSTALL_LOCK_CONFIG_PATH), " INSTALL LOCK ");
		} catch (IOException e) {
			status = "error";
			message = "INSTALL_LOCK.CONFIG文件写入失败!";
			exception = stackToString(e);
		}
	}

	if (status.equals("success")) {
		try {
			FileUtils.writeStringToFile(new File(rootPath + INSTALL_INIT_CONFIG_PATH), " INSTALL");
		} catch (IOException e) {
			status = "error";
			message = "INSTALL_INIT.CONFIG文件写入失败!";
			exception = stackToString(e);
		}
	}

	Enumeration<Driver> drivers = DriverManager.getDrivers();
	while (drivers.hasMoreElements()) {
		Driver driver = drivers.nextElement();
		try {
			DriverManager.deregisterDriver(driver);
		} catch (SQLException e) {

		}
	}

	Map<String, String> data = new HashMap<String, String>();
	data.put("status", status);
	data.put("message", message);
	data.put("exception", exception);
	JsonUtils.writeValue(response.getWriter(), data);
%>