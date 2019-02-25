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
	String imgPath ="/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function(){
		/* $(document).on('click','.submit_re_btn',function(){
		    $('.layer_list li label').removeClass('radio_btn');
		  	$('.layer_bg').find('input').removeAttr("checked");
		  	$('.layer_bg').find('i').removeClass('active');
		  	$(this).parents('.layer_bg').hide();
		 }); */
		$('.layer_btn01').click(function(){
		  	$('.layer_bg08').show();
	    });
		$('.layer_btn02').click(function(){
		  	$('.layer_bg02').show();
	    });
		//显示图片
		$(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		});
		setColumdiv();
	});
	//获取上传图片路径
	function setImagePreview(id) {
	    var docObj = document.getElementById(id);
	    var imgObjPreview = document.getElementById("preview"+id);
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
	//获取栏目类型树形结构
	function setColumdiv(){
		var columId=$("#columId").val();
		$.ajax({
			type: "GET",
			url:adminPath+"columconfig/findColumDetailByColumId.do",
			data:{"columId":columId},
			dataType:'json',
			cache: false,
			success: function(result){
				if(result.code==200&&result.data!=null){
					$("#columName").text(result.data.columName);
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
			 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  type="checkbox" att="'+obj.name+'" name="ids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
			 $("#columnBody input").each(function(){
				 if(this.value==obj.id){
				 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" type="checkbox" att="'+obj.name+'" name="ids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
				 }

			 });
			 	html+=is_checked;
			 	if(obj.childList!=null && obj.childList.length>0){
			 		html+=eachSubList('',obj.childList);
			 	}
			 html+='</li>';
		 });
		 $("#columndiv").html(html);
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
			 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  type="checkbox" att="'+obj.name+'" name="ids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
			 $("#columnBody input").each(function(){
				 if(this.value==obj.id){
					 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" type="checkbox" att="'+obj.name+'" name="ids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
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
	//==================
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
	//保存新增banner
	function saveBanner(){
			if(!$("#Form").valid()){
				window.top.mesageTip("warn","请输入合法的链接!");
				return false;
			}
			//如果banner名称没写则提示请输入
		    if($("#banner_name").val()==""){
				mesageTip("warn","请输入banner名称!");
				return false;
			}
		    var imgs=$("#upimages");
		    var fileimages=$(".file_upload_img>i");
		    var txt;
		    $.each(fileimages,function(index,obj){
		    	txt=$(this).text();
		    });
		    if(imgs.length==0 || txt=='未选择文件'){
		    	window.top.mesageTip("warn","请上传图片文件!");
		    	return false;
		    } 
		    $("#Form").submit();
		    //var formData = new FormData($("#Form")[0]);
		    //如果为选择栏目则提示请选择所属栏目
		   /* var $ids=$("#columndiv input[type='checkbox']:checked");
		     if($ids.length!=1){
		    	window.top.mesageTip("warn","请选择所属栏目!");
		    	return false;
		    } */
		    
			/* $.ajax({
				type: "POST",
				url:adminPath+"banner/update.do",
				data:formData,
				dataType:'json',
				cache: false,
		        processData: false,
		        contentType: false,
				success: function(result){
					 if(result.success){					 
						 window.top.mesageTip("success","修改成功");
						 location.href=adminPath+'banner/list.do';
					 }else{
						 window.top.mesageTip("failure","修改失败");
					 }
				}
			}); */
	}
	//移除相关栏目
		function removeBody(param,selector){
		$(selector).each(function(){
			if(this.value==$(param).next().val()){
				$(this).removeAttr("checked");
				$(this).parents('label').find('i').removeClass('active');
			}
		});
		$(param).parent().parent().remove();
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
						<a href="<%=adminPath%>columcontent_colum/managebanner.do?ID=${pd.columId}&topColumId=${pd.columId}">Banner列表</a>
						<i>></i>
						<i>编辑Banner</i>
					</div>
				</div>

				<div class="cms_c_list cf">
					<h3>编辑Banner</h3>
					<form  name="Form" id="Form" action="<%=adminPath%>banner/update.do?columId=${pd.columId}&topColumId=${pd.columId}" method="post" enctype="multipart/form-data">
						<input type="hidden" id="columId" name="columId" value="${pd.columId}"/>
						<input type="hidden" id="is_add" name="is_add" />
						<div class="add_btn_con wrap cf">
							<dl class="cf">
								<dt><span class="hot">*</span>Banner名称</dt>
								<dd><div class="dd_con"><input type="text" name="banner_name" id="banner_name" value="${banner.banner_name}"/></div></dd>
								<dd><div class="dd_con"><input type="hidden" name="id"  value="${banner.id}"/></div></dd>
							</dl>
							<dl class="cf">
								<dt><span class="hot">*</span>所属栏目</dt>
								<%-- <input type="hidden" id="columconfigid" name="columconfigid" value="${banner.columconfigid}"/> --%>
								<dd>
									<div class="dd_con">
										<a href="javascript:void(0);" id="columName" class="layer_btn"
											style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
										</a>
									</div>
								</dd>
							</dl>
							<dl class="cf">
								<dt><span class="hot">*</span>图片内容</dt>
								<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addimg">上传图片</a>&nbsp;&nbsp;<i style="color: gray;">点击可追加</i></div></dd>
								<c:forEach items="${banner.pictures}" var="i" varStatus="num">
								  	<dd style="overflow:inherit;">
									  	<div class="dd_con">
										  	<div class="add_img cf" id="upimages">
											  	<ul>
												  	<li class="file_upload file_upload_img" style="overflow:initial;height:36px;">
														<span style="float:left;">图片</span>
													  	<label style="float:left;" for="file_name_img_${num.count-1 }">
													  		<input type="file" onchange="javascript:setImagePreview('file_name_img_${num.count-1 }');" id="file_name_img_${num.count-1 }" name="images">
													  	<em>上传</em>
													  	</label>
													  	<i style="float:left;">${i.title }</i>
													  	<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
													  	<div class="hid_div" style="height:0px;overflow:hidden;">
														 
														</div>
													  	<input type="hidden"  name="listImage[${num.count-1 }]" value="${i.imageId }">
														<input type="hidden"  name="pictures[${num.count-1 }].imageId" value="${i.imageId }"> 
													  	<div class="yulan" style="position:relative;padding-left:10px;float:left;">预览
														  	<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
															  	<div class="pro_img_big">
															  		<img id="previewfile_name_img_${num.count-1 }" alt="" src="<%=imgPath%>${i.imgurl}" width="150px" height="180px" />
															  	</div>
														  	</div>
													  	</div>
												  	</li>
												  	<li>
													  	<p><span>标题</span><input type="text" name="pictures[${num.count-1 }].title" value="${i.title }">
													  	
													  	</p>
													  	<p><span>链接</span><input type="text" name="pictures[${num.count-1 }].tourl" class="url" value="${i.tourl}"></p>
												  	</li>
												  	<li>
													  	<p><span>副标题</span><input type="text" name="pictures[${num.count-1 }].subhead" value="${i.subhead }"></p>
													</li>
													<li>
													  	<p><span>排序</span><input type="text" name="pictures[${num.count-1 }].forder" value="${i.forder }"></p>
													</li>
												  	<li>
													  	<span>描述</span><h6><textarea class="textarea_numb" name="pictures[${num.count-1 }].bz">${i.bz }</textarea>
													  	<p><span class="word">0</span><span>/</span><span>200</span></p></h6>
												  	</li>
											  	</ul>
											  	<div class="close_add">x</div>
										  	</div>
									  	</div>
								  	</dd>
								</c:forEach>						
							</dl>
						
							<dl class="cf">
								<dt><span class="hot"></span>视频内容</dt>
								<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addfilm">上传视频</a>&nbsp;&nbsp;<i style="color: gray;">点击可追加</i></div></dd>
								<c:forEach items="${banner.videos}" var="v" varStatus="num">
									<dd>
									<div class="dd_con"><div class="add_img cf">
									<ul>
									<li class="file_upload file_upload_film" style="overflow:initial;height:36px;">
									<span style="float:left;">视频</span>
									<label style="float:left;" for="file_name_film_01${num.count-1 }">
									<input type="file" id="file_name_film_01${num.count-1 }" name="films">
									<em>上传</em>
									</label>
									<i style="float:left;">${v.video_title }</i>
									<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
									<div class="hid_div" style="height:0px;overflow:hidden;">
									
									</div>
									<input type="hidden"  name="listVideo[${num.count-1 }]" value="${v.id }">
									<input type="hidden"  name="videos[${num.count-1 }].id" value="${v.id }">
									</li>
									<li>
									<p><span>标题</span>
									<input type="text" name="videos[${num.count-1 }].video_title" value="${v.video_title }">
									</p>
									<p><span>链接</span>
									<input type="text" name="videos[${num.count-1 }].tourl" class="url" value="${v.tourl}"></p>
									
									</li>
									<li>
										<p><span>排序值</span><input type="text" class="digits" name="videos[${num.count-1 }].forder" value="${v.forder}"></p>
									</li>
									<li><span>描述</span>
									<h6><textarea class="textarea_numb" name="videos[${num.count-1 }].video_content">${v.video_content}
									</textarea><p><span class="word">0</span><span>/</span><span>200</span></p>
									</h6>
									</li>
									</ul><div class="close_add">x</div></div></div></dd>
								</c:forEach>
							</dl>
						<script type="text/javascript">
							$(function(){						
								  //添加图片视频上传功能
								  //添加图片
								   var classNimg=0;
								  var num=0;
								  var img_list='';
								  $(document).on('click','.layer_btn_addimg',function(){
									  if($(this).parents('dd').next().size()>0 && $(this).parents('dl').find('dd:last-child').find('i').text()=='未选择文件'){
											 window.top.mesageTip("warn","请上传图片后再点击上传!");
										  }else{
										  	classNimg=classNimg+1;
										  	img_list+='<dd style="overflow:inherit;">';
										  	img_list+='<div class="dd_con">';
										  	img_list+='<div class="add_img cf" id="upimages">';
										  	img_list+='<ul><li class="file_upload file_upload_img" style="overflow:initial;height:36px;">';
											img_list+='<span style="float:left;">图片</span>';
										  	img_list+='<label style="float:left;" for="file_img_'+classNimg+'">';
										  	var fileId='file_img_'+classNimg;
										  	img_list+='<input type="file" onchange="javascript:setImagePreview(\''+fileId+'\');" id="'+fileId+'" name="addimages">';
										  	img_list+='<em>上传</em>';
										  	img_list+='</label><i style="float:left;">未选择文件</i>';
										  	img_list+='<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>';
										  	img_list+='<div class="yulan" style="display:none;position:relative;padding-left:10px;float:left;">预览';
										  	img_list+='<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">';
										  	img_list+='<div class="pro_img_big">';
										  	img_list+='<img id="preview'+fileId+'" alt="" src="" width="150px" height="180px">';
										  	img_list+='</div></div></div>';
										  	img_list+='</li><li><p><span>标题</span><input type="text" name="addpictures['+num+'].title"></p>';
										  	img_list+='<p><span>链接</span><input type="text" class="url" name="addpictures['+num+'].imgurl"></p>';
										  	img_list+='</li><li><p><span>副标题</span><input type="text" name="addpictures['+num+'].subhead"></p>';
										  	
										  	img_list+='</li><li><p><span>排序</span><input type="text" name="addpictures['+num+'].forder"></p>';
										  	
										  	img_list+='</li><li><span>描述</span><h6><textarea class="textarea_numb" name="addpictures['+num+'].bz"></textarea>';
										  	img_list+='<p><span class="word">0</span><span>/</span><span>200</span></p></h6>';
										  	img_list+='</li></ul><div class="close_add">x</div>';
										  	img_list+='</div></div>';
										  	img_list+='</dd>';
										  	num++;
										  	$(this).parents('dl').append(img_list);
										  	img_list='';
										  }
								  })
								  //添加视频
								  var number=0;
								  var classNfilm=0;
								  $(document).on('click','.layer_btn_addfilm',function(){
									  if($(this).parents('dd').next().size()>0 && $(this).parents('dl').find('dd:last-child').find('i').text()=='未选择文件'){
										  window.top.mesageTip("warn","请上传视频后再点击上传!"); 
									  }else{
									  	classNfilm=classNfilm+1;
									  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film_'+classNfilm+'"><input type="file" id="file_name_film_'+classNfilm+'" name="addfilms"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text" name="addvideos['+number+'].video_title"></p><p><span>链接</span><input type="text" class="url" name="addvideos['+number+'].video_url"></p></li><li><p><span>排序值</span><input type="text" class="digits" name="addvideos['+number+'].forder"></p></li><li><span>描述</span><h6><textarea class="textarea_numb" name="addvideos['+number+'].video_content"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
									  	number++;
									  	$(this).parents('dl').append(img_list);
									  }
								  })
								  //关闭图片视频上传功能
								  $(document).on('click','.close_add',function(e){
								  	$(this).parents('dd').remove();
								  	e.stopPropagation();
								  });
							})
						</script>
						</div>
					<div class="all_btn cf">
						<input type="button" class="submit_btn" value="修改" onclick="saveBanner();" />
						<a href="<%=adminPath%>columcontent_colum/managebanner.do?ID=${pd.columId}&topColumId=${pd.columId}" class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close" onclick="popupHiden(this,'#columndiv input','#relationBody input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf">
								<ul id="columndiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的栏目，请添加栏目后，再进行此操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg02');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);" onclick="popupHiden(this,'#columndiv input','#columnBody input')"  class="submit_re_btn">取消</a>
								</div>
							
						</div>
					</div>	
					</form>
				</div>
			
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->
</body>
</html>