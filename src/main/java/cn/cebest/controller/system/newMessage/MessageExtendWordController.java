package cn.cebest.controller.system.newMessage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.newMessage.MessageExtendService;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
@Controller
@RequestMapping("messageExtendWord")
public class MessageExtendWordController extends BaseController{
	@Resource
	private MessageExtendService service;
	/**
	 * 产品属性入口
	 * @return
	 */
	@RequestMapping("list")
	@RequiresPermissions("messageExtendWord:list")
	public String list(Map<String,Object> map,Page page){
		try {
			PageData pd = page.getPd();
			pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
			map.put("tree", service.getData(page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/messageExtendWords/messageExtendWord_list";
	}
	/**
	 * 取得展示数据
	 * @return
	 */
	@RequestMapping("getTree")
	@RequiresPermissions("messageExtendWords:getTree")
	@ResponseBody
	public Map<String,Object> getTree(){
		Map<String,Object> map=new HashMap<>();
		PageData pd=this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
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
	@RequiresPermissions("property:toUpdate")
	public String toUpdate(Map<String,Object> map,String id){
		try {
			service.findById(map,id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/messageExtendWords/messageExtendWord_edit";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping("toAdd")
	@RequiresPermissions("messageExtendWord:toAdd")
	public String toAdd(){
		return "system/messageExtendWords/messageExtendWord_add";
	}
	/**
	 * 删除扩展字段
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("messageExtendWord:delete")
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
	 * 保存扩展字段
	 * @return
	 */
	@RequestMapping("save")
	@RequiresPermissions("messageExtendWord:save")
	@ResponseBody
	public Map<String,Object> save(){
		PageData pd=this.getPageData();
		pd.put("id", this.get32UUID());
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
	 * 修改扩展字段
	 * @return
	 */
	@RequestMapping("update")
	@RequiresPermissions("messageExtendWord:update")
	@ResponseBody
	public Map<String,Object> update(){
		PageData pd=this.getPageData();
		Map<String,Object> map=new HashMap<>();
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
