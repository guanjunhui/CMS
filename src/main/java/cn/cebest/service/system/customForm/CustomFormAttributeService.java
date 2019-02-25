package cn.cebest.service.system.customForm;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.util.PageData;

public interface CustomFormAttributeService {

	/**添加表单项信息
	 * @param pd
	 * @throws Exception
	 */
	public void save(CustomFormAttribute pd)throws Exception;

	/**更新表单项信息
	 * @param pd
	 * @throws Exception
	 */
	public void update(CustomFormAttribute pd)throws Exception;

	/**获取表单项信息
	 * @param pd
	 * @throws Exception
	 */
	public CustomFormAttribute getObjectById(String id)throws Exception;
	
	/**根据表单ID获取表单项以及值的集合
	 * @param formId
	 * @throws Exception
	 */
	public List<CustomFormAttribute> getAttributeAndValueListByFormID(String formId)throws Exception;

	/**根据主键删除信息
	 * @param id
	 * @throws Exception
	 */
	public void deleteByPrimaryKey(String id)throws Exception;

	/**根据表单ID删除信息
	 * @param formId
	 * @throws Exception
	 */
	public void deleteByFormId(String formId)throws Exception;

	/**根据表单ID查询表单项的ID集合
	 * @param formId
	 * @throws Exception
	 */
	public List<String> selectAttrIdsByFormID(String formId) throws Exception;
	
	/**根据表单ID查询表单项（启用）-分页
	 * @param Page page
	 * @throws Exception
	 */
	public List<CustomFormAttribute> getAttributelistPageByFormID(Page page)throws Exception;
	
	/**根据表单ID查询表单项（所有）-分页
	 * @param Page page
	 * @throws Exception
	 */
	public List<CustomFormAttribute> getAttributeAlllistPageByFormID(Page page)throws Exception;

	/**根据表单ID查询表单项（仅显示项）
	 * @param Page page
	 * @throws Exception
	 */
	public List<CustomFormAttribute> getAttributeListByFormID(String formId)throws Exception;
	
	/**
	 * 根据表单ID查询表单项(全部)
	 * @param Page page
	 * @throws Exception
	 */
	public List<CustomFormAttribute> getAttributeListAllByFormID(String formId)throws Exception;

	/**根据表单ID获取表单项以及值的集合
	 * 并转换为头部以及值的对应集合
	 * @param page
	 * @throws Exception
	 */
	public CustomFormVo getAttributeAndValuePageByFormID(Page page)throws Exception;
	
	/**根据表单ID导出数据
	 * @param PageData
	 * @throws Exception
	 */
	public CustomFormVo getAttributeAndValueByFormID(PageData pd)throws Exception;

	/**根据表单ID以及检索值获取表单项以及值的集合
	 * 并转换为头部以及值的对应集合
	 * @param String formId
	 * @param String condition
	 * @throws Exception
	 */
	public CustomFormVo getAttributeAndValueByFormIdAndCondition(String formId,String condition)throws Exception;

	CustomFormVo getAttributeAndValueByFormIdAndAddress(String formId, String condition) throws Exception;

}
