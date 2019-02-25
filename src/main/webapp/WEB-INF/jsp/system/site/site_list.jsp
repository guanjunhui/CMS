<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = "//"
			+ request.getServerName()
			+ path + "/";
	String adminPath = (String)application.getAttribute("adminPath");
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body class="no-skin">
	<div class="cms_con cf">
	<!-- jsp导航返回栏 -->
			<div class="cms_con cf">
			<div class="cms_c_inst neirong cf">
				<div class="left cf">
					<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
					<i>站点列表</i>
				</div>
			</div>
			<form action="<%=adminPath%>site/sitelistPage.do" method="post">
				<div class="cms_c_list biaoge_con zhandian_con cf">
					<div class="h3 cf">
						<div class="h3_left cf">
								<a href="<%=adminPath%>site/goAddSite.do">+添加站点</a>								
						</div>
					</div>
					<div class="table cf">
						<dl class="list_bg col_05 cf">
							<dt class="cf">
								<div class="tit5">
									<div class="tit5_con">
										<span>ID</span>
										域名
									</div>
								</div>
								<div class="sele05">操作</div>
								<div class="sele05">状态</div>
								<div class="sele05">创建时间</div>
								<div class="sele05 other_width">站点名称</div>
							</dt>
							<c:choose>
								<c:when test="${not empty list}">
									<c:forEach items="${list}" var="obj" varStatus="vs">
										<dd class="cf">
											<div class="dd_tit cf">
												<div class="tit5">
													<div class="tit5_con">
														<span>${vs.index+1}</span>
														${obj.siteDomain }
													</div>
												</div>
												<div class="sele05 sanJi">
													<a href="javascript:;">管理<span class="sanjiao"></span></a>
													<ul class="guanli_con cf">
														<li><a href="<%=adminPath%>site/goAddSite.do?siteId=${obj.siteId}">编辑</a></li>
														<li><a href="javascript:void(0);" onclick="confirmDiv('${obj.siteId}','${obj.siteStatus}','${obj.siteName}')">
															<c:if test="${obj.siteStatus==1 }">
																暂停访问
															</c:if>
															<c:if test="${obj.siteStatus==0 }">
																启动访问
															</c:if>
														</a></li>
													</ul>
												</div>
												<div class="sele05">
													<c:if test="${obj.siteStatus==1 }">
														正常访问
													</c:if>
													<c:if test="${obj.siteStatus==0 }">
														暂停访问
													</c:if>
												</div>
												<div class="sele05"><fmt:formatDate value="${obj.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
												<div class="sele05 other_width">${obj.siteName}</div>
											</div>
										</dd>
									</c:forEach>
								</c:when>
							</c:choose>
						</dl>
						<div class="bottom_con cf">
							<div class="page_list cf">
								${page.pageStr}						
							</div>
						</div>
					</div>
					</div>
				</div>

				</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>

<script type="text/javascript">
$(function(){
	$('#templateType').change(function(){
		var type=$(this).children('option:selected').val();
		window.location.href=adminPath+"template/list.do?TEM_TYPE="+type; 
	})
	$("#templateType").val('${pd.TEM_TYPE}');
});
function search(){
	var TEM_NAME=$("#TEM_NAME").val();
	window.location.href=adminPath+"template/list.do?TEM_NAME="+TEM_NAME; 
}
function confirmDiv(id,status,name){
	var title='';
	var content='';
	if(status=='1'){
		title='确认暂停站点【'+name+'】访问吗？';
		content='此操作会中断站点【'+name+'】的访问';
	}else{
		title='确认启用站点【'+name+'】访问吗？';
		content='此操作会重新启用站点【'+name+'】的访问';
	}
	mesageConfirm('切换访问状态',title,content,"changestatus('"+id+"','"+status+"')");
}
function changestatus(id,status){
	hideConfirm();
	$.ajax({
		type: "GET",
		url: adminPath+"site/changeStatus.do",
		dataType:'json',
		data:{"siteId":id,"status":status},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","状态改变成功!");
				 setTimeout("location.href='<%=adminPath%>site/sitelistPage.do'",1000);
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	});
}
</script>
</html>