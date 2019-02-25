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
			<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
			<i>表单列表</i>
		</div>
	</div>
	<form action="<%=adminPath%>customform/formlist.do" method="post" name="Form" id="Form">
		<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="javascript:void(0)" onclick="add('');">+添加表单</a>
					</div>
				</div>
			<div class="table cf">
				<dl class="list_bg col_06 cf">
					<dt class="cf">
						<div class="tit6">
							<div class="tit6_con">
								表单名称
							</div>
						</div>
						<div class="sele06">操作</div>
						<div class="sele06">包含表单项</div>
						<div class="sele06 other_width">添加时间</div>
						
					</dt>
					<c:choose>
						<c:when test="${not empty list}">
							<c:forEach items="${list}" var="obj" varStatus="vs">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label for="e_${vs.index}"></label>
												${obj.formName}
											</div>
										</div>
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a href="javascript:void(0)" onclick="edit('${obj.id}')">编辑</a></li>
												<li><a href="<%=adminPath%>customformattr/list.do?formId=${obj.id}">维护</a></li>
												<li><a href="<%=adminPath%>customformattr/goEdit.do?formId=${obj.id}">添加</a></li>
												<li><a href="javascript:void(0)" onclick="confirmDiv('${obj.id}')">删除</a></li>
											</ul>
										</div>
										<div class="sele06">${empty obj.attributeList?"0":(fn:length(obj.attributeList))}</div>
										<div class="sele06 other_width"><fmt:formatDate value="${obj.createdTime}" pattern="yyyy-MM-dd"/></div>
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
	 window.top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="编辑表单信息";
	 diag.URL = '<%=adminPath%>customform/goEdit.do?id='+id;
	 diag.Width = 500;
	 diag.Height = 260;
	 diag.Modal = true;				//有无遮罩窗口
	 diag. ShowMaxButton = true;	//最大化按钮
     diag.ShowMinButton = true;		//最小化按钮 
	 diag.CancelEvent = function(){ //关闭事件
		location.href="<%=adminPath%>customform/list.do";
		diag.close();
	 };
	 diag.show();
	 window.top.hangge();
}
function add(id){
	 window.top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="添加表单信息";
	 diag.URL = '<%=adminPath%>customform/goEdit.do?id='+id;
	 diag.Width = 500;
	 diag.Height = 260;
	 diag.Modal = true;				//有无遮罩窗口
	 diag. ShowMaxButton = true;	//最大化按钮
     diag.ShowMinButton = true;		//最小化按钮 
	 diag.CancelEvent = function(){ //关闭事件
			location.href=location.href;
			diag.close();
	 };
	 diag.show();
	 window.top.hangge();
}
function confirmDiv(id){
	var title='确认删除该表单吗？';
	var content='此操作会删除该表单以及此表单下的所有数据！';
	mesageConfirm('删除表单',title,content,"deleteform('"+id+"')");
}

//删除
function deleteform(id){
	$.ajax({
		type: "GET",
		url:adminPath+"customform/delete.do",
		data:{"id":id},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				 window.top.mesageTip("success","删除成功!");
				 location.href="<%=adminPath%>customform/list.do";	 
			}else{
				 window.top.mesageTip("failure","删除失败!");
			}
		}
	});
}
</script>
</html>