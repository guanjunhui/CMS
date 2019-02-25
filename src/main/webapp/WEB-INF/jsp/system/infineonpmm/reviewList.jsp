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
		
		/* $(".right").click(function(){
			var id = $(this).data('id');
			window.location.href=adminPath+"keyWords/list.do?resouceId="+id;
		}); */
	});
	//当批量删除问题的时候提示是否删除
	function confirmDelDivs(){
		var $ids=$("#did input[type='checkbox']:checked");
		if($ids.length==0){
			window.top.mesageTip("warn","请先选择要删除的选项!");
			return false;
		}
		var	title='确认删除选中的选项吗?';
		var	content='此操作删除的数据不可恢复,请谨慎操作!';
		mesageConfirm('批量删除操作',title,content,"deleteChecks()");
	}
	//批量删除问题的功能
	function deleteChecks(){
		var $ids=$("#did input[type='checkbox']:checked");
		var ids=[];
		$.each($ids,function(index,obj){
			ids.push(obj.value);
		});
		
		window.location.href=adminPath+"keyWords/delete.do?ids="+ids.join(",");		
	}
	
	//根据id查看详情
	function detailById(id){
		window.location.href=adminPath+"keyWords/getKeyWords.do?id="+id;
	}
	
	//根据id删除
	function deleteKey(id){
		window.location.href=adminPath+"keyWords/delete.do?ids="+id;
	}
	
	//根据id修改
	function updateHandle(id,status){
		window.location.href=adminPath+"keyWords/editKeyWords.do?id="+id+"&status="+status;
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
.submit_btn {

    display: block;
    background: #ff9f20;
    border: 1px solid #ff9f20;
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
<script>
$(function(){
	$(document).on('click','.tit7',function(e){
		var show_dom=$(this).parents('dd').children('dl');
		if(show_dom.css('display')=='none'){
			show_dom.show();
		}else{
			show_dom.hide();
		}
		e.stopPropagation();
	});
	$(document).on('click','.dd_slt_img img',function(e){
		$('.big_imggg img').attr('src',$(this).attr('src'));
		$(this).parents('dd').find('.big_imggg').show();
		e.stopPropagation();
	})
	$(document).on('click','.big_imggg',function(){
		$(this).hide();
		$('.big_imggg img').attr('src','');
	});
})
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
				<form id="Form" action="<%=adminPath%>review/list.do" method="post">
				<div class="cms_c_list biaoge_con cf">
					<div class="h3 cf">
						<%-- <div class="h3_left cf"><a href="<%=adminPath%>keyWords/goAdd.do">+添加关键词</a></div>
						<div class="search">
							<input placeholder="请输入提问内容" name="key" type="text" id="key"><input id="ss" type="button"  value="搜索" />
						</div> --%>
					</div>
					<div class="table cf">

						<dl class="list_bg col_03 cf" id="did">
							<dt class="cf">
								<div class="sele03 "></div>
								<div class="sele03" style="width:25%">内容</div>
								<div class="sele03">包含关键词</div>
								<div class="sele03">状态</div>
								<div class="sele03">创建时间</div>
								<div class="sele03">操作</div>
							</dt>
							<c:if test="${!empty list}">
								<c:forEach items="${list}" var="l" varStatus="mun">
									<dd class="cf">
									<div class="dd_tit cf other_bg">
										<div class="sele03">
											<input type="checkbox" value="${l.id}">
										</div>
										<div class="sele03" style="width:25%">${l.content}</div>
										<div class="sele03">${l.keyWords}</div>
										<div class="sele03">
											<c:if test="${l.status==0 }">
												不允许发布
											</c:if>
											<c:if test="${l.status==1 }">
												允许发布
											</c:if>
										</div>
										<div class="sele03 ">${l.createTime}</div>
										<div class="sele03 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
												<ul class="guanli_con cf">
													<c:if test="${l.status==0 }"><li><a onclick="updateHandle('${l.id }',1)">开启</a></li></c:if>
													<c:if test="${l.status==1 }"><li><a onclick="updateHandle('${l.id }',0)">关闭</a></li></c:if>
													<li><a onclick="detailById('${l.id }')" href="javascript:;">编辑</a></li>
													<li><a onclick="deleteKey('${l.id }')" href="javascript:;">删除</a></li>
												</ul>
										</div>
									</div>
									</dd>
								</c:forEach>
							</c:if>
							
						</dl>
										
						<div class="bottom_con cf">
							<div class="all_checkbox">
								<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
								<a id="sc" href="javascript:;" onclick="confirmDelDivs();" >批量删除</a>
							</div>
							<div class="page_list cf">
								${page.pageStr}					
							</div>
						</div>
					</div>
					</div>
				</form>
				</div>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<!-- cms_con结束 -->

</body>
<script type="text/javascript">
$(function(){
	$("#c_10").click(function(){
		 if (this.checked) {
		   $(".dd_tit :checkbox").prop("checked", true);
		 } else {
		   $(".dd_tit :checkbox").prop("checked", false);
		 }
	});
	
});
</script>
</html>          