$(function(){
	$("#search").click(function(){
		if($("[name='measurement']:checked").length!=0){
			if($("#beginAnalysisTime").val()==""){
				$.ligerDialog.warn('请选择统计区间!');
				return false;
			}
			else{
				if(!validation()){
					$.ligerDialog.warn('只能统计一个年度的数据,请重新选择统计区间!');
					$("#beginAnalysisTime").val("");
					return false;
				}
			}
		}
		$("#searchForm").submit();
	})
	function validation(){
		var measurement=$("[name='measurement']:checked").val();
		if(measurement&&measurement!=3){
			//判断两个日期是否是在同一年，不能跨年统计
			var beginDate=$("[name='beginAnalysisTime']").val();
			var endDate=$("[name='endAnalysisTime']").val();
			if(beginDate&&endDate){
				return beginDate.substring(0,4)==endDate.substring(0,4);
			}
			return false;
		}
		return true;
	}
});