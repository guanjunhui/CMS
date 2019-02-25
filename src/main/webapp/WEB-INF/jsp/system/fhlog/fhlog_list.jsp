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
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body>
	<!-- cms_con开始 -->
	<div class="cms_con cf">

		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
				<i>日志列表</i>
			</div>
		</div>
		
		<form id="Form" action="<%=adminPath%>fhlog/list.do" method="post">
			<div class="cms_c_list biaoge_con chanpin_con juese_con cf">
				<div class="h3 cf">
					<div class="h3_left cf">
						<dd class="data_dd">
							<div class="dd_con">
								<div class="sle_data">
									<span><input id="d5221" name="lastStart" class="Wdate" 
										type="text" value="${pd.lastStart}" 
										onclick="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.click();},maxDate:'#F{$dp.$D(\'d5222\')}'})" /><i></i></span>
									<em>至</em> <span><input id="d5222" name="lastEnd"
										class="Wdate" type="text" value="${pd.lastEnd}"
										onclick="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})" /><i></i></span>
									<div class="disable"></div>
								</div>
							</div>
						</dd>
					</div>
					<div class="search">
						<input type="text" id="key" name="keywords" value="${pd.keywords}"><input
							id="ss" type="button" value="搜索" />
					</div>
				</div>

				<div class="table cf rizhi_dl">
					<dl class="list_bg col_04 cf" id="did">
						<dt class="cf">
							<div class="tit4">
								<div class="tit4_con">
									<div class="pro_img">序号</div>
									用户名
								</div>
							</div>
							<div class="sele04">操作</div>
							<div class="sele04 other_width">操作时间</div>
							<div class="sele04 other_width2">事件</div>
						</dt>

						<c:forEach items="${varList}" var="var" varStatus="vs">
							<dd class="cf">
								<div class="dd_tit cf">
									<div class="tit4">
										<div class="tit4_con">
											<label for="e_${vs.index+1}"><input type="checkbox" id="e_${vs.index+1}" value="${var.FHLOG_ID}" /><i></i></label>
											<div class="pro_img" style="background:none;">${vs.index+1}</div>
											${var.USERNAME}
										</div>
									</div>
									<div class="sele04 sanJi">
										<a onclick="deleteSingle('${var.FHLOG_ID}')" href="javascript:;">删除</a>
									</div>
									<div class="sele04 other_width">${var.CZTIME}</div>
									<div class="sele04 other_width2">${var.CONTENT}</div>
								</div>
							</dd>
							
							
						</c:forEach>
					</dl>
		</form>
		<div class="bottom_con cf">
			<div class="all_checkbox">
				<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
				<a id="sc" href="javascript:;">删除</a>
			</div>

		</div>

		<div class="bottom_con cf">
			<div class="page_list cf">${page.pageStr}</div>
		</div>

		<!-- cms_con结束 -->
		<script type="text/javascript" src="plugins/My97DatePicker/WdatePicker.js"></script>
</body>

<script type="text/javascript">
		$(function(){
			top.hangge();
			//全选反选
			$("#c_10").click(function(){
				var isChecked = this.checked;
				var checkboxs = $("dl input[type='checkbox']");
				$(checkboxs).each(function(){
					this.checked = isChecked;
				});
			});
			
			/* 搜索  */
			$("#ss").click(function(){
				$("#Form").submit();
			});
			
			/* 批量删除 */
			$("#sc").click(function(){
				var $ids=$("#did input[type='checkbox']:checked");
				if($ids.length==0){
					window.top.mesageTip("warn","请选择删除的项!");
					return false;
				}
				var ids=[];
				var param="";
				$.each($ids,function(index,obj){
					ids.push(obj.value);
				});
				param+=ids.join(",");
				var	title='确认批量删除所选信息吗？';
				var	content='此操作会删除该信息';
				mesageConfirm('删除信息',title,content,"batchDel('"+param+"')");
				
			});

		});
		function batchDel(param){
			hideConfirm();
			$.ajax({
				type: "POST",
				url: '<%=adminPath%>fhlog/deleteAll.do?tm='+new Date().getTime(),
		    	data: {DATA_IDS:param},
				dataType:'json',
				cache: false,
				success: function(data){
					window.top.mesageTip("success","删除成功!");
					window.location.href = "<%=adminPath%>fhlog/list.do";
				}
			});
		}
		//删除
		function deleteSingle(Id){
			var	title='确认删除此条信息吗？';
			var	content='此操作会删除该信息';
			mesageConfirm('删除信息',title,content,"deleteById('"+Id+"')");
		}
		//单条删除
		function deleteById(Id){
			hideConfirm();
			$.ajax({
				type:'get',
				url : "<%=adminPath%>fhlog/delete.do?FHLOG_ID="+Id+"&tm="+new Date().getTime(),
				success :function(data){
					if(data == "success"){
						window.top.mesageTip("success","删除成功!");
						window.location.href = "<%=adminPath%>fhlog/list.do";
				}
			}
		});
			
		//列表显示
		function list(){
			
		}
	}
</script>

</html>