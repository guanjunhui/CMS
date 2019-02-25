package cn.cebest.controller.front.infineon.ipc;

import java.net.URLDecoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcSolutionService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年10月30日
 * @company 中企高呈
 */
@Controller
@RequestMapping("solution")
public class IpcSolutionController extends BaseController{
	
	private static Logger logger = Logger.getLogger(IpcTopicController.class);
	@Autowired
	private IpcSolutionService ipcSolutionService;
	
	/**
	 * 查询解决方案列表
	 * @return
	 */
	@RequestMapping(value = "/getSolutionList", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult listByUserId(Page page) {
		JsonResult jr = null;
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data;
		try {
			if(pd.get("queryKey") != null){
				//page.setQueryKey(URLDecoder.decode(pd.getString("queryKey").toString(),"UTF-8"));
				page.setQueryKey(pd.getString("queryKey").toString());
			}else{
				page.setQueryKey("");
			}
			data = ipcSolutionService.getSolutionByCondation(page);
			jr = new JsonResult(200, "success", data);
		} catch (Exception e) {
			jr = new JsonResult(500, "服务器异常");
			logger.error("查询我发表的话题列表异常：", e);
			e.printStackTrace();
		}
		
		return jr;
	}
}
