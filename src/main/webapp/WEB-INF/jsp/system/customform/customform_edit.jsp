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
			<form action="<%=adminPath%>customform/save.do" name="Form" id="Form" method="post">
				<input type="hidden" id="id" name="id" value="${pd.id}" />
				<div class="cms_c_list cf" style="box-shadow: rgb(238, 238, 238) 0px 0px 0px;">
					<h3></h3>
					<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt><span class="hot">*</span>表单名称</dt>
							<dd><div class="dd_con"><input type="text" id="formName" name="formName" value="${pd.formName}" class="required" /></div></dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" class="submit_btn" onclick="save();" value="保存" />
						<a href="javascript:void(0)" onclick="top.Dialog.close();" class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
</body>
<script type="text/javascript">
function save(){
	if(!$("#Form").valid()){
		return false;
	}
	window.top.jzts();
	var formData=$("#Form").jsonObject();
	$.ajax({
		type: "POST",
		url:adminPath+"customform/save.do",
		data:formData,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 top.Dialog.close();
			 }else{
				 window.top.mesageTip("failure","保存失败，请联系管理员!");
			 }
		}
	});
	window.top.hangge();
}
</script>
</html>
