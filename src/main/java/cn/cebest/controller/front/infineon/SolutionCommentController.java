package cn.cebest.controller.front.infineon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.SolutionComment;
import cn.cebest.entity.system.infineon.SolutionCommentVO;
import cn.cebest.service.web.infineon.SolutionCommentService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 * 评论
 * @author
 *
 */
@RestController
@RequestMapping(value = "/solutionComment")
public class SolutionCommentController extends BaseController {

	@Autowired
	private SolutionCommentService solutionCommentService;
	
	/**
	 * 添加评论
	 * @return
	 */
	@RequestMapping(value = "/synch", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult synch(SolutionCommentVO solution){
		solution.setId(this.get32UUID());
		JsonResult jr = solutionCommentService.synchComment(solution);
		
		return jr;
	}
	
	
	/**
	 * 获取评论树
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/commentList")
	@ResponseBody
	public JsonResult commentList(Page page){
		PageData pd = this.getPageData();
		page.setPd(pd);
		JsonResult jr = solutionCommentService.getCommentList(page);
		
		return jr;
	}
	
	/**
	 * 点赞
	 * @return
	 */
	@RequestMapping(value = "/like")
	@ResponseBody
	public JsonResult likeComment(){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
		JsonResult jr = solutionCommentService.likeComment(pd);
		return jr;
	}
	
	/**
	 * 取消点赞
	 * @return
	 */
	@RequestMapping(value = "/cancelLike")
	@ResponseBody
	public JsonResult cancelLike(){
		PageData pd = this.getPageData();
		JsonResult jr = solutionCommentService.cancelLike(pd);
		return jr;
	}
	
	/**
	 * 取消点赞
	 * @return
	 */
	@RequestMapping(value = "/getAllUser")
	@ResponseBody
	public JsonResult getAllUser(){
		PageData pd = this.getPageData();
		JsonResult jr = solutionCommentService.getAllUser(pd);
		return jr;
	}
}
