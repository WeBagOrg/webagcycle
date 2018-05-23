<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/codegen/include/customForm.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/js/hotent/scriptMgr.js"></script>
<script type="text/javascript">
	function afterOnload(){
		var afterLoadJs=[
		     			'${ctx}/js/hotent/formdata.js',
		     			'${ctx}/js/hotent/subform.js'
		 ];
		ScriptMgr.load({
			scripts : afterLoadJs
		});
	}
</script>
<table cellpadding="2" cellspacing="0" border="1" class="formTable">
 <tbody>
  <tr>
   <td colspan="2" class="formHead">ypxxdemo</td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">样品编号:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input type="text" name="m:ypxxdemo:ypbh" lablename="样品编号" class="inputText" validate="{maxlength:50}" isflag="tableflag" value="${ypxxdemo.ypbh}" /></span></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">样品名称:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input type="text" name="m:ypxxdemo:ypmc" lablename="样品名称" class="inputText" validate="{maxlength:100}" isflag="tableflag" value="${ypxxdemo.ypmc}" /></span></td>
  </tr>
  <tr>
   <td align="right" nowrap="nowarp" style="width:20%;" class="formTitle">采样人:</td>
   <td class="formInput" style="width:80%;"><span name="editable-input" style="display:inline-block;" isflag="tableflag"><input type="text" name="m:ypxxdemo:cyr" lablename="采样人" class="inputText" validate="{maxlength:100}" isflag="tableflag" value="${ypxxdemo.cyr}" /></span></td>
  </tr>
 </tbody>
</table>
