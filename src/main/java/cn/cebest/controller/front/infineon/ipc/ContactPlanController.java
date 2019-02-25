package cn.cebest.controller.front.infineon.ipc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.infineon.ContactPlanService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

/**
 * 联系该方案表单
 * @author wangweijie
 * @Date 2018年9月25日
 * @company 中企高呈
 */
@RestController
@RequestMapping("plan")
public class ContactPlanController extends BaseController{
	@Autowired
	private ContactPlanService contactPlanService;
	/**
	 * 联系该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	public JsonResult saveContactPlan(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	contactPlanService.savecontactPlan(pd);
		} catch (Exception e) {
			logger.error("save the ContactPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 收藏该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="collection",method=RequestMethod.POST)
	public JsonResult collectionPlan(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	contactPlanService.collectionPlan(pd);
		} catch (Exception e) {
			if(e.getMessage().equals("您已收藏")){
				return new JsonResult(Const.HTTP_ERROR_401, e.getMessage());
			}
			logger.error("save the collectionPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 取消收藏该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancelCollection",method=RequestMethod.POST)
	public JsonResult cancelCollection(HttpServletRequest request){
		PageData pd = this.getPageData();
	    //获取数据
	    try {
	    	contactPlanService.cancelCollection(pd);
		} catch (Exception e) {
			logger.error("cancel collection the collectionPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
	/**
	 * 点赞该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="thumb",method=RequestMethod.POST)
	public JsonResult thumbPlan(HttpServletRequest request){
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	contactPlanService.thumbPlan(pd);
		} catch (Exception e) {
			if(e.getMessage().equals("您已点赞")){
				return new JsonResult(Const.HTTP_ERROR_401, e.getMessage());
			}
			logger.error("save the collectionPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 取消点赞该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancelThumb",method=RequestMethod.POST)
	public JsonResult cancelThumb(HttpServletRequest request){
		PageData pd = this.getPageData();
		//获取数据
		try {
			contactPlanService.cancelThumbPlan(pd);
		} catch (Exception e) {
			
			logger.error("cancel the thumb collectionPlan occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 是否点赞该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="isThumb",method=RequestMethod.POST)
	public JsonResult isThumb(HttpServletRequest request){
		JsonResult result = null;
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	String count = contactPlanService.isThumb(pd);
	    	if("0".equals(count)){
	    		//未点赞
	    		result = new JsonResult(Const.HTTP_OK, "ok","0");
	    	}else{
	    		//已点赞
	    		result = new JsonResult(Const.HTTP_OK, "ok","1");
	    	}
		} catch (Exception e) {
			logger.error("save the collectionPlan occured error!",e);
			result = new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 回去点赞量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getThumbNum",method=RequestMethod.POST)
	public JsonResult getThumbNum(HttpServletRequest request){
		JsonResult result = null;
		PageData pd = this.getPageData();
		//获取数据
		try {
			String count = contactPlanService.getThumbNum(pd);
			result = new JsonResult(Const.HTTP_OK, "ok",count);
		} catch (Exception e) {
			logger.error("save the collectionPlan occured error!",e);
			result = new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return result;
	}
	
	/**
	 * 是否收藏该方案
	 * @param request
	 * @return
	 */
	@RequestMapping(value="isCollection",method=RequestMethod.POST)
	public JsonResult isCollectionPlan(HttpServletRequest request){
		JsonResult result = null;
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID());
	    //获取数据
	    try {
	    	String count = contactPlanService.isCollectionPlan(pd);
	    	if("0".equals(count)){
	    		//未收藏
	    		result = new JsonResult(Const.HTTP_OK, "ok","0");
	    	}else{
	    		//已收藏
	    		result = new JsonResult(Const.HTTP_OK, "ok","1");
	    	}
		} catch (Exception e) {
			logger.error("save the collectionPlan occured error!",e);
			result = new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return result;
	}
	
	
}
