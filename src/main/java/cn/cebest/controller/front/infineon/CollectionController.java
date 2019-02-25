package cn.cebest.controller.front.infineon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.Collection;
import cn.cebest.service.web.infineon.CollectionService;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
/**
 * 收藏
 * @author
 *
 */
@RestController
@RequestMapping(value = "/collection")
public class CollectionController extends BaseController {

	@Autowired
	private CollectionService collectionService;
	
	/**
	 * 根据用户id查询收藏列表
	 * @return
	 */
	@RequestMapping(value = "/getCollections", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult listByUserId(Page page) {
		PageData pd = this.getPageData();
		page.setPd(pd);
		JsonResult jr = collectionService.listByUserId(page);
		return jr;
	}
	
	
	/**
	 * 添加收藏
	 * @param collection
	 * @return
	 */
	@RequestMapping(value = "/addCollection")
	@ResponseBody
	public JsonResult addCollection(Collection collection) {
		collection.setId(this.get32UUID());
		JsonResult jr = collectionService.addCollection(collection);
		return jr;
	}
	
	/**
	 * 获取用户收藏信息
	 * @param collection
	 * @return
	 */
	@RequestMapping(value = "/getColl")
	@ResponseBody
	public JsonResult getColl(Collection collection) {
		JsonResult jr = collectionService.getColl(collection);
		return jr;
	}
	
	/**
	 * 取消收藏
	 * @return
	 */
	@RequestMapping(value = "/delColl")
	@ResponseBody
	public JsonResult delColl(String id) {
		JsonResult jr = collectionService.delColl(id);
		return jr;
	}
}
