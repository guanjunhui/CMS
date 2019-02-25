package cn.cebest.interceptor.shiro;

import java.util.Iterator;
 
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 *
 * @author wangweijie
 * @Date 2018年9月6日
 * @company 中企高呈
 */
@SuppressWarnings("deprecation")
public class MyCasRealm extends CasRealm{
    /**
     * 授权，获取用户的角色、权限
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }
        Iterator it = principals.fromRealm(getName()).iterator();
        String username = null;
        if (it.hasNext()) {
            username = (String) it.next();
        } else {
            username = principals.toString();
        }
        //获取用户响应的permission
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (username != null) {
            // 添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
            info.addRole("admin");
            // 添加权限
            info.addStringPermission("admin:manage");
            System.out.println("已为用户赋予了[admin]角色和[admin:manage]权限");
            return info;
        }
        return info;
    }
}
