package cn.cebest.entity.system;

public class RolePermission {

	private String roleId;
	
	private String siteId;
	
	private String menuId;
	
	private String type;

	public RolePermission() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RolePermission(String roleId, String siteId, String menuId, String type) {
		super();
		this.roleId = roleId;
		this.siteId = siteId;
		this.menuId = menuId;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
}
