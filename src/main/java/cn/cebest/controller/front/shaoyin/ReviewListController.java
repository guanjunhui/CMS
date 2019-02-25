package cn.cebest.controller.front.shaoyin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.portal.common.resolve.fetchshaoyin.PaseTool;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewMainVO;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value = "/review")
public class ReviewListController extends BaseController {
	
	/**读取json文件转json数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public JsonResult ReviewList(HttpServletRequest request){
		PageData pd = new PageData();
		//获取参数
		pd = this.getPageData();
		ReviewMainVO reviewMainVO = null;
		Map<String, ReviewMainVO> reviewFromJson = PaseTool.getReviewFromJson();
	    //获取数据
	    try {
	    	reviewMainVO = reviewFromJson.get(pd.getString("productID"));
		} catch (Exception e) {
			logger.error("save the question occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",reviewMainVO);
	}	
	
}