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
	String imgPath = "/uploadFiles/uploadImgs/";
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
.nub {
	border: 1px solid #747474;
	width: 35px !important;
	height: 20px !important;
	line-height: 20px !important;
	text-align: center;
	text-indent: 0 !important;
}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function(){
		top.hangge();
		/* 搜索  */
		$("#ss").click(function(){
			var $key=$("#key").val();
			$("Form").submit(); 
		});
		/* 删除 */
		$("#sc").click(function(){
			var $ids=$("#did input[type='checkbox']:checked");
			if($ids.length==0){
				window.top.mesageTip("warn","请选择删除的选项");
				return false;
			}
			var ids=[];
			var param="ids=";
			$.each($ids,function(index,obj){
				ids.push(obj.value);
			});
			param+=ids.join("&ids=");
			var	title='确认批量删除所选信息吗？';
			var	content='此操作会删除该信息';
			mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
			
		});
		$("#exprot").click(function(){
			var $ids=$("#did input[type='checkbox']:checked");
			if($ids.length==0){
				window.top.mesageTip("warn","请选择导出的选项");
				return false;
			}
			var ids=[];
			var param="ids=";
			$.each($ids,function(index,obj){
				ids.push(obj.value);
			});
			param+=ids.join("&ids=");
			window.location.href=adminPath+"join/exprot.do?"+param; 
		})
		
	});

function batchDel(param){
	hideConfirm();
	window.location.href=adminPath+"join/delete.do?"+param;
}
function deleteById(data){
	var param="ids="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
	
	
	function updateById(data){
		if(data==0){
			return false;
		}
		window.location.href=adminPath+"join/toUpdate.do?id="+data; 
	}
	//导入
	function improt(){
		var formData=new FormData();
		formData.append("file",$("#file_name_xlsx")[0].files[0]);
		 $.ajax({
			type: "POST",
			url:adminPath+"join/improt.do",
			data:formData,
			processData:false,
	        contentType:false,
			success: function(result){
				 if(result.success){
						 window.top.mesageTip("success","上传成功");
						 window.location.href=adminPath+"product/list.do";
				 }else{
					 window.top.mesageTip("failure","上传失败");
				 }
			}
		});
	}	
</script>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <i>加盟商列表</i>
			</div>
		</div>
		
		<form id="Form" action="<%=adminPath%>join/list.do" method="post">
			<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>join/toAdd.do">+添加加盟商</a>
						
					</div>
					<div class="a_btn cf">
							<a href="<%=adminPath%>join/exprotTemplate.do"><img
								src="static/images/chu.png" alt="">获取导入模板</a>
							<p href="javascript:;" class="file_upload_xlsx">
								<img src="static/images/ru.png" alt=""> <label
									for="file_name_xlsx"><input type="file" name="file"
									id="file_name_xlsx"><em>导入</em></label><i></i><a
									href="javascript:;" onclick="improt()" class="upl_file"
									style="display: none;">确定上传</a><a href="javascript:;"
									class="remove_file" style="display: none;">取消上传</a>
							</p>
							<a href="javascript:void(0)" id="exprot"><img
								src="static/images/chu.png" alt="">导出</a>
						</div>
					<div class="search">
						<input type="text" name="myName" placeholder="请输入门店名称"
							id="key"><input id="ss" type="button" value="搜索" />
					</div>
				</div>
				<script type="text/javascript">
								$(function(){
									$(document).on("change",".file_upload_xlsx input[type='file']",function(){
								    var filePath=$(this).val();
								    if(filePath.indexOf("xlsx")!=-1||filePath.indexOf("xls")!=-1){
								        var arr=filePath.split('\\');
								        var fileName=arr[arr.length-1];
								        $(this).parents('.file_upload_xlsx').find('i').html('');
								        $(this).parents('.file_upload_xlsx').find('.upl_file').show();
								        $(this).parents('.file_upload_xlsx').find('.remove_file').show();
								    }else{
								        $(this).parents('.file_upload_xlsx').find('i').html("您上传文件类型有误！");
								        $(this).parents('.file_upload_xlsx').find('.upl_file').hide();
								        $(this).parents('.file_upload_xlsx').find('.remove_file').hide();
								        return false 
								    }
									})
									$(document).on('click','.remove_file',function(){
										var file = $(this).parent().find("input[type='file']");
										file.after(file.clone().val(''));      
										file.remove(); 
										$(this).parent().find('i').html('未选择文件');
										$(this).hide();
									});
								})
							</script>
				<style>
.biaoge_con>div.h3 .h3_left .a_btn p {
	display: inline-block;
	line-height: 34px;
	margin-left: 10px;
	color: #999;
}

.biaoge_con>div.h3 .h3_left .a_btn p img {
	margin: 8px 5px;
}

.file_upload_xlsx label {
	display: inline-block;
	position: relative;
	border-radius: 3px;
	cursor: pointer;
	width: 30px;
}

.file_upload_xlsx label em {
	position: absolute;
	top: 50%;
	left: 0%;
	transform: translate(0%, -50%);
	color: #999;
	text-align: center;
	right: 0;
}

.file_upload_xlsx input {
	visibility: hidden;
}

.file_upload_xlsx p {
	display: inline-block;
	margin-left: 20px;
	color: #2da1f8;
}

.file_upload_xlsx a {
	display: inline-block;
	margin-left: 20px;
	color: #2da1f8;
}

.biaoge_con>div.h3 .h3_left .a_btn .file_upload_xlsx a {
	color: #2da1f8;
}

.file_upload_xlsx i {
	color: #999;
	padding-left: 5px;
}
</style>
				<div class="table cf c07">
					<dl class="list_bg col_07 cf" id="did">
						<dt class="cf">
							<div class="tit7">
								<div class="tit7_con">
									门店名称
								</div>
							</div>
							<div class="sele07">操作</div>
							<div class="sele07">品牌</div>
							<div class="sele07 other_width">发布时间</div>
							<div class="sele07">市</div>
							<div class="sele07">省</div>
							<div class="sele07">区域</div>
						</dt>

						<c:if test="${!empty listPage }">
							<c:forEach items="${listPage }" var="item" varStatus="mun">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit7">
											<div class="tit7_con">
												<label><input type="checkbox" value="${item.id }" /><i></i></label>
												${item.myName }
											</div>
										</div>
										<div class="sele07 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a onclick="updateById('${item.id }')"
													href="javascript:void(0)">编辑</a></li>
												<li><a onclick="deleteById('${item.id }')"
													href="javascript:;">删除</a></li>
											</ul>
										</div>
											<div class="sele07">${item.brand }</div>



										<div class="sele07 other_width">
												${item.createTime }
										</div>
										
										    <div class="sele07">${item.city }</div>
										
										<div class="sele07">
											${item.province }
										</div>
										<div class="sele07">${item.area }</div>
									</div>
								</dd>
							</c:forEach>
						</c:if>
					</dl>
		</form>
		<div class="bottom_con cf">
			<div class="all_checkbox">
				<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
				<a id="sc" href="javascript:;">删除</a>
			</div>
			<div class="page_list cf">${page.pageStr}</div>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
		<!-- cms_con结束 -->
</body>
</html>