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
@RequestMapping(value="pmmComment")
public class PmmCommentController extends BaseController {
	String menuUrl = "pmmComment/list.do"; //菜单地址(权限用)
	
	
	@Autowired
	private PmmCommentService pmmCommentService;
	
	@RequestMapping("list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = pmmCommentService.getCommentList(page);
		mv.setViewName("system/infineonpmm/solutionComment");
		mv.addObject("commentList", data.get("data"));
		mv.addObject("page", data.get("page"));
		mv.addObject("resouceId", pd.get("resouceId"));
		return mv;
	}
	
	/**
	 * 根据评论id删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteComment")
	public String deleteById() {
		PageData pd = this.getPageData();
		String ids = (String) pd.get("ids");
		if(!"".equals(ids) && ids != null){
			String[] array = ids.split(",");
			pd.put("array", array);
		}
		pmmCommentService.deleteComment(pd);
		String resouceId = (String) pd.get("resouceId");
		return "redirect:list.do?resouceId="+resouceId;
	}
	
	@RequestMapping("getCommentById")
	public ModelAndView getCommentById() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		Map<String, Object> data = pmmCommentService.getCommentById(pd);
		mv.setViewName("system/infineonpmm/solutionCommentDetail");
		mv.addObject("data", data.get("data"));
		return mv;
	}
}
