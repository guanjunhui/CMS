//检索
function searchs(){
	top.jzts();
	$("#schoolForm").submit();
}

//获取字典数据
function getDic(id,code){
	$.ajax({
		type: "GET",
		url: adminPath+"dictionaries/getDic?code="+code,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 $.each(result.data, function(i, item){
			         var html='<option value="'+item.DICTIONARIES_ID +'">'+item.NAME+'</option>';
					 $("#"+id).append(html);
				 });
	            $("#"+id).trigger("chosen:updated");
			 }
		}
	});
}

$(function(){
	getDic('scTuition','004');//添加学费检索条件
	getDic('scScale','005');//添加规模检索条件
	getDic('scCourse','002');//添加课程检索条件
	getDic('scArea','003');//添加地区检索条件
	getDic('scCountry','006');//添加大学留学国家检索条件

});