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
<body>
		<form action="<%=adminPath%>role.do" method="post" name="Form" id="Form">
		<div class="cms_con cf">
<!-- jsp导航返回栏 -->
<div class="cms_c_inst neirong cf">
	<div class="left cf">
		<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
		<i>角色管理</i>
	</div>
</div>			<div class="cms_c_list biaoge_con juese_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>role/toAdd.do" >+创建角色</a>
					</div>
				</div>
				<div class="table cf">
					<dl class="list_bg col_04 cf">
						<dt class="cf">
							<div class="tit4">
								<div class="tit4_con">
									角色名称
								</div>
							</div>
							<div class="sele04">操作</div>
							<div class="sele04">状态</div>
							<div class="sele04 other_width">角色描述</div>
						</dt>
						<c:choose>
							<c:when test="${not empty roleList}">
								<c:forEach items="${roleList }" var="role">
									<dd class="cf">
										<div class="dd_tit cf">
											<div class="tit4">
												<div class="tit4_con">
													${role.ROLE_NAME }
												</div>
											</div>
											<div class="sele04 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<li><a href="<%=adminPath%>role/toAdd.do?ROLE_ID=${role.ROLE_ID}">编辑</a></li>
													<c:choose>
														<c:when test="${role.STATE == 1 }">
															<li><a href="<%=adminPath%>role/changeStatus.do?ROLE_ID=${role.ROLE_ID}&STATE=${role.STATE}">禁用</a></li>
														</c:when>
														<c:otherwise>
															<li><a href="<%=adminPath%>role/changeStatus.do?ROLE_ID=${role.ROLE_ID}&STATE=${role.STATE}">启用</a></li>
														</c:otherwise>
													</c:choose>
													<li><a href="<%=adminPath%>user/listUsers.do?ROLE_ID=${role.ROLE_ID}">成员管理</a></li>
													<li><a href="javascript:void(0);" onclick="delRole('${role.ROLE_ID}');">删除</a></li>
												</ul>
											</div>
											<c:choose>
												<c:when test="${role.STATE == 1 }">
													<div class="sele04">启用</div>
												</c:when>
												<c:otherwise>
													<div class="sele04">禁用</div>
												</c:otherwise>
											</c:choose>
											<div class="sele04 other_width">${role.DESCRIPTION }</div>
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
<script type="text/javascript">
$(function(){
	top.hangge();
});
function delRole(roleId){
	$.ajax({
		type: "GET",
		url:adminPath+"role/delete.do",
		data:{"ROLE_ID":roleId},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				var data=result.data;
				if(data=="1"){
					window.location.href=adminPath+"role.do";
				}else{
					 window.top.mesageTip("warn","该角色已被使用，请先解除该角色与用户的关系!");
				}
			}
		}
	});

}
</script>
</body>
</html>          