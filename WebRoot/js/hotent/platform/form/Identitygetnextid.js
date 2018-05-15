function Identitygetnextid(alias,callBack){
	if(alias==null || alias==undefined){
		$.ligerDialog.warn("别名为空！",'提示信息');
		return;
	}
	var url=__ctx + "/platform/system/identity/getnextId.ht?alias=" +alias;
	url=url.getNewUrl();
	$.post(url,{"alias":alias},function(data){
		if(data.success==0){
			$.ligerDialog.warn("输入别名不正确！",'提示信息');
			return;
		}
		if(callBack){
			callBack(data);
		}
	});
}