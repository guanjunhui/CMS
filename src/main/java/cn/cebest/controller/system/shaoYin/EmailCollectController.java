package cn.cebest.controller.system.shaoYin;

import java.util.List;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.EmailCollectService;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月24日
 * @company 中企高呈
 */
@RequestMapping("sysEmail")
@Controller
public class EmailCollectController extends BaseController{
	String menuUrl = "sysEmail/list.do"; //菜单地址(权限用)
	@Autowired
	private EmailCollectService emailCollectService;
	/**
	 * 邮箱收集
	 * @param request
	 * @return
	 */
	@RequestMapping(value="list",method=RequestMethod.GET)
	@RequiresPermissions("sysEmail:list")
	public ModelAndView list(@SuppressWarnings("rawtypes") Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = null;
		try {
			list = emailCollectService.ListPage(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("system/shaoYin/emailCollectList");
		mv.addObject("list", list);
		return mv;
	}
	
	/**
	 * 刪除联系我们记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sysEmail:delete")
	public String deleteContactUs(String id) {
		try {
			if(id!=null && id!=""){
				PageData pd = new PageData();
				pd.put("ID", id);
				emailCollectService.delete(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除邮箱异常");
			e.printStackTrace();
		}
		return "redirect:list.do";
	}
	
	/**
	 * 刪除联系我们记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/deletes")
	@RequiresPermissions("sysEmail:deletes")
	public String deletesContactUs(String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				emailCollectService.deletes(ids);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除邮箱异常");
			e.printStackTrace();
		}
		
		return "redirect:list.do";
	}
}
