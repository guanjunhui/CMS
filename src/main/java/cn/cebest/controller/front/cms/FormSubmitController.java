package cn.cebest.controller.front.cms;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.FormRecordBo;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 * @Description: 爱依瑞斯静态页面表单提交入口
 * @author: GuanJunHui
 * @date:   2018年7月30日 下午3:43:28    
 * @version V1.0 
 * @Copyright: 2018   
 *
 */
@Controller
@RequestMapping("/form")
public class FormSubmitController extends BaseController{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	private CustomFormAttributeService customFormAttributeService;
	
	@ResponseBody
	@RequestMapping("/joinForm")
	public String joinForm(@RequestParam(value = "name") String name,@RequestParam(value = "sex") String sex,@RequestParam(value = "phone") String phone,
			@RequestParam(value = "investment") String investment,@RequestParam(value = "email") String email,
			@RequestParam(value = "area") String area,@RequestParam(value = "bz") String bz,ServletRequest request, ServletResponse response) throws Exception {
		//解决跨域问题
		HttpServletResponse httpServletResponse =(HttpServletResponse)response;
		// 指定允许其他域名访问 
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		// 响应类型 
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST");
		// 响应头设置 
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		Map<String, String> map = new HashMap<String,String>();
		map.put("name", name);
		map.put("sex", sex);
		map.put("phone", phone);
		map.put("investment", investment);
		map.put("email", email);
		map.put("area", area);
		map.put("bz", bz);
		dao.save("FormMapper.joinForm", map);
		return "OK";
	}
	
	@ResponseBody
	@RequestMapping("/appointmentForm")
	public String AppointmentForm(@RequestParam(value = "phone") String phone,@RequestParam(value = "region") String region,
			@RequestParam(value = "area") String area,ServletRequest request, ServletResponse response) throws Exception {
		//解决跨域问题
		HttpServletResponse httpServletResponse =(HttpServletResponse)response;
		// 指定允许其他域名访问 
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		// 响应类型 
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST");
		// 响应头设置 
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		Map<String, String> map = new HashMap<String,String>();
		map.put("area", area);
		map.put("region", region);
		map.put("phone", phone);
		dao.save("FormMapper.appointmentForm", map);
		return "OK";
	}
	
	@ResponseBody
	@RequestMapping(value="/list")
	public Object golist(Page page) throws UnsupportedEncodingException{
		PageData pd = this.getPageData();
		page.setPd(pd);
		CustomFormVo customFormVo = new CustomFormVo();
		try {
			customFormVo = customFormAttributeService.getAttributeAndValuePageByFormID(page);
		} catch (Exception e) {
			logger.error("get the customform attr and data list failed!",e);
		}
		JsonResult jsonResult=new JsonResult(200, "ok",customFormVo);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	public static void main(String[] args) {
		String temp = "15312341234";
		System.out.println(temp.matches(".*1.*"));
	}
	
}
