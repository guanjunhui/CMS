package cn.cebest.service.web.template;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.TemplateTree;
import cn.cebest.entity.system.Template;
import cn.cebest.util.PageData;
/** 
 * 说明：模板管理
 * 创建人：qichangxin
 * 创建时间：2017-09-27
 */
public interface TemplateManager {
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表(全部)转化为树形结构
	 * @param pd
	 * @throws Exception
	 */
	public List<TemplateTree> listAllTree(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	public Template findPoById(String id)throws Exception;

	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	public Integer check(String[] arrayDATA_IDS) throws Exception;
	
	/**获取指定类型的模板
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findListByDefinedType(PageData pd)throws Exception;

	public List<TemplateTree> findTreesByDefinedType(PageData pd)throws Exception;

	/** 重置指定类型的的数据默认为否
	 * @param type
	 * @throws Exception
	 */
	public void resetDefault(String type)throws Exception;
	
	/**更新模板为默认
	 * @param type
	 * @throws Exception
	 */
	public void updateDefault(String id)throws Exception;

	Template findSitePoById(String id) throws Exception;


	/**
	 * 查询所有模板，复制网站栏目及模板时使用
	 * @return
	 * @throws Exception
	 */
	public List<Template> listAllForCopy(String siteID) throws Exception;
	
	/**
	 * 批量保存模板，复制网站栏目及模板时使用
	 * @param list
	 * @throws Exception
	 */
	public void insertBatchTemplate(List<Template> list) throws Exception;
	
	
	/**
	 * 复制整站模板之前，如果执行过复制，再次执行需要先根据网站ID批量删除之前复制的模板
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteBeforCopy(String siteID)throws Exception;
}
