var graduationIndex=0;
var scPartnerIndex=0;
var areaData=[];
//添加招生条件
function addGraduationDest(obj){
	$(obj).parent().hide();
	var html="<tr class='imTr'>";
	html+='<td style="width:79px;text-align: right;padding-top: 13px;"></td>';
	html+='<td style="width:40%">';
	var index=graduationIndex++;
	html+='<input style="width:89%" name="graduationDestkey"   id="graduationDestkey'+index+'" value="" type="text" placeholder="学校或国家"  title="学校或国家" style="width:50%;" />';
	html+='<input style="width:10%" name="graduationDestvalue" id="graduationDestvalue'+index+'" value="" type="text" placeholder="人数"  title="人数" style="width:50%;" />';
	html+='</td>';
	html+='<td id="graduation" class="graduation" style="width:79px;text-align:left;padding-top: 13px;border:none">';
	html+='<a class="btn btn-mini btn-primary" onclick="addGraduationDest(this);">添加</a>';
	html+='<a class="btn btn-mini btn-primary" onclick="movGraduationDest(this);">删除</a></td></tr>';
	$(obj).parent().parent().after(html);
}

//毕业去向初始化
function defaultGraduationDest(scGraduationDestfb){
	var jsonArry=eval(scGraduationDestfb);
	if(!jsonArry){
		return;
	}
	var length=jsonArry.length;
	var html;
	for(var i=0;i<length;i++){
		var jsonObj=jsonArry[i];
		if(i==0){
			$("#graduationDestkey").val(jsonObj.key);
			$("#graduationDestvalue").val(jsonObj.value);
			if(length>1){
				$(".graduation").hide();
			}
		}else{
			html="<tr class='imTr'>";
			html+='<td style="width:79px;text-align: right;padding-top: 13px;"></td>';
			html+='<td style="width:40%">';
			var index=graduationIndex++;
			html+='<input style="width:89%" name="graduationDestkey"   id="graduationDestkey'+index+'" value="'+jsonObj.key+'" type="text" placeholder="学校或国家"  title="学校或国家" style="width:50%;" />';
			html+='<input style="width:10%" name="graduationDestvalue" id="graduationDestvalue'+index+'" value="'+jsonObj.value+'" type="text" placeholder="人数"  title="人数" style="width:50%;" />';
			html+='</td>';
			if(i==(length-1)){
				html+='<td id="graduation" class="graduation" style="width:79px;text-align:left;padding-top: 13px;border:none">';
				html+='<a class="btn btn-mini btn-primary" onclick="addGraduationDest(this);">添加</a>';
				html+='<a class="btn btn-mini btn-primary" onclick="movGraduationDest(this);">删除</a></td>';
			}
			html+='</tr>';
		}
	}
	$(".imTr").after(html);
}

//合作院校初始化
function defaultscPartner(scPartner){
	var jsonArry=eval(scPartner);
	if(!jsonArry){
		return;
	}
	var length=jsonArry.length;
	var html;
	for(var i=0;i<length;i++){
		var jsonObj=jsonArry[i];
		if(i==0){
			$("#scPartnerName").val(jsonObj.key);
			//$("#scPartnerImgFile").val(jsonObj.value);
			if(length>1){
				$(".scPartner").hide();
			}
		}else{
			html="<tr class='scPartnerTr'>";
			html+='<td style="width:79px;text-align: right;padding-top: 13px;"></td>';
			var index=scPartnerIndex++;
			html+='<td><input name="scPartnerName" id="scPartnerName'+index+'" value="'+jsonObj.key+'" type="text" placeholder="合作院校名称"  title="合作院校名称" style="width:100%;" /></td>';
			html+='<td style="width:79px;text-align: right;padding-top: 13px;">合作院校照片:</td>';
			html+='<td style="width:20%;">';
			html+='<input type="file" name="scPartnerImgFile" id="scPartnerImg'+index+'" maxlength="255" placeholder="选择要上传的照片" title="选择合作院校照片" style="width:100%;"/></td>';
			if(i==(length-1)){
				html+='<td id="scPartner" class="scPartner"  style="width:180px;text-align: left;padding-top: 13px;border:none">';
				html+='<a class="btn btn-mini btn-primary" onclick="addscPartner(this);">添加</a>';
				html+='<a class="btn btn-mini btn-primary" onclick="movscPartner(this);">删除</a></td>';
			}
			html+='</tr>';
		}
	}
	$(".scPartnerTr").after(html);
}



function movGraduationDest(obj){
	$(obj).parents('.imTr').prev().find(".graduation").show();
	$(obj).parent().parent().remove();
}
//上传学校照片
function addPic(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="添加照片";
	 diag.URL = adminPath+'schoolmanage/goAddFile.do';
	 diag.Width = 800;
	 diag.Height = 490;
	 diag.CancelEvent = function(){//关闭事件
		var fileId=diag.innerFrame.contentWindow.document.getElementById('fileId').value;
		if(fileId!=''){
			var ids=$("#scImgPath").val();
			$("#scImgPath").val(ids+fileId);
		}
		diag.close();
	 };
	 diag.show();
}

//上传学校视频
function addVedio(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="上传视频";
	 diag.URL = adminPath+'schoolmanage/goAddVideo.do';
	 diag.Width = 800;
	 diag.Height = 490;
	 diag.CancelEvent = function(){
		var videoId=diag.innerFrame.contentWindow.document.getElementById('videoId').value;
		if(videoId!=''){
			var ids=$("#scMediaPath").val();
			$("#scMediaPath").val(ids+videoId);
		}
		//关闭事件
		diag.close();
	 };
	 diag.show();
}

//编辑学校描述
function editSchoolDes(Id){
	 var scDes=$("#scDes").val();
	 if(Id==""&&scDes!=""){
		 Id=scDes;
	 }
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="编辑学校介绍";
	 diag.URL = adminPath+'schoolmanage/goschooldes.do?ID='+Id;
	 diag.Width = 800;
	 diag.Height = 600;
	 diag.Modal = true;				//有无遮罩窗口
	 diag.ShowMaxButton = true;	//最大化按钮
     diag.ShowMinButton = true;		//最小化按钮 
	 diag.CancelEvent = function(){ //关闭事件
		var txtID=diag.innerFrame.contentWindow.document.getElementById('ID').value;
		$("#scDes").val(txtID);
		diag.close();
	 };
	 diag.show();
}

//根据学校名称定位学校地址
function PositioningSchool(){
	 var scSite=$("#scSite").val();
	 /**if(!scSite){
	     $("#scSitetip").tips({
			 side:3,
	         msg:'请填写学校地址',
	         bg:'#AE81FF',
	         time:2
         });
	     $("#scSite").focus();
	     return false;
	 }*/
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="定位学校地址";
	 diag.URL = adminPath+'schoolmanage/goschoolposi.do?scSite='+scSite;
	 diag.Width = 1000;
	 diag.Height = 800;
	 diag.Modal = true;				//有无遮罩窗口
	 diag. ShowMaxButton = true;	//最大化按钮
     diag.ShowMinButton = true;		//最小化按钮 
	 diag.CancelEvent = function(){ //关闭事件
		var position=diag.innerFrame.contentWindow.document.getElementById('position').value;
		var site=diag.innerFrame.contentWindow.document.getElementById('suggestId').value;
		if(position){
			$("#scPoint").val(position);
		}
		if(site){
			$("#scSite").val(site);
		}
		diag.close();
	 };
	 diag.show();
}


//添加合作院校
function addscPartner(obj){
	$(obj).parent().hide();
	var html="<tr class='scPartnerTr'>";
	html+='<td style="width:79px;text-align: right;padding-top: 13px;"></td>';
	var index=scPartnerIndex++;
	html+='<td><input name="scPartnerName" id="scPartnerName'+index+'" value="" type="text" placeholder="合作院校名称"  title="合作院校名称" style="width:100%;" /></td>';
	html+='<td style="width:79px;text-align: right;padding-top: 13px;">合作院校照片:</td>';
	html+='<td style="width:20%;">';
	html+='<input type="file" name="scPartnerImgFile" id="scPartnerImg'+index+'" maxlength="255" placeholder="选择要上传的照片" title="选择合作院校照片" style="width:100%;"/>';
	html+='</td><td id="scPartner" class="scPartner"  style="width:180px;text-align: left;padding-top: 13px;border:none">';
	html+='<a class="btn btn-mini btn-primary" onclick="addscPartner(this);">添加</a>';
	html+='<a class="btn btn-mini btn-primary" onclick="movscPartner(this);">删除</a></td></tr>';
	$(obj).parent().parent().after(html);
}
//删除合作院校
function movscPartner(obj){
	$(obj).parents('.scPartnerTr').prev().find(".scPartner").show();
	$(obj).parent().parent().remove();
}

//select多选
function chose_mult_set_ini(select, values) {
    var arr = values.split(',');
    var length = arr.length;
    var value = '';
    for (i = 0; i < length; i++) {
        value = arr[i];
        $(select + " option[value='" + value + "']").attr('selected', 'selected');
    }
    $(select).trigger("chosen:updated");
}

//保存
function save(){
	if($("#scNameCn").val()==""){
		$("#scNameCntip").tips({
			side:3,
            msg:'请填写学校名称',
            bg:'#AE81FF',
            time:2
        });
		$("#scNameCn").focus();
		return false;
	}

	if($("#scSite").val()==""||$("#scPoint").val()==""){
		$("#scSitetip").tips({
			side:3,
            msg:'请填写学校地址',
            bg:'#AE81FF',
            time:2
        });
		$("#scSite").focus();
		return false;
	}

	if($("#scDes").val()==""){
		$("#scDestip").tips({
			side:3,
            msg:'请编辑学校介绍',
            bg:'#AE81FF',
            time:2
        });
		$("#scDes").focus();
		return false;
	}
	var scArea="";
	var province=$("#province").val();
	if(province!=""){
		scArea=province;
	}
	var city=$("#city").val();
	if(city!=""){
		scArea+=","+city;
	}
	var area=$("#area").val();
	if(area!=""){
		scArea+=","+area;
	}
	$("#scArea").val(scArea);
	$("#schoolForm").submit();
	$("#zhongxin").hide();
	$("#zhongxin2").show();
}

//获取地区
function getArea(){
	$.ajax({
		type: "GET",
		url: adminPath+"dictionaries/getallchild?code=003",
		dataType:'json',
		cache: false,
		success: function(result){
			 if(result.code==200){
				 areaData=result.data;
				 $.each(result.data, function(i, tree){
					 if(tree.pid==""){
				         var html='<option value="'+tree.id +'">'+tree.name+'</option>';
						 $("#province").append(html);
					 }
				 });
	            $("#province").trigger("chosen:updated");
	            setDefaultArea();
			 }
		}
	});
}
//地区初始化
function setDefaultArea(){
	 var areaId=$("#scArea").val();
	 var areaArry=[];
	 if(!areaId){
		 return;
	 }
	 areaArry=areaId.split(",");
	 var province="";
	 var city="";
	 var area="";
	 if(areaArry.length>0){
		 if(areaArry.length==3){
			 province=areaArry[0];
			 city=areaArry[1];
			 area=areaArry[2];
		 }else if(areaArry.length==2){
			 province=areaArry[0];
			 city=areaArry[1];
		 }else if(areaArry.length==1){
			 province=areaArry[0];
		 }
	 }
	 $.each(areaData, function(i, tree){
		 if(tree.id==province){
			 $("#province").val(tree.id);
			 $("#province").trigger("chosen:updated");
		 }
		 if(tree.id==city){
	         var html='<option value="'+tree.id +'">'+tree.name+'</option>';
			 $("#city").append(html);
			 $("#city").val(tree.id);
	         $("#city").trigger("chosen:updated");
		 }
		 if(tree.id==area){
	         var html='<option value="'+tree.id +'">'+tree.name+'</option>';
			 $("#area").append(html);
			 $("#area").val(tree.id);
	         $("#area").trigger("chosen:updated");
		 }

	 });
}



$(function(){
	
	//学校星级设置
	$('.star').on('click',function(){
		if($(this).hasClass('fa-star-o')){
			$(this).removeClass('fa-star-o').addClass('fa-star');
		}else{
			$(this).removeClass('fa-star').addClass('fa-star-o');
		}
		var star=0;
		$(this).parent().children("i").each(function(i){
			if($(this).hasClass('fa-star')){
				star++;
			}
		});
		$("#scStarLevel").val(star);
	});
	//课程选中
	var scCoursefb=$("#scCoursefb").val();
	chose_mult_set_ini('#scCourse', scCoursefb);
	//留学国家选中
	var scCountryfb=$("#scCountryfb").val();
	chose_mult_set_ini('#scCountry', scCountryfb);
	//学校星级默认选中
	var starCount=$("#scStarLevel").val();
	var count=parseInt(starCount);
	$("#stardiv").children("i").each(function(i){
		if(i<count){
			$(this).removeClass('fa-star-o').addClass('fa-star')
		}
	});
	getArea();
	$('#province').on('change', function(e, params) {
		 var id=params.selected;
		 $("#city").empty();
         var html='<option value=""></option>';
		 $("#city").append(html);
		 $("#area").empty();
		 $.each(areaData, function(i, tree){
			 if(tree.pid==id && $("#city").val()!=tree.id){
		         var html='<option value="'+tree.id +'">'+tree.name+'</option>';
				 $("#city").append(html);
			 }
		 });
        $("#city").trigger("chosen:updated");
        $("#area").trigger("chosen:updated");

	});
	$('#city').on('change', function(e, params) {
	  	 var id=params.selected;
		 $("#area").empty();
         var html='<option value=""></option>';
		 $("#area").append(html);
		 $.each(areaData, function(i, tree){
			 if(tree.pid==id && $("#area").val()!=tree.id){
		         var html='<option value="'+tree.id +'">'+tree.name+'</option>';
				 $("#area").append(html);
			 }
		 });
         $("#area").trigger("chosen:updated");
	});

});




