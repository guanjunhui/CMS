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
		  	$('.layer_bg08').show();
	    });
		$("#bname").blur(function(){
			$.ajax({
				type: "POST",
				url:adminPath+"contentExtendFiledController/findCount.do",
				data:{NAME:$("#bname").val()},
				success:function(data){
					if(data.success){
						$("#nameSpan").html("");
					}else{
						$("#nameSpan").html("类型名称不能重复");
					}
				}
			});
		});
	});
	
	
//提示弹窗
function myMesageConfirm(a,b,c,_callback){
	html='<div class="layer_bg layer_bg04"><div class="layer_con cf xunwen"><h3><span>'+a+'</span><p class="close">x</p></h3><dl><dt><span class="ico bg-tishi03"></span></dt><dd><p>'+b+'</p><p>'+c+'</p></dd></dl><div class="all_btn cf"><input type="button" class="submit_btn" onclick="'+_callback+'"  value="确定" /><a href="javascript:;" class="submit_re_btn">取消</a></div></div></div>'
	$('body').append(html);
}
/* function save(type){
	if(!$("#Form").valid()){
		return false;
	} 
	if($("#nameSpan").val().length!=0){
		window.top.mesageTip("warn","名称重复");
	}
	var $type=$("#typediv input[type='radio']:checked");
	if($type.length==0){
	var	title='您确定不选上级品牌吗？';
	var	content='此操作会将此作为顶级品牌';
	myMesageConfirm('添加信息',title,content,"sub('"+type+"')");
	}else{
		sub(type);
	}
} */
function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	var formData=$("#Form").jsonObject();
		$.ajax({
			type: "POST",
			url:adminPath+"employExtendFiledController/save.do",
			data:formData,
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 window.top.mesageTip("success","操作成功");
						 location.href=adminPath+'cemployExtendFiledController/toAdd.do';
					 }else{
						 window.top.mesageTip("success","操作成功");
						 location.href=adminPath+'employExtendFiledController/list.do';
					 }
				 }else{
					 window.top.mesageTip("failure","操作失败");
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
				<i>></i> <a href="<%=adminPath%>contentExtendFiledController/list.do">招聘属性管理</a> <i>></i> <i>添加属性</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑属性</h3>

			<form id="Form" method="post">
				<div class="add_btn_con wrap cf">

					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>属性名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="bname" name="NAME" required><span
										id="nameSpan" class="warn_tip"></span>
								</div>
							</dd>
						</dl>
					</div>


					<dl class="cf">
						<dt>
							<span class="hot">*</span>属性类型
						</dt>
						<dd>
							<div class="dd_con">
								<select id="u1813_input" name="FIELDTYPE" class="form-control" style="width: 200px;">
									<option value="1">文本</option>
									<option value="2">长文本</option>
									<option value="3">日期</option>
									<option value="4">整数</option>
									<option value="5">小数</option>
								</select>
							</div>
						</dd>
					</dl>
				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" onclick="save('0')" /> <a href="javascript:void(0)"
						class="submit_a_btn" onclick="save('1')">保存并继续添加</a> <a
						href="<%=adminPath%>/employExtendFiledController/list.do" class="submit_re_btn">取消</a>
				</div>
				
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->


</body>
</html>