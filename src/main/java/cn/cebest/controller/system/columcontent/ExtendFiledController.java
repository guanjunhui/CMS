package cn.cebest.controller.system.columcontent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
@Controller
@RequestMapping("columcontent_extend")
public class ExtendFiledController  extends BaseController{
	@Autowired
	private ContentExtendFiledService service;
	/**
	 * 产品属性入口
	 * @return
	 */
	@RequestMapping("list")
	public String list(Map<String,Object> map,Page<List<PageData>> page){
		PageData pd=this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			Page<List<PageData>> pageResult=service.getData(page);
			map.put("page", pageResult);
		} catch (Exception e) {
			logger.error("select the extend occured error!", e);
		}
		pd.put("columType", pd.getString("TYPE"));
		map.put("pd", pd);
		return "system/cloulmcontent/extendFiled_list";
	}
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("getTree")
	@ResponseBody
	public Map<String,Object> getTree(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_3);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		try {
			map.put("tree", service.getData(pd));
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logger.error("select the extend occured error!", e);
		}
		return map;
	}
	
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("findCount")
	@ResponseBody
	public Map<String,Object> findCount(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("TYPE",Const.COLUM_TYPE_3);
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
	public String toUpdate(Map<String,Object> map,@ModelAttribute("columType") String columType,
			@ModelAttribute("columId") String columId,
			@ModelAttribute("topColumId") String topColumId){
		PageData pd=this.getPageData();
		pd.put("TYPE",columType);
		pd.put("COLUM_ID", pd.getString("columId"));
		try {
			map.putAll(service.findById(pd));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/cloulmcontent/extendFiled_edit";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("toAdd")
	public String toAdd(@ModelAttribute("columType") String columType,@ModelAttribute("columId") String columId,
			@ModelAttribute("topColumId") String topColumId){
		return "system/cloulmcontent/extendFiled_add";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("delete")
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
	@ResponseBody
	public Map<String,Object> save(@ModelAttribute("columType") String columType){
		PageData pd=this.getPageData();
		pd.put("ID", this.get32UUID());
		pd.put("TYPE",columType);
		pd.put("COLUM_ID", pd.getString("columId"));
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
	@ResponseBody
	public Map<String,Object> update(@ModelAttribute("columType") String columType){
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
		pd.put("TYPE",columType);
		pd.put("COLUM_ID", pd.getString("columId"));
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
