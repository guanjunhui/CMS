package cn.cebest.controller.front.cms;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通用跳转页
 * @author lenovo
 *
 */
@Controller
public class JumpController {

	@RequestMapping(value="jump/{page}",method=RequestMethod.GET)
	public String pagejump(@PathVariable("page") String page,String topicId, ModelMap model){
		if(StringUtils.isNotEmpty(topicId)){
			model.addAttribute("topicId", topicId);
		}
		return page;
	}
}
