package cn.cebest.controller.front.infineon.ipc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.impl.calendar.BaseCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcCollectionService;
import cn.cebest.service.system.infineon.impl.IpcCollectionServiceImpl;
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
@RequestMapping("col")

public class IpcCollectionController extends BaseController{
	private static Logger logger = Logger.getLogger(IpcCollectionController.class);
	@Autowired
	private IpcCollectionService ipcCollectionService;
	/**
	 * 根据用户id查询收藏解决方案列表
	 * @return
	 */
	@RequestMapping(value = "/getPlan", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult listByUserId(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcCollectionService.listPlanByUserId(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询收藏解决方案列表异常：", e);
			e.printStackTrace();
		}
		
		return jr;
	}
	/**
	 * 根据用户id查询收藏话题列表
	 * @return
	 */
	@RequestMapping(value = "/getTopic", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult topicListByUserId(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcCollectionService.topicListByUserId(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询收藏话题列表异常：", e);
			e.printStackTrace();
		}
		
		return jr;
	}
	/**
	 * 收藏方案或话题
	 * @param request
	 * @return
	 */
	@RequestMapping(value="collection",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult collection(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //收藏
	    try {
	    	ipcCollectionService.collection(pd);
		} catch (Exception e) {
			if(e.getMessage().equals("您已收藏")){
				return new JsonResult(Const.HTTP_ERROR_401, e.getMessage());
			}
			logger.error("save the collectionPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 取消方案或话题
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancel",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult cancel(HttpServletRequest request){
		PageData pd = this.getPageData();
	    //收藏
	    try {
	    	ipcCollectionService.cancel(pd);
		} catch (Exception e) {
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
}
