package cn.cebest.controller.system.web.survey;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.entity.system.survey.Survey;
import cn.cebest.service.web.survey.SurveyManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelView;
import cn.cebest.util.PageData;
import cn.cebest.util.Tools;
/** 
 * 说明：用户调查
 * 创建人：qiaozhipeng
 * 创建时间：2017-09-20
 */
@Controller
@RequestMapping(value="/survey")
public class SurveyController extends BaseController {
	String menuUrl = "survey/list.do"; //菜单地址(权限用)
	@Resource(name="surveyService")
	private SurveyManager surveyService;
	/**新增调查
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView saveSurvey(Survey survey,HttpSession session) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("CREAT_TIME", Tools.date2Str(new Date()));	//发表时间
		User user = (User)session.getAttribute(Const.SESSION_USER);
		pd.put("USER_ID", user.getUSER_ID());	//设置创建用户id
		try {
			surveyService.save(pd);
			mv.addObject("msg","success");
			mv.setViewName("save_result");
		} catch (Exception e) {
			mv.addObject("msg","failed");
			e.printStackTrace();
		}
		return mv;
	}
	
	/**级联删除问卷调查
	 * @param out
	 * @param SURVEY_ID  问卷调查id
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			surveyService.delete(pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			surveyService.edit(pd);
			mv.addObject("msg","success");
			mv.setViewName("save_result");
		} catch (Exception e) {
			mv.addObject("msg","failed");
			e.printStackTrace();
		}
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Survey");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String startTime = pd.getString("startTime");	//开始时间
		String endTime = pd.getString("endTime");		//结束时间
		if(startTime != null && !"".equals(startTime)){
			pd.put("startTime", startTime+" 00:00:00");
		}
		if(endTime != null && !"".equals(endTime)){
			pd.put("endTime", endTime+" 23:59:59");
		} 
		page.setPd(pd);
		List<PageData>	varList = surveyService.list(page);	//列出Survey列表
		mv.setViewName("web/survey/survey_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("web/survey/survey_add");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = surveyService.findById(pd);	//根据ID读取
		mv.setViewName("web/survey/survey_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	/**去设计问卷调查页面
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/goDesign")
	public ModelAndView goDesign()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		Survey survey= surveyService.getSurveyDeeply(pd);	//根据ID读取
		mv.addObject("survey",survey);
		mv.setViewName("web/survey/survey_design");
		mv.addObject("msg", "design");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**去问卷审核(查看问卷结果)页面
	 * @param pd
	 * @throws Exception
	 */
	@RequestMapping(value="/goCheck")
	public ModelAndView goCheck()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String startTime = pd.getString("startTime");	//开始时间
		String endTime = pd.getString("endTime");		//结束时间
		if(startTime != null && !"".equals(startTime)){
			pd.put("startTime", startTime+" 00:00:00");
		}
		if(endTime != null && !"".equals(endTime)){
			pd.put("endTime", endTime+" 23:59:59");
		} 
		Survey survey= surveyService.getSurveyAndAnswer(pd);	//根据ID读取
		mv.addObject("survey",survey);
		mv.setViewName("web/survey/survey_check");
		mv.addObject("msg", "design");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**预留接口--审核问卷
	 * @throws Exception
	 */
	@RequestMapping(value="/check")
	public void check(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"审核Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			surveyService.check(pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**预留接口--批量审核问卷
	 * @throws Exception
	 */
	@RequestMapping(value="/checkAll")
	@ResponseBody
	public Object checkAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量审核Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			surveyService.checkAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Survey");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			surveyService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Survey到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("调查编号");	//1
		titles.add("调查名");	//2
		titles.add("创建时间");	//3
		titles.add("状态");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = surveyService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("SURVEY_ID"));	    //1
			vpd.put("var2", varOList.get(i).getString("SURVEY_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("CREAT_TIME"));	    //3
			vpd.put("var4", varOList.get(i).getString("STATUS"));	    //4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
