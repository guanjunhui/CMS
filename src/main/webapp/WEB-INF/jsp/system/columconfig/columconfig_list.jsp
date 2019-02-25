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

<style>
@media only screen and (max-width: 1366px){
  .col_07 .tit7{margin-right: -730px;}
  .col_07 .tit7 .tit7_con{margin-right: 730px;}
  .col_07 .sele07.other_width{width: 130px;}
}
</style>
</head>
<body class="no-skin">
	<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<a href="<%=adminPath%>columgroup/list.do">栏目组</a><i>></i>
			<i>栏目列表</i>
		</div>
	</div>
	<form action="<%=adminPath%>columconfig/list.do" id="Form" method="post">
		<input type="hidden" id="COLUMGROUP_ID" name="COLUMGROUP_ID" value="${pd.COLUMGROUP_ID}" />
		<div class="cms_c_list biaoge_con cf">
			<div class="h3 cf">
				<div class="h3_left">
					<a href="<%=adminPath%>columconfig/goAdd.do?COLUMGROUP_ID=${pd.COLUMGROUP_ID}">+添加栏目</a>
					<div class="selec_co cf">
						<select id="templateType" class="form-control">
							<option value="">请选择栏目类型</option>
							<option value="6">首页栏目</option>
							<option value="1">内容栏目</option>
							<option value="2">资讯栏目</option>
							<option value="3">产品栏目</option>
							<option value="4">招聘栏目</option>
							<option value="5">下载栏目</option>
						</select>
					</div>
				</div>
				<div class="search">
					<input type="text" id="COLUM_NAME" name="COLUM_NAME"><input onclick="getData();" type="button" value="搜索" />
				</div>
			</div>
			<div class="table zhandian_con cf">
				<dl class="list_bg col_07 cf">
					<dt class="cf">
						<div class="tit7">
							<div class="tit7_con">
								栏目名称
							</div>
						</div>
						<div class="sele07 ">操作</div>
						<div class="sele07 ">添加时间</div>
						<div class="sele07 ">栏目类型</div>
						<div class="sele07 ">栏目状态</div>
						<div class="sele07 ">栏目顺序</div>
						<div class="sele07 other_width">栏目描述</div>
					</dt>
				</dl>
			</div>
		</div>
	</form>
		</div>
		<div class="footer">© 中企高呈 版权所有</div>
</body>

<script type="text/javascript">
$(function(){
	getData();
	
});	
/* 选择模板类型 */
$(function(){
	top.hangge();
	$('#templateType').change(function(){
		var type=$(this).children('option:selected').val();
		var group_id =$("#COLUMGROUP_ID").val();
		var information="TEM_TYPE="+type;
		$.ajax({
			type: "POST",
			url:adminPath+"columconfig/list.do",
			data:{"TEM_TYPE":type,"COLUMGROUP_ID":group_id},
			dataType:'json',
			cache: false,
			success:function(result){
				$(".list_bg").find("dd").remove();
				$(".list_bg").find(".error").remove();
				 var html='';
				 if(result.code==200){
					 var data=result.data;
					 var list=data.list;
					 if(list==null||list.length<=0){
						 html='<div class="error" style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>';
					 }else{
						 html=apeendBody(list,'');
					 }
				 }else{
					 html='<div class="error" style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">程序发生异常，请联系管理员！ </div>';
				 }
				 $('.list_bg').append(html);
				 //间隔行色变化
				 $('.biaoge_con .table .dd_tit').each(function(index,el){
					 if(index%2==0){
					 	$(this).addClass('other_bg');
					 }
				 });
			}
		});
	});
	$("#templateType").val('${pd.TEM_TYPE}');
});
function getData(){
	$(".list_bg").find("dd").remove();
	$(".list_bg").find(".error").remove();
	var formData=$("#Form").jsonObject();
	$.ajax({
		type: "POST",
		url:adminPath+"columconfig/list.do",
		data:formData,
		dataType:'json',
		cache: false,
		success:function(result){
			 var html='';
			 if(result.code==200){
				 var data=result.data;
				 var list=data.list;
				 if(list==null||list.length<=0){
					 html='<div class="error" style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>';
				 }else{
					 html=apeendBody(list,'');
				 }
			 }else{
				 html='<div class="error" style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">程序发生异常，请联系管理员！ </div>';
			 }
			 $('.list_bg').append(html);
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
		var status=obj.columDisplay;
		html+='<dd class="cf">';
			html+='<div id="div_'+obj.id+'" class="dd_tit cf" name="indexDiv">';
				html+='<div class="tit7">';
					html+='<div class="tit7_con xiala_btn">';
						var count=obj.childLevel;
						for(var m=0;m<count;m++){
							html+='<span class="zhanW2"></span>';
						}	
						if(obj.subConfigList!=null){
							html+='<span class="san_ico"></span>';
						}
						html+=obj.columName;
					html+='</div>';
				html+='</div>';
				html+='<div class="sele07 sanJi">';
					html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
					html+='<ul class="guanli_con cf">';
						html+='<li><a href="javascript:void(0);" onclick="edit(\''+obj.id+'\')">编辑</a></li>';
						if(status=='1'){
							html+='<li><a href="javascript:void(0);" onclick="changeStatus(\''+obj.id+'\',\''+status+'\')">隐藏</a></li>';
						}else{
							html+='<li><a href="javascript:void(0);" onclick="changeStatus(\''+obj.id+'\',\''+status+'\')">显示</a></li>';
						}
						html+='<li><a href="javascript:void(0);" onclick="confirmDiv(\''+obj.id+'\',\''+obj.columName+'\')">删除</a></li>';
					html+='</ul>';
				html+='</div>';
				html+='<div class="sele07 sanJi">'+obj.createtime+'</div>';
				var tmplTypeText="";
				var templateType=obj.columType;
				if(templateType=='1'){
					tmplTypeText='内容栏目';
				}else if(templateType=='2'){
					tmplTypeText='资讯栏目';
				}else if(templateType=='3'){
					tmplTypeText='产品栏目';
				}else if(templateType=='4'){
					tmplTypeText='招聘栏目';
				}else if(templateType=='5'){
					tmplTypeText='下载栏目';
				}else if(templateType=='6'){
					tmplTypeText='首页栏目';
				}
				html+='<div class="sele07 sanJi">'+tmplTypeText+'</div>';
				var statusText='';
				if(status=='1'){
					statusText='显示';
				}else if(status=='0'){
					statusText='隐藏';
				}
				html+='<div class="sele07 sanJi">'+statusText+'</div>';
				
				var sort=obj.sort;
				html+='<div class="sele07 sanJi "><input type="text" style="width:50px; height:22px;" id="SORT" name="SORT" onBlur="updateColumSort()" value="'+sort+'"></div>';
				
				var desc=''
				if(obj.columDesc){
					desc=obj.columDesc;
				}
				
				html+='<div class="sele07 sanJi other_width">'+desc+'</div>';
			html+='</div>';
			if(obj.subConfigList!=null){
				html+='<dl>';
				html+=apeendBody(obj.subConfigList,'');
				html+='</dl>';
			}
		html+='</dd>';
	});
	return html;
}

function edit(id){
	var groupId=$("#COLUMGROUP_ID").val();
	window.location.href=adminPath+'columconfig/goAdd.do?ID='+id+"&COLUMGROUP_ID="+groupId;
}
//显示、隐藏
function changeStatus(id,status){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/changestatus.do",
		data:{"COLUM_DISPLAY":status,"ID":id},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				 window.top.mesageTip("success","状态修改成功!");
				 var id=$("#COLUMGROUP_ID").val();
				 location.href="<%=adminPath%>columconfig/golist?COLUMGROUP_ID="+id;	 
			}else{
				 window.top.mesageTip("failure","状态修改失败!");
			}
		}
	});
}

function confirmDiv(id,name){
	var title='确认删除栏目【'+name+'】吗？';
	var content='此操作会删除该栏目以及此栏目下的所有内容数据！';
	mesageConfirm('删除栏目',title,content,"deleteColum('"+id+"')");
}

//删除
function deleteColum(id){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/delColumconfig.do",
		data:{"ID":id},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				 window.top.mesageTip("success","栏目删除成功!");
				 var id=$("#COLUMGROUP_ID").val();
				 location.href="<%=adminPath%>columconfig/golist.do?COLUMGROUP_ID="+id;	 
			}else{
				 window.top.mesageTip("failure","栏目删除失败!");
			}
		}
	});
}

function updateColumSort(){
	 var list=new Array();
	 $("div[name='indexDiv']").each(function(){
		var columSort=new Object();
		var id= $(this).attr("id");
		id=id.split("_")[1];
		//alert(id);
		var sort=$(this).find("input").val();
		columSort.ID=id;
		columSort.SORT=sort;
		list.push(columSort);
	 });
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/updateColumSort.do",
		data:{"list":JSON.stringify(list)},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200){
				 //var id=$("#COLUMGROUP_ID").val();
				 //location.href="<%=adminPath%>columconfig/golist.do?COLUMGROUP_ID="+id;	 
			}else{
				 window.top.mesageTip("failure","栏目修改失败!");
			}
		}
	});
}
</script>
</html>
