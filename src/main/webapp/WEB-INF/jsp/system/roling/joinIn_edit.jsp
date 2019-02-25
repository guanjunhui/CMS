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
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">

<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">	
//提示弹窗
function myMesageConfirm(a,b,c,_callback){
	html='<div class="layer_bg layer_bg04"><div class="layer_con cf xunwen"><h3><span>'+a+'</span><p class="close">x</p></h3><dl><dt><span class="ico bg-tishi03"></span></dt><dd><p>'+b+'</p><p>'+c+'</p></dd></dl><div class="all_btn cf"><input type="button" class="submit_btn" onclick="'+_callback+'"  value="确定" /><a href="javascript:;" class="submit_re_btn">取消</a></div></div></div>'
	$('body').append(html);
}

function save(){
	if(!$("#Form").valid()){
		return false;
	}
	var formData=$("#Form").jsonObject();
		$.ajax({
			type: "POST",
			url:adminPath+"join/update.do",
			data:formData,
			success: function(result){
				 if(result.success){
						 window.top.mesageTip("success","操作成功");
						 location.href=adminPath+'join/list.do';
				 }else{
					 window.top.mesageTip("failure","操作失败");
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
				<i>></i> <a href="<%=adminPath%>join/list.do">加盟商列表</a> <i>></i> <i>添加加盟商</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑内容</h3>

			<form id="Form" method="post" >
			<input type="hidden" id="id" name="id" required value="${addressQuery.id }">
				<div class="add_btn_con wrap cf">

					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>门店名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="myName" name="myName" required value="${addressQuery.myName }">
								</div>
							</dd>
						</dl>
					</div>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>省份
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="province" name="province" required value="${addressQuery.province }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>市
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="city" name="city" required value="${addressQuery.city }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>区域
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="area" name="area" required value="${addressQuery.area }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>品牌
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="brand" name="brand" required value="${addressQuery.brand }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>xPoint
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="xPoint" name="xPoint" value="${addressQuery.xPoint }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>yPoint
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="yPoint" name="yPoint" value="${addressQuery.yPoint }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>电话
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="tel" name="tel" required value="${addressQuery.tel }">
								</div>
							</dd>

					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>地址
						</dt>
						<dd>
								<div class="dd_con">
									<input type="text" id="address" name="address" required value="${addressQuery.address }">
								</div>
							</dd>

					</dl>
				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" onclick="save()" /> 
						<a href="<%=adminPath%>join/list.do" class="submit_re_btn">取消</a>
				</div>
				
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->


</body>
</html>