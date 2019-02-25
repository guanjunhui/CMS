package cn.cebest.service.system.product;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.util.PageData;

/**
 * 商品类型service接口
 * @author wzd
 *
 */
public interface ProductTypeService {
	/**
	 * 跳向添加页面时,查询栏目和类型的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void findProductTypeToList(Map<String, Object> map, PageData pd) throws Exception;
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * @return
	 * @throws Exception 
	 */
	 List<Product_Type> getTreeData(PageData pd) throws Exception;
	/**
	 * 保存产品类型
	 * @param product
	 * @throws Exception 
	 */
	void save(Product_Type product, PageData pd) throws Exception;
	/**
	 * 跳向修改页面时,查询栏目和类型的方法
	 * @param map
	 * @param page
	 * @return 
	 * @throws Exception 
	 */
	Map<String, Object> findProductTypeToEdit(Map<String, Object> map, String id) throws Exception;
	/**
	 * 修改的方法
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void update(Product_Type product, PageData pd)throws Exception;
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
	void updataStatus(Map<String, Object> map) throws Exception;
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * @return
	 * @throws Exception 
	 */
	List<Product_Type> getlistTreeData(PageData pd)throws Exception;
	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;
	/**
	 * 根据栏目id查询文件类型
	 */
	public List<Product_Type> findProduct_TypeByColumnIds(String id) throws Exception;
	/**
	 * 
	 * @param map
	 * @throws Exception
	 */
	void updateSort(Map<String, Object> map)throws Exception;
	/**
	 * 根据条件返回所有值得list集合,key值与数据库字段名相同,区分大小写
	 */
	public List<Product_Type> findProduct_TypeAllByAll(Map<String,Object> map) throws Exception;
	
	/**
	 * 通过ID查询信息
	 */
	public Product_Type findTypeInfoById(String id) throws Exception;
	/**
	 * 重复性验证
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer findcount(Map<String, Object> map) throws Exception;
	Integer findBrandcount(Map<String, Object> map) throws Exception;
	Integer findPropertycount(Map<String, Object> map) throws Exception;
	List<Product_Type> getTreeByColumId(PageData pd)throws Exception;
	List<Product_Type> getTypeByColumId(PageData pd)throws Exception;

	List<Product_Type> getTypesByColumId(PageData pd)throws Exception;
	
	Product_Type findProductTypeById(String id) throws Exception;
	void updataTypeStatus(PageData pd) throws Exception;
	//根据栏目查询分类对应的产品数量
	List<PageData> selectCountByTypeAndColumID(String id) throws Exception;
	
}
