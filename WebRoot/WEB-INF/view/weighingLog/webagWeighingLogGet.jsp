
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>webag_weighing_log明细</title>
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
				<span class="tbar-label">webag_weighing_log详细信息</span>
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
   <td colspan="2" class="formHead">webag_weighing_log</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">称重日志编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${webagWeighingLog.weighingLogNo}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类型编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${webagWeighingLog.wasteTypeNo}</span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">回收袋编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag">${webagWeighingLog.bagNo}</span></td>
  </tr>
  <tr>
      <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">当时的价格:</td>
      <td class="formInput" style="width:80%;">${webagWeighingLog.thePriceOfTheTime}</td>
  </tr>
  <tr>
      <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">重量:</td>
      <td class="formInput" style="width:80%;">${webagWeighingLog.weight}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">返利金额:</td>
   <td class="formInput" style="width:80%;">${webagWeighingLog.amountOfRebate}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建时间:</td>
   <td class="formInput" style="width:80%;">${webagWeighingLog.createDate}</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者id:</td>
   <td class="formInput" style="width:80%;">${webagWeighingLog.creatorId}</td>
  </tr>
 </tbody>
</table>
</body>
</html>

