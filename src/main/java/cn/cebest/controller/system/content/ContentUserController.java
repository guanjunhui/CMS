package cn.cebest.controller.system.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.service.system.comment.CommentService;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.PageData;
import cn.cebest.util.UuidUtil;

/**
 * @Description: 解决方案和管理员管理
 * @author: GuanJunHui
 * @date:   2018年12月7日 上午9:59:03    
 * @version V1.0 
 * @Copyright: 2018   
 *
 */
@Controller
@RequestMapping("ContentUser")
public class ContentUserController extends BaseController{

	@Autowired
	private UserManager userService;
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("reSendMail")
	@ResponseBody
	public String reSendMail(String userId,String resourceId,String date) throws Exception {
		PageData pd = this.getPageData();
		String flag = commentService.reSendMail(pd);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		if (!StringUtils.isBlank(flag) && "success".equals(flag)) {
			resultMap.put("data", "success");
			resultMap.put("code", 200);
		}else {
			resultMap.put("data", "false");
			resultMap.put("code", 500);
		}
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(resultMap);
		return result;
	}
	
	@RequestMapping("sendMessage")
	private void deleteRelation() throws Exception{
		commentService.sendRelationMail();
	}
	
	@RequestMapping("deleteRelation")
	private void deleteRelation(String userId,String RELATIONID) throws Exception{
		PageData pd = this.getPageData();
		pd.put("userId", userId);
		if (!StringUtils.isBlank(RELATIONID)) {
			pd.put("RELATIONID", RELATIONID.split(","));
			commentService.deleteRelationByIds(pd);
		}
	}
	
	/**
	 * @Description:  查找管理员列表
	 * @author: GuanJunHui     
	 * @param @return  
	 * @return ModelAndView
	 * @date:  2018年12月7日 上午10:01:04   
	 * @version V1.0
	 * @throws Exception 
	 */
	@RequestMapping("findUserList")
	public ModelAndView findUserList(Page page) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		List<User> userList = userService.listUsers(page);
		modelAndView.addObject("user", userList);
		modelAndView.setViewName("system/contentuser/contentuser_list");
		return modelAndView;
	}
	
	@RequestMapping("toupdate")
	public ModelAndView toUpdate(String id,String userId) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		PageData pd = this.getPageData();
		pd.put("id", id);
		pd.put("userId", userId);
		List<PageData> userComments = commentService.findCommentListByUserId(pd);
		List<PageData> findCommentListByUserId = commentService.findNoReCommentListByUserId(pd);
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("relationComment", userComments);
		modelAndView.addObject("comments", findCommentListByUserId);
		modelAndView.setViewName("system/contentuser/contentuser_edit");
		return modelAndView;
	}
	
	@RequestMapping("saveCommentIdByUserId")
	public void insertCommentIdByUserId(String checkIds,String userId) throws Exception {
		List<Map<String, Object>> relationList = new ArrayList<>();
		String[] commentId = checkIds.split(",");
		for (int i = 0; i < commentId.length; i++) {
			PageData pd = this.getPageData();
			pd.put("commentId", commentId[i]);
			pd.put("userId", userId);
			List<PageData> userComments = commentService.findCommentListByUserId(pd);
			if (userComments.isEmpty()) {
				Map<String, Object> map = new HashMap<>();
				map.put("checkIds", commentId[i]);
				map.put("userId", userId);
				map.put("id", UuidUtil.get32UUID());
				relationList.add(map);
			}
		}
		if (!relationList.isEmpty()) {
			commentService.insertCommentByUserId(relationList);
		}
	}
	
}
