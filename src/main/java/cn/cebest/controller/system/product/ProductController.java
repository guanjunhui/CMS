package cn.cebest.controller.system.product;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Recommend;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.download.KeyValuesUtil;
import cn.cebest.entity.system.product.Brand;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.vo.RecommendVo;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.recommend.RecommendService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelRead;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;

/**
 * 产品管理
 * 
 * @author wzd
 *
 */
@Controller
@RequestMapping(value = "product")
public class ProductController extends BaseController {
	String menuUrl = "product/list.do"; // 菜单地址(权限用)

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "fhlogService")
	private FHlogManager FHLOG;
	
	@Resource(name = "seoService")
	private SeoService service;
	
	@Autowired
    private RecommendService recommendService;
	/**
	 * 入口方法
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@RequiresPermissions("product:list")
	public String index(Map<String, Object> map, Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "产品展示页");
		PageData pd = this.getPageData();
		String keywords = pd.getString("PRODUCT_KEYWORDS");
		if(keywords!=null && !"".equals(keywords)){
			pd.put("PRODUCT_KEYWORDS", keywords.trim());
		}
		if (pd.getString("NO_ASC") == null && pd.getString("NO_DESC") == null && pd.getString("NAME_ASC") == null
				&& pd.getString("NAME_DESC") == null && pd.getString("CREATED_TIME_ASC") == null
				&& pd.getString("CREATED_TIME_DESC") == null) {
			pd.put("UPDATE_TIME", "UPDATE_TIME");
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		productService.findProductToList(map, page);
		map.put("pd", pd);
		return "system/product/product_list";// 转发到
	}

	/**
	 * 展示排序
	 * 
	 * @param map
	 * @param page
	 * @return
	 */
	@RequestMapping("sortlistPage")
	@RequiresPermissions("product:sortlistPage")
	public String sortlistPage(Map<String, Object> map, Page page) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		if (pd.getString("NO_ASC") == null && pd.getString("NO_DESC") == null && pd.getString("NAME_ASC") == null
				&& pd.getString("NAME_DESC") == null && pd.getString("CREATED_TIME_ASC") == null
				&& pd.getString("CREATED_TIME_DESC") == null) {
			pd.put("UPDATE_TIME", "UPDATE_TIME");
		}
		try {
			logBefore(logger, Jurisdiction.getUsername() + "产品排序");
			productService.findProductToList(map, page);
			map.put("typeId", pd.getString("typeId"));
			map.put("columId", pd.getString("columId"));
			map.put("pd", pd);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "产品排序出现异常");
			e.printStackTrace();
		}
		return "system/product/product_list";// 转发到
	}

	/**
	 * 跳转到添加页面的方法
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getTree")
	@ResponseBody
	@RequiresPermissions("product:getTree")
	public Map<String, Object> getTree() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加product页面");
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();// 此时pd是没有数据的,表结构定下以后手动填一些参数
		pd.put("TEMPLATE_TYPE_3", Const.TEMPLATE_TYPE_3);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		productService.toAddFind(map, pd);

		return map;//
	}

	@RequestMapping(value = "toAdd")
	@RequiresPermissions("product:toAdd")
	public String toAdd(Map<String,Object> map,String flag,String columId,String topColumId) throws Exception {
		map.put("flag", flag);
		map.put("columId", columId);
		map.put("topColumId", topColumId);
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加product页面");
		return "system/product/product_add";// 转发到添加页
	}

	/**
	 * 前端发送ajax请求查询下级属性
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findProperty")
	@ResponseBody
	@RequiresPermissions("product:findProperty")
	public List<PageData> findProperty(String id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加product页面");
		return productService.findPropertyById(id);
	}

	/**
	 * 根据产品类型查询该类型下的所有产品
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findProductBytypeId")
	@ResponseBody
	@RequiresPermissions("product:findProductBytypeId")
	public List<Product> findProductBytypeId(String[] id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "根据产品类型查询该类型下的所有产品");
		if (id.length == 0) {
			return null;
		}
		return productService.findProductBytypeId(id);
	}
	
	@RequestMapping(value = "/findProductfByIds")
	@ResponseBody
	public List<PageData> findProductfByIds(String[] id) throws Exception {
		if (id.length == 0) {
			return null;
		}
		return productService.findProductfByIds(id);
	}

	/**
	 * 根据产品id查询类型
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findProductTypeById")
	@ResponseBody
	@RequiresPermissions("product:findProductTypeById")
	public List<Product> findProductTypeById(String[] id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "根据产品id查询类型");
		return productService.findProductTypeById(id);
	}

	/**
	 * 添加的方法,修改可能也用这个方法
	 * 
	 * @param product
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("product:add")
	public ModelAndView add(Product product, MultipartFile file,
			@RequestParam(value = "ids", required = false) String[] ids,
			@RequestParam MultipartFile[] sultipartFiles,
			@RequestParam MultipartFile[] images, int is_add,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {// 相关产品的id数组
		PageData pd = new PageData();
		ModelAndView mav = this.getModelAndView();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增product");
		product.setId(this.get32UUID());
		product.setCreated_Time(DateUtil.getTime());
		product.setUpdate_Time(DateUtil.getTime());
		product.setProductRelevantIdList(ids);
		if (product.getHot() == null || "".equals(product.getHot())) {
			product.setHot("0");
		}
		if (product.getRecommend() == null || "".equals(product.getRecommend())) {
			product.setRecommend("0");
		} else {
			product.setRecommend_time(DateUtil.getTime());
		}
		Boolean is_ColumnOrYype = false;
		List<KeyValuesUtil> key_values = new ArrayList<>();
		String[] typeids = product.getProducttypeids();
		List<String> columnids = product.getColumnids();
		if(columnids!=null && columnids.size()>0&&typeids==null){
			for (String string : columnids) {
					KeyValuesUtil key_value = new KeyValuesUtil();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
				
			}
			is_ColumnOrYype=true;
			product.setObjKey_Value(key_values);
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
			product.setObjKey_Value(key_values);
		}
		pd.put("is_ColumnOrYype", is_ColumnOrYype);
		Boolean flag = false;
		try {
			List<Video> files = product.getVideoList();
			for (int i=0;i<sultipartFiles.length;i++) {
				Video downloads = files.get(i);
				downloads.setId(this.get32UUID());
				downloads.setMaster_id(product.getId());
				if (null != sultipartFiles[i] && !sultipartFiles[i].isEmpty()) {
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
					fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
					downloads.setVideo_type(extName);//后缀名
					downloads.setVideo_url(ffile + "/" + fileName);// 路径
				}
			}

			List<Image> ig = product.getTimageList();// 图片
			for (int j = 0; j < images.length; j++) {
				Image downloads = ig.get(j);
				downloads.setImageId(this.get32UUID()); // 主键
				downloads.setMaster_id(product.getId());
				downloads.setForder(j+1);
				if (null != images[j] && !images[j].isEmpty()) {
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
					fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
					downloads.setImgurl(ffile + "/" + fileName); // 路径

				}
			}
			if (null != file && !file.isEmpty()) {
				String ffile = DateUtil.getDays(), fileName = "";
				String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

				logBefore(logger, Jurisdiction.getUsername() + "新增图片");
				pd.put("productImageId", this.get32UUID()); // 主键
				product.setImgeId(pd.getString("productImageId"));
				pd.put("productTitle", file.getOriginalFilename()); // 标题
				pd.put("productImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
			}
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
			product.setSiteId(siteId);
			product.setRelease_Time(DateUtil.getTime());
			pd.put("flag", flag);
			
			List<ExtendFiledUtil> fileds=product.getFileds();
			/*if(fileds!=null&&fileds.size()>0){
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				product.setFiledJson(json);
			}*/
			if(fileds!=null&&fileds.size()>0){
				MyCompartor mc = new MyCompartor();//创建比较器对象
			    Collections.sort(fileds,mc);     //按照age升序 22，23，
			    
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				product.setFiledJson(json);
			}
			productService.save(product, pd); // 执行保存
			mav.addObject("result", new JsonResult(200, "添加成功"));
		} catch (Exception e) {
			mav.addObject("result", new JsonResult(500, "添加失败"));
			logBefore(logger, Jurisdiction.getUsername() + "新增产品出现异常");
			e.printStackTrace();
		}

		if (is_add == 1) {
			//mav.setViewName("redirect:toAdd.do");
			mav.setViewName("redirect:toAdd.do?flag="+product.getFlag()+"&columId="+columId+"&topColumId="+topColumId);
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}

	/**
	 * 刪除商品的方法
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("product:delete")
	public String deleteProduct(String[] id) {
		try {
			productService.deleteProduct(id);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "redirect:list.do";
	}

	/**
	 * 跳转到修改页面的方法
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toEdit")
	@RequiresPermissions("product:toEdit")
	public String toEdit(Map<String, Object> map, String id,String columId,String topColumId) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转到修改页面");
		PageData pd = this.getPageData();// 此时pd是没有数据的,表结构定下以后手动填一些参数
		pd.put("TEMPLATE_TYPE_3", Const.TEMPLATE_TYPE_3);
		pd.put("MASTER_ID", id);
		map.put("seo", service.querySeoForObject(pd));
		Map<String,Object> resultMap=productService.toAddFind(map, pd);
		map.putAll(resultMap);
		map.put("relevantList", productService.findProductRelevant(id));
		Product product = productService.findProductById(id);
		map.put("brands", productService.findProductbrands(id));
		map.put("fproductids", productService.findProductfproductids(id));
		ObjectMapper objectMapper = new ObjectMapper();
		
		if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
			@SuppressWarnings("unchecked")
			List<ExtendFiledUtil> fileds = objectMapper.readValue(product.getFiledJson(), List.class);
			product.setFileds(fileds);
		}
		map.put("product", product);
		map.put("pd", pd);
		map.put("columId", columId);
		map.put("topColumId", topColumId);
		return "system/product/product_edit";// 转发到添加页
	}

	/**
	 * 查询相同类型的产品,点击相关产品选择按钮时接收ajax请求
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryProduct")
	@ResponseBody
	@RequiresPermissions("product:queryProduct")
	public List<PageData> queryProductByCode(String id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "查询相同类型的产品");
		return productService.findProductRelevantBycode(id);
	}

	/**
	 * 执行修改的方法
	 * 
	 * @param product
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/update")
	@RequiresPermissions("product:update")
	public ModelAndView update(Product product, MultipartFile file,
			@RequestParam MultipartFile[] sultipartFiles,
			@RequestParam(value = "ids", required = false) String[] ids,
			@RequestParam MultipartFile[] images, int is_add,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		PageData pd = new PageData();

		logBefore(logger, Jurisdiction.getUsername() + "修改product");
		Boolean flag = false;
		product.setUpdate_Time(DateUtil.getTime());
		product.setProductRelevantIdList(ids);
		ModelAndView mav = this.getModelAndView();

		
		Boolean is_ColumnOrYype = false;
		List<KeyValuesUtil> key_values = new ArrayList<>();
		String[] typeids = product.getProducttypeids();
		List<String> columnids = product.getColumnids();
		if(columnids!=null && columnids.size()>0&&typeids==null){
			for (String string : columnids) {
					KeyValuesUtil key_value = new KeyValuesUtil();
					key_value.setKey(string);
					key_value.setValues(null);
					key_values.add(key_value);
			}
			is_ColumnOrYype=true;
			product.setObjKey_Value(key_values);
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
			product.setObjKey_Value(key_values);
		}
		pd.put("is_ColumnOrYype", is_ColumnOrYype);
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		product.setSiteId(siteId);
		
		List<Video> files = product.getTvideoList();
		int vsize=product.getTvideoList().size();
		int n=0;
		for (int i=0;i<sultipartFiles.length;i++) {
				Video downloads=null;
				if (null != sultipartFiles[i] && !sultipartFiles[i].isEmpty()) {
				if(i>=vsize-1&&files.size()>n){
					downloads = files.get(n);
					downloads.setId(this.get32UUID());
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
					fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
					downloads.setVideo_type(extName);//后缀名
					downloads.setMaster_id(product.getId());
					downloads.setVideo_url(ffile + "/" + fileName);// 路径
					n++;
				}else{
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 文件上传路径
					String extName = sultipartFiles[i].getOriginalFilename().substring(sultipartFiles[i].getOriginalFilename().lastIndexOf("."));
					fileName = FileUpload.fileUp(sultipartFiles[i], filePath, this.get32UUID());// 保存文件,
					product.getVideoList().get(i).setVideo_url(ffile + "/" + fileName);
					product.getVideoList().get(i).setVideo_type(extName);
					String pach=product.getVideoList().get(i).getVideo_url();
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
					}
				}
			}
		}
		// 内容图
		List<Image> ig = product.getTimageList();// 图片
		int isize = product.getImageids().size();
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
					downloads.setMaster_id(product.getId());
					downloads.setImgurl(ffile + "/" + fileName); // 路径
					downloads.setForder(j);
					m++;
				} else {
					String ffile = DateUtil.getDays(), fileName = "";
					String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
					fileName = FileUpload.fileUp(images[j], filePath, this.get32UUID());// 保存文件,
					product.getImageList().get(j).setMaster_id(product.getId());
					String pach = product.getImageList().get(j).getImgurl();
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
					}
					product.getImageList().get(j).setImgurl(ffile + "/" + fileName);
					product.getImageList().get(j).setForder(j);
					product.getImageList().get(j).setFlag(true);
				}
			}
		}
		// 封面图
		if (null != file && !file.isEmpty()) {
			String ffile = DateUtil.getDays(), fileName = "";
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

			logBefore(logger, Jurisdiction.getUsername() + "新增图片");
			pd.put("productImageId", this.get32UUID()); // 主键
			product.setImgeId(pd.getString("productImageId"));
			pd.put("productTitle", file.getOriginalFilename()); // 标题
			pd.put("productImageUrl", ffile + "/" + fileName); // 路径
			pd.put("bz", "图片管理处上传"); // 备注
			Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
			flag = true;
		}
		pd.put("flag", flag);

		
		try {
			
			List<ExtendFiledUtil> fileds=product.getFileds();
			if(fileds!=null&&fileds.size()>0){
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(fileds);
				product.setFiledJson(json);
			}

			productService.updateProduct(product, pd); // 执行修改
			mav.addObject("result", new JsonResult(200, "修改成功"));
		} catch (Exception e) {
			mav.addObject("result", new JsonResult(500, "修改失败"));
			e.printStackTrace();
		}
		if (is_add == 1) {
			mav.setViewName("redirect:toAdd.do");
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}

	@RequestMapping(value = "/updataStatus")
	@ResponseBody
	@RequiresPermissions("product:updataStatus")
	public Map<String, Object> updataStatus(String[] ids, String product_Status) {
		Map<String, Object> map = new HashMap<>();
		map.put("ids", ids);
		map.put("product_Status", product_Status);
		try {
			logBefore(logger, Jurisdiction.getUsername() + "修改产品状态");
			productService.updataStatus(map);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 推荐
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataRecommend")
	@ResponseBody
	@RequiresPermissions("product:updataRecommend")
	public Map<String, Object> updataRecommend(String[] ids, String recommend) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "推荐产品");
		try {
			Product product = new Product();
			product.setRecommend_time(DateUtil.getTime());
			product.setProductRelevantIdList(ids);
			product.setRecommend(recommend);
			productService.updataRecommendAndTop(product);
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
	@RequiresPermissions("product:updataTop")
	public Map<String, Object> updataTop(String[] ids, String top) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "置顶产品");
		try {
			Product product = new Product();
			product.setTop_time(DateUtil.getTime());
			product.setProductRelevantIdList(ids);
			product.setTop(top);
			productService.updataRecommendAndTop(product);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "置顶产品出现异常");
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping("exprotTemplate")
	public void exprotTemplate(HttpServletRequest request, HttpServletResponse response) {
		logBefore(logger, Jurisdiction.getUsername() + "新增产品出现异常");
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("产品模板");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("产品名称");
		cell = row.createCell(1);
		cell.setCellValue("概要");
		cell = row.createCell(2);
		cell.setCellValue("关键字");
		cell = row.createCell(3);
		cell.setCellValue("编号");
		cell = row.createCell(4);
		cell.setCellValue("状态");
		cell = row.createCell(5);
		cell.setCellValue("跳转地址");
		// 把excel返回客户端
		try {
			OutputStream os;

			response.setContentType("application/octet-stream;charset=UTF-8");
			String browser = request.getHeader("User-Agent");
			String fn = URLEncoder.encode("产品列表", "UTF-8");
			if (browser.toLowerCase().contains("firefox")) {
				fn = new String("产品列表".getBytes("UTF-8"), "ISO8859-1");
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("exprot")
	public void exprotQueryList(String[] id, HttpServletRequest request, HttpServletResponse response) {
		PageData pd = this.getPageData();
		pd.put("ids", id);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("产品");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		// cell.setCellValue("ID");
		// cell = row.createCell(1);
		cell.setCellValue("名称");
		cell = row.createCell(1);
		cell.setCellValue("类型");
		cell = row.createCell(2);
		cell.setCellValue("模板名称");
		cell = row.createCell(3);
		cell.setCellValue("概要描述");
		cell = row.createCell(4);
		cell.setCellValue("关键字");
		cell = row.createCell(5);
		cell.setCellValue("创建时间");
		cell = row.createCell(6);
		cell.setCellValue("修改时间");
		cell = row.createCell(7);
		cell.setCellValue("编号");
		cell = row.createCell(8);
		cell.setCellValue("状态");
		cell = row.createCell(9);
		cell.setCellValue("跳转地址");
		cell = row.createCell(10);
		cell.setCellValue("品牌名称");
		cell = row.createCell(11);
		cell.setCellValue("图片title");
		try {
			List<Product> list = productService.downloadsQueryList(pd);
			if (list != null && list.size() > 0) {
				int i = 1;
				for (Product product : list) {
					row = sheet.createRow(i);
					// cell = row.createCell(0);
					// cell.setCellValue(product.getId());
					cell = row.createCell(0);
					cell.setCellValue(product.getName());
					cell = row.createCell(1);
					cell.setCellValue(product.getProductTypeList().get(0).getType_name());
					cell = row.createCell(2);
					cell.setCellValue(product.getProduct_TemplateName());
					cell = row.createCell(3);
					cell.setCellValue(product.getProduct_Summary());
					cell = row.createCell(4);
					cell.setCellValue(product.getProduct_KeyWords());
					cell = row.createCell(5);
					cell.setCellValue(product.getCreated_Time());
					cell = row.createCell(6);
					cell.setCellValue(product.getUpdate_Time());
					cell = row.createCell(7);
					cell.setCellValue(product.getNo());
					cell = row.createCell(8);
					cell.setCellValue(product.getProduct_Status().equals("1") ? "显示" : "隐藏");
					cell = row.createCell(9);
					cell.setCellValue(product.getProduct_WbUrl());
					cell = row.createCell(10);
					cell.setCellValue(product.getBrandName());
					cell = row.createCell(11);
					cell.setCellValue(product.getTitle());
					i++;
				}
			}
			// 把excel返回客户端
			OutputStream os;

			response.setContentType("application/octet-stream;charset=UTF-8");
			String browser = request.getHeader("User-Agent");
			String fn = URLEncoder.encode("产品列表", "UTF-8");
			if (browser.toLowerCase().contains("firefox")) {
				fn = new String("产品列表".getBytes("UTF-8"), "ISO8859-1");
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("improt")
	@ResponseBody
	public Map<String, Object> improtQueryList(MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		pd.put("TEMPLATE_TYPE_3", Const.TEMPLATE_TYPE_3);
		if (file.isEmpty()) {
			map.put("success", false);
		} else {
			try {
				String name = file.getOriginalFilename();
				String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
				if (name.matches("^.+\\.(?i)(xlsx)$")) {
					saveXlsx(file, pd, siteId);
				}
				;
				if (name.matches("^.+\\.(?i)(xls)$")) {
					saveXls(file, pd, siteId);
				}
				map.put("success", true);
			} catch (IOException e) {
				map.put("success", false);
				e.printStackTrace();
			} catch (Exception e) {
				map.put("success", false);
				e.printStackTrace();
			}
		}
		return map;
	}

	private void saveXls(MultipartFile file, PageData pd, String siteId) throws Exception {
		Integer count = 0;
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		Product p = null;
		List<Product> productList = new ArrayList<Product>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {// 行
			row = sheet.getRow(i);
			p = new Product();
			p.setSiteId(siteId);
			p.setId(this.get32UUID());
			p.setUpdate_Time(DateUtil.getTime());
			p.setCreated_Time(DateUtil.getTime());
			// 默认类型
			List<PageData> lpt = productService.findProduct_TypeAll(pd);
			// 类型
			p.setProducttypeids(new String[] { lpt.get(0).getString("ID") });
			// 默认品牌
			List<Brand> bl = productService.findBrandAll(pd);
			p.setBrandId(bl.get(0).getId());
			// 默认模板
			List<PageData> lt = productService.findProductTemplate(pd);
			p.setProduct_TemplateId(lt.get(0).getString("ID"));
			for (int j = 0; j < row.getLastCellNum(); j++) {// 列
				cell = row.getCell(j);
				if (j == 0) {// 第一列:name
					p.setName(cell.getStringCellValue());
				} else if (j == 1) {// 概要
					p.setProduct_Summary(cell.getStringCellValue());
				} else if (j == 2) {// 关键字
					p.setProduct_KeyWords(cell.getStringCellValue());
				} else if (j == 3) {// 编号
					p.setNo(cell.getStringCellValue());
				} else if (j == 4) {// 状态
					p.setProduct_Status("显示".equals(cell.getStringCellValue().trim()) ? "1" : "0");
				} else if (j == 5) {// 跳转地址
					p.setProduct_WbUrl(cell.getStringCellValue());
				}
			}
			productList.add(p);
			// 每三十条保存一次
			if (i % 20 == 0) {
				count += 1;
				productService.save(productList);
				productList.clear();
			}
		}
		// 最后保存一次
		if (productList.size() > 0) {
			productService.save(productList);
		}

	}

	private void saveXlsx(MultipartFile file, PageData pd, String siteId) throws Exception {
		Integer count = 0;
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		Product p = null;
		List<Product> productList = new ArrayList<Product>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {// 行
			row = sheet.getRow(i);
			p = new Product();
			p.setSiteId(siteId);
			p.setId(this.get32UUID());
			p.setUpdate_Time(DateUtil.getTime());
			p.setCreated_Time(DateUtil.getTime());
			// 默认类型
			List<PageData> lpt = productService.findProduct_TypeAll(pd);
			p.setProducttypeids(new String[] { lpt.get(0).getString("ID") });
			// 默认品牌
			List<Brand> bl = productService.findBrandAll(pd);
			p.setBrandId(bl.get(0).getId());
			// 默认模板
			List<PageData> lt = productService.findProductTemplate(pd);
			p.setProduct_TemplateId(lt.get(0).getString("ID"));
			for (int j = 0; j < row.getLastCellNum(); j++) {// 列
				cell = row.getCell(j);
				if (j == 0) {// 第一列:name
					p.setName(cell.getStringCellValue());
				} else if (j == 1) {// 概要
					p.setProduct_Summary(cell.getStringCellValue());
				} else if (j == 2) {// 关键字
					p.setProduct_KeyWords(cell.getStringCellValue());
				} else if (j == 3) {// 编号
					p.setNo(cell.getStringCellValue());
				} else if (j == 4) {// 状态
					p.setProduct_Status("显示".equals(cell.getStringCellValue().trim()) ? "1" : "0");
				} else if (j == 5) {// 跳转地址
					p.setProduct_WbUrl(cell.getStringCellValue());
				}
			}
			productList.add(p);
			// 每三十条保存一次
			if (i % 20 == 0) {
				count += 1;
				productService.save(productList);
				productList.clear();
			}
		}
		// 最后保存一次
		if (productList.size() > 0) {
			productService.save(productList);
		}

	}

	@RequestMapping("improtList")
	@ResponseBody
	public Map<String, Object> improtList(MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		PageData pd = this.getPageData();
		if (file.isEmpty()) {
			map.put("success", false);
		} else {
			try {

				String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE); // 文件上传路径
				String fileName = FileUpload.fileUp(file, filePath, "userexcel"); // 执行上传
				List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
				String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
				saveXlsx(listPd, siteId); // 2:从第3行开始；0:从第A列开始；0:第0个sheet

			} catch (IOException e) {
				map.put("success", false);
				e.printStackTrace();
			} catch (Exception e) {
				map.put("success", false);
				e.printStackTrace();
			}
		}
		return map;
	}

	private void saveXlsx(List<PageData> listPd, String siteId) throws Exception {
		Integer count = 0;

		List<Product> productList = new ArrayList<Product>();
		for (PageData pd : listPd) {// 行 int
			int i = 0;
			Product p = new Product();
			p.setSiteId(siteId);
			p.setId(this.get32UUID());
			p.setUpdate_Time(DateUtil.getTime());
			p.setCreated_Time(DateUtil.getTime());
			// 默认类型
			List<PageData> lpt = productService.findProduct_TypeAll(pd);
			p.setProducttypeids(new String[] { lpt.get(0).getString("ID") });
			// 默认品牌
			List<Brand> bl = productService.findBrandAll(pd);
			p.setBrandId(bl.get(0).getId());
			// 默认模板
			List<PageData> lt = productService.findProductTemplate(pd);
			p.setProduct_TemplateId(lt.get(0).getString("ID"));
			// 列

			p.setName(pd.getString("var0"));

			p.setProduct_Summary(pd.getString("var1"));

			p.setProduct_KeyWords(pd.getString("var2"));

			p.setNo(pd.getString("var3"));

			p.setProduct_Status("显示".equals(pd.getString("var4").trim()) ? "1" : "0");

			p.setProduct_WbUrl(pd.getString("var5"));

			productList.add(p);
			// 每三十条保存一次
			if (i % 20 == 0) {
				count += 1;
				productService.save(productList);
				productList.clear();
			}
			i++;
		}
		// 最后保存一次
		if (productList.size() > 0) {
			productService.save(productList);
		}
	}

	/**
	 * 验证编号
	 * 
	 * @param typeid
	 * @param no
	 * @return
	 */
	@RequestMapping("findNoCount")
	@ResponseBody
	public Map<String, Object> findNoCount(String[] typeid, String no) {
		Map<String, Object> map = new HashMap<>();
		map.put("typeid", typeid);
		map.put("no", no);
		try {
			productService.findNoCount(map);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
     * 保存推荐设置
     * @param RecommendVo recommendVo
     * @return
     */
    @RequestMapping("saveRecommend/{contentId}")
    @ResponseBody
    public JsonResult saveRecommend(@RequestBody RecommendVo[] recommendVo,
            @PathVariable("contentId") String contentId) {
        try {
            recommendService.deleteByContentId(contentId);
        } catch (Exception e) {
            logger.error("delete the product recommend setting data before save new setting occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
        }
        
        if(recommendVo!=null&&recommendVo.length>0){
            List<Recommend> recommendList = new ArrayList<Recommend>();
            for(RecommendVo vo:recommendVo){
                Recommend recommend = new Recommend();
                recommendList.add(recommend);
                recommend.setId(this.get32UUID());
                if(Const.NODE_TYPE_1.equals(vo.getType())){//栏目
                    recommend.setContentId(vo.getContentId());
                    recommend.setType(vo.getColumType());
                    recommend.setTypeId(StringUtils.EMPTY);
                    recommend.setColumId(vo.getId());
                }else if(Const.NODE_TYPE_2.equals(vo.getType())){//分类
                    recommend.setContentId(vo.getContentId());
                    recommend.setType(vo.getColumType());
                    recommend.setTypeId(vo.getId());
                    recommend.setColumId(vo.getColumId());
                }
            }
            //批量插入
            try {
                recommendService.saveBatch(recommendList);
            } catch (Exception e) {
                logger.error("save the product recommend data occured error!", e);
                return new JsonResult(Const.HTTP_ERROR, e.getMessage());
            }
        }
        return new JsonResult(Const.HTTP_OK, "OK");
    }
    
    
    
    /**获取推荐设置的关联关系
     * @return
     */
    @RequestMapping(value="/getRecommendRelation")
    @RequiresPermissions("product:getRecommendRelation")
    @ResponseBody
    public JsonResult getRoleIdsByUserId(@RequestParam(value="contentId") String contentId){
        List<Recommend> list=null;
        try{
            list=recommendService.selectByContentId(contentId);
        } catch(Exception e){
            logger.error("get the recommend setting relation occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
        }
        return new JsonResult(Const.HTTP_OK, "OK",list);
    }
    /*
	 * srot自定义排序
	 * **/
	public class MyCompartor implements Comparator
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
}
