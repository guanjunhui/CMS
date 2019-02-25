package cn.cebest.service.system.appuser.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.service.system.appuser.AppUserExtManager;
import cn.cebest.util.PageData;


/**类名称：会员扩展类
 * @author 中企高呈
 * 修改时间：2017年09月11日
 */
@Service
public class AppUserExtService implements AppUserExtManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception{
		dao.save("AppUserExtMapper.saveU", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("AppUserExtMapper.deleteU", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("AppUserExtMapper.editU", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUserExtMapper.findByUiId", pd);
	}

	@Override
	public void saveImage(PageData pd) throws Exception {
		dao.update("AppUserExtMapper.saveImage", pd);
	}
	
	
}

