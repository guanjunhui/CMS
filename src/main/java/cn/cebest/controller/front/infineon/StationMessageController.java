package cn.cebest.controller.front.infineon;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.web.infineon.StationMessageService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 * 站内消息
 * @author
 *
 */
@RestController
@RequestMapping(value = "/stationMessage")
public class StationMessageController extends BaseController {


	@Autowired
	private StationMessageService stationMessageService;
	
	/**
	 * 站内消息列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "messageList", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult messageList(Page page){
		PageData pd = this.getPageData();
		page.setPd(pd);
		JsonResult jr = stationMessageService.getMessageList(page);
		return jr;
	}
	
	/**
	 * 删除站内消息
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "deleteMessage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult deleteMessage(String[] ids){
		JsonResult jr = stationMessageService.deleteMessage(ids);
		return jr;
	}
	
	@RequestMapping(value = "read", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult updateRead(String[] ids){
		JsonResult jr = stationMessageService.updateRead(ids);
		return jr;
	}
	
	@RequestMapping(value = "readAll", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult updateReadAll(String userId){
		JsonResult jr = stationMessageService.updateReadAll(userId);
		return jr;
	}
	
	@RequestMapping(value = "addMessage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult add(String toUserId, String title){
		JsonResult jr = stationMessageService.addMessage(toUserId,title);
		return jr;
	}
	
	@RequestMapping(value = "readMsg", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult updateRead(String id){
		JsonResult jr = stationMessageService.updateRead(id);
		return jr;
	}
}
