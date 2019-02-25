package cn.cebest.controller.front.cms;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

@Controller
@RequestMapping(value="/baigeController")
public class BaigeController extends BaseController {
    
	private Logger logger = LoggerFactory.getLogger(BaigeController.class);
	
	@Autowired
	private ColumconfigService columconfigService;
	@Autowired
	private MyMessageService messageService;

	@RequestMapping(value="/selectSubColumnListByParientId")
	@ResponseBody
	public List<ColumConfig> selectSubColumnListByParientId(HttpServletRequest request, 
			String parientId) {
		WebSite site = RequestUtils.getSite(request);
		String siteId = site.getId();
		PageData pd = new PageData();
		pd.put("siteId", siteId);
		pd.put("parientId", parientId);
		List<ColumConfig> columnList = null;
		try {
			columnList = columconfigService.selectSubColumnListByParientId(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnList;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/selectProductsByColumnIds")
	@ResponseBody
	public HashMap<String, Object> selectProductsByColumnIds(HttpServletRequest request,
			String columnId, Page page) {
		WebSite site = RequestUtils.getSite(request);
		String siteId = site.getId();
		PageData pd = new PageData();
		pd.put("siteId", siteId);
		pd.put("columnId", columnId);
		page.setPd(pd);
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			resultMap = messageService.selectProductsByColumnIds(page);
			resultMap.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
		}
		return resultMap;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/selectProductsByColumnIdsTop")
	@ResponseBody
	public HashMap<String, Object> selectProductsByColumnIdsTop(HttpServletRequest request,
			String columnId, Page page) {
		WebSite site = RequestUtils.getSite(request);
		String siteId = site.getId();
		PageData pd = new PageData();
		pd.put("siteId", siteId);
		pd.put("columnId", columnId);
		page.setPd(pd);
		HashMap<String, Object> resultMap = new HashMap<>();
		try {
			resultMap = messageService.selectProductsByColumnIdsTop(page);
			resultMap.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
		}
		return resultMap;
	}
	@RequestMapping(value="/selectProductDetailByProId")
	@ResponseBody
	public NewMessage selectProductDetailByProId(HttpServletRequest request,
			String proId, String columnId) {
		WebSite site = RequestUtils.getSite(request);
		String siteId = site.getId();
		PageData pd = new PageData();
		pd.put("siteId", siteId);
		pd.put("proId", proId);
		pd.put("columnId", columnId);
		NewMessage product = null;
		try {
			product = messageService.selectProductDetailByProId(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}
	@RequestMapping(value="/selectRelProByProId")
	@ResponseBody
	public List<NewMessage> selectRelProByProId(HttpServletRequest request,
			String proId, String columnId) {
		WebSite site = RequestUtils.getSite(request);
		String siteId = site.getId();
		PageData pd = new PageData();
		pd.put("siteId", siteId);
		pd.put("proId", proId);
		pd.put("columnId", columnId);
		List<NewMessage> relProList = null;
		try {
			relProList = messageService.selectRelProByProId(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relProList;
	}
	
}
	
 