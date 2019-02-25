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

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.survey.Question;
import cn.cebest.service.web.survey.QuestionManager;
import cn.cebest.service.web.survey.SurveyManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
/** 
 * 说明：用户调查问题
 * 创建人：Qiaozhipeng
 * 创建时间：2017-09-20
 */
@Controller
@RequestMapping(value="/question")
public class QuestionController extends BaseController {
	String menuUrl = "question/list.do"; //菜单地址(权限用)
	@Resource(name="questionService")
	private QuestionManager questionService;
	@Resource(name="surveyService")
	private SurveyManager surveyService;
	@RequestMapping(value="/edit")
	public ModelAndView updateQuestion() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Question");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		questionService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
		
	/**删除
		 * @param out
		  * @param QUESTION_ID
		  * @param  SURVEY_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/delete")
		public void delete(PrintWriter out) throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"删除Question");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
			PageData pd = new PageData();
			pd = this.getPageData();
			questionService.delete(pd);
			out.write("success");
			out.close();
		}
		
		/**新增問題
		 * @param SURVEY_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/save")
		public ModelAndView save() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"新增Question");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
			ModelAndView mv =this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			questionService.save(pd);
			mv.setViewName("save_result");
			mv.addObject("SURVEY_ID", pd.get("SURVEY_ID"));
			mv.addObject("msg","success");
			return mv;
		}
		
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Question");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = questionService.list(page);	//列出Survey列表
		mv.setViewName("web/survey/question_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	/**跳转到添加问题页面
	 * @param QUESTION_ID
	 * @param  SURVEY_ID
	 * @throws Exception
	 */
	@RequestMapping("/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("web/survey/question_add");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**跳转到修改页面
	 * @param QUESTION_ID
	 * @param  SURVEY_ID
	 * @throws Exception
	 */
		@RequestMapping("/goEdit")
		public ModelAndView goEdit()throws Exception{
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			Question question = questionService.findById(pd);	//根据ID读取
			mv.setViewName("web/survey/question_edit");
			mv.addObject("msg", "edit");
			mv.addObject("question", question);
			return mv;
		}
		
		
		 /**批量删除
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/deleteAll")
		@ResponseBody
		public Object deleteAll() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"批量删除Question");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				questionService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
			return AppUtil.returnObject(pd, map);
		}
		
		@InitBinder
		public void initBinder(WebDataBinder binder){
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
		}
}
