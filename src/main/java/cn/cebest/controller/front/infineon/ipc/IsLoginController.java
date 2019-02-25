package cn.cebest.controller.front.infineon.ipc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年9月30日
 * @company 中企高呈
 */
@Controller
@RequestMapping("login")
public class IsLoginController {
	
	/**
	 * 判断是否登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="isLogin",method=RequestMethod.GET)
	@ResponseBody
	public JsonResult collectionPlan(HttpServletRequest request){
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 判断是否登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="islogin",method=RequestMethod.GET)
	public String islogin(HttpServletRequest request){
		return "redirect:http://test.bestcms.com";
	}
	
	
}
