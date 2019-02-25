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
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
$(function(){
	getData();
});
function getData(){
	var formData=$("#Form").jsonObject();
	$.ajax({
		type: "GET",
		url:adminPath+"property/getTree.do",
		data:formData,
		dataType:'json',
		cache: false,
		success:function(result){
			 var html='';
				 var data=result.tree;
					 html=apeendBody(data,'',0);
			 $('#listPage').html(html);
			//间隔行色变化
			 $('.biaoge_con .table .dd_tit').each(function(index,el){
				 if(index%2==0){
				 	$(this).addClass('other_bg');
				 }
			 });
		}
	});
}

function apeendBody(list,html,num){
	var num_=num;
	var html='';
	$.each(list,function(i,obj){
		html+='<dd class="cf">';
		html+='<div class="dd_tit cf">';
			html+='<div class="tit2">';
				html+='<div class="tit2_con xiala_btn">';
					for(var m=0;m<num_;m++){
						html+='<span class="zhanW2"></span>';
					}	
					if(obj.childList.length != 0){
						html+='<span class="san_ico"></span>';
					}
					html+=obj.name;
				html+='</div>';
			html+='</div>';
			html+='<div class="sele02 sanJi">';
				html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
				html+='<ul class="guanli_con cf">';
					html+='<li><a href="javascript:void(0)" onclick="updateById(\''+obj.id+'\')">编辑</a></li>';
					html+='<li><a onclick="deleteById(\''+obj.id+'\')" href="javascript:;">删除</a></li>';
				html+='</ul>';
			html+='</div>';
		html+='</div>';
		if(obj.childList.length!=0){
			html+='<dl>';
			num_++;
			html+=apeendBody(obj.childList,'',num_);
			html+='</dl>';
		}
	html+='</dd>';
	num_=num;
	});
	return html;
}
function batchDel(param){
	hideConfirm();
	$.ajax({
		type: "GET",
		url:adminPath+"property/delete.do",
		data:param,
		dataType:'json',
		cache: false,
		success:function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
				 getData();
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
	
function updateById(data){
	if(data==0){
		return false;
	}
	window.location.href=adminPath+"property/toUpdate.do?id="+data; 
}

</script>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <i>属性列表</i>
			</div>
		</div>
		<form id="Form" action="<%=adminPath%>property/list.do" method="post">
			<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>property/toAdd.do">+添加属性</a>
					</div>
				</div>
				<div class="table cf">
					<dl class="list_bg col_02 cf" id="did">
						<dt class="cf">
							<div class="tit2">
								<div class="tit2_con">属性类型名称</div>
							</div>
							<div class="sele02">操作</div>
						</dt>
						<div id="listPage"></div>
					</dl>
		</form>
		<div class="footer">© 中企高呈 版权所有</div>
		<!-- cms_con结束 -->
</body>
</html>