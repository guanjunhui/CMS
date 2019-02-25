package cn.cebest.service.web.survey.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.survey.Survey;
import cn.cebest.service.web.survey.SurveyManager;
import cn.cebest.util.PageData;
/** 
 * 说明：用户调查
 * 创建人：qiaozhipeng
 * 创建时间：2017-09-20
 */
@Service("surveyService")
public class SurveyService implements SurveyManager {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SurveyMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SurveyMapper.delete", pd);
		dao.delete("SurveyMapper.deleteQuestionBySurveyId", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SurveyMapper.edit", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SurveyMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SurveyMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SurveyMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SurveyMapper.deleteAll", ArrayDATA_IDS);
	}
	/**根据id深度查询问卷调查
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public Survey getSurveyDeeply(PageData pd) throws Exception {
		return (Survey)dao.findForObject("SurveyMapper.getSurveyDeeply", pd);
	}
	/**根据id查询问卷调查结果
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public Survey getSurveyAndAnswer(PageData pd) throws Exception {
		return (Survey)dao.findForObject("SurveyMapper.getSurveyResult", pd);
	}

	@Override
	public void check(PageData pd) throws Exception {
		dao.update("SurveyMapper.check", pd);
	}

	@Override
	public void checkAll(String[] arrayDATA_IDS) throws Exception {
		dao.update("SurveyMapper.checkAll", arrayDATA_IDS);
	}
}
