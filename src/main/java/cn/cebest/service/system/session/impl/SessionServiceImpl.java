package cn.cebest.service.system.session.impl;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import cn.cebest.service.system.session.SessionService;
import cn.cebest.util.Const;
import cn.cebest.util.Jurisdiction;

@Service
public class SessionServiceImpl implements SessionService{

	@Override
	public void SessionInvalid(){
		String USERNAME = Jurisdiction.getUsername();	//当前登录的用户名
		Session session = Jurisdiction.getSession();	//以下清除session缓存
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(USERNAME + Const.SESSION_allmenuList);
		session.removeAttribute(USERNAME + Const.SESSION_menuList);
		session.removeAttribute(USERNAME + Const.SESSION_QX);
		session.removeAttribute(Const.SESSION_userpds);
		session.removeAttribute(Const.SESSION_USERNAME);
		session.removeAttribute(Const.SESSION_USERROL);
		session.removeAttribute("changeMenu");
		session.removeAttribute("DEPARTMENT_IDS");
		session.removeAttribute("DEPARTMENT_ID");
		session.removeAttribute(USERNAME+Const.SESSION_PERSITELIST);
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
	}

}
