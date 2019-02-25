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
<script type="text/javascript" src="<%=basePath%>/plugins/My97DatePicker/WdatePicker.js"></script>
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body>
			<div class="cms_con cf">
<%@ include file="../index/n_prenav.jsp"%>
			<form action="<%=adminPath%>happuser/editU.do" name="Form" id="Form" method="post">
				<input type="hidden" id="USER_ID" name="USER_ID" value="${pd.USER_ID}" />
				<div class="cms_c_list cf">
					<h3>编辑会员</h3>
					<div class="add_btn_con wrap cf">
					
						<dl class="cf">
							<dt><span class="hot">*</span>姓名</dt>
							<dd><div class="dd_con"><input type="text" id="NAME" name="NAME" value="${pd.NAME}" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>用户名</dt>
							<dd><div class="dd_con"><input type="text" id="USERNAME" name="USERNAME" value="${pd.USERNAME}" required/></div></dd>
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
							<dt>
								<span class="hot">*</span>开始日期
							</dt>
							<dd class="data_dd">
								<div class="dd_con">
									<div class="sle_data">
										<span><input id="d5221" name="START_TIME" class="Wdate " type="text" value="${pd.START_TIME}" onclick="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.click();},maxDate:'#F{$dp.$D(\'d5222\')}'})"/><i></i></span>
										<em>结束日期</em>
										<span><input id="d5222" name="END_TIME" class="Wdate " type="text" value="${pd.END_TIME}" onclick="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/><i></i></span>
										<div class="disable"></div>
									</div>
								</div>
							</dd>
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
							<dt>
							<span class="hot">*</span>状态
						</dt>
						<dd style="width: auto">
							<div class="dd_con">
								<a href="javascript:;">
									<select name="STATUS" id="STATUS" class="form-control" style="float:left; width:80px;">
										<option value="1" <c:if test="${pd.STATUS == '1' }">selected</c:if> >正常</option>
										<option value="0" <c:if test="${pd.STATUS == '0' }">selected</c:if> >冻结</option>
									</select>
								</a>
							</div>
						</dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="<%=adminPath%>happuser/listUsers.do"  class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>
<script type="text/javascript">
var isAllow=true;

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
	var ids="";
	$(":checkbox[name='selRoleck']:checked").each(function(i,obj){
		ids+=$(this).val()+",";
	});
	$("#ROLE_ID").val(ids);
	$('.layer_bg02').hide();
}

function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	if(!isAllow){
		return false;
	}
   $("#Form").submit();
}
</script>
</html>          