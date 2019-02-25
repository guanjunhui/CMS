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

/* function save(type){
	if(!$("#Form").valid()){
		return false;
	}
	$("#TXT").val(getContent());
    var formData = new FormData($("#Form")[0]);
	    formData.append("username","111");//不知道IE下为什么，不添加条空数据，报IO错误,谁要是能解决，请给我发邮件，告知qichangxin@300.cn
		 $.ajax({
			type: "POST",
			url:adminPath+"mymessageType/edit.do",
			data:formData,
			dataType:'json',
			cache: false,
	        processData: false,
	        contentType: false,
			success: function(result){
				 if(result.success){
					 if(type=='1'){//继续添加
						 $("#result").text("修改成功");
						 location.href=adminPath+'mymessageType/toAdd.do';
					 }else{
						 $("#result").text("修改成功");
						 location.href=adminPath+'mymessageType/list.do';
					 }
				 }else{
					 $("#result").text("修改失败,请重试");
				 }
			}
		}); 
	} */
	
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
					<div class="cms_con cf">
					<div class="cms_c_inst neirong cf">
					<div class="left cf">
						<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
						<i>></i>
						<a href="<%=adminPath%>/customformdata/list.do?formId=${pd.formId}">表单列表</a>
						<i>></i>
						<i>编辑</i>
					</div>
				</div>	
				<div class="cms_c_list cf">
					<h3>编辑表单数据</h3>
					
					<form  name="Form" id="Form" action="<%=adminPath%>customformdata/Edit.do" method="post" enctype="multipart/form-data" >
					<%-- <input type="hidden" id="typeId"  name="Pid" value="${productType.pid }"/>
					<input type="hidden"  id="mid" name="id" value="${productType.id }"/>
					<input type="hidden"  id="is_add" name="is_add" />
					<input type="hidden" id="TXT" name="type_detail"/> --%>
					<input type="hidden" id="formId" name="formId" value="${pd.formId}" />
						<div class="add_btn_con wrap cf">
							<c:if test="${not empty pd.customFormVo}">
								<c:if test="${not empty pd.customFormVo.recordData}">
									<c:forEach items="${pd.customFormVo.recordHead}" var="objhead" varStatus="headvs">
										<c:forEach items="${pd.customFormVo.recordData[0]}" var="objdata" varStatus="datavs">
											<c:if test="${headvs.index == datavs.index}">
												<dl class="cf">
													<dt>${objhead.name}</dt>
													<input type="hidden" name="formValueIdS" value="${objdata.id}"/>
													<dd><div class="dd_con"><input type="text" name="formValueNameS" value="${objdata.name}"/></div></dd>
												</dl>
											</c:if>
										</c:forEach>
									</c:forEach>
								</c:if>
							</c:if>
						</div>
					<div class="all_btn cf">
					<span id="result"></span>
						<input type="button" class="submit_btn" value="修改" onclick="save('0')"/>
						<a href="<%=adminPath%>customformdata/list.do?formId=${pd.formId}" class="submit_re_btn">取消</a>
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
</html>