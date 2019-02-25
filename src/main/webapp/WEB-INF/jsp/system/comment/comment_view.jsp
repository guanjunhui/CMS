<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
								<form action="<%=adminPath%>user/${msg }.do" name="userForm" id="userForm" method="post">
									<input type="hidden" name="USER_ID" id="user_id" value="${pd.USER_ID }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">用户名:</td>
											<c:if test="${not empty pd.memNick }">
												<td><input type="text" name="memNick" id="memNick"  value="${pd.memNick }"  maxlength="32" title="用户名" style="width:98%;" disabled="disabled"/></td>
											</c:if>
											<c:if test="${empty pd.memNick }">
												<c:if test="${not empty pd.userName }">
													<td><input type="text" name="userName" id="userName"  value="${pd.userName }"  maxlength="32" title="用户名" style="width:98%;" disabled="disabled"/></td>
												</c:if>
											</c:if>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">类型:</td>
											<c:if test="${pd.commentType == 0 }">
												<td><input type="text" name="commentType" id="commentType"  value="评论"  maxlength="32" title="评论" style="width:98%;" disabled="disabled"/></td>
											</c:if>
											<c:if test="${pd.commentType == 1 }">
												<td><input type="text" name="commentType" id="commentType"  value="提问"  maxlength="32" title="提问" style="width:98%;" disabled="disabled"/></td>
											</c:if>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">客户端类型:</td>
											<c:if test="${pd.clientType == 0 }">
												<td><input type="text" name="clientType" id="clientType"  value="手机端"  maxlength="32" title="手机端" style="width:98%;" disabled="disabled"/></td>
											</c:if>
											<c:if test="${pd.clientType == 1 }">
												<td><input type="text" name="clientType" id="clientType"  value="PC端"  maxlength="32" title="PC端" style="width:98%;" disabled="disabled"/></td>
											</c:if>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">学校/留学机构</td>
											<td>
												<c:if test="${not empty pd.contentTitle }">
													<input type="text" name="contentTitle" id="contentTitle"  value="${pd.contentTitle }"  maxlength="32" title="${pd.contentTitle }" style="width:98%;" disabled="disabled"/>
												</c:if>
												<c:if test="${not empty pd.scNameCN }">
													<input type="text" name="scNameCN" id="scNameCN"  value="${pd.scNameCN }"  maxlength="32" title="${pd.scNameCN }" style="width:98%;" disabled="disabled"/>
												</c:if>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">评分:</td>
											<td><input type="text" name="commentScore" id="commentScore"  value="${pd.commentScore }"  maxlength="32" title="评分" style="width:98%;" disabled="disabled"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">分类:</td>
											<td><input type="text" name="typeName" id="typeName"  value="${pd.typeName }"  maxlength="32" title="分类" style="width:98%;" disabled="disabled"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">内容:</td>
											<td><textarea name="commentContent" id="commentContent" disabled="disabled" style="width:98%;height:165px;" rows="7" cols="50" title="">${pd.commentContent }</textarea></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">邮箱:</td>
											<td><input type="email" name="email" id="email"  value="${pd.email }"  maxlength="32" title="邮箱" style="width:98%;" disabled="disabled"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">状态:</td>
											<c:if test="${pd.status == 0 }">
												<td><input type="text" name="status" id="status"  value="开启"  maxlength="32" title="开启" style="width:98%;" disabled="disabled"/></td>
											</c:if>
											<c:if test="${pd.status == 1 }">
												<td><input type="text" name="status" id="status"  value="关闭"  maxlength="32" title="关闭" style="width:98%;" disabled="disabled"/></td>
											</c:if>
										</tr>
									</table>
									</div>
								</form>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
	$(top.hangge());
</script>
</html>