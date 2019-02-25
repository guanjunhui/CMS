package cn.cebest.service.system.customForm;

import java.util.List;

import javax.servlet.http.HttpSession;

import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.util.PageData;

public interface CustomformAttributeValueService {

	/**添加表单属性值数据
	 * @param CustomformAttributeValue pd
	 * @throws Exception
	 */
	public void save(CustomformAttributeValue pd)throws Exception;
	
	/**批量添加表单属性值数据
	 * @param session 
	 * @param List<CustomformAttributeValue> list
	 * @throws Exception
	 */
	public void saveBatch(List<CustomformAttributeValue> list)throws Exception;


	/**根据表单项集合删除表单项值
	 * @param List<String> attrIds
	 * @throws Exception
	 */
	public void deleteByAttrIds(List<String> attrIds)throws Exception;
	
	/**根据创建时间删除表单项值
	 * @param String[] times
	 * @throws Exception
	 */
	public void deleteByTimes(String[] times)throws Exception;
	
	/**根据表单项ID删除表单项值
	 * @param String attrId
	 * @throws Exception
	 */
	public void deleteByAttrId(String attrId)throws Exception;

	public CustomFormVo selectformAttributeValueList(PageData pd)throws Exception;

	void updateformAttributeValue(PageData pd) throws Exception;

}
