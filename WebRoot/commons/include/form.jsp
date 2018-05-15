<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.jee-soft.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="hotent" uri="http://www.jee-soft.cn/paging" %>
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<f:link href="Aqua/css/ligerui-all.css" ></f:link>
<f:link href="tree/zTreeStyle.css" ></f:link>
<f:link href="web.css" ></f:link>
<f:link href="form.css" ></f:link>
<f:js pre="js/lang/common" ></f:js>
<f:js pre="js/lang/js" ></f:js>
<f:js pre="js/lang/view/platform/system" ></f:js>
<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/additional-methods.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.validate.ext.js"></script>
<script type="text/javascript" src="${ctx}/js/util/util.js"></script>
<script type="text/javascript" src="${ctx}/js/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/js/util/form.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/ligerui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="${ctx}/js/lg/plugins/ligerResizable.js" ></script>
<script type="text/javascript" src="${ctx}/js/calendar/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/util/DialogUtil.js" ></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/system/Share.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/system/HtmlUploadDialog.js"></script>
<script type="text/javascript" src="${ctx }/js/hotent/platform/system/FlexUploadDialog.js"></script>
<script type="text/javascript" src="${ctx }/js/util/date.js"></script>

<link rel="stylesheet" href="${ctx}/js/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="${ctx}/js/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${ctx}/js/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="${ctx}/js/kindeditor/lang/zh_CN.js"></script>
<script charset="utf-8" src="${ctx}/js/kindeditor/plugins/code/prettify.js"></script>


<%-- <script type="text/javascript" src="${ctx }/js/hotent/platform/form/AttachMent.js"	></script> 
 --%>
 <script type="text/javascript"	src="${ctx}/js/jquery/plugins/jquery.attach.js"></script>
<link   href="${ctx}/js/jquery/plugins/attach.css" rel="stylesheet" />

<script type="text/javascript" src="${ctx}/codegen/js/PictureShowPlugin.js"></script>
<script type="text/javascript" src="${ctx}/codegen/js/NtkoWebSign.js"></script>
<script type="text/javascript" src="${ctx}/codegen/js/WebSignPlugin.js"></script>
<script type="text/javascript" src="${ctx}/codegen/js/FormUtil.js"></script>
<script type="text/javascript" src="${ctx}/codegen/js/FormInit.js"></script>

<script type="text/javascript" src="${ctx}/js/hotent/platform/form/ReadOnlyQuery.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/form/OfficePlugin.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/form/FormMath.js"></script>
<script type="text/javascript" src="${ctx}/js/hotent/platform/form/Cascadequery.js"></script>


<!-- 替换原有附件上传控件 -->
 <script type="text/javascript" src="${ctx}/codegen/js/AttachMentTemp.js"></script>

