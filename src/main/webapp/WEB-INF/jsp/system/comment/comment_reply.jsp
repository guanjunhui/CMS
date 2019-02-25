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
								<form action="<%=adminPath%>comment/saveCommentReply.do" name="userForm" id="userForm" method="post">
									<input type="hidden" name="commentId" id="commentId" value="${pd.id }"/>
									<input type="hidden" name="commentParentId" id="commentParentId" value="${pd.commentParentId }"/>
									<input type="hidden" name="commentPropertyId" id="commentPropertyId" value="${pd.commentPropertyId }"/>
									
									<input type="hidden" name="commentReplyId" id="commentReplyId" value="${pd.commentReplyId }"/>
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
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">回复内容:</td>
											<td id="nr">
												 <textarea name="commentReplyContent" id="commentReplyContent" style="width:98%;height:165px;" rows="7" cols="50" title="" placeholder="在这里回复提问">${pd.commentReplyContent}</textarea>
											</td>
										</tr>
										<c:if test="${not empty pd.commentReplyUserName }">
											<tr>
												<td style="width:90px;text-align: right;padding-top: 13px;">上次回复人:</td>
												<td><input type="text" name="commentReplyUserName" id="commentReplyUserName"  value="${pd.commentReplyUserName }"  maxlength="32" title="${pd.commentReplyUserName }" style="width:98%;" disabled="disabled"/></td>
											</tr>
										</c:if>
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="reply();">回复</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
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
	$(document).ready(function(){
		if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
	});
	//保存
	function reply(){
		if($("#commentReply").val()==""){
			$("#commentReply").tips({
				side:3,
	            msg:'输入回复内容',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#commentReply").focus();
			return false;
		}else{
			$("#commentReply").val($.trim($("#commentReply").val()));
			$("#userForm").submit();
			$("#zhongxin").hide();
		}
	}
	$(function() {
		//下拉框
		if(!ace.vars['touch']) {
			$('.chosen-select').chosen({allow_single_deselect:true}); 
			$(window)
			.off('resize.chosen')
			.on('resize.chosen', function() {
				$('.chosen-select').each(function() {
					 var $this = $(this);
					 $this.next().css({'width': $this.parent().width()});
				});
			}).trigger('resize.chosen');
			$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
				if(event_name != 'sidebar_collapsed') return;
				$('.chosen-select').each(function() {
					 var $this = $(this);
					 $this.next().css({'width': $this.parent().width()});
				});
			});
			$('#chosen-multiple-style .btn').on('click', function(e){
				var target = $(this).find('input[type=radio]');
				var which = parseInt(target.val());
				if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
				 else $('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
</script>
</html>