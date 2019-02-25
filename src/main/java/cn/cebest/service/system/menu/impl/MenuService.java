package cn.cebest.service.system.menu.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.Menu;
import cn.cebest.service.system.menu.MenuManager;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

/** 
 * 类名称：MenuService 菜单处理
 * 创建人：qichangxin
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("menuService")
public class MenuService implements MenuManager{
	
	public static final String NODETYPE = "nodeType";
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 通过ID获取其子一级菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listAllSubMenuByParentId(String parentId) throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listAllSubMenuByParentId", parentId);
	}
	
	@Override
	public List<Menu> listDisplaySubMenuByParentId(String parentId) throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listDisplaySubMenuByParentId", parentId);
	}
	
	/**
	 * 通过菜单ID获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MenuMapper.getMenuById", pd);
	}
	
	/**
	 * 新增菜单
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception {
		dao.save("MenuMapper.insertMenu", menu);
	}
	
	/**
	 * 取最大ID
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMaxId(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MenuMapper.findMaxId", pd);
	}
	
	/**
	 * 删除菜单
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception {
		dao.save("MenuMapper.deleteMenuById", MENU_ID);
	}
	
	/**
	 * 编辑
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception {
		 dao.update("MenuMapper.updateMenu", menu);
	}
	
	/**
	 * 保存菜单图标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception {
		return (PageData)dao.findForObject("MenuMapper.editicon", pd);
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(String contextPath,String MENU_ID) throws Exception {
		List<Menu> menuList = this.listAllSubMenuByParentId(MENU_ID);
		String adminPath=contextPath+Const.ADMIN_PREFIX;
		for(Menu menu : menuList){
			menu.setMENU_URL(adminPath+"menu/toEdit.do?MENU_ID="+menu.getMENU_ID());
			menu.setSubMenu(this.listAllMenu(contextPath,menu.getMENU_ID()));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}

	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单列表)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQx(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listAllSubMenuByParentId(MENU_ID);
		for(Menu menu : menuList){
			menu.setSubMenu(this.listAllMenuQx(menu.getMENU_ID()));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}

	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单列表)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listDisplayMenuQx(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listDisplaySubMenuByParentId(MENU_ID);
		for(Menu menu : menuList){
			menu.setSubMenu(this.listDisplayMenuQx(menu.getMENU_ID()));
			menu.setTarget("treeFrame");
		}
		return menuList;
	}
	
	@Override
	public List<Menu> listAll() throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listAll", null);
	}

	@Override
	public List<Menu> listTop() throws Exception {
		return (List<Menu>) dao.findForList("MenuMapper.listTop", null);
	}

	@Override
	public List<Menu> listTopAndChilren() throws Exception {
		List<Menu> allList=this.listAll();
		List<Menu> topList=this.listTop();
		if(CollectionUtils.isEmpty(allList)
				|| CollectionUtils.isEmpty(topList)){
			return null;
		}
		List<Menu> resultList=new ArrayList<Menu>();
		for(Menu top:topList){
			resultList.add(top);
			this.appendChild(top, allList);
		}
		return resultList;
	}
	
	private void appendChild(Menu menu,List<Menu> allList){
		for(int i=0;i<allList.size();i++){
			Menu po=allList.get(i);
			if(po.getPARENT_ID().equals(menu.getMENU_ID())){
				if(CollectionUtils.isEmpty(menu.getSubMenu())){
					menu.setSubMenu(new ArrayList<Menu>());
				}
				menu.getSubMenu().add(po);
				allList.remove(i);
				i--;
				this.appendChild(po, allList);
			}
		}
	}

	@Override
	public List<String> getMenuIdsById(PageData pd) throws Exception {
		return (List<String>)dao.findForList("UserMapper.getMenuIdsById", pd);
	}
	/**
	 * 权限调整
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Tree> listTreeTopAndChilren() throws Exception {
		List<Menu> allList=this.listAll();
		List<Menu> topList=this.listTop();
		if(CollectionUtils.isEmpty(allList)
				|| CollectionUtils.isEmpty(topList)){
			return null;
		}
		
		List<Tree> resultList=new ArrayList<Tree>();
		for(Menu top:topList){
			Tree toptree=this.convert(top);
			resultList.add(toptree);
			this.appendTreeChild(toptree, allList);
		}
		return resultList;
	}
	private void appendTreeChild(Tree menu,List<Menu> allList){
		for(int i=0;i<allList.size();i++){
			Menu po=allList.get(i);
			if(po.getPARENT_ID().equals(menu.getId())){
				if(CollectionUtils.isEmpty(menu.getChildList())){
					menu.setChildList(new ArrayList<Tree>());
				}
				Tree child=this.convert(po);
				menu.getChildList().add(child);
				allList.remove(i);
				i--;
				this.appendTreeChild(child, allList);
			}
		}
	}
	public Tree convert(Menu menu) {
		Tree tree = new Tree();
		tree.setId(menu.getMENU_ID());
		tree.setName(menu.getMENU_NAME());
		tree.setPid(menu.getPARENT_ID());
		tree.getAttribute().put(NODETYPE, menu.getNodeType());
		return tree;
	}
}
