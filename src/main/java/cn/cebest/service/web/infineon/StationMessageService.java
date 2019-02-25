package cn.cebest.service.web.infineon;

import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

public interface StationMessageService {

	JsonResult getMessageList(Page page);

	JsonResult deleteMessage(String[] ids);

	JsonResult updateRead(String[] ids);

	JsonResult updateReadAll(String userId);

	JsonResult addMessage(String toUserId, String title);

	JsonResult updateRead(String id);

}
