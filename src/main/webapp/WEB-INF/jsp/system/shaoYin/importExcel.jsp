<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<%@ include file="../../system/index/n_top.jsp"%>
<!-- jsp文件头和头部 -->
<style>
.daoru label{
    float: left;
    height: 35px;
    line-height: 1.8;
    width: 50%;
    padding: 5px 0;
    color:#999;
}
</style>
</head>
<body>

	<!-- cms_con开始 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${columId}&topColumId=${topColumId}">数据导入</a>
				<i>></i>
			</div>
		</div>
		<div class="cms_c_list cf">
			<h3>数据导入</h3>

				<div class="add_btn_con wrap cf">  
					<dl class="cf" style="overflow: inherit;">
						<dt>上次导入结果:</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="daoru">
										<label style="float: left;" id="msg1Id"> 
										</label>
									</li>
								</ul>
							</div>
						</dd>
					</dl>
					<dl class="cf" style="overflow: inherit;">
						<dt>模板下载:</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload">
										<label style="float: left;" id="downloadId"> 
											<form method="post" action="<%=adminPath%>excel/download" >
												<input type="submit" value="点击下载">
												<em>点击下载</em>
											</form>
										</label>
										<i style="float: left;">（请使用该模板，且数据长度固定为12）</i> 
									</li>
								</ul>
							</div>
						</dd>
					</dl>
				<form id="myForm">
					<dl class="cf" style="overflow: inherit;">
						<dt>上传文件:</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload">
										<label for="file_name_img" style="float: left;"> 
											<input type="file" id="file_name_img" onchange="fileChange();" name="file"/>
											<em>上传文件</em>
										</label>
										<i style="float: left;" id="msgId">未选择文件</i> 
									</li>
								</ul>
							</div>
						</dd>
					</dl>
				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" id="buttonId" /> 
				</div>

			</form>
		
		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
</body>
<script type="text/javascript">
	//判断是否可以提交
	var flag = 0;
	$(function(){
		//获取上次导入数据结果信息
		getImportExcelMsg();
		
		$("#buttonId").click(function(){
			if(flag == 1){
				submitForm();
			}
		});
		
	});
	
	//获取上次导入数据结果信息
	function getImportExcelMsg(){
		$.ajax({
	        url:"<%=adminPath%>excel/getMsg",
	        type:"get",
	        dataType:"json",
	        success:function(data){
	       	 	  if (data.code == 200 && data.data != null) {
	       		  	$("#msg1Id").html(data.data.msg+"&nbsp;&nbsp;&nbsp;&nbsp;----" + data.data.createTime);
		          }
	       	 	  if(data.data == null){
	       	 		$("#msg1Id").html("尚没有导入记录");
	       	 	  }
	        }
	    });
	}
	
	//表单提交
	function submitForm(){
		var formData = new FormData($( "#myForm" )[0]); 
		$.ajax({
	        url:"<%=adminPath%>excel/read",
	        type:"post",
	        data:formData,
	        dataType:"json",
	        processData:false,
	        contentType:false,
	        success:function(data){
	       	 	  if (data.code == 200) {
	       		  	alert("后台正在导入数据，请稍后刷新页面!")
		          }else {
					alert("文件内容或格式有误！");
		          }
	        }
	    });
	}
	//验证上传文件格式
	function fileChange(obj){
		var name = $("#file_name_img").val().split(".");
		var nameSuffix = name[name.length-1];
		if(nameSuffix != 'xlsx' && nameSuffix != 'xls'){
			$("#msgId").html("上传的文件类型错误！");
			flag = 0;
		}else{
			name = $("#file_name_img").val().split("\\");
			nameSuffix = name[name.length-1];
			$("#msgId").html(nameSuffix);
			flag = 1
		}
		
		
	}	


</script>


</html>
