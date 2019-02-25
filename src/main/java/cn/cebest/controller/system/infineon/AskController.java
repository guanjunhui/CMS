package cn.cebest.controller.system.infineon;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcTopicService;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;

/**
 * 联系该方案接口
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
@Controller
@RequestMapping(value=" ipcAsk")
public class AskController extends BaseController {
	String menuUrl = " ipcAsk/list.do"; //菜单地址(权限用)
	@Autowired
	private IpcTopicService ipcTopicService;
	
	/**
	 * 技术问答列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("list")
	@RequiresPermissions("ipcAsk:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = ipcTopicService.getAllList(page);
		mv.setViewName("system/infineonipc/askList");
		mv.addObject("list", data.get("data"));
		return mv;
	}
	/**
	 * 技术问答列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("replyList")
	@RequiresPermissions("ipcAsk:replyList")
	public ModelAndView replyList(Page page) throws Exception { 
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = ipcTopicService.getAllReplyList(page);
		mv.setViewName("system/infineonipc/askReplyList");
		mv.addObject("list", data.get("data"));
		mv.addObject("manage_topicId", page.getQueryKey());
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
				obj = ipcTopicService.detailById(id);
			}
		} catch (Exception e) {
			logger.error("根据id查询话题详情失败:", e);
		}
		mv.setViewName("system/infineonipc/askDetail");
		mv.addObject("data", obj);
		return mv;
	}
	/**
	 * 根据话题id删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteById")
	@RequiresPermissions("ipcAsk:deleteById")
	public String deleteById(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				
				PageData pd = new PageData();
				pd.put("id", id);
				ipcTopicService.deleteById(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除联系话题异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}
	/**
	 * 根据回复id删除回复数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteReplyById")
	@RequiresPermissions("ipcAsk:deleteReplyById")
	public String deleteReplyById(@RequestParam(name="id",required=true)String id,
								  @RequestParam(name="topicId",required=true)String topicId) {
		try {
			if(id!=null && id!=""){
				
				PageData pd = new PageData();
				pd.put("id", id);
				ipcTopicService.deleteReplyById(pd);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除话题回复异常");
			e.printStackTrace();
		}

		return "redirect:replyList.do?queryKey="+topicId;
	}
	
	/**
	 * 根据ids批量删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteByIds")
	@RequiresPermissions("ipcAsk:deleteByIds")
	public String deleteByIds(@RequestParam(name="id",required=true)String id) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				PageData pd = new PageData();
				for (String _id : ids) {
					pd.put("id", _id);
					this.ipcTopicService.deleteById(pd);
				}
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "批量删除联系话题异常");
			e.printStackTrace();
		}
		
		return "redirect:list.do";
	}
	/**
	 * 根据ids批量删除话题回复数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteReplyByIds")
	@RequiresPermissions("ipcAsk:deleteReplyByIds")
	public String deleteReplyByIds(@RequestParam(name="id",required=true)String id,
								   @RequestParam(name="topicId",required=true)String topicId) {
		try {
			if(id!=null && id!=""){
				String[] ids = id.split(",");
				PageData pd = new PageData();
				for (String _id : ids) {
					pd.put("id", _id);
					this.ipcTopicService.deleteReplyById(pd);
				}
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "批量删除联系话题异常");
			e.printStackTrace();
		}
		
		return "redirect:replyList.do?queryKey="+topicId;
	}
	
}
