<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.jee-soft.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/lg/ligerui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/ueditor2/form-setting/editor_config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/ueditor2/editor_api.js"></script>
<html>
	<head>
		<script type="text/javascript">
			var editor = [];
			$(function() {
				$("textarea.myeditor").each(function(num) {
					if($(this).attr("name").indexOf("s:")==0){//子表中的富文本框
						var formTypeEdit=$(this).closest('div[formtype=edit]');
						if(formTypeEdit.length==0){
							var ue =new baidu.editor.ui.Editor({minFrameHeight:300,initialFrameWidth:800,lang:'zh_cn'});
							ue.render(this);
							editor.push(ue);	
						}
					}
					else{
						var ue =new baidu.editor.ui.Editor({minFrameHeight:300,initialFrameWidth:800,lang:'zh_cn'});
						ue.render(this);
						editor.push(ue);
					}
				});
			});
			function getEditorData(obj){
				return obj.getContent();
			}
		</script>
	</head>
</html>