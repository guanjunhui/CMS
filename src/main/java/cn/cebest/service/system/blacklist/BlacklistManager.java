package cn.cebest.service.system.blacklist;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

/** 
 * 说明： 访客IP黑名单接口
 * 创建人：中企高呈
 * 创建时间：2017-06-27
 * @version
 */
public interface BlacklistManager{

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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**通过ip获取数据
	 * @param pd
	 * @throws Exception
	 */
	public Integer findByIp(PageData pd)throws Exception;
	
}

