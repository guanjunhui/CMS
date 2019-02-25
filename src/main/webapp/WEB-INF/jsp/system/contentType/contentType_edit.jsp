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
	String imgPath = "/uploadFiles/uploadImgs/";
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
								<form action="<%=adminPath%>contentType/${msg }.do" name="contentTypeForm" id="contentTypeForm" method="post">
									<input type="hidden" name="typeId" id="typeId" value="${pd.typeId }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">类型名称:</td>
											<td id="typeNametip" colspan="3"><input type="text" name="typeName" id="typeName" value="${pd.typeName }" maxlength="32" placeholder="这里输入类型名称" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">类型:</td>
											<td>
												<select id="typeCode" name="typeCode" >
													<option id="typeCode" value="STUORG" <c:if test="${pd.typeCode=='STUORG' }">selected="selected"</c:if>>留学机构</option>
													<option id="typeCode" value="STUINF" <c:if test="${pd.typeCode=='STUINF' }">selected="selected"</c:if>>留学资讯</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">模板:</td>
											<td>
												<select id="typeTemplateId" name="typeTemplateId" style="width:200px;overflow-y:auto;">
													<c:forEach items="${templateList}" var="template">
														<option id="${template.ID}" value="${template.ID}" style="width:98%;" <c:if test="${pd.columTemplatetId==template.ID}">selected</c:if>>${template.TEM_NAME}</option>
													</c:forEach>
												</select>
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">类型状态:</td>
											<td>
												<select id="typeStates" name="typeStates">
													<option id="typeStates" value="0" style="width:98%;" <c:if test="${pd.typeStates==0}">selected</c:if>>启用</option>
													<option id="typeStates" value="1" style="width:98%;" <c:if test="${pd.typeStates==1}">selected</c:if>>暂停</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
										</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	function save(){
		if($("#typeName").val()==""){
			$("#typeNametip").tips({
				side:3,
	            msg:'请填写机构/资讯标题',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#typeName").focus();
			return false;
		}
		
		$("#contentTypeForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
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