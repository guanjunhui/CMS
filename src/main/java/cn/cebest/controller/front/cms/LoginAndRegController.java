package cn.cebest.controller.front.cms;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.bo.MesageBo;
import cn.cebest.entity.system.Phone;
import cn.cebest.service.system.personal.LoginSevice;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.MD5;
import cn.cebest.util.PageData;
import cn.cebest.util.SendMessage;
import cn.cebest.util.mail.SimpleMailSender;

@Controller
@RequestMapping("/personal")
public class LoginAndRegController extends BaseController {
	
	@Resource(name = "loginSevice")
	private LoginSevice loginSevice;
	
	
	/**
	 * 登录
	 * @param password
	 * @param phone
	 * @return66
	 */
	@RequestMapping("/login")
	@ResponseBody
	public JsonResult login(String password,String phone){
		HttpSession session = getSession();
		PageData pd = new PageData();
		pd.put("phone", phone);
		pd.put("password", MD5.md5(password));
		Phone phoneVo = null ;
		try {
			phoneVo=loginSevice.login(pd);
		} catch (Exception e) {
			logger.error(" login occured error!", e);
			return new JsonResult(Const.HTTP_ERROR_400, "账户或密码错误");
		}
		
		if(phoneVo == null){
			return new JsonResult(Const.HTTP_ERROR_400, "账户或密码输入有误");
		}else{
			session.setAttribute("phoneVo", phoneVo);
			return new JsonResult(Const.HTTP_OK, "ok",phoneVo);
		}
	}
	
	/**
	 * 发送验证码
	 * @param phoneNum
	 * @return
	 */
	@RequestMapping("/sendVerification")
	@ResponseBody
	public JsonResult getVerification(String phoneNum,Integer templateType){
		String code = makePhoneNote();	
		MesageBo bo = new MesageBo();
		bo.setPhoneNumbers(phoneNum);
		bo.setTemplateParam(code);
		bo.setOutId("youname");
		try {
			SendMessage.sendMessage(bo,templateType);
		} catch (Exception e) {
			logger.error("SendMessage sendMessage occured error!", e);
			return new JsonResult(Const.HTTP_ERROR_400, "验证码发送错误！");
		}
		return new JsonResult(Const.HTTP_OK, "ok");
	}
	
	/**
	 * 制作短信验证码
	 * 
	 * @return
	 */
	private String makePhoneNote() {
		Random random = new Random();
		String verification = random.nextInt(9999 - 1000 + 1) + 1000 + "";
		HttpSession session = getSession();
		session.setAttribute(Const.SESSION_SECURITY_NOTE_CODE, verification);
		// 拼接code
		String code = "{\"code\":\"" + verification + "\"}";
		return code;
	}
	
	/**
	 * 查询手机是否注册过
	 * @param phoneNum
	 * @return
	 */
	@RequestMapping("checkphone")
	@ResponseBody
	public JsonResult findPersonageByPhone(String phoneNum){
		Phone phoneVo = new Phone();
		PageData pd = new PageData();
		pd.put("phone", phoneNum);
		try {
			phoneVo=loginSevice.findPersonageByPhone(pd);
		} catch (Exception e) {
			logger.error("select the personage datas occured error!", e);
			return new JsonResult(Const.HTTP_ERROR_402, e.toString());
		}
		if(phoneVo == null ){
			return new JsonResult(Const.HTTP_OK, "ok");
		}else{
			return new JsonResult(Const.HTTP_ERROR_403, "该手机号已存在");
		}
		
	}
	
	/**
	 * 用户注册
	 * @param formData
	 * @return
	 */
	@RequestMapping("register")
	@ResponseBody
	public JsonResult reg(@RequestBody(required = true) Map<String,Object> formData){
		HashMap<Object,Object> map = new HashMap<>();
		HttpSession session = getSession();
		PageData pd = new PageData();
		for (Map.Entry<String, Object> entry : formData.entrySet()) {
			pd.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		String  code = (String)session.getAttribute(Const.SESSION_SECURITY_NOTE_CODE);
		if(!pd.getString("code").equalsIgnoreCase(code)){
			
			return new JsonResult(Const.HTTP_ERROR_401, "短信验证码错误！");
		}
		//储存用户信息
		Phone phone = new Phone();
		phone.setId(get32UUID());
		phone.setPassword(MD5.md5(pd.getString("password")));
		phone.setUsername(pd.getString("username"));
		phone.setPhone(pd.getString("phone"));
		phone.setCreateTime(DateUtil.getTime());
		
		try {
			Boolean flag =loginSevice.save(phone);
			if(flag){
				session.setAttribute("phoneInfo", phone);
				session.removeAttribute(Const.SESSION_SECURITY_NOTE_CODE);
			}
		} catch (Exception e) {
			logger.error(" register occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
	/**
	 * 校验信息变更
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping("checkInfoChange")
	@ResponseBody
	public JsonResult findBackPassword(String phoneNum,String username,String code){
		HttpSession session = getSession();
		//获取session中的验证码
		String sessioncode = (String) session.getAttribute(Const.SESSION_SECURITY_NOTE_CODE);
		
			Phone phoneVo = new Phone();
			PageData pd = new PageData();
			pd.put("phone", phoneNum);
			pd.put("username", username);
			try {
				phoneVo=loginSevice.findPersonageByPhone(pd);
			} catch (Exception e) {
				logger.error("select the personage datas occured error!", e);
				return new JsonResult(Const.HTTP_ERROR_402, e.toString());
			}
			if(phoneVo == null ){
				return new JsonResult(Const.HTTP_ERROR_403, "该手机号并未注册过");
			}else{
				if(code.equalsIgnoreCase(sessioncode)){
					
					return new JsonResult(Const.HTTP_OK,"ok",phoneNum);
				}else{
					
					return new JsonResult(Const.HTTP_ERROR_401, "短信验证码错误！");
				}
			}
			
	}
	
	/**
	 * 根据手机修密码
	 * @param password
	 * @param phone
	 * @return
	 */
	@RequestMapping("updatePassword")
	@ResponseBody
	public JsonResult updatepassword(String password,String phone){
		HttpSession session = getSession();
		
			PageData pd = new PageData();
			pd.put("phone", phone);
			pd.put("password",  MD5.md5(password));
			pd.put("updateTime", DateUtil.getTime());
			Boolean flag = false;
			//执行修改
			try {
				 flag=loginSevice.updatePassword(pd);
				
			} catch (Exception e) {
				logger.error("update password failure", e);
				
			}
			if(flag){
				session.setAttribute("phoneInfo", phone);
				return  new JsonResult(Const.HTTP_OK,"ok");
			}else{
				return new JsonResult(Const.HTTP_ERROR_403, "密码修改失败");
			}
			
		
	}
	
	/**
	 * 退出
	 * @return
	 */
	@RequestMapping("loginOut")
	@ResponseBody
	public JsonResult loginOut(){
		HttpSession session = getSession();
		Phone  phoneSession= (Phone) session.getAttribute("phoneVo");
		if(phoneSession == null){
			return new JsonResult(Const.HTTP_OK,"ok");
		}else{
			session.removeAttribute("phoneVo");
		}
		return  new JsonResult(Const.HTTP_OK,"ok");
	}
	
	/**
	 * 测试发送邮件
	 *//*
	@RequestMapping("sendmail")
	@ResponseBody
	public JsonResult sendemail(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("点击下面链接修改密码，48小时生效，链接只能使用一次，请尽快修改！</br>");
		buffer.append("<a href=\'%s${basePath}/jump/login.do\'>找回密码</a>");
		String content = buffer.toString();
		try {
			SimpleMailSender.sendEmail("smtp.163.com", "25" , "18813145242@163.com", "nhhq99", "1934242793@qq.com", "测试java邮件", content, "2");
		} catch (Exception e) {
			logger.error("error", e);
		}
		return new JsonResult(Const.HTTP_OK, "ok");
	}
	*/
	
	
}
