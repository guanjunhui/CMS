package cn.cebest.service.system.product;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.system.product.Brand;
import cn.cebest.util.PageData;

public interface BrandService {
	/**
	 * 取得展示数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<Brand> getTree(PageData pd)throws Exception;
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
	void findById(Map<String,Object> map,String id)throws Exception;
}
