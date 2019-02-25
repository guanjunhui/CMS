package cn.cebest.controller.system.download;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.controller.system.product.ProductController.MyCompartor;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.download.DownloadFiles;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.download.KeyValuesUtil;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.ReadExcelUtil;
import cn.cebest.util.RequestUtils;

import cn.cebest.util.Tools;

import cn.cebest.util.SystemConfig;


/**
 * 文件下载controller
 * 
 * @author WZD
 *
 */
@RequestMapping("fileresource")
@Controller
public class FileResourceController extends BaseController {
	String menuUrl = "fileresource/list.do"; // 菜单地址(权限用)
	@Resource(name = "fileResourceService")
	private FileResourceService service;

	@Resource(name = "contentExtendFiledServiceImpl")
	private ContentExtendFiledService contentExtendFiledService;
	
	@RequestMapping("list")
	@RequiresPermissions("fileresource:list")
	public String index(Page page,Map<String, Object> map)  {
		try {
			PageData pd = this.getPageData();
			pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
			page.setPd(pd);
			if(pd.getString("TOP_TIME")==null && pd.getString("UPDATE_TIME")==null&&pd.getString("CREATED_TIME")==null&&pd.getString("RECOMMEND_TIME")==null){
				pd.put("UPDATE_TIME", "UPDATE_TIME");
			}
			map.put("list", service.getList(page));
			map.put("type", service.getType(page));
			map.put("download_id", pd.getString("download_id"));
			map.put("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/download/fileresource_list";
	}
	/**
	 * 展示排序
	 * 
	 * @param map
	 * @param page
	 * @return
	 */
	@RequestMapping("sortlistPage")
	@RequiresPermissions("fileresource:sortlistPage")
	public ModelAndView sortlistPage(Page page) {
		ModelAndView view = new ModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		String orderKey=pd.getString("orderKey");
		if(StringUtils.isEmpty(orderKey)){
			pd.put("UPDATE_TIME", "UPDATE_TIME");
		}else{
			pd.put(orderKey, orderKey);
		}
 		String nodeType=pd.getString("nodeType");
		if(Const.NODE_TYPE_1.equals(nodeType)){//栏目
			pd.put("columId", pd.getString("nodeId"));
		}else if(Const.NODE_TYPE_2.equals(nodeType)){//分类
			pd.put("columId", pd.getString("nodeColumId"));
			pd.put("typeId", pd.getString("nodeId"));
		}
		try {
			List<FileResources> fileList= service.findMessageTypeList(page);
			view.addObject("fileList", fileList);
			view.addObject("typeId", pd.getString("typeId"));
		} catch (Exception e) {
			logger.error("find the FileResourcesList occured error!", e);
		}
		view.addObject("pd", pd);
		view.setViewName("system/download/fileresource_list");
		return view;// 转发到
	}
	@RequestMapping("getList")
	@ResponseBody
	@RequiresPermissions("fileresource:getList")
	public Map<String, Object> getList(Page page) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		if(pd.getString("TOP_TIME")==null && pd.getString("UPDATE_TIME")==null&&pd.getString("CREATED_TIME")==null&&pd.getString("RECOMMEND_TIME")==null){
			pd.put("UPDATE_TIME", "UPDATE_TIME");
		}
		Map<String, Object> map = new HashMap<>();
		try {
		String keywords=pd.getString("KEYWORDS");
		if(keywords!=null && !"".equals(keywords)){
			keywords.trim();
			pd.put("KEYWORDS", keywords.trim());
		}
		page.setPd(pd);
			map.put("list", service.getList(page));
			map.put("page", page);
			map.put("pd", pd);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;

	}

	@RequestMapping("toAdd")
	@RequiresPermissions("fileresource:toAdd")
	public String toAdd(Map<String, Object> map) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("COLUM_ID", pd.get("columId"));
		pd.put("TYPE", Const.COLUM_TYPE_5);
		map.put("pd", pd);
		try {
			map.put("tree",  contentExtendFiledService.getData(pd));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return "system/download/fileresource_add";
	}

	@RequestMapping("toUpdate")
	@RequiresPermissions("fileresource:toUpdate")
	public String toUpdate(Map<String, Object> map, String id) throws Exception {
		map.putAll(service.findById(map, id));
		PageData pd = this.getPageData();
		map.put("pd", pd);
		//map.put("relevantList", service.findReleventFileByid(id));
		return "system/download/fileresource_edit";
	}

//	@RequestMapping(value = "/save")
//	@ResponseBody
//	@RequiresPermissions("fileresource:save")
//	public Map<String, Object> save(FileResources download, @RequestParam MultipartFile[] sultipartFiles,
//			MultipartFile image) {
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Map<String, Object> map = new HashMap<>();
//		download.setDownload_count(0L);
//		Boolean flag = false;
//		try {
//			// 添加
//			logBefore(logger, Jurisdiction.getUsername() + "新增文件");
//			download.setCreated_time(DateUtil.getTime());// 创建时间
//			download.setUpdate_time(DateUtil.getTime());// 修改时间
//			download.setFileid(this.get32UUID());// id
//			List<DownloadFiles> files = new ArrayList<>();
//			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
//			download.setSiteid(siteId);
//			download.setFiles(files);
//			for (MultipartFile file : sultipartFiles) {
//				if (null != file && !file.isEmpty()) {
//					DownloadFiles downloads = new DownloadFiles();
//					String ffile = DateUtil.getDays(), fileName = "";
//					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
//					String name = file.getOriginalFilename();// 文件原来的的名字
//					String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//
//					/* 不修改名字 */
//					// File f=FileUpload.mkdirsmy( filePath, fileName); //创建路径
//					// FileUtils.copyInputStreamToFile(file.getInputStream(),
//					// f);//保存文件
//
//					fileName = FileUpload.fileUp(file, filePath, this.get32UUID());// 保存文件,
//					downloads.setName(name);
//					downloads.setType(extName);
//					downloads.setFileid(download.getFileid());
//					downloads.setFilepach(ffile + "/" + fileName);// 路径
//					files.add(downloads);
//				}
//			}
//			if (null != image && !image.isEmpty()) {
//				String ffile = DateUtil.getDays(), fileName = "";
//				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
//				fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行上传
//				logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
//				pd.put("productImageId", this.get32UUID()); // 主键
//				download.setImageid(pd.getString("productImageId"));
//				pd.put("productTitle", image.getOriginalFilename()); // 标题
//				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
//				pd.put("bz", "图片管理处上传"); // 备注
//				flag = true;
//			}
//			pd.put("flag", flag);
//			// 保存
//			service.save(download, pd);
//
//			map.put("success", true);
//		} catch (Exception e) {
//			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类异常");
//			map.put("success", false);
//			e.printStackTrace();
//		}
//		return map;
//	}

	@RequestMapping(value = "/save")
	@RequiresPermissions("fileresource:save")
	public ModelAndView save(FileResources download,
			@RequestParam MultipartFile[] sultipartFiles,
			@RequestParam MultipartFile[] images, 
			MultipartFile image,int is_add,@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		PageData pd = new PageData();
		pd = this.getPageData();
		ModelAndView mav=this.getModelAndView();
		download.setDownload_count(0L);
		Boolean flag = false;
		try {
			// 添加
			logBefore(logger, Jurisdiction.getUsername() + "新增文件");
			download.setCreated_time(DateUtil.getTime());// 创建时间
			download.setUpdate_time(DateUtil.getTime());// 修改时间
			download.setFileid(this.get32UUID());// id
			List<DownloadFiles> files = new ArrayList<>();
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			download.setSiteid(siteId);
			download.setFiles(files);
			
			
			Boolean is_ColumnOrYype = false;
			List<KeyValuesUtil> key_values = new ArrayList<>();
			String[] typeids = download.getTypeids();
			List<String> columnids = download.getColumnids();
			if(columnids!=null && columnids.size()>0&&typeids==null){
				for (String string : columnids) {
						KeyValuesUtil key_value = new KeyValuesUtil();
						key_value.setKey(string);
						key_value.setValues(null);
						key_values.add(key_value);
					
				}
				is_ColumnOrYype=true;
				download.setObjKey_Value(key_values);
			}else if(columnids!=null && columnids.size()>0&&typeids!=null &&typeids.length >0){
				for (String string : typeids) {
					String[] str = string.split("-");
					for (int i = 1; i < str.length; i++) {
						KeyValuesUtil key_value = new KeyValuesUtil();
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
					KeyValuesUtil key_value = new KeyValuesUtil();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
				}
				is_ColumnOrYype=true;
				download.setObjKey_Value(key_values);
			}
			pd.put("is_ColumnOrYype", is_ColumnOrYype);
			List<DownloadFiles> downloadMark = download.getDownloadMark();
			int index = 0;
			for (MultipartFile file : sultipartFiles) {
				if (null != file && !file.isEmpty()) {
					DownloadFiles downloads = new DownloadFiles();
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String name = file.getOriginalFilename();// 文件原来的的名字
					String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					String size = convertFileSize(file.getSize());
					/* 不修改名字 */
					// File f=FileUpload.mkdirsmy( filePath, fileName); //创建路径
					// FileUtils.copyInputStreamToFile(file.getInputStream(),
					// f);//保存文件

					fileName = FileUpload.fileUp(file, filePath, this.get32UUID());// 保存文件,
					downloads.setName(name);
					downloads.setType(extName);
					downloads.setFileid(download.getFileid());
					downloads.setFilepach(ffile + "/" + fileName);// 路径
					
					DownloadFiles mark = downloadMark.get(index);
					downloads.setMark(mark.getMark());
					downloads.setSize(size);
					
					files.add(downloads);
					index++;
				}
			}
			if (null != image && !image.isEmpty()) {
				String ffile = DateUtil.getDays(), fileName = "";
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行上传
				logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
				pd.put("productImageId", this.get32UUID()); // 主键
				download.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", image.getOriginalFilename()); // 标题
				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				flag = true;
			}
			List<Image> timageList = download.getTimageList();
			for (int i = 0; i < images.length; i++) {
				Image downloads = timageList.get(i);
				downloads.setImageId(this.get32UUID()); // 主键
				downloads.setMaster_id(download.getFileid());
				downloads.setForder(i);
				if(null != images[i] && !images[i].isEmpty()){
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile; // 文件上传路径
					fileName = FileUpload.fileUp(images[i], filePath, this.get32UUID());// 保存文件,
					downloads.setImgurl(ffile + "/" + fileName); // 路径
				}
				
			}
			
			
			pd.put("flag", flag);
			List<ExtendFiledUtil> fileds=download.getFileds();
			if(fileds!=null&&fileds.size()>0){
				MyCompartor mc = new MyCompartor();//创建比较器对象
			    Collections.sort(fileds,mc);     //按照age升序 22，23，
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				download.setFiledJson(json);
			}
			// 保存
			service.save(download, pd);

			mav.addObject("result",new JsonResult(200,"添加成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(200,"添加失败"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do?columId="+columId+"&topColumId="+topColumId);
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	
	
	public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
	
	/**
	 * 刪除方法
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("deleteFile")
	@ResponseBody
	@RequiresPermissions("fileresource:deleteFile")
	public Map<String, Object> deleteFile(String[] id) {
		Map<String, Object> map = new HashMap<>();
		try {
			service.deleteFile(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 查询相同类型的文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findByTypeIds")
	@ResponseBody
	@RequiresPermissions("fileresource:findByTypeIds")
	public Map<String, Object> findByTypeIds(String[] id,String fileid) {
		Map<String, Object> map = new HashMap<>();
		map.put("fileid", fileid);
		map.put("ids", id);
		try {
			map.put("list", service.findByTypeIds(map));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 查询相同类型的文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findTypeById")
	@ResponseBody
	@RequiresPermissions("fileresource:findTypeById")
	public Map<String, Object> findTypeById(String[] id) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("list", service.findTypeById(id));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
//	/**
//	 * 修改
//	 * @param download
//	 * @param sultipartFiles
//	 * @param image
//	 * @return
//	 */
//	@RequestMapping(value = "/update")
//	@ResponseBody
//	@RequiresPermissions("fileresource:update")
//	public Map<String, Object> update(FileResources download, @RequestParam MultipartFile[] sultipartFiles,
//			MultipartFile image) {
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		Map<String, Object> map = new HashMap<>();
//
//		Boolean flag = false;
//		try {
//			// 添加
//			logBefore(logger, Jurisdiction.getUsername() + "新增文件");
//			download.setUpdate_time(DateUtil.getTime());// 创建时间6
//
//			List<DownloadFiles> files = new ArrayList<>();
//			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
//			download.setSiteid(siteId);
//			download.setFiles(files);
//			for (MultipartFile file : sultipartFiles) {
//				if (null != file && !file.isEmpty()) {
//					DownloadFiles downloads = new DownloadFiles();
//					String ffile = DateUtil.getDays(), fileName = "";
//					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
//					String name = file.getOriginalFilename();// 文件原来的的名字
//					String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//
//					/* 不修改名字 */
//					// File f=FileUpload.mkdirsmy( filePath, fileName); //创建路径
//					// FileUtils.copyInputStreamToFile(file.getInputStream(),
//					// f);//保存文件
//
//					fileName = FileUpload.fileUp(file, filePath, this.get32UUID());// 保存文件,
//					downloads.setName(name);
//					downloads.setType(extName);
//					downloads.setFileid(download.getFileid());
//					downloads.setFilepach(ffile + "/" + fileName);// 路径
//					files.add(downloads);
//				}
//			}
//			if (null != image && !image.isEmpty()) {
//				String ffile = DateUtil.getDays(), fileName = "";
//				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
//				fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行上传
//				logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
//				pd.put("productImageId", this.get32UUID()); // 主键
//				download.setImageid(pd.getString("productImageId"));
//				pd.put("productTitle", image.getOriginalFilename()); // 标题
//				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
//				pd.put("bz", "图片管理处上传"); // 备注
//				flag = true;
//			}
//			pd.put("flag", flag);
//			// 保存
//			service.update(download, pd);
//
//			map.put("success", true);
//		} catch (Exception e) {
//			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类异常");
//			map.put("success", false);
//			e.printStackTrace();
//		}
//		return map;
//	}
	
	/**
	 * 修改
	 * @param download
	 * @param sultipartFiles
	 * @param image
	 * @return
	 */
	@RequestMapping(value = "/update")
	@RequiresPermissions("fileresource:update")
	public ModelAndView update(FileResources download, @RequestParam MultipartFile[] sultipartFiles,@RequestParam MultipartFile[] images,
			MultipartFile image,int is_add,@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		PageData pd = new PageData();
		pd = this.getPageData();
		ModelAndView mav=this.getModelAndView();

		Boolean flag = false;
		try {
			// 添加
			logBefore(logger, Jurisdiction.getUsername() + "新增文件");
			download.setUpdate_time(DateUtil.getTime());// 创建时间6

			List<DownloadFiles> files = new ArrayList<>();
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			download.setSiteid(siteId);
			download.setFiles(files);
			
			
			
			Boolean is_ColumnOrYype = false;
			List<KeyValuesUtil> key_values = new ArrayList<>();
			String[] typeids = download.getTypeids();
			List<String> columnids = download.getColumnids();
			if(columnids!=null && columnids.size()>0&&typeids==null){
				for (String string : columnids) {
						KeyValuesUtil key_value = new KeyValuesUtil();
						key_value.setKey(string);
						key_value.setValues(null);
						key_values.add(key_value);
					
				}
				is_ColumnOrYype=true;
				download.setObjKey_Value(key_values);
			}else if(columnids!=null && columnids.size()>0&&typeids!=null &&typeids.length >0){
				for (String string : typeids) {
					String[] str = string.split("-");
					for (int i = 1; i < str.length; i++) {
						KeyValuesUtil key_value = new KeyValuesUtil();
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
					KeyValuesUtil key_value = new KeyValuesUtil();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
				}
				is_ColumnOrYype=true;
				download.setObjKey_Value(key_values);
			}
			pd.put("is_ColumnOrYype", is_ColumnOrYype);
			List<DownloadFiles> anotherMark = download.getAnotherMark();//原来的标记集合
			int index = 0;
			for (MultipartFile file : sultipartFiles) {
				//有新文件添加
				if (null != file && !file.isEmpty()) {
					DownloadFiles downloads = new DownloadFiles();
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String name = file.getOriginalFilename();// 文件原来的的名字
					String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					String size = convertFileSize(file.getSize());
					/* 不修改名字 */
					// File f=FileUpload.mkdirsmy( filePath, fileName); //创建路径
					// FileUtils.copyInputStreamToFile(file.getInputStream(),
					// f);//保存文件

					fileName = FileUpload.fileUp(file, filePath, this.get32UUID());// 保存文件,
					downloads.setName(name);
					downloads.setType(extName);
					downloads.setFileid(download.getFileid());
					downloads.setFilepach(ffile + "/" + fileName);// 路径
					downloads.setMark(anotherMark.get(index).getMark());
					downloads.setSize(size);
					files.add(downloads);
				}
			}
			List<Image> pictureList = download.getPictureList();
			int size = download.getImageids().size();
			int m = 0 ;
			for (int j = 0; j < images.length; j++) {
				Image downloads = null;
				if (null != images[j] && !images[j].isEmpty()) {
					if (j > size - 1 && pictureList.size() > m) {
						downloads = pictureList.get(m);
						downloads.setImageId(this.get32UUID());
						String ffile = DateUtil.getDays(), fileName = "";
						String filePath = PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile; // 文件上传路径
						fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
						downloads.setMaster_id(download.getFileid());
						downloads.setImgurl(ffile + "/" + fileName); // 路径
						downloads.setForder(j);
						m++;
					} else {
						String ffile = DateUtil.getDays(), fileName = "";
						String filePath = PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile; // 文件上传路径
						fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
						download.getTimageList().get(j).setMaster_id(download.getFileid());
						String pach = download.getTimageList().get(j).getImgurl();
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHIMG + pach); // 删除图片
						}
						download.getTimageList().get(j).setImgurl(ffile + "/" + fileName);
						download.getTimageList().get(j).setForder(j);
						download.getTimageList().get(j).setFlag(true);
					}
				}
			}
			
			
			//封面图
			if (null != image && !image.isEmpty()) {
				String ffile = DateUtil.getDays(), fileName = "";
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行上传
				logBefore(logger, Jurisdiction.getUsername() + "新增下载分类图片");
				pd.put("productImageId", this.get32UUID()); // 主键
				download.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", image.getOriginalFilename()); // 标题
				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				flag = true;
			}
			
			
			
			pd.put("flag", flag);
			List<ExtendFiledUtil> fileds=download.getFileds();
			if(fileds!=null&&fileds.size()>0){
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				download.setFiledJson(json);
			}
			// 保存
			service.update(download, pd);

			mav.addObject("result",new JsonResult(500,"修改成功"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类异常");
			mav.addObject("result",new JsonResult(500,"修改失败"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	/**
	 * 推荐
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataRecommend")
	@ResponseBody
	@RequiresPermissions("fileresource:updataRecommend")
	public Map<String, Object> updataRecommend(String[] ids,String recommend) {
		Map<String, Object> map = new HashMap<>();

		try {
			FileResources file=new FileResources();
			file.setRecommend_time(DateUtil.getTime());
			file.setFileids(ids);
			file.setRecommend(recommend);
			file.setUpdate_time(DateUtil.getTime());// 修改时间
			service.updataRecommendAndTop(file);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 置顶
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataTop")
	@ResponseBody
	@RequiresPermissions("fileresource:updataTop")
	public Map<String, Object> updataTop(String[] ids,String top) {
		Map<String, Object> map = new HashMap<>();

		try {
			FileResources file=new FileResources();
			file.setTop_time(DateUtil.getTime());
			file.setFileids(ids);
			file.setTop(top);
			file.setUpdate_time(DateUtil.getTime());// 修改时间
			service.updataRecommendAndTop(file);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	 /*
		 * srot自定义排序
		 * **/
		class MyCompartor implements Comparator
		{
		     @Override
		     public int compare(Object o1, Object o2)
		    {		
		    	 ExtendFiledUtil e1 = (ExtendFiledUtil) o1;
		    	 ExtendFiledUtil e2 = (ExtendFiledUtil) o2;
		    	 if(e1.getSort() != null && e2.getSort() != null){
		    		 return e1.getSort().compareTo(e2.getSort());
		    	 }else{
		    		 return 0;
		    	 }
		    }
		}
	
	//修改下载量
	@RequestMapping("/updateDownloadcount")
	@ResponseBody
	public void updateDownloadcount(String id) throws Exception{
		if(!"id".equals("") &&  "id" != null){
			service.updateDownlosd_count(id);
		}else{
			return;
		}
	}
	
	
}
