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
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body class="no-skin">
			<div class="cms_c_inst neirong cf">
				<div class="left cf">
					<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
					<a href="<%=adminPath%>customform/list.do">表单列表</a><i>></i>
					<a href="<%=adminPath%>customform/golist.do?formId=${formId}">属性列表</a><i>></i>
					<i>属性维护</i>
				</div>
			</div>
			<form action="<%=adminPath%>customform/save.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id" value="${info.id}" />
				<input type="hidden" id="formId" name="formId" value="${formId}" />
				<input type="hidden" id="defaultattrType" value="${info.attrType}" />
				<div class="cms_c_list zhaopin cf">
					<h3>添加属性</h3>
					<div class="add_btn_con add_line wrap cf">
						<dl class="cf zp_dl">
							<dt><span class="hot">*</span>属性名称</dt>
							<dd><div class="dd_con"><input type="text" id="attrName" name="attrName" value="${info.attrName}" required></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>属性类型</dt>
							<dd>
							  <div class="dd_con">
	                               <select id="attrType" name="attrType" class="form-control" onchange="selectType();" style="width:200px;">
	                                   <option value="0">--请选择--</option>
	                               </select>
                                   <span id="attrTypewarn" class="warn_tip" style="hidden"></span>
                              </div>
                            <dd>
						</dl>
						<dl class="cf zp_dl">
							<dt><span class="hot">*</span>排序值</dt>
							<dd><div class="dd_con"><input type="text" id="attrSort" name="attrSort" value="${info.attrSort}" required class="digits"></div></dd>
						</dl>
						<dl class="cf">
							<dt>是否显示</dt>
							<dd>
								<div class="dd_con">
									<label for="deafault_true"><input type="radio" id="deafault_true" value="1" name="attrStats" /><span>是</span><i></i></label>
									<label for="deafault_false"><input type="radio" id="deafault_false" value="0" name="attrStats" /><span>否</span><i></i></label>
								</div>
							</dd>
						</dl>
						<dl class="cf zp_dl">
							<dt><span class="hot"></span>默认值</dt>
							<dd><div class="dd_con"><input type="text" id="attrDefault" name="attrDefault" value="${info.attrDefault}"></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea id="attrDescription" name="attrDescription" class="textarea_numb">${info.attrDescription}</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>customformattr/list.do?formId=${formId}" class="submit_re_btn">取消</a>
					</div>
				</div>
			</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<input type="hidden" id="attrStatsfb" value="${info.attrStats}" />
</body>
<script type="text/javascript">
$(function(){
	getAttrTypeDate();
	setdefaultStatus();
});
//是否显示
function setdefaultStatus(){
	var isdefault=$("#attrStatsfb").val();
	if(isdefault){
		$("input[name='attrStats']").each(function(){
			if(isdefault==$(this).val()){
				$(this).prop("checked",true);
				$(this).siblings('i').addClass('active');
			}
		});
	}else{
		$("#deafault_true").prop("checked",true);
		$("#deafault_true").siblings('i').addClass('active');
	}
}

//获取栏目类型数据
function getAttrTypeDate(){
     $.ajax({
          type: "GET",
          url:adminPath+"customformattr/getAttrType.do",
          data:{},
          dataType:'json',
          cache: false,
          success: function(result){
               if(result.code==200&&result.data!=null){
                   var html="";
                   var defaultattrType=$("#defaultattrType").val();
                   $.each(result.data,function(index,item){
                       if(item.id==defaultattrType)
  				           html+='<option value="'+item.id +'" selected>'+item.name+'</option>';
                       else
  				           html+='<option value="'+item.id +'">'+item.name+'</option>';
                   });
                   $("#attrType").append(html);
               }
          }
     });
}

function selectType(){
	var attrType=$("#attrType option:selected").val();
	if(attrType!=null&&attrType!=""
			&& attrType!=undefined && attrType!='0'){
	  	$("#attrTypewarn").html("");
	  	$("#attrTypewarn").hide();
	  	return false;
	}
}

function save(type){
	var attrType=$("#attrType option:selected").val();
	if(attrType==null||attrType==""
			||attrType==undefined||attrType=='0'){
	  	$("#attrTypewarn").html("请选择属性类型");
	  	$("#attrTypewarn").show();
	  	return false;
	}
	if(!$("#Form").valid()){
		return false;
	}
    var formData = $("#Form").jsonObject();
    var formId=$("#formId").val();
	$.ajax({
		type: "POST",
		url:adminPath+"customformattr/save.do",
		data:formData,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='1'){//继续添加
					 location.href=adminPath+'customformattr/goEdit.do?formId='+formId;
				 }else{
					 location.href=adminPath+'customformattr/list.do?formId='+formId;
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请联系管理员!");
			 }
		}
	});
}

</script>
</html>
