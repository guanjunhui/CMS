package cn.cebest.controller.system.comment;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.service.system.comment.CommentService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.UserAgentUtil;

/**@author liqk
 * 留言/评论-接口类 
*/
@Controller
@RequestMapping(value="/comment")
public class CommentController extends BaseController {

	String menuUrl = "comment/commentlistPage.do"; //菜单地址(权限用)
	@Resource(name="commentService")
	private CommentService commentService;
	@Resource(name = "contentService")
	private ContentService contentService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	
	/**
	 * 前台接口-新增评论/提问
	 * @return
	 */
	@RequestMapping("/saveComment")
	@ResponseBody
	public Map<String,Object> saveComment(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		HttpServletRequest request=this.getRequest();
		//通过userAgent判断是手机端,还是pc端
		String userAgent = request.getHeader("User-Agent");
		String clientType=UserAgentUtil.clientType(userAgent);
		pd.put("clientType", clientType);
		pd.put("commentPropertyId", this.get32UUID());
		pd.put("commentId", this.get32UUID());
		pd.put("status", "1");	//默认不显示
		pd.put("createTime", DateUtil.getTime());	//当前时间
		try {
			commentService.saveComment(pd);
			resultMap.put("code", 200);
			resultMap.put("msg", "success");
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", "failed");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 前台接口-根据关联ID/用户ID查询(评论/提问列表)
	 * @param relevanceId (关联的ID)
	 * @param commentType (0是评论,1是提问)
	 * @param UserId (用户id)
	 * @param status (0,查询审核通过的)
	 * @param isParent (不为空,查询主提问)
	 * @return
	 */
	@RequestMapping("/listcommentlistPage")
	@ResponseBody
	public Map<String,Object> listcommentlistPage(Page page){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		try {
			List<PageData> commentList=commentService.listcommentlistPage(page);
			resultMap.put("data", commentList);
			resultMap.put("code", 200);
			resultMap.put("msg", "success");
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", "failed");
			e.printStackTrace();
		}
		resultMap.put("page", page);
		return resultMap;
	}
	
	/**
	 * 前台接口-根据提问ID查询跟评
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/listReplyByCommentId")
	@ResponseBody
	public Map<String,Object> listReplyByCommentId(Page page){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		try {
			List<PageData> commentReplyList=commentService.commentReplylistpage(page);
			resultMap.put("data", commentReplyList);
			resultMap.put("code", 200);
			resultMap.put("msg", "success");
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", "failed");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 后台接口-去查看并回复提问页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goCommentReply")
	public ModelAndView goCommentReply() {
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd=commentService.findCommentById(pd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("system/comment/comment_reply");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 后台接口-回复提问
	 * @param commentId
	 * @param userId
	 * @param replyContent
	 * @return
	 */
	@RequestMapping("/saveCommentReply")
	public ModelAndView saveCommentReply(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//从session中取得用户ID
		Session session = Jurisdiction.getSession();
		User user = (User)session.getAttribute(Const.SESSION_USERROL);
		String userId=user.getUSER_ID();
		pd.put("userId", userId);
		pd.put("status", "0");
		pd.put("createTime", DateUtil.getTime());	//当前时间
		HttpServletRequest request=this.getRequest();
		//通过userAgent判断是手机端,还是pc端
		String userAgent = request.getHeader("User-Agent");
		String clientType=UserAgentUtil.clientType(userAgent);
		pd.put("clientType", clientType);
		//评论回复id
		String commentReplyId=pd.getString("commentReplyId");
		if(StringUtils.isNotBlank(commentReplyId)){
			//已经存在回复,更新回复内容
			pd.put("commentReplyId", commentReplyId);
			try {
				commentService.updateCommentReply(pd);
				mv.addObject("msg","success");
			} catch (Exception e) {
				mv.addObject("msg","failed");
				e.printStackTrace();
			}
		}else{
			//之前未回复
			pd.put("newCommentId", this.get32UUID());
			try {
				commentService.saveCommentReply(pd);
				mv.addObject("msg","success");
			} catch (Exception e) {
				mv.addObject("msg","failed");
				e.printStackTrace();
			}
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 后台接口,显示评论/提问列表
	 * @return
	 */
	@RequestMapping(value="/commentlistPage")
	public ModelAndView commentlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String relevanceContentTitle = pd.getString("relevanceContentTitle");	//检索机构的回复/评论
		if(null != relevanceContentTitle && !"".equals(relevanceContentTitle)){
			pd.put("relevanceContentTitle", relevanceContentTitle.trim());
		}
		String startTime = pd.getString("startTime");	//开始时间
		String endTime = pd.getString("endTime");		//结束时间
		if(startTime != null && !"".equals(startTime)){
			pd.put("startTime", startTime+" 00:00:00");
		}
		if(endTime != null && !"".equals(endTime)){
			pd.put("endTime", endTime+" 23:59:59");
		}
		page.setPd(pd);
		try {
			List<PageData> commentList = commentService.commentManagerlistPage(page);	//列出评论列表
			mv.setViewName("system/comment/comment_list");
			mv.addObject("commentList", commentList);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 删除评论
	 * @return
	 */
	@RequestMapping("/delComment")
	public void delComment(PrintWriter out){
		logBefore(logger, Jurisdiction.getUsername()+"删除留言数据");
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			commentService.delComment(pd);
			FHLOG.save(Jurisdiction.getUsername(), "删除留言数据："+pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**
	 * 查看评论
	 * @return
	 */
	@RequestMapping("/findCommentById")
	@ResponseBody
	public ModelAndView findCommentById(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd=commentService.findCommentById(pd);
			mv.addObject("pd", pd);
			mv.addObject("msg","success");
			mv.setViewName("system/comment/comment_view");
		} catch (Exception e) {
			mv.addObject("msg","failed");
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 审核评论
	 * @return
	 */
	@RequestMapping("/auditComment")
	@ResponseBody
	public Object auditComment(
			@RequestParam("commentId")String commentId,
			@RequestParam("status")String status){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(StringUtils.isNotBlank(status) && "0".equals(status)){
	    		status="1";
	    	}else if(StringUtils.isNotBlank(status) && "1".equals(status)){
	    		status="0";
	    	}
			pd.put("status", status);
			pd.put("msg", "ok");
			commentService.auditComment(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAllComment")
	@ResponseBody
	public Object deleteAllComment() {
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"批量删除comment");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			FHLOG.save(Jurisdiction.getUsername(), "批量删除comment");
			List<PageData> pdList = new ArrayList<PageData>();
			String COMMENT_IDS = pd.getString("COMMENT_IDS");
			if(null != COMMENT_IDS && !"".equals(COMMENT_IDS)){
				String arrayCOMMENT_IDS[] = COMMENT_IDS.split(",");
				commentService.deleteAllComment(arrayCOMMENT_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
}
