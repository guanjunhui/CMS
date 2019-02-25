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
	/* 搜索  */
	top.hangge();
	$("#ss").click(function(){
		var $key=$("#key").val();
		$.ajax({
			type: "POST",
			url:adminPath+"fileresource/getList.do",
			data:{KEYWORDS:$key},
			success:function(result){
				 var html='';
					 var data=result.list;
						 html=apeendBody(data,'');
				 $('#setDiv').html(html);
				//间隔行色变化
				 $('.biaoge_con .table .dd_tit').each(function(index,el){
					 if(index%2==0){
					 	$(this).addClass('other_bg');
					 }
				 });
			}
		});
	});
	$("#shortSelect").change(function(){
		var shorts=$("#shortSelect").val();
		var $id=$("#mySelect").val();
		if(shorts=="CREATED_TIME"){
			window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&CREATED_TIME="+shorts;
		}else if(shorts=="UPDATE_TIME"){
			window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&UPDATE_TIME="+shorts;
		}else if(shorts=="TOP_TIME"){
			window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&TOP_TIME="+shorts;
		}else if(shorts=="RECOMMEND_TIME"){
			window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&RECOMMEND_TIME="+shorts;
		}else{
			return false;
		}
		
	});
	/* 选择类型 */
	$("#mySelect").change(function(){
		var $id=$("#mySelect").val();
		var shorts=$("#shortSelect").val();
		if(shorts=="CREATED_TIME"){
			window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&CREATED_TIME="+shorts;
			}else if(shorts=="UPDATE_TIME"){
				window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&UPDATE_TIME="+shorts;
			}else if(shorts=="TOP_TIME"){
				window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&TOP_TIME="+shorts;
			}else if(shorts=="RECOMMEND_TIME"){
				window.location.href=adminPath+"fileresource/list.do?download_id="+$id+"&RECOMMEND_TIME="+shorts;
			}else{
				window.location.href=adminPath+"fileresource/list.do?download_id="+$id;
			}
		 
	});
	//setDiv();
});
function setDiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"fileresource/getList.do",
		success:function(result){
			 var html='';
				 var data=result.list;
				 if(data.length==0){
					 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
				 }else{
					 console.log(result.page);
					 html=apeendBody(data,'',0);
					 $(".page_list").html(result.page.pageStr);
				 }
			 $('#setDiv').html(html);
			//间隔行色变化
			 $('.biaoge_con .table .dd_tit').each(function(index,el){
				 if(index%2==0){
				 	$(this).addClass('other_bg');
				 }
			 });
		}
	});
}
function apeendBody(list,html){
	$.each(list,function(i,obj){
		html+='<dd class="cf">';
		html+='<div class="dd_tit cf">';
		html+='<div class="tit6">';
		html+='<div class="tit6_con">';
		html+='<label for="e_0'+obj.fileid+'"><input type="checkbox" id="e_0'+obj.fileid+'" value="'+obj.fileid+'"/><i></i></label>';
		var top=obj.top;
		var recommend=obj.recommend;
		if(top==1&&recommend==1){
		html+='<i style="color:red;">[顶]</i><i style="color:green;">[荐]</i>'+obj.title+'</div>';
		}
		if(top!=1&&recommend==1){
			html+='<i style="color:green;">[荐]</i>'+obj.title+'</div>';
			}
		if(top==1&&recommend!=1){
			html+='<i style="color:red;">[顶]</i>'+obj.title+'</div>';
			}
		if(top!=1&&recommend!=1){
			html+='<i></i>'+obj.title+'</div>';
			}
		html+='</div>';
		html+='<div class="sele06 sanJi">';
		html+='<a href="javascript:;">管理<span class="sanjiao"></span></a>';
		html+='<ul class="guanli_con cf">';
		html+='<li><a href="javascript:;" onclick="toUpdate(\''+obj.fileid+'\')">编辑</a></li>';
		if(top==1){
			html+='<li><a href="javascript:;" onclick="zd(\''+obj.fileid+'\',0)">取消置顶</a></li>';
		}else{
			html+='<li><a href="javascript:;" onclick="zd(\''+obj.fileid+'\',1)">置顶</a></li>';
		}
		if(recommend==1){
			html+='<li><a href="javascript:;" onclick="tj(\''+obj.fileid+'\',0)">取消推荐</a></li>';
		}else{
			html+='<li><a href="javascript:;" onclick="tj(\''+obj.fileid+'\',1)">推荐</a></li>';
		}
		
		html+='<li><a href="javascript:;" onclick="deleteById(\''+obj.fileid+'\')">删除</a></li>';
		html+='</ul>';
		html+='</div>';
		var tim=obj.created_time;
		html+='<div class="sele06">'+tim.substring(0,10)+'</div>';
		var count=obj.download_count;
		if(count==null){
			count=0;
		}
		html+='<div class="sele06">'+count+'</div>';
		html+='<div class="sele06 other_width">';
		html+=eachFileType(obj.fileTypeList,'');
		html+='</div>';
		html+='<div class="sele06">';
		html+=eachtype(obj.files,'');
		html+='</div>';
	});
	return html;
}
function eachFileType(list,html){
	$.each(list,function(index,obj){
		if(index==list.length-1){
			html+=obj.download_name;
		}else{
			html+=obj.download_name+',';
		}
	});
	return html;
}
function eachtype(list,html){
	$.each(list,function(index,obj){
		if(index==list.length-1){
			html+=obj.type;
		}else{
			html+=obj.type+',';
		}
	});
	return html;
}
function toUpdate(data){
	window.location.href=adminPath+"fileresource/toUpdate.do?id="+data; 
}
function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
function deleteAll(){
	var $ids=$("#setDiv input[type='checkbox']:checked");
	if($ids.length==0){
		window.top.mesageTip("warn","请选择删除的项!");
		return;
	}
	var ids=[];
	var param="id=";
	$.each($ids,function(index,obj){
		ids.push(obj.value);
	});
	param+=ids.join("&id=");
	var	title='确认批量删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
function batchDel(param){
	hideConfirm();
	$.ajax({
		url:adminPath+"fileresource/deleteFile.do",
		type:"get",
		data:param,
		success:function(result){
			if(result.success){
				window.top.mesageTip("success","操作成功");
				setDiv();
			}else{
				window.top.mesageTip("failure","操作失败");
			}
		}
	});
}
//推荐
function tj(id,recommend){
	$.ajax({
		type: "POST",
		url:adminPath+"fileresource/updataRecommend.do",
		data:{ids:id,recommend:recommend},
		success: function(result){
			 if(result.success){
					 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"fileresource/list.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
//批量推荐
function updataRecommends(recommend){
	var $ids=$("#setDiv input[type='checkbox']:checked");
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
		url:adminPath+"fileresource/updataRecommend.do",
		data:param,
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"fileresource/list.do" 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
//批量置顶
function updataTop(top){
	var $ids=$("#setDiv input[type='checkbox']:checked");
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
	param+="&top="+top
	$.ajax({
		type: "POST",
		url:adminPath+"fileresource/updataTop.do",
		data:param,
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"fileresource/list.do" 
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
		url:adminPath+"fileresource/updataTop.do",
		data:{ids:id,top:top},
		success: function(result){
			 if(result.success){
					 window.top.mesageTip("success","操作成功");
					 window.location.href=adminPath+"fileresource/list.do" 
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
			<i>全部文件</i>
		</div>
	</div>
<form  id="Form" action="<%=adminPath%>fileresource/list.do?" method="post" >
	<div class="cms_c_list biaoge_con chanpin_con cf">
	
		<div class="h3 cf">
			<div class="h3_left cf">
				<a href="<%=adminPath%>fileresource/toAdd.do">+添加文件</a>
				
			</div>
			<div class="selec_co cf">
					<select class="form-control" id="mySelect">
						<option value="">请选择所属分类</option>
						<c:forEach items="${type }" var="type">
						<c:if test="${download_id==type.DOWNLOAD_ID }">
							<option selected="selected" value="${type.DOWNLOAD_ID }">${type.DOWNLOAD_NAME }</option>
						</c:if>
						<c:if test="${download_id!=type.DOWNLOAD_ID }">
							<option  value="${type.DOWNLOAD_ID }">${type.DOWNLOAD_NAME }</option>
						</c:if>
						</c:forEach>
					</select>

					<select class="form-control" id="shortSelect">
					<option >请选择排序方式</option>
					<c:choose>
							<c:when test="${!empty pd.CREATED_TIME }">
								<option selected="selected" value="CREATED_TIME">添加时间</option>
								<option value="UPDATE_TIME">修改时间</option>
								<option value="TOP_TIME">置顶时间</option>
								<option value="RECOMMEND_TIME">推荐时间</option>
							</c:when>
							<c:when test="${!empty pd.UPDATE_TIME }">
								<option selected="selected" value="UPDATE_TIME">修改时间</option>
								<option value="CREATED_TIME">添加时间</option>
								<option value="TOP_TIME">置顶时间</option>
								<option value="RECOMMEND_TIME">推荐时间</option>
							</c:when>
							<c:when test="${!empty pd.TOP_TIME }">
								<option selected="selected" value="TOP_TIME">置顶时间</option>
								<option value="CREATED_TIME">添加时间</option>
								<option value="UPDATE_TIME">修改时间</option>
								<option value="RECOMMEND_TIME">推荐时间</option>
							</c:when>
							<c:when test="${!empty pd.RECOMMEND_TIME }">
								 <option selected="selected" value="RECOMMEND_TIME">推荐时间</option>
								<option value="TOP_TIME">置顶时间</option>
								<option value="CREATED_TIME">添加时间</option>
								<option value="UPDATE_TIME">修改时间</option>
							</c:when>
							<c:otherwise>
								<option value="RECOMMEND_TIME">推荐时间</option>
								<option value="TOP_TIME">置顶时间</option>
								<option value="CREATED_TIME">添加时间</option>
								<option value="UPDATE_TIME">修改时间</option>
							</c:otherwise>
						</c:choose>
					</select>
					<div class="search">
						<input type="text" placeholder="请输入关键字" id="key"><input type="button" value="搜索" id="ss" />
					</div>
				</div>

		</div>
		<style>
			.form-control{width:200px;margin-left:10px;}
		</style>
		<div class="table cf" >

			<dl class="list_bg col_06 cf">
							<dt class="cf">
								<div class="tit6">
									<div class="tit6_con">
										文件名称
									</div>
								</div>
								<div class="sele06">操作</div>
								<div class="sele06">发布时间</div>
								<div class="sele06">下载量</div>
								<div class="sele06 other_width">所属分类</div>
								<div class="sele06">类型</div>
							</dt>
							 <div id="setDiv">
							 <c:if test="${!empty list }">
							  <c:forEach items="${list }" var="f">
									 	<dd class="cf">
		<div class="dd_tit cf">
		<div class="tit6">
		<div class="tit6_con">
		<label for="e_0${f.fileid }"><input type="checkbox" id="e_0${f.fileid }" value="${f.fileid }"/><i></i></label>
		<c:if test="${f.top==1&&f.recommend==1 }">
		<i style="color:red;">[顶]</i><i style="color:green;">[荐]</i>${f.title }</div>
		</c:if>
		<c:if test="${f.top!=1&&f.recommend==1 }">
		<i style="color:green;">[荐]</i>${f.title }</div>
			</c:if>
		<c:if test="${f.top==1&&f.recommend!=1 }">
			<i style="color:red;">[顶]</i>${f.title }</div>
			</c:if>
		<c:if test="${f.top!=1&&f.recommend!=1 }">
			<i></i>${f.title }</div>
			</c:if>
						
		
		</div>
		<div class="sele06 sanJi">
		<a href="javascript:;">管理<span class="sanjiao"></span></a>
		<ul class="guanli_con cf">
		<li><a href="javascript:;" onclick="toUpdate('${f.fileid}')">编辑</a></li>
		<c:if test="${f.top==1 }">
			<li><a href="javascript:;" onclick="zd('${f.fileid}',0)">取消置顶</a></li>
		</c:if>
		<c:if test="${f.top!=1 }">
			<li><a href="javascript:;" onclick="zd('${f.fileid}',1)">置顶</a></li>
		</c:if>
		<c:if test="${f.recommend==1 }">
			<li><a href="javascript:;" onclick="tj('${f.fileid}',0)">取消推荐</a></li>
		</c:if>
		<c:if test="${f.recommend!=1 }">
		<li><a href="javascript:;" onclick="tj('${f.fileid}',1)">推荐</a></li>
		</c:if>
		
		<li><a href="javascript:;" onclick="deleteById('${f.fileid}')">删除</a></li>
		</ul>
		</div>
	
	<div class="sele06">
		<c:if test="${not empty f.created_time}">
													<fmt:parseDate value="${f.created_time }" var="yearMonth" pattern="yyyy-MM-dd"/>
													<fmt:formatDate value="${yearMonth}" pattern="yyyy-MM-dd" />
												</c:if>
	
	</div>
		

	<div class="sele06">${f.download_count }</div>
		<div class="sele06 other_width">
		
	<c:forEach items="${f.fileTypeList }" var="t" varStatus="n">
	  <c:if test="${n.count!=f.fileTypeList.size() }">${t.download_name },</c:if>
	  <c:if test="${n.count==f.fileTypeList.size() }">${t.download_name }</c:if>
	</c:forEach>
		</div>
		
	<div class="sele06">
		
		<c:forEach items="${f.files }" var="d" varStatus="m">
		
		<c:if test="${m.count!=f.files.size() }">${d.type },</c:if>
	  <c:if test="${m.count==f.files.size() }">${d.type }</c:if>
		</c:forEach>
		
		</div>
							 	
				</c:forEach>
							  
							  </c:if>	
							 	
									 	
						<c:if test="${empty list }">
                        <div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>
                        
                        </c:if>	 	
							 </div> 
							
						</dl>
						
			<div class="bottom_con cf">
				<div class="all_checkbox">
					<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
					<a class="yincang" href="javascript:;"  onclick="updataRecommends(1)">推荐</a>
					<a class="yincang" href="javascript:;"  onclick="updataTop(1)">置顶</a>
					<a href="javascript:;" onclick="deleteAll()">删除</a>
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