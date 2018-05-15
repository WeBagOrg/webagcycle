<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.hotent.platform.model.bpm.TaskOpinion"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.hotent.platform.model.system.SysUser"%>


<html>
<head>
	<%@include file="/commons/include/get.jsp" %>
	<title>${f:removeHTMLTag(processRun.subject)}--流程审批历史</title>
	<link href="${ctx}/styles/default/css/jquery.qtip.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/js/util/easyTemplate.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery/plugins/jquery.qtip.js" ></script>
	<script type="text/javascript" src="${ctx}/js/util/form.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/ProcessUrgeDialog.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/platform/bpm/FlowUtil.js"></script>
	<script type="text/javascript" src="${ctx}/js/hotent/platform/system/ShowExeInfo.js" ></script>
	<script type="text/javascript" src="${ctx}/js/hotent/platform/system/UserInfo.js"></script>
	<script type="text/javascript">
		$(function(){
			$("a[opinionId]").each(showResult);	
		});
		function showResult(){
			var opinionId=$(this).attr("opinionId"),
				checkStatus= $(this).attr("checkStatus");
			var template=$("#txtReceiveTemplate").val();
			$(this).qtip({  
				content:{
					text:'加载中...',
					ajax:{
						url:__ctx +"/platform/bpm/commuReceiver/getByOpinionId.ht",
						type:"GET",
						data:{opinionId: opinionId },
						success:function(data,status){
							var html=easyTemplate(template,data).toString();
							this.set("content.text",html);
						}
					},
					title:{
						text: checkStatus==36?'重启审批人' :(checkStatus==15?'沟通接收人' : '流转接收人')			
					}
				},
			        position: {
			        	at:'top left',
			        	target:'event',
			        	
   					viewport:  $(window)
			        },
			        show:{
			        	event:"click",
	   			     	solo:true
			        },   			     	
			        hide: {
			        	event:'unfocus',
			        	fixed:true
			        },  
			        style: {
			       	  classes:'ui-tooltip-light ui-tooltip-shadow'
			        } 			    
		 	});	
		}
		
	</script>
	<link rel="icon" href="${ctx}/favicon.ico" mce_href="favicon.ico" type="image/x-icon"/>
</head>
<body>
	<div class="l-layout-header">流程实例-【<i>${processRun.subject}</i>】审批历史明细。</div>
	<div class="panel">
		<c:if test="${param.tab eq 1 }">
			<c:choose>
					<c:when test="${processRun.status==2  || processRun.status==3}">
						<f:tab curTab="taskOpinion" tabName="process" />
					</c:when>
					<c:otherwise>
						<f:tab curTab="taskOpinion" tabName="process" hideTabs="copyUser"/>
					</c:otherwise>
			</c:choose>
		</c:if>
		<div class="panel-body" style="height:120%;overflow:auto;">
  		    <table class="table-grid">
  		    <tr>
  		      <td>任务节点</td>
  		      <td>开始时间</td>
  		      <td>结束时间</td>
  		      <td>经历时间</td>
  		      <td>执行人</td>
  		      <td>审批意见</td>
  		      <td>审批状态</td>
  		    </tr>
  		    <% 
  		    List<TaskOpinion> list = (List) request.getAttribute("taskOpinionList");
  		    Map<String, List> map = new LinkedHashMap<String, List>();
  		    for(TaskOpinion taskOpinion:list) {
  		    	if(map.containsKey(taskOpinion.getTaskKey())){
  					map.get(taskOpinion.getTaskKey()).add(taskOpinion);
  				}else{
  					List<TaskOpinion> optionList = new ArrayList<TaskOpinion>();
  					optionList.add(taskOpinion);
  					map.put(taskOpinion.getTaskKey(), optionList);
  				}
  		    }
	    	Iterator it=map.entrySet().iterator();     
			while(it.hasNext()){    
		    	   Map.Entry entry = (Map.Entry)it.next(); 
		    	   List<TaskOpinion> temdList = new ArrayList<TaskOpinion>();
		    	   temdList = (List<TaskOpinion>) entry.getValue();
  		     %>
 		           <tr>
 		               <td style="width: 80px !important;" rowspan="<%=temdList.size()+1%>">
		       	  			 <%=temdList.get(0).getTaskName() %>
		           	   </td>
 		           </tr>
 		           <%
 		             for(TaskOpinion taskOpinion:temdList){
  		       %>
 		            <tr>
 		               <td style="width: 120px !important;height: 30px">
 		                  <fmt:formatDate value="<%=taskOpinion.getStartTime()%>" pattern="yyyy-MM-dd HH:mm"/>
 		               </td>
 		               <td style="width: 120px !important;">
 		                  <fmt:formatDate value="<%=taskOpinion.getEndTime()%>" pattern="yyyy-MM-dd HH:mm"/>
 		              </td>
 		              <td>
 		                 <%
 		                 if(taskOpinion.getDurTime()!=null){
 		                %>
 		                <%=taskOpinion.getDurTime()/(2*100000)%>分钟
 		                <% 
 		                 }
 		                 %>
 		              </td>
 		              <td style="width: 120px !important;">
 		                 <%
 		                  if(taskOpinion.getExeUserId()!=null && taskOpinion.getExeFullname()!=null){
 		                  %>
 		                     <a href="javascript:userDetail(<%=taskOpinion.getExeUserId()%>);"><%=taskOpinion.getExeFullname()%></a>
 		                  <% 
 		                  }else{
 		                       for(int i = 0;i < taskOpinion.getCandidateUsers().size();i++){
 		                    	 SysUser sysUser = taskOpinion.getCandidateUsers().get(i);
 		                    	 %>
								<a href="javascript:userDetail(<%=sysUser.getUserId() %>);" ><%=sysUser.getFullname() %></a>
  		                 	 <%
 		                       }
 		                  }
 		                 %>
 		              </td>
 		               <td style="width: 120px !important;">
 		               <%
 		                  if(taskOpinion.getOpinion()!=null){
 		                  %>
 		                 <%=taskOpinion.getOpinion() %>
 		                  <% }%>
 		                 <%
 		                  if(taskOpinion.getCheckStatus()==15){
 		                  %>
 		                     <a  href="javascript:;" onclick="return false;" opinionId="<%=taskOpinion.getOpinionId() %>" checkStatus="15" >接收人</a>
 		                  <% 
 		                  }else if(taskOpinion.getCheckStatus()==38 || taskOpinion.getCheckStatus()==43){
 		                 %>
 		                     <a  href="javascript:;" onclick="return false;" opinionId="<%=taskOpinion.getOpinionId() %>" checkStatus="40" >接收人</a>
 		                  <% 
 		                  }
 		                 %>
 		              </td>
 		              <td style="width: 80px !important;">
 		                 <f:taskStatus status="<%=taskOpinion.getCheckStatus() %>" flag="0"></f:taskStatus>
 		              </td>
 		            </tr>
               <%} %>
		          
  		    <%} %>
  		     </table>
  		 </table>
		</div>
  </div>
  
  <textarea id="txtReceiveTemplate"  style="display: none;">
    <div  style="height:150px;width:150px;overflow:auto">
	  	
	  		<#list data as obj>
	  		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
	  		<tr>
	  			<th>接收人</th>
	  			<td>\${obj.receivername }</td>
	  		</tr>
	  		<tr>
	  			<th>状态</th>
	  			<td>
	  				<#if (obj.status==0) >
	  					<span class="red">未读</span>
	  				<#elseif (obj.status==1)>
	  					<span class="green">已读</span>
	  				<#elseif (obj.status==2)>
	  					<span class="green">已反馈</span>
	  				<#elseif (obj.status==3)>
	  					<span class="red">已取消</span>
	  				</#if>
	  			</td>
	  		</tr>
	  		<#if (obj.status==0) >
		  		<tr>
		  			<th>创建时间</th>
		  			<td>\${obj.createtimeStr}</td>
		  		</tr>
		  	<#elseif (obj.status==1)>
			  	<tr>
		  			<th>接收时间</th>
		  			<td>\${obj.receivetimeStr}</td>
			  	</tr>
		  	<#elseif (obj.status==2)>
		  		<tr>
		  			<th>反馈时间</th>
		  			<td>\${obj.feedbacktimeStr}</td>
		  		</tr>
		  	<#elseif (obj.status==3)>
		  		<tr>
		  			<th>取消时间</th>
		  			<td>\${obj.feedbacktimeStr}</td>
		  		</tr>
	  		</#if>
	  		</table>
	  		</#list>
	  	
  	</div>
  </textarea>
</body>
</html>
