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
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function(){
		  
		$('.layer_btn01').click(function(){
		  	$('.layer_bg08').show();
	    });
		$('.layer_btn02').click(function(){
		  	$('.layer_bg02').show();
	    });
		//移除图片
		  $(document).on('click','.remove_file',function(){
				$(this).next('.hid_div').remove();
			});
		//显示图片
		$(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		});
		setColumdiv();
		setTy();
		settemplate();	
	})
	function popupHiden(param,selectordiv,selectorbody){
		$(param).parents('.layer_bg').find("input").removeAttr("checked");
		$(param).parents('.layer_bg li babel').removeClass('radio_btn');
		$(param).parents('.layer_bg').find('i').removeClass('active');
		$(selectordiv).each(function(index,obj){
			$(selectorbody).each(function(i,n){
				if(obj.value==n.value){
					$(obj).attr("checked",true);
					$(obj).parents('label').find('i').addClass('active');
				}
			})
		});
	  	$(param).parents('.layer_bg').hide();
	}
	function modalHide(selectorDiv,selectorBody,selectorModal){//确定模板窗口关闭
		var $ids=$(selectorDiv);
		 if($ids.length==0){
			 $(selectorBody).html('');
			 $(selectorModal).hide();
			return false;
		   } 
			var html="";
			$.each($ids,function(index,obj){
				if(index==$ids.length-1){
					html+="<tr><td width='50%' >"+$(obj).attr("att")+"</td><td><a href='javascript:;' onclick='removeBody(this,\""+selectorDiv+"\")'>移除</a><input type='hidden'  value="+obj.value+" /></td></tr>";
				}else{
					html+="<tr><td width='50%' >"+$(obj).attr("att")+"</td><td><a href='javascript:;' onclick='removeBody(this,\""+selectorDiv+"\")'>移除</a><input type='hidden'  value="+obj.value+" /></td></tr>"+",";
				}
			});
			$(selectorBody).html(html);
		$(selectorModal).hide();
}
	
function setTy(){
	var param=$("#mid").val();
	setTypediv(param);
}

/* ======================================= */
function removeBody(param,selector){
		$(selector).each(function(){
			if(this.value==$(param).next().val()){
				$(this).removeAttr("checked");
				$(this).parents('label').find('i').removeClass('active');
			}
		});
		$(param).parent().parent().remove();
	}
/* ======================================= */
//获取模板类型树形结构
function settemplate(){
	var columId=$("#columId").val();
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/findTemplateDetailByColumId.do",
		data:{"id":columId},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200&&result.data!=null){
				 var data=result.data;
				 $("#u1813_input").html(data.temName);
				 $("#message_template").val(data.id);
			 }
		}
	});
}
//选择模板弹出内容填充
function appendtemplatediv(list){
	 var html='';
	 if(list.length==0){
			html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;color:red;">对不起,没有相关数据!</div>';
			$("#u1813_input").html(html);
			return false;
		}
	 $.each(list,function(index,obj){
		 if(obj.hasDefault){
			 html+='<option selected="selected" value="'+obj.id+'">'+obj.name+'</option>';
		 }else{
 	     	html+='<option value="'+obj.id+'">'+obj.name+'</option>';
		 }
	 });
	 $("#u1813_input").html(html);
}


//获取栏目类型树形结构
function setColumdiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/getAssignTypeTree.do",
		data:{TEM_TYPE:2},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendColumdiv(result.data);
			}
		}
	});
}
//选择栏目弹出内容填充
function appendColumdiv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#columndiv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label ><span>'+obj.name+'</span></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachColumList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#columndiv").html(html);
	 $(":input[name='columnids']:checked").each(function(){
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown()
		});
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
}

function eachColumList(html,list){
	var columId=$("#columId").val();
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	var ischecked='<p><em class="show_btn"></em><label ><input type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	//自动定位栏目
		 	if(columId==obj.id){
			 	ischecked='<p><em class="show_btn"></em><label ><input type="checkbox" checked="checked" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
				var columhtml="<tr><td width='50%' >"+obj.name+"</td><td><a href='javascript:;' onclick='removeBody(this,\"#columndiv :checked\")'>移除</a><input type='hidden'  value="+obj.id+" /></td></tr>";
		 		$("#columnBody").append(columhtml);
		 		$("#columName").html(obj.name);
		 	}
		 	html+=ischecked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachColumList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}
function eachSubList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 $("#columndiv input").each(function(){
			 if(this.value==obj.id){
				 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
				 }
		 });
		 	html+=is_checked;
		 if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

//获取资讯类型树形结构
function setTypediv(){
	var columId=$("#columId").val();
	$.ajax({
		type: "GET",
		url:adminPath+"mymessageType/getTree.do",
		data:{"columId":columId},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendTypediv(result.data);
			}else{
				 var html='';
				 html+='<li>';
				 html+='<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" checked="checked" att="顶级分类" name="pid" value="0"  /><span>作为顶级分类</span><i class="active"></i></label></p>';
				 html+='</li>';
				 $("#typediv").html(html);
			}
		}
	});
}

//选择类型弹出内容填充
function appendTypediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#typediv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_top='<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类" name="pid" value="0"  /><span>作为顶级分类</span><i></i></label></p>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.type_name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
		 $("#typediv input[type='radio']:checked").each(function(){
			if(this.value==obj.id){
				is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input checked="checked" type="radio" att="'+obj.type_name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i class="active"></i></label></p>';

			} 
			if(this.value==0&&index==0){
				is_top='<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类" checked="checked" name="pid" value="0"  /><span>作为顶级分类</span><i class="active"></i></label></p>';
		        html+=is_top;
			}else if(this.value!=0&&index==0){
				is_top='<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类"  name="pid" value="0"  /><span>作为顶级分类</span><i ></i></label></p>';
				html+=is_top;
			}
		 });
		 html+=is_checked;
		  if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#typediv").html(html);
	 $(":input[name='pid']:checked").each(function(){
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown();
		});
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
	 modalHide('#typediv :checked','#typeBody','.layer_bg08');
}
function eachList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.type_name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
		 $("#typediv input[type='radio']:checked").each(function(){
			if(this.value==obj.id){
				is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.type_name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i class="active"></i></label></p>';

			} 
		 });
		 html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}
function save(type){
	/* var typeUrlName=$("#typeUrlName").val();
	if(typeUrlName=="-1"||typeUrlName==undefined||typeUrlName==""||typeUrlName==null){
		 window.top.mesageTip("warn","分类URL名称不能为空");
		 return false;
	} */
	if(!$("#Form").valid()){
		return false;
	}
	$("#TXT").val(getContent());
	$("#is_add").val(type);
	$("#Form").submit();
		
}

/* function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	$("#TXT").val(getContent());
    var formData = new FormData($("#Form")[0]);
	    formData.append("username","111");//不知道IE下为什么，不添加条空数据，报IO错误,谁要是能解决，请给我发邮件，告知qichangxin@300.cn
		 $.ajax({
			type: "POST",
			url:adminPath+"mymessageType/edit.do",
			data:formData,
			dataType:'json',
			cache: false,
	        processData: false,
	        contentType: false,
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 $("#result").text("修改成功");
						 location.href=adminPath+'mymessageType/toAdd.do';
					 }else{
						 $("#result").text("修改成功");
						 location.href=adminPath+'mymessageType/list.do';
					 }
				 }else{
					 $("#result").text("修改失败,请重试");
				 }
			}
		}); 
	} */
	
function setImagePreview(avalue) {
    var docObj = document.getElementById("file_name_img");
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
//百度富文本
setTimeout("ueditor()",500);
function ueditor(){
	UE.getEditor('editor');
}
//ueditor有标签文本
function getContent() {
    var arr = [];
    arr.push(UE.getEditor('editor').getContent());
    return arr.join("");
}
</script>
</head>
<body>
	
	<!-- cms_con开始 -->
				<div class="cms_con cf">
					<div class="cms_con cf">
					<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
						<i>></i> <a href="<%=adminPath%>columcontent_colum/managecontentType.do?ID=${pd.columId}&topColumId=${pd.topColumId}">分类列表</a>
						<i>></i> <i>修改分类</i>
					</div>
				</div>	
				<div class="cms_c_list cf">
					<h3>编辑资讯分类</h3>
					
					<form  name="Form" id="Form" action="<%=adminPath%>mymessageType/edit.do" method="post" enctype="multipart/form-data" >
					<input type="hidden" id="typeId"  name="Pid" value="${productType.PID }"/>
					<input type="hidden"  id="mid" name="id" value="${productType.ID }"/>
					<input type="hidden" id="columId" name="columId" value="${pd.columId}" />
					<input type="hidden" id="topColumId" name="topColumId" value="${pd.topColumId}" />
					<input type="hidden"  id="is_add" name="is_add" />
					<input type="hidden" id="TXT" name="type_detail"/>
						<div class="add_btn_con wrap cf">
						
						<dl class="cf">
							<dt><span class="hot">*</span>所属分类</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn01">+选择</a><span id="typeSpan" class="warn_tip">

							</span></div></dd>
												
						</dl>
						<dl class="cf">
							<dt></dt>
							<dd><div class="dd_con">
								<div class="show_list" style="margin-top:0px;">
									<table class="ev">
										<thead><tr><td width="70%">分类</td><td>操作</td></tr></thead>
										<tbody id="typeBody">
											<c:if test="${not empty productType.TYPE_NAME}">
												<tr><td width='50%' >${productType.TYPE_NAME}</td><td><a href='javascript:;' onclick='removeBody(this,"#typediv :checked")'>移除</a><input type='hidden'  value="${productType.PID }" /></td></tr>
											</c:if>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl>
						
						
						<dl class="cf">
							<dt>
								所属栏目
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id="columName" class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						
						<dl class="cf" style="display:none;">
							<dt></dt>
							<dd><div class="dd_con">
								<div class="show_list" style="margin-top:0px;">
									<table class="ev">
										<thead><tr><td width="70%">栏目</td><td>操作</td></tr></thead>
										<tbody id="columnBody">
											<tr><td width='50%' >${productType.COLUM_NAME }</td><td><a href='javascript:;' onclick='removeBody(this,"#columndiv :checked")'>移除</a><input type='hidden'  value="${productType.COLUMNID}" /></td></tr>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl>
									
						<dl class="cf">
							<input type="hidden" name="message_template" id="message_template"/>
							<dt>
								<span class="hot"></span>所属模板
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id=u1813_input class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot">*</span>分类名称</dt>
								<dd><div class="dd_con"><input type="text" id="type_name" name="type_name" value="${productType.TYPE_NAME }" ></div></dd>
								<dd class="zp_dd">
									<c:if test="${productType.TYPE_WBURL != '' && productType.TYPE_WBURL != null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i class="active"></i></label>
										<div class="dd_con" style="display: block;">
											<input type="text" style="width: 300px;" id="type_wburl" name="type_wburl" value="${productType.TYPE_WBURL }">
										</div>
									</c:if>
									<c:if test="${productType.TYPE_WBURL == '' || productType.TYPE_WBURL == null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
										<div class="dd_con">
											<input type="text" style="width: 300px;" id="type_wburl" name="type_wburl" value="${productType.TYPE_WBURL }">
										</div>
									</c:if>
								</dd>
							</dl>
						</div>
						
						<dl class="cf">
							<dt>
								<span class="hot"></span>分类URL名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" name="typeUrlName" id="typeUrlName" value="${productType.typeUrlName }" required/>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt>分类图片</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label for="file_name_img" style="float: left;"> <input type="file" id="file_name_img" onchange="javascript:setImagePreview();" name="file"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty image.imgurl}">
												<i style="float: left;">${image.title }</i>
												<%-- <i>${image.imgurl}</i> 
												<a href="javascript:;" class="remove_file" style="display:inline-block;">删除</a></li>--%>
												<a href="javascript:;" class="remove_file" style="float: left;">删除</a>
												
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" name="imageId" value="${image.imageId }" />
												</div>
												<div class="yulan"
													style="float: left; position: relative; padding-left: 10px;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px;">
														<div class="pro_img_big">
															<img id="preview" alt="" src="<%=imgPath%>${image.imgurl}" width="150px" height="180px">
														</div>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<i>未选择文件</i>
												<a href="javascript:;" class="remove_file" style="display:none;float: left;">删除</a></li>
												<div class="yulan"
													style="display: none; float: left; position: relative; padding-left: 10px;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px;">
														<div class="pro_img_big">
															<img id="preview" alt="" width="150px" height="180px">
														</div>
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
							<dt>状态</dt>
							<dd>
								<div class="dd_con show_hide">
									<c:if test="${productType.TYPE_STATUS==1 }">
									<label for="show" class="active"><input checked="checked" type="radio" id="show" name="type_status" value="1" /><span>显示</span></label>
									<label for="hide"><input type="radio" id="hide" name="type_status" value="0" /><span>隐藏</span></label>
									</c:if>
									
									<c:if test="${productType.TYPE_STATUS==0 }">
									<label for="show" class="active"><input  type="radio" id="show" name="type_status" value="1" /><span>显示</span></label>
									<label for="hide"><input type="radio" checked="checked" id="hide" name="type_status" value="0" /><span>隐藏</span></label>
									</c:if>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt><span class="hot"></span>详情描述</dt>
							<dd style="z-index:1;position:relative;">
								<div class="dd_con" style="z-index:1;">
									<div class="eidt_box cf" style="z-index:1;">
										<table id="table_report" class="table table-striped table-bordered table-hover" style="z-index:1;">
												<tr>
													<td style="z-index:1;">
													  <script id="editor" type="text/plain" style="width:90.2%;height:240px;z-index:1;">${productType.TXT }</script>
													</td>
												</tr>
												
											</table>
									</div>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt><span class="hot"></span>概要描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea class="textarea_numb" name="type_summary">${productType.TYPE_SUMMARY }</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt>关键字</dt>
							<dd><div class="dd_con"><input type="text" name="type_keywords" value="${productType.TYPE_KEYWORDS }"/></div></dd>
						</dl>
					
						
					</div>
					<div class="all_btn cf">
					<span id="result"></span>
						<input type="button" class="submit_btn" value="修改" onclick="save('0')"/>
						<a href="<%=adminPath%>columcontent_colum/managecontentType.do?ID=${pd.columId}&topColumId=${pd.topColumId}" class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择分类</span><p class="close" onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p></h3>
								<div class="layer_list_other mScrol222 cf" >
									<ul id="typediv" class="radio_lock">
										<input type="radio" checked="checked" name="pid" value="${productType.PID}"></input>
												<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的分类，请添加分类后，再操作</div>
										
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="modalHide('#typediv :checked','#typeBody','.layer_bg08');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);" onclick="popupHiden(this,'#typediv input','#typeBody input')" class="submit_re_btn">取消</a>
								</div>
						</div>
					</div>
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close" onclick="popupHiden(this,'#columndiv input','#columnBody input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="columndiv">
									<input type="checkbox" checked="checked" name="columnids" value="${productType.COLUMNID}"></input>
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的栏目，请添加栏目后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
								<input type="button" onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg02');" class="submit_btn" value="确定" />
								<a href="javascript:void(0);" onclick="popupHiden(this,'#columndiv input','#columnBody input')" class="submit_re_btn">取消</a>
							</div>							
						</div>
					</div>
					</form>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->
<!-- 百度富文本编辑框-->
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>

</body>
</html>