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
					
					<form action="<%=adminPath%>content/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${content.id}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:95px;text-align: right;padding-top: 13px;">内容标题:</td>
								<td><input type="text" name="BANNER_NAME" id="BANNER_NAME" value="${content.contentTitle}" maxlength="100" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">编辑图片</td>
								<td><%-- <input type="text" name="IMGURL" id="IMGURL" value="${pd.IMGURL}" maxlength="255" placeholder="这里输入路径" title="路径" style="width:85%;" onmouseover="showTU('IMGURL','yulantu1');" onmouseout="hideTU('yulantu1');"/>
									<div class="yulantu" id="yulantu1"></div> --%>
									<a class="btn btn-xs btn-info" style="margin-top: -5px;" title="选择" onclick="xuanTp('${content.id}');">选择 </a>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片/视频:</td>
								<td>
									<a class="btn btn-sm btn-success" style="margin-top: -5px;" title="选择" onclick="toAction();">上传 </a>
								</td>
							</tr>
							<!-- <tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr> -->
							<div class="page-header position-relative">
								<table style="width:100%;">
									<tr>
										<td style="vertical-align:top;">
											<a class="btn btn-mini btn-success" onclick="goback('${pd.keywords }','${pd.startTime}','${pd.endTime}','${pd.status}')">返回</a>
										</td>
									</tr>
								</table>
							</div>
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
			if($("#BANNER_NAME").val()==""){
				$("#BANNER_NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BANNER_NAME").focus();
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
		
		//选择图片
		function xuanTp(contentId){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="选择图片";
			 diag.URL = '<%=adminPath%>contentImage/contentImageList.do?contentId='+contentId+'&keyword='+'';
			 diag.Width = 860;
			 diag.Height = 680;
			 diag.CancelEvent = function(){ //关闭事件
				<%--  $("#"+ID).val('<%=adminPath%>'+diag.innerFrame.contentWindow.document.getElementById('xzvalue').value); --%>
				 diag.close();
			 };
			 diag.show();
		}
		
		//SC(上传)
		function toAction(){
			top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=adminPath%>contentImage/goAdd.do?contentId=${content.id}';
			 diag.Width = 800;
			 diag.Height = 490;
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
		
		//显示图片
		function showTU(ID,TPID){
			 $("#"+TPID).html('<img width="200" src="'+$("#"+ID).val()+'">');
			 $("#"+TPID).show();
		}
		
		//隐藏图片
		function hideTU(TPID){
			 $("#"+TPID).hide();
		}
		//返回
	 	function goback(keywords,startTime,endTime,status){
		 	window.location.href='<%=adminPath%>content/contentlistPage.do?keywords='+keywords+'&startTime='+startTime+'&endTime='+endTime+'&status='+status;
		}
		</script>
</body>
</html>