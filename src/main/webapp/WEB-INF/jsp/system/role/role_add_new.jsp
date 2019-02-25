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
<!-- cms_con开始 -->
			<div class="cms_con cf">
<!-- jsp导航返回栏 -->
<div class="cms_c_inst neirong cf">
	<div class="left cf">
		<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
		<a href="<%=adminPath%>role.do">角色管理</a><i>></i>
		<i>角色维护</i>
	</div>
</div>
			<form action="<%=adminPath%>role/save.do" name="Form" id="Form" method="post">
				<input type="hidden" id="ROLE_ID" name="roleId" value="${pd.ROLE_ID }" />
				<input type="hidden" id="STATE" name="state" value="${pd.STATE }" />
				<div class="cms_c_list cf">
					<h3>创建角色</h3>
					<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt><span class="hot">*</span>角色名称</dt>
							<dd><div class="dd_con"><input type="text" id="ROLE_NAME" name="roleName" value="${pd.ROLE_NAME }" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>系统权限</dt>
							<dd style="width:auto"><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn01">+选择系统权限</a><span class="warn_tip" style="display:none;"></span></div></dd>
							<dd>
								<div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">栏目</td><td>操作</td></tr></thead>
										<tbody id="relationBody">
										</tbody>
									</table>
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
												<textarea name="description" class="textarea_numb">${pd.DESCRIPTION}</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>role.do" class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg_other layer_bg07" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择系统权限</span><p class="close">x</p></h3>
							<div class="guanliquanxian cf">
								<h4>选择站点</h4>
								<div class="layer_list mScrol cf">
									<ul id="sitediv">
										<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的站点，请添加站点后，再操作</div>
									</ul>
								</div>
								
								<div class="all_checkbox"><label for="c_101"><input type="checkbox" onclick="clearMap();" id="c_101" /><span>全选</span><i></i></label></div>
							</div>
							<div class="guanlian_list cf">
								<h4>选择管理权限</h4>
								<div class="layer_list_other cf" style="overflow-y:scroll">
									<ul id="menudiv">
										<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的菜单，请添加菜单后，再操作</div>
									</ul>
								</div>
							</div>
							<div class="all_btn cf">
								<input type="button" class="submit_btn" onclick="setSiteMenu();" value="确定" />
								<a href="javascript:;" class="submit_re_btn" onclick="colseSite(this);">取消</a>
							</div>
						</div>
					</div>
				</div>
			</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->
</body>
<script type="text/javascript">
var siteMenuMap={};
var currentSiteId='';
var siteIdArry=new Array();
var isSite=false;
$(function(){
	$('.layer_btn01').click(function(){
	  	$('.layer_bg07').show();
    });
	setSitediv();
	setMenudiv();
});
//获取菜单树形结构
function setMenudiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"menu/getAllTree.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendMenudiv(result.data);
				defaultSiteMenu();
		       //$(".mScrol222").mCustomScrollbar("update");
			}
		}
	});
}

//选择菜单弹出内容填充
function appendMenudiv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label onchange="pushMenuIds(\''+obj.id+'\')" for="cc_'+obj.id+'"><input type="checkbox"  name="selMenuck" value="'+obj.id+'" data-type="'+obj.attribute.nodeType+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#menudiv").html(html);
	 //控制折叠按钮是否显示
     $('.layer_list_other li .show_btn').each(function(){
    	if($(this).parent('p').next('dl').size()==0){
    		$(this).hide();
    	}
     });
     //$(".mScrol222").mCustomScrollbar("update");
}

function eachSubList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label onchange="pushMenuIds(\''+obj.id+'\')" for="cc_'+obj.id+'"><input type="checkbox" name="selMenuck" value="'+obj.id+'" data-type="'+obj.attribute.nodeType+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

//获取站点树形结构
function setSitediv(){
	$.ajax({
		type: "GET",
		url:adminPath+"site/getAllSite.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendSitediv(result.data);
			}
		}
	});
}

//选择站点弹出内容填充
function appendSitediv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label><input type="checkbox" name="selSiteck" onclick="changeMenu(\''+obj.siteId+'\')" value="'+obj.siteId+'" id="ck_'+obj.siteId+'" /><i></i></label><span onclick=changeMenu(\''+obj.siteId+'\')>'+obj.siteName+'</span></p>';
		 html+='</li>';
		 siteIdArry.push(obj.siteId);
	 });
	 $("#sitediv").html(html);
}

function setSiteMenu(){
	//var names=new Array();
	var bodyHtml='';
	$(":checkbox[name='selSiteck']:checked").each(function(i,obj){
		//names.push($(this).parent().next("span").text());
		bodyHtml+='<tr id="heheda">';
		bodyHtml+='<td><i class="special">'+$(this).parent().next("span").text()+'</i></td>';
		bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteSite(this)" data-id="'+$(this).val()+'" class="remove_file">移除</a></td>';
		bodyHtml+='</tr>';
	});
	//$(".warn_tip").show();
	//$(".warn_tip").html(names.join(","));
	if(bodyHtml!=''){
		$("#relationBody").html(bodyHtml);
		var isSelectPermis=false;
		
		for(var key in siteMenuMap){  
			if(siteMenuMap[key]!=null&&siteMenuMap[key].length>0){
				isSelectPermis=true;
			}
		}
		if(isSelectPermis){
			$(".warn_tip").html("");
		}
	}
	$('.layer_bg07').hide();
}

//切换站点对应的菜单权限
function changeMenu(siteId){
	//如果有站点没有选中，则取消全选状态,并清除其站点数组对象
	if(!$("#ck_"+siteId).is(":checked")){
		$("#c_101").attr("checked",false);
		$("#c_101").siblings('i').removeClass('active');
		delete siteMenuMap[siteId];
	}
	
	//如果全选，则中断执行
	if($("#c_101").is(":checked")){
		return false;
	}
	//如果此站点未选中，则中断执行
	if(!$("#ck_"+siteId).is(":checked")){
		return false;
	}
	
	//如果此站点对应的存储菜单id的Map为空，则创建
	currentSiteId=siteId;
	var menuArry=siteMenuMap[siteId];
	if(menuArry==null||menuArry==undefined){
		menuArry=new Array();
		siteMenuMap[siteId]=menuArry;
	}
	
	//先清除菜单的选中的状态
	$(":input[name='selMenuck']").each(function(){
		var ckId=$(this).attr("id");
		if($(this).siblings('i').hasClass('active')){
			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
			return true;
		}
	});
	
	//重置此站点对应的菜单的选中状态
	$.each(menuArry,function(index,obj){
		var idArry=obj.split(",");
		var dataId="cc_"+idArry[0];
		$(":input[name='selMenuck']").each(function(){
			var ckId=$(this).attr("id");
			if(ckId==dataId && $(this).siblings('i').hasClass('active')==false){
				$(this).attr("checked","checked");
				$(this).siblings('i').addClass('active');
				return true;
			}
		});
	});
}

//站点-菜单映射数据push，由菜单点机事件触发
function pushMenuIds(menuId){
	var length=$(":checkbox[name='selSiteck']:checked").length;
	if(length<=0){
		$("#cc_"+menuId).prop("checked")==false;
		window.top.mesageTip("warn","请先选择站点!");
		return false;
	}
	
	//此节点与其子节点的状态一起变动
	if($("#cc_"+menuId).prop("checked")==true){
		$("#cc_"+menuId).parent().parent().parent().find('input').prop("checked",true);
		$("#cc_"+menuId).parent().parent().parent().find('i').addClass('active');
	}else{
		$("#cc_"+menuId).parent().parent().parent().find('input').prop("checked",false);
		$("#cc_"+menuId).parent().parent().parent().find('i').removeClass('active');
	}
	
	//全选
	if($("#c_101").is(":checked")){
		$.each(siteIdArry,function(index,obj){
			var menuArry=siteMenuMap[obj];
			if(menuArry==null||menuArry==undefined){
				menuArry=new Array();
				siteMenuMap[obj]=menuArry;
			}
			
			$("#cc_"+menuId).parents('li').find('input').each(function(){
				if($(this).is(":checked")){
					menuArry.push($(this).val()+","+$(this).data("type"));
				}else{
					removeArryByValue(menuArry,$(this).val()+","+$(this).data("type"));
				}
			});
			
		});
		return false;
	}
	//添加映射关系
	if(siteMenuMap[currentSiteId]==null
			||siteMenuMap[currentSiteId]==undefined){
		return false;
	}
	
	$("#cc_"+menuId).parents('li').find('input').each(function(){
		removeArryByValue(siteMenuMap[currentSiteId],$(this).val()+","+$(this).data("type"));
		if($(this).is(":checked")){
			siteMenuMap[currentSiteId].push($(this).val()+","+$(this).data("type"));
		}
		//else{
			//removeArryByValue(siteMenuMap[currentSiteId],$(this).val());
		//}
	});
	
}

//如果全选取消，则清除Map
function clearMap(){
	if(!$("#c_101").is(":checked")){
		for(var key in siteMenuMap){
			delete siteMenuMap[key];
		}
		$(":input[name='selMenuck']").each(function(){
			if($(this).siblings('i').hasClass('active')==true){
				$(this).attr("checked",false);
				$(this).siblings('i').removeClass('active');
			}
		});
	}
}

//默认选中菜单
function defaultSiteMenu(){
	var ROLE_ID=$("#ROLE_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"role/getPermissionByRoleId.do",
		data:{"roleId":ROLE_ID},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				var data=result.data;
				//填充映射map数据
				$.each(data,function(index,obj){
					var menuArry=siteMenuMap[obj.siteId];
					if(menuArry==null||menuArry==undefined){
						menuArry=new Array();
						siteMenuMap[obj.siteId]=menuArry;
					}
					siteMenuMap[obj.siteId].push(obj.menuId+","+obj.type);
				});
				//选中站点
				var fisrtSiteId='';
			 	//var names=new Array();
			 	var bodyHtml='';
			 	var isSelected=false;
				for(var key in siteMenuMap){
					var dataId="ck_"+key;
					$(":input[name='selSiteck']").each(function(){
						var ckId=$(this).attr("id");
						if(fisrtSiteId==''){
							fisrtSiteId=key;
						}
						if(ckId==dataId){
							$(this).attr("checked","checked");
							$(this).siblings('i').addClass('active');
							//names.push($(this).parent().next("span").text());
							bodyHtml+='<tr id="heheda">';
							bodyHtml+='<td><i class="special">'+$(this).parent().next("span").text()+'</i></td>';
							bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteSite(this)" data-id="'+key+'" class="remove_file">移除</a></td>';
							bodyHtml+='</tr>';
							isSelected=true;
						}
					});
				}
				if(isSelected){
					$("#relationBody").html(bodyHtml);
				}
				changeMenu(fisrtSiteId);
				//$(".warn_tip").show();
				//$(".warn_tip").html(names.join(","));
			}
		}
	});
}

function colseSite(obj){
    /*$('.layer_list li label').removeClass('radio_btn');
    $(obj).parents('.layer_bg').find('input').removeAttr("checked");
    $(obj).parents('.layer_bg').find('i').removeClass('active');
  	$(".warn_tip").html("");
	for(var key in siteMenuMap){
		delete siteMenuMap[key];
	}*/
  	$(obj).parents('.layer_bg').hide();
}


function deleteSite(obj){
	$(obj).parents("tr").remove();
	var value=$(obj).data('id');
	var selectedId="ck_"+value;
	//先清除菜单的选中的状态
	$(":input[name='selSiteck']").each(function(){
		var ckId=$(this).attr("id");
		if(selectedId==ckId){

			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
			return true;
		}
	});
	$.each(siteMenuMap[value],function(index,item){
		var opId="cc_"+item;
		$(":input[name='selMenuck']").each(function(){
			var id=$(this).attr("id");;
			if(opId==id&&$(this).siblings('i').hasClass('active')==true){
				$(this).attr("checked",false);
				$(this).siblings('i').removeClass('active');
				return true;
			}
		});
	});
	delete siteMenuMap[value];
}

function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	var isSelectPermis=false;
	for(var key in siteMenuMap){  
		if(siteMenuMap[key]!=null&&siteMenuMap[key].length>0){
			isSelectPermis=true;
		}else{
			delete siteMenuMap[key];
		}
	}

	if(!isSelectPermis){
		$(".warn_tip").html("请设定权限");
		$(".warn_tip").show();
		return false;
	}
	
    var formData = $("#Form").jsonObject();
    formData.siteMenuMap=siteMenuMap;
	$.ajax({
		type: "POST",
		url:adminPath+"role/save.do",
		data:JSON.stringify(formData),
		dataType:'json',
		cache: false,
		contentType:'application/json;charset=utf-8',
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='0'){//添加
					 location.href=adminPath+'role.do';
				 }else{
					 location.href=adminPath+'role/toAdd.do';
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	});
}
</script>
</html>          
