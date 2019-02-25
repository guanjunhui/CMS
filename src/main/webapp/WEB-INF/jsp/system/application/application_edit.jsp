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
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
	function save(){
		if(!$("#Form").valid()){
			return false;
		}
		$("#Form").submit();
	}
</script>
</head>
<body>

	<!-- cms_con开始 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <a href="<%=adminPath%>application/list.do">申请列表</a>
				<i>></i> <i>修改申请</i>
			</div>
		</div>

		<div class="cms_c_list cf">
			<h3>编辑内容</h3>

			<form action="<%=adminPath%>application/updateApplication.do" id="Form" method="post" enctype="multipart/form-data">
				<input type="hidden" name="id" value="${content.id }" />
				<div class="add_btn_con wrap cf">
					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt>
								<span class="hot">*</span>公司名称
								
							</dt>
							<dd>
								<div class="dd_con">
									<input type="text" name="companyname"
										value="${application.companyname }" required>
								</div>
							</dd>
						</dl>
					</div>
					<dl class="cf">
						<dt><span class="hot">*</span>事项</dt>
						<dd>
							<div class="dd_con"><input type="text" name="item" value="${application.item }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>所属行业</dt>
						<dd>
							<div class="dd_con"><input type="text" name="industry" value="${application.industry }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>合作领域/项目</dt>
						<dd>
							<div class="dd_con"><input type="text" name="field" value="${application.field }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>成立时间</dt>
						<fmt:formatDate value="${application.establishment }" var="ymd" pattern="yyyy-MM-dd hh:mm:ss"/>
						<dd><div class="dd_con"><input id="release_time" style="width:185px;"  class="Wdate" type="text" value="${ymd}" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" name="establishment"/></div></dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>官方网站</dt>
						<dd>
							<div class="dd_con"><input type="text" name="website" value="${application.website }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>注册资本</dt>
						<dd>
							<div class="dd_con"><input type="text" name="registercapital" value="${application.registercapital }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>员工总数</dt>
						<dd>
							<div class="dd_con"><input type="text" name="number" value="${application.number }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>年销售额</dt>
						<dd>
							<div class="dd_con"><input type="text" name="annualsales" value="${application.annualsales }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>生产量</dt>
						<dd>
							<div class="dd_con"><input type="text" name="annualoutput" value="${application.annualoutput }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>公司地址</dt>
						<dd>
							<div class="dd_con"><input type="text" name="companyaddress" value="${application.companyaddress }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot"></span>厂房面积</dt>
						<dd>
							<div class="dd_con"><input type="text" name="workshoparea" value="${application.workshoparea }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>姓名</dt>
						<dd>
							<div class="dd_con"><input type="text" name="fullname" value="${application.fullname }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>职务</dt>
						<dd>
							<div class="dd_con"><input type="text" name="post" value="${application.post }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>联系电话</dt>
						<dd>
							<div class="dd_con"><input type="text" name="contactnumber" value="${application.contactnumber }" />
							</div>
						</dd>
					</dl>
					<dl class="cf">
						<dt><span class="hot">*</span>电子邮件</dt>
						<dd>
							<div class="dd_con"><input type="text" name="email" value="${application.email }" />
							</div>
						</dd>
					</dl>
				</div>
				<div class="all_btn cf">
					<span id="result"></span><input type="button" class="submit_btn" value="保存" onclick="save()" />  
					<a href="<%=adminPath%>/application/list.do" class="submit_re_btn">取消</a>
				</div>
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->
</body>
</html>