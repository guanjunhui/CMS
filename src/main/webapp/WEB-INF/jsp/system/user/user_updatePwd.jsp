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
		<form action="<%=adminPath%>user/updatePwd.do" name="Form" id="Form" method="post">
			<input type="hidden" id="USER_ID" name="USER_ID" value="${USER_ID}" />
			<div class="cms_c_list cf">
				<h3></h3>
				<div class="add_btn_con wrap cf">
					<dl class="cf">
						<dt><span class="hot">*</span>原密码</dt>
						<dd><div class="dd_con"><input type="password" id="PASSWORDOLD" name="PASSWORDOLD" value="" required onblur="checkoldpswd();"/><span class="warn_tip"></span></div></dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>新密码</dt>
						<dd><div class="dd_con"><input type="password" id="PASSWORD" name="PASSWORD" value=""/></div></dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>确认密码</dt>
						<dd><div class="dd_con"><input type="password" id="PASSWORDAgain" name="PASSWORDAgain" value=""/></div></dd>
					</dl>
				</div>
				<div class="all_btn cf">
					<input type="button" onclick="updatePwd();" class="submit_btn" value="保存" />
					<a href="javascript:void(0);" onclick="top.Dialog.close();" class="submit_re_btn">取消</a>
				</div>
			</div>
		</form>
</body>
<script type="text/javascript">
var oldFlag=false;
//获取角色树形结构
function checkoldpswd(){
	var oldPwd=$("#PASSWORDOLD").val();
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "POST",
		url:adminPath+"user/checkoldpswd.do",
		data:{"oldPwd":oldPwd,"userId":userId},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				console.log(result.data);
				if(result.data=='0'){
					$(".warn_tip").text("原密码错误");
					oldFlag=false;
				}else{
					$(".warn_tip").text("");
					oldFlag=true;
				}
			}
		}
	});
}

//获取角色树形结构
function updatePwd(){
	if(!$("#Form").valid()){
		return false;
	}
	if(!oldFlag){
		return false;
	}
	var pwd=$("#PASSWORD").val();
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "POST",
		url:adminPath+"user/updatePwd.do",
		data:{"USER_ID":userId,"PASSWORD":pwd},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				window.top.mesageTip("success","密码修改成功!");
				location.href="<%=adminPath%>logout.do";
			}else{
				 window.top.mesageTip("failure","密码修改失败!");
			}
		}
	});
}

</script>
</html>          