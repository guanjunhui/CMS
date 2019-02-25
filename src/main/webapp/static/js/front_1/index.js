$(function(){
//banner
  var sync1 = $("#sync1");
  var sync2 = $("#sync2");

  sync1.owlCarousel({
    autoPlay:true,
    singleItem : true,
    slideSpeed : 1000,
    navigation: true,
    pagination:false,
    afterAction : syncPosition,
    responsiveRefreshRate : 200,
    navigationText:["<",">"],
    afterInit:function(el){
      // el.find(".owl-item").each(function(){
      //   var mLeft=($(this).find('img').width()-$(window).width())/2
      //   $(this).find('img').css({
      //     'marginLeft':-mLeft
      //   });
      // });
    	$('.notice_con').show();
    }
  });

  sync2.owlCarousel({
    items : 4,
    pagination:false,
    responsiveRefreshRate : 110,
    afterInit : function(el){
      el.find(".owl-item").eq(0).addClass("synced");
    }
  });

  function syncPosition(el){
    var current = this.currentItem;
    $("#sync2")
      .find(".owl-item")
      .removeClass("synced")
      .eq(current)
      .addClass("synced")
    if($("#sync2").data("owlCarousel") !== undefined){
      center(current)
    }

  }

  $("#sync2").on("click", ".owl-item", function(e){
    e.preventDefault();
    var number = $(this).data("owlItem");
    sync1.trigger("owl.goTo",number);
  });

  function center(number){
    var sync2visible = sync2.data("owlCarousel").owl.visibleItems;

    var num = number;
    var found = false;
    for(var i in sync2visible){
      if(num === sync2visible[i]){
        var found = true;
      }
    }

    if(found===false){
      if(num>sync2visible[sync2visible.length-1]){
        sync2.trigger("owl.goTo", num - sync2visible.length+2)
      }else{
        if(num - 1 === -1){
          num = 0;
        }
        sync2.trigger("owl.goTo", num);
      }
    } else if(num === sync2visible[sync2visible.length-1]){
      sync2.trigger("owl.goTo", sync2visible[1])
    } else if(num === sync2visible[0]){
      sync2.trigger("owl.goTo", num-1)
    }
  }

//banner结束
  //新闻公告
  if($('.not_bxslider').find('dd').size()>3 && $(window).width()>1269){
    $('.not_bxslider').bxSlider({
      'mode':'vertical',
      'auto':true,
      'pager':false,
      'controls':false,
      'minSlides': 3,
      'moveSlides': 1,
      'slideMargin': 15,
      'autoHover':true,
      'pause':2000
    });
  }else if($('.not_bxslider').find('dd').size()>1 && $(window).width()<=1269){
    $('.not_bxslider').bxSlider({
      'mode':'vertical',
      'auto':true,
      'pager':false,
      'controls':false,
      'minSlides': 1,
      'moveSlides': 1,
      'slideMargin': 0,
      'autoHover':true,
      'adaptiveHeight':true,
      'pause':2000
    });
  }

  bank_1($('.link_bxslider'),4,20);

  
})

function bank_1(obj,numb,m_right) {
    obj.each(function (index, element) {
      var obj = $(this)
      var num = numb;
      obj.find(".bx-clone").remove()
      obj.find("ul li,ul").attr({ "style": "" })
      var ulhtml = obj.find("ul")
      obj.remove(".bx-wrapper");
      obj.html(ulhtml);
      if($(window).width()<1280) num=3;
      if($(window).width()<768) num=2;
      var xl = obj.width() / num;
      if (obj.find("ul li").size() > num) {
          slider=obj.find("ul").bxSlider({
              infiniteLoop: true,
              auto: true,
              mode: 'horizontal',
              minSlides: num,
              maxSlides: num,
              moveSlides: 1,
              slideWidth: xl,
              pager:false,
              controls:false,
              slideMargin:m_right,
              'pause':2000
          });
      }else{
        slider=obj.find("ul").bxSlider({
              infiniteLoop: true,
              auto: false,
              mode: 'horizontal',
              minSlides: num,
              maxSlides: num,
              moveSlides: 1,
              slideWidth: xl,
              pager:false,
              controls:false,
              slideMargin:m_right,
              'pause':2000
          });
      }

    });
} 