//输入框高亮特效
function hilite(o){
o = document.getElementById("div"+o.name.toString());
o.style.border ='2px solid #007EFF';//变色
}
function delite(obj){
obj = document.getElementById("div"+obj.name.toString());
obj.style.border ='';//恢复边框
}


//复选框背景特效
;(function($$){
	$$.fn.hcheckbox=function(options){
		$$(':checkbox+label',this).each(function(){
			$$(this).addClass('checkbox');
            if($$(this).prev().is(':disabled')==false){
                if($$(this).prev().is(':checked'))
				    $$(this).addClass("checked");
            }else{
                $$(this).addClass('disabled');
            }
		}).click(function(event){
				if(!$$(this).prev().is(':checked')){
				    $$(this).addClass("checked");
                    $$(this).prev()[0].checked = true;
                }
                else{
                    $$(this).removeClass('checked');			
                    $$(this).prev()[0].checked = false;
                }
                event.stopPropagation();
			}
		).prev().hide();
	}


})(jQuery)


$(function(){
	$('#chklist').hcheckbox();

	$('#btnOK').click(function(){
		var checkedValues = new Array();
		$('#chklist :checkbox').each(function(){
			if($(this).is(':checked'))
			{
				checkedValues.push($(this).val());
			}
		});

		alert(checkedValues.join(','));

	})
});




