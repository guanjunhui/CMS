package cn.cebest.controller.system.infineon;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.PmmCommentService;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value="pmmContent")
public class PmmContentController extends BaseController {

	String menuUrl = "pmmContent/list.do"; //菜单地址(权限用)
	
	@Autowired
	private PmmCommentService pmmCommentService;
	
	@RequestMapping("list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = pmmCommentService.getContentList(page);
		mv.setViewName("system/infineonpmm/solutionContent");
		mv.addObject("contentList", data.get("data"));
		mv.addObject("page", data.get("page"));
		return mv;
	}
}
