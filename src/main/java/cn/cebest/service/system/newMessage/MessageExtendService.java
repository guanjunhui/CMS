package cn.cebest.service.system.newMessage;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.MessageExtendWords;
import cn.cebest.util.PageData;

public interface MessageExtendService {
	/**
	 * 取得展示数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<MessageExtendWords> getTree(PageData pd)throws Exception;
	/**
	 * 删除属性
	 * @param id
	 * @throws Exception 
	 */
	void delete(String[] id) throws Exception;
	/**
	 * 保存扩展字段
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd) throws Exception;
	/**
	 * 修改字段
	 * @param pd
	 * @throws Exception
	 */
	void update(PageData pd)throws Exception;
	/**
	 * 根据id查询
	 * @param id
	 * @throws Exception
	 */
	void findById(Map<String,Object> map,String id)throws Exception;
	//分页展示数据
	List<MessageExtendWords> getData(Page page) throws Exception;

}
