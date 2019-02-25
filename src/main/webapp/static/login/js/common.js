$(function(){
	var container = $('.container-fluid-full');
	var wrapper = $('.wrapper');
	var conWrapper = $('.con-wrapper');
	

	$(window).resize(function(){
		// 页面高度适应浏览器尺寸，保持为一屏
		container.height($(window).height());

		// 中间内容区高度
		wrapper.height($(window).height()-90);

		conWrapper.height($(window).height()-90)

	}).resize();


	
})