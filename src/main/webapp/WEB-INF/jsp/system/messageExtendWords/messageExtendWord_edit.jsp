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
<script type="text/javascript">
$(function(){
	var types=$(".wordtype");
	var field=$("#fieldType").val();
	$.each(types,function(index,obj){
		var select=$(obj).val();
		if(select==field){
			$(obj).attr("selected","true");
		}
	});
	
});
function save(type){
	if(!$("#Form").valid()){
		return false;
	} 
	if($("#nameSpan").val().length!=0){
		window.top.mesageTip("warn","名称重复");
	}
	sub(type);
}
function sub(type){
	    var name=$("#pname").val();
	    var fieldType=$("#u1813_input").val();
	    var id=$("#id").val();
		$.ajax({
			type: "POST",
			url:adminPath+"messageExtendWord/update.do",
			data:{fieldtype:fieldType,name:name,id:id},
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 window.top.mesageTip("success","操作成功");
						 location.href=adminPath+'messageExtendWord/toAdd.do';
					 }else{
						 window.top.mesageTip("success","操作成功");
						 location.href=adminPath+'messageExtendWord/list.do';
					 }
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
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
						<a href="<%=adminPath%>messageExtendWord/list.do">扩展管理</a><i>></i>
						<i>修改属性</i>
					</div>
				</div>
				
				<div class="cms_c_list cf">
					<h3>编辑内容</h3>
					
					<form   id="Form" method="post"  >
						<div class="add_btn_con wrap cf">
						
							<div class="zhaopin zp_con01 cf">
								<dl class="cf zp_dl">
									<dt><span class="hot">*</span>扩展字段名称</dt>
									<dd><div class="dd_con"><input type="text" id="pname" value="${property.name }" name="name" required><span id="nameSpan" class="warn_tip"></span></div></dd>
									<input type="hidden" id="id" value="${property.id }"/>
									<input type="hidden" id="fieldType" value="${property.fieldtype}"/>
								</dl>
							</div>
							<dl class="cf">
								<dt><span class="hot">*</span>扩展字段类型</dt>
								<dd>
									<div class="dd_con">
										<select id="u1813_input" name="fieldtype" class="form-control" style="width:200px;" >
								           <option class="wordtype" value="1">文本</option>
									       <option class="wordtype" value="2">长文本</option>
									       <option class="wordtype" value="3">日期</option>
									       <option class="wordtype" value="4">整数</option>
									       <option class="wordtype" value="5">小数</option>
								        </select>
									</div>
								</dd>
							</dl>
						</div>
						<div class="all_btn cf">
							<span id="result"></span>
							<input type="button" class="submit_btn" value="保存" onclick="save('0')"/>
							<a href="javascript:void(0)" class="submit_a_btn" onclick="save('1')">保存并继续添加</a>
							<a href="<%=adminPath%>/messageExtendWord/list.do" class="submit_re_btn">取消</a>
						</div>
				</form>
					
				</div>
			</div>
	<div class="footer">© 中企高呈 版权所有</div>
<!-- cms_con结束 -->

</body>
</html>