﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
	String wechatPath = "/uploadFiles/wechat/";
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
		/* 搜索  */
		$("#ss").click(function(){
			var $key=$("#key").val();
			$key=$.trim($key);
			window.location.href=adminPath+"contactPlan/list.do?keywords="+encodeURI(encodeURI($key)); 
		});
	});
	//当批量删除问题的时候提示是否删除
	function confirmDelDivs(){
		var $ids=$("#did input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要删除的选项!");
			return false;
		}
		var	title='确认删除选中的选项吗?';
		var	content='此操作删除的数据不可恢复,请谨慎操作!';
		mesageConfirm('批量删除操作',title,content,"deleteChecks()");
	}
	//批量删除问题的功能
	function deleteChecks(){
		var $ids=$("#did input[type='checkbox']:checked");
		var ids=[];
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		window.location.href=adminPath+"ipcAsk/deleteReplyByIds.do?id="+ids.toString()+"&topicId=${manage_topicId}";		
	}
	
	//通过id删除数据
	function deleteById(data){
		window.location.href=adminPath+"ipcAsk/deleteReplyById.do?id="+data+"&topicId=${manage_topicId}";
	}
	//当删除问题的时候提示是否删除
	function confirmDelDiv(data){
		var	title='确认删除该选项吗?';
		var	content='此操作删除的数据不可恢复,请谨慎操作!';
		mesageConfirm('删除操作',title,content,"deleteById('"+data+"')");
	}
	
	
	//根据id查看详情
	function detailById(id){
		window.location.href=adminPath+"contactPlan/detailById.do?id="+id;
	}
</script>
<style>
.tit4{cursor:pointer;}
.tit4:hover a{color:#f00;}
.dd_slt_img span{display:inline-block;width:15.66%;float:left;margin-left:1%;}
.dd_slt_img span img{width:100%;cursor:pointer;}
.big_imggg{display:none;cursor:pointer;position:fixed;top:0px;bottom:0px;left:0px;right:0px;background:rgba(0,0,0,0.7)!important;z-index:99999;}
.big_imggg span{width:80%;display:block;margin:50px auto;height:90%;overflow:auto;}
.big_imggg img{width:100%;}
</style>
<script>
$(function(){
	$(document).on('click','.tit5',function(e){
		var show_dom=$(this).parents('dd').children('dl');
		if(show_dom.css('display')=='none'){
			show_dom.show();
		}else{
			show_dom.hide();
		}
		e.stopPropagation();
	});
	$(document).on('click','.dd_slt_img img',function(e){
		$('.big_imggg img').attr('src',$(this).attr('src'));
		$(this).parents('dd').find('.big_imggg').show();
		e.stopPropagation();
	})
	$(document).on('click','.big_imggg',function(){
		$(this).hide();
		$('.big_imggg img').attr('src','');
	});
})
</script>
</head>
<body>

			<!-- cms_con开始 -->
			<div class="cms_con cf">

				<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
						<i>></i>
						<a href="<%=adminPath%>problem/list.do">表单管理</a>
						<i>></i>
						<i>问答回复</i>
					</div>
				</div>
				<div class="cms_c_list biaoge_con cf">
					<div class="h3 cf">
						<%-- <div class="h3_left cf"><a href="<%=adminPath%>problem/goAdd.do">+添加问题</a></div>	 --%>			
						<div class="search">
							<!-- <input placeholder="请输入提问内容" type="text" id="key"><input id="ss" type="button"  value="搜索" /> -->
						</div>
					</div>
					<div class="table cf">

						<dl class="list_bg col_05 cf" id="did">
							<dt class="cf">
								<div class="tit5">
									<div class="tit5_con">
										回答内容
									</div>
								</div>
								<div class="sele05">操作</div>
								<div class="sele05">时间</div>
								<div class="sele05">最佳</div>
								<div class="sele05 other_width">回答人</div>
							</dt>
							<c:if test="${!empty list}">
								<c:forEach items="${list}" var="map" varStatus="mun">
									<dd class="cf">
										<div class="dd_tit cf">
											<div class="tit5">
												<div class="tit5_con">
													<label ><input type="checkbox" value="${map.id}" /><i></i></label>
													<c:if test="${map.isAdopt==0 }">${map.reply}</c:if>
													<c:if test="${map.isAdopt==1 }"><font color='red'>*&nbsp;</font>${map.reply}</c:if>
												</div>
											</div>
											<div class="sele05 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<%-- <li><a onclick="detailById('${map.id }')" href="javascript:;">详情</a></li> --%>
													<li><a onclick="confirmDelDiv('${map.id }')" href="javascript:;">删除</a></li>
												</ul>
											</div>
											<div class="sele05">
												${map.createTime}
											</div>
											<div class="sele05">
												<c:if test="${map.isAdopt==1 }">是</c:if>
												<c:if test="${map.isAdopt!=1 }">否</c:if>
											</div>
											<div class="sele05 other_width">
												${map.nickname}
											</div>	
										</div>
										
									</dd>
								</c:forEach>
							</c:if>
							<c:if test="${empty list}">
								<div  class="sele06 sanJi sele_" style="text-align:center;padding-top:30px;color:#666;width:100%;">没有您要查询的数据!</div>
							</c:if>
						</dl>
						<div class="bottom_con cf">
							<div class="all_checkbox">
								<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
								<a id="sc" href="javascript:;" onclick="confirmDelDivs();" >批量删除</a>
							</div>
							<div class="page_list cf">
								${page.pageStr}					
							</div>
						</div>
						<div class="all_btn cf">
							<span id="result"></span>
							<input type="button" class="submit_btn" value="返回" onclick="cancel()"/>
						</div>
					</div>
					</div>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->
			
</body>
<script type="text/javascript">
	function cancel(){
		window.location.href = "<%=adminPath%>/ipcAsk/list.do";
	}
</script>

</html>          