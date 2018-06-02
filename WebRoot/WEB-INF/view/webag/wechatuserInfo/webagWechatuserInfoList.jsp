<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>微信用户表管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">微信用户表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht"><span></span>修改</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.ht"><span></span>删除</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
					<div class="row">
						<span class="label">id:</span><input type="text" name="Q_id_L"  class="inputText" />
						<span class="label">昵称:</span><input type="text" name="Q_nickName_S"  class="inputText" />
						<span class="label">微信openId:</span><input type="text" name="Q_openId_S"  class="inputText" />
						<span class="label">省份:</span><input type="text" name="Q_province_S"  class="inputText" />
						<span class="label">城市:</span><input type="text" name="Q_city_S"  class="inputText" />
						<span class="label">注册时间 从:</span> <input  name="Q_begincreateTime_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endcreateTime_DG" class="inputText date" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="webagWechatuserInfoList" id="webagWechatuserInfoItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${webagWechatuserInfoItem.id}">
				</display:column>
				<display:column property="nickName" title="昵称" sortable="true" sortName="NICKNAME"></display:column>
				<display:column property="openId" title="微信openId" sortable="true" sortName="OPENID"></display:column>
				<display:column property="province" title="省份" sortable="true" sortName="PROVINCE"></display:column>
				<display:column property="city" title="城市" sortable="true" sortName="CITY"></display:column>
				<display:column  title="注册时间" sortable="true" sortName="CREATETIME">
					<fmt:formatDate value="${webagWechatuserInfoItem.createTime}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.ht?id=${webagWechatuserInfoItem.id}" class="link del"><span></span>删除</a>
					<a href="edit.ht?id=${webagWechatuserInfoItem.id}" class="link edit"><span></span>编辑</a>
					<a href="get.ht?id=${webagWechatuserInfoItem.id}" class="link detail"><span></span>明细</a>
				</display:column>
			</display:table>
			<hotent:paging tableId="webagWechatuserInfoItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


