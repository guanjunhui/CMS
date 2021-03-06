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
			$("#Form").submit();
			//window.location.href=adminPath+"contactPlan/list.do?keywords="+encodeURI(encodeURI($key)); 
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
		window.location.href=adminPath+"contactPlan/deleteByIds.do?id="+ids.toString();		
	}
	
	//通过id删除数据
	function deleteById(data){
		window.location.href=adminPath+"contactPlan/deleteById.do?id="+data;
	}
	//当删除问题的时候提示是否删除
	function confirmDelDiv(data){
		var	title='确认删除该选项吗?';
		var	content='此操作删除的数据不可恢复,请谨慎操作!';
		mesageConfirm('删除操作',title,content,"deleteById('"+data+"')");
	}
	
	//更新处理状态
	function updateHandle(id,sta){
		$.ajax({
            url:adminPath+"contactPlan/updateHandle",
            type:"post",
            data:{
            	"id":id,
            	"handle":sta
            },
            dataType:"json",
            success:function(data){
           	 	  if (data.code == 200) {
           			window.location.href="${basePath}/manage/contactPlan/list.do";
		          }else{
		        	  alert("服务器错误");
		          }
            }
        });
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
						<a href="<%=adminPath%>contactPlan/list.do">表单管理</a>
						<i>></i>
						<i>联系方案</i>
					</div>
				</div>
				<form id="Form" action="<%=adminPath%>contactPlan/list.do" method="post">
				<div class="cms_c_list biaoge_con cf">
					<div class="h3 cf">
						<%-- <div class="h3_left cf"><a href="<%=adminPath%>problem/goAdd.do">+添加问题</a></div>	 --%>			
						<div class="h3_left cf">
							<div class="a_btn cf">
								<%-- <a href="<%=adminPath%>ipcExcel/download.do?key=1"><img src="static/images/chu.png" alt="">获取导入模板</a>
								<p href="javascript:;" class="file_upload_xlsx">
									<img src="static/images/ru.png" alt=""> 
									<label for="file_name_xlsx">
									<input type="file" name="file" id="file_name_xlsx"><em>导入</em></label><i></i>
									<a href="javascript:;" onclick="improt(1)" class="upl_file" style="display: none;">确定上传</a>
									<a href="javascript:;" class="remove_file" style="display: none;">取消上传</a>
								</p> --%>
								<a href="<%=adminPath%>ipcExcel/export.do?key=1"><img src="static/images/chu.png" alt="">导出</a>
							</div>
						</div>
						<div class="search">
							<input placeholder="请输入提问内容" type="text" id="key" name="keywords"><input id="ss" type="button"  value="搜索" />
						</div>
					</div>
					<div class="table cf">

						<dl class="list_bg col_05 cf" id="did">
							<dt class="cf">
								<div class="tit5">
									<div class="tit5_con">
										方案名称
									</div>
								</div>
								<div class="sele05">操作</div>
								<div class="sele05">时间</div>
								<div class="sele05">姓名</div>
								<div class="sele05 other_width">邮箱</div>
							</dt>
							<c:if test="${!empty list}">
								<c:forEach items="${list}" var="map" varStatus="mun">
									<dd class="cf">
										<div class="dd_tit cf">
											<div class="tit5">
												<div class="tit5_con">
													<label ><input type="checkbox" value="${map.id }" /><i></i></label>
													<c:if test="${map.handle==1 }"><i style="color:green;">[已处理]</i>${map.planName}</c:if>
													<c:if test="${map.handle!=1 }">${map.planName}</c:if>
												</div>
											</div>
											<div class="sele05 sanJi">
												<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<c:if test="${map.handle==1 }"><li><a onclick="updateHandle('${map.id }',0)">已处理</a></li></c:if>
													<c:if test="${map.handle!=1 }"><li><a onclick="updateHandle('${map.id }',1)">未处理</a></li></c:if>
													<li><a onclick="detailById('${map.id }')" href="javascript:;">详情</a></li>
													<li><a onclick="confirmDelDiv('${map.id }')" href="javascript:;">删除</a></li>
												</ul>
											</div>
											<div class="sele05">
												${map.createTime}
											</div>
											<div class="sele05">
												${map.username}
											</div>
											<div class="sele05 other_width">
												${map.email}
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
					</div>
					</div>
				</form>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->

</body>
<script type="text/javascript">
//导入
function improt(key){
	top.jzts();
	var formData=new FormData();
	formData.append("file",$("#file_name_xlsx")[0].files[0]);
	formData.append("key",key);
	 $.ajax({
		type: "POST",
		url:adminPath+"ipcExcel/import.do",
		data:formData,
		processData:false,
        contentType:false,
		success: function(result){
			top.hangge();
			var code=result.code;
			if(code==200){
				 window.top.mesageTip("success","数据导入成功!");
				 location.href=adminPath+"contactPlan/list.do";
			}else if(code==400){
				 window.top.mesageTip("failure","请确保导入的文件内有数据！");
			}else if(code==401){
				 window.top.mesageTip("failure","请确保表单项不为空！");
			}else if(code==402){
				 window.top.mesageTip("failure","请确保文件第"+result.data+"行的列数正确！");
			}else if(code==403){
				 window.top.mesageTip("failure",result.message);
			}else if(code==-1){
				 window.top.mesageTip("failure","上传系统错误，请联系管理员！");
			}
		}
	});
}

</script>


</html>          