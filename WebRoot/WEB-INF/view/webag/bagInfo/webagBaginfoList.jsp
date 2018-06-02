<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>回收袋信息管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
    $(function() {
        var options={};
        if(showResponse){
            options.success=showResponse;
        }
        var frm=$('#webagBaginfoForm').form();
	});
	function startFlow(obj,id){
		var linkObj=$(obj);
		if(!linkObj.hasClass('disabled')) {
			linkObj.addClass('disabled');
			$.post("run.ht?isList=1&id="+id+"&saveData=0&executeType=start",function(responseText){
				
				var obj = new com.hotent.form.ResultMessage(responseText);
				if (obj.isSuccess()) {
					$.ligerDialog.success("启动流程成功！", "成功", function(rtn) {
						this.close();
						window.location.href = "${ctx}/undefined/Webag/webagBaginfo/list.ht";
					});
				} else {
					$.ligerDialog.error(obj.getMessage(),"提示信息");
					linkObj.removeClass("disabled");
				}
				
			});
		}
		
	}
    function showResponse(responseText) {
        var obj = new com.hotent.form.ResultMessage(responseText);
        if (obj.isSuccess()) {
            $.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
                if(rtn){
                    window.location.href = window.location.href;
                }else{
                    window.location.href = window.location.href;
                }
            });
        } else {
            $.ligerDialog.error(obj.getMessage(),"提示信息");
        }
    }
    function addQRs(){
        var str="";
        $("[name='id'][checked]").each(function(){
            str+=$(this).val()+",";
        });
       if (str==""){
           $.ligerDialog.error("请选择要生成二维码的回收袋","提示信息");
		} else{
           window.location.href = "${ctx}/webag/bagInfo/webagBaginfo/addQR.ht?id="+str;
	   }

    }

    /**
     *  导出二维码
     */
    function downLoadZip(){
        if ($(":checkbox[name=id]:checked").size() == 0) {
            alert("请至少选择一条记录进行导出操作！");
        }else{
            var checkboxs=$(":checkbox[name=id]:checked");
            var idsValue='';
            for(var i=0;i<checkboxs.size();i++){
                idsValue+=checkboxs[i].value+',';
            }
            if(idsValue!=''){
                idsValue=idsValue.substring(0, idsValue.length-1);
            }
            $(document.body).append('<form id = "queryForm" method="post" action="downLoadZip.ht"></form>');
            var queryForm = $('#queryForm');
            //清空内容
            queryForm.empty();
            queryForm.append("<input type='hidden' name='ids' value='"+idsValue+"'>");
            queryForm.submit();//表单提交
        }
    }
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">回收袋信息管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch"><span></span>查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="edit.ht"><span></span>添加</a></div>
					<%--<div class="l-bar-separator"></div>
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht"><span></span>修改</a></div>--%>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add2"  href="javascript:void(0)" onclick="addQRs()"><span></span>批量生成二维码</a></div>
                    <div class="l-bar-separator"></div>
                    <div class="group"><a class="link import"  onclick="downLoadZip()"><span></span>批量下载二维码</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
					<div class="row">
						<%--<span class="label">ID:</span><input type="text" name="Q_ID_L"  class="inputText" />--%>
						<span class="label">袋子编号:</span><input type="text" name="Q_bagNo_S"  class="inputText" />
						<span class="label">添加时间 从:</span> <input  name="Q_begincreateDate_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endcreateDate_DG" class="inputText date" />
						<span class="label">创建者:</span><input type="text" name="Q_creatorName_S"  class="inputText" />
						<%--<span class="label">创建者id:</span><input type="text" name="Q_creatorId_L"  class="inputText" />--%>
						<span class="label">使用次数:</span><input type="text" name="Q_useTime_L"  class="inputText" />
						<span class="label">袋子是否启用:</span>
						<input type="checkbox" name="Q_bagStatus_S" value="已启用" <c:if test ="${paramValues['Q_bagStatus_S']=='已启用'}">checked="checked"</c:if>><span class="label click">已启用</span>
						<input type="checkbox" name="Q_bagStatus_S" value="未启用" <c:if test ="${paramValues['Q_bagStatus_S']=='未启用'}">checked="checked"</c:if>><span class="label click">未启用</span>
						<%--<input type="checkbox" name="Q_bagStatus_S"  class="inputText" />--%>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="webagBaginfoList" id="webagBaginfoItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${webagBaginfoItem.id}">
				</display:column>
				<display:column property="bagNo" title="袋子编号" sortable="true" sortName="BAGNO"></display:column>
				<display:column  title="添加时间" sortable="true" sortName="CREATEDATE">
					<fmt:formatDate value="${webagBaginfoItem.createDate}" pattern="yyyy-MM-dd"/>
				</display:column>
				<display:column property="creatorName" title="创建者" sortable="true" sortName="CREATORNAME"></display:column>
				<%--<display:column property="creatorId" title="创建者id" sortable="true" sortName="CREATORID"></display:column>--%>
				<display:column property="useTime" title="使用次数" sortable="true" sortName="USETIME"></display:column>
				<display:column property="bagStatus" title="袋子是否启用" sortable="true" sortName="BAGSTATUS"></display:column>
				<display:column property="isHaveQR" title="是否生成二维码" sortable="true" sortName="ISHAVEQR"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<c:if test="${webagBaginfoItem.runId==0}">
						<a href="del.ht?id=${webagBaginfoItem.id}" class="link del"><span></span>删除</a>
						<%--<a href="javascript:;" onclick="startFlow(this,'${webagBaginfoItem.id}')" class="link run"><span></span>提交</a>--%>
					</c:if>
					<a href="edit.ht?id=${webagBaginfoItem.id}" class="link edit"><span></span>编辑</a>
					<a href="get.ht?id=${webagBaginfoItem.id}" class="link detail"><span></span>明细</a>
					<c:if test="${empty webagBaginfoItem.QRUrl}">
						<a href="addQR.ht?id=${webagBaginfoItem.id}" class="link add2"><span></span>生成二维码</a>
					</c:if>

				</display:column>
			</display:table>
			<hotent:paging tableId="webagBaginfoItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


