package cn.cebest.service.system.employ;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.EmployField;
import cn.cebest.entity.system.Recruit;
import cn.cebest.util.PageData;

public interface EmployService {

	List<PageData> listPage(Page page) throws Exception;

	void del(PageData pd) throws Exception;

	void delAll(String[] arrayEmploy_IDS) throws Exception;

	void save(PageData pd) throws Exception;

	PageData findById(PageData pd) throws Exception;

	void edit(PageData pd) throws Exception;

	void recommend(PageData pd) throws Exception;

	void top(PageData pd) throws Exception;
	/**
	 * 根据栏目ids批量删除
	 */
   void deleteByColumnIds(String[] ids) throws Exception;
	/**
	 * 查询各个栏目下置顶的产品
	 */
	List<PageData> findTopList() throws Exception;
	/**
	 * 根据id查询模板路径
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	PageData findTemplatePachById(String contentId)throws Exception;
	
	List<Recruit> findlistPageAllByColumId(Page page)throws Exception;

	/**
	 * 查询当前站点下 栏目类型=4(招聘栏目)的所有栏目
	 * @author liu
	 */
	List<PageData> findColum(PageData pd)throws Exception;

	/**
	 * 通过招聘栏目id查询有关数据
	 * @author liu
	 */
	List<PageData> selectlistPageByColumIDD(Page page) throws Exception;
	
	//江铜接口开始=========================
	List<Recruit> findlistPageByColumId(Page page) throws Exception;

	void updateStatusByIds(PageData pd) throws Exception;

	void hot(PageData pd) throws Exception;

	void updateSort(PageData pd) throws Exception;
	
	//江铜接口结束=======================
	
	/**
	 * 查找职位基础字段
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<EmployField> findlistEmployFields(Page page) throws Exception;

	Recruit findRecruitByTypeOrColumnid(PageData pd) throws Exception;

}
