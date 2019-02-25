package cn.cebest.service.information.pictures;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;


/** 图片管理接口
 * @author 中企高呈
 * 修改时间：2015.11.2
 */
public interface PicturesManager {
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**广告位列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> bannerList(Page page)throws Exception;
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**批量获取
	 * @param ArrayDATA_IDS
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getAllById(String[] ArrayDATA_IDS)throws Exception;
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	public void delTp(PageData pd)throws Exception;

	/**根据栏目ID批量删除图片
	 * @param pd
	 * @throws Exception
	 * @author liu 20171219
	 */
	public void delPicutre(String[] ids) throws Exception;

	
	
}

