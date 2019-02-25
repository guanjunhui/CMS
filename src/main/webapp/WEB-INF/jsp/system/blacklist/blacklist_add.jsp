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
			<form action="<%=adminPath%>blacklist/${msg }.do" name="Form" id="Form" method="post">
				<input type="hidden" id="BLACKLIST_ID" name="BLACKLIST_ID" value="${pd.BLACKLIST_ID}" />
				<div id="zhongxin" class="cms_c_list cf">
					<h3></h3>
					<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt><span class="hot">*</span>IP</dt>
							<dd><div class="dd_con"><input type="text" class="required" name="IP" id="IP" value="${pd.IP}" maxlength="20" placeholder="这里输入IP" title="IP" style="width:98%;"/></div></dd>
						</dl>
					</div>
					<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt><span class="hot">*</span>备注</dt>
							<dd><div class="dd_con"><input type="text" class="required" name="BZ" id="BZ" value="${pd.BZ}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></div></dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" class="submit_btn" onclick="save();" value="提交" />
						<a href="javascript:void(0)" onclick="top.Dialog.close();" class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
<style type="text/css">
.dialog_minbtn,.dialog_maxbtn,.dialog_closebtn{display:none!important;}
</style>
</body>
<script type="text/javascript">
$(top.hangge());
//保存
function save(){
	if($("#IP").val()==""){
		$("#IP").tips({
			side:3,
            msg:'请输入IP',
            bg:'#AE81FF',
            time:2
        });
		$("#IP").focus();
	return false;
	}
	if($("#BZ").val()==""){
		$("#BZ").tips({
			side:3,
            msg:'请输入备注',
            bg:'#AE81FF',
            time:2
        });
		$("#BZ").focus();
	return false;
	}
	$("#Form").submit();
	$("#zhongxin").hide();
}
</script>
</html>