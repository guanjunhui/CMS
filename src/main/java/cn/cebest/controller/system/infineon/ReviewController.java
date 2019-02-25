package cn.cebest.controller.system.infineon;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.ReviewService;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value="review")
public class ReviewController extends BaseController {

	String menuUrl = "review/list.do"; //菜单地址(权限用)
	
	@Autowired
	private ReviewService reviewService;
	
	@RequestMapping("list")
	public ModelAndView list(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = reviewService.getReviewList(page);
		mv.setViewName("system/infineonpmm/reviewList");
		mv.addObject("list", data.get("data"));
		mv.addObject("page", data.get("page"));
		return mv;
	}
	
}
