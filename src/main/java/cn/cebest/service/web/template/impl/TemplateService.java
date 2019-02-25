package cn.cebest.service.web.template.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.TemplateTree;
import cn.cebest.entity.system.Template;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

/** 
 * 说明：模板管理
 * 创建人：Qiaozhipeng
 * 创建时间：2017-09-27
 */
@Service("templateService")
public class TemplateService implements TemplateManager {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("TemplateMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("TemplateMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("TemplateMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TemplateMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TemplateMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TemplateMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TemplateMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public Integer check(String[] ArrayDATA_IDS)throws Exception {
		return (Integer)dao.findForObject("TemplateMapper.check", ArrayDATA_IDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateTree> listAllTree(PageData pd) throws Exception {
		List<PageData> listAll =(List<PageData>)dao.findForList("TemplateMapper.listAll", pd);
		List<TemplateTree> treeList=new ArrayList<TemplateTree>();
		TemplateTree tree_1=new TemplateTree();
		PageData columnGroup = (PageData) dao.findForObject("ColumGroupMapper.selectObject", pd);
		tree_1.setId(pd.getString("COLUMGROUP_ID"));
		tree_1.setName(columnGroup.getString("COLUM_GROUPNAME"));
		treeList.add(tree_1);
		if(CollectionUtils.isNotEmpty(listAll)){
			for(PageData po:listAll){
				tree_1.getChildList().add(convertTree(po));
			}
		}
		return treeList;
	}
	
	private TemplateTree convertTree(PageData pd){
		TemplateTree tree=new TemplateTree();
		tree.setId(pd.getString("ID"));
		tree.setName(pd.getString("TEM_NAME"));
		String isDefault=pd.getString("IS_DEFAULT");
		if(Const.YES.equals(isDefault)){
			tree.setHasDefault(true);
		}else{
			tree.setHasDefault(false);
		}
		return tree;
	}

	@Override
	public List<PageData> findListByDefinedType(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("TemplateMapper.findListByDefinedType", pd);
	}

	@Override
	public List<TemplateTree> findTreesByDefinedType(PageData pd) throws Exception {
		List<PageData> list=(List<PageData>)dao.findForList("TemplateMapper.findListByDefinedType", pd);
		List<TemplateTree> treeList=new ArrayList<TemplateTree>();
		if(CollectionUtils.isNotEmpty(list)){
			for(PageData po:list){
				treeList.add(convertTree(po));
			}
			return treeList;
		}
		
		return null;
	}

	@Override
	public void resetDefault(String type) throws Exception {
		dao.update("TemplateMapper.resetDefault", type);
	}

	@Override
	public void updateDefault(String id) throws Exception {
		dao.update("TemplateMapper.updateDefault", id);
	}

	@Override
	public Template findPoById(String id) throws Exception {
		return (Template)dao.findForObject("TemplateMapper.findPoById", id);
	}
	
	@Override
	public Template findSitePoById(String id) throws Exception {
		return (Template)dao.findForObject("TemplateMapper.findSitePoById", id);
	}
	
	@Override
	public List<Template> listAllForCopy(String siteID) throws Exception {
		return (List<Template>)dao.findForList("TemplateMapper.listAllForCopy", siteID);
	}
	@Override
	public void insertBatchTemplate(List<Template> list) throws Exception{
		dao.batchSave("TemplateMapper.insertBatch", list);
	}
	@Override
	public void deleteBeforCopy(String siteID)throws Exception{
		dao.delete("TemplateMapper.deleteBeforCopy", siteID);
	}
}
