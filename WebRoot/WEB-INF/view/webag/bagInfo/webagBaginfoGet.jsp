
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>回收袋信息明细</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript" src="${ctx}/js/hotent/subform.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/FlowDetailWindow.js"></script>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">回收袋信息详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<div class="group"><a class="link detail"  onclick="FlowDetailWindow({runId:${runId}})" href="javascript:;" ><span></span>流程明细</a></div>
						<div class="l-bar-separator"></div>
						<a class="link back" href="list.ht"><span></span>返回</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<table class="formTable" cellspacing="0" cellpadding="2" border="1">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">回收袋信息</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">袋子编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${webagBaginfo.bagNo}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">添加时间:</td>
   <td class="formInput" style="width:80%;">${webagBaginfo.createDate}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${webagBaginfo.creatorName}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者id:</td>
   <td class="formInput" style="width:80%;">${webagBaginfo.creatorId}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">使用次数:</td>
   <td class="formInput" style="width:80%;">${webagBaginfo.useTime}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">袋子是否启用:</td>
   <td class="formInput" style="width:80%;"><label><input name="m:webag_baginfo:bagStatus" value="是" lablename="袋子是否启用" validate="{}" type="radio" disabled="disabled" data="${webagBaginfo.bagStatus}" />是</label><label><input name="m:webag_baginfo:bagStatus" value="否" lablename="袋子是否启用" validate="{}" type="radio" disabled="disabled" data="${webagBaginfo.bagStatus}" />否</label></td>
  </tr>
 </tbody>
</table>
</body>
</html>

