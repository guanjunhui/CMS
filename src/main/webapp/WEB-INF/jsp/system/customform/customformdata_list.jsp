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
	<!-- jsp导航返回栏 -->
			<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<i>表单列表</i>
		</div>
	</div>
	<style>
	.biaoge_con div.table{overflow-x:auto}
	.list_bg.col_03{min-width:1340px;overflow-x:auto}
		.col_03 .sele03{float:left;text-align:center;width:10%;word-wrap:break-word;line-height:1.4;padding:5px 0;}
		.chanpin_con .dd_tit input[type="checkbox"]{position:static;float:left;margin-left:5px;margin-top:6px;}
		.chanpin_con .col_03 dt input[type="checkbox"]{visibility:hidden;position:static;float:left;margin-left:5px;margin-top:6px;}
		.dd_tit label input{visibility:inherit;}
		.chanpin_con .dd_tit q{float:left;padding-left:10px;}
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
	<form action="<%=adminPath%>customformdata/list.do?formId=${formId}" method="post" name="Form" id="Form">
		<input type="hidden" name="formId" id="formId_data" value="${formId}">
		<input type="hidden" name="customform_keyword" id="customform_keyword" value="${customform_keyword}">
		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<div class="a_btn cf">
						<a href="<%=adminPath%>customformdata/exprotTemplate.do?formId=${formId}"><img src="static/images/chu.png" alt="">获取导入模板</a>
						<p href="javascript:;" class="file_upload_xlsx">
							<img src="static/images/ru.png" alt=""> 
							<label for="file_name_xlsx">
							<input type="file" name="file" id="file_name_xlsx"><em>导入</em></label><i></i>
							<a href="javascript:;" onclick="improt('${formId}')" class="upl_file" style="display: none;">确定上传</a>
							<a href="javascript:;" class="remove_file" style="display: none;">取消上传</a>
						</p>
						<a href="javascript:;" onclick="exportData('${formId}')"><img src="static/images/chu.png" alt="">导出</a>
					</div>
				</div>
				<div class="search">
					<input type="text" placeholder="请输入搜索内容" id="key"><input id="ss" type="button" value="搜索" />
				</div>
			</div>
			<div class="table cf">
				<dl class="list_bg col_03 cf">
					<dt class="cf">
						<input type="checkbox" value=""/>
						<c:if test="${not empty info.recordHead}">
							<c:forEach items="${info.recordHead}" var="obj" varStatus="vs">
								<c:if test="${vs.count<13}">
									<div class="sele03">${obj.name}</div>
								</c:if>
							</c:forEach>
								<div class="sele03">操作</div>
						</c:if>
					</dt>
					<c:choose>
						<c:when test="${not empty info.recordData}">
							<c:forEach items="${info.recordData}" var="datalist" varStatus="vs">
								<dd class="cf">
									<div class="dd_tit cf">
										<c:forEach items="${datalist}" var="data" varStatus="go">
											<c:if test="${go.first==true}">
												<input type="checkbox" value="${data.creatTime}"/>
										 	</c:if>
											<c:if test="${go.count<13}">
												<div class="sele03">
													${data.name}
												</div>
											</c:if>
											<c:if test="${go.last}">
												<div class="sele03">
													<input type="button" class="submit_btn right" style="margin-left:25%" value="修改" onclick="updateById('${formId}','${data.creatTime}')" aria-invalid="false">
												</div>
											</c:if>
										</c:forEach>
									</div>
								</dd>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>
						</c:otherwise>
					</c:choose>
					
				</dl>
				<div class="bottom_con cf">
					<div class="all_checkbox">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
						<a href="javascript:;" onclick="confirmDel('${formId}')">删除</a>
					</div>
					<div class="page_list cf">
						${page.pageStr}
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
	//全选按钮
	$(document).on('change','.all_checkbox input',function(){
		if(this.checked){   
		    $(".dd_tit :checkbox").prop("checked", true);
		}else{   
		    $(".dd_tit :checkbox").prop("checked", false);
	    }   
	});
    //设置全选复选框
    $(".dd_tit :checkbox").click(function(){
    	allchk();
  	});
})

function updateById(formId,creatTime){
	 if(formId==0 && formId=='' && creatTime==0 && creatTime==''){
			return false;
		}
	 window.location.href=adminPath+"customformdata/goEdit.do?formId="+formId+'&creatTime='+creatTime; 
}

</script>

<script type="text/javascript">
$(function(){
	top.hangge();
});
function allchk(){
    var chknum = $(".dd_tit :checkbox").size();//选项总个数
    var chk = 0;
    $(".dd_tit :checkbox").each(function () {  
      if($(this).prop("checked")==true){
        chk++;
      }
    });
    if(chknum==chk){//全选
        $(".all_checkbox i").addClass('active');
        $(".all_checkbox input").prop("checked",true);
    }else{//不全选
        $(".all_checkbox i").removeClass('active')
        $(".all_checkbox input").prop("checked",false);
    }
  }

//导入
function improt(formId){
	top.jzts();
	var formData=new FormData();
	formData.append("file",$("#file_name_xlsx")[0].files[0]);
	formData.append("formId",formId);
	 $.ajax({
		type: "POST",
		url:adminPath+"customformdata/import.do",
		data:formData,
		processData:false,
        contentType:false,
		success: function(result){
			top.hangge();
			var code=result.code;
			if(code==200){
				 window.top.mesageTip("success","数据导入成功!");
				 location.href=adminPath+"customformdata/list.do?formId="+formId;
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

//导入
function exportData(formId){
	location.href=adminPath+"customformdata/exportData.do?formId="+formId;
}
//删除
function confirmDel(formId){
	var ids=$(".dd_tit [type='checkbox']:checked");
	if(ids.length==0){
		window.top.mesageTip("warn","请选择删除的选项");
		return false;
	}
	var param=[];
	$.each(ids,function(index,obj){
		param.push(obj.value);
	});
	var	title='确认批量删除所选信息吗？';
	var	content='此操作会删除所选中数据';
	mesageConfirm('删除信息',title,content,"batchDel('"+param.join(',')+"','"+formId+"')");
}
function batchDel(param,formId){
	hideConfirm();
    $.ajax({
		type: "POST",
		url:adminPath+"customformdata/delete.do",
		dataType:"json",
		data:{"dates":param},
		success: function(result){
			var code=result.code;
			if(code==200){
				 window.top.mesageTip("success","数据删除成功!");
				 location.href=adminPath+"customformdata/list.do?formId="+formId;
			}else{
				 window.top.mesageTip("failure","数据删除失败，请联系管理员！");
			}
		}
	});
}
function SF(id,audit,formId){
	$.ajax({
		type: "POST",
		url:adminPath+"customformdata/updataAudit.do",
		data:{ids:id,audit:audit},
		success: function(result){
			 if(result.success){
				 window.top.mesageTip("success","操作成功");
				 location.href=adminPath+"customformdata/list.do?formId="+formId; 
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
/* 根据关键词搜索  */
$("#ss").click(function(){
	var formId=$("#formId_data").val();
	var $id=$("#key").val();
	$id=$.trim($id);
	window.location.href=adminPath+"customformdata/list.do?customform_keyword="+encodeURI(encodeURI($id))+"&formId="+formId;
});
</script>
</html>
