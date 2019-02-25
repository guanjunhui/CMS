package cn.cebest.service.system.serviceNetwork;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.ServiceNetwork.Agency;
import cn.cebest.entity.system.ServiceNetwork.ServiceNetwork;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.util.PageData;

public interface ServiceNetworkService {

	/**
	 * 代理商显示列表
	 */
	List<ServiceNetwork> serviceNetworkListPage(Page page) throws Exception;
	/**
	 * zengxiangpeng
	 * service接口：新增代理商
	 */
	void saveServiceNetwork(ServiceNetwork serviceNetwork) throws Exception;
	
	void saveServiceNetwork(List<ServiceNetwork> list) throws Exception;
	/**
	 * zengxiangpeng
	 * service接口：修改根据id查询代理商
	 */
	ServiceNetwork selectServiceNetworkById(String id) throws Exception;
	/**
	 * zengxiangpeng
	 * service接口：修改代理商
	 */
	void updateServiceNetwork(ServiceNetwork serviceNetwork) throws Exception;
	/**
	 * zengxiangpeng
	 * service接口：批量删除代理商
	 */
	void delAllServiceNetwork(String[] id) throws Exception;
	/**
	 * Contact页面加载地址
	 */
	/*List<Banner> loadAddress(String country) throws Exception;*/
	
	List<Content> contentlistPage(Page page) throws Exception;
	
	List<Content> listContentlistPage(Page page) throws Exception;

	Content detailByContentId(Page page) throws Exception;
	
	List<Content> contentFrontlistPage(Page page) throws Exception;

	void delContent(PageData pd) throws Exception;

	void delAllContent(String[] arrayCONTENT_IDS) throws Exception;

	Content findContentById(PageData pd) throws Exception;

	void editContent(Content content) throws Exception;

	void auditContent(PageData pd) throws Exception;

	List<PageData> contentTypeList(PageData pd) throws Exception;

	List<PageData> contentRelevantList(PageData pd) throws Exception;

	void editContentTxt(PageData pd) throws Exception;

	PageData findContentTxtById(PageData pd) throws Exception;

	void saveContent(Content content) throws Exception;

	void updateContent(Content content, PageData pd) throws Exception;

	List<PageData> findAll(PageData pd) throws Exception;

	Content findContentById(String id) throws Exception;

	void deleteAll(String[] id) throws Exception;

	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;

	List<ColumConfig> findTopAndChildList(PageData pd) throws Exception;

	void updataRecommendAndTop(Content content) throws Exception;

	/**
	 * 根据栏目id查询内容
	 */
	public List<Content> findContentByColumnIds(String id) throws Exception;

	/**
	 * 根据栏目id查询所有内容（时间倒序）
	 */
	public List<Content> findContentsByColumnId(String id) throws Exception;

	/**
	 * 根据条件返回所有值得list集合,key值与数据库字段名相同,区分大小写
	 */
	public List<PageData> findContentAllByAll(Map<String, Object> map) throws Exception;

	/**
	 * 查询各个栏目下置顶的内容
	 */
	List<PageData> findTopList() throws Exception;

	/**
	 * 查询模板路径和栏目
	 * 
	 * @param contentId
	 */
	Content findTemplatePachById(String contentId) throws Exception;


	List<PageData> findColum(PageData pd)throws Exception;



	/**
	 * 根据内容id查询详情
	 */
	Content findTxt(String id) throws Exception;
	/**
	 * 根据栏目id查询全部列表
	 */ 
	List<PageData> selectlistPageByColumID(Page page)throws Exception;
	
	/**
	 * 根据栏目id查询内容
	 * @param id
	 * @return
	 */
	List<ContentInfoBo> findContentInfoBoById(String id)throws Exception;

	Content findContentsByColumnIdOne(String id) throws Exception;
	 List<Content> selectlistPageByColumIDD(Page page) throws Exception;

	 List<Content> contentCompanyProfile(Page page) throws Exception;
	/**
	 * 根据栏目id查询内容，内容按创建时间排序
	 */
	 List<Content> ContentListOrderByCreateTime(Page page) throws Exception;

	void updatePv(String contentId)throws Exception;

	List<Content> findContent(PageData pd)throws Exception;

	List<Content> contentContact(PageData pd) throws Exception;
	/**
	 * 获取服务商性质
	 * zengxiangpeng
	 */
	List<ServiceNetwork> selectServiceNature() throws Exception;
	
	/**
	 * 前台查询服务商
	 */
	List<ServiceNetwork> selectServiceFront(Page page) throws Exception;
	
	/**
	 * 产询所有国家
	 * @throws Exception 
	 */
	List<Agency> selectCountry(Agency agency) throws Exception;
	
	List<ServiceNetwork> selectNetworkByName(PageData pd) throws Exception;
	
	void saveProductRegistration(ProductRegistration productRegistration) throws Exception;
	
	void saveProductRegistration(List<ProductRegistration> list) throws Exception;
	
	

}
