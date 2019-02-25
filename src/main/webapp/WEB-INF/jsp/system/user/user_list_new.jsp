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
		<form action="<%=adminPath%>user/listUsers.do" method="post" name="Form" id="Form">
		<div class="cms_con cf">
<%@ include file="../index/n_prenav.jsp"%>
			<div class="cms_c_list biaoge_con juese_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>user/goAddU.do" >+创建管理员</a>
					</div>
				</div>
				<div class="table cf">
					<dl class="list_bg col_07 cf">
						<dt class="cf">
							<div class="tit7">
								<div class="tit7_con">
									管理员
								</div>
							</div>
							<div class="sele07">操作</div>
							<div class="sele07">状态</div>
							<div class="sele07">最后登录时间</div>
							<div class="sele07">角色</div>
							<div class="sele07 other_width">备注</div>
							<div class="sele07">姓名</div>
						</dt>
						<c:choose>
							<c:when test="${not empty userList}">
								<c:forEach items="${userList }" var="user">
									<dd class="cf">
										<div id="div_${user.USER_ID}" class="dd_tit cf">
											<div class="tit7">
												<div class="tit7_con">
													${user.USERNAME}
												</div>
											</div>
											<div class="sele07 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<li><a href="javascript:;" onclick="openRole('${user.USER_ID}')">权限</a></li>
													<li><a href="<%=adminPath%>user/goAddU.do?USER_ID=${user.USER_ID}">编辑</a></li>
													<c:choose>
														<c:when test="${user.STATUS == 1 }">
															<li><a href="<%=adminPath%>user/changeStatus.do?USER_ID=${user.USER_ID}&STATUS=${user.STATUS}">禁用</a></li>
														</c:when>
														<c:otherwise>
															<li><a href="<%=adminPath%>user/changeStatus.do?USER_ID=${user.USER_ID}&STATUS=${user.STATUS}">启用</a></li>
														</c:otherwise>
													</c:choose>
													
													<li><a href="javascript:void(0);" onclick="confirmDelDiv('${user.USERNAME}','${user.USER_ID}')">删除</a></li>
												</ul>
											</div>
											<div class="sele07 hide_font">
												<c:choose>
													<c:when test="${user.STATUS == 1 }">
														<div class="sele04">启用</div>
													</c:when>
													<c:otherwise>
														<div class="sele04">禁用</div>
													</c:otherwise>
												</c:choose>
											</div>
											<div class="sele07">
												<c:if test="${not empty user.LAST_LOGIN}">
													<fmt:parseDate value="${user.LAST_LOGIN}" var="yearMonth" pattern="yyyy-MM-dd"/>
													<fmt:formatDate value="${yearMonth}" pattern="yyyy-MM-dd" />
												</c:if>
											</div>
											<div class="sele07">${user.ROLE_NAME} </div>
											<div class="sele07 other_width">${user.bz} </div>
											<div class="sele07">${user.NAME} </div>
										</div>
									</dd>
								</c:forEach>
							</c:when>
						</c:choose>
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
							<input type="hidden" id="userIdGlobal" value=""/>
						</div>
					</dl>
					<div class="bottom_con cf">
						<div class="page_list cf">
							${page.pageStr}
						</div>
					</div>
				</div>
				</div>
			</div>
			</form>
			<div class="footer">© 中企高呈 版权所有</div>
		</div>
<script type="text/javascript">
$(function(){
	top.hangge();
	setRolediv();
});
function confirmDelDiv(name,id){
	var	title='确认删除管理员【'+name+'】的信息吗？';
	var	content='此操作会删除该用户的信息';
	mesageConfirm('删除用户信息',title,content,"delUser('"+id+"')");
}

function delUser(id){
	location.href='<%=adminPath%>user/deleteU.do?USER_ID='+id;
}

//选择权限
function openRole(userId){
	$.ajax({
		type: "GET",
		url:adminPath+"role/getRoleIdsByUserId.do",
		data:{"userId":userId},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				//重置此站点对应的菜单的选中状态
				$(":input[name='selRoleck']").each(function(){
					if($(this).siblings('i').hasClass('active')==true){
						$(this).attr("checked",false);
						$(this).siblings('i').removeClass('active');
						$(this).parents('li').removeClass('tog_san');
					}
				});
				//设置默认已选择权限
				$.each(result.data,function(index,obj){
					var dataId="cc_"+obj;
					$(":input[name='selRoleck']").each(function(){
						var ckId=$(this).attr("id");
						if(ckId==dataId && $(this).siblings('i').hasClass('active')==false){
							$(this).attr("checked","checked");
							$(this).siblings('i').addClass('active');
							$(this).parents('li').addClass('tog_san');
							var This = $(this).parents('li');
							This.find('dl').stop().slideDown();
							return true;
						}
					});
				});
			}
		}
	});
	$("#userIdGlobal").val(userId);
	$('.layer_bg02').show();
}
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
     $('.layer_list_other li .show_btn').each(function(){
     	if($(this).parent('p').next('dl').size()==0){
     		$(this).hide();
     	}
     });
}

function setRoleId(){
	var roleIds="";
	var cktext="";
	$(":checkbox[name='selRoleck']:checked").each(function(i,obj){
		roleIds+=$(this).val()+",";
		cktext+=$(this).next("span").text()+",";
	});
	if(cktext.lastIndexOf(',')!=-1){
		cktext=cktext.substring(0,cktext.length-1);
	}
	$('.layer_bg02').hide();
	var userId=$("#userIdGlobal").val();
	if(roleIds==null||roleIds==undefined||roleIds=='') return false;
	if(userId==null||userId==undefined||userId=='') return false;
	$.ajax({
		type: "GET",
		url:adminPath+"user/changeRole.do",
		data:{"userId":userId,"roleIds":roleIds},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				window.top.mesageTip("success","权限设置成功!");
				$("#div_"+userId).children("div").eq(4).html(cktext)
			}
		}
	});

}

function colseRole(obj){
    $(obj).parents('.layer_bg').find('input').removeAttr("checked");
    $(obj).parents('.layer_bg').find('i').removeClass('active');
  	$(obj).parents('.layer_bg').hide();
}

</script>
</body>
</html>          