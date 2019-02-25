package cn.cebest.controller.system.template;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.TemplateTree;
import cn.cebest.service.web.image.ImageManager;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;
/** 
 * 说明：模板管理
 * 创建人：qichangxin
 * 创建时间：2017-09-27
 */
@Controller
@RequestMapping(value="/template")
public class TemplateController extends BaseController {
	
	String menuUrl = "template/list.do"; //菜单地址(权限用)
	@Resource(name="templateService")
	private TemplateManager templateService;
	@Autowired
	private ImageManager imageService;
	/**新增
	 * @param 
	 * @throws Exception
	 */
	@RequiresPermissions("template:save")
	@RequestMapping(value="/save")
	public ModelAndView save(HttpServletRequest request,int ifAppendSave){
		logBefore(logger, Jurisdiction.getUsername()+"新增template");
		PageData pd = new PageData();
		ModelAndView mod=this.getModelAndView();
		//上传缩略图
		String fileId="";
		//转成文件上传请求
	    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
	    MultipartFile file=murequest.getFile("TEM_IMAGEPATH");
	    if(!file.isEmpty()){
			try {
				fileId = this.saveFileByName(file);
			} catch (Exception e) {
				logger.error("upload the file failed!",e);
				return mod.addObject("result", new JsonResult(500, e.getMessage()));
			}
			pd.put("TEM_IMAGEPATH", fileId);
	    }
	    //获取普通表单数据
	    pd.put("TEM_NAME", murequest.getParameter("TEM_NAME"));
	    pd.put("TEM_FILEPATH", murequest.getParameter("TEM_FILEPATH"));
	    String type=murequest.getParameter("TEM_TYPE");
	    pd.put("TEM_TYPE", type);
	    pd.put("IS_DEFAULT", murequest.getParameter("IS_DEFAULT"));
	    pd.put("TYPE", murequest.getParameter("TYPE"));

		try {
			String id=murequest.getParameter("ID");
			String idDefault=murequest.getParameter("IS_DEFAULT");
			if(Const.YES.equals(idDefault)){//重置此类别的默认模板
				templateService.resetDefault(type);
			}
			if(StringUtils.isEmpty(id)){
				pd.put("ID", this.get32UUID());
				pd.put("CREATETIME", Tools.date2Str(new Date()));	//发表时间
				templateService.save(pd);
			}else{
				pd.put("ID", id);
				templateService.edit(pd);
			}
		} catch (Exception e) {
			logger.error("save the template failed!",e);
			return mod.addObject("result", new JsonResult(500, e.getMessage()));
		}
		if(ifAppendSave==1){
			mod.setViewName("redirect:goEdit.do");
			return mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功,继续添加!"));
		}
		mod.setViewName("redirect:list.do");
		return mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功!"));
	}
	
	/*暂时保留异步添加
	@RequiresPermissions("template:save")
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult save(HttpServletRequest request){
		logBefore(logger, Jurisdiction.getUsername()+"新增template");
		PageData pd = new PageData();
		//上传缩略图
		String fileId="";
		//转成文件上传请求
	    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
	    MultipartFile file=murequest.getFile("TEM_IMAGEPATH");
	    if(!file.isEmpty()){
			try {
				fileId = this.saveFileByName(file);
			} catch (Exception e) {
				logger.error("upload the file failed!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
			pd.put("TEM_IMAGEPATH", fileId);
	    }
	    //获取普通表单数据
	    pd.put("TEM_NAME", murequest.getParameter("TEM_NAME"));
	    pd.put("TEM_FILEPATH", murequest.getParameter("TEM_FILEPATH"));
	    String type=murequest.getParameter("TEM_TYPE");
	    pd.put("TEM_TYPE", type);
	    pd.put("IS_DEFAULT", murequest.getParameter("IS_DEFAULT"));
	    pd.put("TYPE", murequest.getParameter("TYPE"));

		try {
			String id=murequest.getParameter("ID");
			String idDefault=murequest.getParameter("IS_DEFAULT");
			if(Const.YES.equals(idDefault)){//重置此类别的默认模板
				templateService.resetDefault(type);
			}
			if(StringUtils.isEmpty(id)){
				pd.put("ID", this.get32UUID());
				pd.put("CREATETIME", Tools.date2Str(new Date()));	//发表时间
				templateService.save(pd);
			}else{
				pd.put("ID", id);
				templateService.edit(pd);
			}
		} catch (Exception e) {
			logger.error("save the template failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}*/

     /**删除
	 * @param out
	 * @throws Exception
	 */
	@RequiresPermissions("template:delete")
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除template");
		PageData pd = new PageData();
		pd = this.getPageData();
		String ID = pd.getString("ID");
		String ArrayDATA_IDS[] ={ID};
		if(templateService.check(ArrayDATA_IDS)>0){
			out.write("false");
		}else{
			templateService.delete(pd);
			out.write("success");
		}
		out.close();
	}
		
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequiresPermissions("template:list")
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表template");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String temName = pd.getString("TEM_NAME");//模板名称检索
		if(StringUtils.isNotEmpty(temName)){
			pd.put("TEM_NAME", new String(temName.trim().getBytes("ISO-8859-1"),"UTF-8"));
		}
		page.setPd(pd);
		List<PageData>	varList = templateService.list(page);//列出template列表
		mv.setViewName("system/template/template_list");
		mv.addObject("list", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());//按钮权限
		return mv;
	}
		
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除template");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			if(	templateService.check(ArrayDATA_IDS)==0){
				templateService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{pd.put("msg", "no");}
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		map.put("msg", pd.getString("msg"));
		return AppUtil.returnObject(pd, map);
	}
		
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequiresPermissions("template:goEdit")
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = templateService.findById(pd);	//根据ID读取
		mv.setViewName("system/template/template_edit");
		mv.addObject("pd", pd);
		return mv;
	}
		
	 /**获取所有模板
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getTree")
	@ResponseBody
	public JsonResult getTree(){
		PageData pd = new PageData();
		pd = this.getPageData();
		List<TemplateTree> treeList=null;
		try {
			treeList = templateService.listAllTree(pd);
		} catch (Exception e) {
			logger.error("get the template Tree occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",treeList);
	}

	 /**获取指定类型的模板
	 * @param type 栏目类型
	 * @param temType 模板类型
	 * @throws Exception
	 */
	@RequestMapping(value="/getDefinedTree")
	@ResponseBody
	public JsonResult getDefinedTree(@RequestParam("type") String type,
			@RequestParam("temType") String temType){
		List<TemplateTree> treeList=null;
		try {
			PageData pageData=new PageData();
			pageData.put("type", type);
			pageData.put("temType", temType);
			treeList = templateService.findTreesByDefinedType(pageData);
		} catch (Exception e) {
			logger.error("get the template by defined type occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",treeList);
	}

		
	 /**保存图片
	 * @param
	 * @throws Exception
	 */
	private String saveFileByName(MultipartFile file) throws Exception{
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile;//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());//执行上传
		}else{
			logger.error("dont access the file!");
		}
		String fileId=this.get32UUID();
		PageData pd = new PageData();
		pd.put("IMAGE_ID", fileId);						//主键
		pd.put("TITLE", "模板缩略图");							//标题
		pd.put("TYPE", 6);
		pd.put("NAME", file.getOriginalFilename());			//文件名
		pd.put("IMGURL", ffile + "/" + fileName);				//路径
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		pd.put("BZ", "模板缩略图");							//备注
		Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
		imageService.save(pd);
		return fileId;
	}


}
