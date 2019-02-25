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
public interface IpcCollectionService {

	/**
	 * @param page
	 * @return
	 */
	Map<String, Object> listPlanByUserId(Page page)throws Exception ;

	/**
	 * @param page
	 * @return
	 */
	Map<String, Object> topicListByUserId(Page page)throws Exception;

	/**
	 * @param pd
	 */
	void collection(PageData pd)throws Exception;

	/**取消收藏
	 * @param pd
	 */
	void cancel(PageData pd)throws Exception;
	
}
