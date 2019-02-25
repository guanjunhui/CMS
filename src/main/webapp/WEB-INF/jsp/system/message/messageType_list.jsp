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

.nub{border:1px solid #747474;width:35px!important;height:20px!important;line-height:20px!important;text-align:center;text-indent:0!important;}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
$(function(){
	/* 搜索  */
	$("#ss").click(function(){
		var $key=$("#key").val();
		$.ajax({
			type: "GET",
			url:adminPath+"mymessageType/getlistTree.do",
			data:"type_keywords="+encodeURI(encodeURI($key)),
			success:function(result){
				var data=result.data;
				var html='';
				if(data==null){
					html+='<div  class="sele06 sanJi sele_" style="text-align:center;padding-top:30px;color:#666;width:100%;">没有您要查询的数据!</div>';
				}else{
					html=apendBody(data,'');
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
function edit(data,sta){
	window.location.href=adminPath+"mymessageType/toEdit.do?id="+data; 
}
function setType_status(data,sta){//
	$.ajax({
		url:adminPath+"mymessageType/updateTypeStatus.do",
		type:"post",
		data:{ids:data,STATUS:sta},
		success:function(result){
			if(result.success){
				window.top.mesageTip("success","修改成功");
				getData();
			}else{
				window.top.mesageTip("failure","修改失败");
			}
		}
	});
}

function batchDel(param){
	hideConfirm();
	$.ajax({
		url:adminPath+"mymessageType/delete.do",
		type:"get",
		data:param,
		success:function(data){
			if(data.success){
				window.top.mesageTip("success","删除成功");
				getData();
			}else{
				window.top.mesageTip("failure","删除失败");
			}
		}
	})
}
function deleteById(data){
	var param="id="+data;
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
}

 function getData(){
	$.ajax({
		type: "GET",
		url:adminPath+"mymessageType/getlistTree.do",
		dataType:'json',
		cache: false,
		success:function(result){
			
			 var html='';
				 var data=result.data;
				 if(data!=null){
					 html=apendBody(data,'',0);
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

function apendBody(list,html,num){
	var num_=num;
	$.each(list,function(i,obj){
		html+='<dd class="cf">';
		html+='<div class="dd_tit cf">';
			html+='<div class="tit6">';
				html+='<div class="tit6_con xiala_btn">';
					for(var m=0;m<num;m++){
						html+='<span class="zhanW2"></span>';
					}	
					if(obj.childList.length!=0){
						html+='<span class="san_ico"></span>';
					}
					html+=obj.type_name;			
				html+='</div>';
			html+='</div>';
			var status=obj.type_status;
			html+='<div class="sele06 sanJi">';
				html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
				html+='<ul class="guanli_con cf">';
					html+='<li><a href="javascript:void(0)" onclick="edit(\''+obj.id+'\')">编辑</a></li>';
					if(status=='1'){
					    html+='<li><a href="javascript:;" onclick="setType_status(\''+obj.id+'\',0)">隐藏</a></li>';
					}else{
						html+='<li><a href="javascript:;" onclick="setType_status(\''+obj.id+'\',1)">显示</a></li>';
					}
					html+='<li><a href="javascript:;" onclick="deleteById(\''+obj.id+'\')">删除</a></li>';
				html+='</ul>';
			html+='</div>';
			var statusText='';
			if(status=='1'){
				statusText='显示';
			}else if(status=='0'){
				statusText='隐藏';
			}
			html+='<div class="sele06">'+statusText+'</div>';
			html+='<div class="sele06">'+obj.count+'</div>';
			html+=setColum(obj.columConfigList,'');
			var sort=obj.sort;
			if(sort==null){
				sort='';
			}
			html+='<div class="sele06"><input type="text" onblur="typeSort(this);"  pai="'+obj.id+'" value="'+sort+'" class="nub" /></div>';
		html+='</div>';
		if(obj.childList.length!=0){
			html+='<dl>';
			num_++;
			html+=apendBody(obj.childList,'',num_);
			html+='</dl>';
		}
		
	html+='</dd>';
	num_=num;
	});
	return html;
}
function setColum(list,html){
	var h="";
	$.each(list,function(index,obj){
		//html+='<div class="sele06 other_width">'+obj.columName+'</div>';
		h+=obj.columName+"&nbsp&nbsp";
	});
	html+='<div class="sele06 other_width">'+h+'</div>';
	return html;
}
//更新sort并显示排序结果
function typeSort(cs){
	var zz=/^[1-9][0-9]*/;
	var sort=$(cs).val();
	var data=$(cs).attr("pai");
	if(zz.test(sort)){
		$.ajax({
			url:adminPath+"mymessageType/updateSort.do",
			type:"post",
			data:{id:data,sort:sort},
			success:function(result){
				if(result.success){
					getData();
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
			<i>></i>
			<a href="<%=adminPath%>mymessageType/list.do">资讯分类</a>
			<i>></i>
			<i>资讯分类</i>
		</div>
	</div>
	<div class="cms_c_list biaoge_con chanpin_con cf">
		<div class="h3 cf">
			<div class="h3_left cf">
				<a href="<%=adminPath%>mymessageType/toAdd.do">+添加分类</a>
				
			</div>
			<span id="result"></span>
			<div class="search">
				<input type="text" placeholder="请输入关键字" id="key"><input type="button" value="搜索" id="ss" />
			</div>
		</div>
		<div class="table cf" >

			<dl class="list_bg col_06 cf" id="start">
				<dt class="cf">
					<div class="tit6">
						<div class="tit6_con">
							分类名称
						</div>
					</div>
					
					<div class="sele06">操作</div>
					<div class="sele06">状态</div>
					<div class="sele06">包含内容</div>
					<div class="sele06 other_width">所属栏目</div>
					<div class="sele06">排序</div>
				</dt>
				<div  id="listPage">
				
				</div>
			</dl>
		</div>
		</div>
	</div>
	<div class="footer">© 中企高呈 版权所有</div>
</div>
<!-- cms_con结束 -->
</body>
</html>