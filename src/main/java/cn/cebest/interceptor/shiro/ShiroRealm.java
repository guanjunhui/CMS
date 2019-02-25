package cn.cebest.interceptor.shiro;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cebest.entity.web.WebSite;
import cn.cebest.interceptor.CurrentThreadVariable;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.service.system.role.RoleManager;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.security.UsernamePasswordExtendToken;


/**
 * @author qichangxin
 *  2015-3-6
 */
public class ShiroRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

	
	/*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 String username = (String)token.getPrincipal();//得到用户名 
	     //String enterPassword = new String((char[])token.getCredentials()); 	//得到密码
		 String password="";
		 String salt="";
		 PageData pd=null;
		 if(token instanceof UsernamePasswordExtendToken){
			 UsernamePasswordExtendToken customToken=(UsernamePasswordExtendToken) token;
			 if(customToken.isAdmin()){
				 try {
					pd=this.userManager.findByUsername(new PageData("USERNAME",token.getPrincipal()));
					if(pd==null||pd.isEmpty())
						throw new UnknownAccountException("userName["+token.getPrincipal()+"] does not exist!");
				 } catch (Exception e) {
					 logger.error("findByUsername occured error in shiro realm!", e);
				 }
			 }else{
				try {
					pd=this.appuserManager.findByUsername(new PageData("USERNAME",token.getPrincipal()));
				} catch (Exception e) {
					 logger.error("error hapeend in findByAppUsername", e);
				}
			 }
		 }else{
			 throw new RuntimeException("token into shiro realm occured error!");
		 }
		 
		password= pd.getString("PASSWORD");//得到密码
		salt=pd.getString("SALT");

	     if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
	    	 ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
	    	 SimpleAuthenticationInfo ai= new SimpleAuthenticationInfo(username, 
	    			 password, credentialsSalt,getName());
	    	 return ai;
	     }else{
	    	 return null;
	     }
	}
	
	/*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		String username = (String) principals.getPrimaryPrincipal();
		PageData user=null;
		try {
			user = userManager.findByUsername(new PageData("USERNAME",username));
		} catch (Exception e) {
			 logger.error("error hapeend in findByAppUsername", e);
		}
		WebSite currentWebSite=CurrentThreadVariable.getSite();
		if(currentWebSite==null){
			throw new RuntimeException("the current site does not find!");
		}
		String currentSiteId=currentWebSite.getId();
		//判断是否拥有当前站点权限
		if(ishashSitePerm(user,currentSiteId)){
			auth.setStringPermissions(this.getPerms(user, currentSiteId));
		}
		return auth;
	}

	public boolean ishashSitePerm(PageData user,String siteId){
		String userName=user.getString("USERNAME");
		if(Const.SUPER_ADMIN_NAME.equals(userName)){
			return true;
		}

		String userId=user.getString("USER_ID");
		boolean isCurrentSitePerm=false;
		//判断是否拥有当前站点权限
		List<String> siteList=null;
		try {
			siteList = roleService.getSiteIdsByRoleId(userId);
		} catch (Exception e) {
			 logger.error("error hapeend in getPermissionByRoleId", e);
		}
		if(CollectionUtils.isNotEmpty(siteList)){
			for(String id:siteList){
				if(siteId.equals(id)){
					isCurrentSitePerm=true;
				}
			}
		}
		return isCurrentSitePerm;
	}
	
	public Set<String> getPerms(PageData user,String siteId){
		Set<String> perms =new HashSet<String>();
		if(Const.SUPER_ADMIN_NAME.equals(user.getString("USERNAME"))){
			perms.add("*");
		}else{
			PageData pd = new PageData();
			pd.put("USER_ID", user.getString("USER_ID"));
			pd.put("SITE_ID", siteId);
			List<String> permCodeList=null;
			try {
				permCodeList=roleService.getMenuPerCodesById(pd);
			} catch (Exception e) {
				 logger.error("get user-menu relations ocuured error", e);
			}
			if(CollectionUtils.isNotEmpty(permCodeList)){
				HashSet<String> permList=new HashSet<String>();
				for(String code:permCodeList){
					if(StringUtils.isNotEmpty(code)&&!"#".equals(code)){
						permList.add(code);
					}
				}
				perms.addAll(permList);
			}
		}
		return perms;
	}
	
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }
	@Autowired
	private UserManager userManager;
	@Autowired
	private AppuserManager appuserManager;
	@Autowired
	private RoleManager roleService;
}
