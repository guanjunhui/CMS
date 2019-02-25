package cn.cebest.controller.system.download;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.service.system.download.FileTypeService;
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
import cn.cebest.util.Watermark;

/**
 * 下载类型controller
 * @author wzd
 *
 */
@RequestMapping("downloadType")
@Controller
public class FileTypeController extends BaseController{
	
	@Resource(name="fileTypeService")
	private FileTypeService service;
	String menuUrl = "downloadType/list.do"; // 菜单地址(权限用)
	
	
	@RequestMapping("list")
	@RequiresPermissions("downloadType:list")
	public String index(){
		return "system/download/fileType_list";
	}
	@RequestMapping("toAdd")
	@RequiresPermissions("downloadType:toAdd")
	public ModelAndView toAdd(@ModelAttribute(value="columId") String columId,
			@ModelAttribute(value="topColumId") String topColumId){
		ModelAndView mv = this.getModelAndView();
		//return "system/download/fileType_add";
		mv.setViewName("system/download/fileType_add");
		return mv;
	}
	@RequestMapping("toUpdate")
	@RequiresPermissions("downloadType:toUpdate")
	public String toUpdate(Map<String,Object> map,String id,String columId ,String topColumId) throws Exception{
		try {
			map.putAll(service.findById(map,id));
			PageData pd=this.getPageData();
			pd.put("MASTER_ID", id);
			map.put("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/download/fileType_edit";
	}
	/**
	 * 展示的页面发送ajax返回层级结构的文件类型数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getListTree")
	@ResponseBody
	@RequiresPermissions("downloadType:getListTree")
	public JsonResult getListTree() throws Exception{
		PageData pd=this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<FileType> list=service.getListTree(pd);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	/**
	 * 添加的页面发送ajax返回层级结构的文件类型数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getTree")
	@ResponseBody
	@RequiresPermissions("downloadType:getTree")
	public JsonResult getTree(String columId) throws Exception{
		PageData pd=this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("columId", columId);
		List<FileType> list=service.getTreeData(pd);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
	@RequestMapping(value="/getTreeByColumId")
	@ResponseBody
	@RequiresPermissions("downloadType:getTreeByColumId")
	public JsonResult getTreeByColumId(String[] columnid) throws Exception{
		PageData pd=this.getPageData();
		pd.put("columnids", columnid);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<FileType> list=service.getTreeByColumId(pd);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	@RequestMapping(value="/getTypeByColumId")
	@ResponseBody
	@RequiresPermissions("downloadType:getTypeByColumId")
	public JsonResult getTypeByColumId(String[] columnid) throws Exception{
		PageData pd=this.getPageData();
		pd.put("columnids", columnid);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<FileType> list=service.getTypeByColumId(pd);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
	/**
	 * 添加的页面发送ajax返回层级结构的文件类型数据
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/search")
	@ResponseBody
	@RequiresPermissions("downloadType:search")
	public JsonResult search() throws Exception{
		PageData pd=this.getPageData();
		String keywords=pd.getString("KEYWORDS");
		if(keywords!=null && !"".equals(keywords)){
			pd.put("KEYWORDS", keywords.trim());
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<FileType> list=service.search(pd);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	/**
	 * 保存
	 * @return
	 */
//	@RequestMapping(value="/save")
//	@ResponseBody
//	@RequiresPermissions("downloadType:save")
//	public Map<String,Object> save(FileType download,MultipartFile file){
//		String ffile = DateUtil.getDays(), fileName = "";
//		PageData pd = new PageData();
//		Map<String,Object> map=new HashMap<>();
//		
//		// 添加
//		logBefore(logger, Jurisdiction.getUsername() + "新增产品分类");
//		download.setDownload_id(this.get32UUID());
//		if(download.getPid()==null || "".equals(download.getPid())){
//			download.setPid("0");
//		};
//		download.setCreated_time(DateUtil.getTime());
//		Boolean flag=false;
//		try {
//			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
//			download.setSiteid(siteId);
//			if (null != file && !file.isEmpty()) {
//				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
//				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传
//
//				
//				logBefore(logger, Jurisdiction.getUsername()+"新增产品分类图片");
//				pd.put("productImageId", this.get32UUID());			//主键
//				download.setImageid(pd.getString("productImageId"));
//				pd.put("productTitle", file.getOriginalFilename());					//标题
//				pd.put("productImageUrl", ffile + "/" + fileName);	//路径
//				pd.put("bz", "图片管理处上传");							//备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
//				flag=true;
//			}
//			pd.put("flag", flag);
//			//保存
//			service.save(download,pd);
//			map.put("success", true);
//		}catch (Exception e) {
//			logBefore(logger, Jurisdiction.getUsername()+"新增产品分类异常");
//			map.put("success", false);
//			e.printStackTrace();
//		}
//		return map;
//	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value="/save")
	@RequiresPermissions("downloadType:save")
	public ModelAndView save(FileType download,MultipartFile file,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId){
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		ModelAndView mav=this.getModelAndView();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增产品分类");
		download.setDownload_id(this.get32UUID());
		if(download.getPid()==null || "".equals(download.getPid())){
			download.setPid("0");
		};
		download.setCreated_time(DateUtil.getTime());
		Boolean flag=false;
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			download.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				
				logBefore(logger, Jurisdiction.getUsername()+"新增产品分类图片");
				pd.put("productImageId", this.get32UUID());			//主键
				download.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", file.getOriginalFilename());					//标题
				pd.put("productImageUrl", ffile + "/" + fileName);	//路径
				pd.put("bz", "图片管理处上传");							//备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag=true;
			}
			pd.put("flag", flag);
			//保存
			service.save(download,pd);
			mav.addObject("result",new JsonResult(200,"添加成功"));
		}catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername()+"新增产品分类异常");
			mav.addObject("result",new JsonResult(500,"添加失败"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontentType.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	/**
	 * 刪除方法
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	@RequiresPermissions("downloadType:delete")
	public Map<String,Object> deleteFileType(String[] id) {
		Map<String,Object> map=new HashMap<>();
		try {
			 service.deleteFileType(id);
			 map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}

		return map;
	}
	/**
	 * 修改
	 * @return
	 */
//	@RequestMapping(value="/edit")
//	@ResponseBody
//	@RequiresPermissions("downloadType:edit")
//	public Map<String,Object> edit(FileType download,MultipartFile file,String[] columnids){
//		String ffile = DateUtil.getDays(), fileName = "";
//		PageData pd = this.getPageData();
//		Map<String,Object> map=new HashMap<>();
//		logBefore(logger, Jurisdiction.getUsername() + "修改product");
//		Boolean flag=false;
//		download.setUpdate_time(DateUtil.getTime());
//		download.setColumnids(columnids);
//		if(download.getPid()==null ||"".equals(download.getPid())){
//			download.setPid("0");
//		}
//		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
//		try {
//			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
//			download.setSiteid(siteId);
//			if (null != file && !file.isEmpty()) {
//				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传
//
//				
//				logBefore(logger, Jurisdiction.getUsername()+"新增产品分类图片");
//				pd.put("productImageId", this.get32UUID());			//主键
//				download.setImageid(pd.getString("productImageId"));
//				pd.put("productTitle", file.getOriginalFilename());					//标题
//				pd.put("productImageUrl", ffile + "/" + fileName);	//路径
//				pd.put("bz", "图片管理处上传");							//备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
//				flag=true;
//			}
//			pd.put("flag", flag);
//			service.update(download,pd); // 执行修改
//			map.put("success", true);
//		} catch (Exception e) {
//			map.put("success", false);
//			if (flag) {
//				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
//			}
//			e.printStackTrace();
//		}
//		
//		return map;
//	}
	
	
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("downloadType:edit")
	public ModelAndView edit(FileType download,MultipartFile file,String[] columnids,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId){
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();
		ModelAndView mav=this.getModelAndView();
		logBefore(logger, Jurisdiction.getUsername() + "修改product");
		Boolean flag=false;
		download.setUpdate_time(DateUtil.getTime());
		download.setColumnids(columnids);
		if(download.getPid()==null ||"".equals(download.getPid())){
			download.setPid("0");
		}
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			download.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				
				logBefore(logger, Jurisdiction.getUsername()+"新增产品分类图片");
				pd.put("productImageId", this.get32UUID());			//主键
				download.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", file.getOriginalFilename());					//标题
				pd.put("productImageUrl", ffile + "/" + fileName);	//路径
				pd.put("bz", "图片管理处上传");							//备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag=true;
			}
			pd.put("flag", flag);
			service.update(download,pd); // 执行修改
			mav.addObject("result",new JsonResult(200,"修改成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(500,"修改失败"));
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
			}
			e.printStackTrace();
		}
		
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontentType.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	@RequestMapping(value = "/updateSort")
	@ResponseBody
	@RequiresPermissions("downloadType:updateSort")
	public Map<String, Object> updateSort(String id,Integer sort) {
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
	@RequiresPermissions("downloadType:findcount")
	public Map<String, Object> findcount(String download_name,String download_id) {
		Map<String, Object> map = new HashMap<>();
		map.put("download_name", download_name);
		map.put("download_id", download_id);
		map.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			Integer i=service.findcount(map);
			if(i>0){
				map.put("success", false);
				map.put("count", i);
			}else{
				map.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/updataStatus")
	@ResponseBody
	@RequiresPermissions("downloadType:updataStatus")
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
}
