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
	String imgPath = path+"/../uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
</head>
<body class="no-skin">
<!-- jsp导航返回栏 -->
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>			
			<i>系统管理员入口，此权限不开放给客户</i>
		</div>
	</div>
			<form action="<%=adminPath%>clone/goCopySite.do" name="Form" id="Form" method="post" enctype="text/plain">
				<input type="hidden" name="siteId" id="siteId" value=""/>
				<div class="cms_c_list cf">
					<h3>复制网站数据</h3>
					<div class="add_btn_con wrap cf">
						<h5 class="zp_h5"></h5>
						<dl class="cf">
							<dt><span class="hot">*</span>复制源站点</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="siteFrom" name="siteFrom" required>	
									<option value="" selected="selected">选择复制的站点</option>																									
										<c:choose>										
											<c:when test="${not empty siteList}">
												<c:forEach items="${siteList}" var="obj" varStatus="vs">
													<option value="${obj.siteId}">${obj.siteName}</option>														
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</dd>
						</dl>						
						<dl class="cf">
							<dt><span class="hot">*</span>复制到站点</dt>
							<dd><div class="dd_con">
									<select class="form-control" id="siteTo" name="siteTo" required>	
									<option value="" selected="selected">选择新的站点</option>																									
										<c:choose>
											<c:when test="${not empty siteList}">
												<c:forEach items="${siteList}" var="obj" varStatus="vs">
													<option value="${obj.siteId}">${obj.siteName}</option>														
												</c:forEach>
											</c:when>
										</c:choose>
									</select>
								</div>
							</dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>源模板目录</dt>
							<dd><div class="dd_con"><input type="text" id="templateCatalogue" name="templateCatalogue" value="E:\apache-tomcat-7.0.77\webapps\cmsbest_upgrade_code\WEB-INF\html\yongchang\" style="color:#888888" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>复制到目录</dt>
							<dd><div class="dd_con"><input type="text" id="templateNewCatalogue" name="templateNewCatalogue" value="E:\yongchang\en\" style="color:#888888" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>模板文件后缀</dt>
							<dd><div class="dd_con"><input type="text" id="templateSuffix" name="templateSuffix" value=".html" style="color:#888888" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>新模板前缀</dt>
							<dd><div class="dd_con"><input type="text" id="templatePrefix" name="templatePrefix" value="en-" style="color:#888888" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>站点语言前缀</dt>
							<dd><div class="dd_con"><input type="text" id="lanuagePrefix" name="lanuagePrefix" value="英文站点-" style="color:#888888" required/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot">*</span>是否复制内容数据</dt>
							<dd><div class="dd_con">
									<input type="radio" id="iscopydata" name="iscopydata" value="1" style="color:#888888" checked/><label>是</label>
									<input type="radio" id="iscopydata" name="iscopydata" value="0" style="color:#888888"/><label>否</label>
								</div>
							</dd>
						</dl>
					</div>
					<%-- <div class="add_btn_con add_line wrap cf">
						<h5 class="zp_h5">邮箱信息</h5>
						<dl class="cf">
							<dt><span class="hot"></span>邮件地址</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailAddr" name="siteEmailAddr" value="${pd.siteEmailAddr}" class="email"/></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>密码</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailPwd" name="siteEmailPwd" value="${pd.siteEmailPwd}" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>发信名称</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailPrefix" name="siteEmailPrefix" value="${pd.siteEmailPrefix}" /></div></dd>
						</dl>
						<dl class="cf">
							<dt><span class="hot"></span>发送服务器（SMTP）</dt>
							<dd><div class="dd_con"><input type="text" id="siteEmailSmtp" name="siteEmailSmtp" value="${pd.siteEmailSmtp}" /></div></dd>
						</dl>
					</div> --%>
					<div class="all_btn cf">showContents
						<input type="button" onclick="save(0);" class="submit_btn" value="开始复制" />	
						<input type="reset"  class="submit_btn" value="取消" />
						<input type="button" onclick="save(1);" class="submit_btn" value="删除内容数据" />
						<input type="button" onclick="save(2);" class="submit_btn" value="删除栏目数据" />
						<input type="button" onclick="save(3);" class="submit_btn" value="测试内容数据" />
						
					</div>
				</div>
			</form>
				
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
</body>
<script type="text/javascript">
function save(tag){
	if(!$("#Form").valid()){
		return false;
	}
    var formData = new FormData($("#Form")[0]);
    var dopath = "clone/goSuperAdmin?SUPER=SUPERDOTHIS";
    if(tag==0){
    	dopath = "clone/goCopySite.do";
    }if(tag==1){
    	dopath = "clone/goDelContent.do";
    }if(tag==2){
    	dopath = "clone/goDelColumn.do";
    }if(tag==3){
    	dopath = "clone/showContents.do";
    }
	$.ajax({
		type: "POST",
		url:adminPath+dopath,
		data:formData,
		dataType:'json',
		cache: false,
        processData: false,
        contentType: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 location.href=adminPath+'clone/goSuperAdmin?SUPER=SUPERDOTHIS';
			 }else{
				 window.top.mesageTip("failure","保存失败,请重试!");
			 }
		}
	});
}
</script>
</html>
