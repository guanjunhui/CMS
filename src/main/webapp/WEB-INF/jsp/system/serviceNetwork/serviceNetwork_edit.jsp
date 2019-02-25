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
	String imgPath = path+"/../uploadFiles/uploadImgs/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<script type="text/javascript" src="http://ditu.google.cn/maps/api/js?sensor=false&amp;language=en&amp;key=AIzaSyAyXwq79dCk3XgeYR0eu8B6PzoCdA_jP3k"></script>
<base href="<%=basePath%>">
<%@ include file="../../system/index/n_top.jsp"%>
	<script>
		$(function(){
			imgPath = <%=imgPath%>;
		})
	</script>
<!-- jsp文件头和头部 -->
<script type="text/javascript">
	$(function(){
		$('.layer_btn01').click(function(){
		  	$('.layer_bg08').show();
	    });
		$('.layer_btn02').click(function(){
		  	$('.layer_bg02').show();
	    });
		//开始
		selectContinent();//获取服务网点所属大洲
		selectCountry();//获取代理商所属国家(回显用)
		/* selectServiceNature();//获取服务商性质 */
	});
	/*  */
//开始
//获取服务商性质
function selectServiceNature(){
	$.ajax({
		type: "GET",
		url:adminPath+"serviceNetwork/selectServiceNature.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result!=null){
				 appendServiceNature(result);
			 } 
		}
	});
}
function appendServiceNature(list){
	var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">sorry,no data!</div>';
		 $("#continent").html(html);
		 return false;
	 }
	 html += "<option selected disabled>请选择：</option>";
	 $.each(list,function(index,obj){
	     html+='<option value="'+obj.serviceNatureId+'">'+obj.serviceNatureName+'</option>';
	 });
	 $("#nature").html(html);
	 $("#nature option[value='${serviceNetwork.serviceNatureId}']").attr("selected","selected");
}
//获取代理商所属大洲
function selectContinent(){
	$.ajax({
		type: "GET",
		url:adminPath+"serviceNetwork/selectCountry.do",
		data:{},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result!=null){
				 appendContinent(result);
			 } 
		}
	});
}
//获取代理商所属大洲（添加大洲）
function appendContinent(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#continent").html(html);
		 return false;
	 }
	 html += "<option selected disabled>请选择：</option>";
	 $.each(list,function(index,obj){
 	     html+='<option value="'+obj.continentId+'">'+obj.continentEN+'</option>';
	 });
	 $("#continent").html(html);
	 //回显大洲
	 $("#continent option[value='${serviceNetwork.continentId}']").attr("selected","selected");
}

//获取代理商所属国家（回显用）
function selectCountry(){
	$.ajax({
		type: "GET",
		url:adminPath+"serviceNetwork/selectCountry.do",
		data:{"continentId":"${serviceNetwork.continentId}"},
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result!=null){
				 appendCountryLook(result);
			 } 
		}
	});
}
//获取代理商所属国家（添加国家）（回显用）
function appendCountryLook(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#country").html(html);
		 return false;
	 }
	 html += "<option selected disabled>请选择：</option>";
	 $.each(list,function(index,obj){
 	     html+='<option value="'+obj.countryId+'">'+obj.countryEN+'</option>';
	 });
	 $("#country").html(html);
	//回显国家
	 $("#country option[value='${serviceNetwork.countryId}']").attr("selected","selected");
}
//大洲—国家二级联动
$(function(){
	$('#continent').change(function(){
		var continentId = $("#continent").val();
		$.ajax({
			type: "GET",
			url:adminPath+"serviceNetwork/selectCountry.do",
			data:{"continentId":continentId},
			dataType:'json',
			cache: false,
			success: function(result){
				 if(result!=null){
					 appendCountry(result);
				 } 
			}
		});
	});
});
//大洲—国家二级联动（添加国家）
function appendCountry(list){
	 var html='';
	 if(list.length==0){
		 html='<div style="width:100%;text-align:center;height:50px;line-height:50px;font-size:16px;margin-top:50px;">对不起,没有相关数据</div>';
		 $("#country").html(html);
		 return false;
	 }
	 html += "<option selected disabled>请选择：</option>";
	 $.each(list,function(index,obj){
		/*  if(obj.is_default=='1'){
			 html+='<option selected="selected" value="'+obj.continentId+'">'+obj.continentEN+'</option>';
		 } */
 	     html+='<option value="'+obj.countryId+'">'+obj.countryEN+'</option>';
	 });
	 $("#country").html(html);
}

//保存
function save(type){
	if(!$("#Form").valid()){
		return false;
	} 
 	var longitude = $("#longitude").val();
	if(longitude.length == 0) {
		alert("请在地图上选择服务商位置！");
		return false;
	}
	$("#is_add").val(type);
	$("#Form").submit();
}
//结束
</script>
</head>
<body>

	<!-- cms_con开始 -->
	<div class="cms_con cf">
		<div class="cms_c_inst neirong cf">
			<div class="left cf">
				<a href="javascript:top.location.href='<%=adminPath%>index.do'">首页</a>
				<i>></i> <a href="<%=adminPath%>serviceNetwork/serviceNetworkListPage.do">服务网点管理</a>
				<i>></i> <i>修改服务网点</i>
			</div>
		</div>

			
			<!-- <p class="coordinate"></p> -->
		<!-- 开始 -->
		<div class="cms_c_list cf">
			<h3>修改服务网点</h3>
			<input type="text" id="searchAddress"><input type="button" onclick="searchAddress()" value="SEARCH">
			<div class="content-box outer-box">
				<div class="inner-wrap">
					<div id="map_canvas" class="map-box" style="height:500px;"></div>
				</div>
			</div>
			<br/>
			<form action="<%=adminPath%>serviceNetwork/updateServiceNetwork.do" id="Form" method="post" >
				<input type="hidden" id="is_add" name="is_add" />
				<input type="hidden" id="id" name="id" value="${serviceNetwork.id}">
				<div class="add_btn_con wrap cf">
					<!-- 服务网点名称 -->
					<div class="zhaopin zp_con01 cf">
						<dl class="cf zp_dl">
							<dt><span class="hot">*</span>名称</dt>
							<dd>
								<div class="dd_con">
									<input type="text" id="name" name="name" value="${serviceNetwork.name}" required>
									<input type="hidden" id="longitude" name="longitude" value="${serviceNetwork.longitude}" required>
									<input type="hidden" id="latitude" name="latitude" value="${serviceNetwork.latitude}" required>
								</div>
							</dd>
						</dl>
					</div>
					<!-- 所属大洲 -->
					<!-- <dl class="cf">
						<dt><span class="hot">*</span>所属国家</dt>
						<dd>
							<div class="dd_con">
								<select id="continent" name="continentId" class="form-control" style="width: 200px;"></select>
							</div>
						</dd>
						
					</dl> -->
					<input type="text" id="countryhide" value="${serviceNetwork.countryEN}" style="display: none">
					<dl class="cf">
						<dt><span class="hot">*</span>所属国家</dt>
						<dd>
							<div class="dd_con">
								<select id="countryId" name="countryEN" value="${serviceNetwork.countryEN}"class="form-control country" style="width: 200px;"></select>
							</div>
						</dd>
						
					</dl>
					<!-- 所属国家 -->
					<!-- <dl class="cf">
						<dt><span class="hot">*</span>所属国家</dt>
						<dd>
							<div class="dd_con">
								<select id="country" name="countryId" class="form-control" style="width: 200px;">
									<option selected="selected" disabled="disabled">请选择：</option>
								</select>
							</div>
						</dd>
					</dl> -->
					<!-- 服务网点地址 -->
					<dl class="cf zp_dl">
						<dt><span class="hot">*</span>地址</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="address" name="address" value="${serviceNetwork.address}" required>
							</div>
						</dd>
					</dl>
					<%-- <dl class="cf zp_dl">
						<dt>联系人</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="contact" name="Contact" value="${serviceNetwork.contact}">
							</div>
						</dd>
					</dl> --%>
					<dl class="cf zp_dl">
						<dt>电话</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="phone" name="phone" value="${serviceNetwork.phone}">
							</div>
						</dd>
					</dl>
					<!-- 服务网点邮箱 -->
					<%-- <dl class="cf zp_dl">
						<dt><span class="hot">*</span>邮箱</dt>
						<dd>
							<div class="dd_con">
								<input type="text" id="email" name="email" value="${serviceNetwork.email}" required>
							</div>
						</dd>
					</dl> --%>
					<!-- <div class="cf" id="extend"></div> -->
				</div>
				<div class="all_btn cf">
					<span id="result"></span> 
					<input type="button" class="submit_btn" value="修改" onclick="save('0')" /> 
					<a href="<%=adminPath%>/serviceNetwork/serviceNetworkListPage.do" class="submit_re_btn">取消</a>
				</div>
			</form>

		</div>
		<div class="footer">© 中企高呈 版权所有</div>
	</div>
	<!-- cms_con结束 -->
	<script type="text/javascript">
		$.ajax({
	        url:"${basePath}/country/allCountrys",
	        type:"get",
	        dataType:"json",
	        async:false,
	        success:function(data){
	       	 	  if (data.code == 200) {
	       	 		var str = "";
	       	 		var country = $("#countryhide").val();
	       	 		if(country!=" " && country!="undefined"){
	       	 			str += "<option>"+country+"</option>";
	       	 		}
	       	 	  	$(data.data).each(function(){
	       	 	  		str += "<option>"+this+"</option>";
	       	 	  	});
	       	 	  	$(".country").html(str);
	       	 	  }
	        }
	    });
		var longitude = "${serviceNetwork.longitude}";
		var latitude = "${serviceNetwork.latitude}";
		var map = null;
		var marker = null;
		map = new google.maps.Map(document.getElementById("map_canvas"), {  
	        center: new google.maps.LatLng(longitude, latitude),
	        zoom: 5,//区域 深度  
	        mapTypeId: google.maps.MapTypeId.HYBRID  //ROADMAP
	    });
		marker = new google.maps.Marker({  
			position: new google.maps.LatLng(longitude, latitude),  
			map: map  
		});
		
		var geocoder = new google.maps.Geocoder();
		$('#continent').change(function(){
			$("#longitude").val("");
			$("#latitude").val("");
			var continentName = $("#continent option:selected").text();
			geocoder.geocode({ address: continentName }, function geoResults(results, status) {  
		        if (status == google.maps.GeocoderStatus.OK) {
	 	            var str = results[0].geometry.location;
	 	            str = str + "";
		            str = str.replace(" ", "");
		    		str = str.substring(1);
		    		var l = str.length;
		    		str = str.substring(0, l-1);
		    		var ind=str.indexOf(",");
		    		var s1 = str.substring(0, ind);
		    		var s2 = str.substring(ind + 1); 
		    		map = new google.maps.Map(document.getElementById("map_canvas"), {  
		    	        center: new google.maps.LatLng(s1, s2),
		    	        zoom: 3,//区域 深度  
		    	        mapTypeId: google.maps.MapTypeId.HYBRID  //ROADMAP
		    	    }); 
	    			//点击地图添加标记
	    			map.addListener('click', function(e) {
			    		//if(flag.length==0) {
			    			//alert("请选择服务商性质！");
			    		//}else {
		    				if(marker!=null) {
		    			    	marker.setMap(null);
		    				}
		    				//alert(e.latLng);
		    				placeMarkerAndPanTo(e.latLng, map);
		    			//}
	    			});
		        }  
		        else {  
		            alert("：error " + status);  
		        }  
		    });  
		});
		//点击地图添加标记
		map.addListener('click', function(e) {
			//if(flag.length==0) {
				//alert("请选择服务商性质！");
			//}else {
				if(marker!=null) {
			    	marker.setMap(null);
				}
				//alert(e.latLng);
				placeMarkerAndPanTo(e.latLng, map);
			//}
		});
		function placeMarkerAndPanTo(latLng, map) {
			//获取经纬度
			var lonlat = latLng + "";
			lonlat = lonlat.replace(" ", "");
			lonlat = lonlat.substring(1);
			var le = lonlat.length;
			lonlat = lonlat.substring(0, le-1);
			var inde = lonlat.indexOf(",");
			var longitude = lonlat.substring(0, inde);
			var latitude = lonlat.substring(inde + 1);
			$("#longitude").val(longitude);
			$("#latitude").val(latitude);
			marker = new google.maps.Marker({ //创建marker对象
		    	position: latLng,
		        map: map
		        //icon: image
		    });
		    map.panTo(latLng); //地图居中到当前坐标
		}
		function searchAddress(){
			var continentName = $("#searchAddress").val();
			geocoder.geocode({ address: continentName }, function geoResults(results, status) {  
		        if (status == google.maps.GeocoderStatus.OK) {
	 	            var str = results[0].geometry.location;
	 	            str = str + "";
		            str = str.replace(" ", "");
		    		str = str.substring(1);
		    		var l = str.length;
		    		str = str.substring(0, l-1);
		    		var ind=str.indexOf(",");
		    		var s1 = str.substring(0, ind);
		    		var s2 = str.substring(ind + 1); 
		    		map = new google.maps.Map(document.getElementById("map_canvas"), {  
		    	        center: new google.maps.LatLng(s1, s2),
		    	        zoom: 10,//区域 深度  
		    	        mapTypeId: google.maps.MapTypeId.HYBRID  //ROADMAP
		    	    }); 
		    		placeMarkerAndPanTo(new google.maps.LatLng(s1, s2), map);
	    			//点击地图添加标记
	    			map.addListener('click', function(e) {
			    		//if(flag.length==0) {
			    			//alert("请选择服务商性质！");
			    		//}else {
		    				if(marker!=null) {
		    			    	marker.setMap(null);
		    				}
		    				//alert(e.latLng);
		    				placeMarkerAndPanTo(e.latLng, map);
		    			//}
	    			});
		        }  
		        else {  
		            alert("：error " + status);  
		        }  
		    }); 
		}
	</script>
</body>
</html>