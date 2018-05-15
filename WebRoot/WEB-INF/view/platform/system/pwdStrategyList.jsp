<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>密码策略管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bus/BusQueryRuleUtil.js" ></script>
<script type="text/javascript">
	function setEnable(ids,enable){
		var url=__ctx +"/platform/system/pwdStrategy/ngjs.ht";
		$.post(url,{ids:ids,enable:enable,action:"setEnable"},function(data){
			if(data.status){
				$.ligerDialog.success(data.msg,"提示信息",function(){
					$("#btnSearch").click();
				});
			}else{
				 $.ligerDialog.error(data.msg,"提示信息");
			}
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<c:if test="${!empty busQueryRule.filterList && fn:length(busQueryRule.filterList) >1}">
			<div class="l-tab-links">
				<ul style="left: 0px; ">
					<c:forEach items="${busQueryRule.filterList}" var="filter">
						<li tabid="${filter.key}" <c:if test="${busQueryRule.filterKey ==filter.key}"> class="l-selected"</c:if>>
							<a href="list.ht?__FILTERKEY__=${filter.key}" title="${filter.name}">${filter.desc}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">密码策略管理列表</span>
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
					<div class="group" style="float: right;">
						<f:a onclick="BusQueryRuleUtil.saveFilter({tableName:'${busQueryRule.tableName}',filterKey:'${busQueryRule.filterKey}',filterFlag:'${busQueryRule.filterFlag}'})" alias="saveFilter_${busQueryRule.tableName}" css="link save"  showNoRight="false"><span></span>保存条件</f:a>
						<f:a onclick="BusQueryRuleUtil.myFilter({tableName:'${busQueryRule.tableName}',url:'${busQueryRule.url}'})" alias="myFilter_${busQueryRule.tableName}" css="link ok"  showNoRight="false"><span></span>过滤器</f:a>
						<f:a onclick="BusQueryRuleUtil.eidtDialog({tableName:'${busQueryRule.tableName}'})" alias="customQuery_${busQueryRule.tableName}" css="link setting" showNoRight="false" ><span></span>高级查询</f:a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht?__FILTERKEY__=${busQueryRule.filterKey}&__IS_QUERY__=0">
					<div class="row">
						<span class="label">是否启动:</span>
						<select name="Q_enable_SN">
							<option></option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="pwdStrategyList" id="pwdStrategyItem" requestURI="list.ht?__FILTERKEY__=${busQueryRule.filterKey}&__FILTER_FLAG__=${busQueryRule.filterFlag}" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<f:col name="id">
					<display:column title="${checkAll}" media="html" style="width:30px;">
				  		<input type="checkbox" class="pk" name="id" value="${pwdStrategyItem.id}">
					</display:column>
				</f:col>
				<f:col name="initPwd">
					<display:column property="initPwd" title="初始化密码" maxLength="80"></display:column>
				</f:col>
				<f:col name="pwdRule">
					<display:column title="密码策略" maxLength="80">
					<c:choose>
						<c:when test="${pwdStrategyItem.pwdRule==0}">
							<span class="red">无限制</span>
						</c:when>
						<c:when test="${pwdStrategyItem.pwdRule==1}">
							<span class="green">数字加字母</span>
						</c:when>
						<c:when test="${pwdStrategyItem.pwdRule==2}">
							<span class="green">数字加字母加特殊字符</span>
						</c:when>
					</c:choose>
					</display:column>
				</f:col>
				<f:col name="pwdLength">
					<display:column title="密码长度" maxLength="80">
					<c:choose>
						<c:when test="${pwdStrategyItem.pwdLength==0}">
							<span class="red">无限制</span>
						</c:when>
						<c:otherwise>
							<span class="green">${pwdStrategyItem.pwdLength}</span>
						</c:otherwise>
					</c:choose>
					</display:column>
				</f:col>
				<f:col name="validity">
					<display:column title="密码有效期" maxLength="80">
					<c:choose>
						<c:when test="${pwdStrategyItem.validity==0}">
							<span class="red">不处理</span>
						</c:when>
						<c:otherwise>
							<span class="green">锁定账号</span>
						</c:otherwise>
					</c:choose>
					</display:column>
				</f:col>
				<f:col name="handleOverdue">
					<display:column title="密码过期处理" maxLength="80">
					<c:choose>
						<c:when test="${pwdStrategyItem.handleOverdue==0}">
							<span class="red">不处理</span>
						</c:when>
						<c:otherwise>
							<span class="green"></span>
						</c:otherwise>
					</c:choose>
					</display:column>
				</f:col>
				<f:col name="enable">
					<display:column title="是否启动" sortable="true" sortName="ENABLE_" maxLength="80">
					<c:choose>
						<c:when test="${pwdStrategyItem.enable==0}">
							<span class="red">否</span>
						</c:when>
						<c:otherwise>
							<span class="green">是</span>
						</c:otherwise>
					</c:choose>
					</display:column>
				</f:col>
				<display:column title="管理" media="html" style="width:220px">
					<a href="del.ht?id=${pwdStrategyItem.id}" class="link del">删除</a>
					<a href="edit.ht?id=${pwdStrategyItem.id}" class="link edit">编辑</a>
					<c:choose>
						<c:when test="${pwdStrategyItem.enable==0}">
							<a onclick="setEnable(${pwdStrategyItem.id},1)" class="link run">启动</a>
						</c:when>
						<c:otherwise>
							<a onclick="setEnable(${pwdStrategyItem.id},0)" class="link stop">禁用</a>
						</c:otherwise>
					</c:choose>
				</display:column>
			</display:table>
			<hotent:paging tableId="pwdStrategyItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


