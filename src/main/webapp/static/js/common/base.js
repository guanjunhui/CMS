
$(function(){
	public_search($('.duo_ser'));
	
    if(!placeholderSupport()){
        $('[placeholder]').focus(function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(function() {
            var input = $(this);
            if (input.val() == '' || input.val() == input.attr('placeholder')) {
                input.addClass('placeholder');
                input.val(input.attr('placeholder'));
            }
        }).blur();
    };
    if($(window).width()>1024){
        $('html').addClass('no_touch')
    }
    if(!IsPC()){$('body').addClass('touch_body')}
    //内页导航缩小效果
    if($('body').hasClass('n_body')){
        $(window).scroll(function(){
            if($(window).scrollTop()>0){
                $('.n_header').addClass('scroll_h')
            }else{
                $('.n_header').removeClass('scroll_h')
            }
        });
    }


    //二级导航
    if(IsPC()){
        $('.nav li').hover(function(){
            $(this).find('.nav_er').stop().fadeIn(200);
        },function(){
            $(this).find('.nav_er').stop().fadeOut(200);
        });
    }else{
        $('.nav>ul>li a').click(function(e){
            if($(this).next('.nav_er').css('display')=='none'){
                $(this).next('.nav_er').stop().fadeIn(200);
                e.preventDefault();
            }else{
                $(this).next('.nav_er').stop().fadeOut(200);
            }
        }); 
    }
    
    //导航按钮
    $('.nav_btn').click(function(){
        $('.header03').toggleClass('btn_click');
        if($('.header03').hasClass('btn_click')){
            $('.nav_er').hide();
        }
    });
    //首页移动端导航
    $(window).resize(function(){
        //针对ipadPro设置样式
        if($(window).height()>=1024 && $(window).width()<=1300){
            $('body').removeClass('ipadPro1024');
            $('body').addClass('ipadPro1366');
        }else if($('.touch_body').size()>0 && $(window).width()>1300){
            $('body').removeClass('ipadPro1366');
            $('body').addClass('ipadPro1024');
        }

    }).resize();

    // 获取当前url
    var url_location = window.location.pathname;

    var arr = [];

    arr = url_location.split('/');

    

})

function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
}
function scrollT($Dom){
    if($(window).scrollTop()+$(window).height()>=$Dom.offset().top+200){
        return true;
    }
}
function mainBgResize1($img, width, height, W_b, H_b) {

    var sw = width,
        sh = width / W_b * H_b
    if (sh < height) {
        sh = height;
        sw = height / H_b * W_b
    }
    $img.css({height:sh,width:sw, marginTop: -(sh - height) / 2, marginLeft: -(sw - width) / 2, 'visibility': 'visible' });

}
//判断是否Firefox浏览器
function isFirefox(){
    if (navigator.userAgent.indexOf("Firefox") > -1) 
    return true;
    else
    return false;
}
//判断是否IE浏览器
function isIE() {
    if (!!window.ActiveXObject || "ActiveXObject" in window)
    return true;
    else
    return false;
}
function IsPC() { 
   var userAgentInfo = navigator.userAgent; 
   var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"); 
   var flag = true; 
   for (var v = 0; v < Agents.length; v++) { 
       if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; } 
   } 
   return flag; 
}


//内容栏目下拉


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