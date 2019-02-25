<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = "//" + request.getServerName()
			+ path + "/";
	String adminPath = (String) application.getAttribute("adminPath");
	String casLoginUrl = (String) application.getAttribute("casLoginUrl");
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>${pd.SYSNAME}</title>
<meta charset="utf-8">
<meta name="author" content="" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="renderer" content="webkit" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1"
	name="viewport" />
<title>BestCMS</title>
<link rel="shortcut icon" href="staticRelease/images/back/favicon.ico" type="image/x-icon" />
<script>
	if (/*@cc_on!@*/false && document.documentMode === 10)
		document.documentElement.className += ' ie10';
</script>
<script>
	if (/*@cc_on!@*/false && document.documentMode === 11)
		document.documentElement.className += ' ie11';
</script>
<!-- 添加IE10+Class -->
<link rel="stylesheet" href="static/login/css/base.css">
<link rel="stylesheet" href="static/login/css/style.css">
<!--公用文件 END-->
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="static/login/js/common.js"></script>
<script src="static/login/js/bootstrap.min.js"></script>
<script src="static/login/js/jquery.easing.1.3.js"></script>
<script src="static/login/js/jquery.mobile.customized.min.js"></script>
<script src="static/login/js/camera.min.js"></script>
<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
<script type="text/javascript">
if (window != top){
	top.location.href=location.href;
}
$(document).ready(function() {
	changeCode1();
	$("#codeImg").bind("click", changeCode1);
	//$("#zcodeImg").bind("click", changeCode2);
});

function loginSubmit(){
	var code=$("#code").val();
	$.ajax({
		type: "GET",
		url: "<%=adminPath%>checkCode",
    	data: {'code':code},
		dataType:'json',
		cache: false,
		success: function(result){
			if("-1" == result.code){
				$("#code").tips({
					side : 1,
					msg : result.message,
					bg : '#FF5080',
					time : 15
				});
				$("#code").focus();
			}else{
				flushLoginTicket();
			}		
		} 
	});
}

function flushLoginTicket(){
	 $.ajax({
		   url: '<%=casLoginUrl%>?get-lt=true&service=<%=basePath%>casLogin&n=ajax',
	       dataType: "jsonp",
	       jsonpCallback: "jsonpcallback",
	       success: function (data) {
	         $('#J_LoginTicket').val(data.lt); 
	         $('#J_Execution').val(data.execution);
	         $('#login_form').submit();
	       },  
	       error:function(){
	       	alert('网络访问错误!');
	       }  
  	});
}



/**
window.addEventListener("message",function(e){
	console.log(e.data);
},false);*/

var logincallback = function(result) {
	if (result.data == "usererror"){
		$("#J_Username").tips({
			side : 1,
			msg : "用户名或密码有误",
			bg : '#FF5080',
			time : 15
		});
		$("#J_Username").focus();
	}else{
		window.location.href="<%=adminPath%>index.do";
	}
}; 

//服务器校验
function severCheck(){
	if(check()){
		var loginname = $("#loginname").val();
		var password = $("#password").val();
		var code=$("#code").val();
		$.ajax({
			type: "POST",
			url: "<%=adminPath%>loginsubmit.do",
	    	data: {'username':loginname,'password':password,'code':code,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(result){
				if("success" == result.data){
					window.location.href="<%=adminPath%>index.do";
				}else if("usererror" == result.data){
					$("#loginname").tips({
						side : 1,
						msg : "用户名或密码有误",
						bg : '#FF5080',
						time : 15
					});
					
					$("#loginname").focus();
				}else if("codeerror" == result.data){
					$("#code").tips({
						side : 1,
						msg : "验证码输入有误",
						bg : '#FF5080',
						time : 15
					});
					
					$("#code").focus();
				}else{
					$("#loginname").tips({
						side : 1,
						msg : "缺少参数",
						bg : '#FF5080',
						time : 15
					});
					
					$("#loginname").focus();
				}
			}
		});
	}
}

	$(document).keyup(function(event) {
		if (event.keyCode == 13) {
			$("#to-recover").trigger("click");
		}
	});

	function genTimestamp() {
		var time = new Date();
		return time.getTime();
	}

	function changeCode1() {
		$("#codeImg").attr("src", "<%=adminPath%>code.do?t=" + genTimestamp());
	}

	//客户端校验
	function check() {
		if ($("#loginname").val() == "") {
			$("#loginname").tips({
				side : 2,
				msg : '用户名不得为空',
				bg : '#AE81FF',
				time : 3
			});
			
			$("#loginname").focus();
			return false;
		} else {
			$("#loginname").val(jQuery.trim($('#loginname').val()));
		}
		if ($("#password").val() == "") {
			$("#password").tips({
				side : 2,
				msg : '密码不得为空',
				bg : '#AE81FF',
				time : 3
			});
			
			$("#password").focus();
			return false;
		}
		if ($("#code").val() == "") {
			$("#code").tips({
				side : 1,
				msg : '验证码不得为空',
				bg : '#AE81FF',
				time : 3
			});
			
			$("#code").focus();
			return false;
		}
		$("#loginbox").tips({
			side : 1,
			msg : '正在登录 , 请稍后 ...',
			bg : '#68B500',
			time : 10
		});

		return true;
	}

	//登录注册页面切换
	function changepage(value) {
		if (value == 1) {
			$("#windows1").hide();
			$("#windows2").show();
			changeCode2();
		} else {
			$("#windows2").hide();
			$("#windows1").show();
			changeCode1();
		}
	}

	//注册
	function rcheck() {
		if ($("#USERNAME").val() == "") {
			$("#USERNAME").tips({
				side : 3,
				msg : '输入用户名',
				bg : '#AE81FF',
				time : 2
			});
			$("#USERNAME").focus();
			$("#USERNAME").val('');
			return false;
		} else {
			$("#USERNAME").val(jQuery.trim($('#USERNAME').val()));
		}
		if ($("#PASSWORD").val() == "") {
			$("#PASSWORD").tips({
				side : 3,
				msg : '输入密码',
				bg : '#AE81FF',
				time : 2
			});
			$("#PASSWORD").focus();
			return false;
		}
		if ($("#PASSWORD").val() != $("#chkpwd").val()) {
			$("#chkpwd").tips({
				side : 3,
				msg : '两次密码不相同',
				bg : '#AE81FF',
				time : 3
			});
			$("#chkpwd").focus();
			return false;
		}
		if ($("#name").val() == "") {
			$("#name").tips({
				side : 3,
				msg : '输入姓名',
				bg : '#AE81FF',
				time : 3
			});
			$("#name").focus();
			return false;
		}
		if ($("#EMAIL").val() == "") {
			$("#EMAIL").tips({
				side : 3,
				msg : '输入邮箱',
				bg : '#AE81FF',
				time : 3
			});
			$("#EMAIL").focus();
			return false;
		} else if (!ismail($("#EMAIL").val())) {
			$("#EMAIL").tips({
				side : 3,
				msg : '邮箱格式不正确',
				bg : '#AE81FF',
				time : 3
			});
			$("#EMAIL").focus();
			return false;
		}
		if ($("#rcode").val() == "") {
			$("#rcode").tips({
				side : 1,
				msg : '验证码不得为空',
				bg : '#AE81FF',
				time : 3
			});
			$("#rcode").focus();
			return false;
		}
		return true;
	}

	//提交注册
	function register() {
		if (rcheck()) {
			var nowtime = date2str(new Date(), "yyyyMMdd");
			$.ajax({
				type : "POST",
				url : 'appSysUser/registerSysUser.do',
				data : {
					USERNAME : $("#USERNAME").val(),
					PASSWORD : $("#PASSWORD").val(),
					NAME : $("#name").val(),
					EMAIL : $("#EMAIL").val(),
					rcode : $("#rcode").val(),
					FKEY : $.md5('USERNAME' + nowtime + ',fh,'),
					tm : new Date().getTime()
				},
				dataType : 'json',
				cache : false,
				success : function(data) {
					alert(data.result);
					if ("00" == data.result) {
						$("#windows2").hide();
						$("#windows1").show();
						$("#loginbox").tips({
							side : 1,
							msg : '注册成功,请登录',
							bg : '#68B500',
							time : 3
						});
						changeCode1();
					} else if ("04" == data.result) {
						$("#USERNAME").tips({
							side : 1,
							msg : "用户名已存在",
							bg : '#FF5080',
							time : 15
						});
						
						$("#USERNAME").focus();
					} else if ("06" == data.result) {
						$("#rcode").tips({
							side : 1,
							msg : "验证码输入有误",
							bg : '#FF5080',
							time : 15
						});
						
						$("#rcode").focus();
					}
				}
			});
		}
	}

	//邮箱格式校验
	function ismail(mail) {
		return (new RegExp(
				/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/)
				.test(mail));
	}
	//js  日期格式
	function date2str(x, y) {
		var z = {
			y : x.getFullYear(),
			M : x.getMonth() + 1,
			d : x.getDate(),
			h : x.getHours(),
			m : x.getMinutes(),
			s : x.getSeconds()
		};
		return y.replace(/(y+|M+|d+|h+|m+|s+)/g, function(v) {
			return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1)))
					.slice(-(v.length > 2 ? v.length : 2))
		});
	};
</script>
</head>
<body>
	<div class="container-fluid-full login">
		<div class="w1200 cleargap prz">
			<div class="ibox login-bar style-verti">
				<div class="login-inner">
					<h4 class="container-title">管理员登录</h4>
					<form method="post" action="${casLoginUrl}" id="login_form" target="ssoLoginFrame">
						<div class="mt50 prz">
							<div class="info-item check-item">
								<div class="input-outer clearfix form-item">
									<p class="login-icon-box cleargap fl">
										<img src="static/login/icon-user.png" class="ibox style-verti">
										<span class="ibox blank style-verti"></span>
									</p>
									<input type="text" id="J_Username" name="username"
										class="fl login-input" placeholder="请输入用户名" value="CE28911116">
								</div>
								<div class="input-tip mt10">请输入用户名</div>
							</div>
							<div class="mt10 info-item check-item">
								<div class="input-outer clearfix form-item">
									<p class="login-icon-box cleargap fl">
										<img src="static/login/icon-pw.png" class="ibox style-verti">
										<span class="ibox blank style-verti"></span>
									</p>
									<input type="password" id="J_Password" name="password"
										class="fl login-input" placeholder="请输入密码" 
										data-count="0" value="tykj123456">
								</div>
								<div class="input-tip mt10">请输入密码</div>
							</div>
							<div class="pra input-tip input-error" id="errorTip">用户名或密码不正确，请重新输入</div>
							<div class="mt10 verif check-item">
								<div class="verif-cont info-item">
									<div class="clearfix form-item">
										<div class="input-outer clearfix fl">
											<p class="login-icon-box cleargap fl">
												<img src="static/login/yan.png" class="ibox style-verti">
												<span class="ibox blank style-verti"></span>
											</p>
											<input type="text" name="code" id="code"
												class="fl login-input" placeholder="请输入验证码">
											
										</div>
										<div class="clearfix fl" style="margin-top:20px;margin-left:10px;cursor:pointer">
											<img style="height: 22px;" id="codeImg" alt="点击更换"
												title="点击更换" src="" />
										</div>
									</div>
								</div>
							</div>
						</div>
						<%-- <c:if test="${pd.isZhuce == 'yes' }">
						<input type="button" class="login-btn mt10" value="注册" onclick="changepage(1);">
						</c:if> --%>
						<input type="button" id="to-recover" class="login-btn mt10" value="登录"
							onclick="loginSubmit();">
						<!-- cas隐藏域 -->
					    <input type="hidden" name="checkCode" value="vsd" />
				        <input type="hidden" name="isajax" value="true" />
				        <input type="hidden" name="isframe" value="true" />
				        <input type="hidden" name="execution" id="J_Execution" value="" /> <br>
				        <input type="hidden" name="lt" value="" id="J_LoginTicket">
				        <input type="hidden" name="_eventId" value="submit" />
					</form>
				</div>
				
			</div>
			<div class="ibox blank style-verti"></div>
			<div class="copyright pra">
				©<a href="http://www.cebest.com">中企高呈</a> 版权所有
			</div>
		</div>
	</div>
	<iframe style="display:none;width:10;height:10" id="ssoLoginFrame" name="ssoLoginFrame"/>
</body>
</html>
