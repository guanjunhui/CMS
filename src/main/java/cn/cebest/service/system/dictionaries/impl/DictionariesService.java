package cn.cebest.service.system.dictionaries.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.Dictionaries;
import cn.cebest.service.system.dictionaries.DictionariesManager;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

/** 
 * 说明： 数据字典
 * 创建人：中企高呈
 * 创建时间：2015-12-16
 * @version
 */
@Service("dictionariesService")
public class DictionariesService implements DictionariesManager{
	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DictionariesMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DictionariesMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DictionariesMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DictionariesMapper.datalistPage", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictionariesMapper.findById", pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictionariesMapper.findByBianma", pd);
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionaries> listSubDictByParentId(String parentId) throws Exception {
		return (List<Dictionaries>) dao.findForList("DictionariesMapper.listSubDictByParentId", parentId);
	}
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String contextPath,String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		String adminPath=contextPath+Const.ADMIN_PREFIX;
		for(Dictionaries dict : dictList){
			dict.setTreeurl(adminPath+"dictionaries/list.do?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(contextPath,dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DictionariesMapper.findFromTbs", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dictionaries listParentAndChildDict(String BIANMA) throws Exception {
		Dictionaries dictionaries=this.findByBianmaPoJo(BIANMA);
		if(dictionaries==null){
			logger.warn("warn:no record find by bianma["+BIANMA+"]");
			return null;
		}
		List<Dictionaries> childList=(List<Dictionaries>) dao.findForList("DictionariesMapper.listChildDict", dictionaries.getDICTIONARIES_ID());
		dictionaries.setSubDict(childList);
		return dictionaries;
	}

	@Override
	public Dictionaries findByBianmaPoJo(String BIANMA) throws Exception {
		return (Dictionaries)dao.findForObject("DictionariesMapper.findByBianmaPoJo", BIANMA);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dictionaries> findAll() throws Exception {
		return (List<Dictionaries>) dao.findForList("DictionariesMapper.findAll", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tree> findAllChildByBianma(String BIANMA) throws Exception {
		List<Dictionaries> all=(List<Dictionaries>) dao.findForList("DictionariesMapper.findAll", null);
		Dictionaries partent=(Dictionaries)dao.findForObject("DictionariesMapper.findByBianmaPoJo", BIANMA);
		List<Tree> treeList=new ArrayList<Tree>();
		for(int i=0;i<all.size();i++){
			Dictionaries child=all.get(i);
			if(child.getPARENT_ID().equals(partent.getDICTIONARIES_ID())){
				Tree tree=convertTree(child,true);
				treeList.add(tree);
				all.remove(i);
				i--;
				eachChild(tree,treeList,all);
			}
		}
		
		return treeList;
	}
	
	private static void eachChild(Tree tree,List<Tree> treeList,List<Dictionaries> all){
		for(int i=0;i<all.size();i++){
			Dictionaries child=all.get(i);
			if(child.getPARENT_ID().equals(tree.getId())){
				Tree childTree=convertTree(child,false);
				treeList.add(childTree);
				all.remove(i);
				i--;
				eachChild(childTree,treeList,all);
			}
		}
	}
	
	
	private static Tree convertTree(Dictionaries po,boolean isParent){
		Tree tree=new Tree();
		tree.setId(po.getDICTIONARIES_ID());
		if(isParent){
			tree.setPid("");
		}else{
			tree.setPid(po.getPARENT_ID());
		}
		tree.setName(po.getNAME());
		return tree;
	}

	@Override
	public String findNameById(String id) throws Exception {
		return (String) dao.findForObject("DictionariesMapper.findNameById", id);
	}

}

