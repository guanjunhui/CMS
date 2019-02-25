package cn.cebest.service.system.role.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.RolePermissionBo;
import cn.cebest.entity.bo.SiteMain;
import cn.cebest.entity.bo.UserRole;
import cn.cebest.entity.system.Role;
import cn.cebest.entity.system.RolePermission;
import cn.cebest.service.system.role.RoleManager;
import cn.cebest.util.PageData;

/**	角色
 * @author qichangxin
 * 修改日期：2015.11.6
 */
@Service("roleService")
public class RoleService implements RoleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**列出此组下级角色
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Role> listAllRolesByPId(PageData pd) throws Exception {
		return (List<Role>) dao.findForList("RoleMapper.listAllRolesByPId", pd);
	}
	@Override
	public List<Role> listAllRoles(Page page) throws Exception {
		return (List<Role>) dao.findForList("RoleMapper.listPageAllRoles", page);
	}
	/**通过id查找
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findObjectById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("RoleMapper.findObjectById", pd);
	}
	
	/**添加
	 * @param pd
	 * @throws Exception
	 */
	public void add(PageData pd) throws Exception {
		dao.save("RoleMapper.insert", pd);
	}
	
	/**保存修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		dao.update("RoleMapper.edit", pd);
	}
	
	/**删除角色
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRoleById(String ROLE_ID) throws Exception {
		dao.delete("RoleMapper.deleteRoleById", ROLE_ID);
	}
	
	/**给当前角色附加菜单权限
	 * @param role
	 * @throws Exception
	 */
	public void updateRoleRights(Role role) throws Exception {
		dao.update("RoleMapper.updateRoleRights", role);
	}
	
	/**通过id查找
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public Role getRoleById(String ROLE_ID) throws Exception {
		return (Role) dao.findForObject("RoleMapper.getRoleById", ROLE_ID);
	}
	
	/**给全部子角色加菜单权限
	 * @param pd
	 * @throws Exception
	 */
	public void setAllRights(PageData pd) throws Exception {
		dao.update("RoleMapper.setAllRights", pd);
	}
	
	/**权限(增删改查)
	 * @param msg 区分增删改查
	 * @param pd
	 * @throws Exception
	 */
	public void saveB4Button(String msg,PageData pd) throws Exception {
		dao.update("RoleMapper."+msg, pd);
	}

	
	@Override
	public void changeStatus(PageData pd) throws Exception {
		dao.update("RoleMapper.changeStatus", pd);
	}
	@Override
	public List<Role> getAllRoles() throws Exception {
		return (List<Role>) dao.findForList("RoleMapper.getAllRoles", null);
	}
	@Override
	public void deleteRolePermission(String roleId) throws Exception {
		dao.delete("RoleMapper.deleteRolePermission", roleId);
	}
	@Override
	public void batchSaveRolePermission(List<RolePermissionBo> list) throws Exception {
		dao.batchSave("RoleMapper.batchSaveRolePermission", list);
	}
	@Override
	public List<String> getSiteIdsByRoleId(String userId) throws Exception {
		return (List<String>) dao.findForList("RoleMapper.getSiteIdsByRoleId", userId);
	}
	@Override
	public void deleteUserRole(String userId) throws Exception {
		dao.delete("RoleMapper.deleteUserRole", userId);
	}
	@Override
	public void batchsaveUserRole(List<UserRole> list) throws Exception {
		dao.batchSave("RoleMapper.batchsaveUserRole", list);
	}
	@Override
	public List<String> getRoleIdsByUserId(String userId) throws Exception {
		return (List<String>) dao.findForList("RoleMapper.getRoleIdsByUserId", userId);
	}
	@Override
	public List<String> getMenuPerCodesById(PageData pd) throws Exception {
		return (List<String>) dao.findForList("RoleMapper.getMenuPerCodesById", pd);
	}
	@Override
	public List<RolePermission> getPermissionByRoleId(String roleId) throws Exception {
		return (List<RolePermission>) dao.findForList("RoleMapper.getPermissionByRoleId", roleId);
	}
	@Override
	public List<SiteMain> getSitesByUserId(String userId) throws Exception {
		return (List<SiteMain>) dao.findForList("RoleMapper.getSitesByUserId", userId);
	}
	@Override
	public List<SiteMain> getNoCurentSitesByUserId(String userId,String currentSiteId) throws Exception {
		PageData pd=new PageData();
		pd.put("userId", userId);
		pd.put("currentSiteId", currentSiteId);
		return (List<SiteMain>) dao.findForList("RoleMapper.getNoCurentSitesByUserId", pd);
	}

}
