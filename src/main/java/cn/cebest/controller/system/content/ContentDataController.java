package cn.cebest.controller.system.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.KeyValuesBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;

/**
 * @author liqk 留学资讯,留学机构
 */
@Controller
@RequestMapping(value = "/contentData")
public class ContentDataController extends BaseController {

	String menuUrl = "contentData/contentlistPage.do"; //菜单地址(权限用)
	@Resource(name = "contentService")
	private ContentService contentService;
	@Resource(name = "templateService")
	private TemplateManager templateService;//模板
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;//栏目
	@Resource(name = "fhlogService")
	private FHlogManager FHLOG;
	@Resource(name = "seoService")
	private SeoService service;
	@Resource(name = "contentExtendFiledServiceImpl")
	private ContentExtendFiledService contentExtendFiledService;

	/**
	 * 保存内容wzd
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveContent")
	@RequiresPermissions("contentData:saveContent")
	public ModelAndView saveContent(Content content, @RequestParam MultipartFile[] sultipartFiles, @RequestParam MultipartFile[] images, MultipartFile image,MultipartFile image2, int is_add, @RequestParam("columId") String columId, @RequestParam("topColumId") String topColumId) {
		logBefore(logger, Jurisdiction.getUsername() + "新增内容content");
		PageData pd = new PageData();
		content.setId(this.get32UUID());
		content.setContentStatus("1");
		content.setSolrStatus("1");
		Date date = new Date();
		content.setCreatedTime(DateUtil.getTime());//
		//content.setReleaseTime(DateUtil.getTime());
		content.setUpdateTime(DateUtil.getTime());
		content.setPv(0L);
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		content.setSiteId(siteId);

		
		Boolean is_ColumnOrYype = false;
		List<KeyValuesBo> key_values = new ArrayList<>();
		String[] typeids = content.getContenttypeids();
		List<String> columnids = new ArrayList<String>();
		CollectionUtils.addAll(columnids, content.getColumconfigIds());
		if(columnids!=null && columnids.size()>0&&typeids==null){
			for (String string : columnids) {
					KeyValuesBo key_value = new KeyValuesBo();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
				
			}
			is_ColumnOrYype=true;
			content.setObjKey_Value(key_values);
		}else if(columnids!=null && columnids.size()>0&&typeids!=null &&typeids.length >0){
			for (String string : typeids) {
				String[] str = string.split("-");
				for (int i = 1; i < str.length; i++) {
					KeyValuesBo key_value = new KeyValuesBo();
					key_value.setKey(str[i]);
					key_value.setValues(str[0]);
					key_values.add(key_value);
					for (int j=0 ;j<columnids.size();j++){
						if(columnids.get(j).equals(str[i])){
							columnids.remove(j);
							j--;
						}
					}
				}
			}
			for (String string : columnids) {
				KeyValuesBo key_value = new KeyValuesBo();
				key_value.setKey(string);
				key_value.setValues(null);
				key_values.add(key_value);
			}
			is_ColumnOrYype=true;
			content.setObjKey_Value(key_values);
		}
		pd.put("is_ColumnOrYype", is_ColumnOrYype);
		
		ModelAndView mav = this.getModelAndView();
		List<Video> files = content.getVideoList();
		for (int i = 0; i < sultipartFiles.length; i++) {
			Video downloads = files.get(i);
			downloads.setId(this.get32UUID());
			downloads.setMaster_id(content.getId());
			if (null != sultipartFiles[i] && !sultipartFiles[i].isEmpty()) {
				String ffile = DateUtil.getDays(), fileName = "";
				String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
				String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
				fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
				downloads.setName(sultipartFiles[i].getOriginalFilename());
				downloads.setVideo_type(extName);//后缀名
				downloads.setVideo_url(ffile + "/" + fileName);// 路径
			}
		}
		List<Image> ig = content.getImageList();//图片
		for (int j = 0; j < images.length; j++) {
			Image downloads = ig.get(j);
			downloads.setImageId(this.get32UUID()); // 主键
			downloads.setMaster_id(content.getId());
			if (null != images[j] && !images[j].isEmpty()) {
				String ffile = DateUtil.getDays(), fileName = "";
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
				downloads.setImgurl(ffile + "/" + fileName); // 路径
				downloads.setName(images[j].getOriginalFilename());
			}
		}
		if (null != image && !image.isEmpty()) {
			Image im = new Image();
			im.setImageId(this.get32UUID()); // 主键
			content.setSurface_imageid(im.getImageId());
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行上传
			logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
			im.setTitle(image.getOriginalFilename()); // 标题
			im.setImgurl(ffile + "/" + fileName); // 路径
			im.setBz("图片管理处上传"); // 备注
			ig.add(im);
		}
		if (null != image2 && !image2.isEmpty()) {
			Image im = new Image();
			im.setImageId(this.get32UUID()); // 主键
			content.setSurface_imageid2(im.getImageId());
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(image2, filePath, this.get32UUID()); // 执行上传
			logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
			im.setTitle(image2.getOriginalFilename()); // 标题
			im.setImgurl(ffile + "/" + fileName); // 路径
			im.setBz("图片管理处上传"); // 备注
			ig.add(im);
		}
		// 保存
		try {
			List<ExtendFiledUtil> fileds = content.getFileds();
			if (fileds != null && fileds.size() > 0) {

				MyCompartor mc = new MyCompartor();//创建比较器对象
				Collections.sort(fileds, mc); //按照age升序 22，23，

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				content.setFiledJson(json);
			}
			content.setPd(pd);
			contentService.saveContent(content);
			mav.addObject("result", new JsonResult(200, "添加成功"));
		} catch (Exception e) {
			mav.addObject("result", new JsonResult(500, "添加失败"));
			e.printStackTrace();
		}
		if (is_add == 1) {
			//				mav.setViewName("redirect:toAdd.do");
			mav.setViewName("redirect:toAdd.do?columId=" + columId + "&topColumId=" + topColumId);
			return mav;
		}
		//			mav.setViewName("redirect:contentlistPage.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID=" + columId + "&topColumId=" + topColumId);
		return mav;
	}

	/**
	 * 修改内容wzd
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateContent")
	@RequiresPermissions("contentData:updateContent")
	public ModelAndView updateContent(Content content, @RequestParam MultipartFile[] sultipartFiles, @RequestParam MultipartFile[] images, MultipartFile image,MultipartFile image2, int is_add, @RequestParam("columId") String columId, @RequestParam("topColumId") String topColumId) {
		logBefore(logger, Jurisdiction.getUsername() + "新增内容content");
		content.setUpdateTime(DateUtil.getTime());
		content.setContentStatus("1");
		PageData pd = this.getPageData();
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		content.setSiteId(siteId);

		
		Boolean is_ColumnOrYype = false;
		List<KeyValuesBo> key_values = new ArrayList<>();
		String[] typeids = content.getContenttypeids();
		List<String> columnids = new ArrayList<String>();
		CollectionUtils.addAll(columnids, content.getColumconfigIds());
		if(columnids!=null && columnids.size()>0&&typeids==null){
			for (String string : columnids) {
					KeyValuesBo key_value = new KeyValuesBo();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
				
			}
			is_ColumnOrYype=true;
			content.setObjKey_Value(key_values);
		}else if(columnids!=null && columnids.size()>0&&typeids!=null &&typeids.length >0){
			for (String string : typeids) {
				String[] str = string.split("-");
				for (int i = 1; i < str.length; i++) {
					KeyValuesBo key_value = new KeyValuesBo();
					key_value.setKey(str[i]);
					key_value.setValues(str[0]);
					key_values.add(key_value);
					for (int j=0 ;j<columnids.size();j++){
						if(columnids.get(j).equals(str[i])){
							columnids.remove(j);
							j--;
						}
					}
				}
			}
			for (String string : columnids) {
				KeyValuesBo key_value = new KeyValuesBo();
				key_value.setKey(string);
				key_value.setValues(null);
				key_values.add(key_value);
			}
			is_ColumnOrYype=true;
			content.setObjKey_Value(key_values);
		}
		pd.put("is_ColumnOrYype", is_ColumnOrYype);
		
		ModelAndView mav = this.getModelAndView();
		List<Video> files = content.getTvideoList();
		int vsize = content.getTvideoList().size();
		int n = 0;
		for (int i = 0; i < sultipartFiles.length; i++) {
			Video downloads = null;
			if (null != sultipartFiles[i] && !sultipartFiles[i].isEmpty()) {
				if (i >= vsize - 1 && files.size() > n) {
					downloads = files.get(n);
					downloads.setId(this.get32UUID());
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
					fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
					downloads.setVideo_type(extName);//后缀名
					downloads.setMaster_id(content.getId());
					downloads.setVideo_url(ffile + "/" + fileName);// 路径
					downloads.setName(sultipartFiles[i].getOriginalFilename());
					n++;
				} else {
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
					fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
					content.getVideoList().get(i-n).setVideo_type(extName);
					String pach = content.getVideoList().get(i-n).getVideo_url();
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除图片
					}
					content.getVideoList().get(i-n).setVideo_url(ffile + "/" + fileName);
					content.getVideoList().get(i-n).setName(sultipartFiles[i].getOriginalFilename());
				}
			}
		}
		List<Image> ig = content.getTimageList();//图片
		int isize = content.getImageids().size();
		int m = 0;
		for (int j = 0; j < images.length; j++) {
			Image downloads = null;
			if (null != images[j] && !images[j].isEmpty()) {
				if (j > isize - 1 && ig.size() > m) {
					downloads = ig.get(m);
					downloads.setImageId(this.get32UUID());
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
					fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
					downloads.setMaster_id(content.getId());
					downloads.setImgurl(ffile + "/" + fileName); // 路径
					downloads.setName(images[j].getOriginalFilename());
					m++;
				} else {
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
					fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
					content.getImageList().get(j).setMaster_id(content.getId());
					//content.getImageList().get(j).setForder(j);
					String pach = content.getImageList().get(j).getImgurl();
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
					}
					content.getImageList().get(j).setImgurl(ffile + "/" + fileName);
					content.getImageList().get(j).setFlag(true);
					content.getImageList().get(j).setName(images[j].getOriginalFilename());
				}
			}
		}
		Boolean flag = false;
		if (null != image && !image.isEmpty()) {
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(image, filePath, this.get32UUID());// 保存文件,
			flag = true;
			logBefore(logger, Jurisdiction.getUsername() + "修改图片");
			pd.put("productTitle", image.getOriginalFilename()); // 标题
			pd.put("bz", "图片管理处上传"); // 备注
			pd.put("productImageId", this.get32UUID());
			pd.put("productImageUrl", ffile + "/" + fileName); // 路径
			content.setSurface_imageid(pd.getString("productImageId"));
		}
		pd.put("flag", flag);
		Boolean flag2 = false;
		if (null != image2 && !image2.isEmpty()) {
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(image2, filePath, this.get32UUID());// 保存文件,
			flag2 = true;
			logBefore(logger, Jurisdiction.getUsername() + "修改图片");
			pd.put("productTitle2", image2.getOriginalFilename()); // 标题
			pd.put("bz2", "图片管理处上传"); // 备注
			pd.put("productImageId2", this.get32UUID());
			pd.put("productImageUrl2", ffile + "/" + fileName); // 路径
			content.setSurface_imageid2(pd.getString("productImageId2"));
		}
		pd.put("flag2", flag2);
		// 保存
		try {
			List<ExtendFiledUtil> fileds = content.getFileds();
			if (fileds != null && fileds.size() > 0) {

				MyCompartor mc = new MyCompartor();//创建比较器对象
				Collections.sort(fileds, mc); //按照age升序 22，23，

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				content.setFiledJson(json);
			}
			content.setPd(pd);
			contentService.updateContent(content);
			mav.addObject("result", new JsonResult(200, "修改成功"));
		} catch (Exception e) {
			mav.addObject("result", new JsonResult(500, "修改失败"));
			e.printStackTrace();
		}
		if (is_add == 1) {
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		//			mav.setViewName("redirect:contentlistPage.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID=" + columId + "&topColumId=" + topColumId);
		return mav;
	}

	/**
	 * 查找所有内容
	 * 
	 */
	@RequestMapping(value = "/findAll")
	@ResponseBody
	@RequiresPermissions("contentData:findAll")
	public Map<String, Object> findAll() {
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			List<PageData> contentList = contentService.findAll(pd); //列出内容详情列表
			map.put("contentList", contentList);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUpdate")
	@RequiresPermissions("contentData:toUpdate")
	public String toUpdate(Map<String, Object> map, String id) throws Exception {
		map.put("content", contentService.findContentById(id));
		PageData pd = this.getPageData();
		pd.put("MASTER_ID", id);
		map.put("seo", service.querySeoForObject(pd));
		map.put("pd", pd);
		return "system/content/contentdata_edit";
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("contentData:toAdd")
	public ModelAndView toAdd(Map<String, Object> map, @ModelAttribute(value = "columId") String columId, @ModelAttribute(value = "topColumId") String topColumId) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("COLUM_ID", columId);
		pd.put("TYPE", Const.COLUM_TYPE_1);
		try {
			mv.addObject("tree",  contentExtendFiledService.getData(pd));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		mv.setViewName("system/content/contentdata_add");
		return mv;
	}

	/**
	 * 后台接口,内容列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/contentlistPage")
	@RequiresPermissions("contentData:contentlistPage")
	public ModelAndView contentlistPage(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
			String keywords = pd.getString("keywords");//内容详情标题检索条件
			if (keywords != null && !"".equals(keywords)) {
				keywords.trim();
				pd.put("keywords", keywords);
			}
			if (pd.getString("TOP_TIME") == null && pd.getString("UPDATE_TIME") == null && pd.getString("CREATE_TIME") == null && pd.getString("RECOMMEND_TIME") == null) {
				pd.put("UPDATE_TIME", "UPDATE_TIME");
			}
			page.setPd(pd);

			List<Content> contentList = contentService.contentlistPage(page); //列出内容详情列表
			mv.setViewName("system/content/contentCdata_list");
			mv.addObject("contentList", contentList);
			mv.addObject("type", contentService.findColum(pd));
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = "/contentByColumnIdlistPage")
	@RequiresPermissions("contentData:contentByColumnIdlistPage")
	public ModelAndView contentByColumnIdlistPage(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		if (pd.getString("TOP_TIME") == null && pd.getString("UPDATE_TIME") == null && pd.getString("CREATE_TIME") == null && pd.getString("RECOMMEND_TIME") == null) {
			pd.put("UPDATE_TIME", "UPDATE_TIME");
		}
		page.setPd(pd);
		try {
			List<Content> contentList = contentService.selectlistPageByColumIDD(page); //列出内容详情列表
			mv.setViewName("system/content/contentCdata_list");
			mv.addObject("contentList", contentList);
			mv.addObject("type", contentService.findColum(pd));
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 后台接口,内容列表
	 * 
	 * @return
	 */
	/*
	 * @RequestMapping(value="/contentlistPage")
	 * 
	 * @RequiresPermissions("contentData:contentlistPage") public ModelAndView
	 * contentlistPage(Page page,@ModelAttribute(value="colum_id") String
	 * colum_id){ ModelAndView mv = this.getModelAndView(); PageData pd = new
	 * PageData(); pd = this.getPageData(); pd.put("SITEID",
	 * RequestUtils.getSite(this.getRequest()).getId()); String keywords =
	 * pd.getString("keywords"); //内容详情标题检索条件 if(null != keywords &&
	 * !"".equals(keywords)){ pd.put("keywords", keywords.trim()); }
	 * page.setPd(pd); try { List<Content> contentList =
	 * contentService.contentlistPage(page); //列出内容详情列表
	 * mv.setViewName("system/content/contentCdata_list");
	 * mv.addObject("contentList", contentList); mv.addObject("pd", pd);
	 * mv.addObject("QX",Jurisdiction.getHC()); //按钮权限 } catch (Exception e) {
	 * e.printStackTrace(); } return mv; }
	 */
	/**
	 * 批量删除内容
	 * 
	 */
	@RequestMapping(value = "/delAllContent")
	@ResponseBody
	@RequiresPermissions("contentData:delAllContent")
	public Map<String, Object> delAllContent(String[] id) {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除content");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			contentService.deleteAll(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 后台接口,栏目列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public JsonResult columconfiglistPage() {
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ColumConfig> list = null;

			pd.put("siteid", RequestUtils.getSite(this.getRequest()).getId());
			list = contentService.findTopAndChildList(pd);//列出栏目列表

			map.put("list", list);
		} catch (Exception e) {
			logger.error("get the colum list occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(), null);
		}
		return new JsonResult(Const.HTTP_OK, "OK", map);
	}

	/**
	 * 后台接口,栏目列表
	 * 
	 * @return
	 */
	@RequiresPermissions("contentData:golist")
	@RequestMapping(value = "/golist")
	public ModelAndView golist() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/content/columconfig_list");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 推荐
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataRecommend")
	@ResponseBody
	@RequiresPermissions("contentData:updataRecommend")
	public Map<String, Object> updataRecommend(String[] ids, String recommend) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "推荐产品");
		try {
			Content content = new Content();
			Date now = new Date();
			content.setRecommend_time(DateUtil.getTime());
			content.setContentRelevantIdList(ids);
			content.setRecommend(recommend);
			content.setUpdateTime(DateUtil.getTime());
			contentService.updataRecommendAndTopAndHot(content);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "推荐产品产品出现异常");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 置顶
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataTop")
	@ResponseBody
	@RequiresPermissions("contentData:updataTop")
	public Map<String, Object> updataTop(String[] ids, String top) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "置顶产品");
		try {
			Content content = new Content();
			Date now = new Date();
			content.setTop_time(DateUtil.getTime());
			content.setContentRelevantIdList(ids);
			content.setTop(top);
			content.setUpdateTime(DateUtil.getTime());
			contentService.updataRecommendAndTopAndHot(content);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "置顶产品出现异常");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 更新排序
	 * 
	 * @param id
	 * @param sort
	 * @return
	 */
	@RequestMapping("updateSort")
	@ResponseBody
	public JsonResult updateSort() {
		PageData pd = this.getPageData();
		try {
			contentService.updateSort(pd);
		} catch (Exception e) {
			logger.error("update the product sort occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/* end */

	/*
	 * srot自定义排序
	 **/
	class MyCompartor implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			ExtendFiledUtil e1 = (ExtendFiledUtil) o1;
			ExtendFiledUtil e2 = (ExtendFiledUtil) o2;
			if (e1.getSort() != null && e2.getSort() != null) {
				return e1.getSort().compareTo(e2.getSort());
			} else {
				return 0;
			}
		}
	}

}
