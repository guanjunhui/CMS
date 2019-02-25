package cn.cebest.controller.system.download;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
@Controller
@RequestMapping("downloadExtendFiledController")
public class DownloadExtendFiledController  extends BaseController{
	@Resource(name="contentExtendFiledServiceImpl")
	private ContentExtendFiledService service;
	/**
	 * 产品属性入口
	 * @return
	 */
	@RequestMapping("list")
	@RequiresPermissions("downloadExtendFiledController:list")
	public String list(Map<String,Object> map,Page page){
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_5);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			map.put("tree", service.getData(page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/download/extendFiled_list";
	}
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("getTree")
	@RequiresPermissions("downloadExtendFiledController:getTree")
	@ResponseBody
	public Map<String,Object> getTree(String id){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_5);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("COLUM_ID", id);
		try {
			map.put("tree", service.getData(pd));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("findCount")
	@RequiresPermissions("downloadExtendFiledController:findCount")
	@ResponseBody
	public Map<String,Object> findCount(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_5);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			Integer i=(Integer) service.findCount(pd);
			map.put("count", i);
			if(i==null || i==0)
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
	@RequiresPermissions("downloadExtendFiledController:toUpdate")
	public String toUpdate(Map<String,Object> map){
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_5);
		try {
			service.findById(pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/download/extendFiled_edit";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("toAdd")
	@RequiresPermissions("downloadExtendFiledController:toAdd")
	public String toAdd(){
		return "system/download/extendFiled_add";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("downloadExtendFiledController:delete")
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
	@RequiresPermissions("downloadExtendFiledController:save")
	@ResponseBody
	public Map<String,Object> save(){
		PageData pd=this.getPageData();
		pd.put("ID", this.get32UUID());
		pd.put("TYPE",Const.COLUM_TYPE_5);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		Map<String,Object> map=new HashMap<>();
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
	@RequiresPermissions("downloadExtendFiledController:update")
	@ResponseBody
	public Map<String,Object> update(){
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
		pd.put("TYPE",Const.COLUM_TYPE_5);
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
