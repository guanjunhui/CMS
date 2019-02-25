<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = "//" + request.getServerName()
			+ path + "/";
	String adminPath = (String) application.getAttribute("adminPath");
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

<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<script src="js/respond.src.js"></script>
<![endif]-->
<!--公用文件 END-->
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="static/login/js/common.js"></script>

<script>
		if (window != top){
			top.location.href=location.href;
		}
  		//window.setTimeout(showfh,3000); 
  		var timer;
		function showfh(){
			fhi = 1;
			//关闭提示晃动屏幕，注释掉这句话即可
			//timer = setInterval(xzfh2, 10); 
		};
		var current = 0;
		function xzfh(){
			current = (current)%360;
			document.body.style.transform = 'rotate('+current+'deg)';
			current ++;
			if(current>360){current = 0;}
		};
		var fhi = 1;
		var current2 = 1;
		function xzfh2(){
			if(fhi>50){
				document.body.style.transform = 'rotate(0deg)';
				clearInterval(timer);
				return;
			}
			current = (current2)%360;
			document.body.style.transform = 'rotate('+current+'deg)';
			current ++;
			if(current2 == 1){current2 = -1;}else{current2 = 1;}
			fhi++;
		};
	</script>
</head>
<body>
	<div class="container-fluid-full login" style="background-image:url(staticRelease/images/back/login.jpg);">
		<div class="w1200 cleargap prz">
			<div class="ibox login-bar style-verti">
				<div class="login-inner">
					<h4 class="container-title">管理员登录</h4>
					<form action="" id="login_form">
						<div class="mt50 prz">
							<div class="info-item check-item">
								<div class="input-outer clearfix form-item">
									<p class="login-icon-box cleargap fl">
										<img src="static/login/icon-user.png" class="ibox style-verti">
										<span class="ibox blank style-verti"></span>
									</p>
									<input type="text" id="loginname" name="loginname"
										class="fl login-input" placeholder="请输入用户名">
								</div>
								<div class="input-tip mt10">请输入用户名</div>
							</div>
							<div class="mt10 info-item check-item">
								<div class="input-outer clearfix form-item">
									<p class="login-icon-box cleargap fl">
										<img src="static/login/icon-pw.png" class="ibox style-verti">
										<span class="ibox blank style-verti"></span>
									</p>
									<input type="password" id="password" name="password"
										class="fl login-input" placeholder="请输入密码" data-value="123"
										data-count="0">
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
							onclick="severCheck();">
					</form>

				</div>

			</div>
			<div class="ibox blank style-verti"></div>
			<div class="copyright pra">
				©<a href="http://www.cebest.com">中企高呈</a> 版权所有
			</div>
		</div>
	</div>

</body>
</html>
<script type="text/javascript">
$(document).ready(function() {
	changeCode1();
	$("#codeImg").bind("click", changeCode1);
	//$("#zcodeImg").bind("click", changeCode2);
});

		//服务器校验
		function severCheck(){
			if(check()){
				var loginname = $("#loginname").val();
				var password = $("#password").val();
				var code=$("#code").val();
				$.ajax({
					type: "POST",
					url: "<%=adminPath%>login.do",
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
			showfh();
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
			showfh();
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
			showfh();
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

	function savePaw() {
		if (!$("#saveid").attr("checked")) {
			$.cookie('loginname', '', {
				expires : -1
			});
			$.cookie('password', '', {
				expires : -1
			});
			$("#loginname").val('');
			$("#password").val('');
		}
	}

	function saveCookie() {
		if ($("#saveid").attr("checked")) {
			$.cookie('loginname', $("#loginname").val(), {
				expires : 7
			});
			$.cookie('password', $("#password").val(), {
				expires : 7
			});
		}
	}

	jQuery(function() {
		var loginname = $.cookie('loginname');
		var password = $.cookie('password');
		if (typeof (loginname) != "undefined"
				&& typeof (password) != "undefined") {
			$("#loginname").val(loginname);
			$("#password").val(password);
			$("#saveid").attr("checked", true);
			$("#code").focus();
		}
	});

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
						showfh();
						$("#USERNAME").focus();
					} else if ("06" == data.result) {
						$("#rcode").tips({
							side : 1,
							msg : "验证码输入有误",
							bg : '#FF5080',
							time : 15
						});
						showfh();
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
	<script src="static/login/js/bootstrap.min.js"></script>
	<script src="static/login/js/jquery.easing.1.3.js"></script>
	<script src="static/login/js/jquery.mobile.customized.min.js"></script>
	<script src="static/login/js/camera.min.js"></script>
	<!-- <script src="static/login/js/templatemo_script.js"></script> 
	<script src="static/login/js/ban.js"></script>-->
	<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript" src="static/js/jquery.cookie.js"></script>