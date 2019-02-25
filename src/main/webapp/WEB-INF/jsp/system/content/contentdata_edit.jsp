<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script type="text/javascript" charset="utf-8" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		setContentType();
		var friendtype = "${content.friend}"
		$("#"+friendtype+"").attr("selected",true);
		$('.layer_btn01').click(function(){ //所属分类窗口打开
			 var $ids=$("#columnBody input");
				if($ids.length==0){
					window.top.mesageTip("warn","请选择所属栏目");
					return false;
				}
				ids=[];
				$.each($ids,function(){
					ids.push(this.value);
				});
				param="columnid=";
				param+=ids.join("&columnid=");
				
				setTypediv(param);
		  	$('.layer_bg08').show();
	    });
/* 		$('.layer_btn01').click(function(){
		  	$('.layer_bg08').show();
	    }); */
		$('.layer_btn02').click(function(){
		  	$('.layer_bg02').show();
	    });
		$('.layer_btn0b').click(function(){// 扩展字段窗口打开
		  	$('.layer_bg0b').show();
	    });
		//显示图片
		 $(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		}); 
		$('.layer_btn05').click(function(){// 相关产品窗口打开
			var $rlb= $("#relationBody input");
			$.ajax({
				url:adminPath+"contentData/findAll.do",
				type:"post",
				data:{ID:$("#ID").val()},
				success:function(data){
					var html='';
					if(data.contentList.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的相关内容，请添加内容后，再操作</div>';
						$("#productdiv").html(html);
						return false;
					}
					 $.each(data.contentList,function(index,obj){
						 html+='<li>';
						 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" name="'+obj.CONTENT_TITLE+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.CONTENT_TITLE+'</span><i></i></label></p>';
						 $.each($rlb,function(i,n){
							 if(n.value==obj.ID){
								 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" checked="checked" name="'+obj.CONTENT_TITLE+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.CONTENT_TITLE+'</span><i class="active"></i></label></p>';
							 }
						 });

				 	       html+=is_checked;
						 
						 html+='</li>';
					 });
					$("#productdiv").html(html);
					$("#productdiv :checked").each(function(){
						var This = $(this).parents('li');
						This.find('dl').slideDown()
					});
					 //控制折叠按钮是否显示
				     $('.layer_list_other li .show_btn').each(function(){
				    	if($(this).parent('p').next('dl').size()==0){
				    		$(this).hide();
				    	}
				     });
				}
			})
		  	$('.layer_bg09').show();
	    });
		defaultStatus();
		setColumdiv();
		setTemplatediv();
		setExtendDiv();
	});
//显示与隐藏
function defaultStatus(){
	var selectedVal=$("#defaultColumDisplay").val();
	console.log(selectedVal); 
	if(selectedVal==null||selectedVal==''||selectedVal==undefined){
		selectedVal='1';
	}
	$(":input[name='solrStatus']").each(function(){
		if($(this).val()==selectedVal){
			$(this).attr("checked","checked");
			$(this).parents().addClass('active');
		}
	});
}	
	/*  */
function setExtendDiv(){
		$.ajax({
			url:adminPath+'contentExtendFiledController/getTree.do',
			data:{},
			type:'GET',
			success:function(data){
				appendExtenddiv(data.tree);
			}
		})
	}
function appendExtenddiv(list){
	var html='';
	if(list.length==0){
		html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		$("#extenddiv").html(html);
		return false;
	}
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input filedType="'+obj.FIELDTYPE+'" type="checkbox" att="'+obj.NAME+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.NAME+'</span><i></i></label></p>';
			 $("#extend input[type='hidden']").each(function(){
				 if(this.value==obj.ID){
					 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input filedType="'+obj.FIELDTYPE+'" checked="checked" type="checkbox" att="'+obj.NAME+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.NAME+'</span><i class="active"></i></label></p>';
				 }
			 });
				html+=is_checked;
		 html+='</li>';
	 });
	 $("#extenddiv").html(html);
	 $('.layer_list_other li .show_btn').each(function(){
     	if($(this).parent('p').next('dl').size()==0){
     		$(this).hide();
     	}
     });
}
function popupHidens(param,selectordiv){
	$(param).parents('.layer_bg').find("input").removeAttr("checked");
	$(param).parents('.layer_bg li babel').removeClass('radio_btn');
	$(param).parents('.layer_bg').find('i').removeClass('active');
		$(selectordiv).each(function(index,obj){
			$("#extend input").each(function(i,n){
				if(obj.value==n.value){
					$(obj).attr("checked",true);
					$(obj).parents('label').find('i').addClass('active');
				}
			})
		});
	  	$(param).parents('.layer_bg').hide();
}
function propertyModalHide(){//确定产品属性窗口关闭
			var $id=$("#extenddiv input[type='checkbox']:checked");
			if($id.length==0){
				$("#extend").html("");
				$('.layer_bg0b').hide();
				return false;
			}
			var htmlStr="";
			$.each($id,function(index,obj){
				if($(obj).attr('filedType')==3){
					htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><input type='date' name='fileds["+index+"].value' ><input name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></div></dd></dl>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/>";
				}else  if($(obj).attr('filedType')==2){
					htmlStr+="<dl class='cf'><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><ul><li><h6>";
					htmlStr+="<textarea class='textarea_numb' name='fileds["+index+"].value'></textarea><input name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/>";
					htmlStr+="<p><span class='word'>0</span><span>/</span><span>200</span></p>";
					htmlStr+="</h6></li></ul></div></dd></dl>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/>";
				}else{
					htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><input type='text' name='fileds["+index+"].value' ><input name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></div></dd></dl>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/>";
				}
			})
			$("#extend").html(htmlStr);
			$('.layer_bg0b').hide();
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
	function modalHide(selectorDiv,selectorBody,selectorModal){//确定模板窗口关闭
			var $ids=$(selectorDiv);
			 if($ids.length==0){
				 if(selectorDiv=='#columndiv :checked'){
					 $(selectorBody).html('');
					 $(selectorModal).hide();
					 $("#typediv").html('');
					 $("#typeBody").html('');
					return false;
				 }else{
					 $(selectorBody).html('');
					 $(selectorModal).hide();
					return false;
				 }
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
			if(selectorDiv=='#columndiv :checked'){
				var s=$("#columnBody input");
				if(s.length==0){
					$("#typediv").html('');
					$("#typeBody").html('');
					return false;
				}
				ids=[];
				$.each(s,function(){
					ids.push(this.value);
				});
				param="columnid=";
				param+=ids.join("&columnid=");
				$.ajax({
					type: "GET",
					url:adminPath+"contentType/getTreeByColumId.do",
					data:param,
					dataType:'json',
					cache: false,
					success: function(result){
						if(result.code==200&&result.data!=null){
							appendTypediv(result.data);
							modalHide('#typediv :checked','#typeBody','.layer_bg08');
						}
					}
				});
			}
	}

/*
//获取模板类型树形结构
function setTemplatediv(){
	$.ajax({
		type: "GET",
		url:adminPath+"template/getDefinedTree.do",
		data:{type:1,temType:2},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200&&result.data!=null){
				 appendTemplatediv(result.data);
			 }
		}
	});
}

//选择模板弹出内容填充
function appendTemplatediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#u1813_input").html(html);
		 return false;
	 }
	 var templateid='${content.contentTemplateId}';
	 $.each(list,function(index,obj){
		 if(obj.id==templateid){
			 html+='<option selected="selected" value="'+obj.id+'">'+obj.name+'</option>';
		 }
 	     html+='<option value="'+obj.id+'">'+obj.name+'</option>';
	 });
	 $("#u1813_input").html(html);
}*/

//获取模板类型树形结构
function setTemplatediv(){
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
				 $("#contentTemplateId").val(data.id);
			 }
		}
	});
}

function setproductdiv(){//确定相关内容窗口关闭 
		 var $ids=$("#productdiv input[type='checkbox']:checked");
			var htmlStr="";
			$.each($ids,function(index,obj){
					htmlStr+="<tr>";
					htmlStr+="<td width='50%' >"+$(obj).attr("name")+"</td>";
					htmlStr+="<td ><input type='hidden' name='contentRelevantIdList'  value='"+obj.value+"'/><a href='javascript:;' onclick='removes(this)'>移除</a></td></tr>";
			});
			$("#relationBody").html(htmlStr);
		$('.layer_bg09').hide();
	}

function removes(param){
	$(param).parent().parent().remove();
}
function removeBody(param,selector){
	$(selector).each(function(){
		if(this.value==$(param).next().val()){
			$(this).removeAttr("checked");
			$(this).parents('label').find('i').removeClass('active');
		}
	});
	$(param).parent().parent().remove();
	if(selector=='#columndiv :checked'){
		var $ids=$("#columnBody input");
		if($ids.length==0){
			$("#typediv").html('');
			$("#typeBody").html('');
			return false;
		}
		ids=[];
		$.each($ids,function(){
			ids.push(this.value);
		});
		param="columnid=";
		param+=ids.join("&columnid=");
		$.ajax({
			type: "GET",
			url:adminPath+"contentType/getTreeByColumId.do",
			data:param,
			dataType:'json',
			cache: false,
			success: function(result){
				if(result.code==200&&result.data!=null){
					appendTypediv(result.data);
					modalHide('#typediv :checked','#typeBody','.layer_bg08');
				}
			}
		});
	}
}
//获取栏目类型树形结构
function setColumdiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/getAssignTypeTree.do",
		data:{TEM_TYPE:1},
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
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><span>'+obj.name+'</span></label></p>';
				$("#columndiv input[type='checkbox']:checked").each(function(){
					if(this.value==obj.id){
						 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><span>'+obj.name+'</span></label></p>';
					 	 $("#columName").html(obj.name);
					}
				})
		 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	// $("#columndiv").html(html);
	 $(":input[name='columconfigIds']:checked").each(function(){
			var This = $(this).parents('li');
			This.find('dl').slideDown()
		});
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
}

function eachSubList(html,list){
	 var columId=$("#columId").val();
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  att="'+obj.name+'" type="checkbox" name="columconfigIds" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
			$("#columndiv input[type='checkbox']:checked").each(function(){
				if(this.value==obj.id){
					 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input checked="checked"  att="'+obj.name+'" type="checkbox" name="columconfigIds" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
				 	 $("#columName").html(obj.name);
				}
			})
	 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

function setContentType(){
	var $ids=$("#columnBody input");
	if($ids.length==0){
		return false;
	}
	ids=[];
	$.each($ids,function(){
		ids.push(this.value);
	});
	param="columnid=";
	param+=ids.join("&columnid=");
	setTypediv(param);
	/* $.ajax({
		type: "GET",
		url:adminPath+"contentType/getTreeByColumId.do",
		data:param,
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendTypediv(result.data);
				modalHide('#typediv :checked','#typeBody','.layer_bg08');
			}
		}
	}); */
}

//获取产品类型树形结构
function setTypediv(param){
	$.ajax({
		type: "GET",
		url:adminPath+"contentType/getTreeByColumId.do",
		data:param,
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendTypediv(result.data);
			}
		}
	});
}

//选择产品类型弹出内容填充
function appendTypediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#typediv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var typeidAndColumnids=obj.id+'-'+eachColumnids('',obj.columConfigList);
		 	 var is_checked='<p><em class="show_btn"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" type="checkbox"  att="'+obj.typeName+'" name="contenttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.typeName+'</span><i></i></label></p>';
	 
			
		 	$("#typediv input[type='checkbox']:checked").each(function(){
		 		if($(this).attr("typeid")==obj.id){
		 			is_checked='<p><em class="show_btn"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" checked="checked" type="checkbox" att="'+obj.typeName+'" name="contenttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.typeName+'</span><i class="active"></i></label></p>';
		 		}
		 	})
		 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#typediv").html(html);
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
}
function eachColumnids(html,list){
	var html='';
	$.each(list,function(index,obj){
		if(list.length-1==index){
			html+=obj.id+'';
		}else{
			html+=obj.id+'-';
		}
	});
	return html;
}
function eachList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var typeidAndColumnids=obj.id+'-'+eachColumnids('',obj.columConfigList);
		 var sum = $("#contentTypeId").val();
		 var is_checked = '';
		 if(sum != obj.id){
			 is_checked='<p><em class="show_btn"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" type="checkbox"  att="'+obj.typeName+'" name="contenttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.typeName+'</span><i></i></label></p>';
		 }else{
			 is_checked='<p><em class="show_btn"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" checked="checked" type="checkbox" att="'+obj.typeName+'" name="contenttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.typeName+'</span><i class="active"></i></label></p>';
		 }
		 	$("#typediv input[type='checkbox']:checked").each(function(){
		 		if($(this).attr("typeid")==obj.id){
		 			is_checked='<p><em class="show_btn"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" checked="checked" type="checkbox" att="'+obj.typeName+'" name="contenttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.typeName+'</span><i class="active"></i></label></p>';
		 		}
		 	})
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
	if(!$("#Form").valid()){
		return false;
	}
	$("#is_add").val(type);
	$("#TXT").val(getContent());
	$("#Form").submit();
}

function setImagePreview(avalue) {
    var docObj = document.getElementById("file_name_img");
    var imgObjPreview = document.getElementById("preview");
	if(avalue!=undefined){
		docObj= document.getElementById("file_name_img_00"+avalue);
		imgObjPreview = document.getElementById("preview"+avalue);
	}
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

function setImagePreview2(avalue) {
    var docObj = document.getElementById("file_name_img_");
    var imgObjPreview = document.getElementById("preview_");
	if(avalue!=undefined){
		docObj= document.getElementById(avalue);
		imgObjPreview = document.getElementById("preview_"+avalue);
	} 
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

/* sort排序,表单验证 */
function typeSort(param){
	var sort=$(param).val();
	if(sort==null || sort==undefined || sort=="") return false;
	var old=$(param).data("old");
	if(sort==old) return false;
	var zz=/^[1-9][0-9]*$/;
	if(zz.test(sort)){
		
	}else{
		window.top.mesageTip("warn","请输入正整数");
		var file = $(param);
		file.after(file.clone().val("")); 
		file.remove();
	}
}

</script>
</head>
<body>

	<!-- cms_con开始1 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}">内容列表</a>
				<i>></i> <i>修改内容</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑内容</h3>

			<form action="<%=adminPath%>contentData/updateContent.do?columId=${pd.columId}&topColumId=${pd.topColumId}" id="Form"
				method="post" enctype="multipart/form-data">
				<input type="hidden" id="columId" name="columId" value="${pd.columId}"/>
				<input type="hidden" id="TXT" name="contentTxt" /> 
				<input type="hidden" id="ID" name="id" value="${content.id }" /> 
				<input type="hidden" id="defaultColumDisplay" value="${content.solrStatus}"/>
				
				<input type="hidden" id="is_add" name="is_add" />
				<div class="add_btn_con wrap cf">


					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>标题
								
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="contentTitle" name="contentTitle"
										value="${content.contentTitle }" required>
								</div>
							</dd>
							<dd class="zp_dd">
								<c:if test="${content.contentWbUrl != '' && content.contentWbUrl != null}">
									<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i class="active"></i></label>
									<div class="dd_con" style="display: block;">
										<input type="text" style="width: 300px;" id="type_wburl" name="contentWbUrl" value="${content.contentWbUrl }" class="url">
									</div>
								</c:if>
								<c:if test="${content.contentWbUrl == '' || content.contentWbUrl == null}">
									<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
									<div class="dd_con">
										<input type="text" style="width: 300px;" id="type_wburl" name="contentWbUrl" value="${content.contentWbUrl }" class="url">
									</div>
								</c:if>
							</dd>
						</dl>
					</div>
					<!-- <dl class="cf">
						<dt>
							<span class="hot">*</span>所属栏目
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn02"
									style="display: inline-block;">+选择</a> <span id="columnSpan"
									class="warn_tip"> </span>
							</div>
						</dd>
					</dl> -->
					<dl class="cf">
						<dt>副标题</dt>
						<dd>
							<div class="dd_con show_hide">
								<input type="text" style="width:316px"  name="subtitle" value="${content.subtitle}">
							</div>
						</dd>
					</dl>
					<dl class="cf" style="display:none;">
						<dt></dt>
						<dd>
							<div class="dd_con">
								<div class="show_list" style="margin-top: 0px;">
									<table class="ev">
										<thead>
											<tr>
												<td width="70%">栏目</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="columnBody">
											<c:forEach items="${content.columConfigList }" var="v"
												varStatus="n">
												<tr>
													<td width='50%'>${v.columName }</td>
													<td><a href='javascript:;'
														onclick='removeBody(this,"#columndiv :checked")'>移除</a><input
														type='hidden' value="${v.id }" /></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</dd>
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
					<dl class="cf">
						<dt>
							<span class="hot"></span>所属分类
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn01">+选择</a><span
									id="typeSpan" class="warn_tip"> </span>
							</div>
						</dd>

					</dl>
					<dl class="cf">
						<dt></dt>
						<dd>
							<div class="dd_con">
								<div class="show_list" style="margin-top: 0px;">
									<table class="ev">
										<thead>
											<tr>
												<td width="70%">分类</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="typeBody">
											<c:if test="${!empty content.contentTypeList && fn:length(content.contentTypeList)>0}">
												<c:forEach items="${content.contentTypeList }" var="v"
													varStatus="num">
													<c:if test="${!empty content.typeId }">
														<tr>
														<td width='50%'>${v.typeName }</td>
														<td><a href='javascript:;'
															onclick='removeBody(this,"#typediv :checked")'>移除</a>
															<input id="contentTypeId" type='hidden' value="${content.typeId }" />
														</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>
					
					<!-- <dl class="cf">
						<dt>
							<span class="hot">*</span>所属模板
						</dt>
						<dd>
							<div class="dd_con">
								<select id="u1813_input" name="contentTemplateId"
									class="form-control" style="width: 200px;">

								</select>

							</div>
						</dd>

					</dl> -->
					<dl class="cf">
						<input type="hidden" name="contentTemplateId" id="contentTemplateId"/>
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
					<dl class="cf" style="overflow: inherit;">
						<dt>封面图片</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label
										for="file_name_img" style="float: left;"> <input
											type="file" id="file_name_img"
											onchange="javascript:setImagePreview();" name="image"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty content.surface_imageid}">
												<i style="float: left;">${content.title }</i>
												<a href="javascript:;" class="remove_file"
													style="float: left;">删除</a>
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" id="imageid" name="surface_imageid"
														value="${content.surface_imageid }" />
												</div>
												<div class="yulan"
													style="float: left; position: relative; padding-left: 10px;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px; z-index: 99;">
														<div class="pro_img_big">
															<img id="preview" alt=""
																src="<%=imgPath%>${content.surface_imageurl}"
																width="150px" height="180px">
														</div>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<i style="float: left;">未选择文件</i>
												<a href="javascript:;" class="remove_file"
													style="display: none; float: left;">删除</a>
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
										</c:choose></li>
								</ul>
							</div>
						</dd>
					</dl>
					<dl class="cf" style="overflow: inherit;">
						<dt>封面图片2</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label
										for="file_name_img_" style="float: left;"> <input
											type="file" id="file_name_img_"
											onchange="javascript:setImagePreview2();" name="image2"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty content.surface_imageid2}">
												<i style="float: left;">${content.title2 }</i>
												<a href="javascript:;" class="remove_file"
													style="float: left;">删除</a>
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" id="imageid" name="surface_imageid2"
														value="${content.surface_imageid2 }" />
												</div>
												<div class="yulan"
													style="float: left; position: relative; padding-left: 10px;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px; z-index: 99;">
														<div class="pro_img_big">
															<img id="preview" alt=""
																src="<%=imgPath%>${content.surface_imageurl2}"
																width="150px" height="180px">
														</div>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<i style="float: left;">未选择文件</i>
												<a href="javascript:;" class="remove_file"
													style="display: none; float: left;">删除</a>
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
										</c:choose></li>
								</ul>
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt>发布时间</dt>
						<dd>
							<div class="dd_con">
								<input id="release_time" style="width:185px;" name="releaseTime" value="${content.releaseTime}" class="Wdate" type="text" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd'})" />
							</div>
						</dd>
					</dl>
					<c:if test="${pd.columId eq 'fb65fe99fc574aa4844646d83bc428a0'}">
						<dl class="cf">
							<dt>合作伙伴</dt>
							<dd>
								<div class="dd_con">
									<select id="friend" name="friend" style="width:185px;height: 35px">
										<option id="associated-partner" value="associated-partner">associated-partner</option>
										<option id="preferred-partner" value="preferred-partner">preferred-partner</option>
										<option id="premium-partner" value="premium-partner">premium-partner</option>
									</select>
								</div>
							</dd>
						</dl>
					</c:if>
					<dl class="cf">
						<dt>
							<span class="hot"></span>图片内容
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:;" class="layer_btn layer_btn_addimg">上传图片</a>&nbsp;&nbsp;<i
									style="color: gray;">点击可追加</i>
							</div>
						</dd>
						<c:forEach items="${content.imageList }" var="i" varStatus="num">
							<dd style="overflow: inherit;">
								<div class="dd_con">
									<div class="add_img cf">
										<ul>
											<li class="file_upload file_upload_img"
												style="overflow: initial; height: 36px;"><span
												style="float: left;">图片</span> <label style="float: left;"
												for="file_name_img_00${num.count-1 }"> <input
													type="file"
													onchange="javascript:setImagePreview('${num.count-1 }');"
													id="file_name_img_00${num.count-1 }" name="images">
													<em>上传</em>
											</label> <c:if test="${i.imgurl!=null }">
													<i style="float: left;">${i.name }</i>
												</c:if> <c:if test="${i.imgurl==null }">
													<i style="float: left;">没有上传图片</i>
												</c:if> <a href="javascript:;" class="remove_file"
												style="float: left;">删除</a>
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" name="imageids[${num.count-1 }]"
														value="${i.imageId }">
												</div> 
												<input type="hidden" name="imageList[${num.count-1 }].imageId" value="${i.imageId }"> 
												<input type="hidden" name="imageList[${num.count-1 }].imgurl" value="${i.imgurl }">
												<div class="yulan"
													style="position: relative; padding-left: 10px; float: left;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px;">
														<div class="pro_img_big">
															<img id="preview${num.count-1 }" alt=""
																src="<%=imgPath%>${i.imgurl}" width="150px"
																height="180px">
														</div>
													</div>
												</div></li>
											<li>
												<p>
													<span>标题</span><input type="text"
														name="imageList[${num.count-1 }].title"
														value="${i.title }">

												</p>
												<p>
													<span>链接</span><input type="text"
														name="imageList[${num.count-1 }].tourl"
														value="${i.tourl }">
												</p>
											</li>
											<li>
											  	<p><span>排序</span><input type="text"  class="digits"  name="imageList[${num.count-1 }].forder" value="${i.forder }">  	
											  	</p>
										  	</li>
											<li>
												<p>
													<span>副标题</span><input type="text"
														name="imageList[${num.count-1 }].subhead"
														value="${i.subhead }">

												</p>
												
											</li>
											<%-- <li>
												<p>
													<span>背景颜色</span><input type="text" name="imageList[${num.count-1 }].color" value="${i.color }">
												</p>
											</li> --%>
											<li>
												<p>
													<span>背景颜色</span><input type="color" name="imageList[${num.count-1 }].color" value="${i.color }" >
												</p>
											</li>
											<li><span>描述</span>
											<h6>
													<textarea class="textarea_numb"
														name="imageList[${num.count-1 }].bz">${i.bz }</textarea>
													<p>
														<span class="word">0</span><span>/</span><span>200</span>
													</p>
												</h6></li>
										</ul>
										<div class="close_add">x</div>
									</div>
								</div>
							</dd>
						</c:forEach>
					</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>视频内容
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:;" class="layer_btn layer_btn_addfilm">上传视频</a>&nbsp;&nbsp;<i
									style="color: gray;">点击可追加 - 仅支持mp4后缀</i>
							</div>
						</dd>
						<c:forEach items="${content.videoList }" var="v" varStatus="num">
							<dd>
								<div class="dd_con">
									<div class="add_img cf">
										<ul>
											<li class="file_upload file_upload_film"
												style="overflow: initial; height: 36px;"><span
												style="float: left;">视频</span> <label style="float: left;"
												for="file_name_film_01${num.count-1 }"> <input
													type="file" id="file_name_film_01${num.count-1 }"
													name="sultipartFiles"> <em>上传</em>
											</label> <c:if test="${v.video_url!=null }">
													<i style="float: left;">${v.name }</i>
												</c:if> <c:if test="${v.video_url==null }">
													<i style="float: left;">没有上传视频</i>
												</c:if> <a href="javascript:;" class="remove_file"
												style="float: left;">删除</a>
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" name="videoids[${num.count-1 }]"
														value="${v.id }">

												</div> <input type="hidden"
												name="videoList[${num.count-1 }].video_url"
												value="${v.video_url }"> <input type="hidden"
												name="videoList[${num.count-1 }].id" value="${v.id }">
											</li>
											<li>
												<p>
													<span>标题</span> <input type="text"
														name="videoList[${num.count-1 }].video_title"
														value="${v.video_title }">
												</p>
												<p>
													<span>链接</span> <input type="text"
														name="videoList[${num.count-1 }].tourl"
														value="${v.tourl }" class="url">
												</p>

											</li>
											<li>
												<p><span>排序值</span><input type="text" class="digits" name="videoList[${num.count-1 }].forder" value="${v.forder}"></p>
											</li>
											<li><span>描述</span>
												<h6>
													<textarea class="textarea_numb" name="videoList[${num.count-1 }].video_content">
														${v.video_content }
													</textarea>
													<p>
														<span class="word">0</span><span>/</span><span>200</span>
													</p>
												</h6></li>
										</ul>
										<div class="close_add">x</div>
									</div>
								</div>
							</dd>
						</c:forEach>
					</dl>

					<script>
						$(function(){		
							$(document).on('click','.remove_file',function(){
								$(this).next('.hid_div').remove();
							});
							  //添加图片
							  var classNimg=0;
							  var num=0;
							  var img_list='';
							  $(document).on('click','.layer_btn_addimg',function(){
								  if($(this).parents('dd').next().size()>0 && $(this).parents('dl').find('dd:last-child').find('i').text()=='未选择文件'){
									  window.top.mesageTip("warn","请上传文件再点击上传");
								  }else{
							  	classNimg=classNimg+1;
							  	img_list+='<dd style="overflow:inherit;">';
							  	img_list+='<div class="dd_con">';
							  	img_list+='<div class="add_img cf">';
							  	img_list+='<ul><li class="file_upload file_upload_img" style="overflow:initial;height:36px;">';
								img_list+='<span style="float:left;">图片</span>';
							  	img_list+='<label style="float:left;" for="file_name_img_'+classNimg+'">';
							  	var fileId='file_name_img_'+classNimg;
							  	img_list+='<input type="file" onchange="javascript:setImagePreview(\''+fileId+'\');" id="'+fileId+'" name="images">';
							  	img_list+='<em>上传</em>';
							  	img_list+='</label><i style="float:left;">未选择文件</i>';
							  	img_list+='<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>';
							  	img_list+='<div class="yulan" style="display:none;position:relative;padding-left:10px;float:left;">预览';
							  	img_list+='<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">';
							  	img_list+='<div class="pro_img_big">';
							  	img_list+='<img id="preview'+fileId+'" alt="" src="" width="150px" height="180px">';
							  	img_list+='</div></div></div>';
							  	img_list+='</li><li><p><span>标题</span><input type="text" name="timageList['+num+'].title"></p>';
							  	img_list+='<p><span>链接</span><input type="text" name="timageList['+num+'].tourl" class="url"></p>';
							  	
							  	img_list+='</li><li><p><span>排序</span><input type="text" class="digits" name="timageList['+num+'].forder"></p>';
							  	
							  	img_list+='</li><li><p><span>副标题</span><input type="text" name="timageList['+num+'].subhead"></p>';
							  	img_list+='</li><li><p><span>背景颜色</span><input type="color" name="timageList['+num+'].color"></p>';
							  	img_list+='</li><li><span>描述</span><h6><textarea class="textarea_numb" name="timageList['+num+'].bz"></textarea>';
							  	img_list+='<p><span class="word">0</span><span>/</span><span>200</span></p></h6>';
							  	img_list+='</li></ul><div class="close_add">x</div>';
							  	img_list+='</div></div>';
							  	img_list+='</dd>';
							  	num++;
							  	$(this).parents('dl').append(img_list);
							  	img_list='';}
							  })
							  //添加视频
							  var number=0;
							  var classNfilm=0;
							  $(document).on('click','.layer_btn_addfilm',function(){
								  if($(this).parents('dd').next().size()>0 && $(this).parents('dl').find('dd:last-child').find('i').text()=='未选择文件'){
									  alert("请上传文件再点击上传") 
								  }else{
							  	classNfilm=classNfilm+1;
							  	var img_list='<dd>';
						  		img_list+='<div class="dd_con">';
						  		img_list+='<div class="add_img cf">';
						  		img_list+='<ul>';
						  		img_list+='<li class="file_upload file_upload_film">';
						  		img_list+='<span>视频</span>';
						  		img_list+='<label for="file_name_film_'+classNfilm+'">';
						  		img_list+='<input type="file" id="file_name_film_'+classNfilm+'" name="sultipartFiles">';
						  		img_list+='<em>上传</em></label>';
						  		img_list+='<i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a>';
						  		img_list+='</li><li><p><span>标题</span>';
						  		img_list+='<input type="text" name="tvideoList['+number+'].video_title"></p>';
						  		img_list+='<p><span>链接</span>';
						  		img_list+='<input type="text" name="tvideoList['+number+'].tourl" class="url">';
						  		img_list+='</p></li><li><p>';
						  		img_list+='<span>排序值</span><input type="text" class="digits" name="tvideoList['+number+'].forder"></p></li>';
						  		img_list+='<li><span>描述</span><h6><textarea class="textarea_numb" name="tvideoList['+number+'].video_content"></textarea>';
						  		img_list+='<p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>';
							  	number++;
							  	$(this).parents('dl').append(img_list);}
							  })
							  //关闭图片视频上传功能
							  $(document).on('click','.close_add',function(e){
							  	$(this).parents('dd').remove();
							  	e.stopPropagation();
							  });
						})
						</script>
					<!-- 
					<dl class="cf">
						<dt>
							<span class="hot"></span>属性管理
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn0b">+选择</a><span
									id="propertySpan" class="warn_tip"></span>
							</div>
						</dd>

					</dl>
					-->
					<dl class="cf">
						<dt>solr状态</dt>
						<dd>
							<div class="dd_con show_hide">
								
								<label for="show"><input type="radio" id="show" name="solrStatus" value="1" /><span>显示</span></label> 
								<label for="hide"><input type="radio" id="hide" name="solrStatus" value="0" /><span>隐藏</span></label>
							</div>
						</dd>
					</dl>
					
					<dl class="cf">
						<dt>标记</dt>
						<dd>
							<div class="dd_con hot_re">
								<c:if test="${content.hot==1 }">
									<label for="hot" class="active">
										<input type="checkbox"id="hot" checked name="hot" value="1" /><span>热</span><i></i>
									</label>
								</c:if>
								<c:if test="${content.hot!=1 }">
									<label for="hot">
										<input type="checkbox" id="hot" name="hot" value="1" /><span>热</span><i></i>
									</label>
								</c:if>
								<c:if test="${content.recommend==1 }" >
									<label for="re" class="active">
										<input type="checkbox" id="re" checked name="recommend" value="1" /><span>荐</span><i></i>
									</label>
								</c:if>
								<c:if test="${content.recommend!=1 }">
									<label for="re">
										<input type="checkbox" id="re" name="recommend" value="1" /><span>荐</span><i></i>
									</label>
								</c:if>
							</div>
						</dd>
					</dl>
					
					<div class="cf" id="extend">
					<c:forEach items="${content.fileds }" var="filed" varStatus="num">
							<dl>
								<dt>
									<span class='hot'></span>${filed.name }</dt>
								<dd>
									<div class='dd_con'>
										<c:if test="${filed.fieldtype==3 }"><input type='date' name='fileds[${num.count-1 }].value' value="${filed.value }"><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></c:if>
										<c:if test="${filed.fieldtype!=3 && filed.fieldtype!=2}"><input type='text' name='fileds[${num.count-1 }].value' value="${filed.value }"><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></c:if>
										<c:if test="${filed.fieldtype==2 }">
											<ul><li><h6>
												<textarea name='fileds[${num.count-1 }].value'>${filed.value }</textarea><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6></li></ul>
										</c:if>
									</div>
								</dd>
							</dl>
							<input type='hidden' name='fileds[${num.count-1 }].id' value='${filed.id }' />
							<input type='hidden' name='fileds[${num.count-1 }].name' value='${filed.name }' />
							<input type='hidden' name='fileds[${num.count-1 }].fieldtype' value='${filed.fieldtype }' />
						</c:forEach>
					
					</div>
					<dl class="cf">
						<dt>
							<span class="hot"></span>详情描述
						</dt>
						<dd style="z-index: 1; position: relative;">
							<div class="dd_con" style="z-index: 1;">
								<div class="eidt_box cf" style="z-index: 1;">
									<table id="table_report"
										class="table table-striped table-bordered table-hover"
										style="z-index: 1;">
										<tr>
											<td style="z-index: 1;"><script id="editor"
													type="text/plain"
													style="width: 90%; height: 240px; z-index: 1;">${content.contentTxt}</script>
											</td>
										</tr>

									</table>
								</div>
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt>
							<span class="hot"></span>概要描述
						</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li>
										<h6>
											<textarea class="textarea_numb" name="contentSummary">${content.contentSummary }</textarea>
											<p>
												<span class="word">0</span><span>/</span><span>200</span>
											</p>
										</h6>
									</li>
								</ul>
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>方案来源</dt>
						<dd><div class="dd_con"><input type="text" name="programme" value="${content.programme}" /></div></dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>SEO_标题</dt>
						<dd><div class="dd_con"><input type="text" name="seo_title" value="${seo.SEO_TITLE }" /></div></dd>
					</dl>
					<dl class="cf">
						<dt>SEO关键字</dt>
						<dd>
							<div class="dd_con">
								<input type="text" name="contentKeyWords"
									value="${seo.SEO_KEYWORDS }" />
									<input type="hidden" name="SEO_ID" value="${seo.ID }"/>
							</div>
						</dd>
					</dl>

					<dl class="cf">
							<dt>SEO描述</dt>
							<dd><div class="dd_con"><input type="text" name="seo_description" value="${seo.SEO_DESCRIPTION }"/></div></dd>
						</dl>
					<dl class="cf">
						<dt>相关内容</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:;" class="layer_btn layer_btn05">+选择</a>
							</div>
						</dd>
						<dd>
							<div class="dd_con">
								<div class="show_list">
									<table class="ev">
										<thead>
											<tr>
												<td width="50%">内容标题</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="relationBody">
											<c:forEach items="${content.contentRelevantList }" var="list">
												<tr>
													<td width='50%'>${list.contentTitle }</td>
													<td><a href='javascript:;' onclick='removes(this)'>移除</td>
												</tr>
												<input type='hidden' name='contentRelevantIdList'
													value='${list.id }' />
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>

				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" onclick="save('0')" /> 
						<!-- <a href="javascript:void(0)" class="submit_a_btn" onclick="save('1')">保存并继续添加</a>  -->
						<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}"
						class="submit_re_btn">取消</a>
				</div>
				<!-- 弹窗——相关内容 -->
				<div class="layer_bg layer_bg08" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择分类</span>
							<p class="close"
								onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="typediv" class="radio_lock">
								<c:forEach items="${content.contentTypeList }" var="t">
									<c:forEach items="${content.columConfigList }" var="c">
										<input type="checkbox" typeid="${t.id }" name="contenttypeids"
											checked="checked" value="${t.id }-${c.id}">
									</c:forEach>
								</c:forEach>
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的产品分类，请添加分类后，再操作</div>

							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#typediv :checked','#typeBody','.layer_bg08');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#typediv input','#typeBody input')">取消</a>
						</div>
					</div>
				</div>
				<div class="layer_bg layer_bg02" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择栏目</span>
							<p class="close"
								onclick="popupHiden(this,'#columndiv input','#columnBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="columndiv">
								<c:forEach items="${content.columConfigList }" var="c"
									varStatus="n">
									<input type="checkbox" name="columconfigIds" checked="checked"
										value="${c.id }" />
								</c:forEach>
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的栏目，请添加栏目后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg02');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								onclick="popupHiden(this,'#columndiv input','#columnBody input')"
								class="submit_re_btn">取消</a>
						</div>

					</div>
				</div>

				<div class="layer_bg layer_bg09" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>相关内容</span>
							<p class="close">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="productdiv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的相关内容，请添加内容后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button" onclick="setproductdiv();"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="confirm_re_btn">取消</a>
						</div>

					</div>
				</div>
				<div class="layer_bg layer_bg0b" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>属性管理</span>
							<p class="close" onclick="popupHidens(this,'#extenddiv input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="extenddiv" class="radio_lock">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的属性，请添加后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button" onclick="propertyModalHide();"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHidens(this,'#extenddiv input')">取消</a>
						</div>

					</div>
				</div>
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->
	<!-- 百度富文本编辑框-->
	<script type="text/javascript" charset="utf-8"
		src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8"
		src="plugins/ueditor/ueditor.all.js"></script>
</body>
</html>