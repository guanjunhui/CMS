package cn.cebest.service.system.WarrantyClaim;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
public interface UserService {

	/**用户注册
	 * @param pd
	 */
	int saveUser(PageData pd) throws Exception ;
	
	/**
	 * @param pd
	 * @param request
	 */
	Object queryUser(PageData pd) throws Exception ;

	/**
	 * @param email
	 * @return
	 */
	Integer sendEmail(String email,HttpServletRequest request) throws Exception;

	/**修改密码
	 * @param pd
	 */
	void upatePassword(PageData pd) throws Exception;

	/**后台列表
	 * @param page
	 * @return
	 */
	List<PageData> ListPage(Page page)throws Exception ;

	/**
	 * @param pd
	 */
	void delete(PageData pd) throws Exception ;

	/**
	 * @param ids
	 */
	void deletes(String[] ids) throws Exception ;

	/**
	 * @param id
	 * @return
	 */
	Object detailById(String id) throws Exception;

}
