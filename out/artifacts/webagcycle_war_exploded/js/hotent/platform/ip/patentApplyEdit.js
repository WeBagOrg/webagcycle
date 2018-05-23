// =======================================================================================================================
// 选择母申请号
     
		function selectApply()
		{  
			$("#parentNumber").blur();// 防止FOCUS连续执行
			ApplyDialog({callback:function(applyId,applyNumber){			
			$("#parentNumber").val(applyNumber);				
			},isSingle:true});			
		};
		
// ======================================================================================================================
		
		 // OA主办方名字		
		function selectOAPerson(obj)
		{  UserDialog({callback:function(id,name){			 
			   $(obj).closest("tr").children("td:eq(0)").find("input").val(name);
			},isSingle:true});
		};
		
		function cleanOAPerson(obj)
		{  	 
			   $(obj).closest("tr").children("td:eq(0)").find("input").val("");
			
		};
		
// ======================================================================================================================
// 选择人员 专利工程师
		function selectUser(fieldName,fieldId)
		{
			$("#"+fieldName).blur();// 防止FOCUS连续执行
			UserDialog({callback:function(userId,fullname){
					$("#"+fieldId).val(userId);
					$("#"+fieldName).val(fullname);
					$("#resetUser").show();
			},isSingle:true});
		};
		// 代理人 可以是多选
		function selectAgent(fieldName)
		{
			$("#"+fieldName).blur();// 防止FOCUS连续执行
			UserDialog({callback:function(userId,fullname,emails,phones,orgnames){	
				var shuzu=fullname.split(","); 
			   if(shuzu.length<3)
					{
				    $("#"+fieldName).val(fullname);
				    //如果两个代理人公司一样，只显示一个
					var org=orgnames.split(",");
					var orgname=orgnames;
					if(org.length==2)
						{  
						   if(org[0]==org[1])orgname=org[0];
						}
					$("#agency").val(orgname);
					$("#cleanAgent").show();					
					}
			   else
				   { $.ligerDialog.warn("代理人最多2个，请重新选择");
				      $("#"+fieldName).val("");
				   }
			},isSingle:false});
		};
		
// =======================================================================================================================
// 选择组织机构
			function selectOrg(fieldName,fieldId){
			$("#"+fieldName).blur();// 防止FOCUS连续执行
			OrgDialog({callback:function(orgId,orgName){
				$("#"+fieldId).val(orgId);
				$("#"+fieldName).val(orgName);
			    $("#resetOrg").show();
			},isSingle:true});
		}  
// =======================================================================================================================
// 选择申请人
		function selectOrgName(obj)
		{   var flag=true;
			$(obj).blur();// 防止FOCUS连续执行
			OrgDialog({callback:function(orgId,orgName,orgEnName){	
				
				 $("[title='applicantNameCn']").each(function(){
						if($(this).val()==orgName){
							$.ligerDialog.warn('该申请人已经添加,请重新选择!');
							flag=false;
							return;
						}
			   		 })
			   		 if(flag)
			{$(obj).closest("tr").children("td:eq(0)").find("input").val(orgName);// 暂时利用td
		//	$(obj).closest("tr").children("td:eq(1)").find("input").val(orgEnName);// 暂时利用td	组织树更改 没有英文名称了	
			}																// 位置找到应赋值的input
			},isSingle:true});
		};
		
		
		
		// 异步:保存申请人
		function saveApplicant(obj)
		{ 
			var ctx=$("#ctx").val();
			var cn=$(obj).parent().parent().children("td:eq(0)").find("input").val();// 发明人中文名字
			var en=$(obj).parent().parent().children("td:eq(1)").find("input").val();// 发明人外文名称
			var appId=$("#appId").val();
			var appNumber=$("#appNumber").val();	
			var xh=$(obj).parent().children("input").val();	
			var url=  ""+ctx+"/platform/ip/applicant/saveDialog.ht";
			if(appId==""){$.ligerDialog.warn("请先保存申请信息，再添加申请人信息！");return;}		
			if(cn){
				param={'CN':cn,'EN':en,'ID':appId,'NAME':appNumber,'XH':xh};
				$.ligerDialog.waitting('正在保存中,请稍候...');
   				$.post(url,param,function(data){
   					if(data){
   		    			if(data.result=="1"){// 成功
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.success(data.message);			        			
		        			$(obj).parent().children("input").val(data.cause);
		        			$(obj).parent().parent().children("th :last").children("input").val(data.cause);
		        		}
		        		else{// 失败
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.error(data.message);
		        		}
   				}// if
			});// post
			}// if
		}
		// 异步:删除申请人
		 function delApplicant(obj){
			       var ctx=$("#ctx").val();
				   var appId=$("#appId").val();
				   var xh=$(obj).parent().children("input").val();				   
	    	       $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
				   if(yes){					
	   				param={'ID':appId,'XH':xh};		   			
	   				$.post(""+ctx+"/platform/ip/applicant/delDialog.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){// 成功
			        			$.ligerDialog.success(data.message);			        			
			        			delRow(obj);
			        		}
			        		else{// 失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
				}
				   
			});
	     }
// =======================================================================================================================

		// 选择发明人
		function selectUserName(obj)
		{   var flag=true;
			$(obj).blur();// 防止FOCUS连续执行
			UserDialog({callback:function(id,name, g, j,or,eng){
				 $("[title='inventorId']").each(function(){
						if($(this).val()==id){
							$.ligerDialog.warn('该发明人已经添加,请重新选择!');
							flag=false;
							return;
						}
			   		 })
			   		 if(flag)
			{
			   $(obj).closest("tr").children("th:eq(0)").find("input").val(id);// 发明人id
			   $(obj).closest("tr").children("td:eq(0)").find("input").val(name);
			 //  $(obj).closest("tr").children("td:eq(1)").find("input").val(eng);//变更 没有英文名字
			   }// 发明人名字
																					
			},isSingle:true});
		};
		
		// 异步保存发明人
		function saveCreator(obj)
		{ 
			var ctx=$("#ctx").val();
			var inId=$(obj).parent().parent().children("th:eq(0)").find("input").val();// 发明人Id
			var cn=$(obj).parent().parent().children("td:eq(0)").find("input").val();// 发明人中文名字
			var en=$(obj).parent().parent().children("td:eq(1)").find("input").val();// 发明人外文名称
			var gxl=$(obj).parent().parent().children("td:eq(2)").find("input").val();// 发明人贡献率
			var appId=$("#appId").val();
			var appNumber=$("#appNumber").val();
			var xh=$(obj).parent().children("input").val();	
			var url=  ""+ctx+"/platform/ip/inventor/saveDialog.ht";
			if(appId==""){$.ligerDialog.warn("请先保存申请信息，再添加发明人信息！");return;}	
			var flag=true;
			var sum=0;
			 $("[title='inventorDevote']").each(function(){
					if($(this).val()!=""){
					//	var devote=Number($(this).val().replace('%', '')) / 100;
						sum +=($(this).val()-0);						
					}
		   		 })		   
		     if(sum>100){
		    	 $.ligerDialog.warn('发明人贡献率总和不能超过100%!');		    	
					flag=false;
					return;
		     }
			if(inId&&flag){				
				param={'inventorId':inId,'inventorNameCn':cn,'inventorNameEn':en,'ID':appId,'NAME':appNumber,'inventorDevote':gxl,'XH':xh};	
				$.ligerDialog.waitting('正在保存中,请稍候...');
   				$.post(url,param,function(data){
   					if(data){
   		    			if(data.result=="1"){// 成功
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.success(data.message);	
		        			$(obj).parent().children("input").val(data.cause);
		        			$(obj).parent().parent().children("th :last").children("input").val(data.cause);
		        		}
		        		else{// 失败
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.error(data.message);
		        		}
   				}// if
			});// post
			}// if
		}
		// 异步删除发明人
		 function delCreator(obj){
			       var ctx=$("#ctx").val();
				   var appId=$("#appId").val();
				   var xh=$(obj).parent().children("input").val();	
				  
	    	       $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
				   if(yes){							
	   				param={'XH':xh,'ID':appId};		
	   				if(xh!="")//序号是否存在
	   				{$.post(""+ctx+"/platform/ip/inventor/delDialog.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){// 成功
			        			$.ligerDialog.success(data.message);			        			
			        			delRow(obj);
			        		}
			        		else{// 失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
				}else
					{
					delRow(obj);
					}
	   				}
			});
	     }
		 
// =======================================================================================================================
		// 清空选项
		function reSet(obj,fieldName,fieldId){
			$(obj).hide();
			$("#"+fieldName).val("");
			$("#"+fieldId).val("");			
		}
		
		function cleanThis(obj){			
			$(obj).hide();
			$(obj).prev("input").val("");
			
		}
// =======================================================================================================================
		// 选择专利 优先权信息
		var priorityDate=new Array();
		var index=0;
		var earlyDate="";
		//得到最早优先权日期
		function selectEarlyDate()
		{ 	$(':hidden[name=priorityHidden]').each(function(){
			priorityDate[index++]=$(this).val();  
			 if(earlyDate==""||earlyDate>$(this).val())earlyDate=$(this).val();//找到最早的优先权日
	         })	        
		}
		//通过优先权获取绝限日期
		function addMoth(d,m){
			   var ds=d.split('-'),_d=ds[2]-0;
			   var nextM=new Date( ds[0],ds[1]-1+m+1, 0 );
			   var max=nextM.getDate();
			   d=new Date( ds[0],ds[1]-1+m,_d>max? max:_d );
		    //   return d.toLocaleDateString().match(/\d+/g).join('-')
			//   alert(d);alert(formatDate(d));
			   return formatDate(d);
			}
			 
		function addPatentClick(obj){
			 var flag=true;
			 $(obj).blur();
			ApplyDialog({callback:function(appId,applyNumber,json,applydate,patentCountry){
				 
				 $("[title='priority']").each(function(){
						if($(this).val()==applyNumber){
							$.ligerDialog.warn('该优先权号已经添加,请重新选择!');
							flag=false;
							return;
						}
			   		 })
				if(appId&&flag){				
				  priorityDate[index++]=applydate;
				  if(earlyDate==""||earlyDate>applydate)earlyDate=applydate;//找到最早的优先权日
				  if(earlyDate!="")
		    	  { $("#vastLimitDate").val(addMoth(earlyDate,12) );
		    	  $("#iVastLimitDate").val(addMoth(earlyDate,32) );}//进入国际申请绝限是最早优先权日后的32个月
		    	  if(earlyDate=="")
		    	  { $("#vastLimitDate").val("");
		    	    $("#iVastLimitDate").val("");
		    	  }	  
				  $(obj).closest("tr").children("td:eq(0)").find("input").val(patentCountry);// 暂时利用td
																								// 位置找到应赋值的input
			      $(obj).closest("tr").children("td:eq(1)").find("input").val(applydate);// 暂时利用td
																							// 位置找到应赋值的input
			      $(obj).closest("tr").children("td:eq(2)").find("input").val(applyNumber);// 暂时利用td
																							// 位置找到应赋值的input
				}
			},isSingle:true,isPriority:"1"});
			showFamily();
		}		
		
		// 异步保存优先权
		function savePriority(obj)
		{ 
			var ctx=$("#ctx").val();
			var nati=$(obj).parent().parent().children("td:eq(0)").find("input").val();// 国家
			var date=$(obj).parent().parent().children("td:eq(1)").find("input").val();// 日期
			var numb=$(obj).parent().parent().children("td:eq(2)").find("input").val();// 号码
			var XH=$(obj).parent().children("input").val();
			var appId=$("#appId").val();
			var appNumber=$("#appNumber").val();
			var url=  ""+ctx+"/platform/ip/priority/saveDialog.ht";
			if(appId==""){$.ligerDialog.warn("请先保存申请信息，再添加优先权信息！");return;}		
			if(numb){
				param={'NA':nati,'DA':date,'ID':appId,'NAME':appNumber,'NU':numb,'XH':XH};	
				$.ligerDialog.waitting('正在保存中,请稍候...');
   				$.post(url,param,function(data){
   					if(data){
   		    			if(data.result=="1"){// 成功
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.success(data.message);	
		        			
		        			$(obj).parent().children("input").val(data.cause);
		        			$(obj).parent().parent().children("th:last").find("input").val(data.cause);
		        		}
		        		else{// 失败
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.error(data.message);
		        		}
   				}// if
			});// post
			}// if
		}
		
		// 异步删除优先权
		 function delPriority(obj){
			       var ctx=$("#ctx").val();
				   var appId=$("#appId").val();	
				   var XH=$(obj).parent().children("input").val();				   
				   var date=$(obj).parent().parent().children("td:eq(1)").find("input").val();// 日期
	    	       $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
				   if(yes){		
					   //把删除的日期清空
					   for(var i in priorityDate)
		               {					
				    	if(priorityDate[i]==date){priorityDate[i]="";}
		               }
					   //如果删除的是最早的优先权日
				        if(earlyDate==date)
						{   
					    	earlyDate="";	
					    	 for(var i in priorityDate)
				               {
					    		 if(earlyDate==""||(priorityDate[i]!=""&&earlyDate>priorityDate[i]))
							    	{							    	
							    		earlyDate=priorityDate[i];//找到最早的优先权日
							    	}
				               }
					    	  if(earlyDate!="")
					    	  { $("#vastLimitDate").val(addMoth(earlyDate,12) );//申请绝限为最早优先权后的12个月
					    	  $("#iVastLimitDate").val(addMoth(earlyDate,32) );}//进入国际申请绝限是最早优先权日后的32个月
					    	  //如果全部都已经清空，申请绝限由用户填写，进入国际申请绝限为申请日后的32个月
					    	  if(earlyDate=="")
					    	  { $("#vastLimitDate").val("");
					    	       if($("#appDate").val()!="")
				    	           {   $("#iVastLimitDate").val(addMoth($("#appDate").val(),32));}
				    	            else $("#iVastLimitDate").val("");
					    	  }
					    }   
					   
	   				param={'XH':XH,'ID':appId};					
	   				$.post(""+ctx+"/platform/ip/priority/delDialog.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){// 成功			        			
			        			$.ligerDialog.success(data.message);
			        			// $(obj).closest("tr").remove();
			        			delRow(obj);
			        		}
			        		else{// 失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
				}
			});
	     }
		//如果是首次申请，则没有优先权信息	      
		function hiddenPriority()
		{
			
			if($("[name='isPrime']:checked").val()=="1")
			{delAll('priorityTable');
			$("#pri1").hide();
			$("#pri2").hide();}
			else
				{
				$("#pri1").show();
				$("#pri2").show();
				}
			
		}
// =======================================================================================================================
		// 选择提案
		function addProposalClick(){
			 $("#proposalCode").blur();
			proposalDialog({callback:function(proposalId,proposalCode,json,proposalTran){
			
				if(proposalTran>0)
				{ var ts="提示：该提案已提交申请"+proposalTran+"次,是否继续添加?";
				 $.ligerDialog.confirm(ts, function (yes) {
					 if(yes)
					 {if(proposalId&&proposalId.length>0){
				        	
				         selectProposalById(proposalId);
			        }}
				 });}
				else
					{
					if(proposalId&&proposalId.length>0){
			        	
				         selectProposalById(proposalId);
			        }
					}
		        
			},isSingle:true});
		}
		
		// 异步查询提案详细信息
		var invenNum=0;// 用来记录发明人个数
		function selectProposalById(id){		  
		  var params={'id':id};
		  var ctx=$("#ctx").val();
		  var url=""+ctx+"/platform/ip/proposal/selectOne.ht";
		  $.post(url,params, function(data){			 
			if(data){ 
		        var proposaldata=data["proposal"];// 提案
		        if(proposaldata){		        	
		          $("#proposalId").val(proposaldata["id"]);// 提案id
		          $("#proposalCode").val(proposaldata["caseNum"]).attr("disabled","disabled");// 提案号
		         $("[name='originTitle']").val(proposaldata["proposalName"]).attr("readonly","readonly");// 提案名称
		        
		         if(proposaldata["applyCountry"]!=null)
		         {  $("#appCountry").val(proposaldata["applyCountry"]);// PCT		           
		            $("[name='appCounType']").each(function(){		            	
	        			  if($(this).val()=='1'){
	        				 $(this).attr("checked",true);
	        			  }
	        		  });
		         
		         }
		          $("[name='patType']").each(function(){
		        			  if($(this).val()==proposaldata["patentType"]){
		        				  $(this).attr("checked",true);
		        			  }
		        		  });// 提案类型
		           if(proposaldata.inventors){
		        	 var inventorHtml=$("#creatorTable tr ").last();
		        	   // 清除原有记录
		            // var inventorHtml=$("#creatorTable tr");
		        	   invenNum=proposaldata.inventors.length;	

			           for(var i=0;i<proposaldata.inventors.length;i++){
			           	if(i>0){
			           	    addRow('creatorTable');
			           	    inventorHtml=$("#creatorTable tr").last();			           	
			           	}
			           		//inputData(inventorHtml,proposaldata.inventors[i],proposaldata.user[i]);
			           	inputData(inventorHtml,proposaldata.inventors[i])
			           }
		          } 
	           	  //项目
		          var projectdata=proposaldata["projectList"];// 提案
		          if(projectdata){
		        	for(var i=0;i<projectdata.length;i++){
		        		showProjectRow(projectdata[i]);
		        	}
		          }
		        //产品
		          var productdata=proposaldata["productList"];// 提案
		          if(productdata){
		        	for(var i=0;i<productdata.length;i++){
		        		showProduct(productdata[i]);
		        	}
		          }
		          // 显示清除按钮
		          $("#resetProposal").show();
		        }
		        //技术领域
		        var technologydata=data["technology"];//技术领域
		        if(technologydata){
		        	$("[name='technologyName']").val(technologydata.technologyName);
	   			 	$("[name='technologyId']").val(technologydata.id);
		        }
			}
		  });
		 
		}
		
		// 设置数据到页面
		/*function inputData(inventorHtml,inventorData,user){	
			alert(inventorData.inventorId);
			alert(inventorData.inventor);
			alert(user.englishName);
			alert(inventorData.inventorDevote);
			inventorHtml.closest("tr").children("th:eq(0)").find("input").val(inventorData.inventorId);
			inventorHtml.closest("tr").children("td:eq(0)").find("input").val(inventorData.inventor);
			if(user!=null)inventorHtml.closest("tr").children("td:eq(1)").find("input").val(user.englishName);
			inventorData.inventorDevote=Number(inventorData.inventorDevote.replace('%', ''));
			inventorHtml.closest("tr").children("td:eq(2)").find("input").val(inventorData.inventorDevote);// 暂时利用td
																											// 位置找到应赋值的input
		}*/
		function inputData(inventorHtml,inventorData){				
			inventorHtml.closest("tr").children("th:eq(0)").find("input").val(inventorData.inventorId);
			inventorHtml.closest("tr").children("td:eq(0)").find("input").val(inventorData.inventor);
			//if(user!=null)inventorHtml.closest("tr").children("td:eq(1)").find("input").val(user.englishName);
			inventorData.inventorDevote=Number(inventorData.inventorDevote.replace('%', ''));
			inventorHtml.closest("tr").children("td:eq(2)").find("input").val(inventorData.inventorDevote);// 暂时利用td
																											// 位置找到应赋值的input
		}
		// 清除
		function cleanProposal(){
			$("#proposalId").val("");// 提案id
	        $("#proposalCode").val("");
	        $("[name='originTitle']").val("");	
	        $("[name='appCountry']").val("");	
	        $("[name='appCounType']").attr("checked",false);
	        
	        for(var i=0;i<invenNum;i++)	
	        {
	        $("[name='inventors["+i+"].inventorNameCn']").val("");
	        $("[name='inventors["+i+"].inventorDevote']").val("");
	        }
	        delAll('creatorTable');
	        $("#proposalCode").removeAttr("disabled");
	        $("[name='originTitle']").removeAttr("disabled");	 
	        $("[name='patType']").attr("checked",false);
	        //删除项目对应的行记录
	        $("[name='projectRow']").not(":hidden").remove();
	        $("[name='productRow']").not(":hidden").remove();
	        $("[name='technologyId']").val("");
			$("[name='technologyName']").val("");
	        $("#resetProposal").hide();
		}
		
		
// =======================================================================================================================
		// 添加行
	
		function addRow(rowId){
			
			var html=$("#"+rowId).children().children("tr:last").clone().show();;
		
			// 追加添加的行
			$("#"+rowId).children().children("tr:last").after(html);// 追加到最后面一行
			 html.children("td").each(function(){
					  $(this).children("input").each(function(){	
						formatHtmls($(this));
				   })	
				});
				 html.children("th").each(function(){					 
					  $(this).children("input").each(function(){						
						  formatHtmls($(this));						
				   })				   	
				});
				 html.children("th:last").children("a").show();
		}	
		
	// 删除行
		function delRow(obj){				
		// 首行清空 
		 if(obj.parentNode.parentNode.rowIndex==0){			
		 $(obj).closest("tr").children("td").each(function(){
					  $(this).children("input").each(function(){
					  $(this).val("");
		 });
		});
		 // $(obj).closest("tr").hide();
		 }
		 else{	$(obj).closest("tr").remove();}
		}
	// 删除所有行
		function delAll(obj){	
			var html=$("#"+obj).children().children("tr:last").clone().show();
			 html="<tr>"+html.html()+"</tr>";// 添加一整行
			$("#"+obj).children().children("tr").remove();// 删除原来的
			$("#"+obj).children().append(html);// 添加一整行
			$("#"+obj).children().children("tr").children("td").each(function(){
					  $(this).children("input").each(function(){	
						formatHtmls($(this));
				   })	
				});
			$("#"+obj).children().children("tr").children("th").each(function(){					 
					  $(this).children("input").each(function(){						
					  $(this).val("");
						
				   })	
				});
	
		}
			
	// 增加一个table
			function addTable(table)
		{
		      // var Ahtml=$("#"+table).clone(true).show();// 复制隐藏的TABLE true则克隆事件
		        var html=$("#"+table).parent().children("table :last").clone(true);// 复制最后一个TABLE		        
		        var text=html.attr("id").split("_")[0];// 分隔号前面的文本
		      	var index=html.attr("id").split("_")[1]-0;// 分隔号的index值		      	
		      	html.attr("id", text+"_"+(++index));// 复制Table
				$("#"+table).parent().children("table :last").after(html);
				// 遍历所有的input，将其id的index累加
				 html.children().children("tr").each(function(){
				    $(this).children("td").each(function(){
					  $(this).children("input").each(function(){	
						formatThis($(this),index);						
					})	;
					  
					  //附件					 
					 $(this).find("textarea").each(function(){							
						formatThis($(this),index);
					})
					 $(this).find(".attachement").each(function(){							
						 $(this).html('');
					})
					 $(this).find("a").each(function(){							
						var regex=/\w+\[(\d+)\]\.\w+/;		  
						var name= $(this).attr("field").match(regex)[0];	    		
						var num= $(this).attr("field").match(regex)[1]-0;		
						name=name.replace(num+"",num+1+"");
						 $(this).attr("field",name);
						
					 })			 
					 
					 
				   });
					 
					 
					 
				     $(this).children("th").each(function(){
					  $(this).children("input").each(function(){	
						formatThis($(this),index);						
					})
				   });
				})
				
				// return html;
		}
	// 删除一个table
		function delTable(obj){
		 var index= $(obj).closest("table").attr("id").split("_")[1]-0;// 分隔号的index值
		// 首行清空 隐藏
		 if(index==0){			
		 $(obj).closest("table").children().children("tr").each(function(){
		              $(this).children("td").each(function(){
					  $(this).children("input").each(function(){
						  formatThis($(this),index);	
		 }); 
					  //附件					 
						 $(this).find("textarea").each(function(){							
							formatThis($(this),index);
						})
						 $(this).find(".attachement").each(function(){							
							 $(this).html('');
						})
						 $(this).find("a").each(function(){							
							var regex=/\w+\[(\d+)\]\.\w+/;		  
							var name= $(this).attr("field").match(regex)[0];	    		
							var num= $(this).attr("field").match(regex)[1]-0;		
							name=name.replace(num+"",num+1+"");
							 $(this).attr("field",name);
							
						 })			 
						   
		              
		              
		              
		              });
		              
		              $(this).children("th").each(function(){
						  $(this).children("input").each(function(){
							  formatThis($(this),index);	
			 }); });
		              
		              
		              
		              
		              
		 
		 
		 });
		// $(obj).closest("table").hide();
		 }
		 else{	$(obj).closest("table").remove();}
		}	
		// 将对象的id后置index累加，并清空value值
		function formatHtmls(html){
		    var regex=/\w+\[(\d+)\]\.\w+/;		  
			var name=html.attr("name").match(regex)[0];	    		
			var num=html.attr("name").match(regex)[1]-0;		
			name=name.replace(num+"",num+1+"");
			html.attr("name",name);
			html.attr("value","");			
			
		}
		
		function formatThis(html,index){
			  var regex=/\w+\[(\d+)\]\.\w+/;	
			  var reg=/\w+\_(\d+)\w+/;
				var name=html.attr("name").match(regex)[0];
				var num=html.attr("name").match(regex)[1]-0;				
				name=name.replace(num+"",index+"");
				html.attr("name",name);
			//	html.attr("value","");	
				html.val('');
				if(html.attr("id")!=null)
					{
					var id=html.attr("id").match(reg)[0];
					var n=html.attr("id").match(reg)[1]-0;				
					id=id.replace(n+"",index+"");
					html.attr("id",id);		
					html.removeAttr("value");
					/*if($(html).parent().children("ul")!=null)
						{
						var id=html.parent().children("ul").attr("id").match(reg)[0];
						var n=html.parent().children("ul").attr("id").match(reg)[1]-0;				
						id=id.replace(n+"",index+"");
						html.parent().children("ul").attr("id",id);		
						html.parent().children("ul").children().remove();
						}*/
					}
					
				
			}
		
// =======================================================================================================================
// 复审复议信息
	 
	  function addReExam(){		
		var appNumber=$("#appNumber").val();
		var appId=$("#appId").val();
		var alias="fyfsbh";
		var t=document.getElementById("caseTable");		
	       if(appNumber==""||appId==""){$.ligerDialog.warn("请先填写并保存申请号，再添加复审复议信息！");return;}
		   else {ReExamDialog({callback:function(patentJson){	
			 // 添加成功后，查找到该申请号对应的复议复审信息
		        if(patentJson){
			      appendRow(patentJson);}
			    },appId:appId,alias:alias});}
	          $.ajaxSetup({async:false});  //同步 
     		  var caseStatus= validStatus();     		
              updateStatus(caseStatus); 		
		
	      }
// 编辑复审复议信息===========================================================================================================
	function editReExam(obj){	   
	      var id=$(obj).parent().children("input").val();	      
		  ReExamDialog({callback:function(patentJson){
		              deleteitem(obj);	
			 // 添加成功后，查找到该申请号对应的复议复审信息
		        if(patentJson){
			          appendRow(patentJson);}
			    },id:id});
		  $.ajaxSetup({async:false});  //同步 
 		  var caseStatus= validStatus();     		
          updateStatus(caseStatus); 		
	           }
	
// 删除复审复议信息===========================================================================================================
	function deleteitem(obj)
		 {
		  var t=document.getElementById("caseTable"); 
		   var curRow = obj.parentNode.parentNode;
		 // alert(curRow.rowIndex);
		   t.deleteRow(curRow.rowIndex);// 删除
		 }
	 function delReexam(obj){
	       var ctx=$("#ctx").val();		  
		   var reexamNO=$(obj).parent().children("input").val();
	       $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
		   if(yes){							
				param={'CN':reexamNO};					
				$.post(""+ctx+"/platform/ip/patentReexam/delDialog.ht",param,function(data){
		    		if(data){
		    			if(data.result=="1"){// 成功
	        			$.ligerDialog.success(data.message);	        			
	        			deleteitem(obj);
	        			  $.ajaxSetup({async:false});  //同步 
	             		  var caseStatus= validStatus();     		
	                      updateStatus(caseStatus); 		}
	        		else{// 失败
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.error(data.message);
	        		}
		    		}
		    	 })
		}
	});
}
// =====================================================================================================================
	 // 显示到页面上
     function appendRow(patentJson)
     {          var t=document.getElementById("caseTable");	
                var test={"fs":"复审","fy":"复议","cg":"成功","sb":"失败"};
				var myobj=eval(patentJson); // 把JSON转换成数组以供赋值
						if(myobj)			
					{ 
					   var r = t.insertRow(1);  // 根据所选，确定行数
					   
					// var xh = r.insertCell();
				   //  var code = r.insertCell(); 		
				     var Type = r.insertCell();
				     var date = r.insertCell(); 		
				     var pName = r.insertCell();
				     var MaNum = r.insertCell(); 
				     var cz = r.insertCell(); 	
				     // 赋值
				   // xh.innerHTML=++count;
				   for(var p in test)
                        {               
                  if(myobj.patentReexamType==p)myobj.patentReexamType=test[p];
                  if(myobj.patentReexamResult==p)myobj.patentReexamResult=test[p];
                  if(myobj.patentReexamType==undefined)myobj.patentReexamType="";
                  if(myobj.patentReexamResult==undefined)myobj.patentReexamResult="";
                        }
					// code.innerHTML=myobj.patentReexamId;  
					 Type.innerHTML=myobj.patentReexamType;
					 date.innerHTML=myobj.referedLaws;
					 pName.innerHTML=myobj.decisionNum;
					 MaNum.innerHTML=myobj.patentReexamResult;
					 cz.innerHTML= " <input type='hidden' name='reexamid' value='"+myobj.id+"'>" +
" <a href=\"javascript:;\" class='link edit'   onclick=\'editReExam(this)\'>编辑 </a> &nbsp;&nbsp; <a href=\"javascript:;\" class='link clean'   onclick=\'delReexam(this);\'>删除 </a>"
                          }
     }     	
     
     
// ======================================================================================================================
		// 增加费用信息
     function checkAppNumber()
     {
    	 var appNumber=$("#appNumber").val();
    	 var appId=$("#appId").val();
    	 if(appNumber==""||appId==""){$.ligerDialog.warn("请先填写并保存申请号，再添加费用信息！");return;}
     }
	  function addExpense(){		
		var appNumber=$("#appNumber").val();
		var appId=$("#appId").val();
		var year=$("#annualFee").val();//年费
		var agency=$("#agency").val();//代理所
		var isReduce=0;//是否减缓
		if($("#isReduce").prop("checked")){
			isReduce=1;
		}
	    var appNo=$("#proposalCode").val();//案件编号
		var ctx=$("#ctx").val();
		var t=document.getElementById("expenseTable");		
	       if(appNumber==""||appId==""){$.ligerDialog.warn("请先填写并保存申请号，再添加费用信息！");return;}
		   else {exDialog({callback:function(patentJson){			   
		        if(patentJson){		        	  
		        	      var obj = eval(patentJson);		        	 
		    			  if(obj!=null&&obj!=undefined&&obj.standardAttr=="nf")//添加费用构成
		    			  {	
		    				  var url=""+ctx+"/platform/ip/standard/selectOne.ht";	        	 
				    		  $.post(url,{'id':obj.expenseStandardId}, function(stand){		    				  
		    				  	$("#annualFee").val(stand["year"]);//页面传值，用于下次添加费用的时候，不能添加年费		    					 
		    				  	var url=""+ctx+"/platform/ip/patentApply/updateFee.ht";
		    				  	$.post(url,{'id':appId,'year':stand["year"]});		    				 	    					  
		    				  });//更能申请中年费信息结束	
		    			  }
		    			  appendExRow(patentJson);  
		    			  }
				    		     	
		        	
			    },appId:appId,year:year,agency:agency,isReduce:isReduce,id:0});
		   }}
	// 编辑费用信息
	function editExpense(obj){	   
	     var id=$(obj).parent().children("input").val();
	     var isReduce=0;//是否减缓
		 if($("#isReduce").prop("checked")){
			 isReduce=1;
		 }	    
		 exDialog({callback:function(patentJson){
			 deleteExpense(obj);	
			 // 添加成功后，查找到该申请号对应的复议复审信息
		        if(patentJson){
			       appendExRow(patentJson);}
			    },id:id,isReduce:isReduce});
	           }
	
	// 页面上删除费用信息
	function deleteExpense(obj)
		 {
		  var t=document.getElementById("expenseTable"); 
		   var curRow = obj.parentNode.parentNode;
		 // alert(curRow.rowIndex);
		   t.deleteRow(curRow.rowIndex);// 删除
		 }
	
	// 异步删除数据库信息
	 function delExpense(obj){
	       var ctx=$("#ctx").val();		 
		   var id=$(obj).parent().children("input").val();//费用主键
		   var appId=$("#appId").val();
		   $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
		   if(yes){				 
				param={'id':id,'appId':appId};					
				$.post(""+ctx+"/platform/ip/expenseManger/delDialog.ht",param,function(data){
		    		if(data){
		    			if(data.result=="1"){// 成功
	        			$.ligerDialog.success(data.message);	
	        			if(data.cause=="1"){$("#annualFee").val(0);}
	        			 deleteExpense(obj);	 }
	        		else{// 失败
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.error(data.message);
	        		}
		    		}
		    	 })
		}
	});
}
	
	 Date.prototype.format = function (format) {
			var o = {
			"M+": this.getMonth() + 1,
			"d+": this.getDate(),
			"h+": this.getHours(),
			"m+": this.getMinutes(),
			"s+": this.getSeconds(),
			"q+": Math.floor((this.getMonth() + 3) / 3),
			"S": this.getMilliseconds()
			}
			if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			}
			for (var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
			}
			}
			return format;
			} 
	 // 显示到页面上
  function appendExRow(patentJson)
  {    
	  var t=document.getElementById("expenseTable");	
             var test={"nf":"年费","sqf":"申请费","qt":"其他费","sqdjt":"授权费"};
				var myobj=eval(patentJson); // 把JSON转换成数组以供赋值
				
					if(myobj.length!=0)			
					{					
					 var r = t.insertRow(1);  // 根据所选，确定行数						
				     var Type = r.insertCell();
				     var date = r.insertCell(); 		
				     var pName = r.insertCell();
				     var MaNum = r.insertCell(); 
				     var cz = r.insertCell(); 	
				    
				   for(var p in test)
                     {               
                    if(myobj.standardAttr==p)myobj.standardAttr=test[p];            
                     }
				  
				
				     Type.innerHTML=myobj.standardAttr;
					 date.innerHTML=myobj.expenseName;
					 pName.innerHTML=myobj.amount;
					
		            var formatDate = new Date(myobj.expenseDate).format("yyyy-MM-dd");		            
		            MaNum.innerHTML=formatDate;
				//	 var date=new Date(myobj.expenseDate);
				//	 MaNum.innerHTML=date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
					//MaNum.innerHTML=" <fmt:formatDate pattern='yyyy-MM-dd' value="+myobj.expenseDate+"/>"
					 cz.innerHTML= " <input type='hidden'  value='"+myobj.id+"'> " +
	"<a href=\"javascript:;\" class='link edit'   onclick=\'editExpense(this)\'>编辑 </a> &nbsp;&nbsp; <a href=\"javascript:;\" class='link clean'   onclick=\'delExpense(this);\'>删除 </a>"
                       }
  }     
  
// =======================================================================================================================
   // 异步保存OA
	function saveOA(obj)
	{ 
		var ctx=$("#ctx").val();
		// 第一行信息
		var letter=$(obj).parent().parent().parent().children("tr :eq(0)").children("td :eq(1)").find("input").val();
		var dispatchDate=$(obj).parent().parent().parent().children("tr :eq(0)").children("td :eq(3)").find("input").val();
		var vastLimitDate=$(obj).parent().parent().parent().children("tr :eq(0)").children("td :eq(5)").find("input").val();
		// 第二行信息
		var sponsor=$(obj).parent().parent().parent().children("tr :eq(1)").children("td :eq(1)").find("input").val();
		var sendDate=$(obj).parent().parent().parent().children("tr :eq(1)").children("td :eq(3)").find("input").val();
		var answerDate=$(obj).parent().parent().parent().children("tr :eq(1)").children("td :eq(5)").find("input").val();		
		// 第三行信息
		var examClause=$(obj).parent().parent().parent().children("tr :eq(2)").children("td :eq(1)").find("input").val();
/*		var originAttachment=$(obj).parent().parent().parent().children("tr :eq(2)").children("th :eq(2)").find("input").val();
*/		
		var attachment=$(obj).parent().parent().parent().children("tr :eq(2)").children("td :eq(3)").find("textarea").val();
		
		
		// 第四行信息
/*		var originAttachmentOut=$(obj).parent().parent().parent().children("tr :eq(3)").children("th :eq(0)").find("input").val();
*/		var attachmentOut=$(obj).parent().parent().parent().children("tr :eq(3)").children("td :eq(1)").find("textarea").val();		
		var oaid=$(obj).parent().children("input[title='responseOAid']").val();			
		var deadlineId=$(obj).parent().children("input[title='responseOAdeadlineid']").val();// 期限
		var appId=$("#appId").val();
		var appNumber=$("#appNumber").val();
		var appNo=$("#proposalCode").val();//申请编号		
		var url=  ""+ctx+"/platform/ip/responseOA/saveDialog.ht";
		if(appId==""){$.ligerDialog.warn("请先保存立案信息，再添加OA信息！");return;}		
		if(letter){
			var param={'appId':appId,'appNumber':appNumber,'id':oaid,'deadlineId':deadlineId,'letter':letter,
					'dispatchDate':dispatchDate,'vastLimitDate':vastLimitDate,'sponsor':sponsor,'sendDate':sendDate,'answerDate':answerDate,
					'examClause':examClause,'attachment':attachment,'attachmentOut':attachmentOut,'appNo':appNo}
/*			'originAttachment':originAttachment,'originAttachmentOut':originAttachmentOut};					
*/			$.ligerDialog.waitting('正在保存中,请稍候...');	
			$.post(url,param,function(data){
					if(data){
		    			if(data.result=="1"){// 成功
		    			/*	$(obj).parent().parent().parent().children("tr :eq(2)").children("th :eq(2)").find("input").val(attachment);
	    					$(obj).parent().parent().parent().children("tr :eq(3)").children("th :eq(0)").find("input").val(attachmentOut);	    					
		    			*/	if(data.cause!=""&&data.cause!=null)		    					
		    				{//更新后ID赋值		    					
		    					$(obj).parent().children("input[title='responseOAid']").val(data.cause);		    					
		    				//更新后deadlineId赋值
		    				  $.post(""+ctx+"/platform/ip/responseOA/getDeadline.ht",{'id':data.cause},function(aa){		    					
		    					  $(obj).parent().children("input[title='responseOAdeadlineid']").val(aa.cause);
		    				  });
		    				}
		    		  
		    			var caseStatus= validStatus();		    			
		    			updateStatus(caseStatus);
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.success(data.message);
	        			// $(obj).hide();
	        		}
	        		else{// 失败
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.error(data.message);
	        		}
				}// if
		});// post			
		}// if
		else
			{$.ligerDialog.warn("请填写官方通知书");}
	}
	// 异步删除OA信息
	 function delOA(obj){
		       var ctx=$("#ctx").val();
		       var appId=$("#appId").val();
			   var id=$(obj).parent().children("input[title='responseOAid']").val();
		       var deadlineId=  $(obj).parent().children("input[title='responseOAdeadlineid']").val();
  	        $.ligerDialog.confirm('是否删除', function (yes) { 	    		 
			  if(yes){			
				  delTable(obj);					
 			var param={'deadlineId':deadlineId,'id':id,'appId':appId};	
 			var url=""+ctx+"/platform/ip/responseOA/delDialog.ht";
 				
 				$.post(url,param,function(data){
 		    		if(data){
 		    			if(data.result=="1"){// 成功
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.success(data.message);
		        			// $(obj).closest("tr").remove();
		        			  $.ajaxSetup({async:false});  //同步 
		             		  var caseStatus= validStatus();     		
		                      updateStatus(caseStatus); 		
		        		}
		        		else{// 失败
		        			$.ligerDialog.closeWaitting();
		        			$.ligerDialog.error(data.message);
		        		}
 		    		}
 		    	 })
			}
		});
   }
// ===================================================================================================================
	   // 添加扩展字段
     function addAdditionalField(){
    	 AdditionalFieldDialog({callback:function(data){
			showAdditionalFieldRow(data);
		},isSingle:true});
     }
     // 显示扩展字段选择后的行
     function showAdditionalFieldRow(data){
    	 // 判断是否已经添加了
    	 var flag=true;
    	 $("[name='additionalFieldId']").each(function(){
			if($(this).val()==data["additionalFieldId"]){
				$.ligerDialog.warn('该字段已经添加,请重新选择!');
				flag=false;
				return;
			}
   		 })
    	 if(data&&flag){
				var row=$("[name='additionalRow']:hidden").clone(false);
				row.children().each(function(i){
					if(i==0)
						$(this).text(data["additionalFieldName"]);
					else if(i==1){
						if(data["additionalFieldType"]=="xllb"){
							var optionJson=eval("("+data["option"]+")");
							var sel="<select name='fieldValue' style='width:80%'>";
							if(optionJson&&optionJson.length>0){
								for(var index=0;index<optionJson.length;index++){
									sel+="<option value='"+optionJson[index].optionValue+"'>"+optionJson[index].optionName+"</option>";
								}
							}
							sel+="</select>";
							$(this).append(sel);
						}
						else if(data["additionalFieldType"]=="wb"){
							var input="<input type='text' name='fieldValue' style='width:80%'/>";
							$(this).append(input);
						}
					}
					else if(i==4){
						var input="<input type='hidden' name='additionalFieldId' value='"+data["additionalFieldId"]+"'/>";
						$(this).append(input);
					}
				})
				row.show()
				$("[name='additionalRow']:hidden").closest("table").append(row);
			}
     }
     function saveAdditionalValue(obj){
    	 var ctx=$("#ctx").val();
    	 var appId=$("#appId").val();
    	 var param={"fieldValue":$(obj).closest("tr").children().eq(1).find("[name='fieldValue']").val(),
    			 "additionalFieldId":$(obj).closest("tr").children().eq(4).find("input:hidden").val(),"dataId":appId};
    	 $.ligerDialog.waitting('正在保存中,请稍候...');
    	 // alert(param.fieldValue+" "+param.additionalFieldId+"
			// "+param.dataId);
    	 $.post(""+ctx+"/platform/ip/additionalField/saveAdditionalValue.ht",param,function(data){
    		if(data){
    			if(data.result=="1"){// 成功
        			$.ligerDialog.closeWaitting();
        			$.ligerDialog.success(data.message);
        			$(obj).closest("tr").children().eq(3).find("[name='del']").attr("flag","true");
        		}
        		else{// 失败
        			$.ligerDialog.closeWaitting();
        			$.ligerDialog.error(data.message);
        		}
    		}
    	 })
     }
     // 删除行
     function delAdditionalValue(obj){
    	 var ctx=$("#ctx").val();
		 var flag=$(obj).attr("flag");
		 var appId=$("#appId").val();
		 $.ligerDialog.confirm('是否删除', function (yes) { 
			 if(yes){
				 if(flag!="false"){
					 var param={"additionalFieldId":$(obj).closest("tr").children().eq(4).find("input:hidden").val(),"dataId":appId};
					
			    	 $.post(""+ctx+"/platform/ip/additionalField/delAdditionalValue.ht",param,function(data){
			    		if(data){
			    			if(data.result=="1"){// 成功
			        			
			        			$(obj).closest("tr").remove();
			        			$.ligerDialog.success(data.message);
			        		}
			        		else{// 失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
			    		}
			    	 })
				 }
				 else{
					 $(obj).closest("tr").remove();
				 }
			 }
		 });
     }
	
// ======================================================================================================================
   // 选择申请国
     function addCountry(a)
     {    	
    	 if($(a).val()=='0')
    	 {    		
    		 $('#appCountry').val("中国");    		 
    		 
    	 }
    	 else
    		 {
    		 var type=$(a).val();
    		 if(type!='2')isSingle=false;
    		 else isSingle=true;//巴黎公约只能有一个
    		
    		 countryDialog({callback:function(id,counRegi){	
    			 // 添加成功后，查找到该申请号对应的复议复审信息
    		        if(id!=undefined&&id!=""){    		        	
    		        	 $('#coungtryIds').val(id); 
    		        	 $('#appCountry').val(counRegi); }
    			    },type:type,isSingle:isSingle});
    		 }
    		 
    		 
     }
 //==================================================================================================================
  //关联产品、项目、和技术领域
     //添加项目
     function addProject(){
    	 itemDialog({callback:function(id,name,itemJson){
    		 showProjectRow(itemJson);
    	 	},isSingle:true});
     }
     function showProjectRow(itemJson){
    	 if(itemJson){
    		//判断添加的项是否已经存在
    		var flag=true;
	    	 $("[name='itemId']:hidden").each(function(){
				if($(this).val()==itemJson["id"]){
					flag=false;
					$.ligerDialog.warn('该项目已经添加,请重新选择!');
					return;
				}
	   		 })
	   		 if(flag){
	   			var row=$("[name='projectRow']:hidden").eq(0).clone(false);
	   			var len=row.children().length;
	   			var appId=$.trim($("[name='id']:hidden").val());
	    		 row.children().each(function(i){
					if($(this).attr("name"))
	    			 $(this).text(itemJson[$(this).attr("name")]);
					if(i==0){
						$(this).append("<input type='hidden' value='"+itemJson["id"]+"' name='itemId'>");
					}
					if(i==len-1&&appId==""){
						$(this).children(".edit").remove();
					}
	    		 })
	    		 row.show();
	    		 $("[name='projectRow']:hidden").closest("table").append(row);	
	    		 return;
	   		 }
    	 }
     }
     function saveProject(obj){
    	 var ctx=$("#ctx").val();
    	 var param={"itemId":$(obj).closest("tr").children().eq(0).find("[name='itemId']").val(),"dataId":$("[name='id']:hidden").val(),"type":1};
    	 if($.trim($("[name='id']:hidden").val())!=""){
    		 $.ligerDialog.waitting('正在保存中,请稍候...');
        	 //alert(param.fieldValue+" "+param.additionalFieldId+" "+param.dataId);
        	 $.post(ctx+"/platform/ip/item/insertMidProject.ht",param,function(data){
        		if(data){
        			if(data.result=="1"){//成功
            			$.ligerDialog.closeWaitting();
            			$.ligerDialog.success(data.message);
            			$(obj).closest("tr").children().eq(3).find(".del").attr("flag","true");
            			$(obj).hide();
            		}
            		else{//失败
            			$.ligerDialog.closeWaitting();
            			$.ligerDialog.error(data.message);
            		}
        		}
        	 })
    	 }
    	 else{
    		 $.ligerDialog.warn('请先保存专利!');
    		 return false;
    	 }
     }
     function delProject(obj){
    	 var ctx=$("#ctx").val();
    	 var flag=$(obj).attr("flag");
    	 $.ligerDialog.confirm('是否删除', function (yes) { 
			 if(yes){
	    	 	 if(flag!="false"){
	   				param={"itemId":$(obj).closest("tr").children().eq(0).find("[name='itemId']").val(),"dataId":$("[name='id']:hidden").val(),"type":1};
	   				$.post(ctx+"/platform/ip/item/delMidProject.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){//成功
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.success(data.message);
			        			$(obj).closest("tr").remove();
			        		}
			        		else{//失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
	    	 	}
	    	 	 else{
	    	 		$(obj).closest("tr").remove();
	    	 	 }
			}
		});
     }
	 function addProduct(){
		 ProductDialog({callback:function(id,name,num,model,json){
			 showProduct(json);
		 },isSingle:true});
	 }
	 function showProduct(productJson){
		 if(productJson){
			//判断添加的项是否已经存在
    		var flag=true;
	    	 $("[name='productId']:hidden").each(function(){
	    		 		 
				if($(this).val()==productJson["id"]){
					flag=false;
					$.ligerDialog.warn('该产品已经添加,请重新选择!');
					return;
				}
	   		 })
	   		 if(flag){
	   			var row=$("[name='productRow']:hidden").eq(0).clone(false);
	   			var len=row.children().length;
	   			var appId=$.trim($("[name='id']:hidden").val());
	    		 row.children().each(function(i){
					if($(this).attr("name"))
	    			 $(this).text(productJson[$(this).attr("name")]);
					if(i==0){
						$(this).append("<input type='hidden' value='"+productJson["id"]+"' name='productId'>");
					}
					if(i==len-1&&appId==""){
						$(this).children(".edit").remove();
					}
	    		 })
	    		 row.show();
	    		 $("[name='productRow']:hidden").closest("table").append(row);	   			 
	   		 }
		 }
	 }
     function saveProduct(obj){
    	 var ctx=$("#ctx").val();
    	 var param={"productId":$(obj).closest("tr").children().eq(0).find("[name='productId']").val(),"dataId":$("[name='id']:hidden").val(),"type":1};
    	 if($.trim($("[name='id']:hidden").val())!=""){
    		 $.ligerDialog.waitting('正在保存中,请稍候...');
	    	 $.post(ctx+"/platform/ip/product/insertMidProduct.ht",param,function(data){
	    		if(data){
	    			if(data.result=="1"){//成功
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.success(data.message);
	        			$(obj).closest("tr").children().eq(3).find(".del").attr("flag","true");
	        			$(obj).hide();
	        		}
	        		else{//失败
	        			$.ligerDialog.closeWaitting();
	        			$.ligerDialog.error(data.message);
	        		}
	    		}
	    	 })
    	 }
	     else{
    		 $.ligerDialog.warn('请先保存专利!');
        	 return false;
	     }
     }
     function delProduct(obj){
    	 var ctx=$("#ctx").val();
    	 var flag=$(obj).attr("flag");
    	 $.ligerDialog.confirm('是否删除', function (yes) { 
			 if(yes){
	    	 	 if(flag!="false"){
	   				param={"productId":$(obj).closest("tr").children().eq(0).find("[name='productId']").val(),"dataId":$("[name='id']:hidden").val(),"type":1};
	   				$.post(ctx+"/platform/ip/product/delMidProduct.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){//成功
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.success(data.message);
			        			$(obj).closest("tr").remove();
			        		}
			        		else{//失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
	    	 	}
	    	 	 else{
	    	 		$(obj).closest("tr").remove();
	    	 	 }
			}
		});
     }
    
     function saveTechnology(obj){
    	 var ctx=$("#ctx").val();
    	 if($.trim($("[name='technologyId']").val())!=""){
    		 if($.trim($("[name='id']:hidden").val())!=""){
    			 var param={"technologyId":$("[name='technologyId']").val(),"dataId":$("[name='id']:hidden").val()};
            	 $.ligerDialog.waitting('正在保存中,请稍候...');
            	 $.post(ctx+"/platform/ip/patentApply/updateTechnology.ht",param,function(data){
            		if(data){
            			if(data.result=="1"){//成功
                			$.ligerDialog.closeWaitting();
                			$.ligerDialog.success(data.message);
                			$(obj).closest("tr").children().eq(1).find(".del").attr("flag","true");
                			$(obj).hide();
                		}
                		else{//失败
                			$.ligerDialog.closeWaitting();
                			$.ligerDialog.error(data.message);
                		}
            		}
            	 })
    		 }
    		 else{
    			 $.ligerDialog.warn('请先保存专利!');
        		 return false;
    		 }
    	 }
    	 else{
    		 $.ligerDialog.warn('请添加技术领域!');
    		 return false;
    	 }
     }
     function delTechnology(obj){
    	 var ctx=$("#ctx").val();
    	 var flag=$(obj).attr("flag");
    	 $.ligerDialog.confirm('是否删除', function (yes) { 
			 if(yes){
				 if(flag&&flag!="false"){
	   				param={"technologyId":0,"dataId":$("[name='id']:hidden").val()};
	   				$.post(ctx+"/platform/ip/patentApply/updateTechnology.ht",param,function(data){
	   		    		if(data){
	   		    			if(data.result=="1"){//成功
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.success(data.message);
			        			$("[name='technologyId']").val("");
			        			$("[name='technologyName']").val("");
			        			$(obj).siblings(".edit").show();
			        		}
			        		else{//失败
			        			$.ligerDialog.closeWaitting();
			        			$.ligerDialog.error(data.message);
			        		}
	   		    		}
	   		    	 })
	    	 	}
	    	 	 else{
	    	 		$("[name='technologyId']").val("");
        			$("[name='technologyName']").val("");
	    	 	 }
			}
		});
     }
     
     //同族
     function showFamily(){
    	 
    	 var obj=$('#familyDiv');
    	 var priorities="";
    	 $("[name$=priority]").each(function(){
    		 priorities+=$(this).val()+",";
    	 });
    	 var iAppNumber=$("[name='ipcPubNumber']").val();
    	 var appId=$.trim($("[name='id']:hidden").val());
    	
    	 var url=$("#ctx").val()+"/platform/ip/patentApply/family.ht?priority="+priorities+"&appId="+appId+"&iAppNumber="+iAppNumber;
         obj.html(obj.attr("tipInfo")).show().load(url);
     }
     
     
    //校验案件状态 
/*    function validStatus2()
		
			var messageMap = {};
			messageMap[0]= "发明名称及申请类型及申请人及发明人不为空。";
			messageMap[1]="请先满足申请号及申请日不为空或申请信息附件中包含“专利申请受理通知书”.";
			messageMap[2]="请先满足申请信息附件中包含“初步审查合格通知书”。";
			messageMap[3]="请先满足公开日及公开号不为空或公开信息附件中包含“发明专利申请公布通知书”。";
			messageMap[4]="请先满足来文附件中包含“进入实质审查程序通知书”或“发明专利申请公布及进入实质审查阶段通知书”。";
			messageMap[5]="请先满足有“复审”类记录";
			messageMap[6]="请先满足授权日及授权号不为空或授权信息附件中包含“办理登记手续通知书”。";
			messageMap[7]="请先满足答复OA信息附件中含“驳回通知书”且无复审类记录.";
			var temp=0;
			var message="";
		   if($("[name='title']").val()!="")//撰写中
				{		
				  temp=1;
				  $("[name='caseStatus']").val("zxz").attr("checked",true);
				}
			
		   if(($("[name='appNumber']").val()!=""&&$("[name='appDate']").val()!="")||$("[name='appAttachment']").val().indexOf("专利申请受理通知书")>0) //已申请
		        {
			       if(temp==1)
			       {  
			    	   temp=2;
			    	   $("[name='caseStatus']").val("ysl").attr("checked",true);
			       }
		        }
		   
		  
		   if($("[name='appAttachment']").val().indexOf("初步审查合格通知书")>0)//已初审
			   {			      
			      if(temp==2)
			    	  {
			    	  temp=3;
			    	  $("[name='caseStatus']").val("ycs").attr("checked",true);
			    	  }
			      else
			        { message="如果案件状态更改为已初审，"+messageMap[temp];	}		   
			   }
		  
		   if(($("[name='pubNumber']").val()!=""&&$("[name='pubDate']").val()!="")||$("[name='pubAttachment']").val().indexOf("发明专利申请公布通知书")>0) //已公开
				   {
				      if(temp==3)
				    	  {
				    	  temp=4;
				    	  $("[name='caseStatus']").val("ygk").attr("checked",true);
				    	  }
				      else
				      {  message="如果案件状态更改为已公开，"+messageMap[temp];	}		   
				   } 
		   var flagOA=false;
		   $("[title='OAattachment']").each(function(){
				if($(this).val().indexOf("进入实质审查程序通知书")>0||$(this).val().indexOf("发明专利申请公布及进入实质审查阶段通知书")>0){
					flagOA=true;
					return false;
					}});
		   if(flagOA)//实审中
			   {
			     if(temp==4)
			    	 {
			    	 temp=5;
			    	 $("[name='caseStatus']").val("ssz").attr("checked",true);
			    	 }
			     else
			      {  message="如果案件状态更改为实审中，"+messageMap[temp];	}		
			   }
		   if(document.getElementById("caseTable").rows.length>1)//复审中
			   {
			   if(temp==5)
		    	 {
		    	 temp=6;
		    	 $("[name='caseStatus']").val("fsz").attr("checked",true);
		    	 }
		     else
		      {  message="如果案件状态更改为复审中，"+messageMap[temp];	}	
			   }
		   
		   if(($("[name='authNumber']").val()!=""&&$("[name='authDate']").val()!="")||$("[name='authAttachment']").val().indexOf("办理登记手续通知书")>0) //已授权
		   {
		      if(temp==5||temp==6)
		    	  {
		    	  temp=7;
		    	  $("[name='caseStatus']").val("ysq").attr("checked",true);
		    	  }
		      else
		      {  message="如果案件状态更改为已授权，"+messageMap[temp];	}		   
		   } 
		   var flagSQ=false;
		   $("[title='OAattachment']").each(function(){
				if($(this).val().indexOf("驳回通知书")>0){
					flagSQ=true;
					return false;
					}});
		   if(flagSQ)//已驳回
			   {
			   if(temp==5||temp==6)
		    	  {
		    	  temp=8;
		    	  $("[name='caseStatus']").val("ybh").attr("checked",true);
		    	  }
		      else
		      {  message="如果案件状态更改为已驳回，"+messageMap[temp];	}	
			   }	   
		   
		  return message;				 
			
		}*///校验案件状态 
     
     
 
    //更新状态
     function updateStatus(status)
     {
    	 var appId=$("#appId").val();    	 
    	 var ctx=$("#ctx").val();
    	 param={"appId":appId,"status":status};
	   	 $.post(ctx+"/platform/ip/patentApply/updateStatus.ht",param);	   	
     }
     

     //校验赋值案件状态
     function validStatus()
		{  
     var temp=0;
	 var caseStatus=$("[name='caseStatus']:checked").val();
	 if( caseStatus!="ych" && caseStatus!="ybh")
	 {    
		 
		   if(($("[name='authNumber']").val()!=""&&$("[name='authDate']").val()!="")) //已授权-6
	        { caseStatus="ysq";
	    	  temp=6;
	    	  }
		  if(document.getElementById("caseTable").rows.length>1&&temp==0)//复审中-5
			   {
			   var  result="";
			   $("#caseTable tr td:nth-child(1)").each(function (key, value) {			 
				   result +=$(this).html();
			   }); //每行第一个td中是否是复审
			   
			   if(result.indexOf("复审")>=0)
		    	 {		    	    	
		    	 caseStatus="fsz";
		    	 temp=5;
		    	 }
			   }
		  if($("[title='OAletter']").val()!=""&&$("[title='OAdispatchDate']").val()!=""&&temp==0)//实审中-4
		   {
	    	 caseStatus="ssz";
	    	 temp=4;
	       }
		   
		  if($("[name='pubNumber']").val()!=""&&$("[name='pubDate']").val()!=""&&temp==0) //已公开-3
		   {      temp=3; 
		    	  caseStatus="ygk";
		    	  
		   } 
		  if($("[name='appNumber']").val()!=""&&$("[name='appDate']").val()!=""&&temp==0)//已申请-2
			  {
			 		    	
	    	   caseStatus="ysl";
	    	   temp=2;	
			  }
		  if($("[name='title']").val()!=""&&temp==0)//撰写中-1
			  {		
			  caseStatus="zxz";
			  temp=1;		
			  }
	 } 
       $("[name='caseStatus']").each(function()
     		{
     	if($(this).val()==caseStatus)$(this).attr("checked",true);
     		})//赋值
        return caseStatus;
	} 
     
     //校验
     function validNullAndAgentInfo()
     {  var flag=true;
        var sum=0;
    	if($("[title='applicantNameCn']").val()==""){  $.ligerDialog.warn('申请人不能为空！');flag=false;}
		if($("[title='inventorNameCn']").val()==""){  $.ligerDialog.warn('发明人不能为空！');flag=false;}				
		$("[title='inventorDevote']").each(function(){
				if($(this).val()!=""){
					var devote=Number($(this).val().replace('%', ''));
					sum +=devote;						
				}
	   		 })	
	   		
	     if(sum!=100){
	    	 $.ligerDialog.warn('发明人贡献率总和应等于100%!');
				flag=false;				
	     }
		/* if($("[name='agentInfo.agentNo']").val()=="")
	     {   
	         if($("[name='agentInfo.caseType']").val()!=""||$("[name='agentInfo.caseClass']").val()!=""||$("[name='agentInfo.caseStyle']").val()!=""||$("[name='agentInfo.caseAssistance']").val()!=""||
	    		 $("[name='agentInfo.caseGroup']").val()!="" || $("[name='agentInfo.protocolNo']").val()!="" || $("[name='agentInfo.protocolType']").val()!="" || $("[name='agentInfo.urgentType']").val()!="" ||
	    		 $("[name='agentInfo.attachment']").val().indexOf("Name")>=0)    		
	           {   
	    	          $.ligerDialog.warn('保存代理信息需填写代理卷号！');
	                  flag=false;
	    	    }
	     }*/
		 
		 
	         if($("[title='OAvastLimitDate']").val()!=""||$("[title='OAsponsor']").val()!=""||$("[title='OAsendDate']").val()!=""||
	        		 $("[title='OAanswerDate']").val()!=""||$("[title='OAexamClause']").val()!="" || $("[title='OAattachment']").val()!="" ||
	        		 $("[title='OAattachmentOut']").val()!="")    		
	           { 
	        	 if($("[title='OAletter']").val()=="")
	  	              {  
	    	          $.ligerDialog.warn('保存OA信息需发文通知书！');
	                  flag=false;
	    	    }
	        	 else
	        		{
	           if($("[title='OAdispatchDate']").val()=="")
	              {  
 	          $.ligerDialog.warn('保存OA信息需发文日期！');
               flag=false;
 	              }
	           }
	           
	         }
	      
	       if($("[title='OAdispatchDate']").val()==""&&$("[title='OAletter']").val()!="")
	    	   {
	    		   $.ligerDialog.warn('保存OA信息需发文日期！');
	    		   flag=false;
	    	   }
	     if($("[title='OAdispatchDate']").val()!=""&&$("[title='OAletter']").val()=="")
	    	   {
	    		   $.ligerDialog.warn('保存OA信息需发文通知书！');
	    		   flag=false;
	    	   }
	   
		 return flag;
     }
     
     
     
/*     //校验赋值案件状态
     function validStatus2()
		{  
	 var temp=0;
	 var caseStatus=$("[name='caseStatus']:checked").val();
	
	 if( $("[name='caseStatus']:checked").val()!="ych")
	 {    if($("[name='title']").val()!="")//撰写中-1
				{		
				  temp=1;				
				  caseStatus="zxz";
				}
	       if(temp==1)
            {
		      if(($("[name='appNumber']").val()!=""&&$("[name='appDate']").val()!="")||$("[name='appAttachment']").val().indexOf("专利申请受理通知书")>0) //已申请-2
		        {
			        temp=2;			    	
			    	   caseStatus="ysl";
			       }
		        }	   
	       if(temp==2)
	    	  {
		   if($("[name='appAttachment']").val().indexOf("初步审查合格通知书")>0)//已初审-3
			   {	
			    	  temp=3;			    	
			    	  caseStatus="ycs";
			    	  }		
			   }
	       if(temp==3&&$("[name='patType']:checked").val()=="fmzl")
	    	  { 
		   if(($("[name='pubNumber']").val()!=""&&$("[name='pubDate']").val()!="")||$("[name='pubAttachment']").val().indexOf("发明专利申请公布通知书")>0) //已公开-4
				   {      temp=4; 
				    	  caseStatus="ygk";
				    	  }   
				   } 
		   
		   if(temp==4&&$("[name='patType']:checked").val()=="fmzl")
	    	 {
			   var flagOA=false;
			   $("[title='OAattachment']").each(function(){
					if($(this).val().indexOf("进入实质审查程序通知书")>0||$(this).val().indexOf("发明专利申请公布及进入实质审查阶段通知书")>0){
						flagOA=true;
						return false;
						}});
		     if(flagOA)//实审中-5
			   {			    
			    	 temp=5;			    	
			    	 caseStatus="ssz";
			    	 }				
			   }
		   if(temp==5||temp==3)
		   { if(document.getElementById("caseTable").rows.length>1)//复审中-6
			   {
			   var  result="";
			   $("#caseTable tr td:nth-child(1)").each(function (key, value) {			 
				   result +=$(this).html();
			   }); //每行第一个td中是否是复审
			   
			   if(result.indexOf("复审")>=0)
		    	 {
		    	 temp=6;		    	
		    	 caseStatus="fsz";
		    	 }
			   }
		   }
		  		  
		   if(temp==5||temp==6)
	    	  {
			   
			   if(($("[name='authNumber']").val()!=""&&$("[name='authDate']").val()!="")||$("[name='authAttachment']").val().indexOf("办理登记手续通知书")>0) //已授权-7
		        {
		    	  temp=7;		    
		    	  caseStatus="ysq";
		    	  }
	    	  }
		  if(temp==5||temp==6)
	    	  {
			   var flagSQ=false;
			   $("[title='OAattachment']").each(function(){
					if($(this).val().indexOf("驳回通知书")>0){
						flagSQ=true;
						return false;
						}});
		         if(flagSQ)//已驳回-8
			       {
		    	  temp=8;		    
		    	  caseStatus="ybh";
		    	  }
			   }		 
	 }   	
       $("[name='caseStatus']").each(function()
     		{
     	if($(this).val()==caseStatus)$(this).attr("checked",true);
     		})//赋值
        return caseStatus;
	} */