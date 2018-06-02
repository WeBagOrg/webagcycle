<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>webag_waste_type管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	function startFlow(obj,id){
		var linkObj=$(obj);
		if(!linkObj.hasClass('disabled')) {
			linkObj.addClass('disabled');
			$.post("run.ht?isList=1&id="+id+"&saveData=0&executeType=start",function(responseText){
				
				var obj = new com.hotent.form.ResultMessage(responseText);
				if (obj.isSuccess()) {
					$.ligerDialog.success("启动流程成功！", "成功", function(rtn) {
						this.close();
						window.location.href = "${ctx}/webag/wasteType/webagWasteType/list.ht";
					});
				} else {
					$.ligerDialog.error(obj.getMessage(),"提示信息");
					linkObj.removeClass("disabled");
				}
				
			});
		}
		
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">webag_waste_type管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht"><span></span>修改</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
					<div class="row">
						<span class="label">id:</span><input type="text" name="Q_id_L"  class="inputText" />
						<span class="label">废品类型编号:</span><input type="text" name="Q_wasteTypeNo_S"  class="inputText" />
						<span class="label">废品类型名称:</span><input type="text" name="Q_wasteTypeName_S"  class="inputText" />
						<span class="label">废品类型单价:</span><input type="text" name="Q_wasteTypePrice_L"  class="inputText" />
						<span class="label">废品类型描述信息:</span><input type="text" name="Q_wasteTypeDescribe_S"  class="inputText" />
						<span class="label">创建时间 从:</span> <input  name="Q_begincreateDate_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endcreateDate_DG" class="inputText date" />
						<span class="label">创建者id:</span><input type="text" name="Q_creatorId_L"  class="inputText" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="webagWasteTypeList" id="webagWasteTypeItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${webagWasteTypeItem.id}">
				</display:column>
				<display:column property="wasteTypeNo" title="废品类型编号" sortable="true" sortName="WASTETYPENO"></display:column>
				<display:column property="wasteTypeName" title="废品类型名称" sortable="true" sortName="WASTETYPENAME"></display:column>
				<display:column property="wasteTypePrice" title="废品类型单价" sortable="true" sortName="WASTETYPEPRICE"></display:column>
				<display:column property="wasteTypeDescribe" title="废品类型描述信息" sortable="true" sortName="WASTETYPEDESCRIBE"></display:column>
				<display:column  title="创建时间" sortable="true" sortName="CREATEDATE">
					<fmt:formatDate value="${webagWasteTypeItem.createDate}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column property="creatorId" title="创建者id" sortable="true" sortName="CREATORID"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<c:if test="${webagWasteTypeItem.runId==0}">
						<a href="del.ht?id=${webagWasteTypeItem.id}" class="link del"><span></span>删除</a>
						<a href="javascript:;" onclick="startFlow(this,'${webagWasteTypeItem.id}')" class="link run"><span></span>提交</a>
					</c:if>
					<a href="edit.ht?id=${webagWasteTypeItem.id}" class="link edit"><span></span>编辑</a>
					<a href="get.ht?id=${webagWasteTypeItem.id}" class="link detail"><span></span>明细</a>
				</display:column>
			</display:table>
			<hotent:paging tableId="webagWasteTypeItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


