package cn.cebest.controller.system.product;

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
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.system.product.ProductTypeService;
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
 * 商品类型contorller
 * 
 * @author wad
 *
 */
@Controller
@RequestMapping(value = "/productType")
public class ProductTypeController extends BaseController {

	String menuUrl = "productType/productTypelistPage.do"; // 菜单地址(权限用)
	@Resource(name = "productTypeService")
	private ProductTypeService service;

	/**
	 * 去往添加的页面
	 * 
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/toAdd")
	@RequiresPermissions("productType:toAdd")
	public String toAdd() throws Exception {
		return "system/product/productType_add";
	}*/
	@RequestMapping(value = "/toAdd")
	@RequiresPermissions("productType:toAdd")
	public ModelAndView toAdd(@ModelAttribute(value="columId") String columId,
			@ModelAttribute(value="topColumId") String topColumId) {
			ModelAndView mv = this.getModelAndView();
			mv.setViewName("system/product/productType_add");
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
	@RequiresPermissions("productType:getTree")
	public JsonResult getTree(String columId) throws Exception {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("columId", columId);
		List<Product_Type> list = service.getTreeData(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	@RequestMapping(value = "/getTreeByColumId")
	@ResponseBody
	@RequiresPermissions("productType:getTreeByColumId")
	public JsonResult getTreeByColumId(String[] columnid) throws Exception {
		PageData pd = this.getPageData();
		pd.put("columnids", columnid);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<Product_Type> list = service.getTreeByColumId(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	@RequestMapping(value = "/getTypeByColumId")
	@ResponseBody
	@RequiresPermissions("productType:getTypeByColumId")
	public JsonResult getTypeByColumId(String[] columnid) throws Exception {
		PageData pd = this.getPageData();
		pd.put("columnids", columnid);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<Product_Type> list = service.getTypeByColumId(pd);
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
	@RequiresPermissions("productType:getlistTree")
	public JsonResult getlistTree() throws Exception {
		PageData pd = this.getPageData();
		String keywords = pd.getString("TYPE_KEYWORDS");
		if (keywords != null && !"".equals(keywords)) {
			pd.put("TYPE_KEYWORDS", keywords.trim());
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		List<Product_Type> list = service.getlistTreeData(pd);
		return new JsonResult(Const.HTTP_OK, "OK", list);
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	@RequiresPermissions("productType:save")
	public ModelAndView save(Product_Type product, MultipartFile file,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<>();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增产品分类");
		product.setId(this.get32UUID());
		if (product.getPid() == null || "".equals(product.getPid())) {
			product.setPid("0");
		}
		;
		product.setCreated_time(DateUtil.getTime());
		;
		Boolean flag = false;
		ModelAndView mav=this.getModelAndView();
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			product.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
				pd.put("productImageId", this.get32UUID()); // 主键
				product.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", file.getOriginalFilename()); // 标题
				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			pd.put("flag", flag);
			// 保存
			service.save(product, pd);
			//map.put("success", true);
			mav.addObject("result",new JsonResult(200,"添加成功"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类异常");
			//map.put("success", false);
			mav.addObject("result",new JsonResult(500,"添加失败"));
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
	@RequestMapping(value = "/productTypelistPage")
	@RequiresPermissions("productType:productTypelistPage")
	public String index() throws Exception {
		return "system/product/productType_list";
	}

	/**
	 * json格式展示页面
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	@RequiresPermissions("productType:listPage")
	public Map<String, Object> listPage() {
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			service.findProductTypeToList(map, pd);
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
	@RequiresPermissions("productType:toEdit")
	public String toEdit(Map<String, Object> map, String id,String columId ,String topColumId) {
		try {
			map.putAll(service.findProductTypeToEdit(map, id));
			PageData pd=this.getPageData();
			pd.put("MASTER_ID", id);
			map.put("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/product/productType_edit";
	}

	/**
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	@RequiresPermissions("productType:edit")
	public ModelAndView edit(Product_Type product, MultipartFile file,int is_add,
			@RequestParam("columId") String columId,
			@RequestParam("topColumId") String topColumId) {
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "修改");
		Boolean flag = false;
		product.setUpdate_time(DateUtil.getTime());
		if (product.getPid() == null || "".equals(product.getPid())) {
			product.setPid("0");
		}
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
		ModelAndView mav=this.getModelAndView();
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			product.setSiteid(siteId);
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
				pd.put("productImageId", this.get32UUID()); // 主键
				product.setImageid(pd.getString("productImageId"));
				pd.put("productTitle", file.getOriginalFilename()); // 标题
				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			pd.put("flag", flag);

			service.update(product, pd); // 执行修改

			//map.put("success", true);
			mav.addObject("result",new JsonResult(200,"修改成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(500,"修改失败"));
			map.put("success", false);
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
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
	@RequiresPermissions("productType:delete")
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
	@RequiresPermissions("productType:updataStatus")
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
	@RequiresPermissions("productType:updateSort")
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
	@RequiresPermissions("productType:findcount")
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

	@RequestMapping(value = "/findPropertycount")
	@ResponseBody
	@RequiresPermissions("productType:findPropertycount")
	public Map<String, Object> findPropertycount(String name, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("id", id);
		try {
			Integer i = service.findPropertycount(map);
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
	@RequiresPermissions("productType:findBrandcount")
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
}
