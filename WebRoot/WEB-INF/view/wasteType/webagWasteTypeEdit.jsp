<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@include file="/commons/include/kindeditor.jsp" %>
<html>
<head>
	<title>编辑 webag_waste_type</title>
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
			var frm=$('#webagWasteTypeForm').form();
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
							$('#webagWasteTypeForm').submit();
						});
					}else{
						frm.handleFieldName();
						$('#webagWasteTypeForm').submit();
					}
				}
			});
			$("a.run").click(function() {
				frm.ajaxForm(options);
				$("#saveData").val(0);
				$('#webagWasteTypeForm').attr("action","run.ht");
				if(frm.valid()){
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}
					data=CustomForm.getData();
					//设置表单数据
					$("#formData").val(data);
					frm.handleFieldName();
					OfficePlugin.submit();
					$('#webagWasteTypeForm').submit();
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
						window.location.href = "${ctx}/webag/wasteType/webagWasteType/list.ht";
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
			    <c:when test="${not empty webagWasteTypeItem.id}">
			        <span class="tbar-label"><span></span>编辑webag_waste_type</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label"><span></span>添加webag_waste_type</span>
			    </c:otherwise>	
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="javascript:;"><span></span>保存</a></div>
				<div class="l-bar-separator"></div>
				<c:if test="${runId==0}">
				<div class="group"><a class="link run"  href="javascript:;"><span></span>提交</a></div>
				<div class="l-bar-separator"></div>
				</c:if>
				<c:if test="${runId!=0}">
					<div class="group"><a class="link detail"  onclick="FlowDetailWindow({runId:${runId}})" href="javascript:;" ><span></span>流程明细</a></div>
					<div class="l-bar-separator"></div>
				</c:if>
				<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
			</div>
		</div>
	</div>
	<form id="webagWasteTypeForm" method="post" action="save.ht">
		<div type="custform">
			<div class="panel-detail">
				<table class="formTable" cellspacing="0" cellpadding="2" border="1">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">webag_waste_type</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类型编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagWasteType:wasteTypeNo" lablename="废品类型编号" class="inputText" validate="{maxlength:100}" isflag="tableflag" type="text" value="${webagWasteType.wasteTypeNo}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类型名称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagWasteType:wasteTypeName" lablename="废品类型名称" class="inputText" validate="{maxlength:256}" isflag="tableflag" type="text" value="${webagWasteType.wasteTypeName}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类型单价:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagWasteType:wasteTypePrice" validate="{maxlength:10}" type="text" value="${webagWasteType.wasteTypePrice}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类型描述信息:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagWasteType:wasteTypeDescribe" lablename="废品类型描述信息" class="inputText" validate="{maxlength:512}" isflag="tableflag" type="text" value="${webagWasteType.wasteTypeDescribe}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建时间:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagWasteType:createDate" class="Wdate" displaydate="1" datefmt="yyyy-MM-dd HH:mm:ss" validate="{empty:false}" type="text" value="<fmt:formatDate value='${webagWasteType.createDate}' pattern='yyyy-MM-dd'/>" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建者id:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagWasteType:creatorId" showtype="{"coinValue":"","isShowComdify":0,"decimalValue":0}" validate="{number:true,maxIntLen:18,maxDecimalLen:0}" type="text" value="${webagWasteType.creatorId}" /></td>
  </tr>
 </tbody>
</table>
			</div>
		</div>
		<input type="hidden" name="id" value="${webagWasteType.id}"/>
		<input type="hidden" id="saveData" name="saveData"/>
		<input type="hidden" name="executeType"  value="start" />
	</form>
</body>
</html>
