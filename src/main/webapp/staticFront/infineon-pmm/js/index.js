
$(function(){
    var wow = new WOW({
      boxClass: 'wow1',
      animateClass: 'fadeInUp',
      offset:       100,          
      mobile:       true,      
      live:         true
    });
    wow.init();
    var wow2 = new WOW({
      boxClass: 'wow2',
      animateClass: 'fadeInLeft',
      offset:       100,          
      mobile:       true,      
      live:         true 
    });
    wow2.init();
    var wow3 = new WOW({
      boxClass: 'wow3',
      animateClass: 'fadeInRight',
      offset:       100,          
      mobile:       true,      
      live:         true 
    });
    wow3.init();
    var wow4 = new WOW({
      boxClass: 'wow4',
      animateClass: 'fadeInDown',
      offset:       100,          
      mobile:       true,      
      live:         true  
    });
    wow4.init();
	var wh=$(window).height();
    $('.header').animate({
        'height':90
    },500)
	
    $('.header .logo').delay(200).animate({
        'opacity':"1",
        'top':0
    },1000)
    $('.header .headeRight').delay(300).animate({
        'opacity':"1",
        'right':0
    },1000)
    $(window).scroll(function(){
        if($(window).width()>767){
            if(scrollT($('.productBox'))){
                $('.productBox .partTil').animate({
                    'opacity':"1",
                    'bottom':0
                },1000,function(){
                    $('.productBox .proSlide').animate({
                        'opacity':"1"
                    },1000,function(){
                        $('.productBox .moreBtn').animate({
                            'opacity':"1",
                            'bottom':0
                        },1000);
                    });
                    $('.productBox .proLeft').delay(500).fadeIn()
                    $('.productBox .proRight').delay(500).fadeIn()
                })
                $('.productBox .partLine').delay(300).animate({
                    'opacity':"1",
                    'bottom':0
                },1000)
            }
        }
        
        if(scrollT($('.SolutionBox'))){
            $('.SolutionBox .partTil').animate({
                'opacity':"1",
                'bottom':0
            },1000)
            $('.SolutionBox .SolutionLeft').delay(100).animate({
                'opacity':"1",
                'left':0
            },1200);
            $('.SolutionBox .SolutionTopBox').delay(100).animate({
                'opacity':"1",
                'top':0
            },1300);
            $('.SolutionBox .SolutionBtm').delay(300).animate({
                'opacity':"1",
                'bottom':0
            },1500);
            $('.SolutionBox .partLine').delay(300).animate({
                'opacity':"1",
                'bottom':0
            },1000)
        }
        if(scrollT($('.PartnerBox'))){
            $('.PartnerBox .partTil').animate({
                'opacity':"1",
                'bottom':0
            },1000,function(){
                $('.PartnerBox .PartnerLi').animate({
                    'opacity':"1",
                    'bottom':0
                },1000)
            })
            $('.PartnerBox .partLine').delay(300).animate({
                'opacity':"1",
                'bottom':0
            },1000)
        }

        if(scrollT($('.newsBox'))){
            $('.newsBox .partTil').animate({
                'opacity':"1",
                'bottom':0
            },1000,function(){

                $('.newsBox .moreBtn').animate({
                    'opacity':"1",
                    'bottom':0
                },1000);
            })
            $('.newsBox .newsLiBox').delay(300).animate({
                'opacity':"1",
                'bottom':0
            },1000);
            $('.newsBox .partLine').delay(300).animate({
                'opacity':"1",
                'bottom':0
            },1000)
        }
        
        if(scrollT($('.partWelfare'))){
        	$(".partWelfare h2").delay(300).animate({
        		'opacity':'1',
        		'bottom':'0'
        	},800);
        	$(".partWelfare ul li").addClass('on');
        }
        
        if(scrollT($('.partApply'))){
        	$(".partApply h2").delay(300).animate({
        		'opacity':'1',
        		'bottom':'0'
        	},800);
      		$(".partApply .line").delay(1000).animate({
        		'width':'50px'
        	},800);
        	$(".partApply .content").delay(1500).animate({
        		'opacity':'1',
        		'left':'0'
        	},800);
        	$(".partApply").delay(2000).animate({
        		'borderColor':'#f2f2f2'
        	},800)
        }
    }).scroll();
	
	//
	$(".header .wapBtn .userBtn>a").click(function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on')
			$(this).siblings().hide();
		}else{
			$(this).addClass('on');
			$(this).siblings().show();
		}
		var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else if (window.event) {
			window.event.cancelBubble = true;
		}
	})
	$(window).click(function(){
		$('.header .wapBtn .userBtn').find('.wapLoginBox').fadeOut();
		$(".header .wapBtn .userBtn>a").removeClass('on')
	});
	
	
	
})
