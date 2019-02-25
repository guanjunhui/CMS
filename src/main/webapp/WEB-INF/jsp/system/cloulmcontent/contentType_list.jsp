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
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<style>
.nub {
	border: 1px solid #747474;
	width: 35px !important;
	height: 20px !important;
	line-height: 20px !important;
	text-align: center;
	text-indent: 0 !important;
}

.div_select {
	height: 80px;
	width: 140px;
	overflow-y: auto;
}

.div_select>.label {
	display: block;
}
.h3_right{float:right;}
.h3_right a{
	display:inline-block;
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
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/n_top.jsp"%>
<script type="text/javascript">
$(function(){
	getData();
});

function setColum(list,html){
	$.each(list,function(index,obj){
		if(index==list.length-1){
		    html+=obj.columName;
		}else{
			html+=obj.columName+",";
		}
	});
	return html;
}
function deleteById(data){
	var	title='确认删除所选信息吗？';
	var	content='此操作会删除该信息';
	mesageConfirm('删除信息',title,content,"batchDel('"+data+"')");
}
function batchDel(param){
	hideConfirm();
	var type=$("#columType").val();
	var str;
	if(type=='1'){//内容栏目
		str=adminPath+"contentType/delete.do";
	}else if(type=='2'){//资讯栏目
		str=adminPath+"mymessageType/delete.do";
	}else if(type=='3'){//产品栏目
		str=adminPath+"productType/delete.do";
	}else if(type=='5'){//下载栏目
		str=adminPath+"downloadType/delete.do";
	}
	$.ajax({
		type: "POST",
		url:str,
		data:{id:param},
		success: function(result){
			 if(result.success == true || result.code==200){
				 window.top.mesageTip("success","操作成功");
				 $("#obj_"+param).remove();
			 }else{
				 window.top.mesageTip("failure","操作失败");
			 }
		}
	});
}

function isHidden(param,sta){
    var type=$("#columType").val();
    var str;
	if(type=='1'){//内容栏目
		str=adminPath+"contentType/updataStatus.do";
	}else if(type=='2'){//资讯栏目
		str=adminPath+"mymessageType/updateStatus.do";
	}else if(type=='3'){//产品栏目
		str=adminPath+"productType/updataStatus.do";
	}else if(type=='5'){//下载栏目
		str=adminPath+"downloadType/updataStatus.do";
	}
     $.ajax({
        type: "GET",
        url:str,
        data:{STATUS:sta,ids:param},
        success: function(result){
             if(result.success == true || result.code==200){
                 var html="";
                 if(sta=='0'){
                     html='<a onclick="isHidden(\''+param+'\',1)" href="javascript:;">显示</a>';
                 }else{
                     html='<a onclick="isHidden(\''+param+'\',0)" href="javascript:;">隐藏</a>';
                 }
                 $("#hide_"+param).html(html);
                 window.top.mesageTip("success","操作成功!");
             }else{
                 window.top.mesageTip("failure","操作失败!");
             }
        }
    }); 
}


//分类维护
function edit(id,type){
	
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	if(type=='1'){//内容栏目
		window.location.href=adminPath+"contentType/toEdit.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='2'){//资讯栏目
		window.location.href=adminPath+"mymessageType/toEdit.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='3'){//产品栏目
		window.location.href=adminPath+"productType/toEdit.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}else if(type=='5'){//下载栏目
		window.location.href=adminPath+"downloadType/toUpdate.do?id="+id+"&columId="+columId+"&topColumId="+topColumId;
	}
}

//分类添加
function addContentType(columType){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	if(columType=='1'){//内容栏目
		window.location.href=adminPath+"contentType/toAdd.do?columId="+columId+"&topColumId="+topColumId;
	}else if(columType=='2'){//资讯栏目
		window.location.href=adminPath+"mymessageType/toAdd.do?columId="+columId+"&topColumId="+topColumId;
	}else if(columType=='3'){//产品栏目
		window.location.href=adminPath+"productType/toAdd.do?flag=1&columId="+columId+"&topColumId="+topColumId;
	}else if(columType=='5'){//下载栏目
		window.location.href=adminPath+"downloadType/toAdd.do?columId="+columId+"&topColumId="+topColumId;
	}
}

function getData(page){
	var columId=$("#columId").val();
	var topColumId=$("#topColumId").val();
	var columType=$("#columType").val();
	var currentPage = 0;
	if(page){
		currentPage = page;
	}
	$.ajax({
		type: "GET",
		url:adminPath+"columcontent_colum/manageContentTypelistPage.do",
		data:{"ID":columId,"topColumId":topColumId,"columType":columType,"showCount":10,"currentPage":currentPage},
		dataType:'json',
		cache: false,
		success:function(result){
			     var html='';
				 var page=result.page;
				 //console.log(result.page.pageStr);
				 if(page.data.length==0){
					 /* window.location.href=adminPath+"columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId; */
				 }else{
					 html=apeendBody(page.data,'',0);
				 }
			 
			 $('#listPage').html(html);
			 $('#listPageStr').html(page.ajaxPageStr);
			//间隔行色变化
			 $('.biaoge_con .table .dd_tit').each(function(index,el){
				 if(index%2==0){
				 	$(this).addClass('other_bg');
				 }
			 });
		}
	});
}

function apeendBody(list,html,num){
	var num_=num;
	var status = 0;
	$.each(list,function(i,obj){
		html+='<dd id="obj_'+obj.id+'"class="cf">';
		html+='<div class="dd_tit cf">';
			html+='<div class="tit4">';
				html+='<div class="tit4_con xiala_btn">';
					for(var m=0;m<num_;m++){
						html+='<span class="zhanW2"></span>';
					}	
					if(obj.childList != null){
						if(obj.childList.length != 0){
							html+='<span class="san_ico"></span>';
						}
					}
					if(obj.status != null){
					  status=obj.status;
					}
					html+='<a href="javascript:;" a>'+obj.name+'</a>';
				html+='</div>';
			html+='</div>';
			
			html += '<div class=" sortVal">'
				if(obj.sort== null || obj.sort == ''){
					var sort = '';
					html += '<input type="text" onblur="typeSort(this);" data-id=\''+obj.id+'\'  value=\''+sort+'\' class="nub"/>';
				}else{
					html += '<input type="text" onblur="typeSort(this);" data-id=\''+obj.id+'\'  value=\''+obj.sort+'\' class="nub"/>';
				}
			html += '</div>'
			
			html+='<div class="sele04 sanJi">';
				html+='<a href="javascript:void(0);">管理<span class="sanjiao"></span></a>';
				html+='<ul class="guanli_con cf">';
					html+='<li><a href="javascript:void(0)" onclick="edit(\''+obj.id+'\',\''+obj.type+'\')">编辑</a></li>';
					if(status=='1'){
					    html+='<li id="hide_'+obj.id+'"><a onclick="isHidden(\''+obj.id+'\',0)" href="javascript:;">隐藏</a></li>';
					}else{
						html+='<li id="hide_'+obj.id+'"><a onclick="isHidden(\''+obj.id+'\',1)" href="javascript:;">显示</a></li>';
					}
					html+='<li><a href="javascript:;" onclick="deleteById(\''+obj.id+'\')">删除</a></li>';
				html+='</ul>';
			html+='</div>';
			//html+='<div class="sele04 sanJi other_width">'+obj.id+'</div>';
		html+='</div>';
		if(obj.childList!=null){
			html+='<dl>';
			num_++;
			html+=apeendBody(obj.childList,'',num_);
			html+='</dl>';
		}
	html+='</dd>';
	num_=num;
	});
	return html;
}
function setColum(list,html){
	$.each(list,function(index,obj){
		if(index==list.length-1){
		    html+=obj.columName;
		}else{
			html+=obj.columName+",";
		}
	});
	return html;
}
//排序
function typeSort(param){
	var type=$("#columType").val();
	var zz=/^[1-9][0-9]*$/;
	var sort=$(param).val();
	var str;
	if(type=='1'){//内容栏目
		str=adminPath+"contentType/updateSort.do";
	}else if(type=='2'){//资讯栏目
		str=adminPath+"mymessageType/updateSort.do";
	}else if(type=='3'){//产品栏目
		str=adminPath+"productType/updateSort.do";
	}else if(type=='5'){//下载栏目
		str=adminPath+"downloadType/updateSort.do";
	}
	if(zz.test(sort)){
		$.ajax({
			type: "GET",
			url:str,
			data:{id:$(param).attr("data-id"),sort:$(param).val()},
			success:function(result){
				if(result.success){
				}else{
					window.top.mesageTip("failure","操作失败");
				}
			}
		});
	}else{
		window.top.mesageTip("warn","请输入正整数");
	}
	
}





</script>
</head>
<body class="no-skin">
	<!-- jsp导航返回栏 -->
	<div class="cms_con cf">
	<div class="cms_c_inst neirong cf">
		<div class="left cf">
			<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a><i>></i>
			<!--<a href="javascript:location.href='<%=adminPath%>columcontent_colum/golist?ID=${pd.topColumId }'">栏目列表</a><i>></i>-->
			<i>分类列表</i>
		</div>
	</div>
	<!-- cms_con开始 -->
	<form action="<%=adminPath%>columcontent_colum/managecontentType.do?ID=${pd.columId}&topColumId=${pd.topColumId}" method="post">
		<input type="hidden" id="columId" name="columId" value="${pd.ID}" />
		<input type="hidden" id="topColumId" name="topColumId" value="${pd.topColumId}" />
		<input type="hidden" id="columType" name="columType" value="${columType}"/>	
		
		<div class="cms_c_list biaoge_con chanpin_con cf">
			<div class="h3 cf">
				<div class="h3_left cf">
					<a href="javascript:void(0);" onclick="addContentType('${columType}');">+添加分类</a>
				</div>
				<div class="h3_right cf">
					<a href="<%=adminPath%>columcontent_colum/managecontent.do?ID=${pd.columId}&topColumId=${pd.topColumId}">内容视图</a>
					<a class="cur" href="<%=adminPath%>columcontent_colum/managecontentType.do?ID=${pd.columId}&topColumId=${pd.topColumId}">分类视图</a>
				</div>
			</div>
			<div class="table cf juese_con">
				<dl class="list_bg col_04 cf">
					<dt class="cf">
						<div class="tit4">
							<div class="tit4_con">
								名称
							</div>
						</div>
						<div class="sele04">操作</div>
					<!-- 	<div class="sele04 other_width">分类ID</div> -->
						<div class="sortVal">排序值</div>
					</dt>
					<div id="listPage"></div>
					<!-- 弹窗——推荐设置-->
				</dl>
				<div class="bottom_con cf">
					<div class="page_list cf" id="listPageStr">
						${page.pageStr}		
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="currentId" value=""/>
		</form>
	 </div>
	<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->
</body>
</html>
