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
			window.location.href=adminPath+"banner/list.do?banner_name="+encodeURI(encodeURI($key)); 
		});
		/* 选择类型 */
		$("#mySelect").change(function(){
			var $id=$("#mySelect").val();
			if($id==0){
				window.location.href=adminPath+"banner/list.do";
			}else{		
				window.location.href=adminPath+"banner/bannerByColumnIdlistPage.do?colum_id="+$id; 
			}
		});
	});
	//当批量删除banner的时候提示是否删除
	function confirmDelDivs(){
		var $ids=$("#did input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择删除的选项!");
			return false;
		}
		var	title='确认删除选中的banner吗?';
		var	content='此操作会删除选中的banner信息,请谨慎操作!';
		mesageConfirm('批量删除banner',title,content,"deleteChecks()");
	}
	//批量删除banner的功能
	function deleteChecks(){
		var $ids=$("#did input[type='checkbox']:checked");
		var ids=[];
		var param="id=";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		window.location.href=adminPath+"banner/delete.do?"+param;		
	}
	
	//通过id删除数据
	function deleteById(data){
		window.location.href=adminPath+"banner/delete.do?id="+data;
	}
	//当删除banner的时候提示是否删除
	function confirmDelDiv(data){
		var	title='确认删除该banner吗?';
		var	content='此操作会删除该banner信息,请谨慎操作!';
		mesageConfirm('删除banner',title,content,"deleteById('"+data+"')");
	}
	
	//更新banner数据也就是修改banner数据
	function updateById(data){
		if(data==0){
			return false;
		}
		window.location.href=adminPath+"banner/toEdit.do?id="+data; 
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
						<a href="<%=adminPath%>banner/list.do">banner管理</a>
						<i>></i>
						<i>banner管理</i>
					</div>
				</div>
				<form id="Form" action="<%=adminPath%>banner/list.do" method="post">
				<div class="cms_c_list biaoge_con cf">
					<div class="h3 cf">
						<div class="h3_left cf"><a href="<%=adminPath%>banner/goAdd.do">+添加Banner</a></div>				
						<div class="selec_co cf"  style="float: left;margin-left:10px;">
							<select class="form-control" id="mySelect" style=" width:200px;">
								<option value="0">请选择所属栏目</option>
								<c:forEach items="${type }" var="type">
								<c:if test="${pd.colum_id==type.ID }">
								<option  value="${type.ID }" selected="selected">${type.COLUM_NAME }</option>
								</c:if>
								<c:if test="${pd.colum_id!=type.ID }">
								<option  value="${type.ID }" >${type.COLUM_NAME }</option>
								</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="search">
							<input placeholder="请输入banner名称" type="text" id="key"><input id="ss" type="button"  value="搜索" />
						</div>
					</div>
					<div class="table cf">

						<dl class="list_bg col_04 cf" id="did">
							<dt class="cf">
								<div class="tit4">
									<div class="tit4_con">
										Banner名称
									</div>
								</div>
								<div class="sele04">操作</div>
								<div class="sele04">包含内容</div>
								<div class="sele04 other_width">所属栏目</div>
							</dt>
							<c:if test="${!empty bannerList}">
								<c:forEach items="${bannerList }" var="map" varStatus="mun">
									<dd class="cf">
										<div class="dd_tit cf">
											<div class="tit4">
												<div class="tit4_con">
													<label ><input type="checkbox" value="${map.id }" /><i></i></label>
													${map.banner_name }
												</div>
											</div>
											<div class="sele04 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<li><a onclick="updateById('${map.id }')" href="javascript:void(0)">编辑</a></li>
													<li><a onclick="confirmDelDiv('${map.id }')" href="javascript:;">删除</a></li>
												</ul>
											</div>
											<div class="sele04">${map.countiv}</div>
											<div class="sele04 other_width">
												<c:forEach items="${map.cols}" var="colm" varStatus="n" >
													<c:if test="${map.cols.size()==n.count}">
														${colm.columName}
													</c:if>
													<c:if test="${map.cols.size()!=n.count}">
														${colm.columName},
													</c:if>
												</c:forEach>
											</div>	
										</div>
									</dd>
								</c:forEach>
							</c:if>
							<c:if test="${empty bannerList}">
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
					</div>
					</div>
				</form>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->

</body>
</html>          