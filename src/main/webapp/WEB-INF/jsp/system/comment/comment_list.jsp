<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						
						<!-- 检索  -->
						<form action="<%=adminPath%>comment/commentlistPage.do" method="post" name="commentForm" id="commentForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="搜索回复/评论内容" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="relevanceContentTitle" value="${pd.relevanceContentTitle }" placeholder="搜索学校/留学机构" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="startTime" id="startTime"  value="${pd.startTime}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始时间"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="endTime" name="endTime"  value="${pd.endTime}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束时间"/></td>
								<td style="vertical-align:top;padding-left:2px;">
								 	<select class="chosen-select form-control" name="commentType" id="commentType" data-placeholder="请选择类型" style="vertical-align:top;width: 120px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${pd.commentType eq '0'}">selected</c:if>>评论</option>
									<option value="1" <c:if test="${pd.commentType eq '1'}">selected</c:if>>提问</option>
								  	</select>
								</td>
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">用户名</th>
									<th class="center">类型</th>
									<th class="center">学校/留学机构</th>
									<th class="center" >客户端类型</th>
									<th class="center" style="width:300px;">内容</th>
									<th class="center" style="width:250px;">时间</th>
									<th class="center" style="width:80px;">状态</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty commentList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${commentList}" var="comment" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<c:if test="${user.USERNAME != 'admin'}"><label><input type='checkbox' name='ids' value="${comment.id }" id="${user.EMAIL }" alt="${user.PHONE }" title="${user.USERNAME }" class="ace"/><span class="lbl"></span></label></c:if>
												<c:if test="${user.USERNAME == 'admin'}"><label><input type='checkbox' disabled="disabled" class="ace" /><span class="lbl"></span></label></c:if>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">
												<c:if test="${not empty comment.memNick }">
													${comment.memNick }
												</c:if>
												<c:if test="${empty comment.memNick }">
													<c:if test="${not empty comment.userName }">
														${comment.userName }
													</c:if>
												</c:if>
											</td>
											<td class="center">
												<c:if test="${comment.commentType==0 }">
													<i class="" title="评论">评论</i>
												</c:if>
												<c:if test="${comment.commentType==1 }">
													<i class="" title="提问">提问</i>
												</c:if>
											</td>
											<td class="center">
												<c:if test="${not empty comment.contentTitle }">
													${comment.contentTitle }
												</c:if>
												<c:if test="${empty comment.contentTitle }">
													<c:if test="${not empty comment.scNameCN }">
														${comment.scNameCN }
													</c:if>
												</c:if>
											</td>
											<td class="center">
												<i class="" title="客户端类型">
													<c:if test="${comment.clientType==0 }">
														手机端
													</c:if>
													<c:if test="${comment.clientType==1 }">
														PC端
													</c:if>
												</i>
											</td>
											<td class="center"><input value="${comment.commentContent }" title="${comment.commentContent }" maxlength="15" disabled="disabled" style="border:0;background:transparent!important;"></td>
											<td class="center">${comment.createTime }</td>
											<td class="center">
												<c:if test="${comment.status==0 }">
													<input type="button" value="开启" id="commentStatus${comment.id }" data-val="${comment.status }" onclick="auditComment('${comment.id }',$(commentStatus${comment.id }).data('val'));">
												</c:if>
												<c:if test="${comment.status==1 }">
													<input type="button" value="关闭" id="commentStatus${comment.id }" data-val="${comment.status }" onclick="auditComment('${comment.id }',$(commentStatus${comment.id }).data('val'));">
												</c:if>
											</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.sms == 1 }">
														<c:if test="${comment.commentType==1 }">
															<a class="btn btn-xs btn-warning" title='查看并回复' onclick="replyComment('${comment.id}','${comment.commentParentId}','${comment.commentPropertyId}');">
																<i class="ace-icon fa fa-envelope-o bigger-120" title="查看并回复"></i>
															</a>
														</c:if>
														<c:if test="${comment.commentType==0 }">
															<a class="btn btn-xs btn-success" title='查看' onclick="viewComment('${comment.id}');">
																<i class="ace-icon fa fa-pencil-square-o bigger-120" title="查看"></i>
															</a>
														</c:if>
													</c:if>
													<%-- <c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-success" title="查看" onclick="viewComment('${comment.id}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="查看"></i>
													</a>
													</c:if> --%>
													<c:if test="${QX.del == 1 }">
													<a class="btn btn-xs btn-danger" onclick="delComment('${comment.id }');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.sms == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="auditComment('${comment.id }','${comment.status }');" class="tooltip-success" data-rel="tooltip" title="审核通过">
																	<span class="blue">
																		<i class="ace-icon fa fa-envelope-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="viewComment('${comment.id }');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="delComment('${comment.id }');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
											</td>
										</tr>
									
									</c:forEach>
									</c:if>
									<c:if test="${QX.cha == 0 }">
										<tr>
											<td colspan="10" class="center">您无权查看</td>
										</tr>
									</c:if>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
					<div class="page-header position-relative">
					<table style="width:100%;">
						<tr>
							<td style="vertical-align:top;">
								<c:if test="${QX.del == 1 }">
								<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
								</c:if>
							</td>
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
						</tr>
					</table>
					</div>
					</form>
	
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	</body>

<script type="text/javascript">
$(top.hangge());
//检索
function searchs(){
	top.jzts();
	$("#commentForm").submit();
}

//删除
function delComment(userId){
	bootbox.confirm("确定要删除此条数据吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=adminPath%>user/delComment.do?commentId="+commentId+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//审核通过
function auditComment(commentId,status){
	$.ajax({
	    url:'<%=adminPath%>comment/auditComment',   //请求的url地址
	    dataType:"json",   //返回格式为json
	    async:true,//请求是否异步，默认为异步，这也是ajax重要特性
	    data:{
	    	"commentId":commentId,
	    	"status":status
	    },
	    type:"POST",   //请求方式
	    beforeSend:function(){
	        //请求前的处理
	        
	    },
	    success:function(req){
	    	var statusId='commentStatus'+commentId;
	    	
	    	if(status==0){
	    		$('#'+statusId+'').data('val','1');
	    		$('#'+statusId+'').val('关闭');
	        }else if(status==1){
	        	//$('#'+statusId+'').attr('data-val','0');
	        	$('#'+statusId+'').data('val','0');
	    		$('#'+statusId+'').val('开启');
	        }
	    },
	    complete:function(){
	        //请求完成的处理
	    },
	    error:function(){
	        //请求出错处理
	    }
	});
}

//回复
function replyComment(commentId,commentParentId,commentPropertyId){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="查看并回复";
	 diag.URL = "<%=adminPath%>comment/goCommentReply.do?commentId="+commentId+"&commentParentId="+commentParentId+"&commentPropertyId="+commentPropertyId;
	 diag.Width = 600;
	 diag.Height = 600;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
		nextPage(${page.currentPage});
	 };
	 diag.show();
}

//查看
function viewComment(commentId){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="详情";
	 diag.URL = '<%=adminPath%>comment/findCommentById.do?commentId='+commentId;
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			nextPage(${page.currentPage});
		}
		diag.close();
	 };
	 diag.show();
}

//删除
function delComment(commentId){
	bootbox.confirm("确定要删除此条数据吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=adminPath%>comment/delComment.do?commentId="+commentId+"&tm="+new Date().getTime();
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//批量操作
function makeAll(msg){
	bootbox.confirm(msg, function(result) {
		if(result) {
			var str = '';
			var emstr = '';
			var phones = '';
			var username = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				  if(document.getElementsByName('ids')[i].checked){
				  	if(str=='') str += document.getElementsByName('ids')[i].value;
				  	else str += ',' + document.getElementsByName('ids')[i].value;
				  	
				  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
				  	else emstr += ';' + document.getElementsByName('ids')[i].id;
				  	
				  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
				  	else phones += ';' + document.getElementsByName('ids')[i].alt;
				  	
				  	if(username=='') username += document.getElementsByName('ids')[i].title;
				  	else username += ';' + document.getElementsByName('ids')[i].title;
				  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zcheckbox").tips({
					side:3,
		            msg:'点这里全选',
		            bg:'#AE81FF',
		            time:8
		        });
				
				return;
			}else{
				if(msg == '确定要删除选中的数据吗?'){
					top.jzts();
					$.ajax({
						type: "POST",
						url: '<%=adminPath%>comment/deleteAllComment.do?tm='+new Date().getTime(),
				    	data: {COMMENT_IDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							 $.each(data.list, function(i, list){
									nextPage(${page.currentPage});
							 });
						}
					});
				}else if(msg == '确定要给选中的用户发送邮件吗?'){
					sendEmail(emstr);
				}else if(msg == '确定要给选中的用户发送短信吗?'){
					sendSms(phones);
				}else if(msg == '确定要给选中的用户发送站内信吗?'){
					sendFhsms(username);
				}
			}
		}
	});
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
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

	
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
});
		
</script>
</html>
