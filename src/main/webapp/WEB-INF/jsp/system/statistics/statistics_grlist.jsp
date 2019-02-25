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
	String imgPath = (String) application.getAttribute("imgPath");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<style>
.div_select {
	height: 80px;
	width: 140px;
	overflow-y: auto;
}

.div_select>.label {
	display: block;
}
</style>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
</head>
<body>
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
				<i>访问记录</i>
			</div>
		</div>

		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<!-- <a onclick="add();">+新增黑名单</a> -->
					<!-- 检索  -->
					<div class="a_btn cf">
						<a href="javascript:void(0)" onclick="toExcel();"><img
							src="static/images/chu.png" alt="">导出</a>
					</div>
				</div>
				<div class="search">
					<form action="<%=adminPath%>statistics/grlist.do" method="post"
						name="Form" id="Form">
						<input type="text" placeholder="这里输入关键词" class="nav-search-input"
							id="key" name="keywords" value="${pd.keywords}"><input
							id="ss" onclick="tosearch();" type="button" value="搜索" />
					</form>
				</div>
			</div>
			<div class="table cf">
				<dl class="list_bg col_07 cf">
					<dt class="cf">
						 <div class="tit7"> 
							<div class="tit7_con">序号</div>
						 </div> 
						<div class="sele07">操作</div>
						<div class="sele07">访问次数</div>
						<div class="sele07">日期</div>
						<div class="sele07">运营商</div>
						<div class="sele07 other_width">地区</div>
						<div class="sele07">IP</div>
					</dt>
					<!-- 遍历开始 -->
					<c:forEach items="${varList}" var="var" varStatus="vs">
						<dd class="cf">
							<div class="dd_tit cf">
								<div class="tit7">
									<div class="tit7_con">
										<label><input type="checkbox" name="ids"
											value="${var.STATISTICS_ID}" /><i style="top:7px;"></i></label> ${vs.index+1}
									</div>
								</div>
								<div class="sele07 sanJi">
									<a href="javascript:;">管理<span class="sanjiao"></span></a>
									<ul class="guanli_con cf">
										<li><a onclick="edit('${var.STATISTICS_ID}');"
											href="javascript:void(0)">加入黑名单</a></li>
										<li><a onclick="confirmDelDiv('${var.STATISTICS_ID}');"
											href="javascript:;">删除</a></li>
									</ul>
								</div>
								<div class="sele07">${var.CCOUNT}</div>
								<div class="sele07">${var.CDATE}</div>
								<div class="sele07">${var.OPERATOR}</div>
								<div class="sele07 other_width">${var.AREA}</div>
								<div class="sele07">${var.IP}</div>
							</div>
						</dd>
					</c:forEach>


				</dl>
				<div class="bottom_con cf">
					<div class="all_checkbox">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
						<a id="sc" onclick="confirmDelDivChecked();"
							href="javascript:void(0);">批量删除</a>
					</div>
					<div class="page_list cf">${page.pageStr}</div>
				</div>
			</div>
		</div>
		<!-- /.main-content -->

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());//关闭加载状态
		//检索
		function tosearch(){
			top.jzts();
			$("#Form").submit();
		}
		$(function() {
		
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
					if(event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				});
				$('#chosen-multiple-style .btn').on('click', function(e){
					var target = $(this).find('input[type=radio]');
					var which = parseInt(target.val());
					if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
					 else $('#form-field-select-4').removeClass('tag-input-style');
				});
			}
		});
		
		//跳转
		function list(){
			 top.jzts();
			 window.location.href='<%=adminPath%>statistics/list.do';
		}
		
		
		//加入黑名单
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="加入黑名单";
			 diag.URL = '<%=adminPath%>statistics/goEdit.do?STATISTICS_ID='+Id;
			 diag.Width = 624;
			 diag.Height = 380;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮 
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage('${page.currentPage}');
				}
				diag.close();
			 };
			 diag.show();
		}
		//当单个删除的时候提示是否删除
		function confirmDelDiv(Id){
			var	title='确定要删除选中的数据吗?';
			var	content='此操作会删除选中的访问记录,请谨慎操作!';
			mesageConfirm('删除选中访问记录',title,content,"deleteById('"+Id+"')");
		}
		//单个删除操作
		function deleteById(Id){
			hideConfirm();
		    var url = "<%=adminPath%>statistics/delete.do?STATISTICS_ID="+Id+"&tm="+new Date().getTime();
			$.get(url,function(data){
				if(data=="success"){
					 window.top.mesageTip("success","删除成功!");
				}
				nextPage('${page.currentPage}');
			}); 
		}
		
		//当批量删除的时候提示是否删除
		function confirmDelDivChecked(){
			var $ids=$("input[type='checkbox']:checked");
			if($ids.length==0){
				window.top.mesageTip("warn","请选择要删除的访问记录");
				return false;
			}
			var	title='确定要删除选中的访问记录吗?';
			var	content='此操作会删除选中的访问记录,请谨慎操作!';
			mesageConfirm('删除选中访问记录',title,content,"deleteByIds()");
		}
		//批量删除操作
		function deleteByIds(){
			var str = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++){
			  if(document.getElementsByName('ids')[i].checked){
			  	if(str=='') str += document.getElementsByName('ids')[i].value;
			  	else str += ',' + document.getElementsByName('ids')[i].value;
			  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
				});
				return false;
			}
			hideConfirm();
			window.location.href=adminPath+"statistics/deleteAll.do?DATA_IDS="+str+"&tm="+new Date().getTime();
		}
		//导出excel
		function toExcel(){
			window.location.href='<%=adminPath%>statistics/excel.do';
		}
		
	</script>


</body>
</html>