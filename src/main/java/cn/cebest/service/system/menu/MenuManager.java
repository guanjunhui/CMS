package cn.cebest.service.system.menu;

import java.util.List;

import cn.cebest.entity.Tree;
import cn.cebest.entity.system.Menu;
import cn.cebest.util.PageData;


/**说明：MenuService 菜单处理接口
 * @author qichangxin
 */
public interface MenuManager {

	/**
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllSubMenuByParentId(String parentId)throws Exception;
	
	/**
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listDisplaySubMenuByParentId(String parentId)throws Exception;

	
	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception;
	
	/**
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception;
	
	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMaxId(PageData pd) throws Exception;
	
	/**
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception;
	
	/**
	 * @param menu
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception;
	
	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception;
	
	/**
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(String contextPath,String MENU_ID) throws Exception;
	
	/**
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQx(String MENU_ID) throws Exception;
	
	/**
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listDisplayMenuQx(String MENU_ID) throws Exception;

	
	public List<Menu> listAll() throws Exception;
	
	public List<Menu> listTop() throws Exception;
	
	public List<Menu> listTopAndChilren() throws Exception;
	
	/**通过用户ID获取具有权限的MenuId集合
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<String> getMenuIdsById(PageData pd)throws Exception;

	List<Tree> listTreeTopAndChilren() throws Exception;


}
