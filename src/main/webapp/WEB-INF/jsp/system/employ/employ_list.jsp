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
<body class="no-skin">
	<div class="cms_con cf">
	<!-- jsp导航返回栏 -->
			<div class="cms_con cf">
			<div class="cms_c_inst neirong cf">
				<div class="left cf">
					<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
					<i>招聘列表</i>
				</div>
			</div>
			<form action="<%=adminPath%>employ/list.do" method="post">
				<div class="cms_c_list biaoge_con zhandian_con cf">
					<div class="h3 cf">
						<div class="h3_left cf">
							<a href="<%=adminPath%>employ/goAdd.do">+发布招聘</a>
							<div class="selec_co cf">
								<select id="subColumn" class="form-control">
									<option value="">请选择所属栏目</option>
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
							<div class="selec_co cf">
								<select class="form-control" id="shortSelect" style=" width:200px;margin-left:10px;">
									<option >请选择排序方式</option>
									<c:choose>
										<c:when test="${!empty pd.RELEASE_TIME }">
											<option selected="selected" value="RELEASE_TIME">发布时间</option>
											<option value="UPDATE_TIME">修改时间</option>
											<option value="TOP_TIME">置顶时间</option>
											<option value="RECOMMEND_TIME">推荐时间</option>
										</c:when>
										<c:when test="${!empty pd.UPDATE_TIME }">
											<option selected="selected" value="UPDATE_TIME">修改时间</option>
											<option value="RELEASE_TIME">发布时间</option>
											<option value="TOP_TIME">置顶时间</option>
											<option value="RECOMMEND_TIME">推荐时间</option>
										</c:when>
										<c:when test="${!empty pd.TOP_TIME }">
											<option selected="selected" value="TOP_TIME">置顶时间</option>
											<option value="RELEASE_TIME">发布时间</option>
											<option value="UPDATE_TIME">修改时间</option>
											<option value="RECOMMEND_TIME">推荐时间</option>
										</c:when>
										<c:when test="${!empty pd.RECOMMEND_TIME }">
											 <option selected="selected" value="RECOMMEND_TIME">推荐时间</option>
											<option value="TOP_TIME">置顶时间</option>
											<option value="RELEASE_TIME">发布时间</option>
											<option value="UPDATE_TIME">修改时间</option>
										</c:when>
										<c:otherwise>
											<option value="RECOMMEND_TIME">推荐时间</option>
											<option value="TOP_TIME">置顶时间</option>
											<option value="RELEASE_TIME">发布时间</option>
											<option value="UPDATE_TIME">修改时间</option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>
						</div>
						<div class="search">
							<input type="text" id="keywords" name="keywords" value="" ><input type="button" onclick="search();" value="搜索" />
						</div>
						
					</div>
					<div class="table cf">
						<dl class="list_bg col_04 cf">
							<dt class="cf">
								<div class="tit4">
									<div class="tit4_con">
										职位名称
									</div>
								</div>
								<div class="sele04">操作</div>
								<div class="sele04">发布时间</div>
								<div class="sele04">所属栏目</div>
							</dt>
							<c:choose>
								<c:when test="${not empty list}">
										<c:forEach items="${list}" var="obj" varStatus="vs">
											<dd class="cf">
												<div class="dd_tit cf">
													<div class="tit4">
														<div class="tit4_con">
															<label><input type="checkbox" value="${obj.ID}:${obj.JOB_DESCRIPTION}" /><i></i></label>
																<c:if test="${obj.IFTOP==1&&obj.IFRECOMMEND==1 }"><i style="color:red;">[顶]</i><i style="color:green;">[荐]</i>${obj.RECRUIT_POSITION}</c:if>
																<c:if test="${obj.IFTOP==1&&obj.IFRECOMMEND!=1 }"><i style="color:red;">[顶]</i>${obj.RECRUIT_POSITION}</c:if>
																<c:if test="${obj.IFTOP!=1&&obj.IFRECOMMEND==1 }"><i style="color:green;">[荐]</i>${obj.RECRUIT_POSITION}</c:if>
																<c:if test="${obj.IFTOP!=1&&obj.IFRECOMMEND!=1 }">${obj.RECRUIT_POSITION}</c:if>
														</div>
													</div>
													<div class="sele04 sanJi">
														<a href="javascript:;">管理<span class="sanjiao"></span></a>
														<ul class="guanli_con cf">
															<li><a href="<%=adminPath%>employ/goAdd.do?ID=${obj.ID}">编辑</a></li>
															<li><a href="javascript:void(0);" onclick="confirmTopDiv('${obj.ID}','${obj.IFTOP}','${obj.RECRUIT_POSITION}')">
																<c:if test="${obj.IFTOP==1 }">
																	取消置顶
																</c:if>
																<c:if test="${obj.IFTOP==0 }">
																	置顶
																</c:if>
															</a></li>
															<li><a href="javascript:void(0);" onclick="confirmHotDiv('${obj.ID}','${obj.IFRECOMMEND}','${obj.RECRUIT_POSITION}')">
																<c:if test="${obj.IFRECOMMEND==1 }">
																	取消推荐
																</c:if>
																<c:if test="${obj.IFRECOMMEND==0 }">
																	推荐
																</c:if>
															</a></li>
															<li><a href="javascript:void(0);" onclick="confirmDelDiv('${obj.ID}:${obj.JOB_DESCRIPTION}','${obj.RECRUIT_POSITION}')">删除</a></li>
														</ul>
													</div>
													<div class="sele04"><fmt:formatDate value="${obj.RELEASE_TIME}" pattern="yyyy-MM-dd"/></div>
													<div class="sele04">${obj.COLUM_NAME}</div>
												</div>
											</dd>
										</c:forEach>
								</c:when>
							</c:choose>
						</dl>
						<div class="bottom_con cf">
							<div class="all_checkbox">
								<label for="c_10"><input value="" type="checkbox" id="c_10" /><span>全选</span><i></i></label>
								<a id="sc" href="javascript:;" onclick="confirmBatchDelDiv();">删除</a>
							</div>
						
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
</body>

<script type="text/javascript">
$(function(){
	
	top.hangge();
	
	/* 所属栏目 */
	$("#subColumn").change(function(){
		var $id=$("#subColumn").val();
		var shorts=$("#shortSelect").val();
		if(shorts=="RELEASE_TIME"){
				window.location.href=adminPath+"employ/list.do?RELEASE_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="UPDATE_TIME"){
				window.location.href=adminPath+"employ/list.do?UPDATE_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="TOP_TIME"){
				window.location.href=adminPath+"employ/list.do?TOP_TIME="+shorts+"&colum_id="+$id;
			}else if(shorts=="RECOMMEND_TIME"){
				window.location.href=adminPath+"employ/list.do?RECOMMEND_TIME="+shorts+"&colum_id="+$id;
			}else{
				window.location.href=adminPath+"employ/employByColumnIdlistPage.do?colum_id="+$id; 
			}
	});
	
	/* 选择排序 */
	$("#shortSelect").change(function(){
		var shorts=$("#shortSelect").val();
		var $id=$("#subColumn").val();
		if(shorts=="RELEASE_TIME"){
			window.location.href=adminPath+"employ/list.do?RELEASE_TIME="+shorts+"&colum_id="+$id;
		}else if(shorts=="UPDATE_TIME"){
			window.location.href=adminPath+"employ/list.do?UPDATE_TIME="+shorts+"&colum_id="+$id;
		}else if(shorts=="TOP_TIME"){
			window.location.href=adminPath+"employ/list.do?TOP_TIME="+shorts+"&colum_id="+$id;
		}else if(shorts=="RECOMMEND_TIME"){
			window.location.href=adminPath+"employ/list.do?RECOMMEND_TIME="+shorts+"&colum_id="+$id;
		}else{
			return false;
		}
		
	});
});

function confirmBatchDelDiv(){
	var $ids=$("input[type='checkbox']:checked");
	if($ids.length==0){
		window.top.mesageTip("warn","请选择删除的信息!");
		return false;
	}
	var	title='确认批量删除所选职位的招聘信息吗？';
	var	content='此操作会删除该招聘信息';
	mesageConfirm('删除信息',title,content,"batchDel()");
	
}

function batchDel(){
	var $ids=$("input[type='checkbox']:checked");
	var ids=[];
	var param="";
	$.each($ids,function(index,obj){
		ids.push(obj.value);
	});
	param=ids.join(",");
	window.location.href=adminPath+"employ/deleteAll.do?ids="+param;
}

function search(){
	var keywords=$("#keywords").val();
	keywords = $.trim(keywords);
	window.location.href=adminPath+"employ/list.do?keywords="+encodeURI(encodeURI(keywords));
}

function confirmTopDiv(id,status,name){
	var	title='确认置顶职位【'+name+'】的招聘信息吗？';
	var	content='此操作会将该招聘信息置顶显示';
	mesageConfirm('招聘置顶',title,content,"topEmploy('"+id+"','"+status+"')");
}

function topEmploy(id,status){
	hideConfirm();
	$.ajax({
		type: "GET",
		url: adminPath+"employ/top.do",
		dataType:'json',
		data:{"ID":id,"IFTOP":status},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","状态改变成功!");
				 setTimeout("location.href='<%=adminPath%>employ/list.do'",1000);
			 }else{
				 window.top.mesageTip("failure","状态改变失败,请重试!");
			 }
		}
	});
}

function confirmHotDiv(id,status,name){
	var	title='确认推荐职位【'+name+'】的招聘信息吗？';
	var	content='此操作会将该招聘信息推荐到首页显示';
	mesageConfirm('招聘推荐',title,content,"hotEmploy('"+id+"','"+status+"')");
}

function hotEmploy(id,status){
	hideConfirm();
	$.ajax({
		type: "GET",
		url: adminPath+"employ/recommend.do",
		dataType:'json',
		data:{"ID":id,"IFRECOMMEND":status},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","状态改变成功!");
				 setTimeout("location.href='<%=adminPath%>employ/list.do'",1000);
			 }else{
				 window.top.mesageTip("failure","状态改变失败,请联系管理员!");
			 }
		}
	});
}

function confirmDelDiv(id,name){
	var	title='确认删除职位【'+name+'】的招聘信息吗？';
	var	content='此操作会删除该招聘信息';
	mesageConfirm('删除信息',title,content,"delEmploy('"+id+"')");
}

function delEmploy(id){
	hideConfirm();
	$.ajax({
		type: "GET",
		url: adminPath+"employ/del.do",
		dataType:'json',
		data:{"ID":id},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","删除成功!");
				 setTimeout("location.href='<%=adminPath%>employ/list.do'",1000);
			 }else{
				 window.top.mesageTip("failure","删除失败,请联系管理员!");
			 }
		}
	});
}

</script>
</html>