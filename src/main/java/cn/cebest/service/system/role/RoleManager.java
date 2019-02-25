package cn.cebest.service.system.role;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.RolePermissionBo;
import cn.cebest.entity.bo.SiteMain;
import cn.cebest.entity.bo.UserRole;
import cn.cebest.entity.system.Role;
import cn.cebest.entity.system.RolePermission;
import cn.cebest.util.PageData;

/**	角色接口类
 * @author qichangxin
 * 修改日期：2015.11.6
 */
public interface RoleManager {
	
	/**列出此组下级角色
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllRolesByPId(PageData pd) throws Exception;
	
	public List<Role> listAllRoles(Page page) throws Exception;

	public List<Role> getAllRoles() throws Exception;
	
	/**通过id查找
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findObjectById(PageData pd) throws Exception;
	
	/**添加
	 * @param pd
	 * @throws Exception
	 */
	public void add(PageData pd) throws Exception;
	
	/**保存修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;
	
	/**删除角色
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRoleById(String ROLE_ID) throws Exception;
	
	/**给当前角色附加菜单权限
	 * @param role
	 * @throws Exception
	 */
	public void updateRoleRights(Role role) throws Exception;
	
	/**通过id查找
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public Role getRoleById(String ROLE_ID) throws Exception;
	
	/**给全部子角色加菜单权限
	 * @param pd
	 * @throws Exception
	 */
	public void setAllRights(PageData pd) throws Exception;
	
	/**权限(增删改查)
	 * @param msg 区分增删改查
	 * @param pd
	 * @throws Exception
	 */
	public void saveB4Button(String msg,PageData pd) throws Exception;

	
	/**批量插入角色-站点-菜单映射
	 * @param RoleMenuBo
	 * @throws Exception
	 */
	public void deleteRolePermission(String roleId) throws Exception;
	public void batchSaveRolePermission(List<RolePermissionBo> list) throws Exception;

	/**切换状态
	 * @param pd
	 * @throws Exception
	 */
	public void changeStatus(PageData pd) throws Exception;

	/**根据用户ID查询站点ID集合
	 * @param pd
	 * @throws Exception
	 */
	public List<String> getSiteIdsByRoleId(String userId) throws Exception;
	
	/**根据用户ID查询站点权限值集合
	 * @param pd
	 * @throws Exception
	 */
	public List<String> getMenuPerCodesById(PageData pd) throws Exception;


	/**批量插入用户-角色映射关系
	 * @param UserRole
	 * @throws Exception
	 */
	public void deleteUserRole(String userId) throws Exception;
	public void batchsaveUserRole(List<UserRole> list) throws Exception;
	public List<String> getRoleIdsByUserId(String userId) throws Exception;

	/**根据角色查询权限映射信息
	 * @param roleId
	 * @throws Exception
	 */
	public List<RolePermission> getPermissionByRoleId(String roleId) throws Exception;

	/**通过用户ID获取用户对应权限的站点
	 * @param userId
	 * @throws Exception
	 */
	public List<SiteMain> getSitesByUserId(String userId) throws Exception;

	/**通过用户ID获取用户对应权限的站点
	 * @param userId,currentSiteId
	 * @throws Exception
	 */
	public List<SiteMain> getNoCurentSitesByUserId(String userId,String currentSiteId) throws Exception;

}
