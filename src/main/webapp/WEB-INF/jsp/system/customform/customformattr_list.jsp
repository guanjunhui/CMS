<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body class="no-skin">
	<!-- jsp导航返回栏 -->
			<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<a href="<%=adminPath%>customform/list.do">表单列表</a><i>></i>
			<i>属性列表</i>
		</div>
	</div>
	<form action="<%=adminPath%>customformattr/list.do?formId=${formId}" method="post" name="Form" id="Form">
		<input type="hidden" id="formId" name="formId" value="${formId}"/>
		<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>customformattr/goEdit.do?formId=${formId}">+添加属性</a>
					</div>
				</div>
			<div class="table cf">
				<dl class="list_bg col_06 cf">
					<dt class="cf">
						<div class="tit6">
							<div class="tit6_con">
								属性名称
							</div>
						</div>
						<div class="sele06">操作</div>
						<div class="sele06">属性类型</div>
						<div class="sele06">排序值</div>
						<div class="sele06 other_width">属性ID</div>
						
					</dt>
					<c:choose>
						<c:when test="${not empty list}">
							<c:forEach items="${list}" var="obj" varStatus="vs">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label for="e_${vs.index}"></label>
												${obj.attrName}
											</div>
										</div>
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a href="javascript:void(0)" onclick="edit('${obj.id}')">编辑</a></li>
												<li><a href="javascript:void(0)" onclick="confirmDiv('${obj.id}','${obj.attrName}')">删除</a></li>
											</ul>
										</div>
										<div class="sele06">
											${obj.attrType}
										</div>
										<div class="sele06">${obj.attrSort}</div>
										<div class="sele06 other_width">${obj.id}</div>
									</div>
								</dd>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>
						</c:otherwise>
					</c:choose>
					
				</dl>
				<div class="bottom_con cf">
					<div class="page_list cf">
						${page.pageStr}
					</div>
				</div>
			</div>
		</div>
		</form>
				 <div class="footer">© 中企高呈 版权所有</div>
			</div> 
</body>

<script type="text/javascript">
$(function(){
	top.hangge();
});
function edit(id){
	var formId=$("#formId").val();
	window.location.href=adminPath+'customformattr/goEdit.do?id='+id+"&formId="+formId;
}
function confirmDiv(id,name){
	var title='确认删除属性【'+name+'】吗？';
	var content='此操作会删除该属性以及此属性下的所有数据！';
	mesageConfirm('删除属性',title,content,"deleteColum('"+id+"')");
}

//删除
function deleteColum(id){
	$.ajax({
		type: "GET",
		url:adminPath+"customformattr/delete.do",
		data:{"id":id},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				 window.top.mesageTip("success","属性删除成功!");
				 var formId=$("#formId").val();
				 location.href="<%=adminPath%>customformattr/list.do?formId="+formId;	 
			}else{
				 window.top.mesageTip("failure","属性删除失败!");
			}
		}
	});
}
</script>
</html>