package cn.cebest.controller.front.cms;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.bo.MesageBo;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RandomUtils;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SendMessage;


/**@author qichangxin
  * 会员注册
 */
@Controller
@RequestMapping(value="/register")
public class MemberRegisterController extends BaseController {
    
	private Logger logger = LoggerFactory.getLogger(MemberRegisterController.class);
	private static final int NUM_LENGTH=6;
	
	@Autowired
	private AppuserManager appuserManager;
	/**
	 * 下发短信验证码
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendmessage", method = RequestMethod.GET)
	public void getMessageCode(HttpServletRequest request,HttpServletResponse response) {

		MesageBo mesageBo=new MesageBo();
		PageData pd = new PageData();
		pd = this.getPageData();
		//手机号
		mesageBo.setPhoneNumbers(pd.getString("PHONE"));
		//验证码
		JSONObject json=new JSONObject();
		String code=RandomUtils.generateNumber(NUM_LENGTH);
		json.put("code", code);
		mesageBo.setTemplateParam(json.toJSONString());
		//业务ID
		mesageBo.setOutId(this.get32UUID());
		try {
			Integer templateType = null;
			try {
				SendMessage.sendMessage(mesageBo,templateType);
			} catch (ClientException e) {
				e.printStackTrace();
			}
//			EHcacheUtils.set("messageCode", code);
		} catch (InterruptedException e) {
			logger.error("send the message code occurred error!", e);
		}
	}

	
	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/registermember")
	public JsonResult registermember(HttpServletRequest request,HttpServletResponse response) {
		PageData pd = new PageData();
		pd = this.getPageData();
		//判断手机号是否注册
		try {
			PageData pageDataPhone=this.appuserManager.findByPhone(pd);
			if(pageDataPhone!=null){
				return jsonResult(Const.HTTP_ERROR, "该手机号已被注册","-2");
			}
		} catch (Exception e) {
			logger.error("find the user by the phone in database occurred error!", e);
		}
		//判断邮箱是否注册
		try {
			PageData pageDataEmail=this.appuserManager.findByEmail(pd);
			if(pageDataEmail!=null){
				return jsonResult(Const.HTTP_ERROR, "该邮箱已被注册","-3");
			}
		} catch (Exception e) {
			logger.error("find the user by the email in database occurred error!", e);
		}
		
		//判断验证码是否正确
		String mesageCode=pd.getString("messageCode");
//		String mesageCodeCache=EHcacheUtils.getString("messageCode");
//		if(!mesageCode.equalsIgnoreCase(mesageCodeCache)){
//			return jsonResult(Const.HTTP_ERROR, "验证码错误","-1");
//		}

		pd.put("USER_ID", this.get32UUID());//ID 主键
		pd.put("BZ", "注册用户");//备注
		pd.put("LAST_LOGIN", "");//最后登录时间
		pd.put("IP", RequestUtils.getRemoteAddr(request));//IP
		pd.put("STATUS", "1");//状态
		pd.put("SKIN", "default");
		pd.put("RIGHTS", "");
		String salt=pd.getString("PHONE")+new SecureRandomNumberGenerator().nextBytes().toHex();
		pd.put("SALT", salt);	
		pd.put("PASSWORD", new SimpleHash("MD5",  pd.getString("PASSWORD"),salt,2).toString());	//密码加密
		try {
			this.appuserManager.saveU(pd);
		} catch (Exception e) {
			logger.error("register the member occurred error!", e);
		}
		return jsonResult(Const.HTTP_OK, "用户注册成功!","1");
	}

	
}
	
 