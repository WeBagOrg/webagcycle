<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.jee-soft.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/ligerui.min.js"></script>
<script charset="utf-8" src="${ctx}/js/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="${ctx}/js/kindeditor/lang/zh_CN.js"></script>
<html>
	<head>
		<script type="text/javascript">
			var editor = [];
			$(function() {
				$("textarea[name^='m:'].myeditor").each(function(num) {
					var k= KindEditor.create(this);
					editor.push(k);
				});
			});
			function getEditorData(obj){
				return obj.html();
			}
		</script>
	</head>
</html>