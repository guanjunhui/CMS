package cn.cebest.controller.front.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
/**
 * 资讯管理
 * 
 * @author lwt
 *
 */
@Controller
@RequestMapping(value = "/newsfront")
public class MyMessageFrontController extends BaseController {

	@Resource(name = "MyMessageService")
	private MyMessageService messageService;
	
	@ResponseBody
	@RequestMapping("/newsPageList")
	public Object newsPageList(Map<String, Object> map, Page page) throws Exception {
		// 用page传参
		PageData pd = this.getPageData();
		map.put("columId", pd.get("columId"));
		map.put("showCount", Integer.parseInt(String.valueOf(pd.get("showCount"))));
		map.put("skipNum", Integer.parseInt(String.valueOf(pd.get("skipNum"))));
		List<NewMessage> findMessageFrontList = messageService.newsPageList(map);
		JsonResult jsonResult=new JsonResult(200, "ok",findMessageFrontList);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	@ResponseBody
	@RequestMapping("/pageList")
	public Object fingListPage(Map<String, Object> map, Page page) throws Exception {
		// 用page传参
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<NewMessage> findMessageFrontList = messageService.findMessagelistPage(page);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", findMessageFrontList);
		resultMap.put("page", page);
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	//根据id查找新闻图片
	@RequestMapping("/findNewsImageList")
	@ResponseBody
	public Object findNewsImageList(Map<String, Object> map, Page page) throws Exception{
		// 用page传参
		PageData pd = this.getPageData();
		page.setPd(pd);
		String id = (String) page.getPd().get("id");
		if ("".equals(id)) {
			return "";
		}
		List<Image> imageList = messageService.findNewsImageList(id);
		JsonResult jsonResult = new JsonResult(200, "ok", imageList);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	
	/**
	 * 通过资讯类型id或者栏目columId或两者同时去查询相关资讯
	 * 
	 * @param id 类型ID
	 * @param columId 栏目ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list(Map<String, Object> map, Page page) throws Exception {
		// 用page传参
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<NewMessage> findMessageFrontList = messageService.findMessageToFrontList(page);
		JsonResult jsonResult=new JsonResult(200, "ok",findMessageFrontList);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
}
