package cn.cebest.entity.bo;

import java.util.Map;

/**
 * 
* 类名称：角色
 */
public class RoleVo {
	
	private String roleId;
	private String roleName;
	private String description;
	private String state;
	private Map<String,String[]> siteMenuMap;
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Map<String, String[]> getSiteMenuMap() {
		return siteMenuMap;
	}
	public void setSiteMenuMap(Map<String, String[]> siteMenuMap) {
		this.siteMenuMap = siteMenuMap;
	}
	
}
