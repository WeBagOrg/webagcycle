<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@include file="/commons/include/kindeditor.jsp" %>
<html>
<head>
	<title>编辑 用户流水明细表</title>
	<%@include file="/codegen/include/customForm.jsp" %>
	<script type="text/javascript" src="${ctx}/js/hotent/formdata.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/subform.js"></script>
	<script type="text/javascript">
		$(function() {
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#webagBillDetailForm').form();
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
							$('#webagBillDetailForm').submit();
						});
					}else{
						frm.handleFieldName();
						$('#webagBillDetailForm').submit();
					}
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
						window.location.href = "${ctx}/webag/webag/webagBillDetail/list.ht";
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
			    <c:when test="${not empty webagBillDetailItem.id}">
			        <span class="tbar-label"><span></span>编辑用户流水明细表</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label"><span></span>添加用户流水明细表</span>
			    </c:otherwise>	
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="javascript:;"><span></span>保存</a></div>
				<div class="l-bar-separator"></div>
				<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
			</div>
		</div>
	</div>
	<form id="webagBillDetailForm" method="post" action="save.ht">
		<div type="custform">
			<div class="panel-detail">
				<table class="formTable" cellspacing="0" cellpadding="2" border="1">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">用户流水明细表</td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">用户微信openID:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:userWeChatID" lablename="用户微信openID" class="inputText" validate="{maxlength:300}" isflag="tableflag" type="text" value="${webagBillDetail.userWeChatID}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">回收袋编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:bagNo" lablename="回收袋编号" class="inputText" validate="{maxlength:150}" isflag="tableflag" type="text" value="${webagBillDetail.bagNo}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">创建时间:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagBillDetail:creatTime" class="Wdate" displaydate="0" datefmt="yyyy-MM-dd" validate="{empty:false}" type="text" value="<fmt:formatDate value='${webagBillDetail.creatTime}' pattern='yyyy-MM-dd'/>" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">操作员id:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagBillDetail:staffId" showtype="validate="{number:true,maxIntLen:19,maxDecimalLen:0}"" type="text" value="${webagBillDetail.staffId}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">职工姓名:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:staffName" lablename="职工姓名" class="inputText" validate="{maxlength:150}" isflag="tableflag" type="text" value="${webagBillDetail.staffName}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">废品类别:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:wasteType" lablename="废品类别" class="inputText" validate="{maxlength:600}" isflag="tableflag" type="text" value="${webagBillDetail.wasteType}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">称重重量:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagBillDetail:weight" showtype="validate="{number:true,maxIntLen:18,maxDecimalLen:4}"" type="text" value="${webagBillDetail.weight}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">单价:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagBillDetail:unitPrice" showtype="validate="{number:true,maxIntLen:18,maxDecimalLen:4}"" type="text" value="${webagBillDetail.unitPrice}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">总价:</td>
   <td class="formInput" style="width:80%;"><input name="m:webagBillDetail:totalPrice" showtype="validate="{number:true,maxIntLen:18,maxDecimalLen:4}"" type="text" value="${webagBillDetail.totalPrice}" /></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">是否给用户发送消息:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:isSendMsg" lablename="是否给用户发送消息" class="inputText" validate="{maxlength:30}" isflag="tableflag" type="text" value="${webagBillDetail.isSendMsg}" /></span></td>
  </tr>
  <tr>
   <td style="width:20%;" class="formTitle" nowrap="nowarp" align="right">用户昵称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input name="m:webagBillDetail:nickName" lablename="用户昵称" class="inputText" validate="{maxlength:600}" isflag="tableflag" type="text" value="${webagBillDetail.nickName}" /></span></td>
  </tr>
 </tbody>
</table>
			</div>
		</div>
		<input type="hidden" name="id" value="${webagBillDetail.id}"/>
		<input type="hidden" id="saveData" name="saveData"/>
		<input type="hidden" name="executeType"  value="start" />
	</form>
</body>
</html>
