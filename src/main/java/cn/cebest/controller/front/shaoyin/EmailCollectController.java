package cn.cebest.controller.front.shaoyin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.WarrantyClaim.EmailCollectService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月24日
 * @company 中企高呈
 */
@RequestMapping("email")
@Controller
public class EmailCollectController extends BaseController{
	@Autowired
	private EmailCollectService emailCollectService;
	/**
	 * 邮箱收集
	 * @param request
	 * @return
	 */
	@RequestMapping(value="collect",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult collect(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	Integer count = emailCollectService.save(pd);
	    	if(count == 1){
	    		return new JsonResult(Const.HTTP_ERROR, "email exist");
	    	}
		} catch (Exception e) {
			logger.error("save the regist user occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, "server error,please wait again...");
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
}
