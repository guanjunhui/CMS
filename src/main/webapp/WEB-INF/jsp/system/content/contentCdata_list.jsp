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
columId='${pd.colum_id}';
show_text=encodeURI(encodeURI('${pd.text}'));
	$(function(){
		
		//栏目树	↓
		//获取栏目类型树形结构
		setColumdiv();
		
		
		//栏目树	↑
		/* 搜索  */
		top.hangge();
		$("#ss").click(function(){
			var key=$("#key").val();
			$("#keywords").val(key);
			$("#Form").submit();
		});
		$("#shortSelect").change(function(){
			var shorts=$("#shortSelect").val();
			var $id=columId;
			var showtext =show_text;
			if(shorts=="CREATED_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?CREATED_TIME="+shorts+"&colum_id="+$id+"&text="+showtext;
			}else if(shorts=="UPDATE_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?UPDATE_TIME="+shorts+"&colum_id="+$id+"&text="+showtext;
			}else if(shorts=="TOP_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?TOP_TIME="+shorts+"&colum_id="+$id+"&text="+showtext;
			}else if(shorts=="RECOMMEND_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?RECOMMEND_TIME="+shorts+"&colum_id="+$id+"&text="+showtext;
			}else{
				return false;
			}
			
		});
		/* 选择类型 */ //之前的栏目选择
		/* $("#mySelect").change(function(){
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
		}); */
	});
	
	
	function setColumdiv(){
		$.ajax({
			type: "GET",
			url:adminPath+"columconfig/getAssignTypeTree.do",
			data:{TEM_TYPE:1},
			dataType:'json',
			cache: false,
			success: function(result){
				if(result.code==200&&result.data!=null){
					appendColumdiv(result.data);
				}
			}
		});
	}
	//选择栏目弹出内容填充
	function appendColumdiv(list){
		 var html='';
		 $.each(list,function(index,obj){
			 html+='<li  data-value='+obj.id+'>';
			 if(obj.childList!=null && obj.childList.length>0){
				 html+='<em>+</em><span  onclick="getData(this)">'+obj.name+'</span>';
				 html+=eachSubList('',obj.childList);
			 }else{
				 html+='<span onclick="getData(this)">'+obj.name+'</span>';
			 }
			 html+='</li>';
		 });
		 $("#columnUl").html(html);
		 $('.layer_list_other li .show_btn').each(function(){
		     	if($(this).parent('p').next('dl').size()==0){
		     		$(this).hide();
		     	}
		     });
	}
	//遍历子栏目
	function eachSubList(html,list){
		 html+='<ol>';
		 $.each(list,function(index,obj){
			 html+='<li  data-value='+obj.id+'>';
			 if(obj.childList!=null && obj.childList.length>0){
				 html+='<em>+</em><span onclick="getData(this)">'+obj.name+'</span>';
				 html+=eachSubList('',obj.childList);
			 }else{
				 html+='<span onclick="getData(this)">'+obj.name+'</span>';
			 }
			 html+='</li>';
		 });
		 html+='</ol>';
		return html;
	}
	//获取点击的栏目id
	function getData(param){
		show_text = encodeURI(encodeURI($(param).text()));
		var shorts=$("#shortSelect").val();
		columId=$(param).parent("li").data("value"); 
		if(shorts=="CREATED_TIME"){
			window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?CREATED_TIME="+shorts+"&colum_id="+columId+"&text="+show_text;
			}else if(shorts=="UPDATE_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?UPDATE_TIME="+shorts+"&colum_id="+columId+"&text="+show_text;
			}else if(shorts=="TOP_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?TOP_TIME="+shorts+"&colum_id="+columId+"&text="+show_text;
			}else if(shorts=="RECOMMEND_TIME"){
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?RECOMMEND_TIME="+shorts+"&colum_id="+columId+"&text="+show_text;
			}else{
				window.location.href=adminPath+"contentData/contentByColumnIdlistPage.do?colum_id="+columId+"&text="+show_text;
			} 
		
	} 
	//当批量删除的时候提示是否删除
	function deleteChecks(){
		var $ids=$("input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要删除的内容数据!");
			return false;
		}
		var ids=[];
		var param="id=";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		var	title='确认删除选中的内容数据吗?';
		var	content='此操作会删除选中的内容数据,请谨慎操作!';
		mesageConfirm('删除内容数据',title,content,"batchDel('"+param+"')");
	}

function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
function batchDel(param){
	hideConfirm();
	$.ajax({
		url:adminPath+"contentData/delAllContent.do",
		type:"get",
		data:param,
		success:function(result){
			if(result.success){
				window.top.mesageTip("success","操作成功");
				window.location.href=adminPath+"contentData/contentlistPage.do?colum_id="+'${colum_id}';
			}else{
				window.top.mesageTip("failure","操作失败");
			}
		}
	});
}
function updateById(data){
	window.location.href=adminPath+"contentData/toUpdate.do?id="+data; 
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
function typeSort(param){
	var zz=/^[1-9][0-9]*$/;
	var sort=$(param).val();
	if(zz.test(sort)){
		$.ajax({
			type: "GET",
			url:adminPath+"contentData/updateSort.do",
			data:{id:$(param).attr("sort"),sort:$(param).val()},
			success:function(result){
				if(result.success){
				}else{
					window.top.mesageTip("failure","操作失败");
				}
			}
		});
	}else{
		window.top.mesageTip("warn","请输入正整数");
	}
	
}
</script>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <i>内容数据</i>
			</div>
		</div>

		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<a href="<%=adminPath%>contentData/toAdd.do?colum_id=${colum_id }">+添加内容</a>
				</div>

				<%-- <div class="selec_co cf" style="float: left; margin-left: 10px;">
					<select class="form-control" id="mySelect" style="width: 200px;">
						<option value="">请选择所属栏目</option>
						<c:forEach items="${type }" var="type">
							<c:if test="${pd.colum_id==type.ID }">
								<option value="${type.ID }" selected="selected">${type.COLUM_NAME }</option>
							</c:if>
							<c:if test="${pd.colum_id!=type.ID }">
								<option value="${type.ID }">${type.COLUM_NAME }</option>
							</c:if>
						</c:forEach>
					</select>
				</div> --%>
				<!-- 栏目展示	↓ -->
				<div class="public_search duo_ser cf">
				  <div class="search_list f_s_list10">
				    <div class="show_text">
				    	${(pd.text==''||pd.text==null)?'请选择栏目':pd.text}
				    </div>
				    <div class="input_hidden"><input type="hidden" value=""></div>
				    <div class="down_btn"></div>
				    <div class="selec_con">
				      <div class="mScrol cf">
				          <ul class="bg" id="columnUl">
				          	<!-- <li data-value='1'><em>+</em><span>新中式1</span>
				          		<ol>
				          			<li><span>新中式11</span></li>
				          			<li><span>新中式12</span></li>
				          			<li><span>新中式13</span></li>
				          		</ol>
				          	</li>
				          	<li data-value='1'><em>+</em><span>新中式2</span>
				          		<ol>
				          			<li><em>+</em><span>新中式21</span>
					                  <ol>
					                    <li><span>新中式21</span></li>
					                    <li><span>新中式22</span></li>
					                    <li><span>新中式23</span></li>
					                  </ol>
				                   </li>
				          			<li><span>新中式22</span></li>
				          			<li><span>新中式23</span></li>
				          		</ol>
				          	</li>
				          	<li data-value='1'><span>新中式3</span></li>
				          	<li data-value='1'><span>新中式4</span></li>
				          	<li data-value='1'><span>新中式5</span></li>
				          	<li data-value='1'><span>新中式6</span></li>
				          	<li data-value='1'><span>新中式7</span></li>
				          	<li data-value='1'><span>新中式8</span></li>
				          	<li data-value='1'><span>新中式9</span></li> -->
				          </ul>
				      </div>
				    </div>
				  </div>
				</div>
				<!-- 栏目展示	↑ -->
				<div class="selec_co cf">
					<select class="form-control" id="shortSelect"
						style="width: 200px; margin-left: 10px;">
						<option>请选择排序方式</option>
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
						<input type="text" placeholder="请输入关键字" id="key"><input
							id="ss" type="button" value="搜索" />
					</div>
				</div>

			</div>
			<div class="table cf">
				<dl class="list_bg col_06 cf" id="did">
					<dt class="cf">
						<div class="tit6">
							<div class="tit6_con">内容数据名称</div>
						</div>
						<div class="sele06">操作</div>
						<div class="sele06">发布时间</div>
						<div class="sele06 other_width">所属栏目1234</div>
						<div class="sele06">排序</div>
					</dt>
					<form id="Form"
						action="<%=adminPath%>contentData/contentlistPage.do"
						method="post">
						<input type="hidden" name="keywords" id="keywords" />
						<c:if test="${!empty contentList }">
							<c:forEach items="${contentList }" var="map">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label><input type="checkbox" value="${map.id }" /><i></i></label>

												<c:if test="${map.top==1&&map.recommend==1 }">
													<i style="color: red;">[顶]</i>
													<i style="color: green;">[荐]</i>${map.contentTitle }</c:if>
												<c:if test="${map.top==1&&map.recommend!=1 }">
													<i style="color: red;">[顶]</i>${map.contentTitle }</c:if>
												<c:if test="${map.top!=1&&map.recommend==1 }">
													<i style="color: green;">[荐]</i>${map.contentTitle }</c:if>
												<c:if test="${map.top!=1&&map.recommend!=1 }">${map.contentTitle }</c:if>
											</div>
										</div>
										
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a onclick="updateById('${map.id }')"
													href="javascript:void(0)">编辑</a></li>
												<li><a onclick="deleteById('${map.id }')"
													href="javascript:;">删除</a></li>
												<c:if test="${map.top!=1 }">
													<li><a href="javascript:;"
														onclick="zd('${map.id }',1)">置顶</a></li>
												</c:if>
												<c:if test="${map.top==1 }">
													<li><a href="javascript:;"
														onclick="zd('${map.id }',0)">取消置顶</a></li>
												</c:if>
												<c:if test="${map.recommend!=1 }">
													<li><a href="javascript:;"
														onclick="tj('${map.id }',1)">推荐</a></li>
												</c:if>
												<c:if test="${map.recommend==1 }">
													<li><a href="javascript:;"
														onclick="tj('${map.id }',0)">取消推荐</a></li>
												</c:if>
											</ul>
										</div>

										<div class="sele06">
											<c:if test="${not empty map.releaseTime}">
												<fmt:parseDate value="${map.releaseTime }" var="yearMonth"
													pattern="yyyy-MM-dd" />
												<fmt:formatDate value="${yearMonth}" pattern="yyyy-MM-dd" />
											</c:if>
										</div>
										<div class="sele06 other_width">
											<c:forEach items="${map.columConfigList }" var="c_ype"
												varStatus="n">
												<c:if test="${map.columConfigList.size()==n.count }">${c_ype.columName }</c:if>
												<c:if test="${map.columConfigList.size()!=n.count }">${c_ype.columName },</c:if>
											</c:forEach>
										</div>
										
										<div class="sele06">
											<input type="text"  onblur="typeSort(this);"  sort="${map.id}"  value="${map.sort}"class="nub" style="width:30px;" />
										</div>
										
									</div>
								</dd>
							</c:forEach>
						</c:if>
						<c:if test="${empty contentList }">
							<div
								style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">对不起,没有相关数据</div>
						</c:if>
					</form>
				</dl>
				<div class="bottom_con cf">
					<div class="all_checkbox" style="width: 250px">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
						<a id="zd" class="zhiding" href="javascript:;"
							onclick="updataTop(1)">置顶</a> <a id="tj" class="tuijian"
							href="javascript:;" onclick="updataRecommends(1)">推荐</a> <a
							id="sc" href="javascript:;" onclick="deleteChecks()">批量删除</a>
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