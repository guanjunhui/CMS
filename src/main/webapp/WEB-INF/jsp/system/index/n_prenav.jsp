<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="cms_c_inst neirong cf">
	<div class="left cf">
	<a href="javascript:void(0)">首页</a>
	</div>
</div>
<script type="text/javascript">
$(function(){
	var path=window.location.href;
	$.ajax({
		type: "GET",
		url:adminPath+"getparentMenu",
		data:{'path':path},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 appendPreHtml(result.data);
			 }else{
			 }
		}
	});
})
function appendPreHtml(navque){
	var html='';
	html=eachPre(html,navque);
	$(".left").append(html);
}
function eachPre(html,queue){
	var parent=queue.parentMenu;
	while(parent!=null){
		html='<i>></i>'+'<a href="javascript:void(0)">'+parent.menu_NAME+'</a>'+html;
		parent=parent.parentMenu;
	}
	html+='<i>></i>'+'<i>'+queue.menu_NAME+'</i>';
	return html;
}

</script>