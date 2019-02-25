package cn.cebest.controller.system.employ;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Employ;
import cn.cebest.entity.system.EmployField;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.system.txt.TxtService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.UuidUtil;

/**
 * @author qichangxin 招聘管理
 */
@Controller
@RequestMapping(value = "/employ")
public class EmployController extends BaseController {

	String menuUrl = "recruit/list.do"; //菜单地址(权限用)
	@Resource(name = "contentService")
	private ContentService contentService;
	@Autowired
	private EmployService employService;
	
	@Autowired
	private TxtService txtService;
	
	@Autowired
	private FHlogManager FHLOG;
	
	@Autowired
	private SeoService service;
	/**
	 * 招聘列表
	 * @return
	 */
	@RequiresPermissions("employ:list")
	@RequestMapping(value="/list")
	public ModelAndView recruitlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String keywords = pd.getString("keywords");		//招聘标题检索条件
		String siteId = RequestUtils.getSiteId(this.getRequest());
		pd.put("SITEID", siteId);
		page.setPd(pd);
		try {
			if(null != keywords && !"".equals(keywords)){
				//pd.put("keywords", new String(keywords.trim().getBytes("ISO-8859-1"),"UTF-8"));
				pd.put("keywords", URLDecoder.decode(keywords, "UTF-8"));
			}
			List<PageData> recruitList = employService.listPage(page);	//列出招聘列表
			mv.setViewName("system/employ/employ_list");
			mv.addObject("list", recruitList);
			mv.addObject("type", employService.findColum(pd));
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error("get employ List page occured error!",e);
		}
		return mv;
	}

	/**
	 * 筛选所属栏目
	 * @author  liu
	 */
	@RequestMapping(value="/employByColumnIdlistPage")
	//@RequiresPermissions("employ:employByColumnIdlistPage")
	public ModelAndView employByColumnIdlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			List<PageData> recruitList = employService.selectlistPageByColumIDD(page);	//列出内容详情列表
			mv.setViewName("system/employ/employ_list");
			mv.addObject("list", recruitList);
			mv.addObject("type", employService.findColum(pd));
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 删除招聘
	 * @return
	 */
	@RequiresPermissions("employ:delete")
	@RequestMapping("/del")
	@ResponseBody
	public JsonResult del(){
		logBefore(logger, Jurisdiction.getUsername()+"删除招聘");
		PageData pd = this.getPageData();
		String ids=pd.getString("ID");
		String[] idArry=StringUtils.split(ids, Const.SPLIT_COLON);
		try {
			employService.del(new PageData("ID",idArry[0]));
			FHLOG.save(Jurisdiction.getUsername(), "删除招聘："+pd);
		} catch (Exception e) {
			logger.error("del employ info occured error!id["+pd.getString("ID")+"]",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		if(idArry.length>=Const.INT_2){
			try {
				txtService.del(idArry[1]);
			} catch (Exception e) {
				logger.error("del employ's txtBlob occured error!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 批量删除招聘
	 */
	@RequestMapping(value="/deleteAll")
	public String delAllRecruit() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除招聘");
		PageData pd = this.getPageData();
		String ids = pd.getString("ids");
		if(ids.endsWith(Const.SPLIT_CHAR)){
			ids=ids.substring(0, ids.length()-1);
		}
		List<String> emplyIdList=new ArrayList<String>();
		List<String> txtIdList=new ArrayList<String>();
		String[] idArry=StringUtils.split(ids, Const.SPLIT_CHAR);
		for(String str:idArry){
			String[] strs=StringUtils.split(str, Const.SPLIT_COLON);
			if(strs.length>=Const.INT_2){
				emplyIdList.add(strs[0]);
				txtIdList.add(strs[1]);
			}else{
				emplyIdList.add(strs[0]);
			}
		}
		
		try {
			FHLOG.save(Jurisdiction.getUsername(), "批量删除招聘");
			employService.delAll(emplyIdList.toArray(new String[emplyIdList.size()]));
		} catch (Exception e) {
			logger.error("batch del employs occured error!",e);
		}
		try {
			txtService.delAll(txtIdList.toArray(new String[txtIdList.size()]));
		} catch (Exception e) {
			logger.error("batch del employ's txtBlob occured error!",e);
		}
		return "redirect:"+Const.ADMIN_PREFIX+"employ/list.do";
	}
	
	/**去新增招聘页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("employ:add")
	@RequestMapping(value="/goAdd")
	public ModelAndView goAddRecruit(@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.addObject("columId", pd.getString("columId"));
		Page page=new Page();
		List<EmployField> fields=null;
		Map employFields=null;
		try {
			
			fields=employService.findlistEmployFields(page);
			employFields=this.resetEmployFields(fields);
			
			if(pd.getString("ID")!=null && !"".equals(pd.getString("ID"))){
				pd=employService.findById(pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("system/employ/employ_edit");
		mv.addObject("pd", pd);
		mv.addObject("columId", columId);
		mv.addObject("topColumId", topColumId);
		mv.addObject("employFields", employFields);
		return mv;
	}
	
	/**保存招聘
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView saveRecruit(int ifAppendSave,Employ employ,@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId){
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"新增招聘");
		PageData pd = this.getPageData();
		ModelAndView mod=this.getModelAndView();
		String siteId = RequestUtils.getSiteId(this.getRequest());
		pd.put("SITE_ID", siteId);
		
		//岗位职责
		String txtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_DESCRIPTION"))){
			txtId=pd.getString("JOB_DESCRIPTION");
		}else{
			txtId=this.get32UUID();
		}
		String txtValue=pd.getString("JOB_DESCRIPTION_CONTENT");
		PageData txtData=new PageData();
		txtData.put("CONTENT_ID", txtId);
		txtData.put("TXT", txtValue);
		txtData.put("TXT_EXT", "");
		//任职要求
		String requireTxtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_REQUIRE"))){
			requireTxtId=pd.getString("JOB_REQUIRE");
		}else{
			requireTxtId=this.get32UUID();
		}
		String requireTxtValue=pd.getString("JOB_REQUIRE_CONTENT");
		PageData requireTxtData=new PageData();
		requireTxtData.put("CONTENT_ID", requireTxtId);
		requireTxtData.put("TXT", requireTxtValue);
		requireTxtData.put("TXT_EXT", "");
		//联系方式
		String contactTxtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_CONTACT"))){
			contactTxtId=pd.getString("JOB_CONTACT");
		}else{
			contactTxtId=this.get32UUID();
		}
		String contactTxtValue=pd.getString("JOB_CONTACT_CONTENT");
		PageData contactTxtData=new PageData();
		contactTxtData.put("CONTENT_ID", contactTxtId);
		contactTxtData.put("TXT", contactTxtValue);
		contactTxtData.put("TXT_EXT", "");
		
		try {
			if(StringUtils.isNotBlank(pd.getString("JOB_DESCRIPTION"))){
				txtService.update(txtData);
			}else{
				txtService.save(txtData);
			}
			
			if(StringUtils.isNotBlank(pd.getString("JOB_REQUIRE"))){
				txtService.update(requireTxtData);
			}else{
				txtService.save(requireTxtData);
			}
			
			if(StringUtils.isNotBlank(pd.getString("JOB_CONTACT"))){
				txtService.update(contactTxtData);
			}else{
				txtService.save(contactTxtData);
			}

		} catch (Exception e) {
			logger.error("save employ job description occured error!",e);
			return mod.addObject("result", new JsonResult(500, e.getMessage()));
		}

		try {
			String recruitPeoplenum=pd.getString("RECRUIT_PEOPLENUM");
			if(StringUtils.isBlank(recruitPeoplenum)){
				pd.put("RECRUIT_PEOPLENUM", "0");
			}
			if(StringUtils.isBlank(pd.getString("START_TIME"))){
				pd.put("START_TIME", null);
			}
			if(StringUtils.isBlank(pd.getString("END_TIME"))){
				pd.put("END_TIME", null);
			}
			if(StringUtils.isBlank(pd.getString("IFRECOMMEND"))){
				pd.put("IFRECOMMEND", Const.NO);
			}
			if(StringUtils.isBlank(pd.getString("IFTOP"))){
				pd.put("IFTOP", Const.NO);
			}
			if(!Const.YES.equals(pd.getString("IFALWAYS"))){
				pd.put("IFALWAYS", Const.NO);
			}
			pd.put("JOB_DESSIMPLE", pd.getString("JOB_DESSIMPLE"));
			pd.put("JOB_DESCRIPTION", txtId);
			pd.put("JOB_REQUIRE", requireTxtId);
			pd.put("JOB_CONTACT", contactTxtId);
			if(StringUtils.isBlank(pd.getString("ID"))){
				pd.put("ID", this.get32UUID());
				String dayTime = DateUtil.getDay();
				pd.put("RELEASE_TIME", dayTime);
				pd.put("UPDATE_TIME", dayTime);
				
				List<ExtendFiledUtil> fileds=employ.getFileds();
				if(fileds!=null&&fileds.size()>0){
					Collections.sort(fileds, new Comparator<ExtendFiledUtil>(){
						@Override
						public int compare(ExtendFiledUtil o1, ExtendFiledUtil o2) {
							
							return (o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue())-(o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue());
						}
					});
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(fileds);
					pd.put("FILEDJSON", json);
				}
				employService.save(pd);
				// 保存seo
				pd.put("MASTER_ID", pd.getString("ID"));
				pd.put("SEO_TITLE", pd.getString("seo_title"));
				pd.put("SEO_KEYWORDS", pd.getString("seo_keywords"));
				pd.put("SEO_DESCRIPTION", pd.getString("seo_description"));
				pd.put("ID", UuidUtil.get32UUID());
				pd.put("CREATE_TIME", DateUtil.getTime());
				service.insetSeo(pd);
			}else{
				pd.put("UPDATE_TIME", DateUtil.getTime());
				List<ExtendFiledUtil> fileds=employ.getFileds();
				if(fileds!=null&&fileds.size()>0){
					Collections.sort(fileds, new Comparator<ExtendFiledUtil>(){
						@Override
						public int compare(ExtendFiledUtil o1, ExtendFiledUtil o2) {
							
							return (o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue())-(o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue());
						}
					});
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(fileds);
					pd.put("FILEDJSON", json);
				}
				// 保存seo
				pd.put("SEO_TITLE", pd.getString("seo_title"));
				pd.put("SEO_KEYWORDS", pd.getString("seo_keywords"));
				pd.put("MASTER_ID", pd.getString("ID"));
				pd.put("SEO_DESCRIPTION", pd.getString("seo_description"));
				service.updateSeo(pd);
				employService.edit(pd);
			}
			FHLOG.save(Jurisdiction.getUsername(), "新增招聘：");
		} catch (Exception e) {
			logger.error("save employ occured error!",e);
			return mod.addObject("result", new JsonResult(500, e.getMessage()));
		}
		if(ifAppendSave==1){
			mod.setViewName("redirect:goAdd.do?columId="+columId+"&topColumId="+topColumId);
			return mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功,继续添加!"));
		}
		mod.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功!"));
	}
	/*
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult saveRecruit(){
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"新增招聘");
		PageData pd = this.getPageData();
		
		String siteId = RequestUtils.getSiteId(this.getRequest());
		pd.put("SITE_ID", siteId);
		
		//岗位职责
		String txtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_DESCRIPTION"))){
			txtId=pd.getString("JOB_DESCRIPTION");
		}else{
			txtId=this.get32UUID();
		}
		String txtValue=pd.getString("JOB_DESCRIPTION_CONTENT");
		PageData txtData=new PageData();
		txtData.put("CONTENT_ID", txtId);
		txtData.put("TXT", txtValue);
		txtData.put("TXT_EXT", "");
		//任职要求
		String requireTxtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_REQUIRE"))){
			requireTxtId=pd.getString("JOB_REQUIRE");
		}else{
			requireTxtId=this.get32UUID();
		}
		String requireTxtValue=pd.getString("JOB_REQUIRE_CONTENT");
		PageData requireTxtData=new PageData();
		requireTxtData.put("CONTENT_ID", requireTxtId);
		requireTxtData.put("TXT", requireTxtValue);
		requireTxtData.put("TXT_EXT", "");
		//联系方式
		String contactTxtId=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(pd.getString("JOB_CONTACT"))){
			contactTxtId=pd.getString("JOB_CONTACT");
		}else{
			contactTxtId=this.get32UUID();
		}
		String contactTxtValue=pd.getString("JOB_CONTACT_CONTENT");
		PageData contactTxtData=new PageData();
		contactTxtData.put("CONTENT_ID", contactTxtId);
		contactTxtData.put("TXT", contactTxtValue);
		contactTxtData.put("TXT_EXT", "");
		
		try {
			if(StringUtils.isNotBlank(pd.getString("JOB_DESCRIPTION"))){
				txtService.update(txtData);
			}else{
				txtService.save(txtData);
			}
			
			if(StringUtils.isNotBlank(pd.getString("JOB_REQUIRE"))){
				txtService.update(requireTxtData);
			}else{
				txtService.save(requireTxtData);
			}
			
			if(StringUtils.isNotBlank(pd.getString("JOB_CONTACT"))){
				txtService.update(contactTxtData);
			}else{
				txtService.save(contactTxtData);
			}

		} catch (Exception e) {
			logger.error("save employ job description occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}

		try {
			String recruitPeoplenum=pd.getString("RECRUIT_PEOPLENUM");
			if(StringUtils.isBlank(recruitPeoplenum)){
				pd.put("RECRUIT_PEOPLENUM", "0");
			}
			if(StringUtils.isBlank(pd.getString("START_TIME"))){
				pd.put("START_TIME", null);
			}
			if(StringUtils.isBlank(pd.getString("END_TIME"))){
				pd.put("END_TIME", null);
			}
			if(StringUtils.isBlank(pd.getString("IFRECOMMEND"))){
				pd.put("IFRECOMMEND", Const.NO);
			}
			if(StringUtils.isBlank(pd.getString("IFTOP"))){
				pd.put("IFTOP", Const.NO);
			}
			if(!Const.YES.equals(pd.getString("IFALWAYS"))){
				pd.put("IFALWAYS", Const.NO);
			}
			pd.put("JOB_DESCRIPTION", txtId);
			pd.put("JOB_REQUIRE", requireTxtId);
			pd.put("JOB_CONTACT", contactTxtId);
			if(StringUtils.
					isBlank(pd.getString("ID"))){
				pd.put("ID", this.get32UUID());
				pd.put("RELEASE_TIME", DateUtil.getDay());
				employService.save(pd);
			}else{
				pd.put("UPDATE_TIME", DateUtil.getTime());
				employService.edit(pd);
			}
			FHLOG.save(Jurisdiction.getUsername(), "新增招聘：");
		} catch (Exception e) {
			logger.error("save employ occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}*/
	
	/**
	 * 置顶
	 * @return
	 */
	@RequiresPermissions("employ:top")
	@RequestMapping("/top")
	@ResponseBody
	public JsonResult top(){
		PageData pd = new PageData();
		pd = this.getPageData();
		String status=pd.getString("IFTOP");
		if(Const.YES.equals(status)){
			pd.put("IFTOP", Const.NO);
		}else if(Const.NO.equals(status)){
			pd.put("IFTOP", Const.YES);
			pd.put("TOP_TIME", DateUtil.getTime());
		}
		try {
			employService.top(pd);
		} catch (Exception e) {
			logger.error(" top employ occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
	/**
	 * 是否推荐
	 * @return
	 */
	@RequiresPermissions("employ:recommend")
	@RequestMapping("/recommend")
	@ResponseBody
	public Object recommendRecruit(){
		PageData pd = new PageData();
		pd = this.getPageData();
		String status=pd.getString("IFRECOMMEND");
		try {
			if(Const.YES.equals(status)){
				pd.put("IFRECOMMEND", Const.NO);
	    	}else if(Const.NO.equals(status)){
				pd.put("IFRECOMMEND", Const.YES);
				pd.put("RECOMMEND_TIME", DateUtil.getTime());
	    	}
			employService.recommend(pd);
		} catch (Exception e) {
			logger.error(" recommend employ occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	private Map<String,String> resetEmployFields(List<EmployField> list){
		Map employFieldsMap = new HashMap();
		for (EmployField field : list) {
			employFieldsMap.put(field.getKeyword(), field.getDisplay_name());
		}
		return employFieldsMap;
	}
	
	
}
