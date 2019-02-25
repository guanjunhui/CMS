package cn.cebest.service.system.product;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.product.Brand;
import cn.cebest.entity.system.product.Product;
import cn.cebest.util.PageData;
/**
 * 产品接口
 * @author wzd
 *
 */
public interface ProductService {
	
	List<PageData> findProductTypeList(String columnId) throws Exception;
	
	List<PageData> findProducts(Page page) throws Exception;
	
	/**
	 * @Description:  根据条件查找商品
	 * @author: GuanJunHui     
	 * @param @return
	 * @param @throws Exception  
	 * @return List<PageData>
	 * @date:  2018年8月3日 上午9:26:41   
	 * @version V1.0
	 */
	List<PageData> findProducts(PageData pd) throws Exception;
	
	/**
	 * 添加产品
	 * @param product
	 */
	void save(Product product,PageData pd) throws Exception;
	
	/**
	 * 查询产品列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	void findProductToList(Map<String,Object> map,Page page)throws Exception;
	/**
	 * 删除产品
	 * @param id
	 * @return
	 */
	void deleteProduct(String [] id)throws Exception;
	/**
	 * 查询所有产品类型
	 * @return
	 * @throws Exception
	 */
	List<PageData> findProduct_TypeAll(PageData pd) throws Exception;
	/**
	 * 根据id查询相同类型产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<PageData> findProductRelevantBycode(String id)throws Exception;
	
	/**
	 * 查询所有品牌
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<Brand> findBrandAll(PageData pd)throws Exception;
	/**
	 * 修改产品
	 * @param product
	 * @throws Exception
	 */
	void updateProduct(Product product,PageData pd)throws Exception;
	
//	================================================================================
	/**
	 * 跳向添加页面时需查询..,的方法
	 * @param map
	 * @param pd
	 * @return 
	 */
	Map<String, Object> toAddFind(Map<String, Object> map, PageData pd)throws Exception;
	/**
	 * 根据id查询下级产品类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<PageData> findPropertyById(String id)throws Exception;
	/**
	 * 根据产品类型id查询产品
	 * @param id
	 * @return
	 */
	List<Product> findProductBytypeId(String[] id)throws Exception;
	/**
	 * 根据产品的id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Product findProductById(String id)throws Exception;
	/**
	 * 根据产品id查询相关联的产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Product> findProductRelevant(String id)throws Exception;
	/**
	 * 根据id查询相关产品
	 * @param id
	 * @return
	 */
	List<Product> findProductTypeById(String[] id)throws Exception;
	/**
	 * 修改状态
	 * @param ids
	 * @throws Exception 
	 */
	void updataStatus(Map<String, Object> map) throws Exception;
	/**
	 * 置顶活推荐
	 * @param ids
	 * @throws Exception 
	 */
	void updataRecommendAndTop(Product product)throws Exception;
	/**
	 * 展示排序
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void sortlistPage(Map<String, Object> map, Page page) throws Exception;
	/**
	 * 导出
	 * @param pd
	 * @return
	 * @throws Exception 
	 */
	List<Product> downloadsQueryList(PageData pd) throws Exception;
	/**
	 * 批量添加
	 * @param productList
	 * @throws Exception 
	 */
	void save(List<Product> productList) throws Exception;
	/**
	 * 
	 */
	List<PageData> findProductTemplate(PageData pd)throws Exception;
	/**
	 * 验证编号
	 * @param typeid
	 * @param no
	 */
	void findNoCount(Map<String,Object> map)throws Exception;
	/**
	 * 根据栏目id查询产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Product> findProductByColumnId(String id)throws Exception;
	/**
	 * 根据条件返回所有值得list集合,key值与数据库字段名相同,区分大小写
	 */
	public List<Product> findProductAllByAll(Map<String,Object> map) throws Exception;
	/**
	 * 查询各个栏目下置顶的产品
	 */
	List<PageData> findTopList()throws Exception;
	/**
	 * 根据id查询模板路径
	 * @param contentId
	 * @return
	 * @throws Exception 
	 */
	PageData findTemplatePachById(String contentId) throws Exception;
	/**
	 * 根据栏目id查询产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Product> queryProductByColumnId(Page page)throws Exception;
	/**
	 * 根据栏目ids删除产品
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	void deleteByColumnIds(String[] ids) throws Exception;
	
	/**
	 * 根据栏目id查询推荐产品
	 * @param id
	 * @return
	 * @throws Exception
	 * @author liu
	 */
	List<Product> findRecommendProductByColumnId(String id)throws Exception;
	
	/**
	 * 根据类型Id获取排除当前产品列表
	 * @param id
	 * @return
	 * @throws Exception
	 *  * @author liu
	 */
	List<Product> findProductColumnIdExcludeByTypeId(PageData pd)throws Exception;
	/**
	 * 根据类型Id获取筛选区域产品列表
	 * @param id
	 * @return
	 * @throws Exception
	 *  * @author liu
	 */
	List<Product> queryProductAreaByColumnId(Page page)throws Exception;

	List<String> findProductbrands(String id)throws Exception;

	List<PageData> findProductfByIds(String[] id)throws Exception;

	List<PageData> findProductfproductids(String id)throws Exception;

	Product findProducBiaoTiById(String productId)throws Exception;
	
	Product findProducTeDianById(String productId)throws Exception;
	
	Product findProducLiangDianById(String productId)throws Exception;

	List<Product> findProductGuiGeById(String productId)throws Exception;

	List<Brand> findProductJieJueById(String productId)throws Exception;

	Product findProducPieJianById(String productId)throws Exception;

	Product findPeiTaoFuWuById(String productId)throws Exception;

	Product findProducLiuChengById(String productId)throws Exception;

	void updataRecommendAndTopAndHot(Product product) throws Exception;

	void updateSort(PageData pd) throws Exception;


	List<Product> selectProductByAllColumdlistPage(Page page) throws Exception;


	Product findProductByname(String typeName) throws Exception;

	void saveImportData(Product product) throws Exception;

	void insertTypeRealtion(PageData pd) throws Exception;

	Product findProductByTypeOrColumnid(PageData pd) throws Exception;

	List<PageData> getProductByIdList(Page page) throws Exception;


	
}
