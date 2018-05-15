<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>ypxxdemo管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">ypxxdemo管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
				</div>	
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="ypxxdemoList" id="ypxxdemoItem" requestURI="getMyDraftJson.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${ypxxdemoItem.id}">
				</display:column>
				<display:column property="ypbh" title="样品编号" sortable="true" sortName="F_YPBH"></display:column>
				<display:column property="ypmc" title="样品名称" sortable="true" sortName="F_YPMC"></display:column>
				<display:column property="cyr" title="采样人" sortable="true" sortName="F_CYR"></display:column>
				<display:column title="管理" media="html" style="width:220px">
				   <a href="edit.ht?id=${ypxxdemoItem.id}&taskId=${ypxxdemoItem.taskId}" class="link edit"><span></span>编辑</a>
				</display:column>
			</display:table>
			<hotent:paging tableId="ypxxdemoItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


