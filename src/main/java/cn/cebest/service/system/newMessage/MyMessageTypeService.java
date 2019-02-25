package cn.cebest.service.system.newMessage;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.util.PageData;
/**
 * 资讯类型接口
 * @author lwt
 *
 */
public interface MyMessageTypeService {
	/**
	 * 查询资讯类型列表
	 */
	void findMessageTypeToList(Map<String,Object> map,Page page)throws Exception;
	/**
	 * 跳向添加页面时需查询..,的方法
	 * @param map
	 * @param pd
	 */
	void toAddFind(Map<String, Object> map, PageData pd)throws Exception;
	/**
	 * 发送异步请求添加到所属栏目
	 */
	public void findAllColumconfig(Map<String, Object> map,Page page)throws Exception;
	/**
	 * 发送异步请求添加到所属分类
	 */
	public void findAllMessageType(Map<String, Object> map,Page page)throws Exception;
	
	//=================================================================================
	/**
	 * 跳向添加页面时,查询栏目和类型的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void findMessageTypeToList(Map<String, Object> map, PageData pd) throws Exception;
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * @return
	 * @throws Exception 
	 */
	 List<NewMessageType> getTreeData(PageData pd) throws Exception;
	 /**
		 * 添加的页面发送ajax返回层级结构的json数据
		 * @return
		 * @throws Exception 
		 */
	List<NewMessageType> getlistTreeData(PageData pd) throws Exception;
	/**
	 * 保存资讯类型
	 * @param product
	 * @throws Exception 
	 */
	void save(NewMessageType message, PageData pd) throws Exception;
	/**
	 * 跳向修改页面时,查询栏目和类型的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	Map<String, Object> findMessageTypeToEdit(String id) throws Exception;
	/**
	 * 修改的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void update(NewMessageType message, PageData pd)throws Exception;
	/**
	 * 删除的方法
	 * @param ids
	 * @throws Exception 
	 */
	void delete(String[] ids)throws Exception;
	/**
	 * 批量修改状态的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void updateMessageStatus(Map<String, Object> map) throws Exception;
	/**
	 * 修改资讯类型状态
	 * @param pd
	 * @throws Exception
	 */
	void updateTypeStatus(PageData pd)throws Exception;
	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;
	/**
	 * 修改资讯类型状态
	 * @param map
	 * @throws Exception
	 */
	void updateMessageTypeStatus(Map<String, Object> map) throws Exception;
	/**
	 * 根据栏目id查询文件类型
	 */
	public List<NewMessageType> findMessage_TypeByColumnIds(String id) throws Exception;
	/**
	 * 更新SORT
	 */
	public void updateTypeSort(PageData pd)throws Exception;
	/**
	 * 通过ID查询信息
	 */
	public NewMessageType findTypeInfoById(String id) throws Exception;
	
	/**
	 * 根据栏目id查找其下的所有资讯类型,并层级结构显示
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<NewMessageType> selectMessageTypeByColumId(String id) throws Exception;
	List<NewMessageType> selectMessageTypeByColumIds(PageData pd) throws Exception;
	/**
	 * 验证用户名是否重复
	 */
	public NewMessageType selectNewMessageType(String name) throws Exception;
	void updateStatus(PageData pd) throws Exception;
	void save(NewMessageType message) throws Exception;
	void update(NewMessageType message) throws Exception;
}
