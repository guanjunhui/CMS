<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<%
String path = request.getContextPath();
String basePath = "//"+request.getServerName()+path+"/";
String memberPath = (String)application.getAttribute("memberPath");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="static/login/js/jquery-1.5.1.min.js"></script>
	
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script>
	function golout(){
		window.location.href="<%=memberPath%>logout";
	}
	
	</script>

  </head>
  
  <body>
    	欢迎来到会员中心. <br>
    	<input type="button" onclick="javascript:golout();" value="会员注销">
    	
  </body>
  
</html>
