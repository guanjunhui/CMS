package cn.cebest.controller.system.customForm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Const;
import cn.cebest.util.Const.CUSTOMFORMTYPE;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
/** 
 * 说明：自定义表单属性
 * 创建人：qichangxin@300.cn
 * 创建时间：2018-01-12
 */
@Controller
@RequestMapping(value="/customformattr")
public class CustomFormAttributeController extends BaseController {
	
	@Autowired
	private CustomFormAttributeService customFormAttributeService;
	
	@Autowired
	private CustomformAttributeValueService customformAttributeValueService;
	
	/**
	 * 表单属性列表
	 * @return
	 */
	@RequiresPermissions("customformattr:list")
	@RequestMapping(value="/list")
	public String golist(ModelMap map,Page page){
		List<CustomFormAttribute> list=null;
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		try {
			list=customFormAttributeService.getAttributeAlllistPageByFormID(page);
		} catch (Exception e) {
			logger.error("get the customform list failed!",e);
		}
		if(CollectionUtils.isNotEmpty(list)){
			for(CustomFormAttribute attrInfo:list){
				String type=CUSTOMFORMTYPE.getMatchName(attrInfo.getAttrType());
				attrInfo.setAttrType(type);
			}
		}
		map.addAttribute("list", list);
		map.addAttribute("formId", pd.getString("formId"));
		return "system/customform/customformattr_list";
	}
	
	/**
	 * 添加或者修改表单属性
	 * @return
	 */
	@RequiresPermissions("customformattr:save")
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult save(CustomFormAttribute attrInfo){
		String id=attrInfo.getId();
		if(StringUtils.isEmpty(id)){
			attrInfo.setId(this.get32UUID());
		}
		try {
			if(StringUtils.isEmpty(id)){
				customFormAttributeService.save(attrInfo);
			}else{
				customFormAttributeService.update(attrInfo);
			}
		} catch (Exception e) {
			logger.error("save or update the customForm attr failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}


	/**
	 * 去栏目添加页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("customformattr:goEdit")
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(@RequestParam(value="id",required=false) String id,
			@RequestParam(value="formId") String formId) {
		ModelAndView mv = this.getModelAndView();
		CustomFormAttribute info = null;
		try {
			info=customFormAttributeService.getObjectById(id);
		} catch (Exception e) {
			logger.error("find the colum by customForm attr["+id+"] occured error!",e);
		}
		mv.setViewName("system/customform/customformattr_edit");
		mv.addObject("formId",formId);
		mv.addObject("info", info);
		return mv;
	}

	/**
	 * 删除表单属性
	 * @return
	 */
	@RequiresPermissions("customformattr:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResult delete(@RequestParam("id") String id){
		try {
			customFormAttributeService.deleteByPrimaryKey(id);
			customformAttributeValueService.deleteByAttrId(id);
		} catch (Exception e) {
			logger.error("save or update the customForm failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/**
	 * 获取属性类型
	 * @return
	 */
	@RequestMapping("/getAttrType")
	@ResponseBody
	public JsonResult getAttrType(){
		List<Tree> list=new ArrayList<Tree>();
		try {
			for(CUSTOMFORMTYPE type:CUSTOMFORMTYPE.values()){
				Tree tree = new Tree();
				tree.setId(type.getCode());
				tree.setName(type.getName());
				list.add(tree);
			}
		} catch (Exception e) {
			logger.error("get the attrType failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}



}
