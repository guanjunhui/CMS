package cn.cebest.service.web.infineon.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.StationMessage;
import cn.cebest.service.web.infineon.StationMessageService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;

@Service("stationMessageService")
public class StationMessageServiceImpl implements StationMessageService {

private static Logger logger = Logger.getLogger(StationMessageServiceImpl.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public JsonResult getMessageList(Page page) {
		JsonResult jr = new JsonResult();
		try {
			Map<String, Object> data = new HashMap<>();
			List<StationMessage> messageList = (List<StationMessage>) dao.findForList("StationMessageMapper.getMessagelistPage", page);
			Integer noRead = (Integer) dao.findForObject("StationMessageMapper.getNoReadNum", page);
			Map<String, Object> newData = new HashMap<>();
			newData.put("noRead", noRead);
			newData.put("data", messageList);
			data.put("newData", newData);
			data.put("page", page);
			jr.setCode(200);
			jr.setData(data);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("获取站内消息列表异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult deleteMessage(String[] ids) {
		JsonResult jr = new JsonResult();
		try {
			dao.delete("StationMessageMapper.delete", ids);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("删除站内消息列表异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult updateRead(String[] ids) {
		JsonResult jr = new JsonResult();
		try {
			dao.update("StationMessageMapper.updateRead", ids);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("更新站内消息状态异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult updateReadAll(String userId) {
		JsonResult jr = new JsonResult();
		try {
			dao.update("StationMessageMapper.updateReadAll", userId);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("更新站内消息状态异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult addMessage(String toUserId, String title) {
		JsonResult jr = new JsonResult();
		try {
			String content = "";
			StationMessage message = new StationMessage();
			// 提问被赞
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			if(!"".equals(title) && null != title){
				content = "您于"+df.format(new Date()) + "联系方案“"+title+"”的请求已收到，我们会尽快跟进并反馈。";
			}else{
				content = "您于"+df.format(new Date()) + "提交的加入合作伙伴申请已收到，我们会尽快跟进并反馈。";
			}
			message.setContent(content);
			message.setToUserId(toUserId);
			dao.save("StationMessageMapper.insert", message);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("通知异常", e);
		}
		return jr;
	}

	@Override
	public JsonResult updateRead(String id) {
		JsonResult jr = new JsonResult();
		try {
			dao.update("StationMessageMapper.updateReadMsg", id);
			jr.setCode(200);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("更新站内消息状态异常", e);
		}
		return jr;
	}

}
