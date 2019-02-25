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
	String frontPath = (String)application.getAttribute("frontPath");
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
<link rel="shortcut icon" href="staticRelease/images/back/favicon.ico" type="image/x-icon" />
<script>if (/*@cc_on!@*/false && document.documentMode === 10) document.documentElement.className += ' ie10';</script>
<script>if (/*@cc_on!@*/false && document.documentMode === 11) document.documentElement.className += ' ie11';</script>
</head>
<body>
<div class="header cf">
	<div class="logo"><a href="<%=adminPath%>index.do"><img src="staticRelease/images/back/logo.jpg" alt="" width="240px"></a></div>
	<div class="header_con cf">
		<div class="con_header cf">
			<div class="header_left cf">
				<div class="logo_text sanjiao cf">
					<p title="${currentSite.siteName }">${currentSite.siteName }</p>
					<p title="${currentSite.siteDomian }">${currentSite.siteDomian }</p>
				</div>
				<div class="logo_con trans cf">
				<div class="mScrol2" style="height:135px;overflow:hidden;">
					<ul class="cf">
						<c:forEach items="${permSiteList}" var="site">
							<li>
								<a href="javascript:changeSite('${site.id}');">
									<p>${site.siteName}</p>
									<p>${site.siteDomian}</p>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
				</div>
			</div>
			<div class="header_mid cf">
				<ul class="nav01">
					<li><a href="<%=adminPath%>index.do"><p>首页</p></a></li>
					<li><a href="<%=adminPath%>contentmanage.do" class="active"><p>内容管理</p></a></li>
					<c:forEach items="${menuList}" var="menu1" varStatus="stat">
						<c:if test="${menu1.hasMenu && '1' == menu1.MENU_STATE}">
							<li><a href="<%=adminPath%>n_index/${menu1.MENU_ID }.do"><p>${menu1.MENU_NAME }</p></a></li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
			<div class="header_right cf">
				<ul>
					<li><a href="<%=basePath%>" target="_blank"><span class="ico bg-ico_04"></span>访问站点</a></li>
					<!-- <li><a href="javascript:;">后台地图</a></li> -->
					<li class="last-li sanjiao"><a href="javascript:;">${user.USERNAME}</a>
						<dl class="trans cf">
							<!-- <dd><a href="javascript:;"><span class="ico bg-ico_05"></span><p>切换账号</p></a></dd> -->
							<dd><a href="javascript:;" onclick="goUpdatePwd('${user.USER_ID}')"><span class="ico bg-ico_09"></span><p>修改密码</p></a></dd>
							<dd><a href="<%=adminPath%>logout.do"><span class="ico bg-ico_11"></span><p>退出</p></a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div id="jzts" style="display:none; width:100%; position:fixed; z-index:99999999;top:0px;left:0px;bottom:0px;right:0px;background:rgba(0,0,0,0.4)">
	<div class="commitopacity" id="bkbgjz"></div>
	<div class="jzts" style="position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);">
		<div style="float:left;"><img src="static/images/loadingi.gif" /> </div>
		<div style="float:left;margin-top:-4px;margin-left:5px;"><h4 class="lighter block red" style="font-size:16px;color:#fff;">&nbsp;加载中</h4></div>
	</div>
</div>

<div class="body_contact cf">
	<iframe id="leftiframe" src="<%=adminPath%>leftnavcontent.do"></iframe>
</div>
<script type="text/javascript" src="static/js/myjs/head.js"></script>
<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
<!--引入弹窗组件2start-->
<script type="text/javascript" src="plugins/attention/drag/drag.js"></script>
<script type="text/javascript" src="plugins/attention/drag/dialog.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/attention/drag/style.css"  />

<!--引入弹窗组件2end-->

<script type="text/javascript">
//清除加载进度
function hangge(){
	$("#jzts").hide();
}
//显示加载进度
function jzts(){
	$("#jzts").show();
}
function mesageTip(z,a){
	if(z=='success'){
		html='<div class="layer_bg layer_bg03 layer_tishi"><div class="layer_con cf tishi"><p><span class="ico bg-tishi02"></span><em>'+a+'</em></p></div></div>'
	}else if(z=='failure'){
		html='<div class="layer_bg layer_bg03 layer_tishi"><div class="layer_con cf tishi"><p><span class="ico bg-tishi01"></span><em>'+a+'</em><span class="ts_close">x</span></p></div></div>'
	}else if(z=='warn'){
		html='<div class="layer_bg layer_bg03 layer_tishi"><div class="layer_con cf tishi"><p><span class="ico bg-tishi03"></span><em>'+a+'</em></p></div></div>'
	}
	$('body').append(html);
	
	if(z=='failure'){
		$(document).on('click','.ts_close',function(){
			$(this).parents('.layer_bg').animate({
				'opacity':'0'
			},500,function(){
				$('.layer_bg').remove()
			})
		})
	}else{
		//这里原来是.layer_bg03   下载栏目,没有选择模板弹窗,保存提示错误,然后再点击选择模板,就没有弹窗内容
		setTimeout(function(){
			$('.layer_tishi').animate({
				'opacity':'0'
			},1500,function(){
				$('.layer_tishi').remove()
			});
		},1500);
	}
}
function goUpdatePwd(id){
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改密码";
	 diag.URL = '<%=adminPath%>user/goUpdatePwd.do?USER_ID='+id;
	 diag.Width = 613;
	 diag.Height = 397;
	 diag.Modal = true;				//有无遮罩窗口
	 diag. ShowMaxButton = true;	//最大化按钮
    diag.ShowMinButton = true;		//最小化按钮 
	 diag.CancelEvent = function(){ //关闭事件
   	 diag.close();
	 };
	 diag.show();
}
function changeSite(siteId)
{
	$.cookie("_site_id_cookie",siteId,{path: adminPath });
	location.href="<%=adminPath%>index.do?changeSite=1&_site_id_param="+siteId;
}

</script>
</body>
</html>