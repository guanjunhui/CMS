package cn.cebest.controller.system.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.service.system.content.contentType.ContentTypeService;
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
 * 内容分类contorller
 * 
 * @author mpl
 *
 */
@Controller
@RequestMapping(value = "/contentType")
public class ContentTypeController extends BaseController {
	
	@Autowired
	private ContentTypeService service;
	
	/**
	 * 去往添加的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/toAdd")
	@RequiresPermissions("contentType:toAdd")
	public String toAdd() throws Exception {
		return "system/content/contentType_add";
	}*/
	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("contentType:toAdd")
	public ModelAndView toAdd(@ModelAttribute(value="columId") String columId,
		@ModelAttribute(value="topColumId") String topColumId) {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/content/contentType_add");
		return mv;
	}
	
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTree")
	@ResponseBody
	@RequiresPermissions("contentType:getTree")
	public JsonResult getTree() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<ContentType> list = service.getTreeData(map);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}
	
	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUpdateTree")
	@ResponseBody
	@RequiresPermissions("contentType:getUpdateTree")
	public JsonResult getUpdateTree(String columId,String ID) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		map.put("columId", columId);
		map.put("ID", ID);
		List<ContentType> list = service.getUpdateTree(map);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}
	
	@RequestMapping(value = "/getTreeByColumId")
	@ResponseBody
	@RequiresPermissions("contentType:getTreeByColumId")
	public JsonResult getTreeByColumId(String[] columnid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("columnids", columnid);
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<ContentType> list = service.getTreeByColumId(map);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	@RequestMapping(value = "/getTypeByColumId")
	@ResponseBody
	@RequiresPermissions("contentType:getTypeByColumId")
	public JsonResult getTypeByColumId(String[] columnid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("columnids", columnid);
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<ContentType> list = service.getTypeByColumId(map);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	/**
	 * 添加的页面发送ajax返回层级结构的json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getlistTree")
	@ResponseBody
	@RequiresPermissions("contentType:getlistTree")
	public JsonResult getlistTree() throws Exception {
		Map<String, Object> map = new HashMap<>();
		String keywords = (String) map.get("TYPE_KEYWORDS");
		if (keywords != null && !"".equals(keywords)) {
			map.put("TYPE_KEYWORDS", keywords.trim());
		}
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<ContentType> list = service.getlistTreeData(map);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save")
	@RequiresPermissions("contentType:save")
	public ModelAndView save(ContentType content, MultipartFile file,int is_add,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		//PageData pd = new PageData();
		ModelAndView mav=this.getModelAndView();
		Map<String, Object> map = new HashMap<>();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增内容分类");
		content.setId(this.get32UUID());
		if (content.getpId() == null || "".equals(content.getpId())) {
			content.setpId("0");
		}
		if (columId != null && !"".equals(columId)) {
			content.setColumnId(columId);
		}
		content.setCreateTime(new Date());
		Boolean flag = false;
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			content.setSiteId(siteId);
			if (null != file && !file.isEmpty()) {
				String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
				map.put("imageId", this.get32UUID()); // 主键
				content.setImageId((String)map.get("imageId"));
				map.put("title", file.getOriginalFilename()); // 标题
				map.put("imgurl", ffile + "/" + fileName); // 路径
				map.put("bz", "图片管理处上传"); // 备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			map.put("flag", flag);
			content.setContentTypeMap(map);
			/*//翻译、将原文和结果的集合取出来
	        content.setTypeUrlName(Translate(content.getTypeName()));*/
			// 保存
			service.save(content);
			mav.addObject("result",new JsonResult(200,"添加成功"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增内容分类异常");
			mav.addObject("result",new JsonResult(200,"添加失败"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
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
	@RequestMapping(value = "/contentTypelistPage")
	@RequiresPermissions("contentType:contentTypelistPage")
	public String index() throws Exception {
		return "system/content/contentType_list";
	}

	/**
	 * json格式展示页面
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	@RequiresPermissions("contentType:listPage")
	public Map<String, Object> listPage() {
		Map<String, Object> map = new HashMap<>();
		//PageData pd = this.getPageData();
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			List<ContentType> contentTypeList=service.findContentTypeToList(map);
			map.put("contentTypeList", contentTypeList);
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
	@RequiresPermissions("contentType:toEdit")
	public String toEdit(Map<String, Object> map, String id ,String columId ,String topColumId) {
		try {
			map.putAll(service.findContentTypeToEdit(id));
			PageData pd=this.getPageData();
			pd.put("MASTER_ID", id);
			map.put("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/content/contentType_edit";
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	@RequiresPermissions("contentType:edit")
	public ModelAndView edit(ContentType content, MultipartFile file,int is_add,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		//PageData pd = this.getPageData();
		ModelAndView mav=this.getModelAndView();
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "修改");
		Boolean flag = false;
		content.setUpdateTime(new Date());
		if (content.getpId() == null || "".equals(content.getpId())) {
			content.setpId("0");
		}
		String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			content.setSiteId(siteId);
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
				
				map.put("imageId", this.get32UUID()); // 主键
				content.setImageId((String)map.get("imageId"));
				map.put("title", file.getOriginalFilename()); // 标题
				map.put("imgurl", ffile + "/" + fileName); // 路径
				map.put("bz", "图片管理处上传"); // 备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			map.put("flag", flag);
			content.setContentTypeMap(map);
			//翻译、将原文和结果的集合取出来
			content.setColumnId(columId);
	        /*content.setTypeUrlName(Translate(content.getTypeName()));*/
			service.update(content); // 执行修改
			mav.addObject("result",new JsonResult(200,"添加成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(500,"添加失败"));
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
			}
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		mav.setViewName("redirect:/manage/columcontent_colum/managecontentType.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	@RequiresPermissions("contentType:delete")
	public Map<String, Object> delete(String[] id) {
		Map<String, Object> map = new HashMap<>();
		try {
			service.delete(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/updataStatus")
	@ResponseBody
	@RequiresPermissions("contentType:updataStatus")
	public Map<String, Object> updataStatus(String[] ids, String STATUS) {
		Map<String, Object> map = new HashMap<>();
		map.put("ids", ids);
		map.put("STATUS", STATUS);
		try {
			service.updataStatus(map);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/updateSort")
	@ResponseBody
	@RequiresPermissions("contentType:updateSort")
	public Map<String, Object> updateSort(String id, Integer sort) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("sort", sort);
		try {
			service.updateSort(map);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/findcount")
	@ResponseBody
	@RequiresPermissions("contentType:findcount")
	public Map<String, Object> findcount(String type_name, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("type_name", type_name);
		map.put("id", id);
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			Integer i = service.findcount(map);
			if (i > 0) {
				map.put("success", false);
				map.put("count", i);
			} else {
				map.put("success", true);
			}
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/findContentycount")
	@ResponseBody
	@RequiresPermissions("contentType:findContentycount")
	public Map<String, Object> findContentycount(String name, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("id", id);
		try {
			Integer i = service.findContentycount(map);
			if (i > 0) {
				map.put("success", false);
				map.put("count", i);
			} else {
				map.put("success", true);
			}
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/findBrandcount")
	@ResponseBody
	@RequiresPermissions("contentType:findBrandcount")
	public Map<String, Object> findBrandcount(String name, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("id", id);
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			Integer i = service.findBrandcount(map);
			if (i > 0) {
				map.put("success", false);
				map.put("count", i);
			} else {
				map.put("success", true);
			}
		} catch (Exception e) {
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
