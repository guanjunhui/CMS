package cn.cebest.service.system.infineon;

import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年9月27日
 * @company 中企高呈
 */
public interface IpcSiteMsgService {

	/**发送站内信
	 * @param pd
	 */
	void saveMsg(PageData pd)throws Exception;

	/**获取我的所有的站内信列表
	 * @param page
	 * @return
	 */
	Map<String, Object> getAllListByUserId(Page page)throws Exception;

	/**获取我的未读消息数
	 * @param userId
	 * @return
	 */
	int getNoReadCount(String userId)throws Exception;

	/**根据站内信id获取详情并设置已读
	 * @param pMap
	 * @return
	 */
	String getDetailById(Map<String, String> pMap)throws Exception;

	/**批量删除站内信
	 * @param ids
	 * @return
	 */
	void deletes(String[] ids)throws Exception;

	/**批量设置站内信已读
	 * @param split
	 */
	void patchSetAllReadyRead(String[] split)throws Exception;
	
}
