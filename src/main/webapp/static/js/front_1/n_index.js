$(function(){
  if($('.resource-type-list').size()>0){
      var $resourceTypeList = $('.resource-type-list');
      $resourceTypeList.infinitescroll({
          navSelector     : "#more",
          nextSelector    : "#more a",
          itemSelector    : ".panel",
          clickb          : false,
          clickobj        : ".load-more-link",
          loading:{
              img: "../images/his_loading.png",
              msgText: '',
              finishedMsg: '没有了',
              finished: function(){
                  $("#infscr-loading").hide();
              }
          }, errorCallback:function(){
             $(".load-more-link").hide();
          }

      }, function(newElements){
          var $newElems = $(newElements);
          $newElems.fadeIn();
          
          return;
      });
  };
  $(document).on('click','.no_left_edit>div.p span i',function(){
    $('.edit_con_original').removeClass('fnt_18');
    $('.edit_con_original').removeClass('fnt_16');
    $('.edit_con_original').removeClass('fnt_14');
    $(this).addClass('hover').siblings().removeClass();
    $('.edit_con_original').addClass($(this).data('size'))
  });
})


// 弹出新窗口打印
/*打印标记*/
function doPrint() {
    bdhtml = window.document.body.innerHTML;
    sprnstr = "<!--startprint-->";
    eprnstr = "<!--endprint-->";

    prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + 17);
    prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
    OpenWindow = window.open("");
    OpenWindow.document.write("<!DOCTYPE HTML><HTML><HEAD><meta http-equiv=\"Content-Type\" content=\"text\/html; charset=utf-8\" \/><TITLE>打印页<\/TITLE><link href=\"../css\/print.css\" rel=\"stylesheet\" type=\"text\/css\" \/><link rel='stylesheet' href='../build/mediaelementplayer.min.css' /><link rel='stylesheet' href='../build/mejs-skins.css' /><\/HEAD><BODY><div id=\"printbox\" class=\"news_cont\" ><\/div><\/BODY><\/HTML>");
    OpenWindow.document.getElementById("printbox").innerHTML = prnhtml;    
    OpenWindow.document.close();
    OpenWindow.print();

}
/*打印区的内容一定要加<!--startprint-->和<!--endprint-->标记*/
//<a href="javascript:;" onClick="doPrint()">打印</a>
