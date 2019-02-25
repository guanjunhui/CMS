
package cn.cebest.controller.front.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.web.Banner;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.web.banner.BannerManagerService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

@RestController
@RequestMapping(value = "/index")
public class IndexBannerController extends BaseController{
	
	protected Logger logger = Logger.getLogger(IndexBannerController.class);

	@Autowired
	private BannerManagerService bannerManagerService;
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value = "/findIndexBanner")
	public Object findIndexBanner(String type,String currentId) {
		PageData pd = new PageData(); 
		Page page=new Page();
		pd.put("columId", currentId);
		pd.put("sort", "sort");
		pd.put("showCount", 5);
		page.setPd(pd);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		if (!StringUtils.isEmpty(type) && "PC".equals(type)) {
			List<Banner> list;
			try {
				list = bannerManagerService.selectBannerDetailByColumnId(pd);
				List<Banner> result=new ArrayList<Banner>();
				for (Banner banner : list) {
					int count =Const.INT_0;
					if(banner.getImageUrl()!=null && banner.getImageUrl()!=""){
						if(count++<10){
							result.add(banner);
						}
					} 
				}
				resultMap.put("list", result);
			} catch (Exception e) {
				logger.error("查询首页banner抛出异常");
				e.printStackTrace();
			}
		}else {
			pd.put("colum_id", currentId);
			pd.put("sort", "sort");
			List<PageData> list = null;
			try {
	 			list = contentService.findPmmImageList(page);
			} catch (Exception e) {
				logger.error("find the colum ocurred error!",e);
			}
			resultMap.put("list", list);
		}
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
}
