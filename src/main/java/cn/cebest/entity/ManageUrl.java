package cn.cebest.entity;

import java.util.Set;

/**
 * @author qichangxin
 */
public class ManageUrl {
	
	private String managePrefix;
	private String manageLogin;
	private Set<String> manageNoLogin;

	public String getManagePrefix() {
		return managePrefix;
	}
	public void setManagePrefix(String managePrefix) {
		this.managePrefix = managePrefix;
	}
	public String getManageLogin() {
		return manageLogin;
	}
	public void setManageLogin(String manageLogin) {
		this.manageLogin = manageLogin;
	}
	public Set<String> getManageNoLogin() {
		return manageNoLogin;
	}
	public void setManageNoLogin(Set<String> manageNoLogin) {
		this.manageNoLogin = manageNoLogin;
	}
	
}
