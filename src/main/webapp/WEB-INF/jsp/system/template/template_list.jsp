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
	String imgPath = path+"/../uploadFiles/uploadImgs/";
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
			<i>模板列表</i>
		</div>
	</div>
	<form action="<%=adminPath%>template/list.do" method="post">
				<div class="cms_c_list biaoge_con chanpin_con cf">
						<div class="h3 cf">
							<div class="h3_left cf">
								<a href="<%=adminPath%>template/goEdit.do">+添加模板</a>
								<div class="selec_co cf">
									<select id="templateType" name="TEM_TYPE" class="form-control">
										<option  value="">请选择模板类型</option>
										<c:choose>
											<c:when test="${pd.TEM_TYPE=='1'}">
												<option selected="selected" value="${pd.TEM_TYPE}">内容模板</option>
												<option value="6">首页模板</option>
												<option value="2">资讯模板</option>
												<option value="3">产品模板</option>
												<option value="4">招聘模板</option>
												<option value="5">下载模板</option>
											</c:when>
											<c:when test="${pd.TEM_TYPE=='2'}">
												<option selected="selected" value="${pd.TEM_TYPE}">资讯模板</option>
												<option value="6">首页模板</option>
												<option value="1">内容模板</option>
												<option value="3">产品模板</option>
												<option value="4">招聘模板</option>
												<option value="5">下载模板</option>
											</c:when>
											<c:when test="${pd.TEM_TYPE=='3'}">
												<option selected="selected" value="${pd.TEM_TYPE}">产品模板</option>
												<option value="6">首页模板</option>
												<option value="1">内容模板</option>
												<option value="2">资讯模板</option>
												<option value="4">招聘模板</option>
												<option value="5">下载模板</option>
											</c:when>
											<c:when test="${pd.TEM_TYPE=='4'}">
												<option selected="selected" value="${pd.TEM_TYPE}">招聘模板</option>
												<option value="6">首页模板</option>
												<option value="1">内容模板</option>
												<option value="2">资讯模板</option>
												<option value="3">产品模板</option>
												<option value="5">下载模板</option>
											</c:when>
											<c:when test="${pd.TEM_TYPE=='5'}">
												<option selected="selected" value="${pd.TEM_TYPE}">下载模板</option>
												<option value="6">首页模板</option>
												<option value="1">内容模板</option>
												<option value="2">资讯模板</option>
												<option value="3">产品模板</option>
												<option value="4">招聘模板</option>
											</c:when>
											<c:when test="${pd.TEM_TYPE=='6'}">
												<option selected="selected" value="${pd.TEM_TYPE}">首页模板</option>
												<option value="1">内容模板</option>
												<option value="2">资讯模板</option>
												<option value="3">产品模板</option>
												<option value="4">招聘模板</option>
												<option value="5">下载模板</option>
											</c:when>
											<c:otherwise>
												<option value="6">首页模板</option>
												<option value="1">内容模板</option>
												<option value="2">资讯模板</option>
												<option value="3">产品模板</option>
												<option value="4">招聘模板</option>
												<option value="5">下载模板</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
							<div class="search">
								<input type="text" id="TEM_NAME" placeholder="请输入模板名称" value="" ><input type="button" onclick="search();" value="搜索"/>
							</div>
						</div>
					<div class="table cf">

						<dl class="list_bg col_06 cf">
							<dt class="cf">
								<div class="tit6">
									<div class="tit6_con">
										<div class="pro_img">缩略图</div>
										模板名称
									</div>
								</div>
								<div class="sele06">操作</div>
								<div class="sele06 other_width">模板目录</div>
								<div class="sele06">模板类型</div>
							</dt>
							<c:choose>
								<c:when test="${not empty list}">
									<c:forEach items="${list}" var="obj" varStatus="vs">
									<dd class="cf">
										<div class="dd_tit cf">
											<div class="tit6">
												<div class="tit6_con">
													<label for="e_${vs.index}"><input type="checkbox" id="e_${vs.index}"/><i></i></label>
													<div class="pro_img"><div class="pro_img_big"><img src="<%=imgPath%>${obj.TEM_IMAGEPATH}" alt=""></div></div>
													${obj.TEM_NAME}
												</div>
											</div>
											<div class="sele06 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<li><a href="javascript:void(0)" onclick="edit('${obj.ID}')">编辑</a></li>
												</ul>
											</div>
											<div class="sele06 other_width">${obj.TEM_FILEPATH}</div>
											<div class="sele06">
												<c:choose>
													<c:when test="${obj.TEM_TYPE=='1'}">
														内容模板
													</c:when>
													<c:when test="${obj.TEM_TYPE=='2'}">
														资讯模板
													</c:when>
													<c:when test="${obj.TEM_TYPE=='3'}">
														产品模板
													</c:when>
													<c:when test="${obj.TEM_TYPE=='4'}">
														招聘模板
													</c:when>
													<c:when test="${obj.TEM_TYPE=='5'}">
														下载模板
													</c:when>
													<c:when test="${obj.TEM_TYPE=='6'}">
														首页模板
													</c:when>
													<c:otherwise>
														未分类
													</c:otherwise>
												</c:choose>
											</div>
										</div>
									</dd>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>
								</c:otherwise>
							</c:choose>
							
						</dl>
						<div class="bottom_con cf">
							<div class="page_list cf">
								${page.pageStr}						
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
	top.hangge();
	$('#templateType').change(function(){
		//var type=$(this).children('option:selected').val();
		var type=$("#templateType").val();
		window.location.href=adminPath+"template/list.do?TEM_TYPE="+type; 
	})
	$("#templateType").val('${pd.TEM_TYPE}');
});
function search(){
	var TEM_NAME=$("#TEM_NAME").val();
	TEM_NAME = $.trim(TEM_NAME);
	window.location.href=adminPath+"template/list.do?TEM_NAME="+TEM_NAME; 
}
function edit(id){
	window.location.href=adminPath+"template/goEdit.do?ID="+id;
}
function del(id){
	window.location.href=adminPath+"template/delete.do?ID="+id;
}
</script>
</html>