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
	String imgPath = path+"/../uploadFiles/uploadImgs/";
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
<div class="cms_c_inst neirong cf">
	<div class="left cf">
		<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
		<a href="<%=adminPath%>template/list.do">模板列表</a><i>></i>
		<i>模板维护</i>
	</div>
</div>
			<form action="<%=adminPath%>template/save.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
				<input type="hidden" id="ID" name="ID" value="${pd.ID}" />
				<input type="hidden" id="ifAppendSave" name="ifAppendSave"/>
				<div class="cms_c_list zhaopin cf">
					<h3>编辑内容</h3>
					<div class="add_btn_con add_line wrap cf">
						<dl class="cf">
							<dt><span class="hot">*</span>模板名称</dt>
							<dd><div class="dd_con"><input type="text" id="TEM_NAME" name="TEM_NAME" value="${pd.TEM_NAME}" class="required" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>模板路径</dt>
							<dd><div class="dd_con"><input type="text" id="TEM_FILEPATH" name="TEM_FILEPATH" value="${pd.TEM_FILEPATH}" required/></div></dd>
						</dl>
						<!-- <dl class="cf">
							<dt><span class="hot">*</span>模板类型</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="TEM_TYPE" name="TEM_TYPE" required>
										<option value="">请选择</option>
										<option value="6">首页模板</option>
										<option value="1">内容模板</option>
										<option value="2">资讯模板</option>
										<option value="3">产品模板</option>
										<option value="4">招聘模板</option>
										<option value="5">下载模板</option>
									</select>
								</div>
							</dd>
						</dl> -->
						<dl class="cf">
							<dt>是否默认</dt>
							<dd>
								<div class="dd_con">
									<label for="deafault_true"><input type="radio" id="deafault_true" value="1" name="IS_DEFAULT" /><span>是</span><i></i></label>
									<label for="deafault_false"><input type="radio" id="deafault_false" value="0" name="IS_DEFAULT" /><span>否</span><i></i></label>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>类型</dt>
							<dd>
								<div class="dd_con">
									<label for="type_true"><input type="radio" id="type_true" value="1" name="TYPE" /><span>列表</span><i></i></label>
									<label for="type_false"><input type="radio" id="type_false" value="2" name="TYPE" /><span>详情</span><i></i></label>
								</div>
							</dd>
						</dl>
						<dl class="cf" style="overflow:inherit;">
							<dt>封面图</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label for="TEM_IMAGEPATH" style="float:left;">
										<input type="file" id="TEM_IMAGEPATH" onchange="javascript:setImagePreview();" name="TEM_IMAGEPATH"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty pd.TEM_IMAGE_NAME}">
												<i style="float:left;">${pd.TEM_IMAGE_NAME}</i>
												<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
												<div class="yulan" onmouseover="javascript:showImg();" style="float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
														<div class="pro_img_big"><img id="preview" alt="" src="<%=imgPath%>${pd.TEM_IMAGE_PATH}" width="150px" height="180px"></div>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<i style="float:left;">未选择文件</i>
												<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>
												<div class="yulan" onmouseover="javascript:showImg();" style="display:none;float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
														<div class="pro_img_big"><img id="preview" alt="" width="150px" height="180px"></div>
													</div>
												</div>
											</c:otherwise>
										</c:choose>
									</li>
								</ul>
								</div>
							</dd>
						</dl>
						
					</div>
					<div class="all_btn cf">
						<input type="button" class="submit_btn" onclick="save('0');" value="保存" />
						<a href="javascript:void(0)"  onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>template/list.do" class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<input type="hidden" id="temTypefb" value="${pd.TEM_TYPE}"/>
			<!-- 弹窗-->
			<div class="layer_bg layer_bg02" style="display:none;">
				<div class="layer_con cf tishi">
					<p><span class="ico bg-tishi02"></span><em>取消置顶成功！</em></p>
				</div>
			</div>
			<input type="hidden" id="isdefaultfb" value="${pd.IS_DEFAULT}" />
			<input type="hidden" id="defaultType" value="${pd.TYPE}"/>
			
</body>
<script type="text/javascript">
$(function(){
	/* var temTypefb=$("#temTypefb").val();
	if(temTypefb){
		$("#TEM_TYPE").val(temTypefb);
	} */
	setdefault();
	setdefaultType();
});
function showImg(){
	$(".pro_img").toggle();
}
function setImagePreview(avalue) {
    var docObj = document.getElementById("TEM_IMAGEPATH");
    var imgObjPreview = document.getElementById("preview");
    if(docObj.files && docObj.files[0])
    {
        //火狐下，直接设img属性
        //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
        imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
    }
    else
    {
        //IE下，使用滤镜
        docObj.select();
        var imgSrc = document.selection.createRange().text;
        var localImagId = document.getElementById("localImag"); //必须设置初始大小
        try {
            localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
            localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
        } catch(e) {
            return false;
        }
        imgObjPreview.style.display = 'none';
        document.selection.empty();
    }
    return true;
}

//是否默认
function setdefault(){
	var isdefault=$("#isdefaultfb").val();
	if(isdefault){
		$("input[name='IS_DEFAULT']").each(function(){
			if(isdefault==$(this).val()){
				$(this).prop("checked",true);
				$(this).siblings('i').addClass('active');
			}
		});
	}else{
		$("#deafault_false").prop("checked",true);
		$("#deafault_false").siblings('i').addClass('active');
	}
}

//类型默认
function setdefaultType(){
	var defaultType=$("#defaultType").val();
	$("input[name='TYPE']").each(function(){
		if(defaultType==$(this).val()){
			$(this).prop("checked",true);
			$(this).siblings('i').addClass('active');
		}
	});
}


function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	$("#ifAppendSave").val(type);
	$("#Form").submit();
    /* 暂时保留异步添加
    var formData = new FormData($("#Form")[0]);
	$.ajax({
		type: "POST",
		url:adminPath+"template/save.do",
		data:formData,
		dataType:'json',
		cache: false,
        processData: false,
        contentType: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='1'){//继续添加
					 location.href=adminPath+'template/goEdit.do';
				 }else{
					 location.href=adminPath+'template/list.do';
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	}); */
}
</script>
</html>
