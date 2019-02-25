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
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body>
			<div class="cms_con cf">
<%@ include file="../index/n_prenav.jsp"%>
			<form action="<%=adminPath%>user/saveU.do" name="Form" id="Form" method="post">
				<input type="hidden" id="USER_ID" name="USER_ID" value="${pd.USER_ID}" />
				<div class="cms_c_list cf">
					<h3>创建管理员</h3>
					<div class="add_btn_con wrap cf">
					<c:if test="${ empty pd.USER_ID}">
						<dl class="cf">
							<dt><span class="hot">*</span>管理员账号</dt>
							<dd><div class="dd_con"><input type="text" id="USERNAME" name="USERNAME" value="${pd.USERNAME}" required onblur="checkUserName();"/><span class="warn_tip" style="display:none;"></span></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>密码</dt>
							<dd><div class="dd_con"><input type="password" id="PASSWORD" name="PASSWORD" value="${pd.PASSWORD}"/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>确认密码</dt>
							<dd><div class="dd_con"><input type="password" id="PASSWORDAgain" name="PASSWORDAgain" value="${pd.PASSWORD}"/></div></dd>
						</dl>
					</c:if>
						<dl class="cf">
							<dt><span class="hot">*</span>姓名</dt>
							<dd><div class="dd_con"><input type="text" id="NAME" name="NAME" value="${pd.NAME}" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>电话</dt>
							<dd><div class="dd_con"><input type="text" id="PHONE" name="PHONE" value="${pd.PHONE}" onblur="checkPhone();" class="required tel"/><span class="warn_tip" style="display:none;"></span></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>邮箱</dt>
							<dd><div class="dd_con"><input type="text" id="EMAIL" name="EMAIL" value="${pd.EMAIL}" onblur="checkEmail();" class="required email"/><span class="warn_tip" style="display:none;"></span></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>备注</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea name="BZ" class="textarea_numb">${pd.BZ}</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>角色</dt>
							<dd style="width:auto"><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn01">+选择角色</a><span class="warn_tip"></span></div></dd>
							<input type="hidden" id="ROLE_ID" name="ROLE_ID" value="${pd.ROLE_ID}"/>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>user/listUsers.do"  class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择系统权限</span><p class="close">x</p></h3>
								<div class="layer_list_other mScrol222 cf">
									<ul id="rolediv">
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="setRoleId();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);" onclick="colseRole(this);" class="submit_re_btn">取消</a>
								</div>
						</div>
					</div>
				</div>
			</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>
<script type="text/javascript">
var isAllow=true;
$(function(){
	$('.layer_btn01').click(function(){
	  	$('.layer_bg02').show();
    });
	setRolediv();
});
//获取角色树形结构
function setRolediv(){
	$.ajax({
		type: "GET",
		url:adminPath+"role/getAllRole.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendRolediv(result.data);
				defaultRole();
			}
		}
	});
}

//选择角色弹出内容填充
function appendRolediv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.role_ID+'"><input type="checkbox" name="selRoleck" value="'+obj.role_ID+'" id="cc_'+obj.role_ID+'" /><span>'+obj.role_NAME+'</span><i></i></label></p>';
		 html+='</li>';
	 });
	 $("#rolediv").append(html);
	 //控制折叠按钮是否显示
     $('.layer_list_other li .show_btn').each(function(){
    	if($(this).parent('p').next('dl').size()==0){
    		$(this).hide();
    	}
     });
}

//默认选中角色
function defaultRole(){
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"role/getRoleIdsByUserId.do",
		data:{"userId":userId},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				var ids=new Array();
				var roleName=new Array();
				$.each(result.data,function(i,obj){
					var defaultId="cc_"+obj;
					$(":input[name='selRoleck']").each(function(){
						var ckId=$(this).attr("id");
						if(ckId==defaultId){
							ids.push(obj);
							roleName.push($(this).next().text());
							$(this).attr("checked","checked");
							$(this).siblings('i').addClass('active');
							$(this).parents('li').addClass('tog_san');
							var This = $(this).parents('li');
							This.find('dl').stop().slideDown();
							return false;
						}
					});
				});
				$("#ROLE_ID").val(ids.join(','));
				$(".warn_tip").html(roleName.join(','));
			}
		}
	});
	
}

//判断用户名是否存在
function checkUserName(){
	var USERNAME=$("#USERNAME").val();
	if(USERNAME==''||USERNAME==undefined||USERNAME==null){
		return false;
	}
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"user/hasU.do",
		data:{"USER_ID":userId,"USERNAME":USERNAME},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				if(result.data=='0'){
					isAllow=false;
					$("#USERNAME").next().html("此用户名已存在");
					$("#USERNAME").next().show();
				}else{
					isAllow=true;
					$("#USERNAME").next().html("");
					$("#USERNAME").next().hide();
				}
			}
		}
	});
}

//判断邮箱是否存在
function checkEmail(){
	var email=$("#EMAIL").val();
	if(email==''||email==undefined||email==null){
		return false;
	}
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"user/hasE.do",
		data:{"USER_ID":userId,"EMAIL":email},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				if(result.data=='0'){
					isAllow=false;
					$("#EMAIL").next().html("此邮箱已存在");
					$("#EMAIL").next().show();
				}else{
					isAllow=true;
					$("#EMAIL").next().html("");
					$("#EMAIL").next().hide();
				}
			}
		}
	});
}

//判断手机号是否存在
function checkPhone(){
	var phone=$("#PHONE").val();
	if(phone==''||phone==undefined||phone==null){
		return false;
	}
	var userId=$("#USER_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"user/hasP.do",
		data:{"USER_ID":userId,"PHONE":phone},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				if(result.data=='0'){
					isAllow=false;
					$("#PHONE").next().html("此手机号已存在");
					$("#PHONE").next().show();
				}else{
					isAllow=true;
					$("#PHONE").next().html("");
					$("#PHONE").next().hide();
				}
			}
		}
	});
}

function setRoleId(){
	var ids=new Array();
	var names=new Array();
	$(":checkbox[name='selRoleck']:checked").each(function(i,obj){
		ids.push($(this).val());
		names.push($(this).next().text());
	});
	$("#ROLE_ID").val(ids.join(","));
	$(".warn_tip").html(names.join(","));
	$('.layer_bg02').hide();
}

function colseRole(obj){
    $(obj).parents('.layer_bg').find('input').removeAttr("checked");
    $(obj).parents('.layer_bg').find('i').removeClass('active');
  	$(".warn_tip").html("");
  	$("#ROLE_ID").val("");
  	$(obj).parents('.layer_bg').hide();
}

function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	var roleId=$("#ROLE_ID").val();
	if(roleId==undefined||roleId==""||roleId==null){
		$(".warn_tip").html("请选择角色");
		return false;
	}
	if(!isAllow){
		return false;
	}
    var formData = $("#Form").jsonObject();
	$.ajax({
		type: "POST",
		url:adminPath+"user/saveU.do",
		data:formData,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='1'){//继续添加
					 location.href=adminPath+'user/goAddU.do';
				 }else{
					 location.href=adminPath+'user/listUsers.do';
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	});
}
</script>
</html>          