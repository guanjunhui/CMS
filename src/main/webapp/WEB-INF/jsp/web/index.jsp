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
	function setCookie(name, value)
	{
		var argv = setCookie.arguments;
		var argc = setCookie.arguments.length;
		var expires = (argc > 2) ? argv[2] : null;
		if(expires!=null)
		{
			var LargeExpDate = new Date ();
			LargeExpDate.setTime(LargeExpDate.getTime() + (expires*1000*60*60*24));
		}
		document.cookie = name + "=" + escape(value)+((expires == null) ? "" : ("; expires=" +LargeExpDate.toGMTString()));
		window.location.href=window.location.href;
	}
	
	
	//服务器校验
	function severCheck(){
		var username = $("input[name='username']").val();
		var password = $("input[name='password']").val();
		$.ajax({
			type: "POST",
			url: "<%=memberPath%>login",
	    	data: {'username':username,'password':password},
			dataType:'json',
			cache: false,
			success: function(result){
				if("success" == result.data){
					window.location.href="<%=memberPath%>index";
				}else if("usererror" == result.data){
					$("#loginname").tips({
						side : 1,
						msg : "用户名或密码有误",
						bg : '#FF5080',
						time : 15
					});
					showfh();
					$("#loginname").focus();
				}else if("codeerror" == result.data){
					$("#code").tips({
						side : 1,
						msg : "验证码输入有误",
						bg : '#FF5080',
						time : 15
					});
					showfh();
					$("#code").focus();
				}else{
					$("#loginname").tips({
						side : 1,
						msg : "缺少参数",
						bg : '#FF5080',
						time : 15
					});
					showfh();
					$("#loginname").focus();
				}
			}
		});
	}

	
	</script>

  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
  	<button id="language1" onclick="javascript:setCookie('local','zh')"> <spring:message code="language.china.text"/></button> 	
	<button id="language2" onclick="javascript:setCookie('local','en')"> <spring:message code="language.english.text"/></button> 
  <spring:message code="message.test"/>
  
  <form action="/memberlogin" method="post">  
    <table>  
	    <tr>  
	    <td width="66" align="right"><font size="3">帐号：</font></td><td colspan="2"><input type="text" name="username" value="test" style="width:200;height:25;"/></td>  
	    </tr>  
	    <tr>  
	    <td align="right"><font size="3">密码：</font></td><td colspan="2"><input type="text" name="password"  value="1" style="width:200;height:25;"/></td>  
	    </tr>  
    </table>  
    <input type="button" onclick="javascript:severCheck();" value="会员登录">
  </form> 
</html>
