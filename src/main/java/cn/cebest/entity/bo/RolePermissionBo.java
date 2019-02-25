package cn.cebest.entity.bo;

public class RolePermissionBo {

	private String roleId;
	
	private String siteId;
	
	private String menuId;

	private String type;
	
	public RolePermissionBo(){}
	
	public RolePermissionBo(String roleId,String siteId,String menuId){
		this.roleId=roleId;
		this.siteId=siteId;
		this.menuId=menuId;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
