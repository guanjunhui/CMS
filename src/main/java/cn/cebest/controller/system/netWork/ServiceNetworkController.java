package cn.cebest.controller.system.netWork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Recommend;
import cn.cebest.entity.system.ServiceNetwork.Agency;
import cn.cebest.entity.system.ServiceNetwork.ServiceNetwork;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.serviceNetwork.ServiceNetworkService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

/**
 * 代理商维护
 */
@Controller
@RequestMapping(value = "/serviceNetwork")
public class ServiceNetworkController extends BaseController {

	String menuUrl = "serviceNetwork/serviceNetworkListPage.do"; //菜单地址(权限用)
	
	/*@Resource(name = "agencyService")
	private AgencyService agencyService;*/
	
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	
	@Resource(name="serviceNetworkService")
	private ServiceNetworkService serviceNetworkService;

	/**
	 * 跳转到地图测试页面（移动）
	 * zengxiangpeng
	 */
	@RequestMapping(value = "/toMapMoveDemo")
	public ModelAndView toMapMoveDemo() {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/serviceNetwork/mapMoveDemo");
		return mv;
	}
	/**
	 * 跳转到地图测试页面
	 * zengxiangpeng
	 */
	@RequestMapping(value = "/toMapDemo")
	public ModelAndView toMapDemo() {
			ModelAndView mv = this.getModelAndView();
			mv.setViewName("system/serviceNetwork/mapDemo");
			return mv;
	}
	
	/**
	 * 获取服务商性质
	 * zengxiangpeng
	 */
	@RequestMapping(value = "/selectServiceNature")
	@ResponseBody
	public List<ServiceNetwork> selectServiceNature() {
		List<ServiceNetwork> serviceList=null;
		try {
			serviceList = serviceNetworkService.selectServiceNature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceList;
	}
	/**
	 * 根据大洲ID获取所有国家
	 * zengxiangpeng
	 */
	@RequestMapping(value = "/selectCountry")
	@ResponseBody
	public List<Agency> selectCountry(Agency agency) {
		List<Agency> agencyList=null;
		try {
			agencyList = serviceNetworkService.selectCountry(agency);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agencyList;
	}
	/**
	 * 服务网点显示列表
	 * zengxiangpeng
	 */
	@RequestMapping(value="/serviceNetworkListPage")
	@RequiresPermissions("serviceNetwork:serviceNetworkListPage")
	public ModelAndView agencyListPage(Page page, HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
			String keywords = pd.getString("keywords");//内容详情标题检索条件
			if(keywords!=null && !"".equals(keywords)){
				keywords.trim();
				pd.put("keywords", keywords);
			}
			if(page.getCurrentPageOrgi()==0){
				String currentPage =RequestUtils.getCookieValue(request, "SNcurrentPage");
				if(currentPage!=null&&!"-0".equals(currentPage)){
					page.setCurrentPage(Integer.parseInt(currentPage));
					RequestUtils.setCookie(request, response, "SNcurrentPage", "-0",null,false);
				}
			}
			page.setPd(pd);
				List<ServiceNetwork> serviceNetworkList = serviceNetworkService.serviceNetworkListPage(page);
				//获取五大洲
				/*List<Agency> regionList = agencyService.selectContinent();	*/
				mv.setViewName("system/serviceNetwork/serviceNetwork_list");
				mv.addObject("serviceNetworkList", serviceNetworkList);      
				/*mv.addObject("region", regionList);*/
				mv.addObject("pd", pd);
				mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 跳转到添加服务网点页面
	 * zengxiangpeng
	 */
	@RequestMapping(value = "/toAddServiceNetwork")
	@RequiresPermissions("serviceNetwork:toAdd")
	public ModelAndView toAddAgency() {
			ModelAndView mv = this.getModelAndView();
			mv.setViewName("system/serviceNetwork/serviceNetwork_add");
			return mv;
	}
	/**
	 * 添加服务网点
	 * zengxiangpeng
	 */
	@RequestMapping(value="/saveServiceNetwork")
	public ModelAndView saveServiceNetwork(ServiceNetwork serviceNetwork,int is_add) {
		logBefore(logger, Jurisdiction.getUsername()+"新增服务网点");
		serviceNetwork.setId(this.get32UUID());
		String time=DateUtil.getTime();
		serviceNetwork.setCreatedTime(time);
		serviceNetwork.setReleaseTime(time);
		serviceNetwork.setUpdateTime(DateUtil.getTime());
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		serviceNetwork.setSiteId(siteId);
		ModelAndView mav=this.getModelAndView();
		try {
			serviceNetworkService.saveServiceNetwork(serviceNetwork);
			mav.addObject("result",new JsonResult(200,"添加成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(500,"添加失败"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAddServiceNetwork.do");
			return mav;
		}
		mav.setViewName("redirect:serviceNetworkListPage.do");
		return mav;
	}
	/**
	 * 跳转到编辑服务网点页面
	 * zengxiangpeng
	 */
	@RequestMapping(value="/toUpdateServiceNetwork")
	public String toUpdateServiceNetwork(Map<String,Object> map,String id) throws Exception{
		map.put("serviceNetwork", serviceNetworkService.selectServiceNetworkById(id));
		return "system/serviceNetwork/serviceNetwork_edit";
	}
	/**
	 * 修改服务网点信息
	 * zengxiangpeng
	 */
	@RequestMapping(value="/updateServiceNetwork")
	public ModelAndView updateAgency(ServiceNetwork serviceNetwork,int is_add) {
		logBefore(logger, Jurisdiction.getUsername()+"修改服务网点");
		serviceNetwork.setUpdateTime(DateUtil.getTime());
		String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
		serviceNetwork.setSiteId(siteId);
		ModelAndView mav=this.getModelAndView();
		try {
			serviceNetworkService.updateServiceNetwork(serviceNetwork);
			mav.addObject("result",new JsonResult(200,"修改成功"));
		} catch (Exception e) {
			mav.addObject("result",new JsonResult(500,"修改失败"));
			e.printStackTrace();
		}
		mav.setViewName("redirect:serviceNetworkListPage.do");
		return mav;
	}
	/**
	 * 批量删除代理商
	 * zengxiangpeng
	 */
	@RequestMapping(value="/delAllServiceNetwork")
	@ResponseBody
	public Map<String,Object> delAllServiceNetwork(String[] id) {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除content");
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			serviceNetworkService.delAllServiceNetwork(id);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
}
