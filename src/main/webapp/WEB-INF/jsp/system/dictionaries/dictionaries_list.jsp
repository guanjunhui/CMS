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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select{height:80px;width:140px;overflow-y:auto;}
.div_select>.label{display:block;}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
</head>
<body>
<!-- cms_con开始 -->
<div class="cms_con cf">

<div class="cms_c_inst neirong cf">
	<div class="left cf">
		<a href="javascript:void(top.location.href='<%=adminPath%>index.do')">首页</a><i>></i>
		<i>数据字典</i>
	</div>
</div>
<form  id="Form" action="<%=adminPath%>dictionaries/list.do" method="post">
	<div class="cms_c_list biaoge_con chanpin_con cf">
		<div class="h3 cf">
			<div class="h3_left cf">
				<%-- <a href="<%=adminPath%>product/toAdd.do">+添加</a> --%>
				<a href="<%=adminPath%>dictionaries/goAddRoot.do" >+添加根类目</a>
				<div class="selec_co cf">
					<select class="form-control" name="DICTIONARIES_ID" id="DICTIONARIES_ID">
						<option value="${DICTIONARIES_ID}" <c:if test="${DICTIONARIES_ID != ''}">selected</c:if>>本级</option>
						<option value="" <c:if test="${DICTIONARIES_ID == ''}">selected</c:if>>全部</option>
					</select>
				</div>
			</div>
			<div class="search">
				<input type="text" id="keywords" name="keywords"  value="${page.pd.keywords }"><input id="ss" type="button" onclick="gsearch();" value="搜索" />
			</div>
		</div>
		
							
		<div class="table cf">

			
			
				<dl class="list_bg col_06 cf" id="did">
				<dt class="cf">
					<div class="tit6">
						<div class="tit6_con">
							名称
						</div>
					</div>
					<div class="sele06 ">操作</div>
					<div class="sele06">排序</div>
					<div class="sele06">编码</div>
					<div class="sele06">序号</div>
					<div class="sele06 other_width">英文</div>
				</dt>
				
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="var" varStatus="vs">
							<dd class="cf">
								<div class="dd_tit cf">
									<div class="tit6">
										<div class="tit6_con">
											<a href="javascript:goSondict('${var.DICTIONARIES_ID }')"><i class="ace-icon fa fa-share bigger-100"></i>&nbsp;${var.NAME}</a>
										</div>
									</div>
									<div class="sele06 sanJi">
										<a href="javascript:;">管理<span class="sanjiao"></span></a>
										<ul class="guanli_con cf">
											<li><a href="javascript:;"  onclick="edit('${var.DICTIONARIES_ID}');">编辑</a></li>
											<li><a href="javascript:;" onclick="del('${var.NAME}','${var.DICTIONARIES_ID}');">删除</a></li>
											<li><a href="javascript:;"  onclick="add('${var.DICTIONARIES_ID}');">新增</a></li>
											<li><a href="javascript:;" onclick="goSondict('${pd.PARENT_ID}');">返回</a></li>
										</ul>
									</div>
									<div class="sele06 hide_font">${var.ORDER_BY}</div>
									<div class="sele06">${var.BIANMA}</div>
									<div class="sele06 ">${vs.index+1}</div>
									<div class="sele06 other_width"><a href="javascript:goSondict('${var.DICTIONARIES_ID }')">${var.NAME_EN}</a></div>
								</div>
							</dd>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div>
							没有相关数据
						</div>
					</c:otherwise>
				</c:choose>
				
							
			</dl>
				
			</form>
			<div class="bottom_con cf">
				<div class="page_list cf">
					${page.pageStr}						
				</div>
			</div>
			
			
<!-- cms_con结束 -->
<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());//关闭加载状态
		//检索
		function gsearch(){
			top.jzts();
			$("#Form").submit();
		}
		
		//去此ID下子列表
		function goSondict(DICTIONARIES_ID){
			top.jzts();
			window.location.href="<%=adminPath%>dictionaries/list.do?DICTIONARIES_ID="+DICTIONARIES_ID;
		};
		
		//新增
		function add(DICTIONARIES_ID){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=adminPath%>dictionaries/goAdd.do?DICTIONARIES_ID='+DICTIONARIES_ID;
			 diag.Width = 500;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				 if('none' == diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display){
					 parent.location.href="<%=adminPath%>dictionaries/listAllDict.do?DICTIONARIES_ID=${DICTIONARIES_ID}&dnowPage=${page.currentPage}";
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(name,id){
			var	title='确认删除【'+name+'】的信息吗？';
			var	content='此操作会删除本信息';
			mesageConfirm('删除字典信息',title,content,"delDictionary('"+id+"')");
		}

		function delDictionary(id){
			var path = "<%=adminPath%>dictionaries/deleteD.do?DICTIONARIES_ID="+id;
			$.ajax({
				type:"get",
				url:path,
				success:function(data){
					if(data == "success"){
						window.top.mesageTip("success","删除成功!");
						parent.location.href = "<%=adminPath%>dictionaries/listAllDict.do?DICTIONARIES_ID=${DICTIONARIES_ID}"+"&keywords="+$('#keywords').val()+"&dnowPage=${page.currentPage}";
					}else{
						//alert("删除失败！请先删除子级或删除占用资源");
						var	title='删除失败！';
						var	content='请先删除子级或删除占用资源';
						mesageConfirm('删除字典信息',title,content,"flush()");
					}
				}
			});
		}
		function flush(){
			window.location.href = "<%=adminPath%>dictionaries/list.do?DICTIONARIES_ID="+'${DICTIONARIES_ID}'+"&keywords="+$('#keywords').val();
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=adminPath%>dictionaries/goEdit.do?DICTIONARIES_ID='+Id;
			 diag.Width = 500;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 parent.location.href="<%=adminPath%>dictionaries/listAllDict.do?DICTIONARIES_ID=${DICTIONARIES_ID}&dnowPage=${page.currentPage}";
				}
				diag.close();
			 };
			 diag.show();
		}
		
	</script>
</body>
</html>