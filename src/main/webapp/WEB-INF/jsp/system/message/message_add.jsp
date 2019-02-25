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
<script type="text/javascript" charset="utf-8" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	/* //弹框取消按钮
	$(document).on('click','.submit_re_btn',function(){
	    $('.layer_list li label').removeClass('radio_btn');
	  	$('.layer_bg').find('input').removeAttr("checked");
	  	$('.layer_bg').find('i').removeClass('active');
	  	$(this).parents('.layer_bg').hide();
  	});
	 */
	$(function(){
		//模板类型模块
		settemplate();
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
		//显示图片
		$(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		});
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
			$.ajax({
				url:adminPath+"mymessage/findMessageBytypeId.do?"+param,
				type:"post",
				success:function(data){
					var html='';
					if(data.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;color:red;">对不起,没有相关数据!</div>';
						$("#productdiv").html(html);
						$('.layer_bg05').show();
						return false;
					}
					 $.each(data,function(index,obj){
						 html+='<li>';
						 	html+='<p><em class="show_btn"></em><label ><input type="checkbox" name="'+obj.message_title+'" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.message_title+'</span><i></i></label></p>';
						 
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
		  	$('.layer_bg05').show();
	    });	
	})
	
	function showImg(){
		$(".pro_img").toggle();
	}
function typeClose(param){
		var $ids=$("#typediv input[type='checkbox']:checked");
		$("#typediv li babel").removeClass('radio_btn');
		$("#typediv").find('i').removeClass('active');
		$.each($ids,function(index,obj){
			$(obj).removeAttr("checked");
		});
		$("#typeSpan").html("");
	  	$(param).parents('.layer_bg').hide();
}
function typeHiden(param){
		$("#typediv").find("input").removeAttr("checked");
		$("#typediv li babel").removeClass('radio_btn');
		$("#typediv").find('i').removeClass('active');
		$("#typeSpan").html("");
	  	$(param).parents('.layer_bg').hide();
}
function templateClose(param){
	$("#templatediv input[type='radio']:checked").removeAttr("checked");
	$("#templatediv li babel").removeClass('radio_btn');
	$("#templatediv").find('i').removeClass('active');
	$("#templateSpan").html("");
  	$(param).parents('.layer_bg').hide();
}
function templateHiden(param){
	$("#templatediv input[type='radio']:checked").removeAttr("checked");
	$("#templatediv li babel").removeClass('radio_btn');
	$("#templatediv").find('i').removeClass('active');
	$("#templateSpan").html("");
  	$(param).parents('.layer_bg').hide();
}
// 根据资讯类型iD,查询资讯,在相关咨询下方显示
function setproductdiv(){// 确定相关窗口关闭
		
		var $ids=$("#productdiv input[type='checkbox']:checked");
		if($ids.length==0){
			$('.layer_bg05').hide();
			return false;
		}
		var ids=[];
		var param="id=";
		var html="";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		//param+=ids.join("&id=");
		$.ajax({
			type:"post",
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
					htmlStr+="<td><a href='javascript:;' onclick='removes(this)'>移除</a></td></tr>";
					  htmlStr+="<input type='hidden' name='ids'  value='"+obj.id+"'/>";
				});
				$("#infoBody").html(htmlStr);
			}
		});
		
	 	$('.layer_bg05').hide();
}
	function removes(param){
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
				$("#relationBody").html('');
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
						modalHide('#typediv :checked','#relationBody','.layer_bg08');
					}
				}
			});
		}
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
	/* //栏目弹窗的确定方法
	function typemodalHide(){
		//点击确定键时,在下方显示出选中的栏目名称
		var $ids=$("#typediv input[type='checkbox']:checked");
		htmlStr="";
		$.each($ids,function(index,obj){
			htmlStr+="<tr><td width='20%'><i class='special'>"+$(obj).attr("text")+"</i></td><td><a href='javascript:void(0);' onclick='look(this);'>查看</a><a href='javascript:void(0);' onclick='removeBody(this,\"#typediv :checked\");'>移除</a><input type='hidden' name='ids'  value='"+obj.value+"'/></td></tr>";
		});
		$("#relationBody").html(htmlStr);
		//点击确定键时,将其窗口关闭
		$('.layer_bg08').hide();
	} */
	/* function typemodalHide(){//确定分类窗口关闭 
		//当选中复选框中的选项时,并在其后跟随选中的资讯类型名称
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
	} */
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
		 	html+='<p><em class="show_btn"></em><label ><span>'+obj.name+'</span></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachColumSubList('',obj.childList);
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
function eachColumSubList(html,list){
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
		 		html+=eachColumSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

/**
//获取模板类型树形结构
function settemplate(){
	$.ajax({
		type: "GET",
		url:adminPath+"template/getDefinedTree.do",
		data:{type:2,temType:2},
		dataType:'json',
		cache: false,
		success: function(result){
			appendtemplatediv(result.data);
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
	 //$("#templatediv").html(html);
	 $("#u1813_input").html(html);
	//控制折叠按钮是否显示
    // $('.layer_list_other li .show_btn').each(function(){
    //	console.log($(this).parent('p').next('dl').size());
    //	if($(this).parent('p').next('dl').size()==0){
    //		$(this).hide();
    //	}
    // });
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

function eachSubList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" att="'+obj.TEM_NAME+'" name="message_template" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.message_title+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
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
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
		 $("#typediv input[type='checkbox']:checked").each(function(){
			 if(this.value==obj.id){
			    is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input columtype="'+obj.id+"-"+obj.columId+'" checked="checked" type="checkbox" att="'+obj.type_name+'"  name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i class="active"></i></label></p>';
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
	//控制折叠按钮是否显示
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
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" columtype="'+obj.id+"-"+obj.columId+'" att="'+obj.type_name+'" name="messagetypeids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.type_name+'</span><i></i></label></p>';
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
		window.top.mesageTip("warn","请填写名字!");
		return false;
	}
	//所属分类
	var $num=$("#typediv input[type='checkbox']:checked");
	if($num.length==0){
		window.top.mesageTip("warn","请选择所属分类!");
		return false;
	}
	    var formData = new FormData($("#Form")[0]);
		$.ajax({
			type: "POST",
			url:adminPath+"mymessage/add.do",
			data:formData,
			dataType:'json',
			cache: false,
	        processData: false,
	        contentType: false,
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 window.top.mesageTip("success","添加成功");
						 location.href=adminPath+'mymessage/toAdd.do';
					 }else{
						 window.top.mesageTip("success","添加成功");
						 location.href=adminPath+'mymessage/list.do';
					 }
				 }else{
					 window.top.mesageTip("failure","添加失败");
				 }
			}
		});
} */
	
function save(type){
	if(!$("#Form").valid()){
		window.top.mesageTip("warn","请输入合法的链接!");
		return false;
	}
	var typeIds=$("#columnBody input");
	if(typeIds.length==0){
		window.top.mesageTip("warn","请选择栏目!");
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
	//var type_id =$("#typediv input[type='checkbox']:checked").attr('value');
	/* if($num.length==0){
		window.top.mesageTip("warn","请选择所属分类!");
		return false;
	} */
	ctids=[];
	$.each($num,function(){
		ctids.push($(this).attr("columtype"));
	});
	//$("#type_id").val(type_id);
	$("#columtype").val(ctids);
	$("#TXT").val(getContent());
	$("#is_add").val(type);
	$("#Form").submit();
		
}
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
		//url:adminPath+"mymessage/getAllExtendWord.do",
		url:adminPath+"messageExtendWord/getTree.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			apendExtendWorddiv(result.tree);
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
</script>
</head>
<body>
	
	<!-- cms_con开始 -->
			<div class="cms_con cf">
				<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i> 
						<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}">内容列表</a><i>></i> 
						<i>添加内容</i>
					</div>
				</div>
				<div class="cms_c_list cf">
					<h3>添加资讯</h3>
					
					<form  name="Form" id="Form" action="<%=adminPath%>mymessage/add.do?columId=${pd.columId}&topColumId=${pd.topColumId}" method="post" enctype="multipart/form-data" >
						<input type="hidden" id="TXT" name="detail"/>
						<input type="hidden" id="is_add" name="is_add"/>
						<!-- <input type="hidden" id="type_id" name="type_id"/> -->
						<input type="hidden" id="columtype" name="columtype"/>
						<input type="hidden" id="columId" value="${pd.columId}"/>
						<div class="add_btn_con wrap cf">
						
						<!-- <dl class="cf">
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
										<tbody id="relationBody">
											<!--选择分类内容展示区  -->
										</tbody>
									</table>
									</div>
								</div></dd>
						</dl>
						
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot">*</span>资讯标题</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="message_title" ></div></dd>
								<dd class="zp_dd">
									<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
									<div class="dd_con"><input type="text" style="width:300px;" id="type_wburl" name="link" ></div>
								</dd>
							</dl>
						</div>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>副标题</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="type_name" ></div></dd>
							</dl>
						</div>
						<!--<dl class="cf">
							<dt><span class="hot"></span>模板</dt>
							<dd>
								<div class="dd_con">
									<select id="u1813_input" name="message_template" class="form-control" style="width:200px;" >
							          
							        </select>
								</div>
							</dd>
						</dl>-->
						
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
									<li class="file_upload file_upload_img"><label for="file_name_img" style="float:left;" ><input type="file" onchange="setImagePreview('file_name_img')" id="file_name_img" name="file"><em>上传图片</em></label>
									<i style="float:left;">未选择文件</i>
										<a href="javascript:;" class="remove_file" style="display:none;float:left;">删除</a>
										<div class="yulan" style="display:none;float:left;position:relative;padding-left:10px;">预览
											<div class="pro_img" style="display:none; position:absolute;left:0px;top:20px;">
												<div class="pro_img_big"><img id="previewfile_name_img" alt="" width="150px" height="180px"></div>
											</div>
										</div>
								</ul>
								</div>
							</dd>
						</dl>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>作者</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="author" ></div></dd>
							</dl>
						</div>
						<div class="zhaopin zp_con01 cf">
							<dl class="cf zp_dl">
								<dt><span class="hot"></span>来源</dt>
								<dd><div class="dd_con"><input type="text" id="name" name="resource" ></div></dd>
							</dl>
						</div>
						<dl class="cf">
							<dt>发布时间</dt>
							<dd>
								<div class="dd_con">
									<input id="release_time" style="width:185px;" name="releaseTime" class="Wdate" type="text" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>标记</dt>
							<dd>
								<div class="dd_con hot_re">
									<label for="re"><input type="checkbox" id="re" name="hot" value="1"/><span>热</span><i></i></label>
									<label for="hot"><input type="checkbox" id="hot" name="recommend" value="1" /><span>荐</span><i></i></label>
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
							
						</div>
						<!-- end -->
						<dl class="cf">
							<dt>图片内容</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addimg">上传图片</a>&nbsp;&nbsp;<i style="color: gray;">点击可追加</i></div></dd>
							
						</dl>
						
						<dl class="cf">
							<dt>视频内容</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn_addfilm">上传视频</a>&nbsp;&nbsp;<i style="color: gray;">点击可追加&nbsp;-&nbsp;仅支持mp4后缀</i></div></dd>
							
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
									  	img_list+='</li><li><p><span>标题</span><input type="text" name="pictures['+num+'].title"></p>';
									  	img_list+='<p><span>链接</span><input type="text" class="url" name="pictures['+num+'].tourl"></p>';
									  	
									  	img_list+='</li><li><p><span>排序</span><input type="text" class="digits" name="pictures['+num+'].forder"></p>';
									  	
									  	img_list+='</li><li><span>描述</span><h6><textarea class="textarea_numb" name="pictures['+num+'].bz"></textarea>';
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
									  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film_'+classNfilm+'"><input type="file" id="file_name_film_'+classNfilm+'" name="films"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text" name="videos['+number+'].video_title"></p><p><span>链接</span><input type="text" class="url" name="videos['+number+'].tourl"></p></li><li><p><span>排序值</span><input type="text" class="digits" name="videos['+number+'].forder"></p></li><li><span>描述</span><h6><textarea class="textarea_numb" name="videos['+number+'].video_content"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
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
													  <script id="editor" type="text/plain" style="width:90%;height:240px;z-index:1;"></script>
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
												<textarea class="textarea_numb" name="description"></textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>seo标题</dt>
							<dd><div class="dd_con"><input type="text" name="seo_title" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>seo概要描述</dt>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea class="textarea_numb" name="seodescription"></textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt>seo关键字</dt>
							<dd><div class="dd_con"><input type="text" name="seokeywords" /></div></dd>
						</dl>
					
					<dl class="cf">
							<dt>相关资讯</dt>
							<dd><div class="dd_con"><a href="javascript:;" class="layer_btn layer_btn05">+选择</a></div></dd>
							<dd><div class="dd_con">
								<div class="show_list">
								<table class="ev">
									<thead><tr><td width="50%">资讯名称</td><td>资讯类型</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作</td></tr></thead>
									<tbody  id="infoBody">
									
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
					
					<div class="layer_bg layer_bg08" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择分类</span><p class="close" onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p></h3>
								<div class="layer_list_other mScrol222 cf" >
									<ul id="typediv" class="radio_lock">
										
												<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的资讯分类，请添加分类后，再操作</div>
										
									</ul>
								</div>
								<div class="all_btn cf">
									<input type="button" onclick="modalHide('#typediv :checked','#relationBody','.layer_bg08');" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#typediv input','#relationBody input')">取消</a>
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
									
					<!-- 弹窗——相关资讯 -->
					<div class="layer_bg layer_bg05" style="display:none;">
						<div class="layer_con cf">
							<h3><span>相关资讯</span><p class="close">x</p></h3>
							<div class="layer_list_other mScrol222 cf" >
								<ul id="productdiv">
									<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">无可选择的资讯分类，请添加分类后，再操作</div>
								</ul>
							</div>
							<div class="all_btn cf">
									<input type="button" onclick="setproductdiv();" class="submit_btn" value="确定" />
									<a href="javascript:void(0);"  class="submit_re_btn" onclick="popupHiden(this,'#typediv input','#relationBody input')">取消</a>
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
</script>
</html>
