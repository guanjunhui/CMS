package cn.cebest.service.system.dictionaries;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.Dictionaries;
import cn.cebest.util.PageData;

/** 
 * 说明： 数据字典接口类
 * 创建人：中企高呈
 * 创建时间：2015-12-16
 * @version
 */
public interface DictionariesManager{

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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception;
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listSubDictByParentId(String parentId) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String contextPath,String parentId) throws Exception;
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception;
	
	/**通过编码获取本级与第一级子级数据列表
	 * @param pd
	 * @throws Exception
	 */
	public Dictionaries listParentAndChildDict(String BIANMA)throws Exception;

	public Dictionaries findByBianmaPoJo(String BIANMA)throws Exception;
	
	/**查询所有数据
	 * @throws Exception
	 */
	public List<Dictionaries> findAll()throws Exception;
	
	/**递归查询所有子级数据
	 * @throws Exception
	 */
	public List<Tree> findAllChildByBianma(String BIANMA)throws Exception;

	
	public String findNameById(String id) throws Exception;


}

