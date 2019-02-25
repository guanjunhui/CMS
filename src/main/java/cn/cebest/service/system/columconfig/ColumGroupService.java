package cn.cebest.service.system.columconfig;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface ColumGroupService {

	/**保存
	 * @param po
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**修改
	 * @param po
	 * @throws Exception
	 */
	public void update(PageData pd)throws Exception;

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;

	/**查询
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd) throws Exception;

	/**查询
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findAll(PageData pd) throws Exception;

	List<PageData> gropulistPage(Page page) throws Exception;

	/**根据名称查询重复验证
	 * @param pd
	 * @throws Exception
	 * @author liuzhule
	 */
	String findByName(PageData pd)throws Exception;

	List<PageData> findPermAllList(PageData pd) throws Exception;
	
	/**
	 * 复制整站栏目组之前，如果执行过复制，再次执行需要先根据网站ID批量删除之前复制的栏目组
	 * @param siteID
	 * @throws Exception
	 */
	public void deleteBeforCopy(String siteID) throws Exception;

	


}
