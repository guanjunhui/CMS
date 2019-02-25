package cn.cebest.controller.system.shaoYin;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.ContactUsService;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
/** 
 * 说明：提问接口
 * 创建人：lwt
 * @version
 */
@Controller
@RequestMapping(value="/contactUs")
public class ContactUsController extends BaseController {
	
	String menuUrl = "contactUs/list.do"; //菜单地址(权限用)
	@Resource
	private ContactUsService contactUsService;
	
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
	@RequiresPermissions("contactUs:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = contactUsService.ListPage(page);
		mv.setViewName("system/shaoYin/contactUsList");
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
	@RequiresPermissions("contactUs:delete")
	public String deleteContactUs(String id) {
		try {
			if(id!=null && id!=""){
				
				PageData pd = new PageData();
				pd.put("ID", id);
				contactUsService.delContactUs(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除联系我们异常");
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
	@RequiresPermissions("contactUs:deletes")
	public String deletesContactUs(String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				contactUsService.delsContactUs(ids);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除联系我们异常");
			e.printStackTrace();
		}
		
		return "redirect:list.do";
	}
	/**
	 * 根据id查询详情
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("detailById")
	@RequiresPermissions("contactPlan:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = contactUsService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询联系我们详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/shaoYin/contactUsDetail");
		mv.addObject("data", obj);
		return mv;
	}
}
