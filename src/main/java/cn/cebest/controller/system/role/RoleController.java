package cn.cebest.controller.system.role;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.RolePermissionBo;
import cn.cebest.entity.bo.RoleVo;
import cn.cebest.entity.system.Menu;
import cn.cebest.entity.system.Role;
import cn.cebest.entity.system.RolePermission;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.menu.MenuManager;
import cn.cebest.service.system.role.RoleManager;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.RightsHelper;
import cn.cebest.util.Tools;
import net.sf.json.JSONArray;
/** 
 * 类名称：RoleController 角色权限管理
 * 创建人：中企高呈
 * 修改时间：2015年11月6日
 * @version
 */
@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	String menuUrl = "role.do"; //菜单地址(权限用)
	@Resource(name="menuService")
	private MenuManager menuService;
	@Autowired
	private SiteService siteService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	
	/** 进入权限首页
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("role:list")
	@RequestMapping
	public ModelAndView list(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		try{
			List<Role> roleList=roleService.listAllRoles(page);
			mv.addObject("roleList", roleList);
			mv.setViewName("system/role/role_list_new");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增/编辑页面
	 * @param ROLE_ID
	 * @return
	 */
	@RequiresPermissions("role:toAdd")
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd=roleService.findObjectById(pd);
			mv.setViewName("system/role/role_add_new");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**保存新增角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult add(@RequestBody RoleVo roleVo){
		PageData pd = new PageData();
		String roleId=roleVo.getRoleId();
		pd.put("ROLE_NAME", roleVo.getRoleName());
		pd.put("DESCRIPTION", roleVo.getDescription());

		try{
			if(StringUtils.isBlank(roleId)){
				roleId=this.get32UUID();
				pd.put("ROLE_ID", roleId);
				pd.put("STATE", Const.VALID);
				roleService.add(pd);
			}else{
				pd.put("ROLE_ID", roleId);
				roleService.edit(pd);
			}
			FHLOG.save(Jurisdiction.getUsername(), "新增角色:"+pd.getString("ROLE_NAME"));
		} catch(Exception e){
			logger.error("save role occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}

		//批量插入角色-菜单映射
		List<RolePermissionBo> menuList=new ArrayList<RolePermissionBo>();
		Map<String,String[]> siteMenuMap=roleVo.getSiteMenuMap();
		/*for(Map.Entry<String,String[]> entry:siteMenuMap.entrySet()){
			String siteId=entry.getKey();
			String[] menuIdArry=entry.getValue();
			for(String menuId:menuIdArry){
				RolePermissionBo bo = new RolePermissionBo();
				bo.setRoleId(roleId);
				bo.setSiteId(siteId);
				bo.setMenuId(menuId);
				menuList.add(bo);
			}
		}*/
		for(Map.Entry<String,String[]> entry:siteMenuMap.entrySet()){
			String siteId=entry.getKey();
			String[] menuIdArry=entry.getValue();
			for(String menuIdAndType:menuIdArry){
				String[] menuIdAndTypeArry=StringUtils.split(menuIdAndType, Const.SPLIT_CHAR);
				RolePermissionBo bo = new RolePermissionBo();
				bo.setRoleId(roleId);
				bo.setSiteId(siteId);
				bo.setMenuId(menuIdAndTypeArry[0]);
				bo.setType(menuIdAndTypeArry[1]);
				menuList.add(bo);
			}
		}
		try {
			roleService.deleteRolePermission(roleId);
			roleService.batchSaveRolePermission(menuList);
		} catch (Exception e) {
			logger.error("update role-menu relations occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
	/**删除角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("role:delete")
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object deleteRole(@RequestParam String ROLE_ID)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除角色");
		PageData pd = new PageData();
		try{
			pd.put("ROLE_ID", ROLE_ID);
			List<PageData> userlist = userService.listAllUserByRoldId(pd);//此角色下的用户
			if(CollectionUtils.isNotEmpty(userlist)){
				return new JsonResult(Const.HTTP_OK, "",Const.NO);
			}
			roleService.deleteRoleById(ROLE_ID);//执行删除
			roleService.deleteRolePermission(ROLE_ID);
			FHLOG.save(Jurisdiction.getUsername(), "删除角色ID为:"+ROLE_ID);
		} catch(Exception e){
			logger.error("delete role occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "",Const.YES);
	}
	
	/**
	 * 显示菜单列表ztree(菜单授权菜单)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menuqx")
	public ModelAndView listAllMenu(Model model,String ROLE_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			Role role = roleService.getRoleById(ROLE_ID);			//根据角色ID获取角色对象
			String roleRights = role.getRIGHTS();					//取出本角色菜单权限
			List<Menu> menuList = menuService.listAllMenuQx("0");	//获取所有菜单
			menuList = this.readMenu(menuList, roleRights);			//根据角色权限处理菜单权限状态(递归处理)
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.setViewName("system/role/menuqx");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存角色菜单权限
	 * @param ROLE_ID 角色ID
	 * @param menuIds 菜单ID集合
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveMenuqx")
	public void saveMenuqx(@RequestParam String ROLE_ID,@RequestParam String menuIds,PrintWriter out)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改菜单权限");
		FHLOG.save(Jurisdiction.getUsername(), "修改角色菜单权限，角色ID为:"+ROLE_ID);
		PageData pd = new PageData();
		try{
			if(null != menuIds && !"".equals(menuIds.trim())){
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));//用菜单ID做权处理
				Role role = roleService.getRoleById(ROLE_ID);	//通过id获取角色对象
				role.setRIGHTS(rights.toString());
				roleService.updateRoleRights(role);				//更新当前角色菜单权限
				pd.put("rights",rights.toString());
			}else{
				Role role = new Role();
				role.setRIGHTS("");
				role.setROLE_ID(ROLE_ID);
				roleService.updateRoleRights(role);				//更新当前角色菜单权限(没有任何勾选)
				pd.put("rights","");
			}
				pd.put("ROLE_ID", ROLE_ID);
				if(!"1".equals(ROLE_ID)){						//当修改admin权限时,不修改其它角色权限
					roleService.setAllRights(pd);				//更新此角色所有子角色的菜单权限
				}
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}

	/**请求角色按钮授权页面(增删改查)
	 * @param ROLE_ID： 角色ID
	 * @param msg： 区分增删改查
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/b4Button")
	public ModelAndView b4Button(@RequestParam String ROLE_ID,@RequestParam String msg,Model model)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			List<Menu> menuList = menuService.listAllMenuQx("0"); //获取所有菜单
			Role role = roleService.getRoleById(ROLE_ID);		  //根据角色ID获取角色对象
			String roleRights = "";
			if("add_qx".equals(msg)){
				roleRights = role.getADD_QX();	//新增权限
			}else if("del_qx".equals(msg)){
				roleRights = role.getDEL_QX();	//删除权限
			}else if("edit_qx".equals(msg)){
				roleRights = role.getEDIT_QX();	//修改权限
			}else if("cha_qx".equals(msg)){
				roleRights = role.getCHA_QX();	//查看权限
			}
			menuList = this.readMenu(menuList, roleRights);		//根据角色权限处理菜单权限状态(递归处理)
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.addObject("msg", msg);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("system/role/b4Button");
		return mv;
	}
	
	/**根据角色权限处理权限状态(递归处理)
	 * @param menuList：传入的总菜单
	 * @param roleRights：加密的权限字符串
	 * @return
	 */
	public List<Menu> readMenu(List<Menu> menuList,String roleRights){
		for(int i=0;i<menuList.size();i++){
			menuList.get(i).setHasMenu(RightsHelper.testRights(roleRights, menuList.get(i).getMENU_ID()));
			this.readMenu(menuList.get(i).getSubMenu(), roleRights);					//是：继续排查其子菜单
		}
		return menuList;
	}
	
	/**
	 * 保存角色按钮权限
	 */
	/**
	 * @param ROLE_ID
	 * @param menuIds
	 * @param msg
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveB4Button")
	public void saveB4Button(@RequestParam String ROLE_ID,@RequestParam String menuIds,@RequestParam String msg,PrintWriter out)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改"+msg+"权限");
		FHLOG.save(Jurisdiction.getUsername(), "修改"+msg+"权限，角色ID为:"+ROLE_ID);
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(null != menuIds && !"".equals(menuIds.trim())){
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
				pd.put("value",rights.toString());
			}else{
				pd.put("value","");
			}
			pd.put("ROLE_ID", ROLE_ID);
			roleService.saveB4Button(msg,pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
	/**获取角色以及菜单、站点
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/getPermissionByRoleId")
	@ResponseBody
	public JsonResult getPermissionByRoleId(@RequestParam(value="roleId") String roleId){
		List<RolePermission> list=null;
		try{
			list=roleService.getPermissionByRoleId(roleId);
		} catch(Exception e){
			logger.error(e.toString(), e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
	/**
	 * 启用、禁用
	 * @param model
	 * @return
	 */
	@RequiresPermissions("role:changeStatus")
	@RequestMapping(value="/changeStatus")
	public ModelAndView changeStatus()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd=this.getPageData();
			String status=pd.getString("STATE");
			if(Const.VALID.equals(status)){
				pd.put("STATE", Const.INVALID);
			}else{
				pd.put("STATE", Const.VALID);
			}
			roleService.changeStatus(pd);
			mv.setViewName("redirect:"+Const.ADMIN_PREFIX+"/role.do");
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.setViewName("error");
		}
		return mv;
	}
	
	/**获取所有角色
	 * @return
	 */
	@RequestMapping(value="/getAllRole")
	@ResponseBody
	public JsonResult getAllRole(){
		List<Role> roleList=null;
		try{
			roleList=roleService.getAllRoles();
		} catch(Exception e){
			logger.error("get all role occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",roleList);
	}

	/**获取用户-角色对应关系
	 * @return
	 */
	@RequestMapping(value="/getRoleIdsByUserId")
	@ResponseBody
	public JsonResult getRoleIdsByUserId(@RequestParam(value="userId") String userId){
		List<String> list=null;
		try{
			list=roleService.getRoleIdsByUserId(userId);
		} catch(Exception e){
			logger.error("get user-r"
					+ "ole relations occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}

}