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
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body class="no-skin">
			<div class="cms_c_inst neirong cf">
				<div class="left cf">
					 <a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
					<a href="<%=adminPath%>columgroup/list.do">栏目组</a><i>></i>
					<a href="<%=adminPath%>columconfig/golist.do?COLUMGROUP_ID=${pd.COLUMGROUP_ID}">栏目列表</a><i>></i> 
					<%--<a href="javascript:location.href='<%=adminPath%>columcontent_colum/golist?ID=${pd.COLUMGROUP_ID}'">栏目列表</a><i>></i>--%>
					<i>栏目维护</i>
				</div>
			</div>
			<form action="<%=adminPath%>columconfig/save.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
				<input type="hidden" id="ID" name="ID" value="${pd.ID}" />
				<input type="hidden" id="COLUMGROUP_ID" name="COLUMGROUP_ID" value="${pd.COLUMGROUP_ID}" />
				<input type="hidden" id="SORT" name="SORT" value="${pd.SORT}" />
				<input type="hidden" id="ifAppendSave" name="ifAppendSave"/>
				<input type="hidden" id="COLUM_TEMPLATETID" name="COLUM_TEMPLATETID" value="${pd.COLUM_TEMPLATETID}"/>
				<input type="hidden" id="TEMPLATET_DETAIL_ID" name="TEMPLATET_DETAIL_ID" value="${pd.TEMPLATET_DETAIL_ID}"/>
				<%-- <input type="hidden" id="COLUM_TYPE" name="COLUM_TYPE" value="${pd.COLUM_TYPE}"/> --%>
				
				<div class="cms_c_list cf">
					<h3>添加栏目</h3>
					<div class="add_btn_con wrap cf">
						<dl class="cf">
							<dt>栏目聚合模板</dt>
							<%-- <c:if test="${empty pd.ID}"> --%>
								<dd style="width:auto"><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn01">+选择</a><span id="templatewarn" class="warn_tip"></span></div></dd>
							<%-- </c:if> --%>
							<dd>
								<div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">栏目模板</td><td>操作</td></tr></thead>
										<tbody id="relationBody">
										</tbody>
									</table>
									</div>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>栏目详情模板</dt>
							<%-- <c:if test="${empty pd.ID}"> --%>
								<dd style="width:auto"><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn03">+选择</a><span id="templatedetailwarn" class="warn_tip"></span></div></dd>
							<%-- </c:if> --%>
							<dd>
								<div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">栏目模板</td><td>操作</td></tr></thead>
										<tbody id="columDetailBody">
										</tbody>
									</table>
									</div>
								</div>
							</dd>
						</dl>
						
						<%--<dl class="cf">
							<dt><span class="hot">*</span>栏目类型</dt>
							<dd>
							  <div class="dd_con">
                               <select id="COLUM_TYPE" name="COLUM_TYPE" class="form-control" style="width:200px;" onchange="listenStatusOfColumType()">
                                   <option value="-1">--请选择--</option>
                               </select>
                                <span id="columTypewarn" class="warn_tip" style="hidden"></span>
                              </div>
                            <dd>
						</dl>--%>
						<dl class="cf">
							<dt><span class="hot">*</span>所属栏目</dt>
							<dd style="width:auto"><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a><span id="columwarn" class="warn_tip"></span></div></dd>
							<dd>
								<div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">所属栏目</td><td>操作</td></tr></thead>
										<tbody id="columBody">
										</tbody>
									</table>
									</div>
								</div>
							</dd>
							<input type="hidden" id="PARENTID" name="PARENTID" value="${pd.PARENTID}"/>
						</dl>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot">*</span>栏目标题</dt>
								<dd><div class="dd_con"><input type="text" id="COLUM_NAME" name="COLUM_NAME" value="${pd.COLUM_NAME}" required></div></dd>
								<dd class="zp_dd">
									<label for="wlink" id="outurllabel"><input id="wlink" name="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
									<div class="dd_con"><input type="text" style="width:300px;" id="OUT_URL" name="OUT_URL" value="${pd.OUT_URL }" class="url"></div>
								</dd>
							</dl>
						</div>
						<dl class="cf">
							<dt><span class="hot"></span>栏目副标题</dt>
							<dd><div class="dd_con"><input type="text" id="COLUM_SUBNAME" name="COLUM_SUBNAME" value="${pd.COLUM_SUBNAME}"  /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>栏目URL名称</dt>
							<dd><div class="dd_con"><input type="text" id="COLUM_URLNAME" name="COLUM_URLNAME" value="${pd.COLUM_URLNAME}"  /></div></dd>
						</dl>
						<input type="hidden" id="columTypefb" value="${pd.COLUM_TYPE}"/>
						<dl class="cf">
							<dt><span class="hot">*</span>栏目类型</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="COLUM_TYPE" name="COLUM_TYPE" required>
										<option value="">请选择</option>
										<option value="1">内容栏目</option>
										<option value="2">资讯栏目</option>
										<option value="3">产品栏目</option>
										<option value="4">招聘栏目</option>
										<option value="5">下载栏目</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<dl class="cf" style="overflow:inherit;">
							<dt>栏目图片</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img">
									<label for="COLUM_IMAGE" style="float:left;"><input type="file" id="COLUM_IMAGE" onchange="javascript:setImagePreview();" name="COLUM_IMAGE"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty pd.PIC_NAME}">
												<i style="float:left;">${pd.PIC_NAME}</i>
												<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
												<div class="yulan" onmouseover="javascript:showImg();" style="float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
														<div class="pro_img_big"><img id="preview" alt="" src="<%=imgPath%>${pd.PIC_PATH}" width="150px" height="180px"></div>
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
						 <dl class="cf" id="myDl">
							<dt><span class="hot"></span>视频内容</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addfilm">上传视频</a>&nbsp;&nbsp;<i style="color: gray;">仅支持mp4后缀</i></div></dd>
							<c:choose>
								<c:when test="${not empty pd.videoName}">
									<dd>
										<div class="dd_con">
											<div class="add_img cf">
												<ul>
													<li class="file_upload file_upload_film">
														<span>视频</span>
															<label for="file_name_film">
																<input type="file" id="file_name_film" name="sultipartFiles"><em>上传</em>
															</label>
														<i>${pd.videoName}</i>
															<a href="javascript:;" class="remove_file" style="display:none;">删除</a>
													</li>
													<li>
														<p><span>标题</span><input type="text" name="video_title" value="${pd.videoTitle}"></p>
														<p><span>链接</span><input type="text" name="tourl" class="url" value="${pd.videoToUrl}"></p>
													</li>
													<li><span>描述</span>
														<h6><textarea class="textarea_numb" name="video_content">${pd.videoContent}</textarea>
															<p><span class="word">0</span><span>/</span><span>200</span></p>
														</h6>
													</li>
											</ul>
											<div class="close_add">x</div>
										</div>
									</div>
								</dd>
							</c:when>
							</c:choose>			
						</dl> 
						<script>
							$(function(){			
								//添加视频
								  $(document).on('click','.layer_btn_addfilm',function(){
									 var num= $("#myDl input[type='file']");
									  if(num.length==1){
										  return;
									  }else{
								  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film"><input type="file" id="file_name_film" name="sultipartFiles"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text" name="video_title"></p><p><span>链接</span><input type="text" name="tourl" class="url"></p></li><li><span>描述</span><h6><textarea class="textarea_numb" name="video_content"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
								  
								  	$(this).parents('dl').append(img_list);}
								  })
								  //关闭图片视频上传功能
								  $(document).on('click','.close_add',function(e){
								  	$(this).parents('dd').remove();
								  	e.stopPropagation();
								  });
							})
						</script>
						<dl class="cf">
							<dt><span class="hot"></span>描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea id="COLUM_DESC" name="COLUM_DESC" class="textarea_numb">${pd.COLUM_DESC }</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>状态</dt>
							<dd>
								<div class="dd_con show_hide">
									<label for="show"><input type="radio" id="show" value="1" name="COLUM_DISPLAY" /><span>显示</span></label>
									<label for="hide"><input type="radio" id="hide" value="0" name="COLUM_DISPLAY" /><span>隐藏</span></label>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>是否设为首页</dt>
							<dd>
								<div class="dd_con show_hide">
									<label for="no"><input type="radio" id="no" value="0" name="INDEX_STATUS" /><span>否</span></label>
									<label for="yes"><input type="radio" id="yes" value="1" name="INDEX_STATUS" /><span>是</span></label>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>SEO_标题</dt>
							<dd><div class="dd_con"><input type="text" id="SEO_TITLE" name="SEO_TITLE" value="${pd.title}"  /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>SEO_关键字</dt>
							<dd><div class="dd_con"><input type="text" id="SEO_KEYWORDS" name="SEO_KEYWORDS" value="${pd.keywords}"  /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>SEO_描述</dt>
							<dd><div class="dd_con"><input type="text" id="SEO_DESCRIPTION" name="SEO_DESCRIPTION" value="${pd.description}"  /></div></dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" onclick="save('0');" class="submit_btn" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>columconfig/golist.do?COLUMGROUP_ID=${pd.COLUMGROUP_ID}" class="submit_re_btn">取消</a>
					</div>
					
					<!-- 模板弹窗——相关内容 -->
					<div id="layer_bg08" class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择聚合模板</span><p class="close">x</p></h3>
								<div class="layer_list mScrol cf">
									<ul id="columTemplatediv" class="radio_lock">
										<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的模板，请添加模板后，再操作</div>
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="setTemplateId();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);" class="submit_re_btn" onclick="colseTemplate(this);">取消</a>
								</div>
						</div>
					</div>
					<div id="layer_bg09" class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择详情模板</span><p class="close">x</p></h3>
								<div class="layer_list mScrol cf">
									<ul id="columTemplateDetaildiv" class="radio_lock">
										<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的模板，请添加模板后，再操作</div>
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="setTemplateDetailId();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);" class="submit_re_btn" onclick="colseTemplate(this);">取消</a>
								</div>
						</div>
					</div>
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close">x</p></h3>
							<div class="layer_list_other mScrol222 cf">
								<ul id="selColumul">
									<li>
										<p><label for="cc_0"><input type="radio" name="selcolum" id="cc_0" value="0"/><span>作为顶级栏目</span><i></i></label></p>
									</li>
								</ul>
							</div>
							<div class="all_btn cf">
								<input type="button" onclick="setColumParentId();" class="submit_btn" value="确定" />
								<a href="javascript:void(0)" class="submit_re_btn" onclick="colseColum(this);">取消</a>
							</div>
						</div>
					</div>
				</div>
			</form>
				<input type="hidden" id="defaultTemplate" value="${pd.COLUM_TEMPLATETID}">
				<input type="hidden" id="defaultTemplateDetailId" value="${pd.TEMPLATET_DETAIL_ID}">
				<input type="hidden" id="defaultParentId" value="${pd.PARENTID}">
				<input type="hidden" id="defaultColumDisplay" value="${pd.COLUM_DISPLAY}">
				<input type="hidden" id="defaultColumStatus" value="${pd.INDEX_STATUS}">
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>
<script type="text/javascript">
$(function(){
	var columTypefb=$("#columTypefb").val();
	if(columTypefb){
		$("#COLUM_TYPE").val(columTypefb);
	}
	
	setColumTemplatediv();
	setColumdiv();//获取栏目
	defaultWlink();
	defaultStatus();
	defaultColumIndexStatus();
	$('.layer_btn01').click(function(){
	  	$('#layer_bg08').show(); //显示模板弹窗
    });
	$('.layer_btn03').click(function(){
	  	$('#layer_bg09').show(); //显示模板弹窗
    });

	$('.layer_btn02').click(function(){
	  	$('.layer_bg02').show();
    });
	
	$("#wlink").on('change',function(){
		if($(this).attr("checked")!='checked'){
			$("#OUT_URL").val('');
		}
	});
	//getcolumTypeDate();栏目类型选择框废弃
});


function showImg(){
	$(".pro_img").toggle();
}

function setImagePreview(avalue) {
    var docObj = document.getElementById("COLUM_IMAGE");
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

//显示与隐藏
function defaultStatus(){
	var selectedVal=$("#defaultColumDisplay").val();
	if(selectedVal==null||selectedVal==''||selectedVal==undefined){
		selectedVal='1';
	}
	$(":input[name='COLUM_DISPLAY']").each(function(){
		if($(this).val()==selectedVal){
			$(this).attr("checked","checked");
			$(this).parents().addClass('active');
		}
	});
}

//是否设为首页 
function defaultColumIndexStatus(){
	var selectedVal=$("#defaultColumStatus").val();
	if(selectedVal==null||selectedVal==''||selectedVal==undefined){
		selectedVal='0';
	}
	$(":input[name='INDEX_STATUS']").each(function(){
		if($(this).val()==selectedVal){
			$(this).attr("checked","checked");
			$(this).parents().addClass('active');
		}
	});
}

function defaultWlink(){
	var outUrl=$("#OUT_URL").val();
	if(outUrl){
		$("#wlink").prop("checked",true);
		$("#wlink").siblings('i').addClass('active');
		$("#wlink").parent().next().show();
	}
}

//获取栏目树形结构
function setColumdiv(){
	var COLUMGROUP_ID=$("#COLUMGROUP_ID").val();
	var ID=$("#ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/getTree.do",
		data:{"COLUMGROUP_ID":COLUMGROUP_ID,"ID":ID},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendColumdiv(result.data);
				var columId=$("#defaultParentId").val();
				defaultColum(columId);
			}
		}
	});
}

//选择栏目弹出内容填充
function appendColumdiv(list){
	 var COLUMGROUP_ID=$("#COLUMGROUP_ID").val();
	 var html='';
	 $.each(list,function(index,obj){
		 if(obj.id!=COLUMGROUP_ID) return true;
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" name="selcolum" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#selColumul").append(html);
	 //控制折叠按钮是否显示
     $('.layer_list_other li .show_btn').each(function(){
    	if($(this).parent('p').next('dl').size()==0){
    		$(this).hide();
    	}
     });
}

function eachSubList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" name="selcolum" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

//获取模板类型树形结构
function setColumTemplatediv(){
	var groupId=$("#COLUMGROUP_ID").val();
	$.ajax({
		type: "GET",
		url:adminPath+"template/getTree.do",
		data:{"COLUMGROUP_ID":groupId},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200&&result.data!=null){
				 appendColumTemplatediv(result.data);
				 appendColumTemplateDetaildiv(result.data);
				 var tempalteId=$("#defaultTemplate").val();
				 defaultTemplate(tempalteId);
				 var defaultTemplateDetailId=$("#defaultTemplateDetailId").val();
				 defaultTemplateDetail(defaultTemplateDetailId);
			 }
		}
	});
}

//选择模板弹出内容填充
function appendColumTemplatediv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 if(obj.childList.length>0){
			 html+='<li>';
		 		html+='<label><span>'+obj.name+'</span><i></i><em class="san"></em></label>';
		 		html+='<dl style="display:none;">';
			 $.each(obj.childList,function(i,item){
				 	html+='<dd><label for="bb_'+item.id+'"><input name="seltempck" type="radio" id="bb_'+item.id+'" value="'+item.id+'"  /><span>'+item.name+'</span><i></i></label> <input name="TEM_TYPE" type="hidden" id="TEM_TYPE_'+obj.id+'" value="'+obj.id+'"  /></dd>';
			 });
			 	html+='</dl>';
			 html+='</li>';
		 }
	 });
	 $("#columTemplatediv").html(html);
}
//选择模板详情弹出内容填充
function appendColumTemplateDetaildiv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 if(obj.childList.length>0){
			 html+='<li>';
		 		html+='<label><span>'+obj.name+'</span><i></i><em class="san"></em></label>';
		 		html+='<dl style="display:none;">';
			 $.each(obj.childList,function(i,item){
				 	html+='<dd><label for="cc_'+item.id+'"><input name="seltempdetailck" type="radio" data-temtype="'+obj.id+'" id="cc_'+item.id+'" value="'+item.id+'"  /><span>'+item.name+'</span><i></i></label></dd>';
			 });
			 	html+='</dl>';
			 html+='</li>';
		 }
	 });
	 $("#columTemplateDetaildiv").html(html);
}

//默认选中聚合模板
function defaultTemplate(selectedId){
	var selectedId="bb_"+selectedId;
	$(":input[name='seltempck']").each(function(){
		if($(this).attr("id")==selectedId){
			$(this).attr("checked","checked");
			$(this).siblings('i').addClass('active');
			$(this).parents('li').addClass('tog_san');
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown()
			var bodyHtml='<tr id="heheda">';
			bodyHtml+='<td><i class="special">'+$(this).next().text()+'</i></td>';
			bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteTemplate(this)" data-id="'+$(this).val()+'" class="remove_file">移除</a></td>';
			bodyHtml+='</tr>';
			$("#relationBody").html(bodyHtml);
		}
	});
}
//默认选中详情模板
function defaultTemplateDetail(selectedId){
	var selectedId="cc_"+selectedId;
	$(":input[name='seltempdetailck']").each(function(){
		if($(this).attr("id")==selectedId){
			$(this).attr("checked","checked");
			$(this).siblings('i').addClass('active');
			$(this).parents('li').addClass('tog_san');
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown();
			var bodyHtml='<tr id="heheda">';
			bodyHtml+='<td><i class="special">'+$(this).next().text()+'</i></td>';
			bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteTemplateDetail(this)" data-id="'+$(this).val()+'" class="remove_file">移除</a></td>';
			bodyHtml+='</tr>';
			$("#columDetailBody").html(bodyHtml);
		}
	});
}
//默认选中栏目
function defaultColum(selectedId){
	var selectedId="cc_"+selectedId;
	$(":input[name='selcolum']").each(function(){
		if($(this).attr("id")==selectedId){
			$(this).attr("checked","checked");
			$(this).siblings('i').addClass('active');
			$(this).parents('li').addClass('tog_san');
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown();
			var bodyHtml='<tr id="heheda">';
			bodyHtml+='<td><i class="special">'+$(this).next().text()+'</i></td>';
			bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteColum(this)" data-id="'+$(this).val()+'" class="remove_file">移除</a></td>';
			bodyHtml+='</tr>';
			$("#columBody").html(bodyHtml);
			return false;
		}
	});
}

//选择栏目
function setColumParentId(){
	var obj=$(":radio[name='selcolum']:checked");
	if(obj.length>0){
		var id=obj.val();
		var text=obj.next().text();
		$("#PARENTID").val(id);
	  	$("#columwarn").html("");
		var bodyHtml='<tr id="heheda">';
		bodyHtml+='<td><i class="special">'+text+'</i></td>';
		bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteColum(this)" data-id="'+id+'" class="remove_file">移除</a></td>';
		bodyHtml+='</tr>';
		$("#columBody").html(bodyHtml);
	}
	$('.layer_bg02').hide();
}

function deleteColum(obj){
	$(obj).parents("tr").remove();
	var selectedId=$(obj).data('id');
	$(":input[name='selcolum']").each(function(){
		if($(this).val()==selectedId){
			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
		}
	});
	var colum=$(":radio[name='selcolum']:checked");
	if(colum.length<1){
		$("#PARENTID").val('');
	}
}

 //动态追加选择的栏目聚合模板
function setTemplateId(){
	var template=$(":radio[name='seltempck']:checked");
	var templateId=template.val();
	var templateText=template.next().text();
	var templateType=template.parent().next().val();
	$("#COLUM_TEMPLATETID").val(templateId);
	if(template.length>0){
		var bodyHtml='<tr id="heheda">';
		bodyHtml+='<td><i class="special">'+templateText+'</i></td>';
		bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteTemplate(this)" data-id="'+templateId+'" class="remove_file">移除</a></td>';
		bodyHtml+='</tr>';
		$("#relationBody").html(bodyHtml);
		//$("#COLUM_TYPE").val(templateType);
	}
	$('#layer_bg08').hide();
	//changeColumType(templateType);
}
//动态追加选择的栏目详情模板
function setTemplateDetailId(){
	var template=$(":radio[name='seltempdetailck']:checked");
	var templateId=template.val();
	var templateText=template.next().text();
	var templateType=template.data("temtype");
	$("#TEMPLATET_DETAIL_ID").val(templateId);
	if(template.length>0){
		var bodyHtml='<tr id="heheda">';
		bodyHtml+='<td><i class="special">'+templateText+'</i></td>';
		bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteTemplateDetail(this)" data-id="'+templateId+'" class="remove_file">移除</a></td>';
		bodyHtml+='</tr>';
		$("#columDetailBody").html(bodyHtml);
	}
	$('#layer_bg09').hide();
	//changeColumType(templateType);
}

 
function deleteTemplate(obj){
	$(obj).parents("tr").remove();
	var selectedId=$(obj).data('id');
	$(":input[name='seltempck']").each(function(){
		if($(this).val()==selectedId){
			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
			$(this).parents('li').removeClass('tog_san');
		}
	});
	$("#COLUM_TEMPLATETID").val('');
}
function deleteTemplateDetail(obj){
	$(obj).parents("tr").remove();
	var selectedId=$(obj).data('id');
	$(":input[name='seltempdetailck']").each(function(){
		if($(this).val()==selectedId){
			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
			$(this).parents('li').removeClass('tog_san');
		}
	});
	$("#TEMPLATET_DETAIL_ID").val('');
}


//关闭选择栏目模板弹窗
function colseTemplate(obj){
    //$('.layer_list li label').removeClass('radio_btn');
    //$(obj).parents('.layer_bg').find('input').removeAttr("checked");
    //$(obj).parents('.layer_bg').find('i').removeClass('active');
  	//$("#templatewarn").html("");
  	//$("#COLUM_TEMPLATETID").val("");
  	$(obj).parents('.layer_bg').hide();
}

function colseColum(obj){
    //$('.layer_list_other li label').removeClass('radio_btn');
    //$(obj).parents('.layer_bg').find('input').removeAttr("checked");
    //$(obj).parents('.layer_bg').find('i').removeClass('active');
  	//$("#columwarn").html("");
  	//$("#PARENTID").val("");
  	$(obj).parents('.layer_bg').hide();
}

$("#COLUM_IMAGE").change(function() {  
    var upFile = $("#COLUM_IMAGE").val();  
        if (!/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG)$/.test(upFile)) {  
            window.top.mesageTip("warn","图片类型必须是.gif,jpeg,jpg,png,bmp中的一种");
            $("#COLUM_IMAGE").val("");  
            return false;  
        }  
});  
function save(type){
	var parentid=$("#PARENTID").val();
	var columTemplatetid=$("#COLUM_TEMPLATETID").val();
	var columUrlName=$("#COLUM_URLNAME").val();
	//$("#COLUM_TYPE").removeAttr("disabled");
	/*var colum_type=$("#COLUM_TYPE option:selected").val();
	if(colum_type=="-1"||colum_type==undefined||colum_type==""||colum_type==null){
		 $("#columTypewarn").html("请选择栏目类型");
		 return false;
	}*/
	/* if(columUrlName=="-1"||columUrlName==undefined||columUrlName==""||columUrlName==null){
		 //前面一个参数warn是错误提示固定要传的，后面一个参数是要显示的字符。
		 window.top.mesageTip("warn","栏目URL名称不能为空");
		 return false;
	} */
	if(parentid==null||parentid==undefined||parentid==""){
	  	$("#columwarn").html("必须字段");
		return false;
	}
	if(!$("#Form").valid()){
		return false;
	}
	$("#ifAppendSave").val(type);
	$("#Form").submit();
}
</script>
</html>
