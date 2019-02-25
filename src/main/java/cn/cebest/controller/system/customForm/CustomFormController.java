package cn.cebest.controller.system.customForm;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.customForm.CustomForm;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.service.system.customForm.CustomFormService;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

/** 
 * 说明：自定义表单
 * 创建人：qichangxin@300.cn
 * 创建时间：2018-01-10
 */
@Controller
@RequestMapping(value="/customform")
public class CustomFormController extends BaseController {
	@Autowired
	private CustomFormService customFormService;
	
	@Autowired
	private CustomFormAttributeService customFormAttributeService;
	
	@Autowired
	private CustomformAttributeValueService customformAttributeValueService;
	
	/**
	 * 表单列表
	 * @return
	 */
	@RequiresPermissions("customform:list")
	@RequestMapping(value="/list")
	public String golist(ModelMap map,Page page){
		List<CustomForm> customFormList=null;
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("siteId", RequestUtils.getSiteId(this.getRequest()));//站点id
		page.setPd(pd);
		try {
			customFormList=customFormService.selectlistPageAllFormAttribue(page);
		} catch (Exception e) {
			logger.error("get the customform list failed!",e);
		}
		map.addAttribute("list", customFormList);
		return "system/customform/customform_list";
	}
	
	
	/**
	 * 添加或者修改表单
	 * @return
	 */
	@RequiresPermissions("customform:save")
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult saveColumconfig(CustomForm customForm){
		String id=customForm.getId();
		if(StringUtils.isEmpty(id)){
			customForm.setId(this.get32UUID());
			customForm.setSiteId(RequestUtils.getSiteId(this.getRequest()));
			customForm.setCreatedTime(DateUtil.DatePattern(new Date()));
		}
		try {
			if(StringUtils.isEmpty(id)){
				customFormService.saveForm(customForm);
			}else{
				customFormService.updateForm(customForm);
			}
		} catch (Exception e) {
			logger.error("save or update the customForm failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/**去修改页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("customform:goEdit")
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(@RequestParam("id") String id) {
		ModelAndView mv = this.getModelAndView();
		CustomForm customForm = null;
		try {
			customForm=customFormService.selectByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("get the customForm by primary key failed!",e);
		}
		mv.setViewName("system/customform/customform_edit");
		mv.addObject("pd", customForm);
		return mv;
	}

	/**
	 * 删除表单
	 * @return
	 */
	@RequiresPermissions("customform:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult delete(@RequestParam("id") String id){
		try {
			customFormService.deleteByPrimaryKey(id);
			//根据表单ID查询属性ID集合
			List<String> attrIdList=customFormAttributeService.selectAttrIdsByFormID(id);
			customFormAttributeService.deleteByFormId(id);
			customformAttributeValueService.deleteByAttrIds(attrIdList);
		} catch (Exception e) {
			logger.error("save or update the customForm failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

}
