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

<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	/* $(function(){
		//弹框取消按钮
		$(document).on('click','.submit_re_btn',function(){
		    $('.layer_list li label').removeClass('radio_btn');
		  	$('.layer_bg').find('input').removeAttr("checked");
		  	$('.layer_bg').find('i').removeClass('active');
		  	$(this).parents('.layer_bg').hide();
		  });
	}); */
	function cancelImport(){
		window.location.href=adminPath+"mymessage/list.do";
	}
</script>
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
							<form action="<%=adminPath%>mymessage/readExcel.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
								<div id="zhongxin">
								<div style="width:95%;" >
									<div>
										<span style="padding-top: 20px;"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></span>
									</div>
									<div class="col_btn">
										<span style="text-align: center;padding-top: 10px;">
											<a class="submit_btn"  onclick="save();">导入</a>
											<a class="submit_re_btn"  onclick="cancelImport();">取消</a>
											<a class="submit_btn"  onclick="window.location.href='<%=adminPath%>/mymessage/downExcel.do'">下载模版</a>
										</span>
									</div>
								</div>
								</div>
								<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
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
<style>
.col-xs-12 .ace-file-input .ace-file-container:before{background:#ff9f20;}
.col-xs-12 .ace-file-input{width:50%;margin:50px auto;}
.col-xs-12 .col_btn{width:290px;margin:0 auto;}
</style>
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());
		$(function() {
			//上传
			$('#excel').ace_file_input({
				no_file:'请选择EXCEL ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xls',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
			});
		});
		
		//保存
		function save(){
			if($("#excel").val()=="" || document.getElementById("excel").files[0] =='请选择xls格式的文件'){
				
				$("#excel").tips({
					side:3,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.xls'){
		    	$("#excel").tips({
					side:3,
		            msg:'请上传xls格式的文件',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#excel").val('');
		    	document.getElementById("excel").files[0] = '请选择xls格式的文件';
		    }
		}
	</script>


</body>
</html>