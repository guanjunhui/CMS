package cn.cebest.util.security;

import org.apache.shiro.authc.UsernamePasswordToken;

@SuppressWarnings("serial")
public class UsernamePasswordExtendToken extends UsernamePasswordToken{
	
	private boolean isAdmin;

	public UsernamePasswordExtendToken(String username, String password,boolean isAdmin) {
		super(username,password);
		this.isAdmin=isAdmin;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
