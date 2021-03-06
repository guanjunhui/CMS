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
		//显示图片
		$(document).on('mouseover','.yulan',function(){
			$(this).find(".pro_img").show();
		});
		$(document).on('mouseleave','.yulan',function(){
			$(this).find(".pro_img").hide();
		});
	})

//通过id删除数据
function deleteById(data){
	var resouceId = $("#resouceId").val();
	window.location.href=adminPath+"pmmComment/deleteComment.do?ids="+data+"&resouceId="+resouceId;
}
//当删除问题的时候提示是否删除
function confirmDelDiv(){
	var data = $("#rid").val();
	var	title='确认删除该选项吗?';
	var	content='此操作删除的数据不可恢复,请谨慎操作!';
	mesageConfirm('删除操作',title,content,"deleteById('"+data+"')");
}
	
//保存类型
function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	sub(type);
}
function sub(type){
	//$("#TXT").val(getContent());
	//$("#is_add").val(type);
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
</script>
<style>
.add_btn_con dl dt{width:160px;margin-right:-160px;}
.add_btn_con dl dd .dd_con{padding-left:170px;}

.submit_btn2 {
    display: block;
    background:  #2da1f8;
    border: 1px solid  #2da1f8;
    width: 85px;
    height: 37px;
    line-height: 35px;
    text-align: center;
    border-radius: 3px;
    color: #fff;
    float: left;
    margin-right: 10px;
}
</style>
</head>
<body>
	
	<!-- cms_con开始 -->
				<div class="cms_con cf">
					<div class="cms_con cf">
					<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
						<i>></i>
						<a href="<%=adminPath%>keyWords/list.do">关键词列表</a>
						<i>></i>
						<i>编辑</i>
					</div>
				</div>	
				<form action="<%=adminPath%>keyWords/editKeyWords.do" method="post">
				<div class="add_btn_con wrap cf">
						
					<div class="zhaopin zp_con01 cf">
					<dl class="cf">
						<dt>
							<span class='hot'>*</span>
							关键词
						</dt>
						<dd>
							<div class="dd_con">
								<input type="text" value="${key.keyWords}" name="keyWords">
							</div>		
						</dd>					
					</dl>
					<dl class="cf">
						<dt>
							<span class='hot'>*</span>
							是否启用
						</dt>
						<dd>
							<div class="dd_con" style="height: 35px;line-height: 35px;">
							
								<input type="radio" <c:if test="${key.status==1}">checked</c:if> value="1" name="status">是
								&nbsp;&nbsp;
								<input type="radio" <c:if test="${key.status==0}">checked</c:if> value="0" name="status">否
							</div>		
						</dd>
					</dl>
					
					<%-- <dl class="cf">
						<dt>
							<span class='hot'>*</span>
							创建时间
						</dt>
						<dd>
							<div class="dd_con">
								<input type="text" value="${key.createTime}" readonly>
							</div>		
						</dd>
					</dl> --%>
					<input type="hidden" name="id" value="${key.id }">
					</div>
					</div>
					<div class="all_btn cf">
						<span id="result"></span>
						<input type="submit" class="submit_btn" value="保存"/>
				</form>
						<input type="button" class="submit_btn2" value="取消"  onclick="javascript:history.back(-1);"/>
					</div>
				
				<div class="footer">© 中企高呈 版权所有</div>
			</div>

</body>
<script type="text/javascript">
	function cancel(){
		window.location.href = "<%=adminPath%>/contactPlan/list.do";
	}
</script>
</html>