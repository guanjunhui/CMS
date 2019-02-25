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
		$('.layer_btn02').click(function(){ // 模板窗口打开
		  	$('.layer_bg02').show();
	    });
		$('.layer_btn0a').click(function(){ // 栏目窗口打开
		  	$('.layer_bg0a').show();
	    });
		$('.layer_btn0b').click(function(){// 扩展字段窗口打开
		  	$('.layer_bg0b').show();
	    });
			setColumTemplatediv();
			setExtendDiv();
			
		$('.layer_btn05').click(function(){// 相关产品窗口打开
			var $ids=$("#typediv input[type='checkbox']:checked");
			 if($ids.length==0){
				 window.top.mesageTip("warn","请选择所属类型");
				return false;
			} 
			 var fileid=$("#fileid").val();
			 var ids=[];
				var param="id=";
				$.each($ids,function(index,obj){
					ids.push($(obj).attr("typeid"));
				});
				param+=ids.join("&id=");
				param+="&fileid="+fileid;
				var $rlb= $("#relationBody input");
			$.ajax({
				url:adminPath+"fileresource/findByTypeIds.do?",
				type:"post",
				data:param,
				success:function(data){
					var html='';
					if(data.list.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
						$("#productdiv").html(html);
						$('.layer_bg09').show();
						return false;
					}
					 $.each(data.list,function(index,obj){
						 console.log(obj);
						 html+='<li>';
						 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.fileid+'"><input type="checkbox" name="'+obj.title+'" value="'+obj.fileid+'" id="cc_'+obj.fileid+'" /><span>'+obj.title+'</span><i></i></label></p>';
								 $.each($rlb,function(i,n){
									 if(n.value==obj.id){
										 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.fileid+'"><input type="checkbox" checked="checked" name="'+obj.title+'" value="'+obj.fileid+'" id="cc_'+obj.fileid+'" /><span>'+obj.title+'</span><i class="active"></i></label></p>';
									 }
								 });

						 	html+=is_checked;
						 
						 html+='</li>';
					 });
					$("#productdiv").html(html);
					$('.layer_list_other li .show_btn').each(function(){
				    	if($(this).parent('p').next('dl').size()==0){
				    		$(this).hide();
				    	}
				     });
				}
			})
		  	$('.layer_bg09').show();
	    });
		
	});
	function setExtendDiv(){
		
		var columId=$('#columId').val();
		$.ajax({
			url:adminPath+'contentExtendFiledController/getTree.do',
			data:{"id":columId},
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
					url:adminPath+"downloadType/getTreeByColumId.do",
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
function setproductdiv(){//确定相关内容窗口关闭 
		var $ids=$("#productdiv input[type='checkbox']:checked"); 
		if($ids.length==0){
			$('.layer_bg09').hide();
			return false;
		}
		 var ids=[];
			var param="id=";
			$.each($ids,function(index,obj){
				ids.push(obj.value);
			});
			param+=ids.join("&id=");
			$.ajax({
				type:"post",
				url:adminPath+"fileresource/findTypeById.do",
				data:param,
				success:function(data){
					htmlStr="";
					$.each(data.list,function(index,obj){
						htmlStr+="<tr>";
						htmlStr+="<td width='50%' >"+obj.title+"</td>";
						htmlStr+="<td>";
						htmlStr+=eachType(obj.fileTypeList,"");
						htmlStr+="</td>"
						htmlStr+="<td><input type='hidden' name='fileids'  value='"+obj.id+"'/><a href='javascript:;' onclick='removes(this)'>移除</a></td></tr>";
						
					});
					$("#relationBody").html(htmlStr);
					 //控制折叠按钮是否显示
				     $('.layer_list_other li .show_btn').each(function(){
				    	if($(this).parent('p').next('dl').size()==0){
				    		$(this).hide();
				    	}
				     });
				}
			});

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
			url:adminPath+"downloadType/getTreeByColumId.do",
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
function flieValidate(param){
	if($(param)[0].files[0].size==0){
		window.top.mesageTip("warn","上传文件大小不能为0");
		return false;
	}
	flieValidates();
}
function flieValidates(){
	var $files=$("#dd_file input[type='file']");
	var nummun=0;
	$.each($files,function(i,n){
		if($(n).val()!=null && $(n).val()!=""){
			nummun+=$(n)[0].files[0].size;
		}
	});
	if(nummun>104857600){
		window.top.mesageTip("warn","上传文件过大");
		return false;
	}else if(nummun==0){
		window.top.mesageTip("warn","上传文件大小不能为0");
		return false;
	}
}
function eachType(list,htmlStr){
	htmlStr="";
	$.each(list,function(index,obj){
		if(list.length-1==index){
			htmlStr+=obj.download_name+"";
		}else{
			htmlStr+=obj.download_name+",";
		}
	});
	return htmlStr;
}

function setColumTemplatediv(){
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
				 $("#templateid").val(data.id);
			 }
		}
	});
}
/*
//获取模板类型树形结构
function setColumTemplatediv(){
	$.ajax({
		type: "GET",
		url:adminPath+"template/getDefinedTree.do",
		data:{type:5,temType:2},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200&&result.data!=null){
				 appendColumTemplatediv(result.data);
			 }
		}
	});
}

//选择模板弹出内容填充
function appendColumTemplatediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#templatediv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  att="'+obj.name+'" type="radio" name="templateid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';	
		 $("#templatediv input[type='radio']:checked").each(function(){
	 			if(this.value==obj.id){
	 				 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" att="'+obj.name+'" type="radio" name="templateid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';

	 			}
	 		});
	 		html+=is_checked;
		 html+='</li>';
	 });
	 $("#templatediv").html(html);
	 $(":input[name='templateid']:checked").each(function(){
			var This = $(this).parents('li');
			This.find('dl').slideDown()
		});
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
}
*/

//获取产品类型树形结构
function setTypediv(param){
	$.ajax({
		type: "GET",
		url:adminPath+"downloadType/getTreeByColumId.do",
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
//选择栏目弹出内容填充
function appendTypediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的产品分类，请添加分类后，再操作</div>';
		 $("#typediv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var typeidAndColumnids=obj.download_id+'-'+eachColumnids('',obj.columConfigList);
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.download_id+'"><input typeid="'+obj.download_id+'" type="checkbox" att="'+obj.download_name+'"  name="typeids" value="'+typeidAndColumnids+'" id="cc_'+obj.download_id+'" /><span>'+obj.download_name+'</span><i></i></label></p>';
		 $("#typediv input[type='checkbox']:checked").each(function(){
			 if($(this).attr("typeid")==obj.download_id){
			    is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.download_id+'"><input typeid="'+obj.download_id+'" checked="checked" type="checkbox" att="'+obj.download_name+'"  name="typeids" value="'+typeidAndColumnids+'" id="cc_'+obj.download_id+'" /><span>'+obj.download_name+'</span><i class="active"></i></label></p>';
			 }
		 });
		 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#typediv").html(html);
	 $(":input[name='typeids']:checked").each(function(){
			var This = $(this).parents('li');
			This.find('dl').slideDown()
		});
	 $('.layer_list_other li .show_btn').each(function(){
	     	if($(this).parent('p').next('dl').size()==0){
	     		$(this).hide();
	     	}
	     });
}

function eachList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var typeidAndColumnids=obj.download_id+'-'+eachColumnids('',obj.columConfigList);
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.download_id+'"><input typeid="'+obj.download_id+'" type="checkbox" att="'+obj.download_name+'"  name="typeids" value="'+typeidAndColumnids+'" id="cc_'+obj.download_id+'" /><span>'+obj.download_name+'</span><i></i></label></p>';
		 $("#typediv input[type='checkbox']:checked").each(function(){
			 if($(this).attr("typeid")==obj.download_id){
			    is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.download_id+'"><input typeid="'+obj.download_id+'" checked="checked" type="checkbox" att="'+obj.download_name+'"  name="typeids" value="'+typeidAndColumnids+'" id="cc_'+obj.download_id+'" /><span>'+obj.download_name+'</span><i class="active"></i></label></p>';
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
//获取栏目类型树形结构
function setColumdiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/getAssignTypeTree.do",
		data:{TEM_TYPE:5},
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
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 $("#columnBody input").each(function(){
			 if(this.value==obj.id){
			 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
			 }

		 });
		 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#columndiv").html(html);
	 $(":input[name='columnids']:checked").each(function(){
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
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 $("#columnBody input").each(function(){
			 if(this.value==obj.id){
				is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input  checked="checked" type="checkbox" att="'+obj.name+'" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>';
			 	$("#columName").html(obj.name);
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
					htmlStr+="<dd><div class='dd_con'><input type='date' name='fileds["+index+"].value' ><input class='sortStr' name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></div></dd></dl>";
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
					htmlStr+="<dd><div class='dd_con'><input type='text' name='fileds["+index+"].value' ><input class='sortStr' name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' onblur='typeSort(this)' style='width:100px;text-align:center'/></div></dd></dl>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/>";
				}
			})
			$("#extend").html(htmlStr);
		
	
	$('.layer_bg0b').hide();

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
function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	var $files=$("#dd_file input[type='file']");
	var nummun=0;
	$.each($files,function(i,n){
		if($(n).val()!=null && $(n).val()!=""){
			nummun+=$(n)[0].files[0].size;
		}
	});
	if(nummun>104857600){
		window.top.mesageTip("warn","上传文件过大");
		return false;
	}else if(nummun==0){
		if($files.length>=2){
			window.top.mesageTip("warn","上传文件大小不能为0");
			return false
		};
	}
	/* var $ids=$("#typediv input[type='checkbox']:checked");
	 if($ids.length==0){
		 window.top.mesageTip("warn","请选择所属分类");
		return false;
	} */
	/*  var $tIds=$("#templatediv input[type='radio']:checked");
	 if($tIds.length==0){
		 window.top.mesageTip("warn","请选择所属模板");
		return false;
	} */
	$("#TXT").val(getContent());
	sub(type);
	    /* var formData = new FormData($("#Form")[0]);
		$.ajax({
			type: "POST",
			url:adminPath+"fileresource/update.do",
			data:formData,
			dataType:'json',
			cache: false,
	        processData: false,
	        contentType: false,
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 window.top.mesageTip("success","保存成功");
						 location.href=adminPath+'fileresource/toAdd.do';
					 }else{
						 window.top.mesageTip("success","保存成功");
						 location.href=adminPath+'fileresource/list.do';
					 }
				 }else{
					 window.top.mesageTip("failure","保存失败");
				 }
			}
		}); */
}
function sub(type){
	$("#is_add").val(type);
	$("#Form").submit();
}
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
<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
			<i>></i>
			<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}">内容列表</a>
			<i>></i>
			<i>文件修改</i>
		</div>
	</div>
				
				<div class="cms_c_list cf">
					<h3>添加文件</h3>
					
					<form   id="Form" action="<%=adminPath %>fileresource/update.do?columId=${pd.columId}&topColumId=${pd.topColumId}" method="post" enctype="multipart/form-data" >
						<input type="hidden" id="TXT" name="TXT"/>
						<input type="hidden" id="is_add" name="is_add">
						<input type="hidden" id="id" name="fileid" value="${file.fileid}"/>
						<input type="hidden" id="columId" value="${pd.columId}"/>
						<div class="add_btn_con wrap cf">
						
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot">*</span>文件标题</dt>
								<dd><div class="dd_con"><input type="text" id="title" name="title" value="${file.title }" required></div></dd>
								<dd class="zp_dd">
									<c:if test="${file.wburl != '' && file.wburl != null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i class="active"></i></label>
										<div class="dd_con" style="display: block;">
											<input type="text" style="width: 300px;" id="wburl" name="wburl" value="${file.wburl }" class="url">
										</div>
									</c:if>
									<c:if test="${file.wburl == '' || file.wburl == null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
										<div class="dd_con">
											<input type="text" style="width: 300px;" id="wburl" name="wburl" value="${file.wburl }" class="url">
										</div>
									</c:if>
								</dd>
							</dl>
						</div>
						<!--  <dl class="cf">
							<dt><span class="hot">*</span>所属栏目</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn0a">+选择</a><span id="columnSpan" class="warn_tip"></span></div></dd>
						
						</dl>-->
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
											<c:forEach items="${file.columConfigList }" var="c" varStatus="num">
											<tr><td width='50%' >${c.columName }</td><td><a href='javascript:;' onclick='removeBody(this,"#columndiv :checked")'>移除</a><input type='hidden'  value="${c.id }" /></td></tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>所属分类</dt>
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
											<c:forEach items="${file.fileTypeList }" var="v" varStatus="num">
											<c:if test="${!empty v.download_id }">
												<tr><td width='50%' >${v.download_name }</td><td><a href='javascript:;' onclick='removeBody(this,"#typediv :checked")'>移除</a><input type='hidden'  value="${v.download_id }" /></td></tr>
											</c:if>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl>
						<dl class="cf up_file_o" id="dd_file">
							<dt>上传文件</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn"><label for="up_file_o1">上传文件<input type="file" id="up_file_o1" value="" name="sultipartFiles" onchange="flieValidate(this)"  style="width:0px;height:0px;overflow:hidden;"></label></a>&nbsp;&nbsp;<i style="color: gray;">点击可追加</i></div></dd>
							<c:if test="${!empty file.files }">
								<dd ><div class="dd_con">
								<div class="show_list">
								<table class="ev">
									<thead><tr><td width="50%">文件名称</td><td>类型</td><td>操作</td><td>文档类型</td></tr></thead>
									<tbody>
									  <c:if test="${!empty file.files&& file.files.size()>0 }">
									  	<c:forEach items="${file.files }" var="f" varStatus="num">
										   <tr>
											   <td>${f.name }</td>
											   <td>${f.type }</td>
											   <td><input type="hidden" name="filesIds[${num.count-1 }]" value="${f.id }" /><a href="javascript:;" class="up_file_o1${num.count }">删除</a></td>
										   	   <td>
												<input type="radio" id="no"  value="0"<c:if test="${f.mark == 0 }">checked</c:if>  name="downloadMark[${num.count-1 }].mark" ><span style="margin-right:10px;">中文</span>
										   	   	<input type="radio" id="yes" value="1" <c:if test="${f.mark == 1}">checked</c:if> name="downloadMark[${num.count-1 }].mark" ><span>英文</span>
										   	   </td>
										   </tr>
									    </c:forEach>
									  </c:if>
									</tbody>
								</table>
								</div>
							</div></dd>
							</c:if>
							<c:if test="${empty file.files }">
								<dd class="display"><div class="dd_con">
								<div class="show_list">
								<table class="ev">
									<thead><tr><td width="50%">文件名称</td><td>类型</td><td>操作</td><td>文档类型</td></tr></thead>
									<tbody>
									</tbody>
								</table>
								</div>
							</div></dd>
							</c:if>
						</dl>
						<script type="text/javascript">
							var td_html="";
							var label_html="";
							var ii=1;
							$(function(){
								$(document).on('change','.up_file_o label',function(e){
									
									var data = $(this).find('input').val();
									if(data!=''){
										$(this).parents('dd').next('dd').show();
										var file_name=data.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
										var file_type=(/[.]/.exec(data)) ? /[^.]+$/.exec(data.toLowerCase()) : '';
										td_html+='<tr><td>'+file_name+'</td><td>'+file_type+'</td><td><a href="javascript:;" class="up_file_o'+ii+'">删除</a></td>';
										td_html+='<td><input type="radio" id="no" value="0" checked="checked" name="anotherMark['+(ii-1)+'].mark"/><span style="margin-right:10px;">中文</span>'
										td_html+='<input type="radio" id="yes" value="1" name="anotherMark['+(ii-1)+'].mark" /><span>英文</span>'
										td_html+='</td></tr>'
										$('.up_file_o .show_list tbody').append(td_html);
										td_html="";

										ii++;
										label_html+='<div  class="dd_con"><a href="javascript:;" class="layer_btn"><label for="up_file_o'+ii+'">上传文件<input type="file" id="up_file_o'+ii+'" name="sultipartFiles" onchange="flieValidate(this)"  style="display:none;"></label></a></div>';
										$(this).parents('.dd_con').css({
											'height':'0',
											'width':'0',
											'overflow':'hidden'
										});

										$(this).parents('dd').append(label_html)
										label_html="";
									}
								})
								$(document).on('click','.up_file_o tbody a',function(){
									if($(this).parents('tbody').find('tr').size()==1){
										$('.up_file_o').find('.display').hide();
									}
									$(this).parents('tr').remove();

									var a_id=$(this).attr('class')
									$('.up_file_o').find('.dd_con').each(function(){
										if($(this).find('input').attr('id')==a_id){
											$(this).remove();
										}
									});
									
								});
							})
						</script>
						<style type="text/css">
							.up_file_o label{display: block;cursor: pointer;}
							.display{display: none;}
						</style>

						<!--  <dl class="cf">
							<dt><span class="hot">*</span>选择模板</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a><span id="templateSpan" class="warn_tip"></span></div></dd>
						
						</dl>-->
						<dl class="cf">
							<input type="hidden" name="templateid" id="templateid"/>
							<dt>
								所属模板
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id="u1813_input" class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						
						<!-- <dl class="cf" style="display:none;">
							<dt></dt>
							<dd><div class="dd_con">
								<div class="show_list" style="margin-top:0px;">
									<table class="ev">
										<thead><tr><td width="70%">模板</td><td>操作</td></tr></thead>
										<tbody id="templateBody">
											<tr><td width='50%' >${file.tem_name }</td><td><a href='javascript:;' onclick='removeBody(this,"#templatediv :checked")'>移除</a><input type='hidden'  value="${file.templateid }" /></td></tr>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl> -->
						
						<dl class="cf" style="overflow:inherit;">
							<dt>封面图片</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label for="file_name_img" style="float:left;">
										<input type="file" id="file_name_img" onchange="javascript:setImagePreview();" name="image"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty file.imageid}">
												<i style="float:left;">${file.i_title }</i>
												<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
												<div class="hid_div" style="height:0px;overflow:hidden;">
													<input type="hidden" name="imageid" value="${file.imageid }"/>
													</div>
												<div class="yulan"  style="float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
														<div class="pro_img_big"><img id="preview" alt="" src="<%=imgPath%>${file.imgurl}" width="150px" height="180px"></div>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<i style="float:left;">未选择文件</i>
												<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>
												<div class="yulan"  style="display:none;float:left;position:relative;padding-left:10px;">预览
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
						<dt>
							<span class="hot"></span>图片内容
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:;" class="layer_btn layer_btn_addimg">上传图片</a>&nbsp;&nbsp;<i
									style="color: gray;">点击可追加</i>
							</div>
						</dd>
						<c:forEach items="${file.timageList }" var="i" varStatus="num">
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

												</div> <input type="hidden"
												name="timageList[${num.count-1 }].imageId"
												value="${i.imageId }"> <input type="hidden"
												name="timageList[${num.count-1 }].imgurl"
												value="${i.imgurl }">
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
														name="timageList[${num.count-1 }].title"
														value="${i.title }">

												</p>
												<p>
													<span>链接</span><input type="text"
														name="timageList[${num.count-1 }].tourl"
														value="${i.tourl }">
												</p>
											</li>
											<li>
											  	<p><span>排序</span><input type="text" name="timageList[${num.count-1 }].forder" value="${i.forder }"></p>
											</li>
											<li><span>描述</span>
											<h6>
													<textarea class="textarea_numb"
														name="timageList[${num.count-1 }].bz">${i.bz }</textarea>
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
					<div class="cf" id="extend">
					<c:forEach items="${file.fileds }" var="filed" varStatus="num">
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
							<dt><span class="hot"></span>详情描述</dt>
							<dd style="z-index:1;position:relative;">
								<div class="dd_con" style="z-index:1;">
									<div class="eidt_box cf" style="z-index:1;">
										<table id="table_report" class="table table-striped table-bordered table-hover" style="z-index:1;">
												<tr>
													<td style="z-index:1;">
													  <script id="editor" type="text/plain" style="width:90%;height:240px;z-index:1;">${file.TXT}</script>
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
												<textarea class="textarea_numb" name="summary">${file.summary }</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
					<dl class="cf">
						<dt>SEO标题</dt>
						<dd><div class="dd_con"><input type="text" name="seo_title" value="${seo.SEO_TITLE }" /></div></dd>
					</dl>
					<dl class="cf">
						<dt>SEO关键字</dt>
						<dd>
							<div class="dd_con">
								<input type="text" name="keywords"
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
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn05">+选择</a></div></dd>
							<dd><div class="dd_con">
								<div class="show_list">
								<table class="ev">
									<thead><tr><td width="50%">文件标题</td><td>所属分类</td><td>操作</td></tr></thead>
									<tbody  id="relationBody">
									<c:forEach items="${aa }" var="relevan">
										     <tr><td width="50%">${relevan.title }</td><td>
										      <c:forEach items="${relevan.fileTypeList }" var="fileType" varStatus="num">
										     	<c:if test="${relevan.fileTypeList.size()==num.count }">${fileType.download_name }</c:if>
										     	<c:if test="${relevan.fileTypeList.size()!=num.count }">${fileType.download_name },</c:if>
										     </c:forEach> 
										     </td>
										     <td><input type="hidden" name="fileids" value="${relevan.id }"/><a href='javascript:;' onclick='removes(this)'>移除</a></td></tr>
									       </c:forEach>
									</tbody>
								</table>
								</div>
							</div></dd>
						</dl>
						
					</div>
					<div class="all_btn cf">
					<span id="result"></span>
						<input type="button" class="submit_btn" value="保存" onclick="save('0')"/>
						<a href="javascript:void(0)" class="submit_a_btn" onclick="save('1')">保存并继续添加</a>
						<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}" class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择分类</span><p class="close" onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p></h3>
								<div class="layer_list_other mScrol222 cf" >
									<ul id="typediv" class="radio_lock">
										<c:forEach items="${file.fileTypeList }" var="v" varStatus="num">
											<c:forEach items="${v.columConfigList }" var="c" varStatus="m">
											  <c:if test="${v.download_id != null }">
											  	<input type="checkbox" typeid="${v.download_id }" checked="checked" name="typeids" value="${v.download_id }-${c.id}"></input>
											  </c:if>
											</c:forEach>
							             </c:forEach>
										 <div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的分类，请添加分类后，再操作</div>
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="modalHide('#typediv :checked','#typeBody','.layer_bg08');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#typediv input','#typeBody input')">取消</a>
								</div>
						</div>
					</div>
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>模板</span><p class="close" onclick="popupHiden(this,'#templatediv input','#templateBody input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="templatediv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的模板，请添加模板后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="modalHide('#templatediv :checked','#templateBody','.layer_bg02');" class="submit_btn" value="确定" />
									<span  class="submit_re_btn" onclick="popupHiden(this,'#templatediv input','#templateBody input')">取消</span>
							</div>
						</div>
					</div>
					<!--  -->
					<div class="layer_bg layer_bg09" style="display:none;">
						<div class="layer_con cf">
							<h3><span>相关内容</span><p class="close" >x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="productdiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的相关内容，请添加内容后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="setproductdiv();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="confirm_re_btn" >取消</a>
								</div>
							
						</div>
					</div>
					
					<div class="layer_bg layer_bg0a" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close" onclick="popupHiden(this,'#columndiv input','#columnBody input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="columndiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的栏目，请添加栏目后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg0a');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#columndiv input','#columnBody input')">取消</a>
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
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
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
							  	img_list+='</li><li><p><span>标题</span><input type="text" name="pictureList['+num+'].title"></p>';
							  	img_list+='<p><span>链接</span><input type="text" name="pictureList['+num+'].tourl" class="url"></p>';
							  	img_list+='</li><li><p><span>排序</span><input type="text" class="digits" name="pictureList['+num+'].forder"></p>';
							  	img_list+='</li><li><span>描述</span><h6><textarea class="textarea_numb" name="pictureList['+num+'].bz"></textarea>';
							  	img_list+='<p><span class="word">0</span><span>/</span><span>200</span></p></h6>';
							  	img_list+='</li></ul><div class="close_add">x</div>';
							  	img_list+='</div></div>';
							  	img_list+='</dd>';
							  	num++;
							  	$(this).parents('dl').append(img_list);
							  	img_list='';}
							  });
							//关闭图片视频上传功能
							  $(document).on('click','.close_add',function(e){
							  	$(this).parents('dd').remove();
							  	e.stopPropagation();
							  });
							  
						});
						</script>
	
	
	
</body>
</html>