package cn.cebest.controller.front.shaoyin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.service.system.WarrantyClaim.ProductRegistrationService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
@RestController
@RequestMapping("product/registration")
public class ProductRegistrationController extends BaseController{
	@Autowired
	private ProductRegistrationService productRegistrationService;
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public JsonResult saveContactUs(ProductRegistration pd,MultipartFile file){
		pd.setId(this.get32UUID());
	    //获取数据
	    try {
	    	productRegistrationService.saveProductRegistration(pd,file);
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	@RequestMapping(value="checkCode",method=RequestMethod.GET)
	public JsonResult checkSerialCode(@RequestParam(name="serialCode")String serialCode){
	    //获取数据
	    try {
	    	Integer count = productRegistrationService.checkSerialCode(serialCode);
	    	if(count != 1){
	    		return new JsonResult(Const.HTTP_ERROR, "serial code error!");
	    	}
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	@RequestMapping(value="updataRecommend",method=RequestMethod.POST)
	public JsonResult updataRecommend(){
		PageData pd = this.getPageData();
		//获取数据
		try {
			productRegistrationService.updataRecommend(pd);
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
}
