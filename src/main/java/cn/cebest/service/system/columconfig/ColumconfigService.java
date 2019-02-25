package cn.cebest.service.system.columconfig;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.util.PageData;

public interface ColumconfigService {
	
	List<PageData> findColumconfiglistPage(Page page) throws Exception;

	List<PageData> columconfiglistPage(Page page) throws Exception;

	void delColumconfig(PageData pd) throws Exception;

	void updateAllColumconfig(String[] arrayCOLUMCONFIG_IDS) throws Exception;

	void saveColumconfig(PageData pd) throws Exception;

	PageData findColumconfigById(PageData pd) throws Exception;
	ColumConfig findColumconfigPoById(PageData pd) throws Exception;


	void editColumconfig(PageData pd) throws Exception;

	void auditColumconfig(PageData pd) throws Exception;
	
	List<ColumConfig> findAllList(PageData pd) throws Exception;
	
	List<ColumConfig> findTopList(PageData pd) throws Exception;
	
	List<ColumConfig> findTopAndChildList(PageData pd) throws Exception;
	
	List<Tree> findTopAndChildTree(PageData pd) throws Exception;
	List<Tree> findTopAndChildTreeByGroupId(PageData pd) throws Exception;
	List<PageData> findAllTree(PageData pd) throws Exception;
	List<PageData> findTopTree(PageData pd) throws Exception;
	
	List<Tree> findAssignTypeTree(PageData pd) throws Exception;
	List<PageData> findAssignTypeAllTree(PageData pd) throws Exception;
	List<PageData> findAssignTypeTopTree(PageData pd) throws Exception;

	List<PageData> columconfigAllList(PageData pd) throws Exception;

	
	List<ColumConfig> findAssignTypeAllColums(PageData pd) throws Exception;
	
	List<ColumConfig> findAssignTypeTopColums(PageData pd) throws Exception;
	
	List<ColumConfig> findAssignTypeTopAndChildList(PageData pd) throws Exception;
	
	void delAll(String[] ids) throws Exception;

	List<PageData> findAllIds(String siteId) throws Exception;

	List<String> findSelfAndChildIds(String parentId,String siteId) throws Exception;

	void updateColumSort(List<ColumConfig> updateList) throws Exception;

	int getIncludeColumCountByColumGroupId(String columGroupId) throws Exception;

	List<PageData> findColumconfigByPid(PageData pd)throws Exception;
	//江铜集团前端显示页面接口开始====================================>
	/**
	 * 根据栏目id查找栏目详情
	 */
	ColumConfig selectColumnById(PageData pd)throws Exception;
	//江铜集团前端显示页面接口结束====================================<

	List<Tree> findTopAndGroupTree(PageData pd) throws Exception;

	List<Tree> findPermGroupAndColumTree(PageData pd) throws Exception;

	List<ColumConfig> findAllListByAssignedColum(PageData pd) throws Exception;

	List<ColumConfig> findSelfAndChildList(PageData pd) throws Exception;

	Template findTemplateDetailByColumId(String id) throws Exception;

	List<Tree> findColumAndTypeTree(PageData pd) throws Exception;

	void deleteColum(String parentId, String siteId) throws Exception;

	ColumConfig findColumDetailByColumId(String columId) throws Exception;

	List<ColumConfig> findColumList() throws Exception;

	void updateColumIndexStatus(PageData pd) throws Exception;

	List<PageData> findColumconfigByPidPage(Page page) throws Exception;
	List<ColumConfig> selectSubColumnListByParientId(PageData pd) throws Exception;

	/**
	 * 通过网站ID查询所有栏目，复制网站栏目及模板时使用
	 * @param siteID
	 * @return
	 * @throws Exception
	 */
	public List<ColumConfig> listAllForCopy(String siteID) throws Exception;
	/**
	 * 批量复制栏目，批量保存，复制网站栏目及模板时使用
	 * @param list
	 * @throws Exception
	 */
	public void insertBatchColumn(List<ColumConfig> list) throws Exception;
	
	/**
	 * 复制整站栏目之前，如果执行过复制，再次执行需要先根据网站ID批量删除之前复制栏目
	 * @param siteID
	 * @throws Exception
	 */
	public void deleteBeforCopy(String siteID) throws Exception;


	ColumConfig selectColumnIdByName(String columnName) throws Exception;

	void delContentAll(String[] ids) throws Exception;

	PageData findColumUrlNameconfigById(String skipPath) throws Exception;

	PageData findContentUrlNameconfigById(PageData pd) throws Exception;

	/**获取父级栏目
	 * @param columId
	 * @return
	 */
	Object getParentName(String columId) throws Exception;

}
