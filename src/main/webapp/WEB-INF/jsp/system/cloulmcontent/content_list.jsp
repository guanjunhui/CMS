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
	String imgPath = basePath+"/uploadFiles/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../index/n_top.jsp"%>
<style>
.div_select {
	height: 80px;
	width: 140px;
	overflow-y: auto;
}

.div_select>.label {
	display: block;
}
.col_06 .sele06 {
    float: right !important;
    width: 120px !important;
    padding-bottom: 1px !important;
}
*{margin:0;padding:0;}
li{list-style-type: none;}
.public_search{float: left;width: 220px;margin-left: 20px;cursor:pointer;}
.mScrol{height: 180px;overflow-y:auto;}
.public_search .down_btn{height: 35px;line-height: 35px;width: 35px;background: url(../img/sele_btn.png) no-repeat center;position: absolute;right: 0px;top: 0px;}
.public_search .down_btn:after{content: "";display: block;position: absolute;top: 45%;left: 50%;width: 5px;height: 5px;
  border-right: 1px solid #000;border-bottom: 1px solid #000;transform:translate(-50%,-50%) rotate(45deg);
}
.public_search .search_list{width: 100%;margin-left: 2%;float: left;position: relative;height:35px;line-height: 35px;border:1px solid #ccc;border-radius: 5px;box-sizing:border-box;}
.public_search .selec_con{z-index:99;height: 185px;display: none;background: #fff;border:1px solid #ccc;position: absolute;top: 33px;left: 0px;right: 0px;}
.public_search .show_text{text-indent: 10px;font-size: 14px;}
.public_search .selec_con ul li{margin-left: 10px;cursor: pointer;line-height: 1.5;font-size: 14px;position: relative;padding-left: 10px;}
.public_search .selec_con ul li ol li{margin-left: 0;}
.public_search .selec_con ul>li>span{font-weight: 700;}
.public_search .selec_con ul>li span{padding-left: 5px;position: relative;display: inline-block;}
.public_search .selec_con ul>li em{display: block;position: absolute;top: 0%;left: 0%;}
.public_search .selec_con ul>li ol{display: none;}
.public_search .selec_con ul li:hover>span{color: #d00217;}
.nub{
	width: 40px !important;
}
</style>
</head>
<body class="no-skin">
	<div class="cms_con cf">
	<!-- jsp导航返回栏 -->
	<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<!--<a href="javascript:location.href='<%=adminPath%>columcontent_colum/golist?ID=${pd.topColumId }'">栏目列表</a><i>></i>-->
			<i>内容列表</i>
		</div>
	</div>
	<form id="form" action="<%=adminPath%>columcontent_colum/managecontent.do" method="post">
		<input type="hidden" id="columId" name="columId" value="${pd.ID}" />
		<input type="hidden" id="topColumId" name="topColumId" value="${pd.topColumId}" />
		<input type="hidden" id="columType" name="columType" value="${columType}"/>
		<input type="hidden" id="typeId" name="typeId" value="${pd.typeId}"/>
		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<%-- <c:choose>
						<c:when test="${not empty page.data}">
							<c:if test="${page.data[0].type != '6'}">
								<a href="javascript:void(0);" onclick="addContent('${columType}');">+添加内容</a>
							</c:if>
						</c:when>
					</c:choose> --%>
					<a href="javascript:void(0);" onclick="addContent('${columType}');">+添加内容</a>
					<a href="javascript:void(0);" style="margin-left: 2px;" onclick="managebanner('${columType}');">维护banner</a>
					<a href="javascript:void(0);" style="margin-left: 2px;" onclick="extendManage('${columType}');">+属性管理</a>
					<%-- <c:choose>
						<c:when test="${columType == '5' || columType == '3'}">
							<a href="javascript:;" onclick="improt()" class="upl_file"	style="margin-left: 2px;" data-type="${columType}">确定上传</a>
							<input type="file" name="file"	id="file_name_xlsx">
						</c:when>
					</c:choose>  --%>
					
					<!-- 
					
					<input type="file" name="file"	id="file_name_xlsx">
									<i></i>
					<a href="javascript:;"	class="remove_file" style="display: none;">取消上传</a> -->
							
					
					
					
					<%-- <div class="public_search duo_ser cf">
					  <div class="search_list f_s_list10">
					    <div class="show_text" id="gaiBian">
					     ${(pd.showText==''||pd.showText==null)?'请选择分类':pd.showText}
					    </div>
					    <div class="input_hidden"><input type="hidden" value=""></div>
					    <div class="down_btn"></div>
					    <div class="selec_con">
					      <div class="mScrol cf">
					          <ul class="bg" id="typeSelectd"></ul>
					      </div>
					    </div>
					  </div>				
					</div> --%>
				</div>
				
				<div class="h3_right cf">
					<a class="cur" href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.ID}&topColumId=${pd.topColumId}">内容视图</a>
					<c:choose>
						<c:when test="${columType != '4'}">
							<a href="<%=adminPath%>columcontent_colum/managecontentType.do?ID=${pd.ID}&topColumId=${pd.topColumId}">分类视图</a>
						</c:when>
					</c:choose>
					<div class="search">
						&nbsp;&nbsp;<input type="text" style="width: 120px;" placeholder="请输入内容名称" id="key" name="keyword"><input id="ss" type="button" value="搜索" />
					</div>
				</div>
				<style>
					.h3_right{float:right;}
					.h3_right a{display:inline-block;
					float:left;
					margin-left:-1px;
					display: inline-block;
					width: 115px;
					height: 35px;
					color:#e33411;
					border:1px solid #e33411;
					text-align: center;
					line-height: 35px;
					}
					.h3_right a.cur{background: #e33411;color:#fff;}
				</style>
			</div>
			<div class="table cf juese_con">
				<dl class="list_bg col_06 cf" id="ttable">
					<dt class="cf">
						<div class="tit6">
							<div class="tit6_con">
								名称${template_type}
							</div>
						</div>
						<div class="sele06">操作</div>
						<div class="sele06">排序值</div>
						<c:choose>
							<c:when test="${columType == '5'}">
								<div class="sele06">下载量</div>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${columType != '4'}">
								<div class="sele06">所属分类</div>
							</c:when>
						</c:choose>
						<div class="sele06 other_width">所属栏目</div>
					</dt>
					<c:choose>
						<c:when test="${not empty page.data}">
							<c:forEach items="${page.data}" var="obj" varStatus="vs">
								<dd id="obj_${obj.id}" class="cf">
									<div class="dd_tit cf">
										<div class="tit6">
											<div class="tit6_con">
												<label for="e_${vs.index}"><input type="checkbox" id="e_${vs.index}" value="${obj.id}"/><i></i></label>
												<c:choose>
													<c:when test="${obj.status=='0'}">
														<span><span style="color: #ccb1b1">[隐藏]</span></span>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${obj.recommend=='1'}">
														<span><span style="color: #36bb20">[推荐]</span></span>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${obj.hot=='1'}">
														<span><span style="color: #f90000">[热]</span></span>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${obj.top=='1'}">
														<span><span style="color: #7f7ff9">[置顶]</span></span>
													</c:when>
												</c:choose>
												${obj.name}
											</div>
										</div>
										<div class="sele06 sanJi">
											<a href="javascript:;">管理<span class="sanjiao"></span></a>
											<ul class="guanli_con cf">
												<li><a href="javascript:void(0)" onclick="edit('${obj.id}','${obj.type}')">编辑</a></li>
												<li><a href="javascript:;" onclick="openRecommend('${obj.id }')">推荐设置</a></li>
												<c:choose>
													<c:when test="${empty obj.status || obj.status=='1'}">
														<li id="hide_${obj.id}"><a onclick="isHidden('${obj.id}',0)" href="javascript:;">隐藏</a></li>
													</c:when>
													<c:otherwise>
														<li id="hide_${obj.id}"><a onclick="isHidden('${obj.id}',1)" href="javascript:;">显示</a></li>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${obj.recommend=='1'}">
														<li id="recommend_${obj.id}"><a onclick="recommendContent('${obj.id}',0)" href="javascript:;">取消推荐</a></li>
													</c:when>
													<c:otherwise>
														<li id="recommend_${obj.id}"><a onclick="recommendContent('${obj.id}',1)" href="javascript:;">推荐</a></li>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${obj.hot=='1'}">
														<li id="hot_${obj.id}"><a onclick="hotContent('${obj.id}',0)" href="javascript:;">取消热</a></li>
													</c:when>
													<c:otherwise>
														<li id="hot_${obj.id}"><a onclick="hotContent('${obj.id}',1)" href="javascript:;">热</a></li>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${obj.top=='1'}">
														<li id="top_${obj.id}"><a onclick="topContent('${obj.id}',0)" href="javascript:;">取消置顶</a></li>
													</c:when>
													<c:otherwise>
														<li id="top_${obj.id}"><a onclick="topContent('${obj.id}',1)" href="javascript:;">置顶</a></li>
													</c:otherwise>
												</c:choose>
												<li><a onclick="deleteById('${obj.id }')" href="javascript:;">删除</a></li>
												
											</ul>
										</div>
										<div class="sele06">
											<input type="text" onblur="updateSort(this);" data-id="${obj.id}" data-old="${obj.sort}" value="${obj.sort}" class="nub"/>
										</div>
										<c:choose>
											<c:when test="${columType == '5'}">
												<div class="sele06">
													<input type="text" class="nub" value="${obj.download_count }" >
												</div>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${columType != '4'}">
												<div class="sele06">
													<c:if test="${not empty obj.typeList}">
														<c:forEach items="${obj.typeList}" var="typeobj" varStatus="tvs">
															<c:if test="${tvs.index == 0}">
																${typeobj.name }
															</c:if>
															<c:if test="${tvs.index != 0}">
																,${typeobj.name }
															</c:if>
														</c:forEach>
													</c:if>
												</div>
											</c:when>
										</c:choose>
										<div class="sele06 other_width">
											<c:if test="${not empty obj.columList}">
												<c:forEach items="${obj.columList}" var="columobj" varStatus="cvs">
													<c:if test="${cvs.index == 0}">
														${columobj.columName }
													</c:if>
													<c:if test="${cvs.index != 0}">
														,${columobj.columName }
													</c:if>
												</c:forEach>
											</c:if>
										</div>
									</div>
								</dd>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">没有相关数据 </div>
						</c:otherwise>
					</c:choose>
					<!-- 弹窗——推荐设置-->
                    <div class="layer_bg layer_bg02" style="display:none;">
                        <div class="layer_con cf">
                            <h3><span>选择推荐位置</span><p class="close">x</p></h3>
                                <div class="layer_list_other mScrol222 cf">
                                    <ul id="recommendDiv">
                                    </ul>
                                </div>
                                <div class="all_btn cf">
                                    <input type="button" onclick="setRecommend();" class="submit_btn" value="确定" />
                                    <a href="javascript:void(0);" onclick="colseRecommend(this);" class="submit_re_btn">取消</a>
                                </div>
                        </div>
                    </div>
				</dl>
				<div class="bottom_con cf">
					<div class="all_checkbox">
						<label for="c_10"><input type="checkbox" id="c_10" /><span>全选</span><i></i></label>
						<a  href="javascript:;" onclick="deleteBatchConfirm();">删除</a>
					</div>
					<div class="page_list cf">
						${page.pageStr}						
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="currentId" value=""/>
		</form>
		<div class="surs cf" style="display: none">
			<p id="tips">正在上传</p>
		</div>
		<style>
			.surs{width:200px;height:80px;background:#fff;line-height:80px;text-align:center;border-radius:8px;display:none;
			box-shadow:0 0 10px #999;;position:fixed;top:45%;left:40%;tranform:translate(-50%,-50%);color:green;font-size:16px;
			}
		</style>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
</body>

<script type="text/javascript">
$(function(){
	/* 根据关键词搜索  */
	$("#ss").click(function(){
		/* var $id=$("#key").val();
		$id=$.trim($id);
		window.location.href=adminPath+"columcontent_colum/managecontent.do?ID=${pd.ID}&topColumId=${pd.topColumId}&keyword="+encodeURI(encodeURI($id));  */
		$("#form").submit();
	
	});
  setRecommendDiv();
  setTypeSelectDiv();
  top.hangge();
  //自定义select框
  public_search($('.duo_ser'));
  $(document).on('click','.public_search .selec_con ul>li em',function(e){
    if($(this).parent().children('ol').css('display')=='none'){
      $(this).parent().children('ol').show();
      $(this).text("-")
    }else{
      $(this).parent().children('ol').hide();
      $(this).text("+")
    }
    $(this).parents('.search_list').find('.selec_con').css('display','block');
    e.stopPropagation();
  })
});
//获取分类数据，并填充入条件内的下拉框
function setTypeSelectDiv(){
	var columId=$("#columId").val();
	var columType=$("#columType").val();
	$.ajax({
		type: "GET",
		url:adminPath+"columcontent_colum/getTypeTree.do",
		data:{"columId":columId,"columType":columType},
		dataType:'json',
		cache: false,
		success: function(result){
			if(result.code==200&&result.data!=null){
				appendTypeDiv(result.data);
			}
		}
	});
}
//填充栏目与分类条件的下拉框
function appendTypeDiv(list){
	 var html='';
	 $.each(list,function(index,obj){
		 html+='<li>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+='<em>+</em>';
		 	}
		 	html+='<span onclick="javascript:searchContent(this)" data-typeId="'+obj.id+'" data-columid="'+obj.columId+'">'+obj.name+'</span>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachTypeSubList('',obj.childList);
		 	}
		 html+='</li>';
	 });
	 var columId=$("#columId").val();
	 html+='<li onclick="javascript:searchContent(this)" data-typeId="-1" data-columid="'+columId+'"><span>其它</span></li>';
	 $("#typeSelectd").html(html);
}

function eachTypeSubList(html,list){
	 html+='<ol>';
	 $.each(list,function(index,obj){
		 	html+='<li>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+='<em>+</em>';
		 	}
		 	html+='<span onclick="javascript:searchContent(this)" data-typeId="'+obj.id+'" data-columid="'+obj.columId+'">'+obj.name+'</span>';
		 	if(obj.childList!=null && obj.childList.length>0){
		 		html+=eachTypeSubList('',obj.childList);
		 	}
		 	html+='</li>';
	 });
	 html+='</ol>';
	return html;
}
function searchContent(obj){
	var typeId=$(obj).data("typeId");
	var columId=$(obj).data("columid");
	var topColumId=$("#topColumId").val();
	window.location.href=adminPath+"columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId+"&typeId="+typeId; 
}
//内容维护
function edit(id,type){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	if(type=='1'){//内容栏目
		window.location.href=adminPath+"contentData/toUpdate.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='2'){//资讯栏目
		window.location.href=adminPath+"mymessage/toEdit.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='3'){//产品栏目
		window.location.href=adminPath+"product/toEdit.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='4'){//招聘栏目
		window.location.href=adminPath+'employ/goAdd.do?ID='+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='5'){//下载栏目
		window.location.href=adminPath+"fileresource/toUpdate.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}
}
//banner维护
function managebanner(id){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	window.location.href=adminPath+'columcontent_colum/managebanner.do?columId='+columId+"&topColumId="+topColumId;
}

//内容添加
function addContent(columType){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	var typeId=$("#typeId").val();
	if(columType=='1'){//内容栏目
		window.location.href=adminPath+"contentData/toAdd.do?columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}else if(columType=='2'){//资讯栏目
		window.location.href=adminPath+"mymessage/toAdd.do?columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}else if(columType=='3'){//产品栏目
		window.location.href=adminPath+"product/toAdd.do?flag=1&columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}else if(columType=='4'){//招聘栏目
		window.location.href=adminPath+"employ/goAdd.do?columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}else if(columType=='5'){//下载栏目
		window.location.href=adminPath+"fileresource/toAdd.do?columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}else if(columType=='6'){//首页栏目
		window.location.href=adminPath+"fileresource/toAdd.do?columId="+columId+"&topColumId="+topColumId+"&typeId="+typeId;
	}
}

function public_search(scroll){
    scroll.click(function(e){
      var s_h=$(this).find('.selec_con');
      if(s_h.css('display')=='none'){
        if(s_h.find('li').length>0){
            s_h.show();
        }
        e.stopPropagation();
      }else{
        $('.search_list').find('.selec_con').css('display','none');
      }
    });
    var aLi=$('.selec_con li');
    for(var i=0;i<aLi.length;i++){
      aLi.eq(i).find('span').not('.oth').click(function(e){
        $(this).parents('.search_list').find('.show_text').html($(this).text());
        $(this).parents('.search_list').find('.input_hidden input').val($(this).text());
        $(this).parents('.search_list').find('.selec_con').css('display','none');
        e.stopPropagation();
      });
    };
}

//获取所有的栏目
function setRecommendDiv(){
    $.ajax({
        type: "GET",
        url:adminPath+"columconfig/getColumAndTypeTree.do",
        data:{},
        dataType:'json',
        cache: false,
        success: function(result){
            if(result.code==200&&result.data!=null){
                appendRecommendDiv(result.data);
            }
        }
    });
}

var count=0;
//推荐设置内容填充
function appendRecommendDiv(list){
     var html='';
     $.each(list,function(index,obj){
         html+='<li>';
             html+='<p><em class="show_btn"></em><label><span>'+obj.name+'</span></label></p>';
             if(obj.childList!=null && obj.childList.length>0){
                 html+=eachRecommdSubList('',obj.childList);
             }
         html+='</li>';
     });
     $("#recommendDiv").html(html);
     //控制折叠按钮是否显示
     $('.layer_list_other li .show_btn').each(function(){
        if($(this).parent('p').next('dl').size()==0){
            $(this).hide();
        }
     });
}
function eachRecommdSubList(html,list){
     html+='<dl style="display:none;">';
     $.each(list,function(index,obj){
         html+='<dd>';
             count++;
             html+='<p><em class="show_btn"></em><label for="cc_'+count+obj.id+'"><input type="checkbox" name="selRecomondck" value="'+obj.id+'" id="cc_'+count+obj.id+'" data-type="'+obj.type+'" data-columtype="'+obj.attribute.columType+'" data-columid="'+(obj.attribute.columId?obj.attribute.columId:"")+'" /><span>'+obj.name+'</span><i></i></label></p>';
             if(obj.childList!=null && obj.childList.length>0){
                 html+=eachRecommdSubList('',obj.childList);
             }
         html+='</dd>';
     });
     html+='</dl>';
    return html;
}

//获取推荐设置的关联关系
function openRecommend(id){
    $.ajax({
        type: "GET",
        url:adminPath+"columcontent_colum/getRecommendRelation.do",
        data:{"contentId":id},
        dataType:'json',
        cache: false,
        success: function(result){
            if(result.code==200){
                //重置此站点对应的菜单的选中状态
                $(":input[name='selRecomondck']").each(function(){
                    if($(this).siblings('i').hasClass('active')==true){
                        $(this).attr("checked",false);
                        $(this).siblings('i').removeClass('active');
                        $(this).parents('li').removeClass('tog_san');
                    }
                });
                //设置默认已选择权限
                $.each(result.data,function(index,obj){
                    var dataColumId=obj.columId;
                    var dataTypeId=obj.typeId;
                    $(":input[name='selRecomondck']").each(function(){
                        var value=$(this).attr("value");
                        var columId=$(this).data("columid");
                        var typeId=$(this).data("typeId");
                        if(value==dataColumId || value==dataTypeId){
                            var type=$(this).data("type");
                            var selected=false;
                            if(type==1 && dataTypeId==""){//栏目
                                selected=true;
                            }else if(type==2 && columId==dataColumId){//分类
                                selected=true;
                            }
                        }
                        if(selected){
                            $(this).attr("checked","checked");
                            $(this).siblings('i').addClass('active');
                            return true;
                        }
                    });
                });
            }
            $('.layer_bg02').show();
        }
    });
    $("#currentId").val(id);
}

function setRecommend(){
    var currentId=$("#currentId").val();
    if(currentId==null||currentId==undefined||currentId=='') return false;

    var jsonArry=[];
    $(":input[name='selRecomondck']:checked").each(function(i,obj){
        var json={};
        json.id=$(this).val();
        json.type=$(this).data('type');
        json.columType=$(this).data('columtype');
        json.columId=$(this).data('columid');
        json.contentId=currentId;
        jsonArry.push(json);
    });

    //if(jsonArry==null||jsonArry.length<1) return false;
    $.ajax({
        type: "POST",
        url:adminPath+"product/saveRecommend/"+currentId+".do",
        data:JSON.stringify(jsonArry),
        dataType:'json',
        cache: false,
        contentType:'application/json;charset=utf-8',
        success: function(result){
            if(result.code==200){
                $('.layer_bg02').hide();
                window.top.mesageTip("success","推荐设置已更改!");
            }else{
                window.top.mesageTip("failure","推荐设置更改失败，请联系管理员!");
            }
        }
    });

}

function colseRecommend(obj){
    $(obj).parents('.layer_bg').find('input').removeAttr("checked");
    $(obj).parents('.layer_bg').find('i').removeClass('active');
      $(obj).parents('.layer_bg').hide();
}

function isHidden(param,sta){
	var columType=$("#columType").val();
	 $.ajax({
		type: "POST",
		url:adminPath+"columcontent_colum/updataStatus.do",
		data:{status:sta,ids:param,columType:columType},
		success: function(result){
			 if(result.code==200){
				 var html="";
				 if(sta=='0'){
					 html='<a onclick="isHidden(\''+param+'\',1)" href="javascript:;">显示</a>';
				 }else{
					 html='<a onclick="isHidden(\''+param+'\',0)" href="javascript:;">隐藏</a>';
				 }
				 $("#hide_"+param).html(html);
				 window.top.mesageTip("success","操作成功!");
				 document.location.reload();
			 }else{
				 window.top.mesageTip("failure","操作失败!");
			 }
		}
	}); 
}

//推荐
function recommendContent(id,recommend){
	var columType=$("#columType").val();
	$.ajax({
		type: "POST",
		url:adminPath+"columcontent_colum/updateRecommend.do",
		data:{ids:id,recommend:recommend,columType:columType},
		success: function(result){
			 if(result.code==200){
				 var html="";
				 if(recommend=='0'){
					 html='<a onclick="recommendContent(\''+id+'\',1)" href="javascript:;">推荐</a>';
				 }else{
					 html='<a onclick="recommendContent(\''+id+'\',0)" href="javascript:;">取消推荐</a>';
				 }
				 $("#recommend_"+id).html(html);
				 window.top.mesageTip("success","操作成功");
				 document.location.reload();
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}
/* 热设置 */
function hotContent(id,hot){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	var columType=$("#columType").val();
	var HotorTopSum=0;
	if(hot != 0){
		var panduan = true;
		$.ajax({
			type: "POST",
			url:adminPath+"columcontent_colum/manageContentList.do",
			data:{ID:columId,topColumId:topColumId},
			success: function(result){
				/* $.each(result.page.data,function(i,val){
					if(val.hot == '1'){
						HotorTopSum=HotorTopSum+1;
					}
					if(HotorTopSum > 0){
						panduan = false;
						window.top.mesageTip("failure","热标识只能设置一条!");
						return false;
					}
				}); */
				if(panduan){
					$.ajax({
						type: "POST",
						url:adminPath+"columcontent_colum/updateHot.do",
						data:{ids:id,hot:hot,columType:columType},
						success: function(result){
							 if(result.code==200){
								 var html="";
								 if(hot=='0'){
									 html='<a onclick="hotContent(\''+id+'\',1)" href="javascript:;">热</a>';
								 }else{
									 html='<a onclick="hotContent(\''+id+'\',0)" href="javascript:;">取消热</a>';
								 }
								 $("#hot_"+id).html(html);
								 window.top.mesageTip("success","操作成功");
								 document.location.reload();
							 }else{
								 window.top.mesageTip("failure","操作失败");
							 }
						}
					});
				}
			}
		});
	}else{
		$.ajax({
			type: "POST",
			url:adminPath+"columcontent_colum/updateHot.do",
			data:{ids:id,hot:hot,columType:columType},
			success: function(result){
				 if(result.code==200){
					 var html="";
					 if(hot=='0'){
						 html='<a onclick="hotContent(\''+id+'\',1)" href="javascript:;">热</a>';
					 }else{
						 html='<a onclick="hotContent(\''+id+'\',0)" href="javascript:;">取消热</a>';
					 }
					 $("#hot_"+id).html(html);
					 window.top.mesageTip("success","操作成功");
					 document.location.reload();
				 }else{
					 window.top.mesageTip("failure","操作失败");
				 }
			}
		});
	}
	
}


//置顶
function topContent(id,top){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	var columType=$("#columType").val();
	var HotorTopSum=0;
	if(top != 0){
		var panduan = true;
		$.ajax({
			type: "POST",
			url:adminPath+"columcontent_colum/manageContentList.do",
			data:{ID:columId,topColumId:topColumId},
			success: function(result){
				/* $.each(result.page.data,function(i,val){
					if(val.top == '1'){
						HotorTopSum=HotorTopSum+1;
					}
					if(HotorTopSum > 3){
						panduan = false;
						window.top.mesageTip("failure","置顶标识设置不能大于四条!");
						return false;
					}
				}); */
				if(panduan){
					$.ajax({
						type: "POST",
						url:adminPath+"columcontent_colum/updateTop.do",
						data:{ids:id,top:top,columType:columType},
						success: function(result){
							 if(result.code==200){
								 var html="";
								 if(top=='0'){
									 html='<a onclick="topContent(\''+id+'\',1)" href="javascript:;">置顶</a>';
								 }else{
									 html='<a onclick="topContent(\''+id+'\',0)" href="javascript:;">取消置顶</a>';
								 }
								 $("#top_"+id).html(html);
								 window.top.mesageTip("success","操作成功");
								 document.location.reload();
							 }else{
								 window.top.mesageTip("failure","操作失败");
							 }
						}
					});
				}
			}
		});
	}else{
		$.ajax({
			type: "POST",
			url:adminPath+"columcontent_colum/updateTop.do",
			data:{ids:id,top:top,columType:columType},
			success: function(result){
				 if(result.code==200){
					 var html="";
					 if(top=='0'){
						 html='<a onclick="topContent(\''+id+'\',1)" href="javascript:;">置顶</a>';
					 }else{
						 html='<a onclick="topContent(\''+id+'\',0)" href="javascript:;">取消置顶</a>';
					 }
					 $("#top_"+id).html(html);
					 window.top.mesageTip("success","操作成功");
					 document.location.reload();
				 }else{
					 window.top.mesageTip("failure","操作失败");
				 }
			}
		});
	}
}

function deleteById(data){
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+data+"')");
}

function batchDel(param){
	hideConfirm();
	var columType=$("#columType").val();
	$.ajax({
		type: "POST",
		url:adminPath+"columcontent_colum/delete.do",
		data:{ids:param,columType:columType},
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","操作成功");
				 $("#obj_"+param).remove();
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}

function deleteBatchConfirm(){
	var $ids=$("#ttable input[type='checkbox']:checked");
	if($ids.length==0){
		window.top.mesageTip("warn","请选择删除的选项");
		return false;
	}
	var	title='确认批量删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"deleteBatch()");

}

function deleteBatch(param){
	var $ids=$("#ttable input[type='checkbox']:checked");
	var ids=[];
	$.each($ids,function(index,obj){
		ids.push(obj.value);
	});
	hideConfirm();
	var columType=$("#columType").val();
	$.ajax({
		type: "POST",
		url:adminPath+"columcontent_colum/delete.do",
		data:{ids:ids,columType:columType},
        traditional: true,
		success: function(result){
			 if(result.code==200){
				 window.top.mesageTip("success","操作成功");
				 $.each(ids,function(i,item){
					 $("#obj_"+item).remove();
				 });
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}


//属性管理
function extendManage(columType){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	window.location.href=adminPath+"columcontent_extend/list.do?TYPE="+columType+"&columId="+columId+"&topColumId="+topColumId;
}

//更新排序值
function updateSort(param){
	var columType=$("#columType").val();
	var sort=$(param).val();
	if(sort==null || sort==undefined || sort=="") return false;
	var old=$(param).data("old");
	if(sort==old) return false;
	var zz=/^[1-9][0-9]*$/;
	if(zz.test(sort)){
		$.ajax({
			type: "GET",
			url:adminPath+"columcontent_colum/updateSort.do",
			data:{id:$(param).data("id"),sort:$(param).val(),columType:columType},
			success:function(result){
				if(result.code==200){
					//window.top.mesageTip("success","设置成功");
				}else{
					window.top.mesageTip("failure","操作失败");
				}
			}
		});
	}else{
		window.top.mesageTip("warn","请输入正整数");
	}
}

 //导入
function improt(){
	var type = $('.upl_file').attr('data-type');
	 
	var formData=new FormData();
	formData.append("file",$("#file_name_xlsx")[0].files[0]);
	console.log(formData);
	console.log(type);
	$(".surs").show();
	  $.ajax({
		type: "POST",
		url:'${basePath}/import/importList.do?type='+type+'',
		data:formData,
		processData:false,
        contentType:false,
		success: function(result){
			 if(result.code == 200){
				 $(".surs").hide();
					 window.top.mesageTip("success","上传成功");
					 //window.location.href=adminPath+"product/list.do";
					 window.location.reload();
			 }else{
				 $(".surs").hide();
				 window.top.mesageTip("failure","上传失败");
			 }
		}
	});  
} 




</script>
</html>