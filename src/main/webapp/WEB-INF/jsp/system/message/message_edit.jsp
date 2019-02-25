<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
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
<script type="text/javascript" charset="utf-8" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		setMessageType();
		//弹框取消按钮
		/* $(document).on('click','.submit_re_btn',function(){
		    $('.layer_list li label').removeClass('radio_btn');
		  	$('.layer_bg').find('input').removeAttr("checked");
		  	$('.layer_bg').find('i').removeClass('active');
		  	$(this).parents('.layer_bg').hide();
		  }); */ 
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
		//热键
		 $('#re').click(function(){
			if($('#ishotlable').hasClass('active')){
				var hotObject=$('#re').attr('value','0');
				var hot=hotObject.val();
				$('#hots').val(hot);
			}else{
				var hotObject=$('#re').attr('value','1');
				var hot=hotObject.val();
				$('#hots').val(hot);
			}
		}); 
		//推荐
		$('#hot').click(function(){
			if($('#isrecommendlable').hasClass('active')){
				var recommendObj=$('#hot').attr('value','0');
				var recommend=recommendObj.val();
				$('#recommends').val(recommend); 
			}else{
				var recommendObj=$('#hot').attr('value','1');
				var recommend=recommendObj.val();
				$('#recommends').val(recommend); 
			}
		});
		 
		
		
		 $('.layer_btn01').click(function(){ //所属分类窗口打开
			var typeIds=$("#columnBody input");
			if(typeIds.length==0){
				window.top.mesageTip("warn","请先选择栏目!");
				return false;
			}
			ids=[];
			$.each(typeIds,function(){
				ids.push(this.value);
			});
			param="columnid=";
			param+=ids.join("&columnid=");
			setTypediv(param); 
		  	$('.layer_bg08').show();
	    });
		 $('.layer_btn04').click(function(){// 字段类型窗口打开
			  	$('.layer_bg04').show();
		  });
		$('.layer_btn02').click(function(){ // 模板窗口打开
		  	$('.layer_bg02').show();
	    });
		//栏目类型模块
		setColumdiv();
		//扩展字段类型
		setExtendWord();
		//打开弹窗后,里面是相关分类的复选框,对其进行操作,如果没选则提示,请先选择资讯类型
		$('.layer_btn05').click(function(){// 相关资讯窗口打开
			var $ids=$("#typediv input[type='checkbox']:checked");
			 if($ids.length==0){
				window.top.mesageTip("warn","请先选择资讯类型!");
				return false;
			} 
			 var ids=[];
				var param="id=";
				$.each($ids,function(index,obj){
					ids.push(obj.value);
				});
				param+=ids.join("&id=");
			var $rlb= $("#relationBody input");
			$.ajax({
				url:adminPath+"mymessage/findMessageBytypeId.do?"+param,
				type:"post",
				success:function(data){
					var html='';
					if(data.length==0){
						html="对不起,没有相关的数据";
						$("#productdiv").html(html);
						$('.layer_bg05').show();
					}
						 $.each(data,function(index,obj){
							 html+='<li>';
							 var is_checked='';
							 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox"  name="'+obj.message_title+'" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.message_title+'</span><i></i></label></p>';
							 $.each($rlb,function(i,n){
								 if(n.value==obj.id){
									  is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" checked="checked" name="'+obj.message_title+'" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.message_title+'</span><i class="active"></i></label></p>'; 
								 }
					          });
							 html+=is_checked;
							 html+='</li>';
						 });

					$("#productdiv").html(html);
				}
			})
		  	$('.layer_bg05').show();
	    });
		settemplate();
		
	})
function typeClose(param){
		var $ids=$("#typediv input[type='checkbox']:checked");
		$("#typediv li label").removeClass('radio_btn');
		$("#typediv").find('i').removeClass('active');
		$.each($ids,function(index,obj){
			$(obj).removeAttr("checked");
		});
		$("#typeSpan").html("");
	  	$(param).parents('.layer_bg').hide();
}
function typeHiden(param){
		$("#typediv").find("input").removeAttr("checked");
		$("#typediv li label").removeClass('radio_btn');
		$("#typediv").find('i').removeClass('active');
		$("#typeSpan").html("");
	  	$(param).parents('.layer_bg').hide();
}
function templateClose(param){
	$("#templatediv input[type='radio']:checked").removeAttr("checked");
	$("#templatediv li label").removeClass('radio_btn');
	$("#templatediv").find('i').removeClass('active');
	$("#templateSpan").html("");
  	$(param).parents('.layer_bg').hide();
}
function templateHiden(param){
	$("#templatediv input[type='radio']:checked").removeAttr("checked");
	$("#templatediv li label").removeClass('radio_btn');
	$("#templatediv").find('i').removeClass('active');
	$("#templateSpan").html("");
  	$(param).parents('.layer_bg').hide();
}
	// 根据资讯类型iD,查询资讯,在相关咨询下方显示
	function setproductdiv(){// 确定相关资讯窗口关闭
		var $ids=$("#productdiv input[type='checkbox']:checked");
		if($ids.length==0){
			$('.layer_bg05').hide();
			return false;
		}
		var ids=new Array();
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		$.ajax({
			type:"POST",
			url:adminPath+"mymessage/findMessagetypeById.do",
			data:{id:ids},
            traditional :true,
			success:function(data){
				htmlStr="";
				$.each(data,function(index,obj){
					htmlStr+="<tr>";
					htmlStr+="<td width='50%' >"+obj.message_title+"</td>";
					htmlStr+="<td>";
					htmlStr+=eachType(obj.messageTypeList,"");
					htmlStr+="</td>";
					htmlStr+="<td><a href='javascript:;' onclick='remove(this)'>移除</a></td></tr>";
					  htmlStr+="<input type='hidden' name='ids'  value='"+obj.id+"'/>";
				});
		$("#relationBody").html(htmlStr);
			}
		});
		
	 	$('.layer_bg05').hide();
	}
	//移除功能
	function remove(param){
		$(param).parent().parent().remove();
	}
	function eachType(list,htmlStr){
		htmlStr="";
		$.each(list,function(index,obj){
			if(list.length-1==index){
			    htmlStr+=obj.type_name+"";
			}else{
				htmlStr+=obj.type_name+",";
			}
		});
		return htmlStr;
	}
	function templateModalHide(){//确定模板窗口关闭
		var $ids=$("#templatediv input[type='radio']:checked");
		if($ids.length==0){
			 $('.layer_bg02').hide();
			return false;
		   } 
			var html="";
			$.each($ids,function(index,obj){
				if(index==$ids.length-1){
					html+=$(obj).attr("att")+"";
				}else{
					html+=$(obj).attr("att")+",";
				}
			});
			$("#templateSpan").html(html);
		$('.layer_bg02').hide();
		
	}
	function typemodalHide(){//确定分类窗口关闭
		var $ids=$("#typediv input[type='checkbox']:checked");
		 if($ids.length==0){
			 window.top.mesageTip("warn","请选择一个资讯类型");
			return false;
		   } 
			var html="";
			$.each($ids,function(index,obj){
				if(index==$ids.length-1){
					html+=$(obj).attr("att")+"";
				}else{
					html+=$(obj).attr("att")+",";
				}
			});
			$("#typeSpan").html(html);
		$('.layer_bg08').hide();
	}
	
	function modalHide(selectorDiv,selectorBody,selectorModal){//确定了类型模板窗口关闭
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
			url:adminPath+"mymessageType/getMessageTypeTree.do",
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
/* ======================================= */
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
			This.find('dl').stop().slideDown()
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

/*
//获取模板类型树形结构
function settemplate(){
	$.ajax({
		type: "GET",
		url:adminPath+"mymessage/getTree.do",
		data:{type:2},
		dataType:'json',
		cache: false,
		success: function(result){		
			appendtemplatediv(result.templateList);
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
	 var defTemplate=$("#message_template").val();
	 $.each(list,function(index,obj){
		 if(obj.ID==defTemplate){
			 html+='<option selected="selected" value="'+obj.ID+'">'+obj.TEM_NAME+'</option>';
		 }else{
	     	html+='<option value="'+obj.ID+'">'+obj.TEM_NAME+'</option>';
		 }
	 });
	 //$("#templatediv").html(html);
	 $("#u1813_input").html(html);
}*/

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

//获取资讯类型树形结构
function setTypediv(param){
	$.ajax({
		type: "GET",
		url:adminPath+"mymessageType/getMessageTypeTree.do",
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

//选择资讯类型弹出内容填充
function appendTypediv(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的资讯分类，请添加分类后，再操作</div>';
		 $("#typediv").html(html);
		 return false;
	 }
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
		 $("#typeBody input").each(function(){
			 if(this.value==obj.id){
			    is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input checked="checked" type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i class="active"></i></label></p>';
			 }
		 });
		 	html+=is_checked;
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#typediv").html(html);
	 $(":input[name='messagetypeids']:checked").each(function(){
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
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
		 $("#typeBody input").each(function(){
			 if(this.value==obj.id){
			    is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.download_id+'"><input checked="checked" type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.download_id+'" /><span>'+obj.type_name+'</span><i class="active"></i></label></p>';
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

/* function save(type){
	if(!$("#Form").valid()){
		window.top.mesageTip("warn","请输入合法的链接!");
		return false;
	}
	$("#TXT").val(getContent());
	//资讯名称
	var name=$("#name").val();
	if(name.length==0){
		window.top.mesageTip("warn","请填写资讯名称!");
		return false;
	}
	var formData = new FormData($("#Form")[0]);
	$.ajax({
		type: "POST",
		url:adminPath+"mymessage/update.do",
		data:formData,
		dataType:'json',
		cache: false,
        processData: false,
        contentType: false,
		success: function(result){
			 if(result.success){
				 if(type=='1'){//继续添加
					 window.top.mesageTip("success","修改成功");
					 location.href=adminPath+'mymessage/toAdd.do';
				 }else{
					 window.top.mesageTip("success","修改成功");
					 location.href=adminPath+'mymessage/list.do';
				 }
			 }else{
				 window.top.mesageTip("failure","修改失败");
			 }
		}
	});
} */
function save(type){
	if(!$("#Form").valid()){
		window.top.mesageTip("warn","请输入合法的链接!");
		return false;
	}
	//资讯名称
	var name=$("#name").val();
	if(name.length==0){
		window.top.mesageTip("warn","请填写资讯名称!");
		return false;
	}
	//所属分类
	var $num=$("#typediv input[type='checkbox']:checked");
	//var type_id=$("#typediv input[type='checkbox']:checked").attr('value');
	/* if($num.length==0){
		window.top.mesageTip("warn","请选择所属分类!");
		return false;
	}  */
	ctids=[];
	$.each($num,function(){
		ctids.push($(this).attr("columtype"));
	});
	/* $("#type_id").val(type_id); */
	$("#columtype").val(ctids);
	$("#TXT").val(getContent());
	$("#is_add").val(type);
	$("#Form").submit();
}	
//获取上传图片路径
function setImagePreview(id) {
    var docObj = document.getElementById('file_name_img');
    //var imgObjPreview = document.getElementById("preview"+id);
    var imgObjPreview = document.getElementById("preview");
	if(id!=undefined){
		//docObj= document.getElementById(id);
		docObj= document.getElementById("file_name_img_"+id);
		imgObjPreview = document.getElementById("previewfile_name_img_"+id);
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
/* 字段类型处理开始 */
function popupHidens(param,selectordiv){
	$(param).parents('.layer_bg').find("input").removeAttr("checked");
	$(param).parents('.layer_bg li babel').removeClass('radio_btn');
	$(param).parents('.layer_bg').find('i').removeClass('active');
		$(selectordiv).each(function(index,obj){
			$("#myDL input[name='value_Type']").each(function(i,n){
				if(obj.value==n.value){
					$(obj).attr("checked",true);
					$(obj).parents('label').find('i').addClass('active');
				}
			})
		});
	  	$(param).parents('.layer_bg').hide();
}
function propertyModalHide(){//确定字段类型窗口关闭
	var $id=$("#propertydiv input[type='checkbox']:checked");
	if($id.length==0){
		$("#myDL").html("");
		$('.layer_bg04').hide();
		return false;
	}
	var htmlStr="";
	$.each($id,function(index,obj){
		htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
		if($(obj).attr("wordType")==3){
			htmlStr+="<dd><div class='dd_con'><input type='date' name='valueText' ><input type='text' name='sort' placeholder='请输入排序值' onblur='typeSort(this);' style='width:100px'/></div></dd></dl>";
		}else if($(obj).attr("wordType")=='2'){
			htmlStr+="<dl class='cf'><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
			htmlStr+="<dd><div class='dd_con'><ul><li><h6>";
			htmlStr+="<textarea class='textarea_numb' name='valueText'></textarea><input type='text' name='sort' placeholder='请输入排序值' onblur='typeSort(this);' style='width:100px'/>";
			htmlStr+="<p><span class='word'>0</span><span>/</span><span>200</span></p>";
			htmlStr+="</h6></li></ul></div></dd></dl>";

		}else{
			htmlStr+="<dd><div class='dd_con'><input type='text' name='valueText' ><input type='text' style='width:100px' name='unit'/><input type='text' name='sort' placeholder='请输入排序值' onblur='typeSort(this);' style='width:100px'/></div></dd></dl>";
		}
		htmlStr+="<input type='hidden' name='value_Type' value='"+obj.value+"'/>";
		htmlStr+="<input type='hidden' name='names' value='"+$(obj).attr("att")+"'/>";
		htmlStr+="<input type='hidden' name='wordTypes' value='"+$(obj).attr("wordType")+"'/>";
	})
	$("#myDL").html(htmlStr);
	$('.layer_bg04').hide();
}
function setExtendWord(){
	$.ajax({
		type: "GET",
		url:adminPath+"mymessage/getAllExtendWord.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			apendExtendWorddiv(result.extendWordsList);
		}
	});
}
function apendExtendWorddiv(list){
	var html='';
	if(list.length==0){
		html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		$("#propertydiv").html(html);
		return false;
	}
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" wordType="'+obj.fieldtype+'" att="'+obj.name+'" value="'+obj.id+'" id="cc_'+obj.id+'" /><span >'+obj.name+'</span><i></i></label></p>'; 
		 html+='</li>';
	 });
	 $("#propertydiv").html(html);
	 $('.layer_list_other li .show_btn').each(function(){
     	if($(this).parent('p').next('dl').size()==0){
     		$(this).hide();
     	}
     });
}
/*字段处理结束 */
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
//获取资讯列表
function setMessageType(){
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
	$.ajax({
		type: "GET",
		url:adminPath+"mymessageType/getMessageTypeTree.do",
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
						<i>修改资讯</i>
					</div>
				</div>
				
				<div class="cms_c_list cf">
					<h3>修改资讯</h3>				
					<form  name="Form" id="Form" action="<%=adminPath%>mymessage/update.do?columId=${pd.columId}&topColumId=${pd.topColumId}" method="post" enctype="multipart/form-data" >
						<input type="hidden" id="TXT" name="detail"/>
						<input type="hidden" id="is_add" name="is_add"/>
						<input type="hidden" id="columtype" name="columtype"/>
						<input type="hidden" id="hots" name="hot"/> 
						<input type="hidden" id="recommends" name="recommend"/>
						<input type="hidden"  name="id" value="${mess.id }"/>
						<!-- <input type="hidden"  id="type_id" name="type_id"/> -->
						<input type="hidden"  name="surface_image" value="${mess.surface_image}"/>
						<input type="hidden" id="columId" value="${pd.topColumId }"/>
						<div class="add_btn_con wrap cf">
						
						<!--<dl class="cf">
							<dt><span class="hot">*</span>所属栏目</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a><span id="columnSpan" class="warn_tip"></span></div></dd>
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
											<!--选择栏目内容展示区  -->
											<c:if test="${not empty columns}">
												<c:forEach items="${columns}" var="column" varStatus="count">
													<c:if test="${column.id == column.id}">
													<c:if test="${count.index ==0}">
													<tr><td width='50%' >${column.columName}</td><td><a href='javascript:;' onclick='removeBody(this,"#columndiv :checked")'>移除</a><input type='hidden'  value="${column.id}" /></td></tr>
													</c:if>
													</c:if>
													<c:if test="${column.id!=column.id}">
														<tr><td width='50%' >${column.columName}</td><td><a href='javascript:;' onclick='removeBody(this,"#columndiv :checked")'>移除</a><input type='hidden'  value="${column.id}" /></td></tr>
													</c:if>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
								</div>
							</div></dd>
						</dl>
						
						<dl class="cf">
							<dt><span class="hot"></span>所属分类</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn01">+选择</a><span class="warn_tip" id="typeSpan"></span></div></dd>
							<dd><div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">分类</td><td>操作</td></tr></thead>
										<tbody id="typeBody">
											<!--选择分类内容展示区  -->
											<c:if test="${!empty mess.messageTypeList}">
												<c:forEach items="${mess.messageTypeList}" var="messagetype" varStatus="tsta">
													<tr><td width='50%' >${messagetype.type_name}</td><td><a href='javascript:;' onclick='removeBody(this,"#typediv :checked")'>移除</a><input type='hidden'  value="${messagetype.id}" /></td></tr>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
									</div>
								</div></dd>
						</dl>
					
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot">*</span>资讯标题</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="message_title" value="${mess.message_title}"></div></dd>
								<dd class="zp_dd">
									<c:if test="${mess.link != '' && mess.link != null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i class="active quxiao"></i></label>
										<div class="dd_con" style="display: block;">
											<input type="text" style="width: 300px;" id="type_wburl" name="link" value="${mess.link}">
										</div>
									</c:if>
									<c:if test="${mess.link == '' || mess.link == null}">
										<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i class="quxiao"></i></label>
										<div class="dd_con">
											<input type="text" style="width: 300px;" id="type_wburl" name="link" value="${mess.link}">
										</div>
									</c:if>
								</dd>
								
							</dl>
						</div>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>副标题</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="type_name" value="${mess.type_name}"></div></dd>
							</dl>
						</div>
						<!-- <dl class="cf">
							<dt><span class="hot"></span>模板</dt>
							<dd>
								<div class="dd_con">
									<select id="u1813_input" name="message_template" class="form-control" style="width:200px;" >
							        </select>
								</div>
							</dd>
							<input type="hidden" id="message_template"  value="${mess.message_template}"/>
						</dl> -->
						<dl class="cf">
							<input type="hidden" name="message_template" id="message_template" value="${mess.message_template}"/>
							<dt>
								所属模板
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id=u1813_input class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>状态</dt>
							<dd>
								<div class="dd_con show_hide">
										<label for="show" class="active"><input checked="checked" type="radio" id="show" name="status" value="1" /><span>显示</span></label>
	
										<label for="hide"><input type="radio" id="hide" name="status" value="0" /><span>隐藏</span></label>
								</div>
							</dd>
						</dl>
						
						<dl class="cf" style="overflow:inherit;">
							<dt>封面图片</dt>
							<dd>
								<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label for="file_name_img" style="float:left;"><input type="file" id="file_name_img" onchange="javascript:setImagePreview();" name="file"><em>上传图片</em></label>
										<c:choose>
											<c:when test="${not empty image.imgurl}">
												<i style="float:left;">${image.title }</i>
												<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
												<div class="hid_div" style="height:0px;overflow:hidden;">
													<input type="hidden"  name="imgeId" value="${image.imageId }"/>
												</div>
												<div class="yulan"  style="float:left;position:relative;padding-left:10px;">预览
													<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;z-index:99;">
														<div class="pro_img_big"><img id="preview" alt="" src="<%=imgPath%>${image.imgurl}" width="150px" height="180px"></div>
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
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>作者</dt>
								<dd><div class="dd_con"><input type="text"  name="author" value="${mess.author}"></div></dd>
							</dl>
						</div>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>来源</dt>
								<dd><div class="dd_con"><input type="text"  name="resource" value="${mess.resource}" ></div></dd>
							</dl>
						</div>
						<dl class="cf">
							<dt>发布时间</dt>
							<fmt:formatDate value="${mess.release_time}" var="ymd" pattern="yyyy-MM-dd hh:mm:ss"/>
							<dd><div class="dd_con"><input id="release_time" style="width:185px;"  class="Wdate" type="text" value="${ymd}" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" name="releaseTime"/></div></dd>
						</dl>
						<dl class="cf">
							<dt>标记</dt>
							<dd>
								<div class="dd_con hot_re">
									<c:if test="${mess.hot==1 }">
										<label class="active" for="re" id = "ishotlable">
											<input type="checkbox" checked="checked" id="re"      value="1" class="ishot" /><span>热</span><i class="active"></i>
										</label>
									</c:if>
									<c:if test="${mess.hot!=1 }">
										<label for="re" id = "ishotlable"><input type="checkbox"    id="re"    value="0"/><span>热</span><i></i></label>
									</c:if>
									<c:if test="${mess.recommend==1 }">
										<label class="active" for="hot" id="isrecommendlable">
											<input type="checkbox" checked="checked" id="hot"    value="1"  class="isrecommend" /><span>荐</span><i></i>
										</label>
									</c:if>
									<c:if test="${mess.recommend!=1 }">
									<label for="hot" id="isrecommendlable">
										<input type="checkbox"   id="hot"   value="0" /><span>荐</span><i></i>
									</label>
									</c:if>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt><span class="hot"></span>属性管理</dt>
							<dd><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn04">+选择</a><span id="propertySpan" class="warn_tip"></span></div></dd>
						</dl>
						<!-- begin -->
						<!--字段-->
						<div class="cf"  id="myDL">
							<c:forEach items="${mess.pvtlist }" var="pvt">
								<dl><dt><span class='hot'></span>${pvt.name }</dt>
					        	<dd><div class='dd_con'>
					        		<c:if test="${pvt.fieldtype=='3' }">
					        			<input type='date' name='valueText' value="${pvt.value }">
					        			<input type='text' name='sort' placeholder='请输入排序值' value="${pvt.sort }" onblur='typeSort(this);' style='width:100px'/>
					        		</c:if>
					        		<c:if test="${pvt.fieldtype=='2' }">
											<ul><li><h6>
												<textarea name='valueText'>${pvt.value }</textarea>
												<input type='text' name='sort' placeholder='请输入排序值' value="${pvt.sort }" onblur='typeSort(this);' style='width:100px'/>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6></li></ul>
										</c:if>
					        		<c:if test="${pvt.fieldtype!='3' && pvt.fieldtype!='2'}">
					        			<input type='text' name='valueText' value="${pvt.value }">
					        			<input type='text' name='sort' placeholder='请输入排序值' value="${pvt.sort }" onblur='typeSort(this);' style='width:100px'/>
					        		</c:if>
					        	</div></dd></dl>
						        <input type='hidden' name='value_Type' value='${pvt.id }'/>
						        <input type='hidden' name='names' value='${pvt.name }'/>
						        <input type='hidden' name='wordTypes' value='${pvt.fieldtype}'/>
							</c:forEach>
						</div>
						<!-- end -->
						
						<dl class="cf">
							<dt>图片内容</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addimg">上传图片</a></div></dd>
						<c:forEach items="${pictlist}" var="i" varStatus="num">
							  	<dd style="overflow:inherit;">
								  	<div class="dd_con">
									  	<div class="add_img cf" id="upimages">
										  	<ul>
											  	<li class="file_upload file_upload_img" style="overflow:initial;height:36px;">
													<span style="float:left;">图片</span>
												  	<label style="float:left;" for="file_name_img_${num.count-1 }">
												  		<input type="file" onchange="javascript:setImagePreview('${num.count-1 }');" id="file_name_img_${num.count-1 }" name="images">
												  	<em>上传</em>
												  	</label>
												  	<i style="float:left;">${i.name }</i>
												  	<a href="javascript:;" class="remove_file" style="float:left;">删除</a>
												  	<div class="hid_div" style="height:0px;overflow:hidden;">
													  <input type="hidden"  name="imgeId" value="${i.imageId }">
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
												  	<p><span>排序</span><input type="text"  class="digits"  name="pictures[${num.count-1 }].forder" value="${i.forder }">  	
												  	</p>
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
							<dt>视频内容</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addfilm">上传视频</a></div></dd>
							<c:forEach items="${videlist}" var="v" varStatus="num">
									<dd>
									<div class="dd_con"><div class="add_img cf">
									<ul>
									<li class="file_upload file_upload_film" style="overflow:initial;height:36px;">
									<span style="float:left;">视频</span>
									<label style="float:left;" for="file_name_film_01${num.count-1 }">
									<input type="file" id="file_name_film_01${num.count-1 }" name="films">
									<em>上传</em>
									</label>
									<i style="float:left;">${v.name }</i>
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
									$(document).on('click','.remove_file',function(){
										$(this).next('.hid_div').remove();
									});
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
										  	img_list+='<label style="float:left;" for="file_name_img_9'+classNimg+'">';
										  	var fileId='file_name_img_9'+classNimg;
										  	img_list+='<input type="file" onchange="javascript:setImagePreview(\''+fileId+'\');" id="'+fileId+'" name="addimages">';
										  	img_list+='<em>上传</em>';
										  	img_list+='</label><i style="float:left;">未选择文件</i>';
										  	img_list+='<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>';
										  	img_list+='<div class="yulan" style="display:none;position:relative;padding-left:10px;float:left;">预览';
										  	img_list+='<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">';
										  	img_list+='<div class="pro_img_big">';
										  	img_list+='<img id="previewfile_name_img_'+fileId+'" alt="" src="" width="150px" height="180px">';
										  	img_list+='</div></div></div>';
										  	img_list+='</li><li><p><span>标题</span><input type="text" name="addpictures['+num+'].title"></p>';
										  	img_list+='<p><span>链接</span><input type="text" class="url" name="addpictures['+num+'].imgurl"></p>';
										  	
										  	img_list+='</li><li><p><span>排序</span><input type="text" class="digits" name="addpictures['+num+'].forder"></p>';
										  	
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
									  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film_'+classNfilm+'"><input type="file" id="file_name_film_'+classNfilm+'" name="addfilms"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text" name="addvideos['+number+'].video_title"></p><p><span>链接</span><input type="text" class="url" name="addvideos['+number+'].tourl"></p></li><li><p><span>排序值</span><input type="text" class="digits" name="addvideos['+number+'].forder"></p></li><li><span>描述</span><h6><textarea class="textarea_numb" name="addvideos['+number+'].video_content"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
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
						<dl class="cf">
							<dt><span class="hot"></span>详情描述</dt>
							<dd style="z-index:1;position:relative;">
								<div class="dd_con" style="z-index:1;">
									<div class="eidt_box cf" style="z-index:1;">
										<table id="table_report" class="table table-striped table-bordered table-hover" style="z-index:1;">
											<tr>
												<td style="z-index:1;">
												  <script id="editor" type="text/plain" style="width:90%;height:240px;z-index:1;">${mess.detail}</script>
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
												<textarea class="textarea_numb" name="description">${mess.description}</textarea>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>seo标题</dt>
							<dd><div class="dd_con"><input type="text" name="seo_title" value="${seo.SEO_TITLE }" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>seo概要描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<input type="hidden" name="SEO_ID" value="${seo.ID }"/>
												<textarea class="textarea_numb" name="seodescription">${seo.SEO_DESCRIPTION }</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>seo关键字</dt>
							<dd><div class="dd_con"><input type="text" name="seokeywords" value="${seo.SEO_KEYWORDS }"/></div></dd>
						</dl>
					
					
					<dl class="cf">
							<dt>相关资讯</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn05">+选择</a></div></dd>
							<dd><div class="dd_con">
								<div class="show_list">
								<table class="ev">
									<thead><tr><td width="50%">资讯名称</td><td>资讯类型</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作</td></tr></thead>
									<tbody  id="relationBody">
										<c:forEach items="${relevantList }" var="relevan">
										     <tr><td width="50%">${relevan.MESSAGE_TITLE }</td><td>${relevan.TYPE_NAME }</td><td><a onclick="remove(this)">移除</a></td></tr>
										      <input type="hidden" name="ids" value="${relevan.ID }"/>
									    </c:forEach>
									</tbody>
								</table>
								</div>
							</div></dd>
						</dl>
						
					</div>
					<div class="all_btn cf">
					<span id="result"></span>
						<input type="button" class="submit_btn" value="修改" onclick="save('0')"/>
						<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}" class="submit_re_btn">取消</a>
					</div>
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close" onclick="popupHiden(this,'#columndiv input','#columnBody input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="columndiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的选择栏目，请添加选择栏目后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg02');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#columndiv input','#columnBody input')">取消</a>
								</div>
							
						</div>
					</div>
					
					<div class="layer_bg layer_bg04" style="display:none;">
						<div class="layer_con cf">
							<h3><span>字段类型</span><p class="close" onclick="popupHidens(this,'#propertydiv input')">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="propertydiv" class="radio_lock">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的字段类型，请添加字段类型后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
								<input type="button" onclick="propertyModalHide();" class="submit_btn" value="确定" />
								<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHidens(this,'#propertydiv input')">取消</a>
							</div>
						</div>
					</div>	
					
					<div class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择分类</span><p class="close" onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p></h3>
								<div class="layer_list_other mScrol222 cf" >
									<ul id="typediv" class="radio_lock">
										
												<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的资讯分类，请添加分类后，再操作</div>
							
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="modalHide('#typediv :checked','#typeBody','.layer_bg08');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#typediv input','#relationBody input')">取消</a>
								</div>
						</div>
					</div>

					<div class="layer_bg layer_bg05" style="display:none;">
						<div class="layer_con cf">
							<h3><span>相关资讯</span><p class="close">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="productdiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的资讯分类，请添加分类后，再操作123</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="setproductdiv();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="confirm_re_btn">取消</a>
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
<script type="text/javascript">
function typeSort(param){
	var sort=$(param).val();
	if(sort==null || sort==undefined || sort=="") return false;
	var zz=/^[1-9][0-9]*$/;
	if(!zz.test(sort)){
		window.top.mesageTip("warn","请输入正整数");
		var file = $(param);
		file.after(file.clone().val("")); 
		file.remove();
	}
}

$('.quxiao').click(function(){
	$('#type_wburl').val("");
});
</script>
</html>