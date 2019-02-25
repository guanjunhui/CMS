package cn.cebest.controller.system.shaoYin;

import java.util.List;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.ProductRegistrationService;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
/** 
 * 说明：提问接口
 * 创建人：lwt
 * @version
 */
@Controller
@RequestMapping(value="registration")
public class ProductRegistrationController extends BaseController {
	
	String menuUrl = "registration/list.do"; //菜单地址(权限用)
	@Autowired
	private ProductRegistrationService productRegistrationService;
	
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
	@RequiresPermissions("registration:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = productRegistrationService.ListPage(page);
		mv.setViewName("system/shaoYin/productRegistrationList");
		mv.addObject("list", list);
		return mv;
	}
	
	/**
	 * 根据id查询详情
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("detailById")
	@RequiresPermissions("contactUs:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = productRegistrationService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询商品注册详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/shaoYin/productRegistrationDetail");
		mv.addObject("data", obj);
		return mv;
	}
}
