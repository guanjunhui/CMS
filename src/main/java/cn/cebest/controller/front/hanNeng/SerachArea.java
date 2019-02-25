package cn.cebest.controller.front.hanNeng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.product.Product;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value = "/searchArea")
public class SerachArea extends BaseController {
	/**
	 * 参数名称
	 */
	private static final String COLUMID = "columId";
	private static final String TYPEID = "typeId";
	private static final String PROUCTID = "prouctId";
	
	@Autowired
	private ProductService productService;
	/**保存问题
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/searchArea")
	@ResponseBody
	public Map<String,Object> searchArea(HttpServletRequest request,Page page,ModelMap model){
		List<Product> productAreaList = null;
		Map<String,Object> map=new HashMap<>();
		try {
		//设置分页参数
		int currentPage =1;
		//int showCount = FrontUtils.getPageSize(params);
		PageData pd = this.getPageData();
		model.addAttribute("area", pd.getString("area"));
		page.setPd(pd);
	    //获取数据
	    	productAreaList = productService.queryProductByColumnId(page);
	    	map.put("productAreaList", productAreaList);
	    	map.put("page", page);
	    	map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logger.error("save the question occured error!",e);
		}
		return map;
	}	
	
}