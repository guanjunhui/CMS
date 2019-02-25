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
	String imgPath = "/uploadFiles/uploadImgs/";
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
		top.hangge();
		$("#shortSelect").change(function(){
			var shorts=$("#shortSelect").val();
			var $id=$("#mySelect").val();
			var colId=$("#columSelect").val();
			if(shorts=="CREATED_TIME_DESC"){
			window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else if(shorts=="CREATED_TIME_ASC"){
				window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else if(shorts=="NAME_DESC"){
				window.location.href=adminPath+"product/sortlistPage.do?NAME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else if(shorts=="NAME_ASC"){
				window.location.href=adminPath+"product/sortlistPage.do?NAME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else if(shorts=="NO_DESC"){
				window.location.href=adminPath+"product/sortlistPage.do?NO_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else if(shorts=="NO_ASC"){
				window.location.href=adminPath+"product/sortlistPage.do?NO_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
			}else{
				return false;
			}
			
		});
		/* 选择类型 */
		$("#mySelect").change(function(){
			var shorts=$("#shortSelect").val();
			var $id=$("#mySelect").val();
			var colId=$("#columSelect").val();
			if(shorts=="CREATED_TIME_DESC"){
				window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="CREATED_TIME_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NAME_DESC"){
					window.location.href=adminPath+"product/sortlistPage.do?NAME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NAME_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?NAME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NO_DESC"){
					window.location.href=adminPath+"product/sortlistPage.do?NO_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NO_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?NO_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else{
					window.location.href=adminPath+"product/sortlistPage.do?typeId="+$id+"&columId="+colId;;
				}
		});
		/* 选择栏目 */
		$("#columSelect").change(function(){
			var colId=$("#columSelect").val();
			var shorts=$("#shortSelect").val();
			var $id=$("#mySelect").val();
			if(shorts=="CREATED_TIME_DESC"){
				window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="CREATED_TIME_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?CREATED_TIME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NAME_DESC"){
					window.location.href=adminPath+"product/sortlistPage.do?NAME_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NAME_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?NAME_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NO_DESC"){
					window.location.href=adminPath+"product/sortlistPage.do?NO_DESC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else if(shorts=="NO_ASC"){
					window.location.href=adminPath+"product/sortlistPage.do?NO_ASC="+shorts+"&typeId="+$id+"&columId="+colId;
				}else{
					window.location.href=adminPath+"product/sortlistPage.do?columId="+colId;
				}
		});
		/* 搜索  */
		$("#ss").click(function(){
			var $key=$("#key").val();
			$("Form").submit(); 
		});
		/* 删除 */
		$("#sc").click(function(){
			var $ids=$("#did input[type='checkbox']:checked");
			if($ids.length==0){
				window.top.mesageTip("warn","请选择删除的选项");
				return false;
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
			
		});
		$("#exprot").click(function(){
			var $ids=$("#did input[type='checkbox']:checked");
			if($ids.length==0){
				window.top.mesageTip("warn","请选择导出的选项");
				return false;
			}
			var ids=[];
			var param="id=";
			$.each($ids,function(index,obj){
				ids.push(obj.value);
			});
			param+=ids.join("&id=");
			window.location.href=adminPath+"product/exprot.do?"+param; 
		})
	});
//导入
function improt(){
	var formData=new FormData();
	formData.append("file",$("#file_name_xlsx")[0].files[0]);
	 $.ajax({
		type: "POST",
		url:adminPath+"product/improt.do",
		data:formData,
		processData:false,
        contentType:false,
		success: function(result){
			 if(result.success){
					 window.top.mesageTip("success","上传成功");
					 window.location.href=adminPath+"product/list.do";
			 }else{
				 window.top.mesageTip("failure","上传失败");
			 }
		}
	});
}

function batchDel(param){
	hideConfirm();
	window.location.href=adminPath+"product/delete.do?"+param;
}
function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
	function isHidden(param,sta){
		 $.ajax({
			type: "POST",
			url:adminPath+"product/updataStatus.do",
			data:{product_Status:sta,ids:param},
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"product/list.do" 
				 }else{
					 window.top.mesageTip("failure","操作失败");
				 }
			}
		}); 
	}
	
	function updateById(data){
		if(data==0){
			return false;
		}
		
		window.location.href=adminPath+"product/toEdit.do?id="+data; 
	}
	//推荐
	function tj(id,recommend){
		$.ajax({
			type: "POST",
			url:adminPath+"product/updataRecommend.do",
			data:{ids:id,recommend:recommend},
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"product/list.do" 
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
			url:adminPath+"product/updataRecommend.do",
			data:param,
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"product/list.do" 
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
			url:adminPath+"product/updataTop.do",
			data:param,
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"product/list.do" 
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
			url:adminPath+"product/updataTop.do",
			data:{ids:id,top:top},
			success: function(result){
				 if(result.success){
					 window.top.mesageTip("success","操作成功");
						 window.location.href=adminPath+"product/list.do" 
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
				<i>></i> <i>产品列表</i>
			</div>
		</div>
		<form id="Form" action="<%=adminPath%>product/list.do" method="post">
			<div class="cms_c_list biaoge_con chanpin_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<a href="<%=adminPath%>product/toAdd.do?flag=1">+添加主商品</a>
						<a href="<%=adminPath%>product/toAdd.do?flag=0">+添加副商品</a>
						<div class="selec_co cf">
							<select class="form-control" id="shortSelect">
								<option>请选择排序方式</option>
								<c:choose>
									<c:when test="${!empty pd.CREATED_TIME_DESC }">
										<option selected="selected" value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:when>
									<c:when test="${!empty pd.CREATED_TIME_ASC }">
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option selected="selected" value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:when>
									<c:when test="${!empty pd.NAME_DESC }">
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option selected="selected" value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:when>
									<c:when test="${!empty pd.NAME_ASC }">
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option selected="selected" value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:when>
									<c:when test="${!empty pd.NO_DESC }">
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option selected="selected" value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:when>
									<c:when test="${!empty pd.NO_ASC }">
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option selected="selected" value="NO_ASC">编号升序</option>
									</c:when>
									<c:otherwise>
										<option value="CREATED_TIME_DESC">添加时间降序</option>
										<option value="CREATED_TIME_ASC">添加时间升序</option>
										<option value="NAME_DESC">名字降序</option>
										<option value="NAME_ASC">名字升序</option>
										<option value="NO_DESC">编号降序</option>
										<option value="NO_ASC">编号升序</option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="selec_co cf">
							<select class="form-control" id="mySelect">
								<option value="">请选择所属分类</option>
								<c:forEach items="${product_TypeList }" var="type">
									<c:if test="${typeId==type.ID }">
										<option selected="selected" value="${type.ID }">${type.TYPE_NAME }</option>
									</c:if>
									<c:if test="${typeId!=type.ID }">
										<option value="${type.ID }">${type.TYPE_NAME }</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<!-- 新加 按栏目搜索 -->
						<div class="selec_co cf">
							<select class="form-control" id="columSelect">
								<option value="">请选择所属栏目</option>
								<c:forEach items="${productColumList }" var="colum">
									<c:if test="${columId==colum.ID }">
										<option selected="selected" value="${colum.ID }">${colum.COLUM_NAME }</option>
									</c:if>
									<c:if test="${columId!=colum.ID }">
										<option value="${colum.ID }">${colum.COLUM_NAME }</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="a_btn cf">
							<a href="<%=adminPath%>product/exprotTemplate.do"><img
								src="../img/chu.png" alt="">获取导入模板</a>
							<p href="javascript:;" class="file_upload_xlsx">
								<img src="static/images/ru.png" alt=""> <label
									for="file_name_xlsx"><input type="file" name="file"
									id="file_name_xlsx"><em>导入</em></label><i></i><a
									href="javascript:;" onclick="improt()" class="upl_file"
									style="display: none;">确定上传</a><a href="javascript:;"
									class="remove_file" style="display: none;">取消上传</a>
							</p>
							<a href="javascript:void(0)" id="exprot"><img
								src="static/images/chu.png" alt="">导出</a>
						</div>
					</div>
					<div class="search">
						<input type="text" name="PRODUCT_KEYWORDS" placeholder="请输入关键字"
							id="key"><input id="ss" type="button" value="搜索" />
					</div>
				</div>
				<script type="text/javascript">
								$(function(){
									$(document).on("change",".file_upload_xlsx input[type='file']",function(){
								    var filePath=$(this).val();
								    if(filePath.indexOf("xlsx")!=-1||filePath.indexOf("xls")!=-1){
								        var arr=filePath.split('\\');
								        var fileName=arr[arr.length-1];
								        $(this).parents('.file_upload_xlsx').find('i').html('');
								        $(this).parents('.file_upload_xlsx').find('.upl_file').show();
								        $(this).parents('.file_upload_xlsx').find('.remove_file').show();
								    }else{
								        $(this).parents('.file_upload_xlsx').find('i').html("您上传文件类型有误！");
								        $(this).parents('.file_upload_xlsx').find('.upl_file').hide();
								        $(this).parents('.file_upload_xlsx').find('.remove_file').hide();
								        return false 
								    }
									})
									$(document).on('click','.remove_file',function(){
										var file = $(this).parent().find("input[type='file']");
										file.after(file.clone().val(''));      
										file.remove(); 
										$(this).parent().find('i').html('未选择文件');
										$(this).hide();
									});
								})
							</script>
				<style>
.biaoge_con>div.h3 .h3_left .a_btn p {
	display: inline-block;
	line-height: 34px;
	margin-left: 10px;
	color: #999;
}

.biaoge_con>div.h3 .h3_left .a_btn p img {
	margin: 8px 5px;
}

.file_upload_xlsx label {
	display: inline-block;
	position: relative;
	border-radius: 3px;
	cursor: pointer;
	width: 30px;
}

.file_upload_xlsx label em {
	position: absolute;
	top: 50%;
	left: 0%;
	transform: translate(0%, -50%);
	color: #999;
	text-align: center;
	right: 0;
}

.file_upload_xlsx input {
	visibility: hidden;
}

.file_upload_xlsx p {
	display: inline-block;
	margin-left: 20px;
	color: #2da1f8;
}

.file_upload_xlsx a {
	display: inline-block;
	margin-left: 20px;
	color: #2da1f8;
}

.biaoge_con>div.h3 .h3_left .a_btn .file_upload_xlsx a {
	color: #2da1f8;
}

.file_upload_xlsx i {
	color: #999;
	padding-left: 5px;
}
</style>
				<div class="table cf">
					<dl class="list_bg col_06 cf" id="did">
						<dt class="cf">
							<div class="tit6">
								<div class="tit6_con">
									<div class="pro_img">缩略图</div>
									产品名称
								</div>
							</div>
							<div class="sele06">操作</div>
							<div class="sele06">字体颜色</div>
							<div class="sele06">发布时间</div>
							<div class="sele06 other_width">所属分类</div>
							<!-- <div class="sele06">编号</div> -->
						</dt>

						<c:if test="${!empty productList }">
							<c:forEach items="${productList }" var="map" varStatus="mun">
								<dd class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label><input type="checkbox" value="${map.id }" /><i></i></label>
												<div class="pro_img">
													<div class="pro_img_big">
														<img src="<%=imgPath%>${map.imgUrl }" alt="">
													</div>
												</div>
												<c:if test="${map.hot==1&&map.recommend==1 }">
													<i style="color: red;">[热]</i>
													<i style="color: green;">[荐]</i>${map.name }</c:if>
												<c:if test="${map.hot==1&&map.recommend!=1 }">
													<i style="color: red;">[热]</i>${map.name }</c:if>
												<c:if test="${map.hot!=1&&map.recommend==1 }">
													<i style="color: green;">[荐]</i>${map.name }</c:if>
												<c:if test="${map.hot!=1&&map.recommend!=1 }">${map.name }</c:if>
											</div>
										</div>
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a onclick="updateById('${map.id }')"
													href="javascript:void(0)">编辑</a></li>
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
												<c:if test="${map.product_Status==1 }">
													<li><a onclick="isHidden('${map.id}',0)"
														href="javascript:;">白色</a></li>
												</c:if>
												<c:if test="${map.product_Status==0 }">
													<li><a onclick="isHidden('${map.id}',1)"
														href="javascript:;">黑色</a></li>
												</c:if>

												<li><a onclick="deleteById('${map.id }')"
													href="javascript:;">删除</a></li>
											</ul>
										</div>
										<c:if test="${map.product_Status==1 }">
											<div class="sele06 hide_font">黑色</div>
										</c:if>
										<c:if test="${map.product_Status==0 }">
											<div class="sele06 hide_font">白色</div>
										</c:if>



										<div class="sele06">
											<c:if test="${not empty map.created_Time}">
												<fmt:parseDate value="${map.created_Time }" var="yearMonth"
													pattern="yyyy-MM-dd" />
												<fmt:formatDate value="${yearMonth}" pattern="yyyy-MM-dd" />
											</c:if>
										</div>
										<div class="sele06 other_width">
											<c:forEach items="${map.productTypeList }" var="t_ype"
												varStatus="n">
												<c:if test="${map.productTypeList.size()!=n.count }">${t_ype.type_name },</c:if>
												<c:if test="${map.productTypeList.size()==n.count }">${t_ype.type_name }</c:if>
											</c:forEach>
										</div>
										<%-- <div class="sele06">${map.no }</div> --%>
									</div>
								</dd>
							</c:forEach>
						</c:if>
						<c:if test="${empty productList }">
							<div
								style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">对不起,没有相关数据</div>
						</c:if>
					</dl>
		</form>
		<div class="bottom_con cf">
			<div class="all_checkbox">
				<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
				<a id="zd" class="zhiding" href="javascript:;"
					onclick="updataTop(1)">置顶</a> <a id="tj" class="tuijian"
					href="javascript:;" onclick="updataRecommends(1)">推荐</a> <a id="sc"
					href="javascript:;">删除</a>
			</div>
			<div class="page_list cf">${page.pageStr}</div>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
		<!-- cms_con结束 -->
</body>
</html>