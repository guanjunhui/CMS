package cn.cebest.controller.front.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.entity.Page;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value="employ")
public class EmployComtroller {

	@Autowired
	private EmployService employService; 
	
	@ResponseBody
	@RequestMapping(value="employList",method=RequestMethod.POST)
	public JsonResult queryEmployList(Page page,Map params,String columId) throws Exception {
		PageData pd = new PageData();
		pd.put("colum_id", columId);
		page.setPd(pd);
		List<PageData> list = employService.listPage(page);
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
}
