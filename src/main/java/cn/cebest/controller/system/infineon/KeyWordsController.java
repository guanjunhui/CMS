package cn.cebest.controller.system.infineon;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.KeyWordsService;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value="keyWords")
public class KeyWordsController extends BaseController {

	String menuUrl = "keyWords/list.do"; //菜单地址(权限用)
	
	@Autowired
	private KeyWordsService keyWordsService;
	
	@RequestMapping("list")
	public ModelAndView list(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		Map<String, Object> data = keyWordsService.getkeyWordsList(page);
		mv.setViewName("system/infineonpmm/keyWordsList");
		mv.addObject("list", data.get("data"));
		mv.addObject("page", data.get("page"));
		return mv;
	}
	
	@RequestMapping("getKeyWords")
	public ModelAndView getKeyWords(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		Map<String, Object> data = keyWordsService.getKeyWords(pd);
		mv.setViewName("system/infineonpmm/keyWordsEdit");
		mv.addObject("key", data.get("data"));
		return mv;
	}
	
	@RequestMapping("addKeyWords")
	public String addKeyWords(){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
		keyWordsService.addKeyWords(pd);
		return "redirect:list.do";
	}
	
	@RequestMapping("goAdd")
	public ModelAndView goAdd(){
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/infineonpmm/keyWordsAdd");
		return mv;
	}
	
	@RequestMapping("editKeyWords")
	public String editKeyWords(){
		PageData pd = this.getPageData();
		keyWordsService.updateKeyWords(pd);
		return "redirect:list.do";
	}
	
	@RequestMapping("delete")
	public String delete(){
		PageData pd = this.getPageData();
		String ids = (String) pd.get("ids");
		if(!"".equals(ids) && ids != null){
			String[] array = ids.split(",");
			pd.put("array", array);
		}
		keyWordsService.deleteKeyWords(pd);
		return "redirect:list.do";
	}
}
