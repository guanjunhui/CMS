package cn.cebest.service.system.infineon;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
public interface ContactPlanService {

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> ListPage(Page page) throws Exception ;

	/**
	 * @param id
	 * @return
	 */
	public Object detailById(String id)throws Exception ;

	/**
	 * @param pd
	 */
	public void updateHandle(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void deleteById(PageData pd)throws Exception;

	/**
	 * @param ids
	 */
	public void deleteByIds(String[] ids)throws Exception;

	/**
	 * @param pd
	 */
	public void savecontactPlan(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void collectionPlan(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void thumbPlan(PageData pd) throws Exception ;

	/**
	 * @param page
	 * @return
	 */
	public List<PageData> ListDetailPage(Page page) throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public String isThumb(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public String isCollectionPlan(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void cancelThumbPlan(PageData pd)throws Exception;

	/**
	 * @param pd
	 */
	public void cancelCollection(PageData pd)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public String getThumbNum(PageData pd)throws Exception;

	/**
	 * @return
	 */
	public List<Map<String, String>> findAll()throws Exception;

	/**
	 * @param pList
	 */
	public void inserts(List<Map<String, String>> pList)throws Exception;

}
