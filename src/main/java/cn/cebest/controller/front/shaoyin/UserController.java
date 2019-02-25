package cn.cebest.controller.front.shaoyin;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.WarrantyClaim.UserService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.redis.RedisUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController{
	@Autowired
	private UserService userService;
	
	/**
	 * 注册
	 * @param request
	 * @return
	 */
	@RequestMapping(value="regist",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult regist(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	Integer count = userService.saveUser(pd);
	    	if(count == 1){
	    		return new JsonResult(Const.HTTP_ERROR_400, "邮箱已存在");
	    	}
		} catch (Exception e) {
			logger.error("save the regist user occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult login(HttpServletRequest request){
		PageData pd = this.getPageData();
	    //获取数据
		JsonResult result = null;
	    try {
	    	Object obj = userService.queryUser(pd);
	    	if(obj != null){
				request.getSession().setAttribute("currentUser",obj);
				result = new JsonResult(Const.HTTP_OK, "OK");
				result.setData(obj);
			}else{
				return new JsonResult(Const.HTTP_ERROR_405, "email or password error!");
			}
		} catch (Exception e) {
			logger.error("the user login occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return result;
	}
	/**
	 * 退出
	 * @param request
	 * @return
	 */
	@RequestMapping(value="loginOut",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult loginOut(HttpServletRequest request){
		//获取数据
		try {
			Object obj = request.getSession().getAttribute("currentUser");
			if(obj != null){
				request.getSession().removeAttribute("currentUser");
			}
		} catch (Exception e) {
			logger.error("the user loginOut occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="sendEmail",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult sendEmail(@RequestParam(name="email")String email,HttpServletRequest request){
	    //获取数据
	    try {
	    	Integer count = userService.sendEmail(email,request);
	    	if(count != 1){
				return new JsonResult(Const.HTTP_ERROR_405, "email error!");
			}
		} catch (Exception e) {
			logger.error("the user login occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 跳转重设密码页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="forgetPassword",method=RequestMethod.GET)
	public ModelAndView forgetPassword(@RequestParam(name="token",required=true)String token){
		ModelAndView mav = this.getModelAndView();
		String email = RedisUtil.getString(token);
		//链接一次生效
		RedisUtil.delString(token);
		if(email != "" && email != null){
			//mav.addObject("email", email);
			mav.setViewName("forward:/channel/669e16da66c54014b5cdfe2f5819e6c8.html?email="+email);
		}
		return mav;
	}
	/**
	 * 重设密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="resetPassword",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult resetPassword(){
		PageData pd = this.getPageData();
		//获取数据
	    try {
	    	userService.upatePassword(pd);
		} catch (Exception e) {
			logger.error("the user login occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
}
