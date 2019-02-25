<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select {
	height: 80px;
	width: 140px;
	overflow-y: auto;
}

.div_select>.label {
	display: block;
}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
$(function(){
	top.hangge();
	/* 根据关键词搜索  */
	$("#ss").click(function(){
		var $id=$("#key").val();
		$id=$.trim($id);
		window.location.href=adminPath+"application/list.do?keyword="+encodeURI(encodeURI($id)); 
	
	});
});
function updateById(data){
	window.location.href=adminPath+"application/toUpdate.do?id="+data; 
}
</script>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <i>合作申请</i>
			</div>
		</div>

		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<div class="a_btn cf">
						<a href="<%=adminPath%>application/excel.do"><img src="static/images/chu.png" alt="">全部导出</a>
					</div>
				</div>
				<div class="selec_co cf">
					<div class="search">
						<input type="text" placeholder="请输入公司名称" id="key"><input
							id="ss" type="button" value="搜索" />
					</div>
				</div>
			</div>
			<div class="table cf">
				<dl class="list_bg col_07 cf" id="did">
					<dt class="cf">
						<div class="tit7">
							<div class="tit7_con">公司名称</div>
						</div>
						<div class="sele07">操作</div>
						<div class="sele07">合作领域</div>
						<div class="sele07">所属行业</div>
						<div class="sele07">联系电话</div>
						<div class="sele07">姓名</div>
						<div class="sele07">事项</div>
					</dt>
					<form id="Form" action="<%=adminPath%>application/list.do" method="post">
						<input type="hidden" name="keywords" id="keywords" />
						<c:if test="${!empty applications }">
							<c:forEach items="${applications }" var="map">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit7">
											<div class="tit7_con">
												<label><input type="checkbox" value="${map.id }" /><i></i></label>
												${map.companyname }
											</div>
										</div>
										<div class="sele07">
											<a onclick="updateById('${map.id }')" href="javascript:void(0)">编辑</a>
										</div>
										<div class="sele07">${map.field}</div>
										<div class="sele07">${map.industry}</div>
										<div class="sele07">${map.contactnumber}</div>
										<div class="sele07">${map.fullname}</div>
										<div class="sele07">${map.item}</div>
									</div>
								</dd>
							</c:forEach>
						</c:if>
						<c:if test="${empty applications }">
							<div
								style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">对不起,没有相关数据</div>
						</c:if>
					</form>
				</dl>
				<div class="bottom_con cf">
					<!-- <div class="all_checkbox" style="width: 250px">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
					</div> -->
					<div class="page_list cf">${page.pageStr}</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">© 中企高呈 版权所有</div>
	<!-- cms_con结束 -->
</body>
</html>