package cn.cebest.service.system.txt;

import cn.cebest.util.PageData;

/** 
 * 说明：  BLOB
 * 创建人：qichangxin
 * 创建时间：2017-09-20
 * @version
 */
public interface TxtService {

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
	
	/**查找
	 * @param po
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**删除
	 * @param po
	 * @throws Exception
	 */
	void del(String id) throws Exception;

	void delAll(String[] ids) throws Exception;

}
