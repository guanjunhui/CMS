package cn.cebest.controller.front.hanNeng;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.util.PageData;

@Controller
@RequestMapping("/history")
public class HistoryController  extends BaseController{

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/getHistory")
	@ResponseBody
	public Content getHistory(String currentId){
		Content findTxt = null;
		try {
			findTxt = contentService.findContentById(currentId);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
		
		return findTxt;
	}
	
	
	@RequestMapping("/getHistoryList")
	@ResponseBody
	public	List<Content>  getHistoryList(String columId){
		int currentPage = 1;
		int showCount = 20;
		Page page = new Page(currentPage, showCount);

		PageData pd = new PageData();
		pd.put("colum_id", columId);
		page.setPd(pd);
		List<Content> contentList =  null;
		try {
			contentList = contentService.ContentListOrderByCreateTime(page);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
		
		return contentList;
	}
}
