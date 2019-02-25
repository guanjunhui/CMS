package cn.cebest.controller.front.cms;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.entity.web.WebSite;
import cn.cebest.service.system.solr.SolrService;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

@Controller
@RequestMapping("/query")
public class ZxwSearchResultController {
	
	@Resource(name = "solrService")
	private SolrService solrService;
	
	@RequestMapping("tosearchZXWResult")
	public String tosearchZXWResult(String keyword,ModelMap model,HttpServletRequest request){
		if(StringUtils.isNotBlank(keyword)){
			try {
				keyword= URLDecoder.decode(keyword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			keyword=StringUtils.EMPTY;
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("isSerach", Const.YES);
		FrontUtils.frontPageData(request, model);
		return "search";
	}
	
	
	@RequestMapping("specialQuery")
	@ResponseBody
	public SearchResult query(String[] ids, String keyword, String columnId, String currentPage, String showCount) throws TemplateException{
		
		//分页数据
		Page page = new Page(Integer.valueOf(currentPage),Integer.valueOf(showCount));
	

		SearchResult list = null;
		try {
			
			list = solrService.findSolrList(keyword,columnId,ids,page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	
}
