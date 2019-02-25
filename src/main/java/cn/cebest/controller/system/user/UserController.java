package cn.cebest.controller.system.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.UserRole;
import cn.cebest.entity.system.Role;
import cn.cebest.entity.system.User;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.menu.MenuManager;
import cn.cebest.service.system.role.RoleManager;
import cn.cebest.service.system.user.UserManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.FileDownload;
import cn.cebest.util.FileUpload;
import cn.cebest.util.GetPinyin;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelRead;
import cn.cebest.util.ObjectExcelView;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;

/** 
 * 类名称：UserController
 * 创建人：FH 中企高呈
 * 更新时间：2015年11月3日
 * @version
 */
@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController {
	
	String menuUrl = "user/listUsers.do"; //菜单地址(权限用)
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="menuService")
	private MenuManager menuService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	
	/**显示用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:list")
	@RequestMapping(value="/listUsers")
	public ModelAndView listUsers(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
		String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
		} 
		page.setPd(pd);
		List<User>	userList = userService.listUsers(page);	//列出用户列表
		List<Role> roleList = roleService.getAllRoles();//列出所有系统用户角色
		mv.setViewName("system/user/user_list_new");
		mv.addObject("userList", userList);
		mv.addObject("roleList", roleList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**删除用户
	 * @param out
	 * @throws Exception 
	 */
	@RequiresPermissions("user:delete")
	@RequestMapping(value="/deleteU")
	public String deleteU() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除user");
		PageData pd = new PageData();
		pd = this.getPageData();
		userService.deleteU(pd);
		//删除用户对应的映射关系
		userService.deleteURole(pd.getString("USER_ID"));
		FHLOG.save(Jurisdiction.getUsername(), "删除系统用户："+pd);
		return "redirect:"+Const.ADMIN_PREFIX+"user/listUsers";
	}
	
	/**去新增用户页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:add")
	@RequestMapping(value="/goAddU")
	public ModelAndView goAddU()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = userService.findById(pd);//列出所有系统用户角色
		mv.setViewName("system/user/user_add_new");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveU")
	@ResponseBody
	public JsonResult saveU(){
		logBefore(logger, Jurisdiction.getUsername()+"新增user");
		PageData pd = new PageData();
		pd = this.getPageData();
		String userId=pd.getString("USER_ID");
		if(StringUtils.isEmpty(userId)){
			userId=this.get32UUID();
			String salt=pd.getString("USERNAME")+new SecureRandomNumberGenerator().nextBytes().toHex();
			pd.put("SALT", salt);	
			pd.put("PASSWORD", new SimpleHash("MD5",  pd.getString("PASSWORD"),salt,Const.PASSWORD_ENCRY_TIMES).toString());	//密码加密
			pd.put("USER_ID", userId);//ID 主键
			pd.put("LAST_LOGIN", "");			//最后登录时间
			pd.put("IP", "");					//IP
			pd.put("STATUS", Const.VALID);				//状态
			pd.put("SKIN", "");
			pd.put("RIGHTS", "");
			pd.put("NUMBER", "");
			try {
				userService.saveU(pd);
				FHLOG.save(Jurisdiction.getUsername(), "新增系统用户："+pd.getString("USERNAME"));
			} catch (Exception e) {
				logger.error("save the user info failed!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
		}else{
			try {
				userService.editU(pd);
			} catch (Exception e) {
				logger.error("update the user info failed!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
		}
		//保存用户-角色的映射关系
		List<UserRole> roleList=new ArrayList<UserRole>();
		String roleIds=pd.getString("ROLE_ID");
		String[] roleIdArry=StringUtils.split(roleIds, Const.SPLIT_CHAR);
		for(String roleId:roleIdArry){
			UserRole userRole=new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			roleList.add(userRole);
		}
		try {
			roleService.deleteUserRole(userId);
			roleService.batchsaveUserRole(roleList);
		} catch (Exception e) {
			logger.error("save the user-role relation failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**判断用户名是否存在
	 * @return
	 */
	@RequestMapping(value="/hasU")
	@ResponseBody
	public JsonResult hasU(){
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUsername(pd) != null){
				return new JsonResult(Const.HTTP_OK, "NO",Const.NO);
			}
		} catch(Exception e){
			logger.error("check the userName failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",Const.YES);
	}
	
	/**判断邮箱是否存在
	 * @return
	 */
	@RequestMapping(value="/hasE")
	@ResponseBody
	public JsonResult hasE(){
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUE(pd) != null){
				return new JsonResult(Const.HTTP_OK, "NO",Const.NO);
			}
		} catch(Exception e){
			logger.error("check the phone failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",Const.YES);
	}
	
	/**判断手机号是否存在
	 * @return
	 */
	@RequestMapping(value="/hasP")
	@ResponseBody
	public JsonResult hasP(){
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByPhone(pd) != null){
				return new JsonResult(Const.HTTP_OK, "NO",Const.NO);
			}
		} catch(Exception e){
			logger.error("check the phone failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",Const.YES);
	}

	
	/**判断编码是否存在
	 * @return
	 */
	@RequestMapping(value="/hasN")
	@ResponseBody
	public Object hasN(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(userService.findByUN(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**去修改用户页面(个人修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditMyU")
	public ModelAndView goEditMyU() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("fx", "head");
		pd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd);	//列出所有系统用户角色
		pd.put("USERNAME", Jurisdiction.getUsername());
		pd = userService.findByUsername(pd);						//根据用户名读取
		mv.setViewName("system/user/user_edit");
		mv.addObject("msg", "editU");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);
		return mv;
	}
	
	/**查看用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/view")
	public ModelAndView view() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if("admin".equals(pd.getString("USERNAME"))){return null;}	//不能查看admin用户
		pd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd);	//列出所有系统用户角色
		pd = userService.findByUsername(pd);						//根据ID读取
		mv.setViewName("system/user/user_view");
		mv.addObject("msg", "editU");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);
		return mv;
	}
	
	/**去修改用户页面(在线管理页面打开)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditUfromOnline")
	public ModelAndView goEditUfromOnline() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if("admin".equals(pd.getString("USERNAME"))){return null;}	//不能查看admin用户
		pd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd);	//列出所有系统用户角色
		pd = userService.findByUsername(pd);						//根据ID读取
		mv.setViewName("system/user/user_edit");
		mv.addObject("msg", "editU");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);
		return mv;
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value="/editU")
	public ModelAndView editU() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ser");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(!Jurisdiction.getUsername().equals(pd.getString("USERNAME"))){		//如果当前登录用户修改用户资料提交的用户名非本人
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}  //校验权限 判断当前操作者有无用户管理查看权限
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限判断当前操作者有无用户管理修改权限
			if("admin".equals(pd.getString("USERNAME")) && !"admin".equals(Jurisdiction.getUsername())){return null;}	//非admin用户不能修改admin
		}else{	//如果当前登录用户修改用户资料提交的用户名是本人，则不能修改本人的角色ID
			pd.put("ROLE_ID", userService.findByUsername(pd).getString("ROLE_ID")); //对角色ID还原本人角色ID
		}
		if(pd.getString("PASSWORD") != null && !"".equals(pd.getString("PASSWORD"))){
			pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
		}
		userService.editU(pd);	//执行修改
		FHLOG.save(Jurisdiction.getUsername(), "修改系统用户："+pd.getString("USERNAME"));
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 批量删除
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteAllU")
	@ResponseBody
	public Object deleteAllU() throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除user");
		FHLOG.save(Jurisdiction.getUsername(), "批量删除user");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String USER_IDS = pd.getString("USER_IDS");
		if(null != USER_IDS && !"".equals(USER_IDS)){
			String ArrayUSER_IDS[] = USER_IDS.split(",");
			userService.deleteAllU(ArrayUSER_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**导出用户信息到EXCEL
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		FHLOG.save(Jurisdiction.getUsername(), "导出用户信息到EXCEL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Jurisdiction.buttonJurisdiction(menuUrl, "cha")){
				String keywords = pd.getString("keywords");				//关键词检索条件
				if(null != keywords && !"".equals(keywords)){
					pd.put("keywords", keywords.trim());
				}
				String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
				String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
				if(lastLoginStart != null && !"".equals(lastLoginStart)){
					pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
				}
				if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
					pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
				} 
				Map<String,Object> dataMap = new HashMap<String,Object>();
				List<String> titles = new ArrayList<String>();
				titles.add("用户名"); 		//1
				titles.add("编号");  		//2
				titles.add("姓名");			//3
				titles.add("职位");			//4
				titles.add("手机");			//5
				titles.add("邮箱");			//6
				titles.add("最近登录");		//7
				titles.add("上次登录IP");	//8
				dataMap.put("titles", titles);
				List<PageData> userList = userService.listAllUser(pd);
				List<PageData> varList = new ArrayList<PageData>();
				for(int i=0;i<userList.size();i++){
					PageData vpd = new PageData();
					vpd.put("var1", userList.get(i).getString("USERNAME"));		//1
					vpd.put("var2", userList.get(i).getString("NUMBER"));		//2
					vpd.put("var3", userList.get(i).getString("NAME"));			//3
					vpd.put("var4", userList.get(i).getString("ROLE_NAME"));	//4
					vpd.put("var5", userList.get(i).getString("PHONE"));		//5
					vpd.put("var6", userList.get(i).getString("EMAIL"));		//6
					vpd.put("var7", userList.get(i).getString("LAST_LOGIN"));	//7
					vpd.put("var8", userList.get(i).getString("IP"));			//8
					varList.add(vpd);
				}
				dataMap.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
				mv = new ModelAndView(erv,dataMap);
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/uploadexcel");
		return mv;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + "Users.xls", "Users.xls");
	}
	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/readExcel")
	public ModelAndView readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		FHLOG.save(Jurisdiction.getUsername(), "从EXCEL导入到数据库");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE);								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "userexcel");							//执行上传
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);		//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			/*存入数据库操作======================================*/
			pd.put("RIGHTS", "");					//权限
			pd.put("LAST_LOGIN", "");				//最后登录时间
			pd.put("IP", "");						//IP
			pd.put("STATUS", "0");					//状态
			pd.put("SKIN", "default");				//默认皮肤
			pd.put("ROLE_ID", "1");
			List<Role> roleList = roleService.listAllRolesByPId(pd);//列出所有系统用户角色
			pd.put("ROLE_ID", roleList.get(0).getROLE_ID());		//设置角色ID为随便第一个
			/**
			 * var0 :编号
			 * var1 :姓名
			 * var2 :手机
			 * var3 :邮箱
			 * var4 :备注
			 */
			for(int i=0;i<listPd.size();i++){		
				pd.put("USER_ID", this.get32UUID());										//ID
				pd.put("NAME", listPd.get(i).getString("var1"));							//姓名
				
				String USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1"));	//根据姓名汉字生成全拼
				pd.put("USERNAME", USERNAME);	
				if(userService.findByUsername(pd) != null){									//判断用户名是否重复
					USERNAME = GetPinyin.getPingYin(listPd.get(i).getString("var1"))+Tools.getRandomNum();
					pd.put("USERNAME", USERNAME);
				}
				pd.put("BZ", listPd.get(i).getString("var4"));								//备注
				if(Tools.checkEmail(listPd.get(i).getString("var3"))){						//邮箱格式不对就跳过
					pd.put("EMAIL", listPd.get(i).getString("var3"));						
					if(userService.findByUE(pd) != null){									//邮箱已存在就跳过
						continue;
					}
				}else{
					continue;
				}
				pd.put("NUMBER", listPd.get(i).getString("var0"));							//编号已存在就跳过
				pd.put("PHONE", listPd.get(i).getString("var2"));							//手机号
				
				pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString());	//默认密码123
				if(userService.findByUN(pd) != null){
					continue;
				}
				userService.saveU(pd);
			}
			/*存入数据库操作======================================*/
			mv.addObject("msg","success");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**显示用户列表(弹窗选择用)
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listUsersForWindow")
	public ModelAndView listUsersForWindow(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastLoginStart");	//开始时间
		String lastLoginEnd = pd.getString("lastLoginEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastLoginStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastLoginEnd", lastLoginEnd+" 00:00:00");
		} 
		page.setPd(pd);
		List<PageData>	userList = userService.listUsersBystaff(page);	//列出用户列表(弹窗选择用)
		pd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd);//列出所有系统用户角色
		mv.setViewName("system/user/window_user_list");
		mv.addObject("userList", userList);
		mv.addObject("roleList", roleList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 启用、禁用
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:changeStatus")
	@RequestMapping(value="/changeStatus")
	public ModelAndView changeStatus()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd=this.getPageData();
			String status=pd.getString("STATUS");
			if(Const.VALID.equals(status)){
				pd.put("STATUS", Const.INVALID);
			}else{
				pd.put("STATUS", Const.VALID);
			}
			userService.changeStatus(pd);
			mv.setViewName("redirect:"+Const.ADMIN_PREFIX+"user/listUsers.do");
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.setViewName("error");
		}
		return mv;
	}

	
	/**
	 * 更新角色
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:changeRole")
	@RequestMapping(value="/changeRole")
	@ResponseBody
	public JsonResult changeRole(@RequestParam(value="roleIds") String roleIds,
			@RequestParam(value="userId") String userId)throws Exception{
		//保存用户-角色的映射关系
		List<UserRole> roleList=new ArrayList<UserRole>();
		String[] roleIdArry=StringUtils.split(roleIds, Const.SPLIT_CHAR);
		for(String roleId:roleIdArry){
			UserRole userRole=new UserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			roleList.add(userRole);
		}
		try {
			roleService.deleteUserRole(userId);
			roleService.batchsaveUserRole(roleList);
		} catch (Exception e) {
			logger.error("save the user-role relation failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",Const.YES);
	}

	/**
	 *校验旧密码
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/checkoldpswd")
	@ResponseBody
	public JsonResult checkoldpswd(@RequestParam(value="oldPwd") String oldPwd,
			@RequestParam(value="userId") String userId)throws Exception{
		try{
			PageData user=this.userService.findById(new PageData("USER_ID",userId));
			String salt=user.getString("SALT");
			String password=new SimpleHash("MD5", oldPwd,salt,Const.PASSWORD_ENCRY_TIMES).toString();
			String passwordDataBase=user.getString("PASSWORD");
			if(!password.equals(passwordDataBase)){
				return new JsonResult(Const.HTTP_OK, "OK",Const.NO);
			}
		} catch(Exception e){
			logger.error("update the user password failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",Const.YES);
	}

	/**去修改密码页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUpdatePwd")
	public ModelAndView goUpdatePwd(@RequestParam(value="USER_ID") String USER_ID) {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/user_updatePwd");
		mv.addObject("USER_ID", USER_ID);
		return mv;
	}

	/**
	 * 修改密码
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/updatePwd")
	@ResponseBody
	public JsonResult updatePwd()throws Exception{
		try{
			PageData pd=this.getPageData();
			PageData user=this.userService.findById(new PageData("USER_ID",pd.getString("USER_ID")));
			String salt=user.getString("SALT");
			pd.put("SALT", salt);	
			pd.put("PASSWORD", new SimpleHash("MD5", pd.getString("PASSWORD"),salt,Const.PASSWORD_ENCRY_TIMES).toString());	//密码加密
			userService.updatePwd(pd);
		} catch(Exception e){
			logger.error("update the user password failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}

}
