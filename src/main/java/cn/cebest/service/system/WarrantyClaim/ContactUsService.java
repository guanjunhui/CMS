package cn.cebest.service.system.WarrantyClaim;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月10日
 * @company 中企高呈
 */
public interface ContactUsService {

	/**新增联系我们表单
	 * @param pd
	 */
	void saveWarrantyClaim(PageData pd)throws Exception;

	/**后台列表方法
	 * @param page
	 * @return
	 */
	List<PageData> ListPage(Page page) throws Exception;

	/**
	 * @param pd
	 */
	void delContactUs(PageData pd) throws Exception;

	/**
	 * @param ids
	 */
	void delsContactUs(String[] ids) throws Exception ;

	/**根据id查询详情
	 * @param id
	 * @return
	 */
	Object detailById(String id) throws Exception ;

}
