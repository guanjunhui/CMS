package cn.cebest.service.system.content.content;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.content.Content;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

public interface ContentService {
	
	List<PageData> findPmmImageList(Page page) throws Exception;
	
	Page findIdsList(Page page) throws Exception;
	
	/**
	 * @Description:  根据父ID查找所有内容
	 * @author: GuanJunHui     
	 * @param @param page
	 * @param @return
	 * @param @throws Exception  
	 * @return List<Content>
	 * @date:  2018年8月1日 下午3:47:13   
	 * @version V1.0
	 */
	List<String> fingAllList(Page page)throws Exception;
	
	/**
	 * 根据栏目id查询内容，内容按创建时间排序
	 */
	 List<Content> ContentListOrderByCreateTime(Page page) throws Exception;

	List<Content> listContentlistPage(Page page) throws Exception;

	Content detailByContentId(Page page) throws Exception;

	List<Content> contentlistPage(Page page) throws Exception;
	
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


	List<PageData> findAll(PageData pd) throws Exception;

	Content findContentById(String id) throws Exception;

	void deleteAll(String[] id) throws Exception;

	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;

	List<ColumConfig> findTopAndChildList(PageData pd) throws Exception;

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
	List<Content> selectlistPageByColumID(Page page)throws Exception;
	/**
	 * 根据栏目id查询全部列表
	 */ 
	int selectlistCountByColumID(Page page)throws Exception;
	
	/**
	 * 根据栏目id查询内容
	 * @param id
	 * @return
	 */
	List<ContentInfoBo> findContentInfoBoById(String id)throws Exception;

	Content findContentsByColumnIdOne(String id) throws Exception;
	 List<Content> selectlistPageByColumIDD(Page page) throws Exception;

	List<Content> contentCompanyProfile(Page page) throws Exception;

	void updatePv(String contentId)throws Exception;

	List<Content> findContent(PageData pd)throws Exception;

	List<Content> contentContact(PageData pd) throws Exception;
	
	void updateSort(PageData pd)throws Exception;

	void updateStatusByIds(PageData pd) throws Exception;

	void updataRecommendAndTopAndHot(Content content) throws Exception;

	void updateContent(Content content) throws Exception;

	List<Content> selectContentBycolumtypeid(PageData pd) throws Exception;

	List<Content> findContentByColumOrTypeList(PageData pd) throws Exception;

	List<Content> selectlistPageByTypeIds(Page page)throws Exception;

	Content findContentByTypeOrColumnid(PageData pd) throws Exception;

	List<PageData> findContentOrColumOrType(PageData pd) throws Exception;

	List<Content> selectOrderlistPageByColumID(Page page) throws Exception;

	List<Content> findContentByCT(PageData pd) throws Exception;

	JsonResult getContentList(Page page);

	JsonResult getNewsList(Page page);

	List<Content> selectlistPageByColumIDpmm(Page page) throws Exception;


}
