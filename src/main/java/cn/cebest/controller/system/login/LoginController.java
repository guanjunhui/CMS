package cn.cebest.controller.system.login;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Tree;
import cn.cebest.entity.bo.SiteMain;
import cn.cebest.entity.system.Menu;
import cn.cebest.entity.system.User;
import cn.cebest.entity.vo.ServerInfoVo;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.service.system.buttonrights.ButtonrightsManager;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.fhbutton.FhbuttonManager;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.menu.MenuManager;
import cn.cebest.service.system.role.RoleManager;
import cn.cebest.service.system.server.ServerService;
import cn.cebest.service.system.session.SessionService;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.JsonUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
/**
 * 总入口
 * @author fh QQ 3 1 3 5 9 6 7 9 0[青苔]
 * 修改日期：2015/11/2
 */
/**
 * @author Administrator
 *
 */
@Controller
public class LoginController extends BaseController {
	
	//@Value("${super.admin.name}")  
	public final String SUPER_ADMIN_NAME = "admin";
	
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="menuService")
	private MenuManager menuService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="buttonrightsService")
	private ButtonrightsManager buttonrightsService;
	@Resource(name="fhbuttonService")
	private FhbuttonManager fhbuttonService;
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	@Autowired
	private SiteService siteService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ColumconfigService columconfigService;
	
	private String sysname = SystemConfig.getPropertiesString("sysname.title");
	
	/**访问登录页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
//		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); 		//读取系统名称
		pd.put("SYSNAME", sysname); 		//读取系统名称
		mv.setViewName("system/index/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**访问未授权页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/noauth", method = RequestMethod.GET)
	public ModelAndView noauth()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("noauth");
		return mv;
	}
	/**请求登录，验证用户(已废弃)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login" , method = RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login(HttpServletRequest request)throws Exception{
		return new JsonResult(200,"登陆成功","success");
	}
	
	/**访问系统首页
	 * @param changeMenu：切换菜单参数
	 * @return
	 */
	@RequestMapping(value="/index")
	public ModelAndView login_index(@RequestParam(value="changeMenu",required=false) String changeMenu,
			HttpServletRequest request,@RequestParam(value="changeSite",required=false) String changeSite){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);						//读取session中的用户信息(单独用户信息)
			if (user != null) {
				String USERNAME = user.getUSERNAME();
				session.setAttribute(Const.SESSION_USERNAME, USERNAME);						//放入用户名到session
				
				String serverPort = request.getServerPort() + "";
				if(serverPort != null && serverPort != ""){
					
				}
				session.setAttribute("AccessAddress", request.getServerName()+":"+request.getServerPort());
				String address = (String) session.getAttribute("AccessAddress");
				
				List<Menu> allmenuList = new ArrayList<Menu>();
				allmenuList = this.getAttributeMenu(session, USERNAME);//菜单缓存
				List<Menu> showmenuList=this.changeMenuF(allmenuList, session, USERNAME, changeMenu);
				//this.getRemortIP(USERNAME);	//更新登录IP
				mv.setViewName("system/index/main");
				mv.addObject("user", user);
				//获取用户具有权限的站点
				WebSite currentSite=RequestUtils.getSite(request);
				if(currentSite==null){
					throw new RuntimeException("the site does not fund!");
				}
				List<SiteMain> permSiteList=this.getAttributeSite(session, user, currentSite.getId());
				if(CollectionUtils.isNotEmpty(permSiteList)){
					Iterator<SiteMain> it = permSiteList.iterator();
					while(it.hasNext()){
						SiteMain siteMain = it.next();
						if(siteMain.getId().equals(currentSite.getId())){
							it.remove();
							break;
						}
					}
				}
				//获取服务器的相关信息.
				mv.addObject("currentSite", currentSite);
				mv.addObject("permSiteList", permSiteList);
				mv.addObject("menuList", showmenuList);
			}else {
				mv.setViewName("system/index/login");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("system/index/login");
			logger.error(e.getMessage(), e);
		}
		pd.put("SYSNAME", sysname); //读取系统名称
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**访问内容管理
	 * @param changeMenu：切换菜单参数
	 * @return
	 */
	@RequestMapping(value="/contentmanage")
	public ModelAndView contentmanage(@RequestParam(value="changeMenu",required=false) String changeMenu,
			HttpServletRequest request){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);						//读取session中的用户信息(单独用户信息)
			if (user != null) {
				String USERNAME = user.getUSERNAME();
				session.setAttribute(Const.SESSION_USERNAME, USERNAME);						//放入用户名到session
				List<Menu> allmenuList = new ArrayList<Menu>();
				allmenuList = this.getAttributeMenu(session, USERNAME);//菜单缓存
				List<Menu> showmenuList=this.changeMenuF(allmenuList, session, USERNAME, changeMenu);
				//this.getRemortIP(USERNAME);	//更新登录IP
				mv.setViewName("system/index/main_content");
				mv.addObject("user", user);
				//获取用户具有权限的站点
				WebSite currentSite=RequestUtils.getSite(request);
				if(currentSite==null){
					throw new RuntimeException("the site does not fund!");
				}
				List<SiteMain> permSiteList=this.getAttributeSite(session, user, currentSite.getId());
				if(CollectionUtils.isNotEmpty(permSiteList)){
					Iterator<SiteMain> it = permSiteList.iterator();
					while(it.hasNext()){
						SiteMain siteMain = it.next();
						if(siteMain.getId().equals(currentSite.getId())){
							it.remove();
							break;
						}
					}
				}
				//获取服务器的相关信息.
				mv.addObject("currentSite", currentSite);
				mv.addObject("permSiteList", permSiteList);
				mv.addObject("menuList", showmenuList);
			}else {
				mv.setViewName("system/index/login");//session失效后跳转登录页面
			}
		} catch(Exception e){
			mv.setViewName("system/index/login");
			logger.error(e.getMessage(), e);
		}
		pd.put("SYSNAME", sysname); //读取系统名称
		mv.addObject("pd",pd);
		return mv;

	}
	
	/**
	 * 跳转到内容管理左侧导航栏
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="leftnavcontent")
	public ModelAndView leftnavcontent(HttpServletRequest request) throws Exception{
		ModelAndView mv = this.getModelAndView();
		List<Tree> groupAndTopColumTree = null;
		try{
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);//读取session中的用户信息(单独用户信息)
			if (user != null) {
				//获取用户具有权限的站点
				WebSite currentSite=RequestUtils.getSite(request);
				//获取内容管理数据
				if(SUPER_ADMIN_NAME.equals(user.getUSERNAME())){
					groupAndTopColumTree=this.columconfigService.findTopAndGroupTree(new PageData("siteid", currentSite.getId()));
				}else{
					PageData pd = new PageData();
					pd.put("siteid", currentSite.getId());
					pd.put("userId", user.getUSER_ID());
					groupAndTopColumTree = this.columconfigService.findPermGroupAndColumTree(pd);
				}
				if(CollectionUtils.isNotEmpty(groupAndTopColumTree))
					mv.addObject("navList", JsonUtil.toJson(groupAndTopColumTree));
				mv.setViewName("system/index/left_content_nav");
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return mv;
	}
	
	/**获取当前路径的父级
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getparentMenu")
	@ResponseBody
	public JsonResult getparentMenu(@RequestParam(value="path") String path){
		try {
			Session session = Jurisdiction.getSession();
			if(session!=null){
				User user = (User)session.getAttribute(Const.SESSION_USER);//读取session中的用户信息(单独用户信息)
				if(user!=null){
					List<Menu> allmenuList=(List<Menu>) session.getAttribute(user.getUSERNAME() + Const.SESSION_allmenuList);
					if(CollectionUtils.isNotEmpty(allmenuList)){
						Menu menuNow=new Menu();
						searchCurrentMenu(allmenuList,path,menuNow);
						if(StringUtils.isNotEmpty(menuNow.getMENU_ID())){
							searchParentMenu(menuNow,allmenuList);
							return new JsonResult(Const.HTTP_OK, "ok", menuNow);
						}
					}
				}
				
			}
		} catch (Exception e) {
			logger.error("find the dictionaries By BianMa happend error!", e);
		}
		return new JsonResult(Const.HTTP_ERROR, "error");
	}

	
	/**菜单缓存
	 * @param session
	 * @param USERNAME
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAttributeMenu(Session session, String USERNAME) throws Exception{
		List<Menu> allmenuList = new ArrayList<Menu>();
		if(null == session.getAttribute(USERNAME + Const.SESSION_allmenuList)){	
			allmenuList = menuService.listDisplayMenuQx("0");							//获取所有菜单
			session.setAttribute(USERNAME + Const.SESSION_allmenuList, allmenuList);//菜单权限放入session中
		}else{
			allmenuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_allmenuList);
		}
		return allmenuList;
	}

	/**站点缓存
	 * @param session
	 * @param USERNAME
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SiteMain> getAttributeSite(Session session, User user,String currentSiteId) throws Exception{
		List<SiteMain> permSiteList = new ArrayList<SiteMain>();
		String userName=user.getUSERNAME();
		if(Const.SUPER_ADMIN_NAME.equals(userName)){
			permSiteList=this.siteService.findAllSiteMainInfo();
		}else{
			String userId=user.getUSER_ID();
			if(null == session.getAttribute(userName + Const.SESSION_PERSITELIST)){	
				permSiteList=this.roleService.getNoCurentSitesByUserId(userId, currentSiteId);
				session.setAttribute(userName + Const.SESSION_PERSITELIST, permSiteList);
			}else{
				permSiteList = (List<SiteMain>)session.getAttribute(userName + Const.SESSION_PERSITELIST);
			}
		}
		return permSiteList;
	}

	/**切换菜单处理
	 * @param allmenuList
	 * @param session
	 * @param USERNAME
	 * @param changeMenu
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> changeMenuF(List<Menu> allmenuList, Session session, String USERNAME, String changeMenu){
		List<Menu> menuList = new ArrayList<Menu>();
		if(null == session.getAttribute(USERNAME + Const.SESSION_menuList) 
				|| (Const.STRING_YES.equals(changeMenu))){
			List<Menu> menuList1 = new ArrayList<Menu>();//系统菜单
			List<Menu> menuList2 = new ArrayList<Menu>();//业务菜单
			for(int i=0;i<allmenuList.size();i++){//拆分菜单
				Menu menu = allmenuList.get(i);
				if(Const.MENU_TYPE_SYSTEM.equals(menu.getMENU_TYPE())){
					menuList1.add(menu);
				}else if(Const.MENU_TYPE_BUSINESS.equals(menu.getMENU_TYPE())){
					menuList2.add(menu);
				}
			}
			session.removeAttribute(USERNAME + Const.SESSION_menuList);
			if(Const.MENU_TYPE_BUSINESS.equals(session.getAttribute(Const.SESSION_CHANGEMENU_KEY))){
				session.setAttribute(USERNAME + Const.SESSION_menuList, menuList1);
				session.removeAttribute(Const.SESSION_CHANGEMENU_KEY);
				session.setAttribute(Const.SESSION_CHANGEMENU_KEY, Const.MENU_TYPE_SYSTEM);
				menuList = menuList1;
			}else{
				session.setAttribute(USERNAME + Const.SESSION_menuList, menuList2);
				session.removeAttribute(Const.SESSION_CHANGEMENU_KEY);
				session.setAttribute(Const.SESSION_CHANGEMENU_KEY, Const.MENU_TYPE_BUSINESS);
				menuList = menuList2;
			}
		}else{
			menuList = (List<Menu>)session.getAttribute(USERNAME + Const.SESSION_menuList);
		}
		return menuList;
	}
	
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping(value="/tab")
	public String tab(){
		return "system/index/tab";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/login_default")
	public ModelAndView defaultPage() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("userCount", Integer.parseInt(userService.getUserCount("").get("userCount").toString())-1);				//系统用户数
		pd.put("appUserCount", Integer.parseInt(appuserService.getAppUserCount("").get("appUserCount").toString()));	//会员数
		pd.put("moban",Tools.readTxtFile(Const.FTLPATH)); //读取模版配置
		mv.addObject("pd",pd);
		mv.setViewName("system/index/default");
		return mv;
	}
	
	/**
	 * 跳转到左侧导航栏
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/{menu}/{menuID}")
	public ModelAndView leftnav(@PathVariable("menu") String menuName,@PathVariable("menuID") String menuID) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Session session = Jurisdiction.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);						//读取session中的用户信息(单独用户信息)
			if (user != null) {
				String USERNAME = user.getUSERNAME();
				List<Menu> allmenuList = new ArrayList<Menu>();
				allmenuList = this.getAttributeMenu(session, USERNAME);	//菜单缓存
				Menu menuOut = null;
				for(Menu menu:allmenuList){
					if(menu.getMENU_ID().equals(menuID)){
						menuOut=menu;
						break;
					}
				}
				List<Menu> showmenuList=this.changeMenuF(allmenuList, session, USERNAME, StringUtils.EMPTY);
				mv.setViewName("system/index/"+menuName);
				mv.addObject("user", user);
				mv.addObject("navList", JsonUtil.toJson(menuOut.getSubMenu()));
				mv.addObject("menuList", showmenuList);
				mv.addObject("menuID", menuID);
				//获取用户具有权限的站点
				WebSite currentSite=RequestUtils.getSite(this.getRequest());
				if(currentSite!=null){
					List<SiteMain> permSiteList=this.getAttributeSite(session, user, currentSite.getId());
					if(CollectionUtils.isNotEmpty(permSiteList)){
						Iterator<SiteMain> it = permSiteList.iterator();
						while(it.hasNext()){
							SiteMain siteMain = it.next();
							if(siteMain.getId().equals(currentSite.getId())){
								it.remove();
								break;
							}
						}
					}
					mv.addObject("currentSite", currentSite);
					mv.addObject("permSiteList", permSiteList);
				}
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		pd.put("SYSNAME", sysname); //读取系统名称
		mv.addObject("pd",pd);
		return mv;
	}

	/**
	 * 用户注销
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout() throws Exception{
		String USERNAME = Jurisdiction.getUsername();	//当前登录的用户名
		logBefore(logger, USERNAME+"退出系统");
		FHLOG.save(USERNAME, "退出");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		sessionService.SessionInvalid();//session 失效
		pd = this.getPageData();
		pd.put("msg", pd.getString("msg"));
		pd.put("SYSNAME", sysname); 		//读取系统名称
		mv.setViewName("system/index/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**设置登录页面的配置参数
	 * @param pd
	 * @return
	 
	public PageData setLoginPd(PageData pd){
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); 		//读取系统名称
		String strLOGINEDIT = Tools.readTxtFile(Const.LOGINEDIT);	//读取登录页面配置
		if(null != strLOGINEDIT && !"".equals(strLOGINEDIT)){
			String strLo[] = strLOGINEDIT.split(",fh,");
			if(strLo.length == 2){
				pd.put("isZhuce", strLo[0]);
				pd.put("isMusic", strLo[1]);
			}
		}
		try {
			List<PageData> listImg = loginimgService.listAll(pd);	//登录背景图片
			pd.put("listImg", listImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pd;
	}*/
	
	/**获取用户权限
	 * @param session
	 * @return
	 */
	public Map<String, String> getUQX(String USERNAME){
		PageData pd = new PageData();
		Map<String, String> map = new HashMap<String, String>();
		try {
			pd.put(Const.SESSION_USERNAME, USERNAME);
			pd.put("ROLE_ID", userService.findByUsername(pd).get("ROLE_ID").toString());//获取角色ID
			pd = roleService.findObjectById(pd);										//获取角色信息														
			map.put("adds", pd.getString("ADD_QX"));	//增
			map.put("dels", pd.getString("DEL_QX"));	//删
			map.put("edits", pd.getString("EDIT_QX"));	//改
			map.put("chas", pd.getString("CHA_QX"));	//查
			List<PageData> buttonQXnamelist = new ArrayList<PageData>();
			if("admin".equals(USERNAME)){
				buttonQXnamelist = fhbuttonService.listAll(pd);					//admin用户拥有所有按钮权限
			}else{
				buttonQXnamelist = buttonrightsService.listAllBrAndQxname(pd);	//此角色拥有的按钮权限标识列表
			}
			for(int i=0;i<buttonQXnamelist.size();i++){
				map.put(buttonQXnamelist.get(i).getString("QX_NAME"),"1");		//按钮权限
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}	
		return map;
	}
	
	/** 更新登录用户的IP
	 * @param USERNAME
	 * @throws Exception
	 */
	public void getRemortIP(String USERNAME) throws Exception {  
		PageData pd = new PageData();
		HttpServletRequest request = this.getRequest();
		String ip = "";
//		if (request.getHeader("x-forwarded-for") == null) {  
//			ip = request.getRemoteAddr();  
//	    }else{
//	    	ip = request.getHeader("x-forwarded-for");  
//	    }
		ip=RequestUtils.getRemoteAddr(request);
		pd.put("USERNAME", USERNAME);
		pd.put("IP", ip);
		userService.saveIP(pd);
	}  
	
	public void searchCurrentMenu(List<Menu> allmenuList,String path,Menu menuFind){
		for(Menu menu:allmenuList){
			boolean flag=false;
			if(path.contains(menu.getMENU_URL())
					|| (!menu.getMENU_URL().equals("#")&&menu.getMENU_URL().lastIndexOf("/")!=-1&&
					path.contains(menu.getMENU_URL().substring(0, menu.getMENU_URL().lastIndexOf("/")-1)))){
				flag=true;
				try {
					BeanUtils.copyProperties(menuFind, menu);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				break;
			}else if(!flag&&CollectionUtils.isNotEmpty(menu.getSubMenu())){
				 this.searchCurrentMenu(menu.getSubMenu(), path,menuFind);
			}
		}
	}

	private void searchParentMenu(Menu menuNow,List<Menu> allmenuList){
		for(Menu menu:allmenuList){
			if(menu.getMENU_ID().equals(menuNow.getPARENT_ID())){
				menuNow.setParentMenu(menu);
				searchParentMenu(menu, allmenuList);
			}else if(CollectionUtils.isNotEmpty(menu.getSubMenu())){
				this.searchParentMenu(menuNow, menu.getSubMenu());
			}
		}
	}
	
	/**获取服务器信息
	 * @return
	 */
	@RequestMapping(value="/freshserverinfo")
	@ResponseBody
	public JsonResult freshserverinfo(HttpServletRequest request,HttpServletResponse response){
		ServerInfoVo serverInfo=null;
		try {
			serverInfo = serverService.getServerInfo();
		    int localPort = request.getLocalPort();
			serverInfo.setPort(localPort);
		} catch (Exception e) {
			logger.error("get service info occured eroor!", e);
		}
		return new JsonResult(Const.HTTP_OK, "OK",serverInfo);
	}
	
}
