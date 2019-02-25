<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = "//" + request.getServerName()
			+ path + "/";
	String adminPath = (String) application.getAttribute("adminPath");
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="<%=basePath%>/plugins/My97DatePicker/WdatePicker.js"></script>
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body>

	<div class="cms_con cf">
<div class="cms_c_inst neirong cf">
	<div class="left cf">
		<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
		<a href="<%=adminPath%>dictionaries/list.do">数据字典</a><i>></i>
		<i>字典维护</i>
	</div>
</div>
		<form action="<%=adminPath%>dictionaries/saveRoot.do" name="Form" id="Form"
			method="post">
			<div class="cms_c_list cf">
				<h3>添加根类目</h3>
				<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt>
								<span class="hot">*</span>名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="NAME" name="NAME" required
										onblur="checkUserName();" /><span class="warn_tip"
										style="display: none;"></span>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class="hot"></span>英文
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="NAME_EN" name="NAME_EN"/>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>
								<span class="hot">*</span>编码
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="BIANMA" name="BIANMA" onblur="hasBianma();" class="required"/><span></span>
								</div>
							</dd>
						</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>内容
						</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="VALUE" name="VALUE"/>
							</div>
							<input type="hidden" id="PARENT_ID" name="PARENT_ID" value="0"/>
						</dd>
					</dl>
					<dl class="cf">
						<dt>
							<span class="hot">*</span>排序
						</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="ORDER_BY" name="ORDER_BY" 
									onblur="checkPhone();" class="required digits" /><span
									class="warn_tip" style="display: none;"></span>
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>备注
						</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="BZ" name="BZ" onblur="checkEmail();"/>
								<span class="warn_tip" style="display: none;"></span>
							</div>
						</dd>
					</dl>
				</div>
				<div class="all_btn cf">
					<input type="button" onclick="save('0');" class="submit_btn" value="保存" />  
					<a href="<%=adminPath%>dictionaries/list.do?DICTIONARIES_ID=0" class="submit_re_btn">取消</a>
				</div>
				
			</div>
		</form>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
</body>
<script type="text/javascript">
	var isAllow = true;
	//判断编码是否存在
	function hasBianma(){
		var BIANMA = $.trim($("#BIANMA").val());
		if("" == BIANMA)return;
		$.ajax({
			type: "POST",
			url: '<%=adminPath%>dictionaries/hasBianma.do',
	    	data: {BIANMA:BIANMA,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					 isAllow = true;
					 $("#BIANMA").next().hide();
				 }else{
					$("#BIANMA").next().html("&nbsp;&nbsp;&nbsp;<font color='red'>此编码已存在,请重新输入</font>");
					$("#BIANMA").next().show();
					 isAllow = false;
				 }
			}
		});
	}
	//判断用户名是否存在
	function checkUserName() {
		var USERNAME = $("#USERNAME").val();
		if (USERNAME == '' || USERNAME == undefined || USERNAME == null) {
			return false;
		}
	}


	function save(type) {
		if (!$("#Form").valid()) {
			return false;
		}
		if (!isAllow) {
			return false;
		}
		var parentId = "${parentId}";
		if(parentId.trim() != '' && parentId != null){
			alert(parentId);
			$("#PARENT_ID").val(parentId)
		}
		var NAME = $("#NAME").val();
		var NAME_EN = $("#NAME_EN").val();
		var BIANMA = $("#BIANMA").val();
		var PARENT_ID = $("#PARENT_ID").val();
		var VALUE = $("#VALUE").val();
		var ORDER_BY = $("#ORDER_BY").val();
		var BZ = $("#BZ").val();
		$.post(
			"<%=adminPath%>dictionaries/saveRoot.do",
			{
				"NAME":NAME,
				"NAME_EN":NAME_EN,
				"BIANMA":BIANMA,
				"PARENT_ID":PARENT_ID,
				"VALUE":VALUE,
				"ORDER_BY":ORDER_BY,
				"BZ":BZ
			},
			function(data){
				if(data == "success"){
					 window.top.mesageTip("success","保存成功!");
					 parent.location.href="<%=adminPath%>dictionaries/listAllDict.do?DICTIONARIES_ID=${DICTIONARIES_ID}&dnowPage=${page.currentPage}";
				}
			}
		);
	}
	
</script>
</html>
