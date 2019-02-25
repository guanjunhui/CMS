package cn.cebest.controller.front.shaoyin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.system.ServiceNetwork.ServiceNetwork;
import cn.cebest.service.system.serviceNetwork.ServiceNetworkService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value = "/frontSelectNetwork")
public class selectNetwork extends BaseController {
	
	@Resource(name="serviceNetworkService")
	private ServiceNetworkService serviceNetworkService;
	/**
	 * 前端页面查询经销商网点
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectNetworkByName")
	@ResponseBody
	public JsonResult selectNetworkByName(HttpServletRequest request){
		PageData pd = new PageData();
		pd = this.getPageData();
		List<ServiceNetwork> resultList = null;
	    //获取数据
	    try {
	    	resultList = serviceNetworkService.selectNetworkByName(pd);
		} catch (Exception e) {
			logger.error("save the question occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",resultList);
	}	
	
}