package cn.cebest.controller.system.content;

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
@RequestMapping("contentExtendFiledController")
public class ContentExtendFiledController extends BaseController{
	@Resource(name="contentExtendFiledServiceImpl")
	private ContentExtendFiledService service;
	/**
	 * 产品属性入口
	 * @return
	 */
	@RequestMapping("list")
	@RequiresPermissions("contentExtendFiledController:list")
	public String list(Map<String,Object> map,Page page){
		PageData pd=this.getPageData();
		page.setPd(pd);
		pd.put("TYPE",Const.COLUM_TYPE_1);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			map.put("tree", service.getData(page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/content/extendFiled_list";
	}
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("getTree")
	@RequiresPermissions("contentExtendFiledController:getTree")
	@ResponseBody
	public Map<String,Object> getTree(String id){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		//pd.put("TYPE",Const.COLUM_TYPE_1);
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
	@RequiresPermissions("contentExtendFiledController:findCount")
	@ResponseBody
	public Map<String,Object> findCount(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("TYPE",1);
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
	@RequiresPermissions("contentExtendFiledController:toUpdate")
	public String toUpdate(Map<String,Object> map){
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_1);
		try {
			service.findById(pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/content/extendFiled_edit";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("toAdd")
	@RequiresPermissions("contentExtendFiledController:toAdd")
	public String toAdd(){
		return "system/content/extendFiled_add";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("contentExtendFiledController:delete")
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
	@RequiresPermissions("contentExtendFiledController:save")
	@ResponseBody
	public Map<String,Object> save(){
		PageData pd=this.getPageData();
		pd.put("ID", this.get32UUID());
		pd.put("TYPE",Const.COLUM_TYPE_1);
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
	@RequiresPermissions("contentExtendFiledController:update")
	@ResponseBody
	public Map<String,Object> update(){
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
		pd.put("TYPE",Const.COLUM_TYPE_1);
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
