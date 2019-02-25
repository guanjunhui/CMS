Swiper.prototype.plugins.fade = function(swiper,params) {
	var slides, wrapperSize, slideSize, initialized;
	var isH = swiper.params.mode == 'horizontal';
	if(!params) return;
	/*=========================
	  Default Parameters
	  ===========================*/
	var defaults = {

	}
	params = params || {};	
	for (var prop in defaults) {
		if (! (prop in params)) {
			params[prop] = defaults[prop]	
		}
	}
	function checkTransForm() {
		'use strict';
		var el = document.body;
		var es = el.style;
		var transform = ['webkitTransform','MsTransform','msTransform','MozTransform','OTransform','transform'];
        for(var i=0; i<transform.length; i++ ){
			var checkItem = transform[i];
			if (es[checkItem] !== undefined) {
				return checkItem;
			}
		}
		return false;
	}
    function init() {
		initialized = true;
		slides = swiper.slides
		var slideMaxWidth = 0;
		var slideMaxHeight = 0;

		for (var i=0; i<slides.length; i++) {
			swiper.setTransition(slides[i], 0)
			var slideWidth = swiper.h.getWidth(slides[i]);
			var slideHeight = swiper.h.getHeight(slides[i]);
			if (slideWidth >= slideMaxWidth) {
				slideMaxWidth = slideWidth;
			}
			if (slideHeight >= slideMaxHeight) {
				slideMaxHeight = slideHeight;
			}
		}
		swiper.wrapper.style.height = slideMaxHeight + 'px';
		swiper.container.style.height = slideMaxHeight + 'px';
		swiper.wrapper.style.width = slideMaxWidth + 'px';
		swiper.container.style.width = slideMaxWidth + 'px';
    }
    function fadeSlide (transform) {
		var transform = transform || {x:0, y:0, z:0};
		for (var i=0; i<swiper.slides.length; i++) {
			var translateY = isH ? 0 : -transform.y
			var translateX = isH ? -transform.x : 0;
			if (checkTransForm()) {
				var slideTransform = 'translate3d('+translateX+'px,'+translateY+'px,'+0+'px)';
				swiper.setTransform(swiper.slides[i], slideTransform);
			} else {
				swiper.slides[i].style.left = translateX + 'px';
				swiper.slides[i].style.top = translateY + 'px';
			}
		}
    }
    var hooks = {
        onFirstInit : function(args) {
			init();
			var transform = {x:swiper.getWrapperTranslate('x'), y:swiper.getWrapperTranslate('y'), z:swiper.getWrapperTranslate('z')};
			fadeSlide(transform);
        },
        onInit : function(args) {
			init();
			var transform = {x:swiper.getWrapperTranslate('x'), y:swiper.getWrapperTranslate('y'), z:swiper.getWrapperTranslate('z')};
			fadeSlide(transform);
        },
        onSetWrapperTransform: function(transform) {
			fadeSlide(transform)
        },
        onSetWrapperTransition: function(args) {
            for (var i=0; i<swiper.slides.length; i++) {
                swiper.setTransition(swiper.slides[i], args.duration)
            }
        }
    }
    return hooks;
}