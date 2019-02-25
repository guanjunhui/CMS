package cn.cebest.controller.front.infineon.ipc;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.cebest.util.SystemConfig;

/**
 *
 * @author wangweijie
 * @Date 2018年11月29日
 * @company 中企高呈
 */
@Controller
@RequestMapping("front")
public class CasCallbackController {
	/**
	 * 单点登出
	 * @param service
	 * @param request
	 * @return
	 */
	@RequestMapping("logout")
	public String logout(@RequestParam(name="service",required=true)String service,
						HttpServletRequest request){
		String logoutUrl = SystemConfig.getPropertiesString("web.cas_logout_url");
		request.getSession().removeAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		String url = "redirect:" + logoutUrl + "?service=" + service;
		return  url;
	}
}
