<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = "//"+request.getServerName()+path+"/";
	String adminPath = (String)application.getAttribute("adminPath");
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<style type="text/css">
	.yulantu{
		z-index: 9999999999999999;
		position:absolute;
		border:2px solid #76ACCD;
		display: none;
	}
	</style>
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
					
					<form action="<%=adminPath%>survey/save.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID"/>
						<input type="hidden" name="TYPE" id="TYPE" />
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:95px;text-align: right;padding-top: 13px;">调查名称:</td>
								<td><input type="text" name="SURVEY_NAME" id="SURVEY_NAME" maxlength="100" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td>
									<input type="radio" name="STATUS" value="1" checked/>启用
									<input type="radio" name="STATUS" value="0" />关闭
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
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
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


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存验证
		function save(){
			if($("#SURVEY_NAME").val()==""){
				$("#SURVEY_NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SURVEY_NAME").focus();
			return false;
			}
			/* if($("#IMGURL").val()==""){
				$("#IMGURL").tips({
					side:3,
		            msg:'请输入路径',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGURL").focus();
			return false;
			}
			if($("#BANNER_TYPEID").val()==""){
				$("#BANNER_TYPEID").tips({
					side:3,
		            msg:'请输入类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BANNER_TYPEID").focus();
			return false;
			} */
			$("#Form").submit();
			/* $("#zhongxin").hide();
			$("#zhongxin2").show(); */
		}
		</script>
</body>
</html>