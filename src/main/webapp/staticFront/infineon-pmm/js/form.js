
var _urlData={
		/*"loginUrl":"http://test.bestcms.com:8081/channel/2bffcce53f4141acb976bdaf7bf86c69.html"*/
		/*"loginUrl":"https://infineon-pmm.xind.cebest.com/channel/2bffcce53f4141acb976bdaf7bf86c69.html"*/
		"loginUrl":"http://power-sensor.infineon.cn/channel/2bffcce53f4141acb976bdaf7bf86c69.html"
};

var tk=new Date().getTime();
	
	/*var dress='http://172.22.151.124:8083/center';*/
	/*var dress = 'https://infineon-ipc.xind.cebest.com/center';*/
	var dress = 'https://power-sensor.infineon.cn/center';
	//验证码
		function maFun(_this){
			var that = _this;
			inputText=that.val();
			$.ajax({
				url: dress+'/pub/verifyCode',
				type:'GET',
				data:{
					code:inputText,
					tk:tk
				},
				xhrFields:{
				    withCredentials:true
				},
				success:function(data){	
					
					if(data.code!=200){
						that.siblings('em').text('验证码不正确');
						flag=false;
						return false;
					} else {
						that.siblings('em').text('');
						return true;						
					}
				}
			})
		}
		
		function showTit (msg){
			$(".SolutionForm.register").show();
			$(".SolutionForm.register .SolutionFormBox").text(msg);		
		}
		
$(function() {
	//过滤危险字符
	$(document).on('keyup', 'input[type=text],textarea,input[type=password]', function () {

        var val = $(this).val().toLocaleLowerCase();
        var otherKey = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |<|>|*|/*|*/|'|;|\\u|insert|select|delete|update|create|drop|script|javascript|alert";
        var goon = true;
        for (var i = 0; i < otherKey.split('|').length ; i++) {
            if (goon) {
                if (val.indexOf(otherKey.split('|')[i]) != -1) {
                    $(this).siblings('em').text('您输入的字符不能包含空格');
                    $(this).val('');
                    goon = false;
                    return;
                }
            }
        }
        if(val.indexOf(' ')>=0){
			$(this).siblings('em').text('您输入的字符不能包含空格');
			return false; 
		}
    });

	var tools = {
	 	init: function() {
	 		
	 		//下拉框模拟
			$(".z-register .input").click(function(){	
				var liLength = $(this).siblings('.select-con').find('ul li').length;
				if($(this).hasClass('on')){
//					$(this).siblings('.select-con').css({
//						'height':'0'
//					});
					$(this).siblings('.select-con').hide();
					$(this).removeClass('on');
				}else{
					$(this).addClass('on');
					if(liLength<=4){
						$(this).siblings('.select-con').show()
					}else{				
						$(this).siblings('.select-con').show();
						$(this).siblings('.select-con').css({
							'height':'168px'
						})
					}			
				};
				var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
				if (e && e.stopPropagation) {
					e.stopPropagation();
				} else if (window.event) {
					window.event.cancelBubble = true;
				}
			})	
			
			$("input[type=file]").change(function(e){
				var objUrl = getObjectURL(this.files[0]); 
			    if (objUrl) {  
			        $(".z-regisCon li .pic div img").attr("src", objUrl) ;  
			    }
			})
			function getObjectURL(file) {  
			    var url = null ;   
			    if (window.createObjectURL!=undefined) { // basic  
			        url = window.createObjectURL(file) ;  
			    } else if (window.URL!=undefined) { // mozilla(firefox)  
			        url = window.URL.createObjectURL(file) ;  
			    } else if (window.webkitURL!=undefined) { // webkit or chrome  
			        url = window.webkitURL.createObjectURL(file) ;  
			    }  
			    return url ;  
			} 
			//点击下拉框获取值给模拟的input
			$(".z-regisCon .select ul").on('click','li',function(){
				var tex = $(this).text();
				$(this).parents().siblings('.input').text(tex).removeClass('on');
				$(this).parents('.select-con').hide();
			})
		
			//点击盒子以外的地方隐藏下拉框
			$(document).click(function(event){
				$(".z-regisCon .select-con").hide();
				$('.z-regisCon .select .input').removeClass('on');
			});
			
		 	//放验证码
		 	$(".z-regisCon .z-ma img").attr('src',dress+'/pub/getCode?tk='+tk);
	 	
	 	
		 	//地区接口
			$.ajax({
				url:dress+'/pub/getAllCountry',
				type:'GET',
				success:function(data){
					if(data.code==200){
						var liText='';
						for(var i=0; i<data.data.length;i++){
							liText +='<li>'+data.data[i]+'</li>' 
						}
						$(".select_area .input").text(data.data[0])
						$(".select_area ul").append(liText)
					}
				}
			})
		
		
			//验证码点击换一换
			$(".z-regisCon .z-ma span").click(function(){
				tk+=1;
				$(".z-regisCon .z-ma img").attr('src',dress+'/pub/getCode?tk='+tk);
				flag=false;
			})
		
		
		
			//登录界面换一换
			$( "#loginIframe" ).contents().find( "span.huan" ).click(function(){
				tk+=1;
				$( "#loginIframe" ).contents().find( "span.huan").siblings('img').attr('src',dress+'/pub/getCode?tk='+tk)
			})
			
			$( "#loginIframe" ).contents().find( "input[name=loginName]" ).blur(function(){
				var _this = $(this);
				var nameTxt = $(this).val();
				if(nameTxt.indexOf('@')!=-1){
					checkEmail(nameTxt,_this);
				}else{
					checkMobile(nameTxt,_this);
				}		
			}).focus(function(){
				var _this = $(this);
				disapper(_this)
			});
		
			$( "#loginIframe" ).contents().find( ".valid img" ).attr('src',dress+'/pub/getCode?tk='+tk)
			$( "#loginIframe" ).contents().find( "input[name=password]" ).blur(function(){
				var _this = $(this);
				errorTex(_this)
			}).focus(function(){
				var _this = $(this);
				disapper(_this)
			})
	
		
			//登录页面
			$( "#loginIframe" ).contents().find( "input[name=submit]" ).click(function(){	
				var loginName = $( "#loginIframe" ).contents().find( "input[name=loginName]").val();
				var passWord = base64encode($( "#loginIframe" ).contents().find( "input[name=password]").val());			
				var code = $( "#loginIframe" ).contents().find( "input[name=code]").val();
				if(loginName==''){
					$( "#loginIframe" ).contents().find( "input[name=loginName]").siblings('em').text('请输入电子邮箱或手机号')
					return false
				}
				if(passWord==''){
					$( "#loginIframe" ).contents().find( "input[name=password]").siblings('em').text('请输入密码')
					return false
				}
				if(code==''){
					$( "#loginIframe" ).contents().find( "input[name=code]").siblings('em').text('请输入验证码')
					return false
				}
				$.ajax({
					url:dress+'/user/login',
					type:'post',
					data:{
						loginName:loginName,
						password:passWord,
						code:code,
						tk:tk
					},
	//				async:false,
					xhrFields:{
					    withCredentials:true
					},
					success:function(e){
						if(e.code!=200){
							tk+=1;
							$( "#loginIframe" ).contents().find('input[name='+e.data+']').siblings('em').text(e.message);
							$( "#loginIframe" ).contents().find( ".valid img" ).attr('src',dress+'/pub/getCode?tk='+tk);
						}else if(e.code==200){
							showTit('登录成功');
							localStorage.setItem('token',e.data.token);
							setTimeout(function(){
								history.back()
							},3000)
						}
					}
				})
			})
	 	},
	 	
	 	checkEmail: function(_this, val, tips) {
	 		var regEmail=/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	 		if(tools.checkSpace(_this, val, tips)){
	 			if(!(regEmail.test(val))){ 
			  	 	_this.siblings('em').text("邮箱输入有误！"); 
			   	 	return false; 
				}else{
					return true;
				}
	 		}
			return false;
	 	},
	 	
	 	checkTel: function(_this, val, tips) {
	 		if (tools.checkSpace(_this, val, tips)){
	 			var reg=/^1[3|4|5|7|8|9][0-9]\d{4,8}$/;//定义手机号正则表达式
				var rg=/^00886-|00852-/;
				if(/^00886-/.test(val)){
					if(val.length!=15){
						_this.siblings('em').text("港澳地区，电话位数按要求输入！");
						return false;
					}
				}else if(/^00852-/.test(val)){
					if(val.length!=14){
						_this.siblings('em').text("港澳地区，电话位数按要求输入！");
						return false;
					}	
				}else if(!(reg.test(val)) || val.length!=11){
			    	_this.siblings('em').text("电话输入有误。港澳地区，电话格式按要求输入！");
			    	return false;
	 			}	
	 			return true;
	 		}	 		
	 		return false;
	 	},
	 	
	 	checkPassword: function(_this, val, tips) {
	 		if(tools.checkSpace(_this, val, tips)){
	 			var reg=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{15,32}$/;
	 			if(!(reg.test(val))){ 
			  	 	_this.siblings('em').text("您输入的密码不符合要求，请按要求输入"); 
			   	 	return false; 
				}else{
					return true;
				}
	 		}
	 		return false;
	 	},
	 		 	
	 	checkSpace: function(_this,val,tips) {
	 		if (val.length === 0) {
	 			_this.siblings('em').text(tips);
	 			return false;
	 		} 
	 		return true;
	 	},
	 	
	 	checkAgree: function(_this) {
	 		if(_this.prop('checked')==false){
	 			$(".agree input[name=check]").siblings('span').find('em').text('请选择是否同意条款');
	 			return false;
	 		}else{
	 			$(".agree input[name=check]").siblings('span').find('em').text('');
	 			return true;
	 		}
	 	},
	 	
	 	checkCompony: function(_this, val, tips) {
	 		var reg = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]|-/i;
			regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
			if (tools.checkSpace(_this, val, tips)){	 		
	 			if(reg.test(val)|| regCn.test(val)){	
	 					_this.siblings('em').text("您输入的只能包含字母、汉字或数字");
					return false;
				}else{					
			    	return true;
				}				
	 		}		 		
	 		return false;
	 	},
	 	checkUserName: function(_this, val, tips) {
	 		var reg = /[1-9]|[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]|-/i;
			var regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;		
	 		if (tools.checkSpace(_this, val, tips)){	 		
	 			if(reg.test(val)|| regCn.test(val)){	
	 					_this.siblings('em').text("您输入的只能是字母或汉字");
					return false;
				}else{					
			    	return true;
				}				
	 		}		 		
	 		return false;
	 	},
	 	
	 	setScrollTop:function(obj) {
	 		var offTop = obj.offset().top;
			$('body,html').animate({ scrollTop: offTop-90 }, 800);
	 	},
	 	
	 	
	 	
	 	//获取输入框长度
	 	testInput:function(obj,length1){
			var val = obj.val();
			
			//获取字符长度
			var len = 0;  		
			for (var i=0; i<val.length; i++) {  
				if (val[i].match( /^[\u4E00-\u9FA5]{1,}$/)) {  
				 	len += 2;  
				} else {  
				    len ++;  
				} 			
			}  
			if(len>length1){
				obj.siblings('em').text('您输入的字符最多'+length1+'字节，请重新输入');
				flag=false;
			}
		},
	 	
	 	
	 	
	 	spaceMsg:{
	 		telTips: '手机号不能为空,请输入...',
	 	},
	 	
	 	checkMsg:function(_this, val, tips){
	 		var val = _this.val();
			var reg = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/i;
			regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
	 		if(reg.test(val)|| regCn.test(val)){	
	 			_this.siblings('em').text("您输入的只能包含字母、汉字或数字");
				return false;
			}else{					
			    return true;
			}		
	 	},
	 	
	 	getObjectURL: function() {
	 		var url = null ;   
	   	 	if (window.createObjectURL!=undefined) { // basic  
	        	url = window.createObjectURL(file) ;  
	   		 } else if (window.URL!=undefined) { // mozilla(firefox)  
	      		url = window.URL.createObjectURL(file) ;  
	   		 } else if (window.webkitURL!=undefined) { // webkit or chrome  
	     		url = window.webkitURL.createObjectURL(file) ;  
	    	}  
	   		return url ;  
	 	}	 		 	
	}
		
	
	
	//姓和名
	$("#register_form input[name=surname],#register_form input[name=name],input[name=username]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkUserName(_this, tex, '您输入的不能为空,请重新输入');
		tools.testInput(_this,26)
	}).focus(function(){
		$(this).siblings('em').text('');
	});
	
	//
	$("#register_form input[name=nickname]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkUserName(_this, tex, '您输入的不能为空,请重新输入');
		tools.testInput(_this,100)
	}).focus(function(){
		$(this).siblings('em').text('');
	});
	
	//邮箱
	$("input[name=email]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkEmail(_this, tex, '您输入的不能为空,请重新输入');
		tools.testInput(_this,100)
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//电话
	$("input[name=phone]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkTel(_this, tex, '您输入的不能为空,请重新输入');
	}).focus(function(){
		$(this).siblings('em').text('');
	});
	
	//密码
	$("#register_form input[name=password]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkPassword(_this, tex, '您输入的不能为空,请重新输入');
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//确认密码
	$("#register_form input[name=spassword]").blur(function(){
		var _this = $(this);
		var tex = _this.val();
		var passwordVal = $("input[name=password]").val();
		if(tex!=passwordVal){
			$(this).siblings('em').text('两次输入的密码不一样，请重新输入');
			$("#register_form input[name=password]").siblings('em').text('两次输入的密码不一样，请重新输入');
		}
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//公司/单位/学校名称
	$('input[name=company]').blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkCompony(_this, tex, '您输入的不能为空,请重新输入');
		tools.testInput(_this,100)
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//渠道
	$('input[name=channelMsg]').blur(function(){
		var _this = $(this);
		var tex = _this.val();
		var inputPlaceholder=$(this).attr('placeholder');
		if(tex==''){
			_this.siblings('em').text(inputPlaceholder);
			
		}
		tools.checkMsg(_this, tex, '您输入的不能为空,请重新输入');		
		tools.testInput(_this,50)
	}).focus(function(){
		$(this).parent().parent().parent().siblings().find('em').text('');
		$(this).parent().parent().siblings('input').prop('checked',true).siblings().find('.input_ipt input').attr('name','channelMsg');
		$(this).parents('li').siblings('.dWidth').find('.input_ipt input').removeAttr('name');
	})
	
	$("#register_form .z-type li").click(function(){
		var _this=$(this);
		_this.siblings().find('em').text('');
		if(_this.hasClass('dWidth')){
			_this.find('.input_ipt input').focus();
			_this.find('.input_ipt em').text('');
		}		
	})
	
	//城市/座机/地址/部门/职称
	$('input[name=city],input[name=seatPhone],input[name=address],input[name=dept],input[name=job]').blur(function(){
		var _this = $(this);
		var tex = _this.val();
		tools.checkMsg(_this, tex, '您输入的不能为空,请重新输入');
		tools.testInput(_this,100)
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//验证码
	$('input[name=ma]').blur(function(){
		var This = $(this);
		maFun(This);
	}).focus(function(){
		$(this).siblings('em').text('');
	})
	
	//注册按钮
	$("#register_form button[name=submit]").click(function(){
		var surname = $('input[name=surname]').val();
		var name = $('input[name=name]').val();
		var nickname = $('input[name=nickname]').val();
		var email = $('input[name=email]').val();
		var phone = $("input[name=phone]").val();
		var _password = $('input[name=password]').val();
		var spassword = $("input[name=spassword]").val();
		var company = $('input[name=company]').val();
		var ma = $("input[name=ma]").val();
		if (!tools.checkUserName($('input[name=surname]'), surname, '您输入的姓，不能为空'))  {	
			tools.setScrollTop($('input[name=surname]'));
			return;
		}
		
		if (!tools.checkUserName($('input[name=name]'), name, '您输入的名，不能为空'))  {	
			tools.setScrollTop($('input[name=name]'));
			return;
		}
		
//		if (!tools.checkUserName($('input[name=nickname]'), nickname, '您输入的昵称，不能为空'))  {	
//			tools.setScrollTop($('input[name=nickname]'));
//			return;
//		}
		
		if (!tools.checkEmail($('input[name=email]'), email, '您输入的邮箱，不能为空'))  {	
			tools.setScrollTop($('input[name=email]'));
			return;
		}
		
		if (!tools.checkTel($('input[name=phone]'), phone, '您输入的电话，不能为空'))  {	
			tools.setScrollTop($('input[name=phone]'));
			return;
		}
		
		if (!tools.checkPassword($('input[name=password]'), _password, '您输入的密码，不能为空'))  {	
			tools.setScrollTop($('input[name=password]'));
			return;
		}
		
		if (!tools.checkPassword($('input[name=spassword]'), spassword, '您输入的确认密码，不能为空'))  {	
			tools.setScrollTop($('input[name=spassword]'));
			return;
		}
		
		if (!tools.checkCompony($('input[name=company]'), company, '您输入的确认公司/学校/单位名称，不能为空'))  {	
			tools.setScrollTop($('input[name=company]'));
			return;
		}
		
		if (maFun($('input[name=ma]'))){	
			tools.setScrollTop($('input[name=ma]'));
			return;
		}
		
		if(!tools.checkAgree($('input[name=check]'))){
			tools.setScrollTop($('input[name=check]'));
			return;
		}
		
		
		$(".z-regisCon>form .agree span em,.z-regisCon .submit em").text('')
		var surname=$("input[name=surname]").val();
		var name=$("input[name=name]").val();
		var nickname=$("input[name=nickname]").val();
		var email= $("input[name=email]").val();
		var phone= $("input[name=phone]").val();
		var check_sub = $("input[name=check_sub]").attr("checked");
		var spassword= base64encode($("input[name=spassword]").val());
		$("input[name=spassword]").val(spassword);
		var w_password= base64encode($("input[name=password]").val());
		$("input[name=password]").val(w_password)
		var country=$("div[name=country]").text();
		var company=$("input[name=company]").val();
		var ma=$("input[name=ma]").val();
		var check=$("input[name=check]").val();		
		var formData = new FormData($( "#register_form" )[0]);
        formData.append("country",country);
        formData.append("code",ma);
        formData.append('tk',tk);
        if(check_sub=="checked"){
        	check_sub=1;
        }else{
        	check_sub=0;
        }
        formData.append("isSub",check_sub);  
        formData.append("check",check);
        formData.append("file",$("#file")[0]);
        formData.append('successUrl',_origin+":443/");
        formData.append('errorUrl',_origin+'/jump/error.html');
		$.ajax({
			url:dress+'/user/regist',
			type:'post',
			data: formData,
			processData: false,
			contentType: false,			
			success:function(data){
				if(data.code!=200){
					tk+=1;
					$("input[name=password]").val('');
					$("input[name=spassword]").val('');
					$("#register_form .z-ma img").attr('src',dress+'/pub/getCode?tk='+tk)
					tools.setScrollTop($('input[name='+data.data+']'));
					$('input[name='+data.data+']').siblings('em').text(data.message);
					flag=false;
				}else if(data.code==200){
					showTit('提交成功，稍后请到提交的邮箱里确认，否则无法登陆')
					setTimeout(function(){
						location.href=_origin;
					},3000)
				}
			}
		});
	});
	
	tools.init();
	



//密码找回，邮箱输入
$(document).on('click', '.forget .email_input', function() {
	var email = $(".forget input[name=email]").val();
	if (!tools.checkEmail($('input[name=email]'), email, '您输入的不能为空,请重新输入'))  {	
		return;
	}
	$.ajax({
		url:dress+'/user/forgetPassword',
		type:'post',
		data:{
			email:email,
			resetUrl:_origin+'/jump/reset.html',
			errorUrl:_origin+'/jump/error.html',
		},
		success:function(e){
			if(e.code!=200){
//				$(".fixBox").show();
				$(".forget .box .input_ipt em").text(e.message)					
			}else if(e.code==200){
				$(".fixBox").show();
				$(".fixBox .content").text('提交成功，请到您输入的邮箱里确认修改密码');										
				setTimeout(function(){
					$(".SolutionForm, .fixBox").hide();
					location.href=_origin;
				},3000)										
			}
		}
	})
})

/*//密码找回，输入密码
$(".forget .reset").click(function(){
	var Password=base64encode($('.forget input[name=password]').val());
	var sPassword=base64encode($('.forget input[name=spassword]').val());	
	var email=location.search.split('email=')[1];
	if (Password=='')  {	
		$('.forget input[name=password]').siblings('em').text('您输入的不能为空,请重新输入')
		return;
	}
	if (Password=='')  {	
		$('.forget input[name=spassword]').siblings('em').text('您输入的不能为空,请重新输入')
		return;
	}
	$.ajax({
		url:dress+'/user/resetPassword',
		type:'post',
		data:{
			password:Password,
			confirmPassword:sPassword,
			email:email,
			errorUrl:_origin+'/error.html'
		},
		success:function(e){
			if(e.code!=200){
//				$(".fixBox").show();
				$(".forget .box .input_ipt em").text(e.message)					
			}else if(e.code==200){
				$(".fixBox").show();
				$(".fixBox .content").text('修改成功，请重新登录');		
				localStorage.setItem('token','')
				setTimeout(function(){
//					$(".SolutionForm, .fixBox").hide();
					$(".fixBox").show();
					$(".fixBox .content").text('修改成功，稍后请重新登录');
					location.href=_origin;
				},3000)										
			}
		}
	})
})*/
});

window.onload= function(){
	 	$( "#loginIframe" ).contents().find( ".valid img" ).attr('src',dress+'/pub/getCode?tk='+tk);
	 		
		$( "#loginIframe" ).contents().find( "input[name=code]" ).blur(function(){
			var that =$(this);
			if($(this).val()==''){
				$(this).siblings('em').text('请输入验证码');
				return
			};
			var imgCon = $(this).val();
			maFun($(this));
			return false
		}).focus(function(){
			$(this).siblings('em').text('')
		})
		
		//登录界面换一换
		$( "#loginIframe" ).contents().find( "span.huan" ).click(function(){
			tk+=1;
			$( "#loginIframe" ).contents().find( "span.huan").siblings('img').attr('src',dress+'/pub/getCode?tk='+tk)
		})
		
		$( "#loginIframe" ).contents().find( "input[name=submit]" ).click(function(){	
			var loginName = $( "#loginIframe" ).contents().find( "input[name=loginName]").val();
			var passWord = base64encode($( "#loginIframe" ).contents().find( "input[name=password]").val());			
			var code = $( "#loginIframe" ).contents().find( "input[name=code]").val();
			if(loginName==''){
				$( "#loginIframe" ).contents().find( "input[name=loginName]").siblings('em').text('请输入电子邮箱或手机号')
				return false
			}
			if(passWord==''){
				$( "#loginIframe" ).contents().find( "input[name=password]").siblings('em').text('请输入密码')
				return false
			}
			if(code==''){
				$( "#loginIframe" ).contents().find( "input[name=code]").siblings('em').text('请输入验证码')
				return false
			}
//			if (!tools.checkTel($('input[name=loginName]'), loginName, '您输入的电话，不能为空'))  {	
//				return;
//			}
			$.ajax({
					url:dress+'/user/login',
					type:'post',
					data:{
						loginName:loginName,
						password:passWord,
						code:code,
						tk:tk
					},
					xhrFields:{
				    	withCredentials:true
					},
					success:function(e){
						if(e.code!=200){
							tk+=1;
							$( "#loginIframe" ).contents().find('input').siblings('em').text('')
							$( "#loginIframe" ).contents().find('input[name='+e.data+']').siblings('em').text(e.message);
							$( "#loginIframe" ).contents().find( ".valid img" ).attr('src',dress+'/pub/getCode?tk='+tk);
						}else if(e.code==200){
							showTit('登录成功')
							localStorage.setItem('token',e.data.token);
							setTimeout(function(){
								location.href=_origin;
							},1500)
						}
					}
				})
			})
}
	var _origin=window.location.origin;
	var _pathname=window.location.pathname.split("/")[2];
