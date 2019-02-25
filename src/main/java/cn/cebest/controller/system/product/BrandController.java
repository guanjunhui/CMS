package cn.cebest.controller.system.product;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.product.BrandService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Watermark;

@Controller
@RequestMapping("brand")
public class BrandController extends BaseController{
	@Resource(name="brandService")
	private BrandService service;
	/**
	 * 产品属性入口
	 * @return
	 */
	@RequestMapping("list")
	@RequiresPermissions("brand:list")
	public String list(){
		return "system/brand/brand_list";
	}
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("getTree")
	@RequiresPermissions("brand:getTree")
	@ResponseBody
	public Map<String,Object> getTree(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		pd.put("SITEID", siteId);
		try {
			map.put("tree", service.getTree(pd));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 跳转到修改页面
	 * @return
	 */
	@RequestMapping("toUpdate")
	@RequiresPermissions("brand:toUpdate")
	public String toUpdate(Map<String,Object> map,String id){
		try {
			service.findById(map,id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/brand/brand_edit";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("toAdd")
	@RequiresPermissions("brand:toAdd")
	public String toAdd(){
		return "system/brand/brand_add";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("brand:delete")
	@ResponseBody
	public Map<String,Object> delete(String[] id){
		Map<String,Object> map=new HashMap<>();
		try {
			service.delete(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 保存属性
	 * @return
	 */
	@RequestMapping("save")
	@RequiresPermissions("brand:save")
	@ResponseBody
	public Map<String,Object> save(MultipartFile file,String name,String pid){
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
		pd.put("id", this.get32UUID());
		if(pid==null || "".equals(pid)){
			pd.put("pid", "0");
		}else{
			pd.put("pid", pid);
		}
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		pd.put("siteid", siteId);
		pd.put("name", name);
		pd.put("createTime", DateUtil.getTime());
		Boolean flag = false;
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
			pd.put("productImageId", this.get32UUID()); // 主键
			//product.setImageid(pd.getString("productImageId"));
			pd.put("productTitle", file.getOriginalFilename()); // 标题
			pd.put("productImageUrl", ffile + "/" + fileName); // 路径
			pd.put("bz", "图片管理处上传"); // 备注
			Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
			flag = true;
		}
		pd.put("flag", flag);
		try {
			service.save(pd);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 保存属性
	 * @return
	 */
	@RequestMapping("update")
	@RequiresPermissions("brand:update")
	@ResponseBody
	public Map<String,Object> update(MultipartFile file,String name,String pid,String id){
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
		pd.put("id", id);
		if(pid==null || "".equals(pid)){
			pd.put("pid", "0");
		}
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		pd.put("siteid", siteId);
		pd.put("name", name);
		pd.put("pid", pid);
		Boolean flag = false;
		// 封面图
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行上传

			logBefore(logger, Jurisdiction.getUsername() + "新增产品分类图片");
			pd.put("productImageId", this.get32UUID()); // 主键
			//product.setImageid(pd.getString("productImageId"));
			pd.put("productTitle", file.getOriginalFilename()); // 标题
			pd.put("productImageUrl", ffile + "/" + fileName); // 路径
			pd.put("bz", "图片管理处上传"); // 备注
			Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
			flag = true;
		}
		pd.put("flag", flag);
		try {
			service.update(pd);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
}
