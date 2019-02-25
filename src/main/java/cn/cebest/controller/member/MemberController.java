package cn.cebest.controller.member;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.system.AppUser;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;


/**@author qichangxin
  * 会员中心
 */
@Controller
@RequestMapping(value="/member")
public class MemberController extends BaseController {
    
	@Autowired
	private AppuserManager appuserService;

	private Logger logger = LoggerFactory.getLogger(MemberController.class);

	/**
	 * 会员中心跳转
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpServletResponse response) {
		return "front/member";
	}

	/**
	 * 会员中心跳转
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String tologin(HttpServletRequest request,HttpServletResponse response) {
		return "web/index";
	}
	
	/**
	 * 会员中心跳转
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request,HttpServletResponse response) {
		PageData pd = new PageData();
		pd = this.getPageData();
		String USERNAME = pd.getString("username");
		String PASSWORD = pd.getString("password");
		if(StringUtils.isEmpty(USERNAME) || StringUtils.isEmpty(PASSWORD)){
			printJson(new JsonResult(-1,"请填写用户名和密码","error"));
			return;
		}
		AppUser appUser=null;
		try {
			appUser = this.appuserService.findPoByUsername(new PageData("USERNAME",USERNAME));
		} catch (Exception e) {
			 logger.error("error hapeend in findAPPUserByName", e);
		}
		if(appUser==null){
			printJson(new JsonResult(-1,"用户名或密码有误","usererror"));
		}
		//密码加密
		String salt=appUser.getSalt();
		SimpleHash pwd=new SimpleHash("MD5",  PASSWORD,salt,2);
		if(!pwd.toHex().equals(appUser.getPassword())){
			printJson(new JsonResult(-1,"密码有误","usererror"));
		}
		pd.put("LAST_LOGIN", DateUtil.getTime().toString());
		pd.put("USER_ID",appUser.getUserId());
		try {//更新最后登陆时间
			this.appuserService.updateLastLogin(pd);
		} catch (Exception e) {
			 logger.error("error hapeend in update appUser lastLogin", e);
		}
		RequestUtils.setMemberUser(this.getRequest(),appUser);
		printJson(new JsonResult(-1,"登陆成功","success"));
	}
	
	/**
	 * 会员用户注销
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/logout")
	public String logout() throws Exception{
		RequestUtils.RemoveMemberUser(this.getRequest());
		return "web/index";
	}

	

}
	
 