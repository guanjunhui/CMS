<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript">
	$(function(){
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
		
		$('.layer_btn0b').click(function(){// 扩展字段窗口打开
		  	$('.layer_bg0b').show();
	    });
		$('.layer_btn03').click(function(){// 品牌窗口打开
		   	$('.layer_bg03').show();
	    }); 
		$('.layer_btn0p').click(function(){// 品牌窗口打开
			var $ids=$("#branddiv input[type='checkbox']:checked");
			if($ids.length==0){
				$("#ssbrandBody").html('');
				$('.layer_bg11').hide();
				return false;
			}
			var ids=[];
			var param="id=";
			var html='';
			$.each($ids,function(index,obj){
				ids.push(obj.value);
			});
			param+=ids.join("&id=");
			$.ajax({
				type:"post",
				url:adminPath+"product/findProductfByIds.do",
				data:param,
				success:function(data){
					if(data.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
						$("#ssbranddiv").html(html);
						$('.layer_bg011').show();
						return false;
					}
					 $.each(data,function(index,obj){
						 html+='<li>';
						 var is_checked='';
						 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" att="'+obj.NAME+'" name="fproductids"  value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span>'+obj.NAME+'</span><i></i></label></p>';
						 $("#ssbrandBody input").each(function(i,o){
							  if(o.value==obj.ID){
							 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" checked="checked" att="'+obj.NAME+'" name="fproductids"  value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span>'+obj.NAME+'</span><i class="active"></i></label></p>';
						 } 
					 });
						 html+=is_checked;
						 html+='</li>';
					 });
			$("#ssbranddiv").html(html);
				}
			});
			
		   	$('.layer_bg011').show();
	    });
		$('.layer_btn0a').click(function(){ // 栏目窗口打开
		  	$('.layer_bg0a').show();
	    });
		//显示图片
		$(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		});
		$('.layer_btn05').click(function(){// 相关产品窗口打开
			var $ids=$("#typediv input[type='checkbox']:checked");
			 if($ids.length==0){
				 window.top.mesageTip("warn","请在第二行选择一个所属分类");
				return;
			} 
			 var ids=[];
				var param="id=";
				$.each($ids,function(index,obj){
					ids.push($(obj).attr('typeid'));
				});
				param+=ids.join("&id=");
			$.ajax({
				url:adminPath+"product/findProductBytypeId.do?"+param,
				type:"post",
				success:function(data){
					var html='';
					if(data.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
						$("#productdiv").html(html);
						$('.layer_bg05').show();
						return false;
					}
					 $.each(data,function(index,obj){
						 html+='<li>';
						 var is_checked='';
						 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox"   value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
						 $("#relationBody input").each(function(i,n){
								 if(n.value==obj.id){
									  is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" checked="checked"  value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i class="active"></i></label></p>'; 
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
		  	$('.layer_bg05').show();
	    });
		//验证编号
		$("#no").blur(function(){
			var no=$("#no").val();
			if(no.length==0){
				return ;
			}
			var type=$("#typediv :checked");
			if(type.length==0){
				window.top.mesageTip("warn","请在第二行所属分类选择一个产品类型");
				return;
			}
			var typeids=[];
			var param="typeid=";
			$.each(type,function(index,obj){
				typeids.push(obj.value);
			});
			param+=typeids.join("&typeid=");
			param+="&no="+no;
			$.ajax({
				url:adminPath+"product/findNoCount.do",
				type:"post",
				data:param,
				success:function(result){
					if(result.success){
						if(result.count>0){
							$("#noSpan").html("商品编号不能重复");
						}else{
							$("#noSpan").html("");
						}
					}else{
						window.top.mesageTip("failure","商品编号验证失败");
					}
				}
			});
		});
		settemplate();
		setExtendDiv();
		setColumdiv();
	});
	//获取栏目类型树形结构
	function setColumdiv(){
		$.ajax({
			type: "GET",
			url:adminPath+"columconfig/getAssignTypeTree.do",
			data:{TEM_TYPE:3},
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
			 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><span>'+obj.name+'</span></label></p>';
			 	if(obj.childList!=null && obj.childList.length>0){
			 		html+=eachColumn('',obj.childList);
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

	function eachColumn(html,list){
		 var columId=$("#columId").val();
		 html+='<dl style="display:none;">';
		 $.each(list,function(index,obj){
			 html+='<dd>';
			 var ischecked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input att="'+obj.name+'" type="checkbox" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	 //自动定位栏目
		 	 if(columId==obj.id){
			 	ischecked='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input att="'+obj.name+'" type="checkbox" checked="checked" name="columnids" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
				var columhtml="<tr><td width='50%' >"+obj.name+"</td><td><a href='javascript:;' onclick='removeBody(this,\"#columndiv :checked\")'>移除</a><input type='hidden'  value="+obj.id+" /></td></tr>";
		 		$("#columnBody").append(columhtml);
		 		$("#columName").html(obj.name);
		 	 }
		 	html+=ischecked;
			 	if(obj.childList!=null && obj.childList.length>0){
			 		html+=eachColumn('',obj.childList);
			 	}
			 html+='</dd>';
		 });
		 html+='</dl>';
		return html;
	}
	function popupHidens(param,selectordiv){
		if(selectordiv=='#propertydiv input'){
			$(param).parents('.layer_bg').find("input").removeAttr("checked");
			$(param).parents('.layer_bg li babel').removeClass('radio_btn');
			$(param).parents('.layer_bg').find('i').removeClass('active');
				$(selectordiv).each(function(index,obj){
					$("#myDL input").each(function(i,n){
						if(obj.value==n.value){
							$(obj).attr("checked",true);
							$(obj).parents('label').find('i').addClass('active');
						}
					})
				});
			  	$(param).parents('.layer_bg').hide();
		}else{
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
					url:adminPath+"productType/getTreeByColumId.do",
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
// 根据产品类型iD,查询产品
function setproductdiv(){// 确定相关产品窗口关闭
		
		var $ids=$("#productdiv input[type='checkbox']:checked");
		if($ids.length==0){
			$("#relationBody").html('');
			$('.layer_bg05').hide();
			return false;
		}
		var ids=[];
		var param="id=";
		var html="";
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		param+=ids.join("&id=");
		$.ajax({
			type:"post",
			url:adminPath+"product/findProductTypeById.do",
			data:param,
			success:function(data){
				htmlStr="";
				$.each(data,function(index,obj){
					htmlStr+="<tr>";
					htmlStr+="<td width='50%' >"+obj.name+"</td>";
					htmlStr+="<td>";
					htmlStr+=eachType(obj.productTypeList,"");
					htmlStr+="</td>";
					htmlStr+="<td><a href='javascript:;' onclick='removes(this)'>移除</a><input type='hidden' name='ids'  value='"+obj.id+"'/></td></tr>";
					 
				});
		$("#relationBody").html(htmlStr);
			}
		});
		
	 	$('.layer_bg05').hide();
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
				url:adminPath+"productType/getTreeByColumId.do",
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
		if(selector=='#branddiv :checked'){
			var $bids=$("#branddiv input[type='checkbox']:checked");
			if($bids.length==0){
				$("#ssbrandBody").html('');
				$('.layer_bg11').hide();
				return false;
			}
			var bids=[];
			var bparam="id=";
			var html='';
			$.each($bids,function(index,obj){
				bids.push(obj.value);
			});
			bparam+=bids.join("&id=");
			$.ajax({
				type:"post",
				url:adminPath+"product/findProductfByIds.do",
				data:bparam,
				success:function(data){
					if(data.length==0){
						html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
						$("#ssbranddiv").html(html);
						$('.layer_bg011').show();
						return false;
					}
					 $.each(data,function(index,obj){
						 html+='<li>';
						 var is_checked='';
						 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" att="'+obj.NAME+'" name="fproductids"  value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span>'+obj.NAME+'</span><i></i></label></p>';
						 $("#ssbrandBody input").each(function(i,o){
							  if(o.value==obj.ID){
							 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="checkbox" checked="checked" att="'+obj.NAME+'" name="fproductids"  value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span>'+obj.NAME+'</span><i class="active"></i></label></p>';
						 } 
					 });
						 html+=is_checked;
						 html+='</li>';
					 });
			$("#ssbranddiv").html(html);
			modalHide('#ssbranddiv :checked','#ssbrandBody','.layer_bg011');ww
				}
			});
		} 
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
	
	function propertyModalHide(selectro){//确定产品属性窗口关闭
		if(selectro=='#myDL'){
			var $id=$("#propertydiv input[type='checkbox']:checked");
			if($id.length==0){
				$("#myDL").html("");
				$('.layer_bg04').hide();
				return false;
			}
					var htmlStr="";
					$.each($id,function(index,obj){
						htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
						htmlStr+="<dd><div class='dd_con'><input type='text' name='pvtList["+index+"].value' ></div></dd></dl>";
						htmlStr+="<input type='hidden' name='pvtList["+index+"].id' value='"+obj.value+"'/>";
						htmlStr+="<input type='hidden' name='pvtList["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					})
					$("#myDL").html(htmlStr);
				
			
			$('.layer_bg04').hide();
		}else{
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
	}


//获取栏目类型树形结构
function settemplate(){
	$.ajax({
		type: "GET",
		url:adminPath+"product/getTree.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			//appendtemplatediv(result.templateList);
			setTemplatediv();
			if('${flag}'=='0'){
				appendbranddiv(result.brandList);
			}else{
				appendssbranddiv(result.brandList);
			}
		}
	});
}

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
				 $("#product_TemplateId").val(data.id);
			 }
		}
	});
}
//获取属性数据
function setExtendDiv(){
	var columId=$("#columId").val();
	$.ajax({
		url:adminPath+'productExtendFiledController/getTree.do',
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
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input filedType="'+obj.FIELDTYPE+'" type="checkbox" att="'+obj.NAME+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.NAME+'</span><i></i></label></p>'; 
		 html+='</li>';
	 });
	 $("#extenddiv").html(html);
	 $('.layer_list_other li .show_btn').each(function(){
     	if($(this).parent('p').next('dl').size()==0){
     		$(this).hide();
     	}
     });
}
function eachProperList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" att="'+obj.name+'"  value="'+obj.id+'" id="cc_'+obj.id+'"/><span>'+obj.name+'</span><i></i></label></p>';
		 	 if(obj.childList!=null && obj.childList.length>0){
		 		 html+=eachProperList('',obj.childList);
		 	} 
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}	
function eachBrandList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.name+'" name="brandId"  value="'+obj.id+'" id="cc_'+obj.id+'"/><span>'+obj.name+'</span><i></i></label></p>';
		 	 if(obj.childList!=null && obj.childList.length>0){
		 		 html+=eachBrandList('',obj.childList);
		 	} 
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}
function eachssBrandList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="checkbox" att="'+obj.name+'" name="brandIds"  value="'+obj.id+'" id="cc_'+obj.id+'"/><span>'+obj.name+'</span><i></i></label></p>';
		 	 if(obj.childList!=null && obj.childList.length>0){
		 		 html+=eachssBrandList('',obj.childList);
		 	} 
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}
//选择栏品牌出内容填充
function appendbranddiv(list){
	 var html='';
		if(list.length==0){
			html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
			$("#branddiv").html(html);
			return false;
		}
		 $.each(list,function(index,obj){
			 html+='<li>';
			 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.name+'" name="brandId" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
			 	if(obj.childList!=null && obj.childList.length>0){
			 		 html+=eachBrandList('',obj.childList);
			 	}
			 html+='</li>';
		 });
		 $("#branddiv").html(html);
		 $('.layer_list_other li .show_btn').each(function(){
		     	if($(this).parent('p').next('dl').size()==0){
		     		$(this).hide();
		     	}
		     }); 
	}
	
function appendssbranddiv(list){
	 var html='';
		if(list.length==0){
			html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
			$("#branddiv").html(html);
			return false;
		}
		 $.each(list,function(index,obj){
			 html+='<li>';
			 	html+='<p><em class="show_btn"></em><label for="ccs_'+obj.id+'"><input type="checkbox" att="'+obj.name+'" name="brandIds" value="'+obj.id+'" id="ccs_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
			 html+='</li>';
		 });
		 $("#branddiv").html(html);
		 $('.layer_list_other li .show_btn').each(function(){
		     	if($(this).parent('p').next('dl').size()==0){
		     		$(this).hide();
		     	}
		     }); 
	}

//选择模板弹出内容填充
 function appendtemplatediv(list){
	var html='';
	
	if(list.length==0){
		html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		$("#templatediv").html(html);
		return false;
	}
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input type="radio" att="'+obj.TEM_NAME+'" name="product_TemplateId" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span>'+obj.TEM_NAME+'</span><i></i></label></p>';
		 	
		 html+='</li>';
	 });
	 $("#templatediv").html(html);
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
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" name="columnid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

//获取产品类型树形结构
function setTypediv(param){
	$.ajax({
		type: "POST",
		url:adminPath+"productType/getTreeByColumId.do",
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
		 var typeidAndColumnids='';
		 typeidAndColumnids+=obj.id+'-'+eachColumnids('',obj.columConfigList);
		 	var is_checked='<p><em class="show_btn "></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" type="checkbox"  att="'+obj.type_name+'" name="producttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.type_name+'</span><i></i></label></p>';
		 	$("#typediv input[type='checkbox']:checked").each(function(){
		 		if($(this).attr("typeid")==obj.id){
		 			is_checked='<p><em class="show_btn jia_j"></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" checked="checked" type="checkbox" att="'+obj.type_name+'" name="producttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.type_name+'</span><i class="active"></i></label></p>';
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

function eachList(html,list){
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
		 html+='<dd>';
		 var typeidAndColumnids='';
		 typeidAndColumnids+=obj.id+'-'+eachColumnids('',obj.columConfigList);
		 var is_checked='<p><em class="show_btn "></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" type="checkbox"  att="'+obj.type_name+'" name="producttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.type_name+'</span><i></i></label></p>';
		 	$("#typediv input[type='checkbox']:checked").each(function(){
		 		if($(this).attr("typeid")==obj.id){
		 			is_checked='<p><em class="show_btn "></em><label for="cc2_'+obj.id+'"><input typeid="'+obj.id+'" checked="checked" type="checkbox" att="'+obj.type_name+'" name="producttypeids" value="'+typeidAndColumnids+'" id="cc2_'+obj.id+'" /><span id="typeSpan">'+obj.type_name+'</span><i class="active"></i></label></p>';
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
function eachColumnids(html,list){
	var html='';
	$.each(list,function(i,n){
		if(list.length-1==i){
			html+=n.id+'';
		}else{
			html+=n.id+'-';
		}
	});
	return html;
}

function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	/* if($("#noSpan").val().length!=0){
		window.top.mesageTip("warn","产品编号不能重复");
		return false;
	} */
	/* var $ids=$("#typediv input[type='checkbox']:checked");
	 if($ids.length==0){
		 window.top.mesageTip("warn","请在第二行所属分类选择一个产品类型");
		return false;
	} */
	/*  var t=$("#templatediv :checked");
	 if(t.length==0){
		 window.top.mesageTip("warn","请选择模板");
		return false;
	} */
	<c:if test="${flag==1 }">
	var $ids=$("#columnBody input");
	
	if($ids.length==0){
		window.top.mesageTip("warn","请选择所属栏目");
		return false;
	} 
	</c:if>
	$("#TXT").val(getContent());
	$("#is_add").val(type);
	$("#Form").submit();
	
}

function setImagePreview(avalue) {
    var docObj = document.getElementById("file_name_img");
    var imgObjPreview = document.getElementById("preview");
    if(avalue!=undefined){
		docObj= document.getElementById(avalue);
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

	<!-- cms_con开始 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
				<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${columId}&topColumId=${topColumId}">内容列表</a><i>></i> 
				<i>添加产品</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑内容</h3>

			<form action="<%=adminPath%>product/add.do?columId=${columId}&topColumId=${topColumId}" id="Form" method="post"
				enctype="multipart/form-data">
				<input type="hidden"  name="flag" value="${flag }"/>
				<input type="hidden" id="TXT" name="productTxt" /> 
				<input type="hidden" id="is_add" name="is_add" />
				<input type="hidden" id="columId" value="${columId}"/>
				<div class="add_btn_con wrap cf">
					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>产品名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="name" name="name" required>
								</div>
							</dd>
							<dd class="zp_dd">
								<label for="wlink"><input id="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
								<div class="dd_con">
									<input type="text" style="width: 300px;" id="type_wburl"
										name="product_WbUrl" class="url">
								</div>
							</dd>
						</dl>
					</div>
					<c:if test="${flag==1 }">
					<!-- <dl class="cf">
						<dt>
							<span class="hot">*</span>所属栏目
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn0a">+选择</a><span
									id="columnSpan" class="warn_tip"></span>
							</div>
						</dd>
					</dl> -->
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
										</tbody>
									</table>
								</div>
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
									id="typeSpan" class="warn_tip"></span>
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
											<!--选择栏目内容展示区  -->
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>
					</c:if>

					<!-- <dl class="cf">
						<dt>
							<span class="hot"></span>产品编号
						</dt>
						<dd>
							<div class="dd_con">
								<input id="no" type="text" name="no" /><span id="noSpan"
									class="warn_tip"></span>
							</div>
						</dd>
					</dl> -->
					<dl class="cf" style="overflow: inherit;">
						<dt>封面图片</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label
										for="file_name_img" style="float: left;"> <input
											type="file" id="file_name_img"
											onchange="javascript:setImagePreview();" name="file"><em>上传图片</em></label>

										<i style="float: left;">未选择文件</i> <a href="javascript:;"
										class="remove_file" style="display: none; float: left;">删除</a>
										<div class="yulan"
											style="display: none; float: left; position: relative; padding-left: 10px;">
											预览
											<div class="pro_img"
												style="display: none; position: absolute; left: 0px; top: 20px;">
												<div class="pro_img_big">
													<img id="preview" alt="" width="150px" height="180px">
												</div>
											</div>
										</div></li>
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

					</dl>
					<c:if test="${flag==1 }">
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

					</dl>
					</c:if>
					<script>
						$(function(){						
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
							  	img_list+='</li><li><span>文字颜色</span><input type="text" name="timageList['+num+'].colorFlag" placeholder="背景图片文字颜色0：白色；1：黑色">';
							  	img_list+='</li><li><span>文字方向</span><input type="text" name="timageList['+num+'].dirFlag" placeholder="背景图片文字方向0：左边；1：右边">';
							  	
							  	img_list+='</li><li><p><span>排序</span><input type="text" class="digits" name="timageList['+num+'].forder"></p>';
							  	
							  	img_list+='</li><li><span>描述</span><h6><textarea class="textarea_numb" name="timageList['+num+'].bz"></textarea>';
							  	img_list+='<p><span class="word">0</span><span>/</span><span>200</span></p></h6>';
							  	img_list+='</li></ul><div class="close_add">x</div>';
							  	img_list+='</div></div>';
							  	img_list+='</dd>';
							  	num++;
							  	$(this).parents('dl').append(img_list);
							  	img_list='';
							  	}
							  });
							//添加视频
							  var number=0;
							  var classNfilm=0;
							  $(document).on('click','.layer_btn_addfilm',function(){
								  if($(this).parents('dd').next().size()>0 && $(this).parents('dl').find('dd:last-child').find('i').text()=='未选择文件'){
									  alert("请上传文件再点击上传") 
								  }else{
							  	classNfilm=classNfilm+1;
							  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film_'+classNfilm+'"><input type="file" id="file_name_film_'+classNfilm+'" name="sultipartFiles"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text" name="videoList['+number+'].video_title"></p><p><span>链接</span><input type="text" name="videoList['+number+'].tourl" class="url"></p></li><li><p><span>排序值</span><input type="text" class="digits" name="videoList['+number+'].forder"></p></li><li><span>描述</span><h6><textarea class="textarea_numb" name="videoList['+number+'].video_content"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
							  	number++;
							  	$(this).parents('dl').append(img_list);}
							  })
							  //关闭图片视频上传功能
							  $(document).on('click','.close_add',function(e){
							  	$(this).parents('dd').remove();
							  	e.stopPropagation();
							  });
						});
						</script>
						
					<dl class="cf">
						<dt>状态</dt>
						<dd>
							<div class="dd_con show_hide">

								<label for="show" class="active"><input
									checked="checked" type="radio" id="show" name="product_Status"
									value="1" /><span>显示</span></label> <label for="hide"><input
									type="radio" id="hide" name="product_Status" value="0" /><span>隐藏</span></label>

							</div>
						</dd>
					</dl>
					<!-- <dl class="cf">
						<dt>
							<span class="hot"></span>模板
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a><span
									id="templateSpan" class="warn_tip"></span>
							</div>
						</dd>
					</dl> -->
					<dl class="cf">
						<input type="hidden" name="product_TemplateId" id="product_TemplateId"/>
						<dt>
							<span class="hot"></span>所属模板
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" id="u1813_input" class="layer_btn"
									style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
								</a>
							</div>
						</dd>
					</dl>
					
					<!--<dl class="cf">
						<dt></dt>
						<dd>
							<div class="dd_con">
								<div class="show_list" style="margin-top: 0px;">
									<table class="ev">
										<thead>
											<tr>
												<td width="70%">模板</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="templateBody">
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>-->
					
					<c:if test="${flag==1 }">
					<dl class="cf">
						<dt>标记</dt>
						<dd>
							<div class="dd_con hot_re">
								<label for="hot"><input type="checkbox" id="hot"
									name="hot" value="1" /><span>热</span><i></i></label> <label for="re"><input
									type="checkbox" id="re" name="recommend" value="1" /><span>荐</span><i></i></label>
							</div>
						</dd>
					</dl>
					</c:if>
					<!-- <dl class="cf">
							<dt><span class="hot">*</span>所属模板</dt>
							<dd>
								<div class="dd_con">
									<select id="u1813_input" name="product_TemplateId" class="form-control" style="width:200px;" >
							          
							          
							        </select>
							      
								</div>
							</dd>
						
						</dl> -->
					<dl class="cf">
						<dt>
							<span class="hot"></span><c:if test="${flag==0 }">所属分组</c:if><c:if test="${flag==1 }">包含分组</c:if>
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn03">+选择</a><span
									id="brandSpan" class="warn_tip"></span>
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
												<td width="70%"></td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="brandBody">
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>
					<!-- end -->
					<!-- 包含分组 -->
					<c:if test="${flag==1 }">
					 <dl class="cf">
						<dt>
							<span class="hot"></span>副产品
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn0p">+选择</a><span
									id="brandSpan" class="warn_tip"></span>
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
												<td width="70%"></td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="ssbrandBody">
										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl> 
					</c:if>
					<!-- 包含分组 -->
					<c:if test="${flag==1 }">
					<dl class="cf">
						<dt>地域</dt>
						<dd><div class="dd_con"><input type="text" name="area" /></div></dd>
					</dl>
					</c:if>
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
					<div class="cf" id="extend"></div>
					
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
													style="width: 90%; height: 240px; z-index: 1;"></script></td>
										</tr>

									</table>
								</div>
							</div>
						</dd>
					</dl>
					<c:if test="${flag==1}">
					<dl class="cf">
						<dt>
							<span class="hot"></span>概要描述
						</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li>
										<h6>
											<textarea class="textarea_numb" name="product_Summary"></textarea>
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
						<dt>SEO标题</dt>
						<dd>
							<div class="dd_con">
								<input type="text" name="seo_title" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt>SEO关键字</dt>
						<dd>
							<div class="dd_con">
								<input type="text" name="product_KeyWords" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
							<dt>SEO描述</dt>
							<dd><div class="dd_con"><input type="text" name="seo_description" /></div></dd>
						</dl>

					<dl class="cf">
						<dt>相关产品</dt>
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
												<td width="50%">产品名称</td>
												<td>产品类型</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="relationBody">

										</tbody>
									</table>
								</div>
							</div>
						</dd>
					</dl>
					</c:if>
				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" onclick="save('0')" /> <a href="javascript:void(0)"
						class="submit_a_btn" onclick="save('1')">保存并继续添加</a> <a
						href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${columId}&topColumId=${topColumId}" class="submit_re_btn">取消</a>
				</div>
				<!-- 弹窗——相关内容 -->
				<div class="layer_bg layer_bg08" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择分类</span>
							<p class="close">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="typediv" class="radio_lock">

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
							<span>模板</span>
							<p class="close"
								onclick="popupHiden(this,'#templatediv input','#templateBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="templatediv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的模板，请添加模板后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#templatediv :checked','#templateBody','.layer_bg02');"
								class="submit_btn" value="确定" /> 
								<span class="submit_re_btn"
								onclick="popupHiden(this,'#templatediv input','#templateBody input')">取消</span>
						</div>

					</div>
				</div>

				<div class="layer_bg layer_bg05" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>相关产品</span>
							<p class="close" onclick="popupHiden(this)">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="productdiv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的相关产品，请添加相关产品后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button" onclick="setproductdiv();"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="confirm_re_btn">取消</a>
						</div>

					</div>
				</div>

				<div class="layer_bg layer_bg03" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<c:if test="${flag==0}"><span>所属分组</span></c:if>
							<c:if test="${flag==1}"><span>包含分组</span></c:if> 
							<p class="close"
								onclick="popupHiden(this,'#branddiv input','#brandBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="branddiv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的品牌，请添加品牌后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#branddiv :checked','#brandBody','.layer_bg03');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#branddiv input','#brandBody input')">取消</a>
						</div>

					</div>
				</div>
				
				 <div class="layer_bg layer_bg011" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>副产品</span>
							<p class="close"
								onclick="popupHiden(this,'#ssbranddiv input','#ssbrandBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="ssbranddiv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的品牌，请添加品牌后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#ssbranddiv :checked','#ssbrandBody','.layer_bg011');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#ssbranddiv input','#ssbrandBody input')">取消</a>
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
				
				<div class="layer_bg layer_bg0a" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择栏目</span>
							<p class="close"
								onclick="popupHiden(this,'#columndiv input','#columnBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="columndiv">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的栏目，请添加栏目后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#columndiv :checked','#columnBody','.layer_bg0a');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#columndiv input','#columnBody input')">取消</a>
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