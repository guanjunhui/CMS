
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
<script type="text/javascript" src="<%=basePath%>/plugins/My97DatePicker/WdatePicker.js"></script>
<style>
	.biaoge_con > div.h3 input[type="text"] {border-right:1px solid #ccc;border-radius:3px;}
	.form-control{width:80px;margin-left:20px;}
</style>
</head>
<body>
		<form action="<%=adminPath%>happuser/listUsers.do" method="post" name="Form" id="Form">
		<div class="cms_con cf">
<%@ include file="../index/n_prenav.jsp"%>
			<div class="cms_c_list biaoge_con juese_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>happuser/goAddU.do" >+创建会员</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<dl style="float:left;">
							<dd class="data_dd">
								<div class="dd_con">
									<div class="sle_data">
										<em>最后登录时间：始</em>
										<span><input style="width:140px;" id="d5221" name="lastLoginStart"
											type="text" value="${pd.lastLoginStart}"
											onclick="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.click();},maxDate:'#F{$dp.$D(\'d5222\')}'})" /><i></i></span>
										<em>至</em> <span><input style="width:140px;" id="d5222" name="lastLoginEnd"
											class="Wdate" type="text" value="${pd.lastLoginEnd}"
											onclick="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})" /><i></i></span>
										<div class="disable"></div>
									</div>
								</div>
							</dd>
						</dl>
						<select name="STATUS" id="STATUS" class="form-control" style="float:left; width:100px;">
							<option value="">-请选择-</option>
							<option value="1" <c:if test="${pd.STATUS == '1' }">selected</c:if> >正常</option>
							<option value="0" <c:if test="${pd.STATUS == '0' }">selected</c:if> >冻结</option>
						</select>
					</div>
					
					<div class="search">
						<input type="text" id="key" name="keywords" value="${pd.keywords}" style="width:250px;"><input
							id="ss" type="button" value="搜索" />
					</div>
					
				</div>
				<div class="table cf">
					<dl class="list_bg col_07 cf">
						<dt class="cf">
							<div class="tit7">
								<div class="tit7_con">
									会员
								</div>
							</div>
							<div class="sele07">操作</div>
							<div class="sele07">状态</div>
							<div class="sele07">到期日期</div>
							<div class="sele07">上次登录时间</div>
							<div class="sele07 other_width">电话</div>
							<div class="sele07">姓名</div>
						</dt>
						<c:choose>
							<c:when test="${not empty userList}">
								<c:forEach items="${userList }" var="user">
									<dd class="cf">
										<div id="div_${user.USER_ID}" class="dd_tit cf">
											<div class="tit7">
												<div class="tit7_con">
													${user.USERNAME}
												</div>
											</div>
											<!-- 操作 -->
											<div class="sele07 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<li><a href="<%=adminPath%>happuser/goEditU?USER_ID=${user.USER_ID}">编辑</a></li>
													<li><a href="javascript:void(0);" onclick="confirmDelDiv('${user.USERNAME}','${user.USER_ID}')">删除</a></li>
													<li><a href="javascript:void(0);" onclick="confirmChangeStatus('${user.STATUS}','${user.USER_ID}','${user.USERNAME}')">
														<c:choose>
															<c:when test="${user.STATUS == '0'}">正常</c:when>
															<c:otherwise>冻结</c:otherwise>
														</c:choose>
													</a></li>
												</ul>
											</div>
											<!--状态  -->
											<div class="sele07 hide_font">
												<div class="sele04" name="${user.USER_ID}">
													<c:choose>
														<c:when test="${user.STATUS == '0'}">
															冻结
														</c:when>
														<c:otherwise>
															正常
														</c:otherwise>
													</c:choose>
												</div>
											</div>
											<!--到期日期  -->
											<div class="sele07">${user.END_TIME} </div>
											<!--上次登录日期  -->
											<div class="sele07">${user.LAST_LOGIN} </div>
											<!--电话 -->
											<div class="sele07 other_width">${user.PHONE} </div>
											<!--姓名  -->	
											<div class="sele07">${user.NAME} </div>
										</div>
									</dd>
								</c:forEach>
							</c:when>
						</c:choose>
					</dl>
					<div class="bottom_con cf">
						<div class="page_list cf">
							${page.pageStr}
						</div>
					</div>
				</div>
				</div>
			</div>
			</form>
			<div class="footer">© 中企高呈 版权所有</div>
		</div>
<script type="text/javascript">

$(function(){
	var msg = "${msg}";
	if(msg == "success"){
		serch();
	}	
});

$(function(){
	top.hangge();
	//setRolediv();
	//搜索
	$("#ss").click(function(){
		serch();
	});
	

});

function serch(){
	$("#Form").submit();
}
//改变状态
function confirmChangeStatus(status,id,username){
	status = (status==1)?0:1;
	var path = "<%=adminPath%>happuser/changeStatus";
	$.ajax({
		type:"post",
		url:path,
		data:{
			USER_ID:id,
			STATUS:status,
			USERNAME:username
		},
		success:function(data){
			if(data == "success"){
				window.location.href = "<%=adminPath%>happuser/listUsers.do";
			}else{
				alert("系统繁忙，请稍后重试....");
			}
		}
	});
}

function confirmDelDiv(name,id){
	var	title='确认删除会员【'+name+'】的信息吗？';
	var	content='此操作会删除该用户的信息';
	mesageConfirm('删除用户信息',title,content,"delUser('"+id+"')");
}

function delUser(id){
	var path = "<%=adminPath%>happuser/deleteU?USER_ID="+id;
	$.ajax({
		type:"get",
		url:path,
		success:function(data){
			if(data == "success"){
				window.location.href = "<%=adminPath%>happuser/listUsers.do";
			}
		}
	});
	location.href='<%=adminPath%>user/deleteU?USER_ID='+id;
}

</script>
</body>
</html>          