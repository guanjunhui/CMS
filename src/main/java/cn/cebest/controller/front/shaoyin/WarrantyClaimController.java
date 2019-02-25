package cn.cebest.controller.front.shaoyin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.WarrantyClaim.ProductRegistrationService;
import cn.cebest.service.system.WarrantyClaim.WarrantyClaimService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

@Controller
@RequestMapping(value = "/frontClaimService")
public class WarrantyClaimController extends BaseController {
	
	@Autowired
	private WarrantyClaimService claimService;
	@Autowired
	private ProductRegistrationService productRegistrationService;
	/**保存问题
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveWarrantyClaim")
	@ResponseBody
	public JsonResult saveWarrantyClaim(HttpServletRequest request){
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("submitTime", DateUtil.getTime());
		pd.put("recommend", Const.APPLY_STATUS_0);
	    //获取数据
	    try {
	    	//校验注册码与邮箱是否一致
	    	Map<String, String> pMap = new HashMap<String, String>();
	    	pMap.put("email", pd.getString("email"));
	    	pMap.put("code", pd.getString("regCode"));
	    	int count = this.productRegistrationService.selectByEmailAndRegistionCode(pMap);
	    	if(count == 0){
	    		return new JsonResult(Const.HTTP_ERROR, "Sorry, the email address does not match the registration code in our database, please re-enter again.");
	    	}
	    	claimService.saveWarrantyClaim(pd);
		} catch (Exception e) {
			logger.error("save the question occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}	
	
}