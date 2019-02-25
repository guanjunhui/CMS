package cn.cebest.service.web.survey;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.survey.Survey;
import cn.cebest.util.PageData;
/** 
 * 说明：用户调查
 * 创建人：qiaozhipeng
 * 创建时间：2017-09-20
 */
public interface SurveyManager {
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**根据id查询调查详细信息
	 * @throws Exception
	 */
	public Survey getSurveyDeeply(PageData pd) throws Exception;
	/**根据id查询调查结果信息
	 * @throws Exception
	 */
	public Survey getSurveyAndAnswer(PageData pd) throws Exception;

	public void check(PageData pd) throws Exception;

	public void checkAll(String[] arrayDATA_IDS) throws Exception;
}
