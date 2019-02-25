$(function(){
  //首页导航按钮
  $('.nav_btn').click(function(){
      $('.header').toggleClass('btn_click');
      if($('.header').hasClass('btn_click')){
        $('.nav').fadeIn();
      }else{
        $('.nav').fadeOut();
      }
  });
  if(!IsPC()){
    $(document).on('click','.nav>ul>li>a',function(e){
      //if($(this).next('.e_nav').size()>0){
      if($(this).parent().find('.e_nav .left').size()>0){
        e.preventDefault();
        $('.nav>ul>li').removeClass('hover');
        $(this).parent('li').addClass('hover');
      }
    });
  }
  //内页子导航按钮
  $('.n_nav_btn').click(function(){
      $('.side_nav').toggleClass('btn_click');
      if($('.side_nav').hasClass('btn_click')){
        $('.n_nav_con').fadeIn();
      }else{
        $('.n_nav_con').fadeOut();
      }
  });
  //子导航点击效果
  $('.side_nav dd dd').hover(function(){
    if($(this).find('ul').size()>0){
      $(this).addClass('dd_hover');
    }
  },function(){
    if(!$(this).find('a').hasClass('hover') && $(this).find('ul').size()>0){
      $(this).removeClass('dd_hover');
    }
  });

  //搜索按钮
  $('.header .right .search_btn').click(function(e){
    $(this).parent().find('.search_pad').fadeIn();
    $(this).parent().find('.search_con').fadeIn();
    $('.search_con').addClass('pad_style')
    e.stopPropagation();
  });
  $('.search_con,.search_con input').click(function(e){
    e.stopPropagation();
  });
  $(document).on('click','.search_pad',function(e){
    $(this).fadeOut();
    $(this).parent().find('.search_con').hide();
    $('.search_con').removeClass('pad_style')
    e.stopPropagation();
  });


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
  
})

function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
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
function scrollT($Dom){
    if($(window).scrollTop()+$(window).height()>=$Dom.offset().top+200){
        return true;
    }
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