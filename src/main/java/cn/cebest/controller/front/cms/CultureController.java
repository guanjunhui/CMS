package cn.cebest.controller.front.cms;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;


@Controller
@RequestMapping(value="/culture")
public class CultureController extends BaseController {
    
	private Logger logger = LoggerFactory.getLogger(CultureController.class);

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value = "/cultureActivityList")
	@ResponseBody
	public List<Content> cultureActivityList(Page page, String currentId, int currentPage, int showCount) {
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("colum_id", currentId);
		
		page.setCurrentPage(currentPage);
		page.setShowCount(showCount);
		page.setPd(pd);	
		List<Content> list = null;
		try {
			int count = contentService.selectlistCountByColumID(page);
			int totalPage = 0;
			if(count%showCount!=0) {
				totalPage = count/showCount + 1;
			} else {
				totalPage = count/showCount;
			}
			if(currentPage <= totalPage) {
				list = contentService.selectlistPageByColumID(page);
			} 
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		return list;	
	}
	
	
}
	
 