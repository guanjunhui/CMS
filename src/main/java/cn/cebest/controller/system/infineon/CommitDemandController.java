package cn.cebest.controller.system.infineon;

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
import cn.cebest.service.system.infineon.CommitDemandService;
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
@RequestMapping(value="commitDemand")
public class CommitDemandController extends BaseController {
	String menuUrl = "commitDemand/list.do"; //菜单地址(权限用)
	@Autowired
	private CommitDemandService commitDemandService;
	
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
	@RequiresPermissions("commitDemand:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> list = commitDemandService.ListPage(page);
		mv.setViewName("system/infineonipc/commitDemandList");
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
	@RequiresPermissions("commitDemand:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = commitDemandService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询我有需求详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/infineonipc/commitDemandDetail");
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
	@RequiresPermissions("commitDemand:deleteById")
	public String deleteById(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				
				PageData pd = new PageData();
				pd.put("id", id);
				commitDemandService.deleteById(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除我有需求异常");
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
	@RequiresPermissions("commitDemand:deleteByIds")
	public String deleteByIds(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				commitDemandService.deleteByIds(ids);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "批量删除我有需求异常");
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
			commitDemandService.updateHandle(pd);
		} catch (Exception e) {
			logger.error("更新我有需求处理状态错误!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
}
