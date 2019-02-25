
package cn.cebest.controller.front.shaoyin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.WarrantyClaim.ContactUsService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月10日
 * @company 中企高呈
 */
@RestController
@RequestMapping("contact")
public class ContactUsController extends BaseController{
	@Autowired
	private ContactUsService contactUsService;
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public JsonResult saveContactUs(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	contactUsService.saveWarrantyClaim(pd);
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
}
