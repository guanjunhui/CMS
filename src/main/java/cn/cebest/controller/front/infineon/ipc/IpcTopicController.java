package cn.cebest.controller.front.infineon.ipc;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcTopicService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年9月27日
 * @company 中企高呈
 */
@Controller
@RequestMapping("topic")

public class IpcTopicController extends BaseController{
	private static Logger logger = Logger.getLogger(IpcTopicController.class);
	@Autowired
	private IpcTopicService ipcTopicService;
	/**
	 * 根据用户id查询发表的话题列表
	 * @return
	 */
	@RequestMapping(value = "/getHotList", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getHotList(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcTopicService.getHotList(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询热门话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 查询热门话题列表
	 * @return
	 */
	@RequestMapping(value = "/getMyList", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult listByUserId(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcTopicService.getMyTopicListByUserId(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询我发表的话题列表异常：", e);
			e.printStackTrace();
		}
		
		return jr;
	}
	
	/**
	 * 发表话题
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult saveTopic(HttpServletRequest request){
		PageData pd = this.getPageData();
		String id = this.get32UUID();
		pd.put("id", id);
		JsonResult jsonResult = null;
	    //获取数据
	    try {
	    	if(StringUtils.isNotEmpty(pd.getString("title")) && StringUtils.isNotEmpty(pd.getString("detail")) && StringUtils.isNotEmpty(pd.getString("userId"))){
	    		ipcTopicService.saveTopic(pd);
		    	jsonResult = new JsonResult(Const.HTTP_OK, "OK",id);
	    	}else{
	    		jsonResult = new JsonResult(Const.HTTP_ERROR_401, "参数不正确");
	    	}
		} catch (Exception e) {
			logger.error("save the topic occured error!",e);
			jsonResult = new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return jsonResult;
	}
	
	/**
	 * 话题回复
	 * @return
	 */
	@RequestMapping(value="reply",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult replyTopic(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	ipcTopicService.saveTopicReply(pd);
		} catch (Exception e) {
			logger.error("save the replyTopic occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 根据id查询话题详情
	 * @return
	 */
	@RequestMapping(value = "/getTopicById", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getTopicById(@RequestParam(name="topicId",required=true) String topicId,String userId) {
		JsonResult jr = null;
		Map<String,String> pMap = new HashMap<>();
		pMap.put("topicId", topicId);
		pMap.put("userId", userId);
		try {
			Map<String, Object> data = ipcTopicService.getTopicById(pMap);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询所有的话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 查询所有的话题列表
	 * @return
	 */
	@RequestMapping(value = "/getAllList", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getAllList(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		page.setIsSolve(pd.getString("isSolve"));
		page.setIsClose(pd.getString("isClose"));
		page.setTime(pd.getString("time"));
		Map<String, Object> data;
		try {
			if(pd.get("text") != null){
				page.setText(URLDecoder.decode(pd.getString("text").toString(),"UTF-8"));
			}else{
				page.setText("");
			}
			
			data = ipcTopicService.getAllList(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询所有的话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 查询所有的话题一级评论列表
	 * @return
	 */
	@RequestMapping(value = "/getReplyList", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getReplyList(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcTopicService.getReplyList(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询话题回复列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	/**
	 * 查询所有的话题二级评论列表
	 * @return
	 */
	@RequestMapping(value = "/getReply2List", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getReply2List(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		try {
			List<Map<String, Object>> data = ipcTopicService.getReply2List(pd);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询话题二级回复列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 修改话题浏览量
	 * @return
	 */
	@RequestMapping(value="updatePageViews",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult updatePageViews(@RequestParam(name="topicId",required=true)String topicId){
	    //修改浏览量
	    try {
	    	ipcTopicService.updatePageViews(topicId);
		} catch (Exception e) {
			logger.error("update topic PageViews occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 关闭话题
	 * @return
	 */
	@RequestMapping(value="close",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult closeTopic(){
		PageData pd = this.getPageData();
		//修改浏览量
	    try {
	    	ipcTopicService.closeTopic(pd);
		} catch (Exception e) {
			logger.error("close topic occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 查询所有的话题列表
	 * @return
	 */
	@RequestMapping(value = "/getMyAnswerTopic", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getMyAnswerTopic(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcTopicService.getMyAnswerTopic(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询我回答的话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 根据话题id和userId查询我的回答
	 * @return
	 */
	@RequestMapping(value = "/getMyReplyByUserIdAndTopicId", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getMyReplyByUserIdAndTopicId(@RequestParam(name="userId",required=true)String userId,
												   @RequestParam(name="topicId",required=true)String topicId) {
		JsonResult jr = null;
		List<Map<String, Object>> data;
		Map<String, String> pMap = new HashMap<>();
		pMap.put("userId", userId);
		pMap.put("topicId", topicId);
		try {
			data = ipcTopicService.getMyReplyByUserIdAndTopicId(pMap);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询我回答的话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
}
