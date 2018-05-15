//产品选择对话框
function ProductDialog(conf)
{
	
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/product/dialog.ht?isSingle='+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '产品选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.productId,rtn.productName,rtn.productNum,rtn.productModel,rtn.productJson);
        }
    });
	
 /* var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  var b=__ctx+"/platform/ip/product/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }
  else if(h.ids){
		  b+="&ids="+h.ids;
	}
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
      h.callback.call(this,j.productId,j.productName,j.productNum,j.productModel,j.productJson);
    }
  }*/
}
//项目选择对话框
function itemDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/item/dialog.ht?isSingle='+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '项目选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.itemId,rtn.itemName,rtn.itemJson);
        }
    });
  /*var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  var b=__ctx+"/platform/ip/item/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
      h.callback.call(this,j.itemId,j.itemName,j.itemJson);
    }
  }*/
}
//专利选择对话框
function patentDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/patent/dialog.ht?isSingle="+conf.isSingle;
	if(conf.typeId){
		url+="&typeId="+conf.typeId;
	  }
	  if(conf.formType){
		  url+="&formType="+conf.formType;
		  if(conf.formType=="zlqqfx"&&conf.productIds){
			  url+="&productIds="+conf.productIds;
		  }
	  }
	  
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '专利选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.patentId,rtn.patentName,rtn.applycode,rtn.applydate,rtn.isApplyFeereduce,rtn.patentType,rtn.cateMaNum,rtn.patentJson,rtn.patentCountry,rtn.patNo);
        }
    });
	
  /*var e=805;
  var l=500;
  h=$.extend(
  {
  }
  ,
  {
    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1
  }
  ,h);
  var dialogLeft=(window.screen.width - e) / 2;
  var dialogTop=(window.screen.height - l) / 2;
  
  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  var b=__ctx+"/platform/ip/patent/dialog.ht?isSingle="+h.isSingle;
  if(h.typeId){
	  b+="&typeId="+h.typeId;
  }
  if(h.formType){
	  b+="&formType="+h.formType
  }
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }
  else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    { //alert(j.patentCountry);
      h.callback.call(this,j.patentId,j.patentName,j.applycode,j.applydate,j.isApplyFeereduce,j.patentType,j.cateMaNum,j.patentJson,j.patentCountry,j.patNo);
    }
  }*/
}




//费用选择对话框
function expenseDialog(conf)
{
	
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/standard/dialog.ht?isSingle="+conf.isSingle+"&standardAttr="+conf.standardAttr;
	
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '费项选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.expenseId,rtn.expenseName,rtn.json,rtn.shortName,rtn.expenseType);
        }
    });
 /* var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  if(!h.isSave){
	  h.isSave=false;
  }
  //alert(h.standardAttr);
  var b=__ctx+"/platform/ip/standard/dialog.ht?isSingle="+h.isSingle+"&standardAttr="+h.standardAttr;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }else if(h.ids){
	  b+="&ids="+h.ids;
	  if(h.patentType){		 
		  b+="&patentType="+h.patentType;
	  }
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
      h.callback.call(this,j.expenseId,j.expenseName,j.json,j.shortName,j.expenseType);
    }
  }*/
}
//提案选择对话框，参数h是一个json数据格式的js对象
function proposalDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/proposal/dialog.ht?isSingle="+conf.isSingle;
	
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '提案选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.proposalId,rtn.proposalName,rtn.json,rtn.proposalTran);
        }
    });
  /*var e=805;
  var l=500;
  h=$.extend(
  {
  }
  ,
  {
    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1
  }
  ,h);
  var dialogLeft=(window.screen.width - e) / 2;
  var dialogTop=(window.screen.height - l) / 2;
  
  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  if(!h.isSave){
	  h.isSave=false;
  }
  var b=__ctx+"/platform/ip/proposal/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
    	
    	h.callback.call(this,j.proposalId,j.proposalName,j.json,j.proposalTran);//调用回调方法
    }
  }*/
}
//技术领域选择对话框
function technologyDialog(h)
{
  var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
 var dialogLeft=(window.screen.width - e) / 2;
var dialogTop=(window.screen.height - l) / 2;
 var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
 if(!h.isSingle)
  {
    h.isSingle=false;
  }
  if(!h.isSave){
	  h.isSave=false;
  }
  var b=__ctx+"/platform/ip/technology/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
    	h.callback.call(this,j.technologyId,j.technologyName,j.technologyJson);
    }
  }
}
//添加扩展字段
function AdditionalFieldDialog(conf)
{
	
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/additionalField/dialog.ht?isSingle='+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '扩展字段选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.additionalFieldJson);
        }
    });
  /*var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  var b=__ctx+"/platform/ip/additionalField/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }
  else if(h.ids){
	  b+="&ids="+h.ids;
	}
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {
      h.callback.call(this,j.additionalFieldJson);
    }
  }*/
}
// 申请选择

function ApplyDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/patentApply/dialog.ht?isSingle='+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '申请选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.applyId,rtn.applyNumber,rtn.patentJson,rtn.appDate,rtn.appCoun);
        }
    });
  /*var e=805;
  var l=600;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  if(!h.isSingle)
  {
    h.isSingle=false;
  }
  if(!h.isSave){
	  h.isSave=false;
  }
  var b=__ctx+"/platform/ip/patentApply/dialog.ht?isSingle="+h.isSingle;
  b=b.getNewUrl();
  var g=new Array();
  if(h.isPriority){
	  b+="&isPriority="+h.isPriority
  }
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else if(h.patType){
	  b+="&patType="+h.patType;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);

  if(h.callback)
  {
    if(j!=undefined)
    {
    	h.callback.call(this,j.applyId,j.applyNumber,j.patentJson,j.appDate,j.appCoun);//调用回调方法
    }
  }*/
}
  //复审复议选择

function ReExamDialog(h)
{
  var e=1200;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:1,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
    var b=__ctx+"/platform/ip/patentReexam/dialog.ht?appId="+h.appId+"&alias="+h.alias+"&id="+h.id;
   
  b=b.getNewUrl();
  var g=new Array(); 
    if(h.arguments)
    {
      g=h.arguments;
    }

  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {  
    	h.callback.call(this,j.json);//调用回调方法
    }
  }
}
//无效诉讼
function lawSuitDialog(h)
{
	  var e=1200;
	  var l=700;
	  h=$.extend(
			  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
			  var dialogLeft=(window.screen.width - e) / 2;
			  var dialogTop=(window.screen.height - l) / 2;
			  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
    var b=__ctx+"/platform/ip/lawsuit/dialog.ht?patentId="+h.patentId+"&alias=ssbh&lawsuitId="+h.lawsuitId+"&type="+h.type;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {
	    if(j!=undefined)
	    {
	    	h.callback.call(this,j.lawsuitJson);//调用回调方法
	    }
	  }
	}
//专利交易
function tradeDialog(h)
{
	  var e=1200;
	  var l=700;
	  h=$.extend(
			  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
			  var dialogLeft=(window.screen.width - e) / 2;
			  var dialogTop=(window.screen.height - l) / 2;
			  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
    var b=__ctx+"/platform/ip/trade/dialog.ht?patentId="+h.patentId+"&alias="+h.alias+"&tradeId="+h.tradeId;
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }
	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {
	    if(j!=undefined)
	    {
	    	h.callback.call(this,j.tradeJson);//调用回调方法
	    }
	  }
	}
//专利奖励
function awardDialog(h)
{
	  var e=1200;
	  var l=700;
	  h=$.extend(
			  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
			  var dialogLeft=(window.screen.width - e) / 2;
			  var dialogTop=(window.screen.height - l) / 2;
			  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
    var b=__ctx+"/platform/ip/award/dialog.ht?patentId="+h.patentId+"&alias="+h.alias+"&awardId="+h.awardId;
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }
	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {
	    if(j!=undefined)
	    {
	    	h.callback.call(this,j.awardJson);//调用回调方法
	    }
	  }
	}

//费用选择

function exDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/expenseManger/dialog.ht?appId="+conf.appId+"&id="+conf.id+"&year="+conf.year+"&agency="+conf.agency+"&isReduce="+conf.isReduce;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '费用添加 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.json);
        }
    });
  /*var e=1200;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  var b=__ctx+"/platform/ip/expenseManger/dialog.ht?appId="+h.appId+"&id="+h.id+"&year="+h.year+"&agency="+h.agency+"&isReduce="+h.isReduce;
   
  b=b.getNewUrl();
  var g=new Array(); 
    if(h.arguments)
    {
      g=h.arguments;
    }

  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {  
    	h.callback.call(this,j.json);//调用回调方法
    }
  }*/
}

//选择国家
function countryDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/country/dialog.ht?type="+conf.type+"&isSingle="+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '国家选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.id,rtn.counRegi);
        }
    });
  /*var e=1200;
  var l=600;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  var b=__ctx+"/platform/ip/country/dialog.ht?type="+h.type+"&isSingle="+h.isSingle;
 
  b=b.getNewUrl();
  var g=new Array(); 
    if(h.arguments)
    {
      g=h.arguments;
    }

  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {  
    	h.callback.call(this,j.id,j.counRegi);//调用回调方法
    }
  }*/
}



//专利选择对话框
function patentAllDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);

	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/patent/allDialog.ht?isSingle="+conf.isSingle;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '专利申请选择器 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.patentId,rtn.patentName,rtn.applycode,rtn.applydate,rtn.isApplyFeereduce,rtn.patentType,rtn.caseNum,rtn.patentJson);
        }
    });
  /*var e=805;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
    if(!h.isSingle)
  {
    h.isSingle=false;
  }
  var b=__ctx+"/platform/ip/patent/allDialog.ht?isSingle="+h.isSingle;
  if(h.formType){
	  b+="&formType="+h.formType;
  }
  b=b.getNewUrl();
  var g=new Array();
  if(h.ids&&h.names)
  {
    var a=h.ids.split(",");
    var k=h.names.split(",");
    for(var f=0;f<a.length;f++)
    {
      var d=
      {
        id:a[f],name:k[f]
      };
      g.push(d);
    }
  }
  else if(h.ids){
	  b+="&ids="+h.ids;
  }
  else
  {
    if(h.arguments)
    {
      g=h.arguments;
    }
  }
  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    { 
      h.callback.call(this,j.patentId,j.patentName,j.applycode,j.applydate,j.isApplyFeereduce,j.patentType,j.caseNum,j.patentJson);
    }
  }*/
}


//年费选择
function yearFeeDialog(conf)
{
	var dialogWidth=805;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);


	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + "/platform/ip/expenseManger/annualDialog.ht?appId="+conf.appId+"&alias="+conf.alias+"&year="+conf.year+"&isReduce="+conf.isReduce+"&appNo="+conf.appNo;
	url=url.getNewUrl();
	
	//重新选择的时候，展现上次数据
	var arrys = new Array();
	if(  conf.ids && conf.names){
		var ids=conf.ids.split(",");
		var names=conf.names.split(",");
		for ( var i = 0; i < ids.length; i++) {
			var selectUsers={
					id:ids[i],
					name:names[i]
			}
			arrys.push(selectUsers);
		}
		
	}else if(conf.arguments){
		arrys=conf.arguments;
	}	

	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '年费添加 ',
        url: url, 
        isResize: true,
        //自定义参数
        arrys: arrys,
        sucCall:function(rtn){
        	conf.callback.call(that,rtn.id);
        }
    });
  /*var e=600;
  var l=500;
  h=$.extend(
		  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
		  var dialogLeft=(window.screen.width - e) / 2;
		  var dialogTop=(window.screen.height - l) / 2;
		  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
  var b=__ctx+"/platform/ip/expenseManger/annualDialog.ht?appId="+h.appId+"&alias="+h.alias+"&year="+h.year+"&isReduce="+h.isReduce+"&appNo="+h.appNo;
   
  b=b.getNewUrl();
  var g=new Array(); 
    if(h.arguments)
    {
      g=h.arguments;
    }

  var j=window.showModalDialog(b,g,c);
  if(h.callback)
  {
    if(j!=undefined)
    {  
    	h.callback.call(this,j.id);//调用回调方法
    }
  }*/
}

function  distributeDialog(conf)
{
	var dialogWidth=600;
	var dialogHeight=520;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/patentApply/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '申请案件分配',
        url: url, 
        isResize: true,
       // scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    });
	  /*var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/patentApply/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }*/
	}
function  distributePatentDialog(conf)
{
	var dialogWidth=600;
	var dialogHeight=520;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/platform/ip/patent/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '专利案件分配',
        url: url, 
        isResize: true,
       // scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    }); 
	/*var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/patent/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }*/
	}

/*function  distributeProposalDialog(h)
{
	  var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/proposal/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }
	}*/

function distributeProposalDialog(conf)
{
	var dialogWidth=400;
	var dialogHeight=320;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var scope="{type:\"system\",value:\"all\"}";
	if(conf.scope) scope = (conf.scope).replace(/\'/g, '"');	
	var url=__ctx + '/platform/ip/proposal/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '案件分配',
        url: url, 
        isResize: true,
        scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    });
}


function distributeTradeDialog(conf)
{
	var dialogWidth=400;
	var dialogHeight=320;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var scope="{type:\"system\",value:\"all\"}";
	if(conf.scope) scope = (conf.scope).replace(/\'/g, '"');	
	var url=__ctx + '/platform/ip/trade/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '案件分配',
        url: url, 
        isResize: true,
        scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    });
}
/*function  distributeTradeDialog(h)
{
	  var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/trade/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }
	}
function  distributeLawsuitDialog(h)
{
	  var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/lawsuit/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }
	}
function  distributeAwardDialog(h)
{
	  var e=400;
	  var l=120;
	  h=$.extend(
	  {} ,  {    dialogWidth:e,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  var dialogLeft=(window.screen.width - e) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+e+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";center=yes;dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/award/distribute.ht?id="+h.id;
	   
	  b=b.getNewUrl();
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }

	  var j=window.showModalDialog(b,g,c);
	  if(h.callback)
	  {  	h.callback.call(this);//调用回调方法
	   	  }
	}*/

function distributeLawsuitDialog(conf)
{
	var dialogWidth=400;
	var dialogHeight=320;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var scope="{type:\"system\",value:\"all\"}";
	if(conf.scope) scope = (conf.scope).replace(/\'/g, '"');	
	var url=__ctx + '/platform/ip/lawsuit/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '案件分配',
        url: url, 
        isResize: true,
        scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    });
}

function distributeAwardDialog(conf)
{
	var dialogWidth=400;
	var dialogHeight=320;	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);	
	if(!conf.isSingle)conf.isSingle=false;
	var scope="{type:\"system\",value:\"all\"}";
	if(conf.scope) scope = (conf.scope).replace(/\'/g, '"');	
	var url=__ctx + '/platform/ip/award/distribute.ht?id='+conf.id;;
	url=url.getNewUrl();	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '案件分配',
        url: url, 
        isResize: true,
        scope : scope,
        //回调函数
       sucCall:function(rtn){
        	conf.callback();
        }
    });
}
//邮件附件转存
function transferToPatentAttachDialog(h){
	var w=805;
	var l=500;
	//中文参数处理
	//var obj=new Object();
	  h=$.extend(
	  {} ,  {    dialogWidth:w,dialogHeight:l,help:0,status:0,scroll:0,center:1}  ,h);
	  //var dialogLeft=($(document).width() - w) / 2;
	  //var dialogTop=($(document).height() - l) / 2;
	  var dialogLeft=(window.screen.width - w) / 2;
	  var dialogTop=(window.screen.height - l) / 2;
	  var c="dialogWidth="+h.dialogWidth+"px;dialogHeight="+h.dialogHeight+"px;help="+h.help+";status="+h.status+";scroll="+h.scroll+";dialogLeft="+dialogLeft+"px;dialogTop="+dialogTop+"px";
	  var b=__ctx+"/platform/ip/patent/attachDialog.ht?isSingle="+h.isSingle;
	  //b=b.getNewUrl();
	  if(h.fileId)
	  {
	    b+="&fileId="+h.fileId;
	  }
	  if(h.mailId)
	  {
	    b+="&mailId="+h.mailId;
	  }
	  if(h.mailSetId)
	  {
	    b+="&mailSetId="+h.mailSetId;
	  }
	  if(h.filename)
	  {
	    b+="&filename="+h.filename;
		  //obj.filename=h.filename  
	  }
	  
	  var g=new Array(); 
	    if(h.arguments)
	    {
	      g=h.arguments;
	    }
	  var j=window.showModalDialog(encodeURI(b),g,c);
	  if(h.callback)
	  {  	
		  h.callback.call(this);//调用回调方法
	  }
}
