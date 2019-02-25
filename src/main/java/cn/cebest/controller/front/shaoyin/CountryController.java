package cn.cebest.controller.front.shaoyin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.WarrantyClaim.CountryService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月17日
 * @company 中企高呈
 */
@Controller
@RequestMapping("country")
public class CountryController extends BaseController{
	@Autowired
	private CountryService countryService;
	
	/**
	 * 获取所有国家
	 * @param request
	 * @return
	 */
	@RequestMapping(value="allCountrys",method=RequestMethod.GET)
	@ResponseBody
	public JsonResult getAllCountrys(HttpServletRequest request){
	    //获取数据
		Object obj = null;
	    try {
	    	obj = countryService.getAllCountrys();
	    	
	    } catch (Exception e) {
			logger.error("save the regist user occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",obj);
	}
}
