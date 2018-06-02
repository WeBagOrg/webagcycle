<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>用户流水明细表管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">用户流水明细表管理列表</span>
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
						<span class="label">用户微信openID:</span><input type="text" name="Q_userWeChatID_S"  class="inputText" />
						<span class="label">回收袋编号:</span><input type="text" name="Q_bagNo_S"  class="inputText" />
						<span class="label">创建时间 从:</span> <input  name="Q_begincreatTime_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endcreatTime_DG" class="inputText date" />
						<span class="label">操作员id:</span><input type="text" name="Q_staffId_L"  class="inputText" />
						<span class="label">职工姓名:</span><input type="text" name="Q_staffName_S"  class="inputText" />
						<span class="label">废品类别:</span><input type="text" name="Q_wasteType_S"  class="inputText" />
						<span class="label">称重重量:</span><input type="text" name="Q_weight_L"  class="inputText" />
						<span class="label">单价:</span><input type="text" name="Q_unitPrice_L"  class="inputText" />
						<span class="label">总价:</span><input type="text" name="Q_totalPrice_L"  class="inputText" />
						<span class="label">是否给用户发送消息:</span><input type="text" name="Q_isSendMsg_S"  class="inputText" />
						<span class="label">用户昵称:</span><input type="text" name="Q_nickName_S"  class="inputText" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="webagBillDetailList" id="webagBillDetailItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${webagBillDetailItem.id}">
				</display:column>
				<display:column property="userWeChatID" title="用户微信openID" sortable="true" sortName="USERWECHATID"></display:column>
				<display:column property="bagNo" title="回收袋编号" sortable="true" sortName="BAGNO"></display:column>
				<display:column  title="创建时间" sortable="true" sortName="CREATTIME">
					<fmt:formatDate value="${webagBillDetailItem.creatTime}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column property="staffId" title="操作员id" sortable="true" sortName="STAFFID"></display:column>
				<display:column property="staffName" title="职工姓名" sortable="true" sortName="STAFFNAME"></display:column>
				<display:column property="wasteType" title="废品类别" sortable="true" sortName="WASTETYPE"></display:column>
				<display:column property="weight" title="称重重量" sortable="true" sortName="WEIGHT"></display:column>
				<display:column property="unitPrice" title="单价" sortable="true" sortName="UNITPRICE"></display:column>
				<display:column property="totalPrice" title="总价" sortable="true" sortName="TOTALPRICE"></display:column>
				<display:column property="isSendMsg" title="是否给用户发送消息" sortable="true" sortName="ISSENDMSG"></display:column>
				<display:column property="nickName" title="用户昵称" sortable="true" sortName="NICKNAME"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.ht?id=${webagBillDetailItem.id}" class="link del"><span></span>删除</a>
					<a href="edit.ht?id=${webagBillDetailItem.id}" class="link edit"><span></span>编辑</a>
					<a href="get.ht?id=${webagBillDetailItem.id}" class="link detail"><span></span>明细</a>
				</display:column>
			</display:table>
			<hotent:paging tableId="webagBillDetailItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


