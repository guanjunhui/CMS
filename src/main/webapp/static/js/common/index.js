
$(function(){
var exts =["JPG","PNG"];
	$('.con_header .header_left').click(function(){
		$(this).toggleClass('header_left_show');
		/*$(".mScrol2").mCustomScrollbar("update");*/

	});
	$('.con_header .header_right li.last-li').click(function(){
		$(this).toggleClass('last_li_show')
	});
	$('table.ev tbody tr:nth-child(2n)').addClass('backgrond_color');
	$('table.od tbody tr:nth-child(2n-1)').addClass('backgrond_color');
	$('.c_list_con dl:nth-child(2n-1)').addClass('backgrond_color');

	

	//滚动条
	/*$(".mScrol").mCustomScrollbar({
	    theme:"rounded-dots"
	});*/
	/*$(".mScrol2").mCustomScrollbar({
	    theme:"rounded-dots"
	});*/
  //自定义多选按钮
  $(document).on('change','.layer_list li label',function(e){
    if($(this).parents('ul').hasClass('radio_lock')){
      $('.layer_list li i').removeClass('active');
      $(this).find('i').addClass('active');
    }else{
      $('.layer_list li label').removeClass('radio_btn');
      $(this).addClass('radio_btn');
      $(this).find('i').toggleClass('active');
    }    
    if(!$(this).parent('li').find('dl').size()==0){
      if($(this).find('input').prop("checked")==true){
        $(this).parents('li').find('dl input').prop("checked",true);
        $(this).parents('li').find('dl i').addClass('active');
      }
    }
  	e.stopPropagation();
  });
  //全选按钮
  $(document).on('change','.all_checkbox input',function(){
    $(this).parent().find('i').toggleClass('active');
    if(this.checked){   
      $(".layer_bg07 .layer_list li :checkbox").prop("checked", true);
      $(".layer_bg07 .layer_list li label i").addClass('active');
      $(".dd_tit label :checkbox").prop("checked", true);
      $(".dd_tit label i").addClass('active');
    }else{   
      $(".layer_bg07 .layer_list li :checkbox").prop("checked", false);
      $(".layer_bg07 .layer_list li label i").removeClass('active')
      $(".dd_tit label :checkbox").prop("checked", false);
      $(".dd_tit label i").removeClass('active');
    }   

  });
  //设置全选复选框
  $(".layer_bg07 .layer_list li :checkbox").click(function(){
    allchk();
  });

  function allchk(){

    var chknum = $(".layer_bg07 .layer_list li :checkbox").size();//选项总个数
    var chk = 0;
    $(".layer_bg07 .layer_list li :checkbox").each(function () {  
      if($(this).prop("checked")==true){
        chk++;
      }
    });
    if(chknum==chk){//全选
      $(".all_checkbox i").addClass('active');
      $(".all_checkbox input").prop("checked",true);
    }else{//不全选
      $(".all_checkbox i").removeClass('active')
      $(".all_checkbox input").prop("checked",false);
    }

  }
  //全选按钮结束

  //外部链接 长期有效
  $(document).on('change','.zp_con01 label',function(){
    $(this).find('i').toggleClass('active');
    $(this).parents('.dd_con').find('.sle_data').toggleClass('open_close');
    if($(this).find('i').hasClass('active')){
    	if($(this).parents('.dd_con').find('.disable').size()>0){
    		$(this).parents('.dd_con').find('.disable').show();
    	}else{
    		$(this).next().show();
    	}
    }else{
    	if($(this).parents('.dd_con').find('.disable').size()>0){
    		$(this).parents('.dd_con').find('.disable').hide();
    	}else{
    		$(this).next().hide();
    	}
    }
  });
  //单选按钮
  $(document).on('change','.zhaopin .add_line label',function(){
    $(this).parent().find('i').removeClass('active')
    $(this).find('i').addClass('active');
  });
  //显示与隐藏
  $(document).on('change','.show_hide label',function(){
    $(this).addClass('active').siblings().removeClass('active');
  });
  //热门与推荐
  $(document).on('change','.hot_re label',function(){
    $(this).toggleClass('active');
  });

  $(document).on('click','.close',function(){
    //$('.layer_list li label').removeClass('radio_btn');
  	//$('.layer_bg').find('input').removeAttr("checked");
  	//$('.layer_bg').find('i').removeClass('active');
  	$(this).parents('.layer_bg').hide();
  });
  /*$(document).on('click','.submit_re_btn',function(){
	    //$('.layer_list li label').removeClass('radio_btn');
	  	//$('.layer_bg').find('input').removeAttr("checked");
	  	//$('.layer_bg').find('i').removeClass('active');
	  	$(this).parents('.layer_bg').hide();
	  });*/
  
  $(document).on('click','.confirm_re_btn',function(){
  	$(this).parents('.layer_bg').hide();
  });
  
  $('.layer_btn01').click(function(){
  	$('.layer_bg01').show();
  	/*$(".mScrol").mCustomScrollbar("update");*/
  });
  function testExt(str){
	  if(exts.toString().indexOf(str)!=-1){
		  return true;
	  }
	  return false;
  }
  function flieValidate(param){
		if($(param)[0].files[0].size>3145728){
			window.top.mesageTip("warn","上传文件大小不能大于3M");
			return false;
		}
		return true;
	}
  //上传文件  文件名 与 删除
  $(document).on("change",".file_upload_img input[type='file']",function(){
    var filePath=$(this).val();
    var str=filePath.substring(filePath.lastIndexOf(".")+1).toUpperCase();
    if(testExt(str)&&flieValidate(this)){
        var arr=filePath.split('\\');
        var fileName=arr[arr.length-1];
        $(this).parents('.file_upload_img').find('i').html(fileName);
        $(this).parents('.file_upload_img').find('.remove_file').show();
        $(this).parents('.file_upload_img').find('.yulan').show();
    }else{
        $(this).parents('.file_upload_img').find('i').html("您上传文件类型有误！");
        $(this).parents('.file_upload_img').find('.remove_file').hide();
        $(this).parents('.file_upload_img').find('.yulan').hide();
        return false 
    }
	})
	$(document).on("change",".file_upload_film input[type='file']",function(){
    var filePath=$(this).val();
    if(filePath.indexOf("mp4")!=-1){
        var arr=filePath.split('\\');
        var fileName=arr[arr.length-1];
        $(this).parents('.file_upload_film').find('i').html(fileName);
        $(this).parents('.file_upload_film').find('.remove_file').show();
    }else{
        $(this).parents('.file_upload_film').find('i').html("您上传文件类型有误！");
        $(this).parents('.file_upload_film').find('.remove_file').hide();
        return false 
    }
	})
	$(document).on('click','.remove_file',function(){
		var file = $(this).parent().find("input[type='file']");
		file.after(file.clone().val(''));      
		file.remove(); 
		$(this).parent().find('i').html('未选择文件');
        $(this).parents('.file_upload_img').find('.yulan').hide();
		$(this).hide();
	});
	//输入数字的限制
/*	$(document).on('keyup','.textarea_numb',function(){
   var len = $(this).val().length;
   if(len > 200){
    $(this).val($(this).val().substring(0,200));
   }
   var num = 200 - len;
   $(this).parent().find('.word').text(num);
  });*/


  $(document).on('keyup','.textarea_numb',function(){
	   var len = $(this).val().length;
	   if(len > 2000){
	    $(this).val($(this).val().substring(0,2000));
	   }
	   var num = 2000 - len;
	   $(this).parent().find('.word').text(num);
});

$('.textarea_numb').each(function(){
	$(this).parent().find('p span').eq(2).text('2000')
	var num = 2000 - $(this).val().length;
	  $(this).parent().find('.word').text(num)
});
  //添加图片视频上传功能
	
  /*var classNimg=0;
  $(document).on('click','.layer_btn_addimg',function(){
  	classNimg=classNimg+1;
  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_img"><span>图片</span><label for="file_name_img_'+classNimg+'"><input type="file" id="file_name_img_'+classNimg+'"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text"></p><p><span>链接</span><input type="text"></p></li><li><span>描述</span><h6><textarea class="textarea_numb"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
  	$(this).parents('dl').append(img_list);
  })
  var classNfilm=0;
  $(document).on('click','.layer_btn_addfilm',function(){
  	classNfilm=classNfilm+1;
  	var img_list='<dd><div class="dd_con"><div class="add_img cf"><ul><li class="file_upload file_upload_film"><span>视频</span><label for="file_name_film_'+classNfilm+'"><input type="file" id="file_name_film_'+classNfilm+'"><em>上传</em></label><i>未选择文件</i><a href="javascript:;" class="remove_file" style="display:none;">删除</a></li><li><p><span>标题</span><input type="text"></p><p><span>链接</span><input type="text"></p></li><li><span>描述</span><h6><textarea class="textarea_numb"></textarea><p><span class="word">0</span><span>/</span><span>200</span></p></h6></li></ul><div class="close_add">x</div></div></div></dd>'
  	$(this).parents('dl').append(img_list);
  })
  //关闭图片视频上传功能
  $(document).on('click','.close_add',function(e){
  	$(this).parents('dd').remove();
  	e.stopPropagation();
  });*/

  //表格效果

$('.biaoge_con .table .dd_tit').each(function(index,el){
  if(index%2==0){
    $(this).addClass('other_bg');
  }
});
$(document).on('click','.xiala_btn',function(e){
  $(this).find('.san_ico').toggleClass('sanJ');
  $(this).parents('.dd_tit').nextAll('dl').slideToggle();
  e.stopPropagation();
});
$(document).on('click','.sanJi',function(e){
  if(!$(this).hasClass('san_open')){
    $('.sanJi').removeClass('san_open');
    $('.sanJi .guanli_con').slideUp();
  }
  
  $(this).toggleClass('san_open');
  $(this).find('.guanli_con').slideToggle();
  e.stopPropagation();
});
$(document).on('click','.sanJi ul',function(e){
  e.stopPropagation();
});
$(document).on('click',function(){
	if($('.sanJi').hasClass('san_open')){
	    $('.sanJi').removeClass('san_open');
	    $('.sanJi .guanli_con').slideUp();
	  }
});
//管理内容
//自定义多选按钮
  $(document).on('change','.dd_tit label',function(e){
    $(this).find('i').toggleClass('active');
    e.stopPropagation();
  });
  //全选按钮
  // 写在上面
  //设置全选复选框
  $(document).on('click','.dd_tit label :checkbox',function(e){
	  
	  //下载全部文件列表页点击全选不起效
  //$(".dd_tit label :checkbox").click(function(){
	e.stopPropagation();
    allchk2();
    
  });

  function allchk2(){

    var chknum = $(".dd_tit label :checkbox").size();//选项总个数
    var chk = 0;
    $(".dd_tit label :checkbox").each(function () {  
      if($(this).prop("checked")==true){
        chk++;
      }
    });

    if(chknum==chk){//全选
      $(".all_checkbox i").addClass('active');
      $(".all_checkbox input").prop("checked",true);
    }else{//不全选
      $(".all_checkbox i").removeClass('active')
      $(".all_checkbox input").prop("checked",false);
    }

  }
  //全选按钮结束
  //缩略图效果

    $('.chanpin_con .pro_img').hover(function(){
        $(this).find('.pro_img_big').stop().slideDown();
    },function(){
        $(this).find('.pro_img_big').stop().slideUp();
    });
    $(document).on('click','.layer_bg08 li>label',function(){
    
		$(this).parent().toggleClass('tog_san');
		$(this).parent().find('dl').slideToggle();
		/*$(".mScrol").mCustomScrollbar("update");*/
    });
    
    
    
    //自定义其他
    /*$(".mScrol222").mCustomScrollbar({
        theme:"rounded-dots"
    });
    $(".mScrol2221").mCustomScrollbar({
        theme:"rounded-dots"
    });
    $(".mScrol2222").mCustomScrollbar({
        theme:"rounded-dots"
    });
    $(".mScrol2223").mCustomScrollbar({
        theme:"rounded-dots"
    });
    $(".mScrol2224").mCustomScrollbar({
        theme:"rounded-dots"
    });*/

        
    $(document).on('click','.layer_list_other li .show_btn',function(e){
	    $(this).toggleClass("jia_j");
	    $(this).parent().parent().children('dl').toggle();

	    	 /* $(".mScrol222").mCustomScrollbar("update");
	    	  $(".mScrol2221").mCustomScrollbar("update");
	    	  $(".mScrol2222").mCustomScrollbar("update");
	    	  $(".mScrol2223").mCustomScrollbar("update");
	    	  $(".mScrol2224").mCustomScrollbar("update");*/


	      e.stopPropagation();
    });
    $(document).on('change','.layer_list_other li label',function(e){
    	 if(!$(this).parents('div').hasClass('layer_bg_other')){
    		if($(this).find('input').attr('type')=='checkbox'){
    	    	    $(this).find('i').toggleClass('active');
    	    	}else if($(this).find('input').attr('type')=='radio'){
    	    		$('.layer_list_other li i').removeClass('active');
    	    	    $(this).find('i').addClass('active');
	    	}
    	 }
    });
    
	

})