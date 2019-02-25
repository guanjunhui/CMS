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
<script type="text/javascript" src="plugins/echarts/echarts.min.js"></script>
<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
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
					<li><a href="<%=adminPath%>index.do" class="active"><p>首页</p></a></li>
					<li><a href="<%=adminPath%>contentmanage.do"><p>内容管理</p></a></li>
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
					<!-- <li><a href="javascript:void(0);">后台地图</a></li> -->
					<li class="last-li sanjiao"><a href="javascript:void(0);">${user.USERNAME}</a>
						<dl class="trans cf">
							<!--<dd><a href="javascript:void(0);"><span class="ico bg-ico_05"></span><p>切换账号</p></a></dd>-->
							<dd><a href="javascript:void(0);" onclick="goUpdatePwd('${user.USER_ID}')"><span class="ico bg-ico_09"></span><p>修改密码</p></a></dd>
							<dd><a href="<%=adminPath%>logout.do"><span class="ico bg-ico_11"></span><p>退出</p></a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="body_contact cf">
	<div class="body_left cf">
		<!-- http://secondboy.coding.io/?num=92 -->
		<ul class="nav01">
			<c:forEach items="${menuList}" var="menu1" varStatus="stat">
				<c:if test="${menu1.hasMenu && '1' == menu1.MENU_STATE}">
					<c:choose>
						<c:when test="${stat.first}">
							<li><a href="<%=adminPath%>n_index/${menu1.MENU_ID }.do" class="active"><p>${menu1.MENU_NAME }</p></a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<%=adminPath%>n_index/${menu1.MENU_ID }.do"><p>${menu1.MENU_NAME }</p></a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<div class="body_right cf">
		<div class="body_right_con cf">
			<!-- cms_con开始 -->
			<div class="cms_con cf">

				<div class="cms_c_inst cf">
					<div class="left cf">
						<p>欢迎您，<span>${user.USERNAME}</span>！</p>
						<a href="javascript:void(0);" onclick="goUpdatePwd('${user.USER_ID}')" class=""><span class="ico bg-ico_08"></span>修改密码</a>
						<a href="<%=adminPath%>logout.do" class=""><span class="ico bg-ico_10"></span>退出</a>
					</div>
					<div class="right cf">
						<p>您上次登录IP：<span>${user.IP}</span></p>
						<p>您上次登录时间：<span>${user.LAST_LOGIN}</span></p>
					</div>
				</div>
				<div class="cms_c_access cms_c_access01 cf">
					<h3>网站访问情况</h3>
					<table class="ev">
						<thead>
							<tr><td width="180"></td><td>浏览量（PV）</td><td>访客数（UV）</td><td>独立IP（IP）</td></tr>
						</thead>
						<tbody id="statisline">
						</tbody>
					</table>
				</div>
				<div class="cms_c_access cms_c_access02 cf">
					<div id="statischart" class="img_con cf">
					</div>
				</div>
				<div class="cms_c_list cf" style="display:none;">
					<h3>服务器信息</h3>
					<div class="c_list_con wrap cf">
						 <dl>
							<dt>服务器计算机名</dt>
							<dd><p id="name"></p></dd>
						</dl>
						<dl>
							<dt>服务器IP地址</dt>
							<dd><p id="ipAddress"></p></dd>
						</dl>
						<dl>
							<dt>服务器域名</dt>
							<dd><p id="domain"></p></dd>
						</dl>
					     <dl>
							<dt>服务器端口</dt>
							<dd><p id="port"></p></dd>
						</dl>
						<dl>
							<dt>服务器cpu使用率</dt>
							<dd><p id="cpuPerc"></p></dd>
						</dl>
						<dl>
							<dt>服务器内存总量</dt>
							<dd><p id="memoryTotal"></p></dd>
						</dl>
						<dl>
							<dt>服务器内存使用量</dt>
							<dd><p id="memoryUsed"></p></dd>
						</dl>
						<dl>
							<dt>服务器内存可用量</dt>
							<dd><p id="memoryFree"></p></dd>
						</dl>
						<dl>
							<dt>服务器内存使用率</dt>
							<dd><p id="memoryPerc"></p></dd>
						</dl>
						<dl>
							<dt>服务器数据库所在磁盘总容量</dt>
							<dd><p id="diskTotal"></p></dd>
						</dl>
						<dl>
							<dt>服务器数据库占用量</dt>
							<dd><p id="databseTotal"></p></dd>
						</dl>
						<dl>
							<dt>服务器数据库所在磁盘剩余容量</dt>
							<dd><p></p></dd>
						</dl> 
					</div>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->
		</div>
	</div>
</div>
<!--引入弹窗组件2start-->
<script type="text/javascript" src="plugins/attention/drag/drag.js"></script>
<script type="text/javascript" src="plugins/attention/drag/dialog.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/attention/drag/style.css"  />
<!--引入弹窗组件2end-->
</body>
<script type="text/javascript">
$(function(){
	//freshServerInfo();
	//setTimeout('freshServerInfo()',60000);
	getStatisData();
	getEchartsData();
});
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
//  function freshServerInfo(){
// 	$.ajax({
 //	    url:'freshserverinfo.do', 
// 	    dataType:"json",   
// 	    async: false,  
// 	    type:"POST",   //请求方式
// 	    success: function(result){
// 		}
// 	});
// } 

function getEchartsData(){
	$.ajax({
	    url:'<%=adminPath%>pvstatistics/getpvmonthdata.do',
	    dataType:"json",//返回格式为json
	    async: false,  
	    type:"GET",//请求方式
	    success: function(result){
	    	if(result.code==200){
	    		setEcharts(result.data);
	    	}
		}
	});
}

function setEcharts(data){
	var dateDate=[];
	var datePm=[];
	var dateUv=[];
	var dateIp=[];
	$.each(data,function(i,item){
		dateDate.push(item.date);
		datePm.push(item.pm);
		dateUv.push(item.uv);
		dateIp.push(item.ip);
	});
    var myChart = echarts.init(document.getElementById('statischart'));
	option = {
		    title: {
		        text: '网站访问情况'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['浏览量','访客数','独立IP']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: dateDate
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name:'浏览量',
		            type:'line',
		            stack: '总量',
		            data:datePm
		        },
		        {
		            name:'访客数',
		            type:'line',
		            stack: '总量',
		            data:dateUv
		        },
		        {
		            name:'独立IP',
		            type:'line',
		            stack: '总量',
		            data:dateIp
		        }
		    ]
		};
	 myChart.setOption(option);
}

function getStatisData(){
	$.ajax({
	    url:'<%=adminPath%>pvstatistics/getpvdata.do',
	    dataType:"json",   //返回格式为json
	    async: false,  
	    type:"GET",   //请求方式
	    success: function(result){
	    	if(result.code==200){
	    		var html='';
	    		var today= result.data.today;
	    		var yesterday=result.data.yesterday;
	    		var month=result.data.month;
				html+='<tr><td>今日</td><td>'+today.pm+'</td><td>'+today.uv+'</td><td>'+today.ip+"</td></tr>";
		    	html+='<tr><td>昨天</td><td>'+yesterday.pm+'</td><td>'+yesterday.uv+'</td><td>'+yesterday.ip+"</td></tr>";
		    	html+='<tr><td>过去30天</td><td>'+month.pm+'</td><td>'+month.uv+'</td><td>'+month.ip+"</td></tr>";
		    	$("#statisline").html(html);
	    	}
		}
	});

}
function changeSite(siteId)
{
	$.cookie("_site_id_cookie",siteId,{expires:7,path: '/' });
	location.href="<%=adminPath%>index.do?changeSite=1&_site_id_param="+siteId;
}

</script>
</html>