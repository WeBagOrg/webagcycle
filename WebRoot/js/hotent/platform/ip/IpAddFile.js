IpAddFile=function(b,c,key)//树div的id，c配置的事件以及url，key指定businessKey即业务主键
{
  this.fileTree=null;
  this.currentNode=null;
  this.conf=c;//指定事件和url
  //this.name=d;//指定文档类型名
  this.divId=b;//指定页面中树div的id
  this.bussinessKey=key;
  var a=this;
  this.loadFileTree=function()
  {
    var f=
    {
      data:
      {
        key:
        {
          name:"name"
        }
        ,simpleData:
        {
          enable:true,idKey:"id",pIdKey:"pId"
        }
      }
      ,
      callback:
      {
        onClick:this.clickHandler,onRightClick:this.rightClickHandler
      }
    };
    var e=__ctx+"/platform/ip/files/addFile.ht";
    if(c.url)
    {
      e=c.url;
    }
    var g=
    {
      //name:this.name,
      bussinessKey:this.bussinessKey
    };
    $.post(e,g,function(q)
    {
      for(var m=0;m<q.length;m++)
      {
        var k=q[m];
        if(k.pId==-1)
        {
          k.icon=__ctx+"/styles/default/images/icon/root.png";
          k.isRoot=1;
        }
      }
      a.fileTree=$.fn.zTree.init($("#"+a.divId),f,q);//初始化树
      var l=a.conf.expandByDepth;
      if(l!=null&&l>=0)
      {
		var h=a.fileTree.getNodesByFilter(function(i)
		{
		  return(i.level==l);
		});
		if(h.length>0)
		{
		  for(var p=0;p<h.length;p++)
		  {
		    a.fileTree.expandNode(h[p],false,false);
		  }
		}
      }
      else
      {
    	  a.fileTree.expandAll(true);
      }
      var o=$(".tree-toolbar").height();
      var j=$("#defLayout").height();
      var n=(parseInt(j)-parseInt(o)*3)+"px";
      $("#"+a.divId).css(
	  {
		  "height":n
	  });
    });//post方法的结束位置
  };//loadFileTree方法的结束位置
this.rightClickHandler=function(e,g,f)//event, treeId, treeNode
{
	if(a.conf.onRightClick)//如果设置了右击事件，则使用设置的右击事件
	{
		a.conf.onRightClick(e,g,f);
	}
};
this.clickHandler=function(e,g,f)
{
	a.currentNode=f;
	if(a.conf.onClick)
	{
		a.conf.onClick(f);
	}
};
this.treeExpandAll=function(e)//expandFlag,true or false
{
	a.fileTree.expandAll(e);
};
this.delNode=function()//删除节点
{
	 var c=function(q)
	  {
	    if(!q)
	    {
	      return;
	    }
	    var f=a.currentNode.id;//表中加载的主键，也是树节点的id
	    if(f!=0)
	    {
		    var e=__ctx+"/platform/ip/files/delFile.ht";
		    var g=
		    {
		    	id:f
		    };
		    $.post(e,g,function(h)
		    {
		    	a.loadFileTree();
		    	$.ligerDialog.success("成功删除!","提示信息");
		    });
	    }
	  };
	  $.ligerDialog.confirm("确认删除此分类吗？","提示信息",c);
};
//定义支持的事件
this.openAddFileDlg=function(h,k)//true
{
	var i=a.currentNode.id;
	var name=a.currentNode.name;
	var f=a.currentNode.isRoot;
	var g=__ctx+"/platform/ip/files/addFileDialog.ht";
	if(h)
	{
		g+="?id="+i+"&name="+name;
	}
	else
	{
		g+="?id="+i;
	}
	if(k)
	{
		g+="&isPrivate=1";
	}
	var e="dialogWidth:500px;dialogHeight:250px";
	g=g.getNewUrl();
	var j=window.showModalDialog(g,"",e);
	a.loadFileTree();
};
};
