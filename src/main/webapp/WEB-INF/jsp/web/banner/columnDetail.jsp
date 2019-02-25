<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  isELIgnored="false"%>
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
</head>
<body class="no-skin">
<style>
ul,ul li{list-style:none;}
.show_li{width:420px;margin:20px auto;}
.show_li li{height:35px;line-height:35px;font-size:14px;overflow:hidden;}
.show_li span{display:block;float:left;width:100px;}
.show_li i{display:block;float:left;width:320px;color:#666;font-style:normal;}
</style>
	<ul class="show_li">
		<li>
			<span>栏目名称:</span><i>${column.columName}</i>
		</li>

		<li>
			<span>访问路径:</span><i>${column.columUrlpath}</i>
		</li>

		<li>
			<span>所属站点ID:</span><i>${column.siteid}</i>
		</li>

		<li>
			<span>栏目副标题:</span><i>${column.columSubname}</i>
		</li>

		<li>
			<span>栏目图片路径:</span><i>${column.columImage}</i>
		</li>

		<li>
			<span>栏目描述:</span><i>${column.columDesc}</i>
		</li>

		<li>
			<span>创建时间:</span><i><fmt:formatDate value="${column.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></i>
		</li>
	</ul>
</body>
</script>
</html>