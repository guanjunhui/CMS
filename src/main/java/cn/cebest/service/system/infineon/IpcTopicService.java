package cn.cebest.service.system.infineon;

import java.util.List;
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
public interface IpcTopicService {

	/**查询我发表的话题列表
	 * @param page
	 * @return
	 */
	Map<String, Object> getMyTopicListByUserId(Page page)throws Exception;

	/**发表话题
	 * @param pd
	 */
	void saveTopic(PageData pd) throws Exception;

	/**话题回复
	 * @param pd
	 */
	void saveTopicReply(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	Map<String, Object> getHotList(Page page)throws Exception  ;

	/**查询所有的话题列表
	 * @param page
	 * @return
	 */
	Map<String, Object> getAllList(Page page)throws Exception;

	/**查询话题回复列表
	 * @param page
	 * @return
	 */
	Map<String, Object> getReplyList(Page page)throws Exception;

	/**修改浏览量
	 * @param topicId
	 */
	void updatePageViews(String topicId)throws Exception;

	/**关闭话题
	 * @param page
	 */
	void closeTopic(PageData pd )throws Exception;

	/**查询话题二级回复
	 * @param pd
	 * @return
	 */
	List<Map<String, Object>> getReply2List(PageData pd)throws Exception;

	/**根据id获取话题详情
	 * @param topicId
	 * @return
	 */
	Map<String, Object> getTopicById(Map<String,String> pMap)throws Exception;

	/**根据用户id查询其回答的话题列表
	 * @param page
	 * @return
	 */
	Map<String, Object> getMyAnswerTopic(Page page)throws Exception;

	/**根据话题id和userId查询我的回答
	 * @param pMap
	 * @return
	 */
	List<Map<String, Object>> getMyReplyByUserIdAndTopicId(Map<String, String> pMap)throws Exception;

	/**根据id删除话题
	 * @param pd
	 */
	void deleteById(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	void deleteReplyById(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	Map<String, Object> getAllReplyList(Page page)throws Exception;

	/**
	 * @param id
	 * @return
	 */
	Object detailById(String id)throws Exception;

	/**
	 * 
	 */
	void scanTopic()throws Exception;
	
}
