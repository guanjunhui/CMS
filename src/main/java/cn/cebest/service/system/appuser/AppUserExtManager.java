package cn.cebest.service.system.appuser;

import cn.cebest.util.PageData;

/** 会员信息扩展类类
 * @author 中企高呈
 * 修改时间：2017.09.11
 */
public interface AppUserExtManager {

	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception;
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception;

	/**上传头像
	 * @param pd
	 * @throws Exception
	 */
	public void saveImage(PageData pd)throws Exception;

}
