package cn.cebest.controller.system.shaoYin;

import java.util.List;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.UserService;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月24日
 * @company 中企高呈
 */
@Controller
@RequestMapping("webUser")
public class WebUserController extends BaseController{
	String menuUrl = "webUser/list.do"; //菜单地址(权限用)
	@Autowired
	private UserService userService;
	
	/**
	 * 联系我们列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("list")
	@RequiresPermissions("webUser:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = userService.ListPage(page);
		mv.setViewName("system/shaoYin/webUserList");
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
	@RequiresPermissions("webUser:delete")
	public String deleteContactUs(String id) {
		try {
			if(id!=null && id!=""){
				PageData pd = new PageData();
				pd.put("ID", id);
				userService.delete(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除前台会员异常");
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
	@RequiresPermissions("webUser:deletes")
	public String deletesContactUs(String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				userService.deletes(ids);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除前台会员异常");
			e.printStackTrace();
		}
		
		return "redirect:list.do";
	}
	/**
	 * 根据id查询详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("detailById")
	@RequiresPermissions("webUser:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = userService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询前台会员详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/shaoYin/webUserDetail");
		mv.addObject("data", obj);
		return mv;
	}
}
