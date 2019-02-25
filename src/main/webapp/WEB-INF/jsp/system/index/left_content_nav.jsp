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
<style type="text/css">
.body_left .nav01 li a q{
	display: inline-block;width: 40px;height: 20px;
	  position: absolute;
    top: 20px;
    right: 10px;
}
.body_left .nav01 li a q i{top: 10px;}
.body_left .nav01 li dl a q{top:10px;}
.body_left .nav01 li dl a q i{top: 10px;}
</style>
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
	var navlist='${navList}';
	if(navlist==null||navlist==undefined||navlist.length<=0){
		$(".body_right_con").html('<div class="error" style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">抱歉！您没有权限访问此页面，请联系管理员。</div>');
	}else{
		appenNavdHtml(navlist);
	}
	
	//首次进入此页面时，左侧导航栏默认选中
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
	/* $('.nav01 li>a q').click(function(e){
		if(!$(this).parents('li').hasClass('active')){
			$('.nav01 li').removeClass('active');
			$(this).parents('li').addClass('active');
			$('.nav01 li').find('dl').slideUp();
			$(this).parent().next('dl').slideToggle();
		}
		e.stopPropagation();
	});
	$('.nav01 li>a+dl dd>a q').click(function(e){
		$(this).parent().toggleClass('e_active');
		$(this).parent().next('dl').slideToggle();
		e.stopPropagation();
	}); */
	$('.nav01 li>a').click(function(e){
		if(!$(this).parents('li').hasClass('active')){
			$('.nav01 li').removeClass('active');
			$(this).parents('li').addClass('active');
			$('.nav01 li').find('dl').slideUp();
			$(this).parent().next('dl').slideToggle();
		}
	});
	$(document).on('click','.body_left .nav01 a',function(e){
		var Asrc=$(this).data('src');
		$('.body_left .nav01 a').removeClass('hover');
		$(this).addClass('hover');
		$('.body_right iframe').attr('src',Asrc);
		if($(this).next('dl').css('display')=='none'&&$(this).next('dl').size()>0){
			$(this).next('dl').slideDown();
			//$(this).parents('li').addClass('active');
		}else{
			$(this).next('dl').slideUp();
			//$(this).parents('li').removeClass('active');
		}
	});
})

function appenNavdHtml(navlist){
	var html='';
	var list=eval(navlist);
	$.each(list, function(i, n){
		html+='<li>';
		if(n.childList!=null&&n.childList.length>0){
			html+='<a href="javascript:;"><span></span><p>'+n.name+'</p><q><i></i></q></a>';
			html+='<dl>';
			html+=eachNav('',n.childList);
			html+='</dl>';
		}else{
			html+='<a href="javascript:;" data-src="<%=adminPath%>columcontent_colum/managecontent.do?ID='+n.id+'&topColumId='+n.id+'"><span></span><p>'+n.name+'</p></a>';
		}
		html+='</li>';
	});
	$(".nav01").append(html);
}
function eachNav(html,list){
	$.each(list, function(i, n){
		html+='<dd>';
		if(n.childList!=null&&n.childList.length>0){
			html+='<a href="javascript:;" data-src="<%=adminPath%>columcontent_colum/managecontent.do?ID='+n.id+'&topColumId='+n.id+'"><span></span>'+n.name+'<q><i></i></q></a>';
			html+='<dl>';
			html+=eachNav('',n.childList);
			html+='</dl>';
		}else{
			html+='<a href="javascript:;" data-src="<%=adminPath%>columcontent_colum/managecontent.do?ID='+n.id+'&topColumId='+n.id+'"><span></span>'+n.name+'</a>';
		}
		html+='</dd>';
	});
	return html;
}

</script>
</body>
</html>