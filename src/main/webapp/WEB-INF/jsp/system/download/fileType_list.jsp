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
.nub{border:1px solid #747474;width:35px!important;height:20px!important;line-height:20px!important;text-align:center;text-indent:0!important;}
.div_select{height:80px;width:140px;overflow-y:auto;}
.div_select>.label{display:block;}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
$(function(){
	/* 搜索  */
	$("#ss").click(function(){
		var $key=$("#key").val();
		if($key.length==0){
			return;
		}
		$.ajax({
			type: "POST",
			url:adminPath+"downloadType/search.do",
			data:{KEYWORDS:$key},
			success:function(result){
				var data=result.data;
				var html='';
				if(data==null){
					html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
				}else{
					html=apeendBody(data,'',0);
				}
				 $('#listPage').html(html);
				//间隔行色变化
				 $('.biaoge_con .table .dd_tit').each(function(index,el){
					 if(index%2==0){
					 	$(this).addClass('other_bg');
					 }
				 });
			}
		});
	});
	getData();
	
});
function edit(data){
	window.location.href=adminPath+"downloadType/toUpdate?id="+data; 
}

function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}
function batchDel(data){
	hideConfirm();
	$.ajax({
		type: "GET",
		url:adminPath+"downloadType/delete.do",
		data:data,
		dataType:'json',
		cache: false,
		success:function(result){
			if(result.success){
				window.top.mesageTip("success","操作成功");
				
				getData();
			}else{
				window.top.mesageTip("failure","操作失败");
			}
		}
	});
	
}
function getData(){
	var formData=$("#Form").jsonObject();
	$.ajax({
		type: "GET",
		url:adminPath+"downloadType/getListTree.do",
		data:formData,
		dataType:'json',
		cache: false,
		success:function(result){
			 var html='';
				 var data=result.data;
				 if(data==null){
					 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
				 }else{
					 
					 html=apeendBody(data,'',0);
				 }
			 $('#listPage').html(html);
			//间隔行色变化
			 $('.biaoge_con .table .dd_tit').each(function(index,el){
				 if(index%2==0){
				 	$(this).addClass('other_bg');
				 }
			 });
		}
	});
}

function apeendBody(list,html,num){
	var num_=num;
	$.each(list,function(i,obj){
		html+='<dd class="cf">';
		html+='<div class="dd_tit cf">';
			html+='<div class="tit6">';
				html+='<div class="tit6_con xiala_btn">';
					for(var m=0;m<num_;m++){
						html+='<span class="zhanW2"></span>';
					}	
					if(obj.childList.length!=0){
						html+='<span class="san_ico"></span>';
					}
					html+=obj.download_name;
				html+='</div>';
			html+='</div>';
			html+='<div class="sele06 sanJi">';
				html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
				html+='<ul class="guanli_con cf">';
					html+='<li><a href="javascript:void(0)" onclick="edit(\''+obj.download_id+'\')">编辑</a></li>';
					html+='<li><a onclick="deleteById(\''+obj.download_id+'\')" href="javascript:;">删除</a></li>';
				html+='</ul>';
			html+='</div>';
			var status=obj.status;
			var statusText='';
			if(status=='1'){
				statusText='显示';
			}else if(status=='0'){
				statusText='隐藏';
			}
			html+='<div class="sele06 sanJi">'+statusText+'</div>';
			html+='<div class="sele06 sanJi">'+obj.count+'</div>';
			html+='<div class="sele06 sanJi sele_">';
			html+=setColum(obj.columConfigList,'');
			html+='</div>';
			var sort=obj.sort;
			if(sort==null){
				sort='';
			}
			html+='<div class="sele06"><input type="text" onblur="typeSort(this);"  sort="'+obj.download_id+'" value="'+sort+'" class="nub" /></div>';
		html+='</div>';
		if(obj.childList!=null){
			html+='<dl>';
			num_++;
			html+=apeendBody(obj.childList,'',num_);
			html+='</dl>';
		}
	html+='</dd>';
	num_=num;
	});
	return html;
}
function setColum(list,html){
	$.each(list,function(index,obj){
		if(list.length-1==index){
			html+=obj.columName;
		}else{
			html+=obj.columName+',';
		}
	});
	return html;
}
function typeSort(param){
	var zz=/^[1-9][0-9]*$/;
	var sort=$(param).val();
	if(zz.test(sort)){
		$.ajax({
			type: "GET",
			url:adminPath+"downloadType/updateSort.do",
			data:{id:$(param).attr("sort"),sort:$(param).val()},
			success:function(result){
				if(!result.success){
					window.top.mesageTip("failure","操作失败");
				}else{
					//window.top.mesageTip("success","修改成功");
				}
			}
		});
	}else{
		window.top.mesageTip("warn","请输入正整数");
	}
	
}
/* =================================================== */
</script>
</head>
<body>
<!-- cms_con开始 -->
<div class="cms_con cf">

<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
			<i>></i>
			<i>分类管理</i>
		</div>
	</div>

	<div class="cms_c_list biaoge_con chanpin_con cf">
		<div class="h3 cf">
			<div class="h3_left cf">
				<a href="<%=adminPath%>downloadType/toAdd.do">+添加分类</a>
				
			</div>
			<span id="result"></span>
			<div class="search">
				<input type="text" placeholder="请输入关键字"  id="key"><input type="button" value="搜索" id="ss" />
			</div>
		</div>
		<div class="table cf" id="list">

			<dl class="list_bg col_06 cf" id="start">
				<dt class="cf">
					<div class="tit6">
						<div class="tit6_con">
							<!-- <div class="pro_img">缩略图</div> -->
							分类名称
						</div>
					</div>
					<div class="sele06">操作</div>
					<div class="sele06">状态</div>
					<div class="sele06 ">包含内容</div>
					<div class="sele06 sele">所属栏目</div>
					<div class="sele06">排序</div>
				</dt>
				<div  id="listPage">
				
				</div>
			</dl>
			<div class="bottom_con cf">
				
				<div class="page_list cf">
					${page.pageStr}						
				</div>
			
			</div>
	
		</div>
		</div>
	</div>
	<div class="footer">© 中企高呈 版权所有</div>
</div>
<!-- cms_con结束 -->
</body>
</html>