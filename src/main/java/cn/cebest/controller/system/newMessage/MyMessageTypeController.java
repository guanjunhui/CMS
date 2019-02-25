package cn.cebest.controller.system.newMessage;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.translate.demo.TransApi;
import com.google.gson.Gson;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;

/**
 * 资讯类型管理
 * @author lwt
 *
 */
@Controller
@RequestMapping(value = "/mymessageType")
public class MyMessageTypeController extends BaseController {
	String menuUrl = "mymessageType/list.do"; // 菜单地址(权限用)

	@Resource(name = "MyMessageTypeService")
	private MyMessageTypeService messageService;

	@Resource(name = "fhlogService")
	private FHlogManager FHLOG;


	/**
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/messageTypelistPage")
	@RequiresPermissions("mymessageType:messageTypelistPage")
	public String index(Map<String, Object> map, Page page) throws Exception {
		// 1 用page传参
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		messageService.findMessageTypeToList(map, page);
		return "system/message/messageType_list";// 转发到资讯类型列表
	}
	/**
	 * 验证类型名是否重复
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/judgeTypeName")
	@RequiresPermissions("mymessageType:judgeTypeName")
	@ResponseBody
	public Map<String, Object> testTypeName(String typeName) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "验证资讯类型名字是否重复");
		String decode = URLDecoder.decode(typeName, "UTF-8");
		Map<String, Object> map = new HashMap<>();
		NewMessageType messageType = messageService.selectNewMessageType(decode);
		if(messageType!=null){
			map.put("success", true);
		}else{
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 去往添加资讯类型页面
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "toAdd")
	@RequiresPermissions("mymessageType:toAdd")
	public String toAdd() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加messageType页面");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} // 校验权限
		return "system/message/messageType_add";// 转发到添加资讯类型页
	}*/
	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("mymessageType:toAdd")
	public ModelAndView toAdd(@ModelAttribute(value="columId") String columId,
		@ModelAttribute(value="topColumId") String topColumId) {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加messageType页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/message/messageType_add");
		return mv;
	}

//==============================================================================
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTree")
	@RequiresPermissions("mymessageType:getTree")
	@ResponseBody
	public JsonResult getTree(String columId) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "添加的页面发送ajax返回层级结构的json数据");
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("columId", columId);
		List<NewMessageType> list = messageService.getTreeData(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}
	
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMessageTypeTree")
	@RequiresPermissions("mymessageType:getMessageTypeTree")
	@ResponseBody
	public JsonResult getMessageTypeTree(String[] columnid) throws Exception {
		PageData pd=this.getPageData();
		pd.put("columnids", columnid);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<NewMessageType> list = messageService.selectMessageTypeByColumIds(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}
	
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getlistTree")
	@RequiresPermissions("mymessageType:getlistTree")
	@ResponseBody
	public JsonResult getlistTree() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "添加的页面发送ajax返回层级结构的json数据");
		PageData pd = this.getPageData();
		String type_keywords = pd.getString("type_keywords");//模板名称检索
		if(StringUtils.isNotEmpty(type_keywords)){
			String decode = URLDecoder.decode(type_keywords, "UTF-8");
			pd.put("type_keywords",decode);
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<NewMessageType> list = messageService.getlistTreeData(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}
	@RequestMapping(value = "/updateSort")
	@RequiresPermissions("mymessageType:updateSort")
	@ResponseBody
	public Map<String, Object> updateSort(String id,Integer sort) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "更新资讯类型sort");
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		try{
			pd.put("id", id);
			pd.put("sort", sort);
			messageService.updateTypeSort(pd);
			map.put("success", true);
		}catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 保存
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "/save")
	@RequiresPermissions("mymessageType:save")
	@ResponseBody
	public ModelAndView save(NewMessageType messageType, MultipartFile file, String[] columnids,int is_add) {
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<>();
		ModelAndView mav=new ModelAndView();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类");
		messageType.setId(this.get32UUID());
		if (messageType.getPid() == null || "".equals(messageType.getPid())) {
			messageType.setPid("0");
		}
		messageType.setCreated_time(DateUtil.getTime());
		messageType.setColumnids(columnids);
		Boolean flag = false;
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			messageType.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类图片");
				pd.put("messageImageId", this.get32UUID()); // 主键
				messageType.setImageid(pd.getString("messageImageId"));
				pd.put("messageTitle", "图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			pd.put("flag", flag);
			// 保存
			messageService.save(messageType, pd);
			//map.put("success", true);
			mav.addObject("result", new JsonResult(200,"添加资讯成功!"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类异常");
			//map.put("success", false);
			mav.addObject("result", new JsonResult(500,"添加资讯失败!"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		mav.setViewName("redirect:list.do");
		return mav;
	}*/
	
	/**
	 * 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save")
	@RequiresPermissions("mymessageType:save")
	@ResponseBody
	public ModelAndView save(NewMessageType messageType, MultipartFile file, String[] columnids,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		ModelAndView mav=new ModelAndView();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类");
		messageType.setId(this.get32UUID());
		if (messageType.getPid() == null || "".equals(messageType.getPid())) {
			messageType.setPid("0");
		}
		messageType.setCreated_time(DateUtil.getTime());
		messageType.setColumnids(columnids);
		Boolean flag = false;
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			messageType.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类图片");
				pd.put("messageImageId", this.get32UUID()); // 主键
				messageType.setImageid(pd.getString("messageImageId"));
				pd.put("messageTitle", "图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			pd.put("flag", flag);
			messageType.setPd(pd);
			//messageType.setTypeUrlName(Translate(messageType.getType_name()));
			// 保存
			messageService.save(messageType);
			//map.put("success", true);
			mav.addObject("result", new JsonResult(200,"添加资讯成功!"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增资讯分类异常");
			//map.put("success", false);
			mav.addObject("result", new JsonResult(500,"添加资讯失败!"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do?ID="+columId+"&topColumId="+topColumId);
			return mav;
		}
		mav.setViewName("redirect:/manage/columcontent_colum/managecontentType.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	/**
	 * 模块的入口,展示页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	@RequiresPermissions("mymessageType:list")
	public String index(Map<String, Object> map) throws Exception {
		//获取get请求并设置编码格式
		HttpServletRequest request = this.getRequest();
		request.setCharacterEncoding("UTF-8");
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		messageService.findMessageTypeToList(map, pd);

		return "system/message/messageType_list";
	}

	/**
	 * json格式展示页面
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/listPage")
	@RequiresPermissions("mymessageType:listPage")
	@ResponseBody
	public Map<String, Object> listPage() {
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			messageService.findMessageTypeToList(map, pd);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 去往编辑的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("mymessageType:toEdit")
	public String toEdit(Map<String, Object> map, String id ,String columId ,String topColumId) {
		try {
			map.putAll(messageService.findMessageTypeToEdit(id));
			PageData pd=this.getPageData();
			pd.put("MASTER_ID", id);
			map.put("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/message/messageType_edit";
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "/edit")
	@RequiresPermissions("mymessageType:edit")
	@ResponseBody
	public Map<String, Object> edit(NewMessageType messageType, MultipartFile file, String[] columnids) {
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "修改message");
		Boolean flag = false;
		messageType.setUpdate_time(DateUtil.getTime());
		messageType.setColumnids(columnids);
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			messageType.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传
				flag = true;

				logBefore(logger, Jurisdiction.getUsername() + "修改图片");
				pd.put("messageTitle", "图片"); // 标题
				pd.put("bz", "图片管理处上传"); // 备注
				pd.put("messageImageId",messageType.getImageid());
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
			}
			pd.put("flag", flag);

			messageService.update(messageType, pd); // 执行修改

			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
			}
			e.printStackTrace();
		}

		return map;
	}*/
	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	@RequiresPermissions("mymessageType:edit")
	@ResponseBody
	public ModelAndView edit(NewMessageType messageType, MultipartFile file, String[] columnids,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();
		Map<String, Object> map = new HashMap<>();
		ModelAndView mev=new ModelAndView();
		logBefore(logger, Jurisdiction.getUsername() + "修改message");
		Boolean flag = false;
		messageType.setUpdate_time(DateUtil.getTime());
		messageType.setColumnids(columnids);
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			messageType.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传
				flag = true;
				logBefore(logger, Jurisdiction.getUsername() + "修改图片");
				pd.put("messageImageId",this.get32UUID());
				pd.put("messageTitle", file.getOriginalFilename()); // 标题
				messageType.setImageid(pd.getString("messageImageId"));
				pd.put("bz", "图片管理处上传"); // 备注
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
			}
			pd.put("flag", flag);
			messageType.setPd(pd);
			//messageType.setTypeUrlName(Translate(messageType.getType_name()));
			messageService.update(messageType); // 执行修改
			//map.put("success", true);
			mev.addObject("result",new JsonResult(200,"修改成功!"));
		} catch (Exception e) {
			//map.put("success", false);
			mev.addObject("result",new JsonResult(500,"修改失败!"));
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
			}
			e.printStackTrace();
		}
		if(is_add==1){
			mev.setViewName("redirect:toAdd.do?ID="+columId+"&topColumId="+topColumId);
			return mev;
		}
		mev.setViewName("redirect:/manage/columcontent_colum/managecontentType.do?ID="+columId+"&topColumId="+topColumId);
		return mev;
	}

/**
 * 根据id删除资讯类型
 * @param id
 * @return
 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions("mymessageType:delete")
	@ResponseBody
	public Map<String, Object> delete(String[] id) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = this.getPageData();
		try {
			messageService.delete(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
/**
 * 修改资讯状态
 * @param ids
 * @return
 */
	@RequestMapping(value = "/updateStatus")
	@RequiresPermissions("mymessageType:updateStatus")
	@ResponseBody
	public Map<String, Object> updataStatus(String[] ids,String STATUS) {
		/*if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		}*/ // 校验权限
		Map<String, Object> map = new HashMap<>();
		map.put("ids", ids);
		map.put("STATUS", STATUS);
		try {
			messageService.updateMessageStatus(map);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 通过id更新资讯类型状态信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateTypeStatus")
	@RequiresPermissions("mymessageType:updateTypeStatus")
	@ResponseBody
	public Map<String, Object> updateStatus(String[] ids,String STATUS){
		logBefore(logger, Jurisdiction.getUsername() + "修改资讯类型状态");
		Map<String, Object> map = new HashMap<>();
		map.put("ids", ids);
		map.put("STATUS", STATUS);
		try {
			messageService.updateMessageTypeStatus(map);
			map.put("success", true);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改资讯类型状态失败!");
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	//翻译方法(zh:中文---en:英文)
	/*
	 * 调用的是百度翻译接口、还可以翻译更多语言**/
	public String Translate(String originalText){
		TransApi api = new TransApi(Const.APP_ID, Const.SECURITY_KEY);
        String transResult = api.getTransResult(originalText, "zh", "en");
        //将string字符串转为map集合
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(transResult, map.getClass());
        //将原文和结果的集合取出来
        ArrayList<Map> map2 = (ArrayList<Map>) map.get("trans_result");
        //将结果打印出来
        return (String) map2.get(0).get("dst");
	}
}
