package cn.cebest.controller.front.infineon.ipc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcSiteMsgService;
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
@RequestMapping("msg")

public class IpcSiteMsgController extends BaseController{
	private static Logger logger = Logger.getLogger(IpcSiteMsgController.class);
	@Autowired
	private IpcSiteMsgService ipcSiteMsgService;
	/**
	 * 发送站内信
	 * @return
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult saveMsg(Page page) {
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	ipcSiteMsgService.saveMsg(pd);
		} catch (Exception e) {
			logger.error("send the msg occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 发送站内信
	 * @return
	 */
	@RequestMapping(value = "/getAllListByUserId", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getAllListByUserId(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			data = ipcSiteMsgService.getAllListByUserId(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询我回答的话题列表异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	/**
	 * 获取未读消息数
	 * @return
	 */
	@RequestMapping(value = "/getNoReadCount", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getNoReadCount(@RequestParam(name="userId")String userId) {
		JsonResult jr = null;
		try {
			int count = ipcSiteMsgService.getNoReadCount(userId);
			jr = new JsonResult(200, "success", count);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("获取未读消息数异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 获取站内信详情
	 * @return
	 */
	@RequestMapping(value = "/getDetailById", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getDetailById(@RequestParam(name="msgId")String msgId,
									@RequestParam(name="state")String state) {
		JsonResult jr = null;
		try {
			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("msgId", msgId);
			pMap.put("state", state);
			String content = ipcSiteMsgService.getDetailById(pMap);
			jr = new JsonResult(200, "success", content);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("获取站内信详情异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 批量删除站内信
	 * @return
	 */
	@RequestMapping(value = "/deletes", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult deletes(@RequestParam(name="ids")String ids) {
		JsonResult jr = null;
		try {
			ipcSiteMsgService.deletes(ids.split(","));
			jr = new JsonResult(200, "success");
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("批量删除站内信异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
	
	/**
	 * 批量设置站内信已读
	 * @return
	 */
	@RequestMapping(value = "/patchSetAllReadyRead", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult patchSetAllReadyRead(@RequestParam(name="ids")String ids) {
		JsonResult jr = null;
		try {
			ipcSiteMsgService.patchSetAllReadyRead(ids.split(","));
			jr = new JsonResult(200, "success");
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("批量设置站内信已读异常：", e);
			e.printStackTrace();
		}
		return jr;
	}
}
