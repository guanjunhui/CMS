<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = "//"
			+ request.getServerName()
			+ path + "/";
	String adminPath = (String)application.getAttribute("adminPath");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="n_top.jsp"%>

<meta charset="utf-8">
<meta name="author" content="jinma" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1" name="viewport" />
<title>后台首页</title>
<script>if (/*@cc_on!@*/false && document.documentMode === 10) document.documentElement.className += ' ie10';</script>
<script>if (/*@cc_on!@*/false && document.documentMode === 11) document.documentElement.className += ' ie11';</script>
</head>
<body>
<div class="body_contact cf">
	<div class="body_left cf">
		<ul class="nav01">
		</ul>
	</div>
	<div class="body_right cf">
		<div class="body_right_con cf">
			<!-- cms_con开始 -->
			<iframe src=""></iframe>
			<!-- cms_con结束 -->
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	appenNavdHtml('${navList}');
	$('.body_left .nav01').find("li").eq(0).find("a").each(function(){
		var asrc=$(this).data('src');
		if(asrc){
			if(asrc.indexOf(".do")!=-1){
				$('.body_left .nav01 a').removeClass('hover');
				$(this).addClass('hover');
				$('.body_right iframe').attr('src',asrc);
				if(!$(this).parents('li').hasClass('active')){
					$('.nav01 li').removeClass('active');
					$(this).parents('li').addClass('active');
					$('.nav01 li').find('dl').slideUp();
					$(this).parents("dl").slideToggle();
				}
				return false;
			}
		}
	});
	//导航点击效果
	$('.nav01 li>a').click(function(e){
		if(!$(this).parent('li').hasClass('active')){
			$('.nav01 li').removeClass('active');
			$(this).parent('li').addClass('active');
			$('.nav01 li').find('dl').slideUp();
			$(this).next('dl').slideToggle();
		}
	});
	$('.nav01 li>a+dl dd>a').click(function(e){
		$(this).toggleClass('e_active');
		$(this).next('dl').slideToggle();
	});
	$(document).on('click','.body_left .nav01 a',function(e){
		var Asrc=$(this).data('src');
		$('.body_left .nav01 a').removeClass('hover');
		$(this).addClass('hover');
		$('.body_right iframe').attr('src',Asrc);
	});
})

function appenNavdHtml(navlist){
	var html='';
	var list=eval(navlist);
	$.each(list, function(i, n){
		html+='<li>';
		if(n.subMenu!=null&&n.subMenu.length>0){
			html+='<a href="javascript:;"><span class="'+n.menu_ICON+'"></span><p>'+n.menu_NAME+'</p><i></i></a>';
			html+='<dl>';
			html+=eachNav('',n.subMenu);
			html+='</dl>';
		}else{
			html+='<a href="javascript:;" data-src="<%=adminPath%>'+n.menu_URL+'"><span class="'+n.menu_ICON+'"></span><p>'+n.menu_NAME+'</p></a>';
		}
		html+='</li>';
	});
	$(".nav01").append(html);
}
function eachNav(html,list){
	$.each(list, function(i, n){
		html+='<dd>';
		if(n.subMenu!=null&&n.subMenu.length>0){
			html+='<a href="javascript:;" ><span class="'+n.menu_ICON+'"></span>'+n.menu_NAME+'<i></i></a>';
			html+='<dl>';
			html+=eachNav('',n.subMenu);
			html+='</dl>';
		}else{
			html+='<a href="javascript:;" data-src="<%=adminPath%>'+n.menu_URL+'"><span class="'+n.menu_ICON+'"></span>'+n.menu_NAME+'</a>';
		}
		html+='</dd>';
	});
	return html;
}

</script>
</body>
</html>