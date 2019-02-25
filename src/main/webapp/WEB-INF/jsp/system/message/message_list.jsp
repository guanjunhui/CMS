<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = "//"
			+ request.getServerName()
			+ path + "/";
	String adminPath = (String)application.getAttribute("adminPath");
	String imgPath = (String)application.getAttribute("imgPath");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select{height:80px;width:140px;overflow-y:auto;}
.div_select>.label{display:block;}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function(){
		top.hangge();
		$("#shortSelect").change(function(){
			var shorts=$("#shortSelect").val();
			if(shorts=="UPDATE_DESC"){
			window.location.href=adminPath+"mymessage/sortlistPage.do?UPDATE_DESC="+shorts;
			}else if(shorts=="HOT_DESC"){
				window.location.href=adminPath+"mymessage/sortlistPage.do?HOT_DESC="+shorts;
			}else if(shorts=="TOP_DESC"){
				window.location.href=adminPath+"mymessage/sortlistPage.do?TOP_DESC="+shorts;
			}else if(shorts=="RECOMMEND_DESC"){
				window.location.href=adminPath+"mymessage/sortlistPage.do?RECOMMEND_DESC="+shorts;
			}else{
				return false;
			}
			
		});
		/* 选择类型 */
		$("#mySelect").change(function(){
			var $id=$("#mySelect").val();
			window.location.href=adminPath+"mymessage/messagelistPage.do?type_id="+$id; 
		});
		/* 根据关键词搜索  */
		$("#ss").click(function(){
			var $id=$("#key").val();
			$id=$.trim($id);
			window.location.href=adminPath+"mymessage/list.do?message_keyword="+encodeURI(encodeURI($id)); 
		
		});
	});
	
	//当批量删除的时候提示是否删除
	function delectChecks(){
		var $ids=$("input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要删除的资讯!");
			return false;
		}
		var	title='确认删除选中的资讯吗?';
		var	content='此操作会删除选中的资讯信息,请谨慎操作!';
		mesageConfirm('删除资讯',title,content,"deleteByIds()");
	}
	//批量删除功能
	function deleteByIds(){
		var $ids=$("input[type='checkbox']:checked");
		var ids=[];
		var param="id=";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		window.location.href=adminPath+"mymessage/delete.do?"+param;
	}
	
	
	
	//单个删除功能
	function deleteById(data){
		window.location.href=adminPath+"mymessage/delete.do?id="+data;
	}
	
	//当删除的时候提示是否删除
	function confirmDelDiv(data){
		var	title='确认删除该资讯吗?';
		var	content='此操作会删除该资讯信息,请谨慎操作!';
		mesageConfirm('删除资讯',title,content,"deleteById('"+data+"')");
	}
	//使正在显示的资讯改为隐藏
	function isHidden(data){
		if(data==null){
			return false;
		}
		//alert(data);
		 window.location.href=adminPath+"mymessage/updateStatus.do?status=0&id="+data; 
	}
	//使正在隐藏的资讯改为显示
	function isView(data){
		if(data==null){
			return false;
		}
		//alert(data);
		window.location.href=adminPath+"mymessage/updateStatus.do?status=1&id="+data; 
	}
	function updateById(data){
		//alert(data);
		if(data==0){
			return false;
		}
		window.location.href=adminPath+"mymessage/toEdit.do?id="+data; 
	}
	//将部分信息导出到Excel中
	function exportPartExcel(){
		var $ids=$("input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要导出的资讯!");
			return false;
		}
		var ids=[];
		var param="id=";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		window.location.href=adminPath+"mymessage/partexcel.do?"+param;
	}
	
	//推荐
	function tj(id,recommend){
		$.ajax({
			type: "POST",
			url:adminPath+"mymessage/updataRecommend.do",
			data:{ids:id,recommend:recommend},
			success: function(result){
				 if(result.success){
						 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"mymessage/list.do" 
				 }else{
					 window.top.mesageTip("failure","操作失败");
				 }
			}
		});
	}
	//批量推荐
	function updataRecommends(recommend){
		var $ids=$("input[type='checkbox']:checked");
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
			url:adminPath+"mymessage/updataRecommend.do",
			data:param,
			success: function(result){
				 if(result.success){
						 window.top.mesageTip("success","推荐成功");
						 window.location.href=adminPath+"mymessage/list.do" 
				 }else{
					 window.top.mesageTip("failure","推荐失败");
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
			url:adminPath+"mymessage/updataTop.do",
			data:{ids:param},
			success: function(result){
				 if(result.success){
						 window.top.mesageTip("success","置顶成功");
						 window.location.href=adminPath+"mymessage/list.do" 
				 }else{
					 window.top.mesageTip("failure","置顶失败");
				 }
			}
		});
	}
	//置顶
	function zd(param,top){
		$.ajax({
			type: "POST",
			url:adminPath+"mymessage/updataTop.do",
			data:{ids:param,top:top},
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"mymessage/list.do" 
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
				<i>></i>
				<a href="<%=adminPath%>mymessage/list.do">资讯管理</a>
				<i>></i>
				<i>资讯管理</i>
			</div>
		</div>
	<form id="Form" action="<%=adminPath%>mymessage/list.do" method="post">
	<div class="cms_c_list biaoge_con chanpin_con cf">
		<div class="h3 cf">
			<div class="h3_left cf">
				<a href="<%=adminPath%>mymessage/toAdd.do">+添加资讯</a>
				<div class="selec_co cf">
					<select class="form-control" id="shortSelect">
						<option >请选择排序方式</option>
						<option value="UPDATE_DESC">按照更新时间排序</option>
						<option value="HOT_DESC">按照热排序</option>
						<option value="TOP_DESC">按照置顶排序</option>
						<option value="RECOMMEND_DESC">按照推荐排序</option>
					</select>
				</div>
				<div class="selec_co cf">
					<select class="form-control" id="mySelect">
						<option value="0">请选择资讯类型</option>
						<c:forEach items="${messageTypeList }" var="type">
							<option value="${type.ID }">${type.TYPE_NAME }</option>
						</c:forEach>
					</select>
				</div>
				<div class="a_btn cf">
					<a href="<%=adminPath%>mymessage/goUploadExcel.do"><img src="static/images/ru.png" alt="">导入</a>
					<a href="<%=adminPath%>mymessage/excel.do"><img src="static/images/chu.png" alt="">全部导出</a>
					<a href="javascript:void(0)" onclick="exportPartExcel();"><img src="static/images/chu.png" alt="">部分导出</a>
				</div>
			</div>
			<div class="search">
				<input type="text" placeholder="请输入资讯名称" id="key"><input id="ss" type="button" value="搜索" />
			</div>
		</div>
		<style>
		.biaoge_con>div.h3 .h3_left .selec_co .form-control{width:150px;margin-left:10px;}
		.biaoge_con>div.h3 input[type="text"]{width:200px;float:left;}
		.biaoge_con>div.h3 input[type="button"]{width:60px;}
		</style>
		<div class="table cf">
			<dl class="list_bg col_06 cf" id="did">
				<dt class="cf">
					<div class="tit6">
						<div class="tit6_con">
							资讯标题
						</div>
					</div>
					<div class="sele06">操作</div>
					<div class="sele06">状态</div>
					<div class="sele06">发布时间</div>
					<div class="sele06 other_width">所属分类</div>
				</dt>
				<c:if test="${!empty messageList}">
					<c:forEach items="${messageList }" var="map" varStatus="mun">
						<dd class="cf">
						<div class="dd_tit cf">
							<div class="tit6">
								<div class="tit6_con">
									<label ><input type="checkbox" value="${map.id}" /><i></i></label>
									<c:if test="${map.top==1&&map.hot==1&&map.recommend==1 }"><i style="color:blue;">[顶]</i><i style="color:red;">[热]</i><i style="color:green;">[荐]</i>${map.message_title }</c:if>
									<c:if test="${map.top!=1&&map.hot==1&&map.recommend==1 }"><i style="color:red;">[热]</i><i style="color:green;">[荐]</i>${map.message_title }</c:if>
									<c:if test="${map.top==1&&map.hot!=1&&map.recommend==1 }"><i style="color:blue;">[顶]</i><i style="color:green;">[荐]</i>${map.message_title }</c:if>
									<c:if test="${map.top==1&&map.hot==1&&map.recommend!=1 }"><i style="color:blue;">[顶]</i><i style="color:red;">[热]</i>${map.message_title }</c:if>
									<c:if test="${map.top!=1&&map.hot!=1&&map.recommend==1 }"><i style="color:green;">[荐]</i>${map.message_title }</c:if>
									<c:if test="${map.top!=1&&map.hot==1&&map.recommend!=1 }"><i style="color:red;">[热]</i>${map.message_title }</c:if>
									<c:if test="${map.top==1&&map.hot!=1&&map.recommend!=1 }"><i style="color:blue;">[顶]</i>${map.message_title }</c:if>
									<c:if test="${map.top!=1&&map.hot!=1&&map.recommend!=1 }">${map.message_title }</c:if>
								</div>
							</div>
							<div class="sele06 sanJi">
								<a href="javascript:;">管理<span class="sanjiao"></span></a>
								<ul class="guanli_con cf">
									<li><a onclick="updateById('${map.id }')" href="javascript:void(0)">编辑</a></li>
									<c:if test="${map.top!='1' }"><li><a href="javascript:;" onclick="zd('${map.id }',1)">置顶</a></li></c:if>
									<c:if test="${map.top=='1' }"><li><a href="javascript:;" onclick="zd('${map.id }',0)">取消置顶</a></li></c:if>
									<c:if test="${map.recommend!='1' }"><li><a href="javascript:;" onclick="tj('${map.id }',1)">推荐</a></li></c:if>
									<c:if test="${map.recommend=='1' }"><li><a href="javascript:;" onclick="tj('${map.id }',0)">取消推荐</a></li></c:if>
									<c:if test="${map.status==1 }">
										<li><a onclick="isHidden('${map.id}')" href="javascript:;">隐藏</a></li>
									</c:if>
									<c:if test="${map.status==0 }">
										<li><a onclick="isView('${map.id}')" href="javascript:;">显示</a></li>
									</c:if>
									
									<li><a onclick="confirmDelDiv('${map.id }')" href="javascript:;">删除</a></li>
								</ul>
							</div>
							<c:if test="${map.status==1 }">
								<div class="sele06 hide_font">显示</div>
							</c:if>
							<c:if test="${map.status==0 }">
								<div class="sele06 hide_font">隐藏</div>
							</c:if>
							<fmt:parseDate value="${map.created_time}" var="yearMonthDay" pattern="yyyy-MM-dd"/>
							<div class="sele06"><fmt:formatDate value="${yearMonthDay}" pattern="yyyy-MM-dd"/></div>
							<div class="sele06 other_width">
							<c:if test="${!empty map.messageTypeList}">
								<c:forEach items="${map.messageTypeList}" var="messagetype" varStatus="tsta">
									<c:if test="${!tsta.last}">
										${messagetype.type_name},
									</c:if>
									<c:if test="${tsta.last}">
										${messagetype.type_name}
									</c:if>
								</c:forEach>
							</c:if>
							</div>
						</div>
					</dd>
					</c:forEach>
				</c:if>
				<c:if test="${empty messageList}">
					<div  class="sele06 sanJi sele_" style="text-align:center;padding-top:30px;color:#666;width:100%;">没有您要查询的数据!</div>
				</c:if>
			</dl>
			<div class="bottom_con cf">
				<div class="all_checkbox" style="width:250px">
					<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
					<a id="zd" class="zhiding" href="javascript:;" onclick="updataTop(1)">置顶</a>
					<a id="tj" class="tuijian" href="javascript:;" onclick="updataRecommends(1)">推荐</a>
					<a id="sc" onclick="delectChecks()" href="javascript:;">批量删除</a>
				</div>				
				<div class="page_list cf">
					${page.pageStr}						
				</div>
			</div>
		</div>	
	</div>
	</form>	
</div>		
	<div class="footer">© 中企高呈 版权所有</div>
<!-- cms_con结束 -->
</body>
</html>