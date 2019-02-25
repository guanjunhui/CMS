<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

<style>
.add_btn_con dl dt{width:160px;margin-right:-160px;}
.add_btn_con dl dd .dd_con{padding-left:170px;}
</style>
</head>
<body>
	
	<!-- cms_con开始 -->
				<div class="cms_con cf">
					<div class="cms_con cf">
					<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
						<i>></i>
						<a href="<%=adminPath%>/registration/list.do">表单列表</a>
						<i>></i>
						<i>详情</i>
					</div>
				</div>	
				<div class="add_btn_con wrap cf">
						
					<div class="zhaopin zp_con01 cf">
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								FULLNAME
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.fullName}">
								</div>		
							</dd>	
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								EMAIL
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.email}">
								</div>		
							</dd>					
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								STREET
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.street}">
								</div>		
							</dd>					
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								LINE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.line}">
								</div>		
							</dd>	
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								CITY
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.city}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								POSTAL_CODE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.postalCode}">
								</div>		
							</dd>					
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								COUNTRY
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.country}">
								</div>		
							</dd>		
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								PHONE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.phone}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								PRODUCT
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.product}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								SPECIFY_OTHER
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.specifyOther}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								SERIAL_NUMBER
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.serialNumber}">
								</div>		
							</dd>
						</dl>
						<dl class="cf" id="imgFileId">
							
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								EXPLANATION
							</dt>
							<dd>
								<div class="dd_con">
									<ul><li>
									<textarea class="textarea_numb right" name="" style="width:50%;">${data.explanation}</textarea>
								</li></ul>
								</div>	
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								PURCHASE_PLACE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.purchasePlace}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								PURCHASE_DATE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.purchaseDate}">
								</div>		
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class='hot'>*</span>
								REGIST_CODE
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" value="${data.registCode}">
								</div>		
							</dd>
						</dl>
						<div class="all_btn cf">
							<span id="result"></span>
							<input type="button" class="submit_btn" value="返回" onclick="cancel()"/>
						</div>
					</div>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>

</body>
<script type="text/javascript">
	$(function(){
		var url = "${data.fileUrl}"
		var regx = /.(gif|jpg|jpeg|png|gif|jpg|png)$/;
		if(url != '' && regx.test(url)){
			var str = '';
			str += "<dt>";
			str += "<span class='hot'>*</span>PICTURE</dt><dd>";
			str += "<div class='dd_con'>";
			str += "<a id='imgId'><img src='uploadFiles/uploadImgs/${data.fileUrl}' width='220px' height='150px'></a>";
			str += "</div>";		
			str += "</dd>";
			$("#imgFileId").append(str);
			$("#imgId").click(function(){
				window.open("<%=basePath%>uploadFiles/uploadImgs/${data.fileUrl}");
			});
		}
	});
	function cancel(){
		window.location.href = "<%=adminPath%>/registration/list.do";
	}
</script>
</html>