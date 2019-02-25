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
top.hangge();
$(function(){
	//getData();
});
function getData(){
	var formData=$("#Form").jsonObject();
	$.ajax({
		type: "GET",
		url:adminPath+"productExtendFiledController/getTree.do",
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
			html+='<div class="tit3">';
				html+='<div class="tit3_con xiala_btn">';
					for(var m=0;m<num_;m++){
						html+='<span class="zhanW2"></span>';
					}	
					
					html+=obj.NAME;
				html+='</div>';
			html+='</div>';
			html+='<div class="sele03 sanJi">';
				html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
				html+='<ul class="guanli_con cf">';
					html+='<li><a href="javascript:void(0)" onclick="updateById(\''+obj.ID+'\')">编辑</a></li>';
					html+='<li><a onclick="deleteById(\''+obj.ID+'\')" href="javascript:;">删除</a></li>';
				html+='</ul>';
			html+='</div>';
			html+='<div class="sele03">';
				html+='<input type="text"  onblur="typeSort(this);"  sort="'+obj.ID+'"  value="'+obj.SORT+'" class="nub" style="width:40px;" />';
			html+='</div>';
		html+='</div>';
		
	html+='</dd>';
	num_=num;
	});
	return html;
}
function batchDel(param){
	hideConfirm();
	$.ajax({
		type: "GET",
		url:adminPath+"productExtendFiledController/delete.do",
		data:param,
		dataType:'json',
		cache: false,
		success:function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
				 location.href=adminPath+'productExtendFiledController/list.do';
				 //getData();
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
	window.location.href=adminPath+"productExtendFiledController/toUpdate.do?ID="+data; 
}

function typeSort(param){
	var zz=/^[1-9][0-9]*$/;
	var sort=$(param).val();
	if(zz.test(sort)){
		$.ajax({
			type: "GET",
			url:adminPath+"productExtendFiledController/updateSort.do",
			data:{id:$(param).attr("sort"),sort:$(param).val()},
			success:function(result){
				if(result.success){
				}else{
					window.top.mesageTip("failure","操作失败");
				}
			}
		});
	}else{
		window.top.mesageTip("warn","请输入正整数");
	}
	
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
		<form id="Form" action="<%=adminPath%>productExtendFiledController/list.do" method="post">
			<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>productExtendFiledController/toAdd.do">+添加属性</a>
					</div>
				</div>
				<div class="table cf">
					<dl class="list_bg col_03 cf" id="did">
						<dt class="cf">
							<div class="tit3">
								<div class="tit3_con">属性名称</div>
							</div>
							<div class="sele03">操作</div>
							<div class="sele03">排序</div>
							
						</dt>
						<div id="listPage">
							<c:forEach items="${tree }" var="filed" varStatus="num">
							<dd class="cf">
				               <div class="dd_tit cf">
					             <div class="tit3">
						            <div class="tit3_con xiala_btn">
							            ${filed.NAME }
						            </div>
					             </div>
					             <div class="sele03 sanJi">
							        <a href="javascript:void(0);">管理<span class="sanjiao"></span></a>
									<ul class="guanli_con cf">
										<li><a href="javascript:void(0)" onclick="updateById('${filed.ID}')">编辑</a></li>
										<li><a onclick="deleteById('${filed.ID}')" href="javascript:;">删除</a></li>
									</ul>
								 </div>
								
								 <div class="sele03">
									<input type="text"  onblur="typeSort(this);"  sort="${filed.ID}"  value="${filed.SORT}"class="nub" style="width:30px;" />
								 </div>
							 </div>
						</dd>
						</c:forEach>
					</div>
				</dl>
			</div>	
		</form>
		<div class="bottom_con cf">
					
					<div class="page_list cf">${page.pageStr}</div>
				</div>
		<div class="footer">© 中企高呈 版权所有</div>
		<!-- cms_con结束 -->
</body>
</html>