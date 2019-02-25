package cn.cebest.controller.system.web.survey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.User;
import cn.cebest.entity.system.survey.Answer;
import cn.cebest.service.web.survey.AnswerManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.Tools;
/** 
 * 说明：用户调查答案
 * 创建人：qiaozhipeng
 * 创建时间：2017-09-20
 */
@Controller
@RequestMapping(value="/answer")
public class AnswerController extends BaseController {
	String menuUrl = "answer/list.do"; //菜单地址(权限用)
	@Resource(name="answerService")
	private AnswerManager answerService;
    /**修改
	 * @param out
	 * @throws Exception
	 */
	
	@RequestMapping(value="/edit")
	public ModelAndView updateQuestion() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改answer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		answerService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
		
	     /**删除
		 * @param out
		 * @throws Exception
		 */
		@RequestMapping(value="/delete")
		public void delete(PrintWriter out) throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"删除answer");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
			PageData pd = new PageData();
			pd = this.getPageData();
			answerService.delete(pd);
			out.write("success");
			out.close();
		}
		
		/**新增
		 * @param SURVEY_ID
		 * @throws Exception
		 */
		@RequestMapping(value="/save")
		public ModelAndView save(HttpSession session) throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"新增answer");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
			ModelAndView mv =this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			pd.put("CREAT_TIME", Tools.date2Str(new Date()));	//发表时间
			User user = (User)session.getAttribute(Const.SESSION_USER);
			pd.put("USER_ID", user.getUSER_ID());	//设置创建用户id
			pd.put("UUID", UUID.randomUUID().toString());
			answerService.save(pd);
			mv.setViewName("save_result");
			mv.addObject("pd", pd.get("pd"));
			mv.addObject("msg","success");
			return mv;
		}
		
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表answer");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = answerService.list(page);	//列出Survey列表
		mv.setViewName("web/survey/question_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
		
		 /**批量删除
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/deleteAll")
		@ResponseBody
		public Object deleteAll() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"批量删除answer");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				answerService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
			return AppUtil.returnObject(pd, map);
		}
		 /**批量插入
		 * @param List<Answer>
		 * @throws Exception
		 */
	public ModelAndView batchSave(HttpSession session) throws Exception {
		logBefore(logger, Jurisdiction.getUsername()+"批量插入answer");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "save")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();		
		pd = this.getPageData();
		String uuid = UUID.randomUUID().toString();
		String create_time = Tools.date2Str(new Date());
		User user=(User)session.getAttribute(Const.SESSION_USER);
		List<Answer> answerList = new ArrayList<Answer>();
		List<Answer> list = (List<Answer>) pd.get("answerList");
		for (int i = 0; i < list.size(); i++) {
			Answer answer = list.get(i);
			String content = answer.getContent();
			Integer question_id = answer.getQuestion_id();
			Integer survey_id = answer.getSurvey_id();
			answerList.add(new Answer(content, uuid, user.getUSER_ID(), question_id, create_time, survey_id));
		}
		answerService.batchSave(answerList);
		mv.setViewName("save_result");
		mv.addObject("pd", pd.get("pd"));
		mv.addObject("msg","success");
		return mv;
	}
}
