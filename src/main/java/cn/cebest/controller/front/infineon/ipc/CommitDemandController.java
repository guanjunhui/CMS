package cn.cebest.controller.front.infineon.ipc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.infineon.CommitDemandService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 * 我有方案表单
 * @author wangweijie
 * @Date 2018年9月25日
 * @company 中企高呈
 */
@RestController
@RequestMapping("commitDemand")
public class CommitDemandController extends BaseController{
	@Autowired
	private CommitDemandService commitDemandService;
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public JsonResult saveContactUs(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	commitDemandService.saveCommitDemand(pd);
		} catch (Exception e) {
			logger.error("save the commitDemand occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
}
