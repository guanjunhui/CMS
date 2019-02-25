package cn.cebest.service.system.WarrantyClaim;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月24日
 * @company 中企高呈
 */
public interface EmailCollectService {

	/**保存邮箱
	 * @param pd
	 * @return
	 */
	Integer save(PageData pd)throws Exception;

	/**
	 * @param page
	 * @return
	 */
	List<PageData> ListPage(Page page)throws Exception;

	/**
	 * @param pd
	 */
	void delete(PageData pd)throws Exception ;

	/**
	 * @param ids
	 */
	void deletes(String[] ids)throws Exception ;

}
