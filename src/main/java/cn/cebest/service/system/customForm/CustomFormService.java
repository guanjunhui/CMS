package cn.cebest.service.system.customForm;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.customForm.CustomForm;
import cn.cebest.util.PageData;

public interface CustomFormService {

	/**根据主键获取表单信息
	 * @param id
	 * @throws Exception
	 */
	public CustomForm selectByPrimaryKey(String id) throws Exception;
	
	/**获取表单项
	 * @param pd
	 * @throws Exception
	 */
	public List<CustomForm> selectAllFormAttribue(PageData pd)throws Exception;
	
	/**获取表单项
	 * @param pd
	 * @throws Exception
	 */
	public List<CustomForm> selectlistPageAllFormAttribue(Page page)throws Exception;


	/**获取表单项树形结构
	 * @param pd
	 * @throws Exception
	 
	public List<ZTree> selectAllFormTreeAttribue(PageData pd)throws Exception;*/

	/**添加表单信息
	 * @param pd
	 * @throws Exception
	 */
	public void saveForm(CustomForm pd)throws Exception;

	/**更新表单信息
	 * @param pd
	 * @throws Exception
	 */
	public void updateForm(CustomForm pd)throws Exception;
	
	/**根据主键删除表单信息
	 * @param id
	 * @throws Exception
	 */
	public void deleteByPrimaryKey(String id) throws Exception;

	

}
