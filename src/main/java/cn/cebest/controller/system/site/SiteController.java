package cn.cebest.controller.system.site;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
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
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.entity.web.WebSite;
import cn.cebest.portal.common.resolve.fetchshaoyin.PaseTool;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewMainVO;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewVO;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.service.web.image.ImageManager;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;

/**
 * 站点管理
 * @author qichangxin
 *
 */
@Controller
@RequestMapping(value = "/site")
public class SiteController extends BaseController {
	
	String menuUrl = "site/sitelistPage.do"; //菜单地址(权限用)
	@Resource(name = "siteService")
	private SiteService siteService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	/*@Autowired
	private PicturesManager picturesService;*/
	@Autowired
	private ImageManager imageService;
	
	/**
	 * 后台接口,站点列表
	 * @return
	 */
	@RequiresPermissions("site:list")
	@RequestMapping(value="/sitelistPage")
	public ModelAndView sitelistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//站点名称检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		try {
			List<PageData> siteList = siteService.sitelistPage(page);//列出站点列表
			mv.setViewName("system/site/site_list");
			mv.addObject("list", siteList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 删除站点
	 * @return
	 */
	@RequiresPermissions("site:delete")
	@RequestMapping("/delSite")
	public void delSite(PrintWriter out){
		logBefore(logger, Jurisdiction.getUsername()+"删除站点");
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			siteService.delSite(pd);
			FHLOG.save(Jurisdiction.getUsername(), "删除站点："+pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**
	 * 批量删除站点
	 * 
	 */
	@RequestMapping(value="/delAllSite")
	@ResponseBody
	public Object delAllSite() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除site");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			FHLOG.save(Jurisdiction.getUsername(), "批量删除site");
			List<PageData> pdList = new ArrayList<PageData>();
			String SITE_IDS = pd.getString("SITE_IDS");
			if(null != SITE_IDS && !"".equals(SITE_IDS)){
				String arraySITE_IDS[] = SITE_IDS.split(",");
				siteService.delAllSite(arraySITE_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**去新增站点
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("site:add")
	@RequestMapping(value="/goAddSite")
	public ModelAndView goAddSite() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(pd.containsKey("siteId")){
			pd=siteService.findSiteById(pd);
		}
		mv.setViewName("system/site/site_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存站点
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveSite")
	@ResponseBody
	public JsonResult saveSite(HttpServletRequest request){
		logBefore(logger, Jurisdiction.getUsername()+"新增站点");
		PageData pd = new PageData();
		//上传LOGO
		String fileId="";
		//转成文件上传请求
	    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
		String id=murequest.getParameter("siteId");
	    MultipartFile file=murequest.getFile("siteLogo");
	    if(!file.isEmpty()){
			try {
				fileId = this.saveFileByName(file,id);
			} catch (Exception e) {
				logger.error("upload the file failed!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
			pd.put("siteLogo", fileId);
	    }
	    //获取普通表单数据
	    pd.put("siteName", murequest.getParameter("siteName"));
	    pd.put("siteLanguage", murequest.getParameter("siteLanguage"));
	    pd.put("siteDomain", murequest.getParameter("siteDomain"));
	    pd.put("siteSubdomain", murequest.getParameter("siteSubdomain"));
	    pd.put("siteRecordno", murequest.getParameter("siteRecordno"));
	    pd.put("siteEmailAddr", murequest.getParameter("siteEmailAddr"));
	    pd.put("siteEmailPwd", murequest.getParameter("siteEmailPwd"));
	    pd.put("siteEmailPrefix", murequest.getParameter("siteEmailPrefix"));
	    pd.put("siteEmailSmtp", murequest.getParameter("siteEmailSmtp"));
	    pd.put("siteKeyword", murequest.getParameter("siteKeyword"));
	    pd.put("siteDesc", murequest.getParameter("siteDesc"));
	    pd.put("siteIndex", murequest.getParameter("siteIndex"));
	    pd.put("ifStatic", murequest.getParameter("ifStatic"));
	    try {
			if(StringUtils.isEmpty(id)){
				pd.put("siteId", this.get32UUID());
				pd.put("createdTime", Tools.date2Str(new Date()));	//发表时间
				pd.put("siteStatus",Const.VALID);
				this.siteService.saveSite(pd);
			}else{
				pd.put("siteId", id);
				this.siteService.editSite(pd);
			}
		} catch (Exception e) {
			logger.error("save the template failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/**
	 * 修改状态
	 * @return
	 */
	@RequiresPermissions("site:changeStatus")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public Object auditSite(
			@RequestParam("siteId")String siteId,
			@RequestParam("status")String status){
		PageData pd = new PageData();
		try {
			if(StringUtils.isNotBlank(status) && "0".equals(status)){
	    		status="1";
	    	}else if(StringUtils.isNotBlank(status) && "1".equals(status)){
	    		status="0";
	    	}
			pd.put("siteStatus", status);
			pd.put("siteId", siteId);
			siteService.changeStatus(pd);
		} catch (Exception e) {
			logger.error("change the site status occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**获取所有的站点
	 * 
	 * @param ROLE_ID
	 * @return
	 */
	@RequestMapping(value="/getAllSite")
	@ResponseBody
	public JsonResult getAllSite(){
		List<WebSite> siteList=null;
		try{
			siteList=siteService.findAllSiteByStatus(Const.VALID);
		} catch(Exception e){
			logger.error(e.toString(), e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",siteList);
	}

	/**通过角色获取站点
	 * 
	 * @param ROLE_ID
	 * @return
	 */
	@RequestMapping(value="/getSitesByRoleId")
	@ResponseBody
	public JsonResult getSitesByRoleId(){
		return null;
//		PageData pd = new PageData();
//		List<WebSite> siteList=null;
//		try{
//			pd = this.getPageData();
//			siteList=siteService.findSiteByRoleId(pd);
//		} catch(Exception e){
//			logger.error(e.toString(), e);
//			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
//		}
//		return new JsonResult(Const.HTTP_OK, "OK",siteList);
	}
	
	 /**保存图片
	 * @param
	 * @throws Exception
	 */
	private String saveFileByName(MultipartFile file,String siteId) throws Exception{
		String  ffile = DateUtil.getDays(), fileName = "";
		if (null != file && !file.isEmpty()) {
			//删除以前上传文件
			if(StringUtils.isNotEmpty(siteId.trim())){
				WebSite webSite=this.siteService.findSitePoById(siteId);
				if(StringUtils.isNotEmpty(webSite.getSiteLogo())){
					//PageData pageData=picturesService.findById(new PageData("PICTURES_ID",webSite.getSiteLogo()));
					PageData pageData=imageService.findById(new PageData("IMAGE_ID",webSite.getSiteLogo()));
					if(pageData!=null){
						String oldpath=SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) +pageData.getString("IMGURL");
						DelAllFile.deleteFile(oldpath);
						imageService.delete(new PageData("IMAGE_ID",webSite.getSiteLogo()));
					}
				}
			}
			//上传新文件
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile;//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());//执行上传
		}else{
			logger.error("dont access the file!");
			return StringUtils.EMPTY;
		}
		String fileId=this.get32UUID();
		PageData pd = new PageData();
		pd.put("IMAGE_ID", fileId);			//主键
		pd.put("TITLE", "图片");								//标题
		pd.put("TYPE", 6);
		pd.put("NAME", file.getOriginalFilename());			//文件名
		pd.put("IMGURL", ffile + "/" + fileName);				//路径
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		pd.put("MASTER_ID", pd.getString("id"));			//附属与
		pd.put("BZ", "图片管理处上传");						//备注
		Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
		imageService.save(pd);
		return fileId;
	}

}