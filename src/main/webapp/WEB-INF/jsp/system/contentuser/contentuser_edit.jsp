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
	String wechatPath = "/uploadFiles/wechat/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<script type="text/javascript" charset="utf-8" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select{height:80px;width:140px;overflow-y:auto;}
.div_select>.label{display:block;}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%> 
<script type="text/javascript">
	$(function(){
		top.hangge();
		/* 搜索  */
		$("#ss").click(function(){
			var content = $("#key").val();
			if(content==null){
				return;
			}
			
			$("#Form").submit();
		});
		
		$(".right").click(function(){
			var id = $(this).data('id');
			window.location.href=adminPath+"pmmComment/list.do?resouceId="+id;
		});
	});
	
	//根据id查看详情
	function detailById(id){
		window.location.href=adminPath+"pmmComment/list.do?resouceId="+id;
	}
</script>
<style>
.tit4{cursor:pointer;}
.tit4:hover a{color:#f00;}
.dd_slt_img span{display:inline-block;width:15.66%;float:left;margin-left:1%;}
.dd_slt_img span img{width:100%;cursor:pointer;}
.big_imggg{display:none;cursor:pointer;position:fixed;top:0px;bottom:0px;left:0px;right:0px;background:rgba(0,0,0,0.7)!important;z-index:99999;}
.big_imggg span{width:80%;display:block;margin:50px auto;height:90%;overflow:auto;}
.big_imggg img{width:100%;}
.col_03 .sele03 {
    float: left;
    text-align: center;
    width: 10%;
    word-wrap: break-word;
    line-height: 1.4;
    padding: 5px 0;
}

</style>
<script>
$(function(){
	$('.layer_btn01').click(function(){ //所属分类窗口打开
	  	$('.layer_bg08').show();
   });
	$('.layer_btn02').click(function(){ //所属分类窗口打开
	  	$('.layer_bg09').show();
   });
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
	//根据管理员ID和解决方案ID插入关联表建立关联关系
	function modalHide(selectorDiv,selectorBody,selectorModal){//确定模板窗口关闭
		var ids = $("input[type=checkbox]:checked");
		var checkIds = new Array();
		$.each(ids,function(index,obj){
			checkIds.push(obj.value);
		});
		if(checkIds.join(",")==null || checkIds.join(",")==""){
			return false;
		}
		var ajaxUrl =adminPath+"ContentUser/saveCommentIdByUserId";
		$.ajax({
			url:ajaxUrl,
			data:{
				"checkIds":checkIds.join(","),
				"userId":"${userId}"
			},
			type:'POST',
			success:function(data){
			}
		});
		window.location.reload();
	}
	//根据关联ID删除表中数据
	function deleteRelations(){
		var ids = $("input[type=checkbox]:checked");
		var checkIds = new Array();
		$.each(ids,function(index,obj){
			checkIds.push(obj.value);
		});
		if(checkIds.join(",")==null || checkIds.join(",")==""){
			window.top.mesageTip("warn","请先选择要删除的选项!");
			return false;
		}
		var ajaxUrl = adminPath+"ContentUser/deleteRelation";
		$.ajax({
			url:ajaxUrl,
			data:{
				"RELATIONID":checkIds.join(","),
				"userId":"${userId}"
			},
			type:'POST',
			success:function(data){
			}
		})
		window.location.reload();
	}
	
	function reSendMail(){
		var release_time = $("#release_time").val();
		if(release_time==null || release_time==""){
			alert("请选择重发日期!");
			return false;
		}
		var ids = $("input[type=checkbox]:checked");
		var checkIds = new Array();
		$.each(ids,function(index,obj){
			checkIds.push(obj.getAttribute('messageID'));
		});
		if(checkIds.join(",")==null || checkIds.join(",")==""){
			return false;
		}
		var ajaxUrl = adminPath+"ContentUser/reSendMail";
		$.ajax({
			url:ajaxUrl,
			data:{
				"RESOURCEID":checkIds.join(","),
				"USERID":"${userId}",
				"DATE":release_time
			},
			type:'POST',
			success:function(data){
			}
		})
		window.location.reload();
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
						<i>方案列表</i>
					</div>
				</div>
				<div class="add_btn_con wrap cf">  
					<dl class="cf">
						<dt>
							<span class="hot"></span>未关联方案
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn01">+选择</a>
								<span id="typeSpan" class="warn_tip"></span>
							</div>
						</dd>

					</dl>
				</div>
				<div class="add_btn_con wrap cf">  
					<dl class="cf">
						<dt>
							<span class="hot"></span>已关联方案
						</dt>
						<dd>
							<div class="dd_con">
								<a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a>
								<span id="typeSpan" class="warn_tip"></span>
							</div>
						</dd>

					</dl>
				</div>
				<div class="add_btn_con wrap cf">  
					<dl class="cf">
						<dt>
							<span class="hot"></span>重发时间
						</dt>
						<dd>
							<div class="dd_con">
								<input id="release_time" style="width:185px;" name="releaseTime" class="Wdate" type="text" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd'})" />
							</div>
						</dd>

					</dl>
				</div>
				<!-- 弹窗——未关联内容 -->
				<div class="layer_bg layer_bg08" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择方案</span>
							<p class="close">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="typediv1" class="radio_lock">
								<c:if test="${!empty comments}">
									<c:forEach items="${comments}" var="l" varStatus="mun">
										<li>
										<label for="cc_${l.ID}">
											<input type="checkbox" name="${l.CONTENT_TITLE}" value="${l.ID}" id="cc_${l.ID}">
											<span>${l.CONTENT_TITLE}</span>
											<i></i>
										</label>
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="modalHide('#typediv1 :checked','#typeBody','.layer_bg08');"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#typediv1 input','#typeBody input')">取消</a>
						</div>
					</div>
				</div>
				<!-- 弹窗——已关联内容 -->
				<div class="layer_bg layer_bg09" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>选择方案</span>
							<p class="close">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="typediv2" class="radio_lock">
								<c:if test="${!empty relationComment}">
									<c:forEach items="${relationComment}" var="l" varStatus="mun">
										<li>
											<label for="cc_${l.RELATIONID}">
												<input type="checkbox" name="${l.CONTENT_TITLE}" value="${l.RELATIONID}" id="cc_${l.RELATIONID}" messageID="${l.ID}">
												<span>${l.CONTENT_TITLE}</span>
												<i></i>
											</label>
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button"
								onclick="reSendMail();"
								class="submit_btn" value="重发" />
							<input type="button"
								onclick="deleteRelations();"
								class="submit_btn" value="删除" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHiden(this,'#typediv2 input','#typeBody input')">取消</a>
						</div>
					</div>
				</div>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->

</body>
</html>          