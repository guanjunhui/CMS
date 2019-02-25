package cn.cebest.controller.system.infineon;

import java.net.URLDecoder;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.ContactPlanService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;

/**
 * 联系该方案接口
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
@Controller
@RequestMapping(value="contactPlan")
public class ContactPlanController extends BaseController {
	String menuUrl = "contactPlan/list.do"; //菜单地址(权限用)
	@Autowired
	private ContactPlanService contactPlanService;
	
	/**
	 * 联系方案列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("list")
	@RequiresPermissions("contactPlan:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = contactPlanService.ListPage(page);
		mv.setViewName("system/infineonipc/contactPlanList");
		mv.addObject("list", list);
		return mv;
	}
	
	/**
	 * 联系方案详情列表（点赞收藏）
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("countList")
	@RequiresPermissions("contactPlan:countList")
	public ModelAndView index1(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = contactPlanService.ListDetailPage(page);
		mv.setViewName("system/infineonipc/contactPlanDetailList");
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
	@RequiresPermissions("contactPlan:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = contactPlanService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询联系方案详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/infineonipc/contactPlanDetail");
		mv.addObject("data", obj);
		return mv;
	}
	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteById")
	@RequiresPermissions("contactPlan:deleteById")
	public String deleteById(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				
				PageData pd = new PageData();
				pd.put("id", id);
				contactPlanService.deleteById(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除联系方案异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}
	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteByIds")
	@RequiresPermissions("contactPlan:deleteByIds")
	public String deleteByIds(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				contactPlanService.deleteByIds(ids);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "批量删除联系方案异常");
			e.printStackTrace();
		}
		
		return "redirect:list.do";
	}
	
	/**
	 * 更新处理状态
	 * @return
	 */
	@RequestMapping(value="updateHandle",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult updateHandle(){
		PageData pd = this.getPageData();
		//获取数据
		try {
			contactPlanService.updateHandle(pd);
		} catch (Exception e) {
			logger.error("更新联系方案处理状态错误!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
}
