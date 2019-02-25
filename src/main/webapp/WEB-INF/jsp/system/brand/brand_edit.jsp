<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = "//" + request.getServerName()
			+ path + "/";
	String adminPath = (String) application.getAttribute("adminPath");
	String imgPath = path + "/../uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">

<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	$(function() {
		$('.layer_btn01').click(function() { //所属分类窗口打开
			$('.layer_bg08').show();
		});
		//显示图片
		$(document).on('mouseover', '.yulan', function() {
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave', '.yulan', function() {
			$(this).find(".pro_img").hide();
		});
		$(document).on('click','.remove_file',function(){
			$(this).next('.hid_div').remove();
		});
		setTypediv();
		$("#bname").blur(function() {
			$.ajax({
				type : "POST",
				url : adminPath + "productType/findBrandcount.do",
				data : {
					name : $("#bname").val(),
					id : $("#id").val()
				},
				success : function(data) {
					if (data.success) {
						$("#nameSpan").html("");
					} else {
						$("#nameSpan").html("类型名称不能重复");
					}
				}
			});
		});
	});
	function popupHiden(param, selectordiv, selectorbody) {
		$(param).parents('.layer_bg').find("input").removeAttr("checked");
		$(param).parents('.layer_bg li babel').removeClass('radio_btn');
		$(param).parents('.layer_bg').find('i').removeClass('active');
		$(selectordiv).each(function(index, obj) {
			$(selectorbody).each(function(i, n) {
				if (obj.value == n.value) {
					$(obj).attr("checked", true);
					$(obj).parents('label').find('i').addClass('active');
				}
			})
		});
		$(param).parents('.layer_bg').hide();
	}
	function modalHide(selectorDiv, selectorBody, selectorModal) {//确定模板窗口关闭
		var $ids = $(selectorDiv);
		if ($ids.length == 0) {
			$(selectorBody).html('');
			$(selectorModal).hide();
			return false;
		}
		var html = "";
		$
				.each(
						$ids,
						function(index, obj) {
							if (index == $ids.length - 1) {
								html += "<tr><td width='50%' >"
										+ $(obj).attr("att")
										+ "</td><td><a href='javascript:;' onclick='removeBody(this,\""
										+ selectorDiv
										+ "\")'>移除</a><input type='hidden'  value="+obj.value+" /></td></tr>";
							} else {
								html += "<tr><td width='50%' >"
										+ $(obj).attr("att")
										+ "</td><td><a href='javascript:;' onclick='removeBody(this,\""
										+ selectorDiv
										+ "\")'>移除</a><input type='hidden'  value="+obj.value+" /></td></tr>"
										+ ",";
							}
						});
		$(selectorBody).html(html);
		$(selectorModal).hide();
	}
	function removeBody(param, selector) {
		$(selector).each(function() {
			if (this.value == $(param).next().val()) {
				$(this).removeAttr("checked");
				$(this).parents('label').find('i').removeClass('active');
			}
		});
		$(param).parent().parent().remove();
	}

	//获取产品类型树形结构
	function setTypediv() {
		$.ajax({
			type : "GET",
			url : adminPath + "brand/getTree.do",
			data : {},
			dataType : 'json',
			cache : false,
			success : function(result) {
				if (result.success) {
					appendTypediv(result.tree);
				}
			}
		});
	}
	//选择产品类型弹出内容填充
	function appendTypediv(list) {
		var html = '';

		$
				.each(
						list,
						function(index, obj) {
							html += '<li>';
							var is_top = '<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类" name="pid" value="0"  /><span>作为顶级分类</span><i></i></label></p>';
							var is_checked = '<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'
									+ obj.name + '</span><i></i></label></p>';
							$("#typediv input[type='radio']:checked")
									.each(
											function() {
												if (this.value == obj.id) {
													is_checked = '<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input checked="checked" type="radio" att="'+obj.name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'
															+ obj.name
															+ '</span><i class="active"></i></label></p>';

												}
												if (this.value == 0
														&& index == 0) {
													is_top = '<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类" checked="checked" name="pid" value="0"  /><span>作为顶级分类</span><i class="active"></i></label></p>';
													html += is_top;
												} else if (this.value != 0
														&& index == 0) {
													is_top = '<p><em class="show_btn"></em><label for="cc_0"><input id=cc_0 type="radio" att="顶级分类"  name="pid" value="0"  /><span>作为顶级分类</span><i ></i></label></p>';
													html += is_top;
												}
											});
							html += is_checked;
							if (obj.childList != null
									&& obj.childList.length > 0) {
								html += eachList('', obj.childList);
							}
							html += '</li>';
						});
		$("#typediv").html(html);
		$("#typediv :checked").each(function() {
			var This = $(this).parents('li');
			This.find('dl').slideDown()
		});
		$('.layer_list_other li .show_btn').each(function() {
			if ($(this).parent('p').next('dl').size() == 0) {
				$(this).hide();
			}
		});
		modalHide('#typediv :checked', '#typeBody', '.layer_bg08');
	}
	function eachList(html, list) {
		html += '<dl style="display:none;">';
		$
				.each(
						list,
						function(index, obj) {
							html += '<dd>';
							var is_checked = '<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" att="'+obj.name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'
									+ obj.name + '</span><i></i></label></p>';
							$("#typediv input[type='radio']:checked")
									.each(
											function() {
												if (this.value == obj.id) {
													is_checked = '<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input checked="checked" type="radio" att="'+obj.name+'" name="pid" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'
															+ obj.name
															+ '</span><i class="active"></i></label></p>';
												}
											});
							html += is_checked;
							if (obj.childList != null
									&& obj.childList.length > 0) {
								html += eachList('', obj.childList);
							}
							html += '</dd>';
						});
		html += '</dl>';
		return html;
	}
	//提示弹窗
	function myMesageConfirm(a, b, c, _callback) {
		html = '<div class="layer_bg layer_bg04"><div class="layer_con cf xunwen"><h3><span>'
				+ a
				+ '</span><p class="close">x</p></h3><dl><dt><span class="ico bg-tishi03"></span></dt><dd><p>'
				+ b
				+ '</p><p>'
				+ c
				+ '</p></dd></dl><div class="all_btn cf"><input type="button" class="submit_btn" onclick="'+_callback+'"  value="确定" /><a href="javascript:;" class="submit_re_btn">取消</a></div></div></div>'
		$('body').append(html);
	}
	function save(type) {
		if (!$("#Form").valid()) {
			return false;
		}
		if ($("#nameSpan").text().length != 0) {
			window.top.mesageTip("warn", "名称重复");
			return false;
		}
		var $type = $("#typediv input[type='radio']:checked");
		if ($type.length == 0) {
			var title = '您确定不选上级分组吗？';
			var content = '此操作会将此作为顶级分组';
			myMesageConfirm('添加信息', title, content, "sub('" + type + "')");
		} else {
			sub(type);
		}
	}
	function sub(type) {
		var pid = $("#typediv input[type='radio']:checked").val();
		/* var name=$("#bname").val();
		var id=$("#id").val(); */
		var formData = new FormData($("#Form")[0]);
		$.ajax({
			type : "POST",
			url : adminPath + "brand/update.do",
			data : formData,
			cache : false,
			processData : false,
			contentType : false,
			success : function(result) {
				if (result.success) {
					if (type == '1') {//继续添加
						window.top.mesageTip("success", "操作成功");
						location.href = adminPath + 'brand/toAdd.do';
					} else {
						window.top.mesageTip("success", "操作成功");
						location.href = adminPath + 'brand/list.do';
					}
				} else {
					window.top.mesageTip("failure", "操作失败");
				}
			}
		});
	}
	function setImagePreview(avalue) {
		var docObj = document.getElementById("file_name_img");
		var imgObjPreview = document.getElementById("preview");
		if (docObj.files && docObj.files[0]) {
			//火狐下，直接设img属性
			//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
			imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
		} else {
			//IE下，使用滤镜
			docObj.select();
			var imgSrc = document.selection.createRange().text;
			var localImagId = document.getElementById("localImag"); //必须设置初始大小
			try {
				localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				localImagId.filters
						.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
			} catch (e) {
				return false;
			}
			imgObjPreview.style.display = 'none';
			document.selection.empty();
		}
		return true;
	}
</script>
</head>
<body>

	<!-- cms_con开始 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <a href="<%=adminPath%>brand/list.do">分组管理</a> <i>></i> <i>修改分组</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑内容</h3>

			<form id="Form" method="post" enctype="multipart/form-data">
				<div class="add_btn_con wrap cf">

					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>分组名称
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="bname" value="${brand.name }"
										name="name" required><span id="nameSpan"
										class="warn_tip"></span>
								</div>
							</dd>
							<input type="hidden" id="id" name="id" value="${brand.id }" />
						</dl>
					</div>

					<dl class="cf" style="overflow: inherit;">
						<dt>封面图片</dt>
						<dd>
							<div class="dd_con">
								<ul>
									<li class="file_upload file_upload_img"><label
										for="file_name_img" style="float: left;"> <input
											type="file" id="file_name_img"
											onchange="javascript:setImagePreview();" name="file"><em>上传图片</em>
									</label> <c:choose>
											<c:when test="${not empty brand.imageid}">
												<i style="float: left;">${brand.title }</i>
												<a href="javascript:;" class="remove_file"
													style="float: left;">删除</a>
												<div class="hid_div" style="height: 0px; overflow: hidden;">
													<input type="hidden" name="imageid"
														value="${brand.imageid }" />
												</div>
												<div class="yulan"
													style="float: left; position: relative; padding-left: 10px;">
													预览
													<div class="pro_img"
														style="display: none; position: absolute; left: 0px; top: 20px;">
														<div class="pro_img_big">
															<img id="preview" alt=""
																src="<%=imgPath%>${brand.imgurl}" width="150px"
																height="180px">
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
						<dt>
							<span class="hot">*</span>上级分组
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
				</div>
				<div class="all_btn cf">
					<span id="result"></span> <input type="button" class="submit_btn"
						value="保存" onclick="save('0')" /> <a href="javascript:void(0)"
						class="submit_a_btn" onclick="save('1')">保存并继续添加</a> <a
						href="<%=adminPath%>/brand/list.do" class="submit_re_btn">取消</a>
				</div>
				<!-- 弹窗——相关内容 -->
				<div class="layer_bg layer_bg08" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择上级分组</span>
							<p class="close"
								onclick="popupHiden(this,'#typediv input','#typeBody input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="typediv" class="radio_lock">
								<input type="radio" checked="checked" name="pid"
									value="${brand.pid}" />
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的上级分组，请添加分组后，再操作</div>

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
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->


</body>
</html>