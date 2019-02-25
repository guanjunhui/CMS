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
public interface CommitPlanService {

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
	public void saveCommitPlan(PageData pd)throws Exception;

	/**
	 * @return
	 */
	public List<Map<String, String>> findAll() throws Exception;

}
