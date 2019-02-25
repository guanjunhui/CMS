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
	String imgPath = "/uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="<%=basePath%>plugins/My97DatePicker/WdatePicker.js"></script>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
</head>
<body class="no-skin">
<!-- jsp导航返回栏 -->
			<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${columId}&topColumId=${topColumId}">内容列表</a><i>></i>
			<i>招聘信息维护</i>
		</div>
	</div>
			<form action="<%=adminPath%>employ/save.do?columId=${columId}&topColumId=${topColumId}" name="Form" id="Form" method="post">
				<input type="hidden" id="ID" name="ID" value="${pd.ID}"/>
				<input type="hidden" id="JOB_DESCRIPTION" name="JOB_DESCRIPTION" value="${pd.JOB_DESCRIPTION}"/>
				<input type="hidden" id="JOB_REQUIRE" name="JOB_REQUIRE" value="${pd.JOB_REQUIRE}"/>
				<input type="hidden" id="JOB_CONTACT" name="JOB_CONTACT" value="${pd.JOB_CONTACT}"/>
				<input type="hidden" id="ifAppendSave" name="ifAppendSave"/>
				<input type="hidden" id="columId" value="${columId}"/>
				<div class="cms_c_list cf zhaopin">
					<h3>发布招聘</h3>
					<div class="add_btn_con zp_con01 wrap cf">
						<h5 class="zp_h5">基本信息</h5>
						<%-- <dl class="cf">
							<dt><span class="hot">*</span>所属栏目</dt>
							<dd style="width:auto"><div class="dd_con"><a href="javascript:void(0);" class="layer_btn layer_btn02">+选择</a><span class="warn_tip"></span></div></dd>
							<input type="hidden" id="COLUMCONFIG_ID" name="COLUMCONFIG_ID" value="${pd.COLUMCONFIG_ID}"/>
							<dd>
								<div class="dd_con">
									<div class="show_list">
									<table class="ev">
										<thead><tr><td width="70%">栏目</td><td>操作</td></tr></thead>
										<tbody id="relationBody">
										</tbody>
									</table>
									</div>
								</div>
							</dd>
						</dl> --%>
						<dl class="cf">
							<input type="hidden" id="COLUMCONFIG_ID" name="COLUMCONFIG_ID" value="${pd.COLUMCONFIG_ID}"/>
							<dt>所属栏目</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id="columName" class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						<dl class="cf">							
							<dt>招聘类型</dt>								
							<dd>
								<div class="dd_con">
									<select id="EMPLOY_TYPE" name="EMPLOY_TYPE" class="form-control">
										<c:choose>
											<c:when test='${pd.EMPLOY_TYPE=="0" }'>
												<option value="1">社会招聘</option>
												<option value="0" selected="selected">校园招聘</option>
											</c:when>
											<c:otherwise>
											     <option value="0">校园招聘</option>
												<option value="1" selected="selected">社会招聘</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</dd>
						</dl>
						<div class="zhaopin zp_con01 cf">			
						
						<c:choose>
							<c:when test="${empty employFields.RECRUIT_TITLE }">
								<dl class="cf zp_dl" style="display:none">
								<dt><span class="hot"></span></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf zp_dl">
								<dt><span class="hot">*</span>${employFields.RECRUIT_TITLE}</dt>								
							</c:otherwise>
						</c:choose>							
								<dd><div class="dd_con"><input type="text" id="RECRUIT_TITLE" name="RECRUIT_TITLE" value="${pd.RECRUIT_TITLE}" required></div></dd>
								<dd class="zp_dd">
									<c:if test="${pd.TOURL != '' && pd.TOURL != null}">
										<label for="wlink" id="outurllabel"><input id="wlink" name="wlink" type="checkbox"><span>转向外部链接</span><i class="active"></i></label>
										<div class="dd_con" style="display: block;">
											<input type="text" style="width: 300px;" id="TOURL" name="TOURL" value="${pd.TOURL }" class="url">
										</div>
									</c:if>
									<c:if test="${pd.TOURL == '' || pd.TOURL == null}">
										<label for="wlink" id="outurllabel"><input id="wlink" name="wlink" type="checkbox"><span>转向外部链接</span><i></i></label>
										<div class="dd_con">
											<input type="text" style="width: 300px;" id="TOURL" name="TOURL" value="${pd.TOURL }" class="url">
										</div>
									</c:if>
								</dd>
							</dl>
						</div>
						<!--  <dl class="cf">
							<dt><span class="hot">*</span>模板</dt>
							<dd>
								<div class="dd_con">
									<select class="form-control" id="TEMPLATE_ID" name="TEMPLATE_ID" required>
									</select>
								</div>
							</dd>
						</dl>-->
						<dl class="cf">
							<input type="hidden" name="TEMPLATE_ID" id="TEMPLATE_ID"/>
							<dt>
								所属模板
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" id="u1813_input" class="layer_btn"
										style="width:auto;display: inline-block;background:#fff;color:#333;text-align:left;cursor:text;font-weight:700;">
									</a>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.RECRUIT_PEOPLENUM }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.RECRUIT_PEOPLENUM}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd><div class="dd_con"><input type="text" id="RECRUIT_PEOPLENUM" name="RECRUIT_PEOPLENUM" value="${pd.RECRUIT_PEOPLENUM}" class="digits"/></div></dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.RECRUIT_EMAIL }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.RECRUIT_EMAIL}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd><div class="dd_con"><input type="text" id="RECRUIT_EMAIL" name="RECRUIT_EMAIL" value="${pd.RECRUIT_EMAIL}" class="email"/></div></dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.TIME }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.TIME}</dt>								
							</c:otherwise>
						</c:choose>							
							<dd class="data_dd">
								<div class="dd_con">
									<div class="sle_data">
										<span><input id="d5221" name="START_TIME" class="Wdate" type="text" onclick="var d5222=$dp.$('d5222');WdatePicker({onpicked:function(){d5222.click();},maxDate:'#F{$dp.$D(\'d5222\')}'})"/><i></i></span>
										<em>至</em>
										<span><input id="d5222" name="END_TIME" class="Wdate" type="text" onclick="WdatePicker({minDate:'#F{$dp.$D(\'d5221\')}'})"/><i></i></span>
										<div class="disable"></div>
										<input type="hidden" id="startTime" value="${pd.START_TIME }">
										<input type="hidden" id="endTime" value="${pd.END_TIME }">
									</div>
									<div class="chang">
										<label for="IFALWAYS"><input type="checkbox" id="IFALWAYS" name="IFALWAYS" value="1"/><span>长期有效</span><i></i></label>
									</div>

								</div>
							</dd>
						</dl>
					</div>
					<div class="add_btn_con add_line wrap cf">
						<h5 class="zp_h5">招聘内容</h5>
						
						<c:choose>
							<c:when test="${empty employFields.RECRUIT_POSITION }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt><span class="hot">*</span>${employFields.RECRUIT_POSITION}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd><div class="dd_con"><input type="text" id="RECRUIT_POSITION" name="RECRUIT_POSITION" value="${pd.RECRUIT_POSITION}" required/></div></dd>
						</dl>						
						<c:choose>
							<c:when test="${empty employFields.SEX }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.SEX}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<select id="SEX" name="SEX" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						<c:choose>
							<c:when test="${empty employFields.WORK_AREAS }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.WORK_AREAS}</dt>								
							</c:otherwise>
						</c:choose>	
							<dd><div class="dd_con"><input type="text" id="WORK_AREAS" name="WORK_AREAS" value="${pd.WORK_AREAS}"/></div></dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.WORK_CATEGORY }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.WORK_CATEGORY}</dt>								
							</c:otherwise>
						</c:choose>
							<dd>
								<div class="dd_con">
									<select id="WORK_CATEGORY" name="WORK_CATEGORY" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.EDUCATION_REQUIRED }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.EDUCATION_REQUIRED}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<select id="EDUCATION_REQUIRED" name="EDUCATION_REQUIRED" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.SALARY_RANGE }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.SALARY_RANGE}</dt>								
							</c:otherwise>
						</c:choose>							
							<dd>
								<div class="dd_con">
									<select id="SALARY_RANGE" name="SALARY_RANGE" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.WORK_DEPARTMENT }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.WORK_DEPARTMENT}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd><div class="dd_con"><input type="text" id="WORK_DEPARTMENT" name="WORK_DEPARTMENT" value="${pd.WORK_DEPARTMENT}"/></div></dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.WORK_AGE }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.WORK_AGE}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<select id="WORK_AGE" name="WORK_AGE" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.AGE_REQUIRED }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.AGE_REQUIRED}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<select id="AGE_REQUIRED" name="AGE_REQUIRED" class="form-control">
										<option value="">请选择</option>
									</select>
								</div>
							</dd>
						</dl>
						
						<dl class="cf">
							<dt>
								<span class="hot"></span>属性管理
							</dt>
							<dd>
								<div class="dd_con">
									<a href="javascript:void(0);" class="layer_btn layer_btn0b">+选择</a>
									<spanid="propertySpan" class="warn_tip"></span>
								</div>
							</dd>
						</dl>
						<div class="cf" id="extend">
							<c:forEach items="${pd.fileds}" var="filed" varStatus="num">
								<dl>
									<dt>
										<span class='hot'></span>${filed.name }</dt>
									<dd>
										<div class='dd_con'>										
											<c:if test="${filed.fieldtype==2 }"><ul><li><h6>
												<input type='text' name='fileds[${num.count-1 }].value' value="${filed.value }"><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6></li></ul></c:if>
											<c:if test="${filed.fieldtype==3 }"><input type='date' name='fileds[${num.count-1 }].value' value="${filed.value }"><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/></c:if>
											<c:if test="${filed.fieldtype!=2 && filed.fieldtype!=3 }"><input type='text' name='fileds[${num.count-1 }].value' value="${filed.value }"><input name='fileds[${num.count-1 }].sort' value="${filed.sort }" type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/></c:if>
										<input type='hidden' id="${filed.id }" name='fileds[${num.count-1 }].id' value='${filed.id }' />
										<input type='hidden' name='fileds[${num.count-1 }].name' value='${filed.name }' />
										<input type='hidden' name='fileds[${num.count-1 }].fieldtype' value='${filed.fieldtype }' />
										</div>
									</dd>
								</dl>
							</c:forEach>
						</div>
						
						<c:choose>
							<c:when test="${empty employFields.JOB_DESSIMPLE }">
								<dl class="cf" style="display:none">
								<dt><span class="hot"></span></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt><span class="hot"></span>${employFields.JOB_DESSIMPLE}</dt>								
							</c:otherwise>
						</c:choose>
							<dd>
								<div class="dd_con">
									<ul>
										<li>
											<h6>
												<textarea style="width:62.5%;" class="textarea_numb" name="JOB_DESSIMPLE">${pd.JOB_DESSIMPLE}</textarea>
												<p><span class="word">0</span><span>/</span><span>200</span></p>
											</h6>
										</li>
									</ul>
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.JOB_DESCRIPTION_CONTENT }">
								<dl class="cf" style="display:none">
								<dt><span class="hot"></span></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt><span class="hot"></span>${employFields.JOB_DESCRIPTION_CONTENT}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<div class="eidt_box cf">
										<script id="editor" type="text/plain" style="width:80%;height:200px;">${pd.JOB_DESCRIPTION_CONTENT}</script>
									</div>
								</div>
							</dd>
						</dl>
						<textarea style="display: none;" name="JOB_DESCRIPTION_CONTENT" id="JOB_DESCRIPTION_CONTENT" ></textarea>
						
						<c:choose>
							<c:when test="${empty employFields.JOB_REQUIRE_CONTENT }">
								<dl class="cf" style="display:none">
								<dt><span class="hot"></span></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt><span class="hot"></span>${employFields.JOB_REQUIRE_CONTENT}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<div class="eidt_box cf">
										<script id="editorRequire" type="text/plain" style="width:80%;height:200px;">${pd.JOB_REQUIRE_CONTENT}</script>
									</div>
								</div>
							</dd>
						</dl>
						<textarea style="display: none;" name="JOB_REQUIRE_CONTENT" id="JOB_REQUIRE_CONTENT" ></textarea>
						
						<c:choose>
							<c:when test="${empty employFields.JOB_CONTACT_CONTENT }">
								<dl class="cf" style="display:none">
								<dt><span class="hot"></span></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt><span class="hot"></span>${employFields.JOB_CONTACT_CONTENT}</dt>								
							</c:otherwise>
						</c:choose>	
							<dd>
								<div class="dd_con">
									<div class="eidt_box cf">
										<script id="editorContact" type="text/plain" style="width:80%;height:200px;">${pd.JOB_CONTACT_CONTENT}</script>
									</div>
								</div>
							</dd>
						</dl>
						<textarea style="display:none;" name="JOB_CONTACT_CONTENT" id="JOB_CONTACT_CONTENT" ></textarea>
												
						<c:choose>
							<c:when test="${empty employFields.SEO_TITLE }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.SEO_TITLE}</dt>								
							</c:otherwise>
						</c:choose>
							<dd>
								<div class="dd_con">
									<input type="text" name="seo_title" value="${pd.seo.SEO_TITLE }" />
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.SEO_KEYWORDS }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.SEO_KEYWORDS}</dt>								
							</c:otherwise>
						</c:choose>						
							<dd>
								<div class="dd_con">
									<input type="text" name="seo_keywords" value="${pd.seo.SEO_KEYWORDS }" />
								</div>
							</dd>
						</dl>
						
						<c:choose>
							<c:when test="${empty employFields.SEO_DESCRIPTION }">
								<dl class="cf" style="display:none">
								<dt></dt>								
							</c:when>
							<c:otherwise>
								<dl class="cf">
								<dt>${employFields.SEO_DESCRIPTION}</dt>								
							</c:otherwise>
						</c:choose>						
								<dd><div class="dd_con"><input type="text" name="seo_description" value="${pd.seo.SEO_DESCRIPTION }" /></div></dd>
						</dl>
					</div>
					<div class="all_btn cf">
						<input type="button" class="submit_btn" onclick="save('0');" value="保存" />
						<a href="javascript:void(0);" onclick="save('1');" class="submit_a_btn">保存并继续添加</a>
						<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${columId}&topColumId=${topColumId}" class="submit_re_btn">取消</a>
					</div>
					
					<!-- 弹窗——相关内容 -->
					<div class="layer_bg layer_bg02" style="display:none;">
						<div class="layer_con cf">
							<h3><span>选择栏目</span><p class="close">x</p></h3>
							<div class="layer_list_other mScrol222 cf">
								<ul id="selColumul">
								</ul>
							</div>
							<div class="all_btn cf">
								<input type="button" onclick="setColumId();" class="submit_btn" value="确定" />
								<a href="javascript:void(0);" onclick="colseColum(this);" class="submit_re_btn">取消</a>
							</div>
						</div>
					</div>
				</div>
				<div class="layer_bg layer_bg0b" style="display: none;">
					<div class="layer_con cf">
						<h3>
							<span>属性管理</span>
							<p class="close" onclick="popupHidens(this,'#extenddiv input')">x</p>
						</h3>
						<div class="layer_list_other mScrol222 cf">
							<ul id="extenddiv" class="radio_lock">
								<div
									style="width: 100%; text-align: center; height: 50px; line-height: 50px; font-size: 16px; margin-top: 50px;">无可选择的属性，请添加后，再操作</div>
							</ul>
						</div>
						<div class="all_btn cf">
							<input type="button" onclick="propertyModalHide();"
								class="submit_btn" value="确定" /> <a href="javascript:void(0);"
								class="submit_re_btn"
								onclick="popupHidens(this,'#extenddiv input')">取消</a>
						</div>

					</div>
				</div>	
				</form>
				<div class="footer">© 中企高呈 版权所有</div>
			</div>
			<input type="hidden" id="defaultTemplateId" value="${pd.TEMPLATE_ID }">
			<input type="hidden" id="SEXFb" value="${pd.SEX }">
			<input type="hidden" id="WORK_CATEGORYFb" value="${pd.WORK_CATEGORY }">
			<input type="hidden" id="EDUCATION_REQUIREDFb" value="${pd.EDUCATION_REQUIRED }">
			<input type="hidden" id="SALARY_RANGEFb" value="${pd.SALARY_RANGE }">
			<input type="hidden" id="WORK_AGEFb" value="${pd.WORK_AGE }">
			<input type="hidden" id="AGE_REQUIREDFb" value="${pd.AGE_REQUIRED }">
			<input type="hidden" id="IFALWAYSFb" value="${pd.IFALWAYS }">
	
<!-- 百度富文本编辑框-->
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
<!-- 百度富文本编辑框-->
</body>

<script type="text/javascript">
var propArr=[];
function sortIndex(){
    $("#extend dl").each(function(index,obj){
        var $inputs=$(obj).find("input");
        $($inputs[0]).attr("name","fileds["+index+"].value");
        $($inputs[1]).attr("name","fileds["+index+"].sort");
        $($inputs[2]).attr("name","fileds["+index+"].id");
        $($inputs[3]).attr("name","fileds["+index+"].name");
        $($inputs[4]).attr("name","fileds["+index+"].fieldtype");
        
    });
}
function removePropertys(param){
    $.each(propArr,function(index, value){
        if(param.toString().indexOf(value)==-1){
            var selector="#"+value;
            $(selector).parents('dl').remove();
        }
    });
    propArr=param;
}
function initPropArr(){
    $("#extend dl").each(function(index,obj){
        var $inputs=$(obj).find("input[type='hidden']");
        propArr.push($($inputs[0]).val());
    });
}

$(function(){
	$("#d5221").val($("#startTime").val());
	$("#d5222").val($("#endTime").val());
	$('.layer_btn0b').click(function(){// 属性窗口打开
	  	$('.layer_bg0b').show();
    });
	setExtendDiv();
	initPropArr();
	setColumdiv();
	getTemplateTree();
	defaultWlink();
	defaultAlways();
	getDic("SEX","007");//性别
	getDic("WORK_CATEGORY","A001");//工作性质
	getDic("EDUCATION_REQUIRED","A002");//学历要求
	getDic("SALARY_RANGE","A003");//薪资范围
	getDic("WORK_AGE","A004");//工作年限
	getDic("AGE_REQUIRED","A005");//年龄要求
	$('.layer_btn02').click(function(){
	  	$('.layer_bg02').show();
    });
	$("#wlink").on('change',function(){
		if($(this).attr("checked")!='checked'){
			$("#TOURL").val('');
		}
	});
	setTimeout("ueditor()",500);
	
});

function setExtendDiv(){
	$.ajax({
		url:adminPath+'employExtendFiledController/getTree.do',
		data:{},
		type:'GET',
		success:function(data){
			appendExtenddiv(data.tree);
		}
	})
}
//填充扩展属性列表数据
function appendExtenddiv(list){
	var html='';
	if(list.length==0){
		html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		$("#extenddiv").html(html);
		return false;
	}
	 $.each(list,function(index,obj){
		 html+='<li>';
		 var is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input filedType="'+obj.FIELDTYPE+'" type="checkbox" att="'+obj.NAME+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.NAME+'</span><i></i></label></p>';
			 $("#extend input[type='hidden']").each(function(){
				 if(this.value==obj.ID){
					 is_checked='<p><em class="show_btn"></em><label for="cc_'+obj.ID+'"><input filedType="'+obj.FIELDTYPE+'" checked="checked" type="checkbox" att="'+obj.NAME+'" value="'+obj.ID+'" id="cc_'+obj.ID+'" /><span >'+obj.NAME+'</span><i class="active"></i></label></p>';
				 }
			 });
				html+=is_checked;
		 html+='</li>';
	 });
	 $("#extenddiv").html(html);
	 $('.layer_list_other li .show_btn').each(function(){
     	if($(this).parent('p').next('dl').size()==0){
     		$(this).hide();
     	}
     });
}	
//百度富文本
function ueditor(){
	UE.getEditor('editor');
	UE.getEditor('editorRequire');
	UE.getEditor('editorContact');
}

//ueditor有标签文本
function getContent() {
    var arr = [];
    arr.push(UE.getEditor('editor').getContent());
    return arr.join("");
}

function getContentRequire(){
    var arr = [];
    arr.push(UE.getEditor('editorRequire').getContent());
    return arr.join("");
}
function getContentContact(){
    var arr = [];
    arr.push(UE.getEditor('editorContact').getContent());
    return arr.join("");
}

//获取字典数据(性别)
function getDic(id,code){
	$.ajax({
		type: "GET",
		url: adminPath+"dictionaries/getDic.do?code="+code,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 var html='';
				 var value=$("#"+id+"Fb").val();
				 $.each(result.data, function(i, item){
					 if(item.dictionaries_ID==value){
				         html+='<option value="'+item.dictionaries_ID +'" selected>'+item.name+'</option>';
					 }else{
				         html+='<option value="'+item.dictionaries_ID +'">'+item.name+'</option>';
					 }
				 });
				 $("#"+id).append(html);
			 }
		}
	});
}
//点击确定-产品扩展属性窗口关闭追加弹出框选中属性
function propertyModalHide(){//确定产品属性窗口关闭
	var $id=$("#extenddiv input[type='checkbox']:checked");
	if($id.length==0){
		$("#extend").html("");
		$('.layer_bg0b').hide();
		return false;
	}
			var htmlStr="";
            var arr = [];
			$.each($id,function(index,obj){
                arr.push(obj.value);
                if(propArr.toString().indexOf(obj.value)!=-1){
                    return true;
                }
				if($(obj).attr('filedType')==3){
					htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><input type='date' name='fileds["+index+"].value' ><input name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/>";
					htmlStr+="<input id='"+obj.value+"' type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/></div></dd></dl>";
				}else  if($(obj).attr('filedType')==2){
					htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><ul><li><h6><input type='text' name='fileds["+index+"].value' ><input name='fileds["+index+"].sort'  type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/><p><span class='word'>0</span><span>/</span><span>200</span></p></h6></li></ul>";
					htmlStr+="<input id='"+obj.value+"' type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/></div></dd></dl>";
				}else{
					htmlStr+="<dl><dt><span class='hot'></span>"+$(obj).attr("att")+"</dt>";
					htmlStr+="<dd><div class='dd_con'><input type='text' name='fileds["+index+"].value' ><input name='fileds["+index+"].sort' type='text' placeholder='请设置排序值' style='width:100px;text-align:center'/>";
					htmlStr+="<input id='"+obj.value+"' type='hidden' name='fileds["+index+"].id' value='"+obj.value+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].name' value='"+$(obj).attr("att")+"'/>";
					htmlStr+="<input type='hidden' name='fileds["+index+"].fieldtype' value='"+$(obj).attr("filedType")+"'/></div></dd></dl>";
				}
			});
            removePropertys(arr);
            $("#extend").append(htmlStr);
            sortIndex();
	$('.layer_bg0b').hide();

}
//点击取消-产品扩展属性窗口关闭
function popupHidens(param,selectordiv){
	$(param).parents('.layer_bg').find("input").removeAttr("checked");
	$(param).parents('.layer_bg li babel').removeClass('radio_btn');
	$(param).parents('.layer_bg').find('i').removeClass('active');
		$(selectordiv).each(function(index,obj){
			$("#extend input").each(function(i,n){
				if(obj.value==n.value){
					$(obj).attr("checked",true);
					$(obj).parents('label').find('i').addClass('active');
				}
			})
		});
	  	$(param).parents('.layer_bg').hide();
}

//点击确定追加弹出框选中栏目
function setColumId(){
	var obj=$(":radio[name='selcolum']:checked");
	$("#COLUMCONFIG_ID").val(obj.val());
	if(obj.length>0){
		var bodyHtml='<tr id="heheda">';
		bodyHtml+='<td><i class="special">'+obj.next().text()+'</i></td>';
		bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteColum(this)" data-id="'+obj.val()+'" class="remove_file">移除</a></td>';
		bodyHtml+='</tr>';
		$("#relationBody").html(bodyHtml);
	}
	$('.layer_bg02').hide();
}

function deleteColum(obj){
	$(obj).parents("tr").remove();
	var selectedId=$(obj).data('id');
	$(":input[name='selcolum']").each(function(){
		if($(this).val()==selectedId){
			$(this).attr("checked",false);
			$(this).siblings('i').removeClass('active');
		}
	});
}

function defaultWlink(){
	var outUrl=$("#TOURL").val();
	if(outUrl){
		$("#wlink").prop("checked",true);
		$("#wlink").siblings('i').addClass('active');
		$("#wlink").parent().next().show();
	}
}

function defaultAlways(){
	var IFALWAYSFb=$("#IFALWAYSFb").val();
	if(IFALWAYSFb){
		$("#IFALWAYS").prop("checked",true);
		$("#IFALWAYS").siblings('i').addClass('active');
		$("#IFALWAYS").parents('.dd_con').find('.disable').show();

	}
}

//获取栏目类型树形结构
function setColumdiv(){
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/getAssignTypeTree.do",
		data:{TEM_TYPE:4},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendColumdiv(result.data);
				//var columId=$("#COLUMCONFIG_ID").val();
				var columId=$("#columId").val();
				defaultColum(columId);
			}
		}
	});
}

//选择栏目弹出内容填充
function appendColumdiv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" name="selcolum" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 $("#selColumul").append(html);
	 //控制折叠按钮是否显示
     $('.layer_list_other li .show_btn').each(function(){
    	if($(this).parent('p').next('dl').size()==0){
    		$(this).hide();
    	}
     });
}

//如果当前栏目有子栏目追加子栏目列表递归循环
function eachSubList(html,list){
	 var columId=$("#columId").val();
	 html+='<dl style="display:none;">';
	 $.each(list,function(index,obj){
	 	 //自动定位栏目
	 	 if(columId==obj.id){
	 		$("#columName").html(obj.name);
	 		$("#COLUMCONFIG_ID").val(obj.id);
	 	 }
		 html+='<dd>';
		 	html+='<p><em class="show_btn"></em><label for="cc_'+obj.id+'"><input type="radio" name="selcolum" value="'+obj.id+'" id="cc_'+obj.id+'" /><span>'+obj.name+'</span><i></i></label></p>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachSubList('',obj.childList);
		 	}
		 html+='</dd>';
	 });
	 html+='</dl>';
	return html;
}

//默认选中栏目
function defaultColum(selectedId){
	var selectedId="cc_"+selectedId;
	var bodyHtml='';
	$(":input[name='selcolum']").each(function(){
		if($(this).attr("id")==selectedId){
			$(this).attr("checked","checked");
			$(this).siblings('i').addClass('active');
			var This = $(this).parents('li');
			This.find('dl').stop().slideDown();
			bodyHtml+='<tr id="heheda">';
			bodyHtml+='<td><i class="special">'+$(this).next().text()+'</i></td>';
			bodyHtml+='<td><a href="javascript:void(0);" onclick="deleteColum(this)" data-id="'+$(this).val()+'" class="remove_file">移除</a></td>';
			bodyHtml+='</tr>';
		}
	});
	$("#relationBody").html(bodyHtml);
}

/*
//获取招聘模板
function getTemplateTree(){
	$.ajax({
		type: "GET",
		url: adminPath+"template/getDefinedTree.do",
		dataType:'json',
		data:{"type":"4","temType":"2"},
		cache: false,
		success: function(result){
			 if(result.code==200){
				 var html='';
				 var hasDefault=false;
				 var templateId=$("#defaultTemplateId").val();
				 $.each(result.data, function(i, item){
					 if((templateId==null||templateId==''||templateId==undefined)
							 &&item.hasDefault){
				         html+='<option value="'+item.id +'">默认</option>';
				         hasDefault=true;
					 }else if(templateId==item.id){
				         html+='<option value="'+item.id +'" selected>'+item.name+'</option>';
					 }else{
				         html+='<option value="'+item.id +'">'+item.name+'</option>';
					 }
				 });
				 if(!hasDefault){
					 $("#TEMPLATE_ID").append('<option value="">请选择</option>');
				 }
				 $("#TEMPLATE_ID").append(html);
			 }
		}
	});
}
*/
//获取模板类型树形结构
function getTemplateTree(){
	var columId=$("#columId").val();
	$.ajax({
		type: "GET",
		url:adminPath+"columconfig/findTemplateDetailByColumId.do",
		data:{"id":columId},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200&&result.data!=null){
				 var data=result.data;
				 $("#u1813_input").html(data.temName);
				 $("#TEMPLATE_ID").val(data.id);
			 }
		}
	});
}

function colseColum(obj){
    //$('.layer_list li label').removeClass('radio_btn');
    //$(obj).parents('.layer_bg').find('input').removeAttr("checked");
    //$(obj).parents('.layer_bg').find('i').removeClass('active');
  	//$(".warn_tip").html("");
  	//$("#COLUMCONFIG_ID").val("");
  	$(obj).parents('.layer_bg').hide();
}

function save(type){
	var columconfigid=$("#COLUMCONFIG_ID").val();
	if(columconfigid==null||columconfigid==""||columconfigid==undefined){
	  	$(".warn_tip").html("请选择栏目");
	  	return false;
	}
	if(!$("#Form").valid()){
		return false;
	}
	//以下是获取富文本框的值
	$("#JOB_DESCRIPTION_CONTENT").val(getContent());
	$("#JOB_REQUIRE_CONTENT").val(getContentRequire());
	$("#JOB_CONTACT_CONTENT").val(getContentContact());
	$("#ifAppendSave").val(type);
	$("#Form").submit();
	
    /* 暂时保留的异步提交表单
    var formData = $("#Form").jsonObject();
	$.ajax({
		type: "POST",
		url:adminPath+"employ/save.do",
		data:formData,
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","保存成功!");
				 if(type=='1'){//继续添加
					 location.href=adminPath+'employ/goAdd.do';
				 }else{
					 location.href=adminPath+'employ/list.do';
				 }
			 }else{
				 window.top.mesageTip("failure","保存失败,请联系管理员!");
			 }
		}
	}); */
}
</script>
</html>
