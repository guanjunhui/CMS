package cn.cebest.service.system.content.content;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;


public interface ContentExtendFiledService {
	/**
	 * 取得展示数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<PageData> getData(PageData pd)throws Exception;
	/**
	 * 删除属性
	 * @param id
	 * @throws Exception 
	 */
	void delete(String[] id) throws Exception;
	/**
	 * 保存属性
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd) throws Exception;
	/**
	 * 修改属性
	 * @param pd
	 * @throws Exception
	 */
	void update(PageData pd)throws Exception;
	/**
	 * 根据id查询
	 * @param id
	 * @throws Exception
	 */
	Map<String,Object> findById(PageData pd)throws Exception;
	Integer findCount(PageData pd)throws Exception;
	Page<List<PageData>> getData(Page<List<PageData>> page)throws Exception;
	void updateSort(Map<String, Object> map);
	
	Map findIdByName(PageData pd) throws Exception;
}
