package cn.cebest.controller.system.fhlog;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelView;
import cn.cebest.util.PageData;

/** 
 * 说明：操作日志记录
 * 创建人：中企高呈
 * 创建时间：2016-05-10
 */
@Controller
@RequestMapping(value="/fhlog")
public class FHlogController extends BaseController {
	
	@Resource(name="fhlogService")
	private FHlogManager fhlogService;
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@RequiresPermissions("fhlog:delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除FHlog");
		PageData pd = new PageData();
		pd = this.getPageData();
		fhlogService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("fhlog:list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastStart = pd.getString("lastStart");	//开始时间
		String lastEnd = pd.getString("lastEnd");		//结束时间
		if(lastStart != null && !"".equals(lastStart)){
			int length = lastStart.length();
			if(length > 10){
				pd.put("lastStart", lastStart);
			}else{
				pd.put("lastStart", lastStart+" 00:00:00");
			}
		}
		if(lastEnd != null && !"".equals(lastEnd)){
			int length = lastEnd.length();
			if(length > 10){
				pd.put("lastEnd", lastEnd);
			}else{
				pd.put("lastEnd", lastEnd+" 00:00:00");
			}
		}
		page.setPd(pd);
		List<PageData>	varList = fhlogService.list(page);		//列出FHlog列表
		mv.setViewName("system/fhlog/fhlog_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		
		return mv;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	@RequiresPermissions("fhlog:deleteAll")
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除FHlog");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			fhlogService.deleteAll(ArrayDATA_IDS);
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
	@RequiresPermissions("fhlog:excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出FHlog到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户名");	//1
		titles.add("操作时间");	//2
		titles.add("事件");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = fhlogService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("USERNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("CZTIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("CONTENT"));	    //3
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
