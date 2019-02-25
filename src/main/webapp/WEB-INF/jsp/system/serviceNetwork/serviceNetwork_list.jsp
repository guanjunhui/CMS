<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select {
	height: 80px;
	width: 140px;
	overflow-y: auto;
}

.div_select>.label {
	display: block;
}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function(){
		/* 搜索  */
		top.hangge();
		$("#ss").click(function(){
			var key=$("#key").val();
			$("#keywords").val(key);
			$("#Form").submit();
		});
		$("#shortSelect").change(function(){
			var shorts=$("#shortSelect").val();
			var $id=$("#mySelect").val();
			if(shorts=="CREATED_TIME"){
			window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?CREATED_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="UPDATE_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?UPDATE_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="TOP_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?TOP_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="RECOMMEND_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?RECOMMEND_TIME="+shorts+"&colum_id="+$id;
			}else{
				return false;
			}
			
		});
		/* 选择类型 */
		$("#mySelect").change(function(){
			var $id=$("#mySelect").val();
			var shorts=$("#shortSelect").val();
			if(shorts=="CREATED_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?CREATED_TIME="+shorts+"&colum_id="+$id;
				}else if(shorts=="UPDATE_TIME"){
					window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?UPDATE_TIME="+shorts+"&colum_id="+$id;
				}else if(shorts=="TOP_TIME"){
					window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?TOP_TIME="+shorts+"&colum_id="+$id;
				}else if(shorts=="RECOMMEND_TIME"){
					window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?RECOMMEND_TIME="+shorts+"&colum_id="+$id;
				}else{
					window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?colum_id="+$id
				}
		});
	});
	
	//当批量删除的时候提示是否删除
	function deleteChecks(){
		var $ids=$("input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要删除的服务网点信息!");
			return false;
		}
		var ids=[];
		var param="id=";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		var	title='确认删除所选服务网点信息吗？';
		var	content='此操作会删除选中的服务网点信息,请谨慎操作!';
		mesageConfirm('删除服务网点信息',title,content,"batchDel('"+param+"')");
	}

function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选服务网点信息吗？';
	var	content='此操作会删除该服务网点信息';
	mesageConfirm('删除服务网点信息',title,content,"batchDel('"+param+"')");
}
function batchDel(param){
	hideConfirm();
	$.ajax({
		url:adminPath+"serviceNetwork/delAllServiceNetwork.do",
		type:"get",
		data:param,
		success:function(result){
			if(result.success){
				window.top.mesageTip("success","操作成功");
				
				window.location.href=adminPath+"serviceNetwork/serviceNetworkListPage.do";
			}else{
				window.top.mesageTip("failure","操作失败");
			}
		}
	});
}
function updateById(data){
	var expires=null;
	var currentPage=${page.currentPage};
	document.cookie ="SNcurrentPage=" + escape(currentPage)+((expires == null) ? "" : ("; expires=" +LargeExpDate.toGMTString()))+";path=/";
	window.location.href=adminPath+"serviceNetwork/toUpdateServiceNetwork.do?id="+data; 
}
//推荐
function tj(id,recommend){
	$.ajax({
		type: "POST",
		url:adminPath+"contentData/updataRecommend.do",
		data:{ids:id,recommend:recommend},
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"contentData/contentlistPage.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
//置顶
function zd(id,top){
	$.ajax({
		type: "POST",
		url:adminPath+"contentData/updataTop.do",
		data:{ids:id,top:top},
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"contentData/contentlistPage.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
//批量置顶
function updataTop(top){
	var $ids=$("#did input[type='checkbox']:checked");
	if($ids.length==0){
		window.top.mesageTip("warn","请选择置顶的选项");
		return;
	}
	var ids=[];
	var param="ids=";
	$.each($ids,function(index,obj){
		ids.push(obj.value);
	});
	param+=ids.join("&ids=");
	param+="&top="+top;
	$.ajax({
		type: "POST",
		url:adminPath+"contentData/updataTop.do",
		data:param,
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"contentData/contentlistPage.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
//批量推荐
function updataRecommends(recommend){
	var $ids=$("#did input[type='checkbox']:checked");
	if($ids.length==0){
		window.top.mesageTip("warn","请选择推荐的选项");
		return;
	}
	var ids=[];
	var param="ids=";
	$.each($ids,function(index,obj){
		ids.push(obj.value);
	});
	param+=ids.join("&ids=");
	param+="&recommend="+recommend;
	$.ajax({
		type: "POST",
		url:adminPath+"contentData/updataRecommend.do",
		data:param,
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"contentData/contentlistPage.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
</script>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <i>服务网点管理</i>
			</div>
		</div>

		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<a href="<%=adminPath%>serviceNetwork/toAddServiceNetwork.do">+添加服务网点</a>
					<a href="<%=adminPath%>excel/jumpImportNetwork.do" style="margin-left:20px;">导入服务网点</a>
					<%-- <a href="<%=adminPath%>serviceNetwork/toMapDemo.do">跳转到地图测试页面</a>
					<a href="<%=adminPath%>serviceNetwork/toMapMoveDemo.do">跳转到移动地图测试页面</a> --%>
				</div>

				<div class="selec_co cf">
					<div class="search">
						<input type="text" placeholder="请输入关键字" id="key"><input
							id="ss" type="button" value="搜索" />
					</div>
				</div>

			</div>
			<div class="table cf">
				<dl class="list_bg col_06 cf" id="did">
					<!-- 标题 -->
					<dt class="cf">
						<div class="tit6">
							<div class="tit6_con">服务网点名称</div>
						</div>
						<div class="sele06">操作</div>
						<div class="sele06">创建时间</div>
						<div class="sele06">电话</div>
						<div class="sele06 other_width">地址</div>
						<div class="sele06">所属国家</div>
					</dt>
					
					<form id="Form"
						action="<%=adminPath%>serviceNetwork/serviceNetworkListPage.do" method="post">
						<input type="hidden" name="keywords" id="keywords" />
						<c:if test="${!empty serviceNetworkList }">
							<c:forEach items="${serviceNetworkList }" var="map">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label><input type="checkbox" value="${map.id }" /><i></i></label>
												${map.name }
											</div>
										</div>
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a onclick="updateById('${map.id }')"
													href="javascript:void(0)">编辑</a></li>
												<li><a onclick="deleteById('${map.id }')"
													href="javascript:;">删除</a></li>
											</ul>
										</div>
										<!-- 创建时间 -->
										<div class="sele06">
											<c:if test="${not empty map.createdTime}">
												<fmt:parseDate value="${map.createdTime }" var="yearMonth" pattern="yyyy-MM-dd" />
												<fmt:formatDate value="${yearMonth}" pattern="yyyy-MM-dd" />
											</c:if>
										</div>
									 	<!-- 电话 -->
										<div class="sele06">
											<c:if test="${not empty map.phone}">${map.phone}</c:if>
										</div> 
										<!-- 地址 -->
										<div class="sele06 other_width">
											<c:if test="${not empty map.address}">${map.address}</c:if>
										</div> 
										<!-- 所属大洲 -->
										<div class="sele06">
											<c:if test="${not empty map.countryEN}">${map.countryEN}</c:if>
										</div>
										
									</div>
								</dd>
							</c:forEach>
						</c:if>
						<c:if test="${empty serviceNetworkList }">
							<div
								style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">对不起,没有相关数据</div>
						</c:if>
					</form>
				</dl>
				<div class="bottom_con cf">
					<div class="all_checkbox" style="width: 250px">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
<!-- 						<a id="zd" class="zhiding" href="javascript:;" onclick="updataTop(1)">置顶</a> 
						<a id="tj" class="tuijian" href="javascript:;" onclick="updataRecommends(1)">推荐</a>  -->
						<a id="sc" href="javascript:;" onclick="deleteChecks()">批量删除</a>
					</div>
					<div class="page_list cf">${page.pageStr}</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">© 中企高呈 版权所有</div>
	<!-- cms_con结束 -->
</body>
</html>