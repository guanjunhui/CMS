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
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<a href="<%=adminPath%>site/sitelistPage.do">站点列表</a><i>></i>
			<i>站点维护</i>
		</div>
	</div>
			<form action="<%=adminPath%>site/save.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
				<input type="hidden" name="siteId" id="siteId" value="${pd.siteId }"/>
				<div class="cms_c_list cf">
					<h3>添加站点</h3>
					<div class="add_btn_con wrap cf">
						<h5 class="zp_h5">基本信息</h5>
						<dl class="cf">
							<dt><span class="hot">*</span>站点名称</dt>
							<dd><div class="dd_con"><input type="text" id="siteName" name="siteName" value="${pd.siteName}" required/></div></dd>
						</dl>
						<dl class="cf" style="overflow:inherit;">
							<dt>站点LOGO</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img">
									<label style="float:left;" for="siteLogo"><input type="file" id="siteLogo" onchange="javascript:setImagePreview();" name="siteLogo"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty pd.siteLogo}">
												<i style="float:left;">${pd.siteLogoName}</i>
												<a href="javascript:;" class="remove_file" style="display:inline-block;float:left;">删除</a>
												<div class="yulan" onmouseover="javascript:showImg();" style="float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
														<div class="pro_img_big"><img id="preview" alt="" src="<%=imgPath%>${pd.sitePath}" width="700px" height="150px"></div>
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
						<dl class="cf">
							<dt><span class="hot">*</span>语言版本</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="siteLanguage" name="siteLanguage" required>
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						<!-- <dl class="cf">
							<dt><span class="hot">*</span>首页栏目</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="siteIndex" name="siteIndex" required>
									</select>
								</div>
							</dd>
						</dl> -->
						<dl class="cf">
							<dt><span class="hot">*</span>站点域名</dt>
							<dd><div class="dd_con"><input type="text" id="siteDomain" name="siteDomain" value="${pd.siteDomain}" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>别名域名</dt>
							<dd><div class="dd_con"><input type="text" id="siteSubdomain" name="siteSubdomain" value="${pd.siteSubdomain}" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>备案号</dt>
							<dd><div class="dd_con"><input type="text" id="siteRecordno" name="siteRecordno" value="${pd.siteRecordno}"/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>关键词</dt>
							<dd><div class="dd_con"><input type="text" id="siteKeyword" name="siteKeyword" value="${pd.siteKeyword}"/></div></dd>
						</dl>
						<!----> 
						<dl class="cf" >
							<dt>是否默认</dt>
							<dd>
								<div class="dd_con">
									<label for="deafault_true"><input type="radio" id="deafault_true" value="1" name="ifStatic" /><span>是</span><i></i></label>
									<label for="deafault_false"><input type="radio" id="deafault_false" value="0" name="ifStatic" /><span>否</span><i></i></label>
								</div>
							</dd>
						</dl> 
						<dl class="cf">
							<dt><span class="hot"></span>描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea id="siteDesc" name="siteDesc" class="textarea_numb">${pd.siteDesc }</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
					</div>
					<div class="add_btn_con add_line wrap cf">
						<h5 class="zp_h5">邮箱信息</h5>
						<dl class="cf">
							<dt><span class="hot"></span>邮件地址</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailAddr" name="siteEmailAddr" value="${pd.siteEmailAddr}" class="email"/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>密码</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailPwd" name="siteEmailPwd" value="${pd.siteEmailPwd}" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>发信名称</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailPrefix" name="siteEmailPrefix" value="${pd.siteEmailPrefix}" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>发送服务器（SMTP）</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailSmtp" name="siteEmailSmtp" value="${pd.siteEmailSmtp}" /></div></dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>site/sitelistPage.do" class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
				<input type="hidden" id="siteIndexFb" value="${pd.siteIndex }">
				<input type="hidden" id="siteLanguageFb" value="${pd.siteLanguage }">
				<input type="hidden" id="ifStaticfb" value="${pd.ifStatic}" />
				
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>
<script type="text/javascript">
$(function(){
	getDic();
	//getTemplateTree();
	setdefault();
});

function showImg(){
	$(".pro_img").toggle();
}
//是否静态化
function setdefault(){
	var isdefault=$("#ifStaticfb").val();
	if(isdefault){
		$("input[name='ifStatic']").each(function(){
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

//获取字典数据(语言环境)
function getDic(){
	$.ajax({
		type: "GET",
		url: adminPath+"dictionaries/getDic.do?code=010",
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 var html='';
				 var siteLanguage=$("#siteLanguageFb").val();
				 $.each(result.data, function(i, item){
					 if(item.bianma==siteLanguage){
				         html+='<option value="'+item.bianma +'" selected>'+item.name+'</option>';
					 }else{
				         html+='<option value="'+item.bianma +'">'+item.name+'</option>';
					 }
				 });
				 $("#siteLanguage").append(html);
			 }
		}
	});
}

//获取栏目
function getTemplateTree(){
	$.ajax({
		type: "GET",
		url: adminPath+"columconfig/findColumList.do",
		dataType:'json',
		data:{},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 var html='';
				 var hasDefault=false;
				 var siteIndex=$("#siteIndexFb").val();
				 $.each(result.data, function(i, item){
					 if((siteIndex==null||siteIndex==''||siteIndex==undefined)
							 &&item.hasDefault){
				         html+='<option value="'+item.id +'" selected>默认</option>';
				         hasDefault=true;
					 }else if(siteIndex==item.id){
				         html+='<option value="'+item.id +'" selected>'+item.columName+'</option>';
				         hasDefault=true;
					 }else{
				         html+='<option value="'+item.id +'">'+item.columName+'</option>';
					 }
				 });
				 if(!hasDefault){
					 $("#siteIndex").append('<option value="">请选择</option>');
				 }
				 $("#siteIndex").append(html);
			 }
		}
	});
}

function setImagePreview(avalue) {
    var docObj = document.getElementById("siteLogo");
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

function save(type){
	if(!$("#Form").valid()){
		return false;
	}
    var formData = new FormData($("#Form")[0]);
	$.ajax({
		type: "POST",
		url:adminPath+"site/saveSite.do",
		data:formData,
		dataType:'json',
		cache: false,
        processData: false,
        contentType: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='1'){//继续添加
					 location.href=adminPath+'site/goAddSite.do';
				 }else{
					 location.href=adminPath+'site/sitelistPage.do';
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	});
}
</script>
</html>
