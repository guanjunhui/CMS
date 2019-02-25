package cn.cebest.controller.system.shaoYin;

import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.WarrantyClaimService;
import cn.cebest.util.DateUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
/** 
 * 说明：提问接口
 * 创建人：lwt
 * @version
 */
@Controller
@RequestMapping(value="/warrantyClaim")
public class WarrantyClaimController extends BaseController {
	
	String menuUrl = "warrantyClaim/list.do"; //菜单地址(权限用)
	@Resource(name="warrantyClaimService")
	private WarrantyClaimService claimService;
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	/*@RequestMapping(value="/goAdd")
	@RequiresPermissions("problem::goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/question/questionAdd");
		return mv;
	}	*/
	/**
	 * banner入口方法
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@RequiresPermissions("warrantyClaim:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String name = pd.getString("title");
		if(StringUtils.isNotEmpty(name)){
			String decode= URLDecoder.decode(name, "UTF-8");
			pd.put("keywords",decode);
		}
		page.setPd(pd);
		List<PageData> list = claimService.warrantyClaimlistPage(page);
		mv.setViewName("system/shaoYin/WarrantyClaimList");
		mv.addObject("list", list);
		return mv;
		}
	/**
	 * @param mess
	 * @param file
	 * @return
	 * 
	 */
	/*@RequestMapping(value = "/add")
	@RequiresPermissions("problem:add")
	@ResponseBody
	public ModelAndView add(Question bann, 
			@RequestParam(value = "ids", required = false) String[] ids,int is_add) {	
		HttpServletRequest request = this.getRequest();//获取当前的request请求
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			logBefore(logger, Jurisdiction.getUsername() + "设置get请求编码出现异常");
			e1.printStackTrace();
		}
		ModelAndView mav=new ModelAndView();
		//图片进行遍历
		List<MultipartFile> images=bann.getImages();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();	
		// 添加
		bann.setId(this.get32UUID());
		bann.setCreate_time(DateUtil.getTime());
		String filePath = PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile; // 图片文件上传路径
		try {
			//保存上传的多张图片
			if (null != images && !images.isEmpty()) {
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					pd.put("MASTER_ID", bann.getId());//保存相关banner的id
					pd.put("IMGURL", ffile + "/" + fileName); // 路径
					//pd.put("create_time", DateUtil.getTime());//保存创建时间
					questionService.saveQuestionImage(pd);
				}
			}
			questionService.saveQuestion(bann); // 执行保存资讯
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "保存banner出现异常");
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:goAdd.do");
			return mav;
		}
		mav.setViewName("redirect:list.do");
		return mav;
	}*/
	/**
	 * 刪除保修索赔
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("warrantyClaim:delete")
	public String deleteProduct(String id) {
		try {
			if(id!=null && !"".equals(id)){
				claimService.delWarrantyClaim(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除banner异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}
	
	/**
	 * 批量删除内容
	 * 
	 */
	@RequestMapping(value = "/delAllWarrantyClaim")
	@RequiresPermissions("warrantyClaim:delAll")
	public String delAllContent(String[] id) {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除WarrantyClaim");
		try {
			if(id!=null && !"".equals(id)){
				claimService.delAllWarrantyClaim(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除banner异常");
			e.printStackTrace();
		}
		return "redirect:list.do";
	}
	/**
	 * 更新状态
	 * 
	 * @param ids
	 * @return
	 */
	/*@RequestMapping("/updateStatus")
	@RequiresPermissions("problem:updateStatus")
	public String updateStatus() {
		PageData pd= this.getPageData();
		try {
			claimService.changeStatus(pd);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改状态异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}*/
	/**
	 * 改变时否处理的状态
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/updateRecommend")
	@RequiresPermissions("problem:updateRecommend")
	public String updateRecommend() {
		PageData pd= this.getPageData();
		try {
			if("0".equals(pd.get("recommend"))){
				pd.put("recommend", null);
				pd.put("recommend_time", null);
			}else{
				pd.put("recommendTime", DateUtil.getTime());
			}
			claimService.updateRecommend(pd);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改状态异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}
	/**
	 * 根据id查询详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("detailById")
	@RequiresPermissions("warrantyClaim:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				obj = claimService.detailById(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询保修索赔详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/shaoYin/WarrantyClaimDetail");
		mv.addObject("data", obj);
		return mv;
	}
	
}
