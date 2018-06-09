<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@include file="/commons/include/kindeditor.jsp" %>
<html>
<head>
	<title>编辑 回收袋信息</title>
	<%@include file="/codegen/include/customForm.jsp" %>
	<script type="text/javascript" src="${ctx}/js/hotent/formdata.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/subform.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/FlowDetailWindow.js"></script>
	<script type="text/javascript">
		$(function() {
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#webagBaginfoForm').form();
			$("a.save").click(function() {
				frm.ajaxForm(options);
				$("#saveData").val(1);
				if(frm.valid()){
					//如果有编辑器的情况下
					$("textarea[name^='m:'].myeditor").each(function(num) {
						var name=$(this).attr("name");
						var data=getEditorData(editor[num]);
						$("textarea[name^='"+name+"']").val(data);
					});
					
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}
					if(OfficePlugin.officeObjs.length>0){
						OfficePlugin.submit(function(){
							frm.handleFieldName();
							$('#webagBaginfoForm').submit();
						});
					}else{
						frm.handleFieldName();
						$('#webagBaginfoForm').submit();
					}
				}
			});
			$("a.run").click(function() {
				frm.ajaxForm(options);
				$("#saveData").val(0);
				$('#webagBaginfoForm').attr("action","run.ht");
				if(frm.valid()){
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}
					data=CustomForm.getData();
					//设置表单数据
					$("#formData").val(data);
					frm.handleFieldName();
					OfficePlugin.submit();
					$('#webagBaginfoForm').submit();
				}
			});
		});
		
		function showResponse(responseText) {
			var obj = new com.hotent.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
					if(rtn){
						window.location.href = window.location.href;
					}else{
						window.location.href = "${ctx}/webag/bagInfo/webagBaginfo/list.ht";
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
	</script>
</head>
<body>
<div class="panel" style="height:100%;overflow:auto;">
	<div class="panel-top">
		<div class="tbar-title">
		    <c:choose>
			    <c:when test="${not empty webagBaginfoItem.id}">
			        <span class="tbar-label"><span></span>编辑回收袋信息</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label"><span></span>添加回收袋信息</span>
			    </c:otherwise>	
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="javascript:;"><span></span>保存</a></div>
				<div class="l-bar-separator"></div>
				<%--<c:if test="${runId==0}">
				<div class="group"><a class="link run"  href="javascript:;"><span></span>提交</a></div>
				<div class="l-bar-separator"></div>
				</c:if>--%>
				<c:if test="${runId!=0}">
					<div class="group"><a class="link detail"  onclick="FlowDetailWindow({runId:${runId}})" href="javascript:;" ><span></span>流程明细</a></div>
					<div class="l-bar-separator"></div>
				</c:if>
				<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
			</div>
		</div>
	</div>
	<form id="webagBaginfoForm" method="post" action="save.ht">
		<div type="custform">
			<div class="panel-detail">
				<table class="formTable" cellspacing="0" cellpadding="2" border="1">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">回收袋信息</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">袋子编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBaginfo:bagNo" lablename="袋子编号" class="inputText" validate="{maxlength:54}" isflag="tableflag" type="text" value="${webagBaginfo.bagNo}" readonly="readonly"/></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">添加时间:</td>
   <td class="formInput" style="width:80%;"><input readonly="readonly" name="m:webagBaginfo:createDate" class="Wdate" displaydate="1" datefmt="yyyy-MM-dd" validate="{empty:false}" type="text" value="<fmt:formatDate value='${webagBaginfo.createDate}' pattern='yyyy-MM-dd' />" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input readonly="readonly" name="m:webagBaginfo:creatorName" lablename="创建者" class="inputText" validate="{maxlength:600}" isflag="tableflag" type="text" value="${webagBaginfo.creatorName}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者id:</td>
   <td class="formInput" style="width:80%;"><input readonly="readonly" name="m:webagBaginfo:creatorId" showtype="{"coinValue":"","isShowComdify":0,"decimalValue":0}" validate="{number:true,maxIntLen:18,maxDecimalLen:0}" type="text" value="${webagBaginfo.creatorId}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">使用次数:</td>
   <td class="formInput" style="width:80%;"><input readonly="readonly" name="m:webagBaginfo:useTime" showtype="validate="{number:true,maxIntLen:19,maxDecimalLen:0}"" type="text" value="${webagBaginfo.useTime}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">袋子是否启用:</td>
   <td class="formInput" style="width:80%;"><label><input name="m:webagBaginfo:bagStatus" value="是" lablename="袋子是否启用" validate="{}" type="radio" data="${webagBaginfo.bagStatus}" />是</label><label><input name="m:webagBaginfo:bagStatus" value="否" lablename="袋子是否启用" validate="{}" type="radio" data="${webagBaginfo.bagStatus}" />否</label></td>
  </tr>
  <c:if test="${not empty webagBaginfo.id}">
  <tr>
	  <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">二维码:</td>
	  <td class="formInput" style="width:80%;"><img src="file:///${webagBaginfo.QRUrl}" width="200px" height="200px"/></td>
  </tr>
  </c:if>
 </tbody>
</table>
			</div>
		</div>
		<input type="hidden" name="id" value="${webagBaginfo.id}"/>
		<input type="hidden" id="saveData" name="saveData"/>
		<input type="hidden" name="executeType"  value="start" />
	</form>
</body>
</html>
