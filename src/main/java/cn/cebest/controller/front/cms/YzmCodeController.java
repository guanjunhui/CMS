package cn.cebest.controller.front.cms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.util.Const;
import cn.cebest.util.CreateYZMCodeUtils;

/**@author qichangxin
 * 站点入口
*/
@Controller
@RequestMapping("/codefront")
public class YzmCodeController extends BaseController{

	@RequestMapping("/getCodeImg")
	public void generate(HttpServletResponse response){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
        
		// 获取创建验证码工具类实例  
        CreateYZMCodeUtils yzm = CreateYZMCodeUtils.getInstance();
		String code = yzm.getCreateYZMImg(output);
		HttpSession session = getSession();
		session.setAttribute(Const.SESSION_SECURITY_FRONT_CODE, code);
		try {
			ServletOutputStream out = response.getOutputStream();
			output.writeTo(out);
			out.close();
		} catch (IOException e) {
			logger.error("reponse write the yan zheng ma IO occured error!", e);
		}
	}
	
	@RequestMapping("/getCode")
	@ResponseBody
	public String getCode(){
		//获取创建验证码工具类实例
		CreateYZMCodeUtils yzm = CreateYZMCodeUtils.getInstance();
		String code = yzm.getCreateYZMCode();
		return code;
	}
	
	

}
