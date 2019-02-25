package cn.cebest.controller.system.dictionaries;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.Dictionaries;
import cn.cebest.service.system.dictionaries.DictionariesManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import net.sf.json.JSONArray;

/** 
 * 说明：数据字典
 * 创建人：中企高呈
 * 创建时间：2015-12-16
 */
@Controller
@RequestMapping(value="/dictionaries")
public class DictionariesController extends BaseController {
	
	String menuUrl = "dictionaries/list.do"; //菜单地址(权限用)
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@RequiresPermissions("dictionaries:save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Dictionaries");
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DICTIONARIES_ID", this.get32UUID());	//主键
		dictionariesService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**保存新增根目录
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/saveRoot")
	@RequiresPermissions("dictionaries:saveRoot")
	public void saveRoot(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Dictionaries");
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DICTIONARIES_ID", this.get32UUID());	//主键
		dictionariesService.save(pd);
		out.write("success");
		out.close();
	}
	
	/**
	 * 删除
	 * @param DICTIONARIES_ID
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteD")
	@ResponseBody
	@RequiresPermissions("dictionaries:deleteD")
	public void delete(@RequestParam String DICTIONARIES_ID,PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Dictionaries");
		PageData pd = new PageData();
		pd.put("DICTIONARIES_ID", DICTIONARIES_ID);
		String errInfo = "success";
		if(dictionariesService.listSubDictByParentId(DICTIONARIES_ID).size() > 0){//判断是否有子级，是：不允许删除
			errInfo = "false";
		}else{
			pd = dictionariesService.findById(pd);//根据ID读取
			if(null != pd.get("TBSNAME") && !"".equals(pd.getString("TBSNAME"))){
				String[] table = pd.getString("TBSNAME").split(",");
				for(int i=0;i<table.length;i++){
					pd.put("thisTable", table[i]);
					try {
						if(Integer.parseInt(dictionariesService.findFromTbs(pd).get("zs").toString())>0){//判断是否被占用，是：不允许删除(去排查表检查字典表中的编码字段)
							errInfo = "false";
							break;
						}
					} catch (Exception e) {
							logger.error("check dic code occured error!", e);
							errInfo = "false2";
							break;
					}
				}
			}
		}
		if("success".equals(errInfo)){
			dictionariesService.delete(pd);	//执行删除
			out.write("success");
			out.close();
		}
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("dictionaries:edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Dictionaries");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		dictionariesService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("dictionaries:list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Dictionaries");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");					//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String DICTIONARIES_ID = null == pd.get("DICTIONARIES_ID")?"":pd.get("DICTIONARIES_ID").toString();
		if(null != pd.get("id") && !"".equals(pd.get("id").toString())){
			DICTIONARIES_ID = pd.get("id").toString();
		}
		pd.put("DICTIONARIES_ID", DICTIONARIES_ID);					//上级ID
		page.setPd(pd);
		List<PageData>	varList = dictionariesService.list(page);	//列出Dictionaries列表
		mv.addObject("pd", dictionariesService.findById(pd));		//传入上级所有信息
		mv.addObject("DICTIONARIES_ID", DICTIONARIES_ID);			//上级ID
		mv.setViewName("system/dictionaries/dictionaries_list");
		mv.addObject("varList", varList);
		mv.addObject("QX",Jurisdiction.getHC());					//按钮权限
		return mv;
	}
	
	/**
	 * 显示列表ztree
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllDict")
	@RequiresPermissions("dictionaries:listAllDict")
	public ModelAndView listAllDict(Model model,String DICTIONARIES_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			JSONArray arr = JSONArray.fromObject(dictionariesService.listAllDict(this.getContextPath(),"0"));
			String json = arr.toString();
			json = json.replaceAll("DICTIONARIES_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subDict", "nodes").replaceAll("hasDict", "checked").replaceAll("treeurl", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("DICTIONARIES_ID",DICTIONARIES_ID);
			mv.addObject("pd", pd);	
			mv.setViewName("system/dictionaries/dictionaries_ztree");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("dictionaries:goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DICTIONARIES_ID = null == pd.get("DICTIONARIES_ID")?"":pd.get("DICTIONARIES_ID").toString();
		pd.put("DICTIONARIES_ID", DICTIONARIES_ID);					//上级ID
		mv.addObject("pds",dictionariesService.findById(pd));		//传入上级所有信息
		mv.addObject("DICTIONARIES_ID", DICTIONARIES_ID);			//传入ID，作为子级ID用
		mv.setViewName("system/dictionaries/dictionaries_edit");
		mv.addObject("msg", "save");
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("dictionaries:goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DICTIONARIES_ID = pd.getString("DICTIONARIES_ID");
		pd = dictionariesService.findById(pd);	//根据ID读取
		mv.addObject("pd", pd);					//放入视图容器
		pd.put("DICTIONARIES_ID",pd.get("PARENT_ID").toString());			//用作上级信息
		mv.addObject("pds",dictionariesService.findById(pd));				//传入上级所有信息
		mv.addObject("DICTIONARIES_ID", pd.get("PARENT_ID").toString());	//传入上级ID，作为子ID用
		pd.put("DICTIONARIES_ID",DICTIONARIES_ID);							//复原本ID
		mv.setViewName("system/dictionaries/dictionaries_edit");
		mv.addObject("msg", "edit");
		return mv;
	}	
	/**
	 *去添加跟类目页面
	 * @return
	 */
	@RequestMapping("goAddRoot")
	@RequiresPermissions("dictionaries:goAddRoot")
	public ModelAndView goAddRoot(String PARENT_ID){
		ModelAndView mv = this.getModelAndView();
		mv.addObject("parentId",PARENT_ID);
		mv.setViewName("system/dictionaries/dictionaries_add");
		return mv;
	}

	/**判断编码是否存在
	 * @return
	 */
	@RequestMapping(value="/hasBianma")
	@ResponseBody
	@RequiresPermissions("dictionaries:hasBianma")
	public Object hasBianma(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(dictionariesService.findByBianma(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**获取指定编码下的内容
	 * @return
	 */
	@RequestMapping(value="/getDic")
	@ResponseBody
	@RequiresPermissions("dictionaries:getDic")
	public Object findByBianma(@RequestParam(value="code") String code){
		Dictionaries dictionaries=null;
		//根据编码获取字典数据
		try {
			dictionaries = dictionariesService.listParentAndChildDict(code);
		} catch (Exception e) {
			logger.error("find the dictionaries By BianMa happend error!", e);
			return new JsonResult(Const.HTTP_ERROR, "error");
		}
		if(dictionaries==null){
			return new JsonResult(Const.HTTP_OK, "ok", null);
		}
		List<Dictionaries> childList =dictionaries.getSubDict();
		return new JsonResult(Const.HTTP_OK, "ok", childList);
	}

	/**获取指定编码下的本级及其所有子级数据
	 * @return
	 */
	@RequestMapping(value="/getallchild")
	@ResponseBody
	@RequiresPermissions("dictionaries:getallchild")
	public JsonResult findAllChlidByBianma(@RequestParam(value="code") String code){
		List<Tree> treeList=null;
		//根据编码获取字典数据
		try {
			treeList = dictionariesService.findAllChildByBianma(code);
		} catch (Exception e) {
			logger.error("find the dictionaries By BianMa happend error!", e);
			return new JsonResult(Const.HTTP_ERROR, "error");
		}
		return new JsonResult(Const.HTTP_OK, "ok", treeList);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
