package cn.cebest.controller.front.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

@Controller
@RequestMapping("columconfig")
public class ColumconfigController extends BaseController{

	@Autowired
	private ColumconfigService columconfigService;
	

	@ResponseBody
	@RequestMapping("/list")
	private JsonResult columconfigList(String columId,Page page) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", columId);
		page.setPd(pd);
		List<PageData> datalist=null;
		try {
			datalist = columconfigService.findColumconfiglistPage(page);
		} catch (Exception e) {
			logger.error("find the colum by ID ["+columId+"]ocurred error!",e);
		}
		return new JsonResult(Const.HTTP_OK, "OK", datalist);
	}
	
	
}
