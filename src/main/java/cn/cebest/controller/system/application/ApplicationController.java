package cn.cebest.controller.system.application;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.application.Application;
import cn.cebest.service.system.application.ApplicationService;
import cn.cebest.util.DateUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelView;
import cn.cebest.util.PageData;

/**
 * @author lwt
 * @time 2018-08-30
 */
@Controller
@RequestMapping(value = "/application")
public class ApplicationController extends BaseController {

	String menuUrl = "application/list.do"; //菜单地址(权限用)
	@Resource(name = "ApplicationService")
	private ApplicationService applicationservice;

	/**修改内容
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateApplication")
	@RequiresPermissions("application:updateApplication")
	public ModelAndView updateContent(Application application) {
		ModelAndView mav=this.getModelAndView();
		try {
			applicationservice.editContent(application);
		} catch (Exception e) {
			logger.error("更新申请列表失败", e);
		}	
		mav.setViewName("redirect:list.do");
		return mav;
	}
	
	/**跳转到修改页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/toUpdate")
	@RequiresPermissions("application:toUpdate")
	public String toUpdate(Map<String,Object> map,String id) throws Exception{
		PageData pd = this.getPageData();
		pd.put("id", id);
		map.put("application", applicationservice.findContentById(pd));
		return "system/application/application_edit";
	}
	
	/**
	 * 申请列表展示
	 * @return
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("application:list")
	public ModelAndView contentlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try {
			String message_keyword = pd.getString("keyword");//模板名称检索
			if(StringUtils.isNotEmpty(message_keyword)){
				String decode= URLDecoder.decode(message_keyword, "UTF-8");
				pd.put("keyword",decode);
			}
			page.setPd(pd);
			List<Application> apps=applicationservice.contentlistPage(page);
			mv.addObject("applications", apps);
			mv.setViewName("system/application/application_list");
		} catch (Exception e) {
			logger.error("申请列表查询失败", e);
		}
		return mv;
	}
	/**导出全部申请信息到EXCEL
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("application:excel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("公司名称");			//1
			titles.add("事项");			//2
			titles.add("您所属的行业");		//3
			titles.add("希望合作的领域/项目");	//4
			titles.add("成立时间");	//5
			titles.add("官方网站");	//6
			titles.add("注册资本");	//7
			titles.add("员工总数");	//8
			titles.add("年销售额");	//9
			titles.add("年产量");	//10
			titles.add("公司地址");	//11
			titles.add("厂房面积");	//12
			titles.add("姓名");	//13
			titles.add("职务");	//14
			titles.add("联系电话");	//15
			titles.add("电子邮件");	//16
			titles.add("申请时间");	//17
			dataMap.put("titles", titles);
			List<PageData> messageList = applicationservice.listAllApplication(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<messageList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", messageList.get(i).getString("COMPANYNAME"));	//1
				vpd.put("var2", messageList.get(i).getString("ITEM"));//2
				vpd.put("var3", messageList.get(i).getString("INDUSTRY"));//3
				vpd.put("var4", messageList.get(i).getString("FIELD"));		//4
				vpd.put("var5", DateUtil.dateFormat((Date)messageList.get(i).get("ESTABLISHMENT"), "yyyy-MM-dd HH:mm:ss"));		//5
				vpd.put("var6", messageList.get(i).getString("WEBSITE"));			//6
				vpd.put("var7", messageList.get(i).getString("REGISTERCAPITAL"));	//7
				vpd.put("var8", messageList.get(i).getString("NUMBER"));		//8
				vpd.put("var9", messageList.get(i).getString("ANNUALSALES"));		//9
				vpd.put("var10", messageList.get(i).getString("ANNUALOUTPUT"));		//10
				vpd.put("var11", messageList.get(i).getString("COMPANYADDRESS"));		//11
				vpd.put("var12", messageList.get(i).getString("WORKSHOPAREA"));		//12
				vpd.put("var13", messageList.get(i).getString("FULLNAME"));		//13
				vpd.put("var14", messageList.get(i).getString("POST"));		//14
				vpd.put("var15", messageList.get(i).getString("CONTACTNUMBER"));		//15
				vpd.put("var16", messageList.get(i).getString("EMAIL"));		//16
				vpd.put("var17", DateUtil.dateFormat((Date)messageList.get(i).get("CREATETIME"), "yyyy-MM-dd HH:mm:ss"));//17
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
			mv = new ModelAndView(erv,dataMap);
			
		} catch(Exception e){
			logger.error("导出所有申请列表失败", e);
		}
		return mv;
	}
}
