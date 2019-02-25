package cn.cebest.controller.system.columconfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
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

import com.alibaba.fastjson.JSON;
import com.baidu.translate.demo.TransApi;
import com.google.gson.Gson;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Tree;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.information.pictures.PicturesManager;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.service.web.banner.BannerManagerService;
import cn.cebest.service.web.image.ImageManager;
import cn.cebest.service.web.video.VideoManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;

/**
 * 栏目管理
 * @author qichangxin
 *
 */
@Controller
@RequestMapping(value = "/columconfig")
public class ColumconfigController extends BaseController {

	public static final String COLUMTYPE = "columType";
	public static final String COLUMID = "columId";
	
	String menuUrl = "columconfig/list.do"; //菜单地址(权限用)
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;
	@Autowired
	private PicturesManager picturesService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private BannerManagerService bannerManagerService;
	@Autowired
	private MyMessageTypeService myMessageTypeService;
	@Autowired
	private EmployService employService;
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private ColumGroupService columGroupService;
	@Autowired
	private VideoManager videoService;
	@Autowired
	private SeoService seoService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MyMessageService mymessageService;
	@Autowired
	private FileResourceService fileResourceService;
	@Autowired
	private ImageManager imageService;
	/**
	 * 后台接口,栏目列表
	 * @return
	 */
	@RequiresPermissions("columconfig:list")
	@RequestMapping(value="/golist")
	public ModelAndView golist(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/columconfig/columconfig_list");
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping(value="/list")
	@ResponseBody
	public JsonResult columconfiglistPage(){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			List<ColumConfig> list = null;
			if(StringUtils.isNotBlank(pd.getString("COLUM_NAME"))){
				pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));
				pd.put("COLUM_NAME", pd.getString("COLUM_NAME").trim());
				list = columconfigService.findAllList(pd);//列出栏目列表
			}else if(StringUtils.isNotBlank(pd.getString("TEM_TYPE"))){
				pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));
				list = columconfigService.findAllList(pd);//列出类型列表
			}else{
				pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));
				list = columconfigService.findTopAndChildList(pd);//列出栏目列表
			}
			map.put("list", list);
		} catch (Exception e) {
			logger.error("get the colum list occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",map);
	}
	
	/**
	 * 删除栏目
	 * @return
	 * @author liu
	 */
	@RequiresPermissions("columconfig:delete")
	@RequestMapping("/delColumconfig")
	@ResponseBody
	public JsonResult delColumconfig(){
		logBefore(logger, Jurisdiction.getUsername()+"删除栏目");
		String siteId = RequestUtils.getSiteId(this.getRequest());
		PageData pd = new PageData();
		PageData pd2 = new PageData();
		pd = this.getPageData();
		pd.put("SITEID", siteId);
		String groupId = pd.getString("COLUMGROUP_ID");
		try {
			if(StringUtils.isNotEmpty(groupId)){
				List<PageData> topColmIds = columconfigService.findTopTree(pd);
				for (PageData colum : topColmIds) {
					List<String> columIds=this.columconfigService.findSelfAndChildIds(colum.getString("ID"),siteId);
					String[] ids=columIds.toArray(new String[columIds.size()]);
					//删除栏目图片
					this.imageService.deleteAll(ids);
					this.videoService.delVideos(ids);
					//根据栏目Id删除SEO
					pd2.put("MASTER_ID", ids);
					this.seoService.deleteSeo(pd2);
					//删除栏目与栏目组的关系.
					columconfigService.delAll(columIds.toArray(new String[columIds.size()]));
					
					//删除内容地址栏的url信息
					columconfigService.delContentAll(columIds.toArray(new String[columIds.size()]));
					
					//删除栏目与内容的映射关系
					this.contentService.deleteByColumnIds(ids);
					this.productTypeService.deleteByColumnIds(ids);
					this.productService.deleteByColumnIds(ids);
					this.bannerManagerService.deleteByColumnIds(ids);
					this.myMessageTypeService.deleteByColumnIds(ids);
					this.mymessageService.deleteMessages(ids);
					this.employService.deleteByColumnIds(ids);
					this.fileTypeService.deleteByColumnIds(ids);
					this.fileResourceService.deleteByColumnIds(ids);
				}
				this.columGroupService.delete(pd);
				return new JsonResult(Const.HTTP_OK, "OK");
			}else{
				List<String> columIds=this.columconfigService.findSelfAndChildIds(pd.getString("ID"), RequestUtils.getSiteId(this.getRequest()));
				//String[] ids=new String[]{pd.getString("ID")};
				String[] ids=columIds.toArray(new String[columIds.size()]);
				//删除栏目图片
				this.imageService.deleteAll(ids);
				this.videoService.delVideos(ids);
				//根据栏目Id删除SEO
				pd2.put("MASTER_ID", ids);
				this.seoService.deleteSeo(pd2);
				//删除栏目与栏目组的关系.
	//			this.columGroupService.columCountReduceByColumnIds(ids);
				columconfigService.delAll(columIds.toArray(new String[columIds.size()]));
				
				//删除内容地址栏的url信息
				columconfigService.delContentAll(columIds.toArray(new String[columIds.size()]));
				
				//删除栏目与内容的映射关系
				this.productTypeService.deleteByColumnIds(ids);
				this.productService.deleteByColumnIds(ids);
				this.contentService.deleteByColumnIds(ids);
				this.bannerManagerService.deleteByColumnIds(ids);
				this.myMessageTypeService.deleteByColumnIds(ids);
				this.mymessageService.deleteMessages(ids);
				this.employService.deleteByColumnIds(ids);
				this.fileTypeService.deleteByColumnIds(ids);
				this.fileResourceService.deleteByColumnIds(ids);
			}
			FHLOG.save(Jurisdiction.getUsername(), "删除栏目："+pd);
		} catch (Exception e) {
			logger.error("delete the colum occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 批量删除栏目详情
	 * 
	 */
	@RequestMapping(value="/delAllColumconfig")
	@ResponseBody
	public Object delAllColumconfig() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除columconfig");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			FHLOG.save(Jurisdiction.getUsername(), "批量删除columconfig");
			List<PageData> pdList = new ArrayList<PageData>();
			String COLUMCONFIG_IDS = pd.getString("COLUMCONFIG_IDS");
			if(null != COLUMCONFIG_IDS && !"".equals(COLUMCONFIG_IDS)){
				String arrayCOLUMCONFIG_IDS[] = COLUMCONFIG_IDS.split(",");
				columconfigService.updateAllColumconfig(arrayCOLUMCONFIG_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error("batch delete the colums occured error!",e);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	
	/**去栏目添加页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("columconfig:goAdd")
	@RequestMapping(value="/goAdd")
	public ModelAndView goAddColumconfig() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String groupId=pd.getString("COLUMGROUP_ID");
		try {
			pd=columconfigService.findColumconfigById(pd);
		} catch (Exception e) {
			logger.error("find the colum by ID["+pd.getString("ID")+"] occured error!",e);
		}
		mv.setViewName("system/columconfig/columconfig_edit");
		if (pd==null) {
			pd=new PageData();
		}
		pd.put("COLUMGROUP_ID", groupId);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存栏目
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView saveColumconfig(HttpServletRequest request,int ifAppendSave){
		logBefore(logger, Jurisdiction.getUsername()+"新增栏目columconfig");
		PageData pd = new PageData();
		PageData pd2 = new PageData();
		ModelAndView mod=this.getModelAndView();
		
		String fileId="";
		String videoId="";
		//转成文件上传请求
	    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
	    MultipartFile file=murequest.getFile("COLUM_IMAGE");
	    MultipartFile video=murequest.getFile("sultipartFiles");
	    String groupId=murequest.getParameter("COLUMGROUP_ID");
	    if(!file.isEmpty()){
			try {
				fileId = this.saveFileByName(file);
			} catch (Exception e) {
				logger.error("upload the file failed!",e);
				return mod.addObject("result", new JsonResult(500, e.getMessage()));
			}
			pd.put("COLUM_IMAGE", fileId);
	    }
	    if(video!=null&&!video.isEmpty()){
			try {
				videoId = this.saveVideoByName(video,request);
			} catch (Exception e) {
				logger.error("upload the Video failed!",e);
				return mod.addObject("result", new JsonResult(500, e.getMessage()));
			}
			pd.put("COLUM_VIDEOID", videoId);
	    }
	    //获取普通表单数据
	    pd.put("COLUM_NAME", murequest.getParameter("COLUM_NAME"));
	    pd.put("COLUM_URLPATH", murequest.getParameter("COLUM_URLPATH"));
	    pd.put("COLUM_DISPLAY", murequest.getParameter("COLUM_DISPLAY"));
	    pd.put("PARENTID", murequest.getParameter("PARENTID"));
	    pd.put("COLUM_TEMPLATETID", murequest.getParameter("COLUM_TEMPLATETID"));
	    pd.put("COLUM_SUBNAME", murequest.getParameter("COLUM_SUBNAME"));
	    pd.put("COLUM_DESC", murequest.getParameter("COLUM_DESC"));
	    pd.put("OUT_URL", murequest.getParameter("OUT_URL"));
	    
        /*//翻译、将原文和结果的集合取出来
        String translate = Translate(murequest.getParameter("COLUM_NAME"));*/
	    //栏目地址栏的url信息
	    pd.put("COLUM_URLNAME", murequest.getParameter("COLUM_URLNAME"));
	    
	    pd2.put("SEO_TITLE", murequest.getParameter("SEO_TITLE"));
	    pd2.put("SEO_KEYWORDS", murequest.getParameter("SEO_KEYWORDS"));
		pd2.put("SEO_DESCRIPTION", murequest.getParameter("SEO_DESCRIPTION"));
	    if (murequest.getParameter("SORT")==null || murequest.getParameter("SORT").isEmpty()) {
	    	pd.put("SORT",0);
		}else {
			pd.put("SORT",murequest.getParameter("SORT"));
		}
	    pd.put("COLUM_TYPE", murequest.getParameter("COLUM_TYPE"));
	    pd.put("TEMPLATET_DETAIL_ID", murequest.getParameter("TEMPLATET_DETAIL_ID"));
	    pd.put("INDEX_STATUS", murequest.getParameter("INDEX_STATUS"));
		String id=murequest.getParameter("ID");
	    try {
			if(StringUtils.isEmpty(id)){
				id=this.get32UUID();
				pd.put("ID",id);
				pd.put("SITEID",RequestUtils.getSiteId(this.getRequest()));
				String Time = Tools.date2Str(new Date());
				pd.put("CREATETIME", Time);//发表时间
				pd.put("UPDATE_TIME",Time);//修改时间
			    pd.put("COLUMGROUP_ID", groupId);
			    if(murequest.getParameter("INDEX_STATUS").equals("1")){
					columconfigService.updateColumIndexStatus(pd);
					siteService.updateSiteIndexStatus(pd);
			    }
				columconfigService.saveColumconfig(pd);
				
				String seoId = this.get32UUID();
				pd2.put("ID",seoId);
				pd2.put("MASTER_ID",id);
				pd2.put("CREATED_TIME", Time);
				seoService.insetSeo(pd2);
			}else{
				pd.put("SITEID",RequestUtils.getSiteId(this.getRequest()));
				pd.put("ID", id);
				pd.put("UPDATE_TIME", Tools.date2Str(new Date()));//修改时间
				if(murequest.getParameter("INDEX_STATUS").equals("1")){
					columconfigService.updateColumIndexStatus(pd);
					siteService.updateSiteIndexStatus(pd);
			    }
				columconfigService.editColumconfig(pd);
				pd2.put("MASTER_ID",id);
				seoService.updateSeo(pd2);
			}
			
		} catch (Exception e) {
			logger.error("save the colum failed!",e);
			return mod.addObject("result", new JsonResult(500, e.getMessage()));
		}
	    if(ifAppendSave==1){
			mod.setViewName("redirect:goAdd.do?COLUMGROUP_ID="+groupId);
			return mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功,继续添加!"));
		}
		mod.setViewName("redirect:golist.do?COLUMGROUP_ID="+groupId);
		mod.addObject("result", new JsonResult(Const.HTTP_OK, "保存成功!"));
		return mod;
	}
	
	/* 暂时保留异步保存
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult saveColumconfig(HttpServletRequest request){
		logBefore(logger, Jurisdiction.getUsername()+"新增栏目columconfig");
		PageData pd = new PageData();
		String fileId="";
		//转成文件上传请求
	    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
	    MultipartFile file=murequest.getFile("COLUM_IMAGE");
	    if(!file.isEmpty()){
			try {
				fileId = this.saveFileByName(file);
			} catch (Exception e) {
				logger.error("upload the file failed!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage());
			}
			pd.put("COLUM_IMAGE", fileId);
	    }
	    //获取普通表单数据
	    pd.put("COLUM_NAME", murequest.getParameter("COLUM_NAME"));
	    pd.put("COLUM_URLPATH", murequest.getParameter("COLUM_URLPATH"));
	    pd.put("COLUM_DISPLAY", murequest.getParameter("COLUM_DISPLAY"));
	    pd.put("PARENTID", murequest.getParameter("PARENTID"));
	    pd.put("COLUM_TEMPLATETID", murequest.getParameter("COLUM_TEMPLATETID"));
	    pd.put("COLUM_SUBNAME", murequest.getParameter("COLUM_SUBNAME"));
	    pd.put("COLUM_DESC", murequest.getParameter("COLUM_DESC"));
	    pd.put("OUT_URL", murequest.getParameter("OUT_URL"));
	    if (murequest.getParameter("SORT")==null || murequest.getParameter("SORT").isEmpty()) {
	    	pd.put("SORT",0);
		}else {
			pd.put("SORT",murequest.getParameter("SORT"));
		}
	    pd.put("COLUM_TYPE", murequest.getParameter("COLUM_TYPE"));
		String id=murequest.getParameter("ID");
	    try {
			if(StringUtils.isEmpty(id)){
				id=this.get32UUID();
				pd.put("ID",id);
				pd.put("SITEID",RequestUtils.getSiteId(this.getRequest()));
				pd.put("CREATETIME", Tools.date2Str(new Date()));//发表时间
				String groupId=murequest.getParameter("COLUMGROUP_ID");
			    pd.put("COLUMGROUP_ID", groupId);
				columconfigService.saveColumconfig(pd);
//				this.columGroupService.columCountIncrement(groupId);
			}else{
				pd.put("ID", id);
				columconfigService.editColumconfig(pd);
			}
		} catch (Exception e) {
			logger.error("save the colum failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}*/
	
	/**
	 * 修改状态
	 * @return
	 */
	@RequiresPermissions("columconfig:changestatus")
	@RequestMapping("/changestatus")
	@ResponseBody
	public Object auditColumconfig(){
		PageData pd = new PageData();
		pd = this.getPageData();
		String status=pd.getString("COLUM_DISPLAY");
		try {
			if(StringUtils.isNotBlank(status) && Const.VALID.equals(status)){
	    		status=Const.INVALID;
	    	}else if(StringUtils.isNotBlank(status) && Const.INVALID.equals(status)){
	    		status=Const.VALID;
	    	}
			pd.put("COLUM_DISPLAY", status);
			columconfigService.auditColumconfig(pd);
		} catch (Exception e) {
			logger.error("change the colum status failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 修改栏目顺序
	 * @return
	 */
	
	@RequiresPermissions("columconfig:updateColumSort")
	@RequestMapping("/updateColumSort")
	@ResponseBody
	public Object updateColumSort(){
		PageData pd = new PageData();
		pd = this.getPageData();
	    String list=(String)pd.get("list");
	    List<ColumConfig> updateList = JSON.parseArray(list,ColumConfig.class);
	    try {
			columconfigService.updateColumSort(updateList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 获取所有栏目
	 * @return
	 */
	@RequestMapping("/getTree")
	@ResponseBody
	public JsonResult getTree(){
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Tree> list=null;
		try {
			pd.put("SITEID", RequestUtils.getSiteId(this.getRequest()));
			list = columconfigService.findTopAndChildTree(pd);//列出栏目列表
		} catch (Exception e) {
			logger.error("get the colum tree occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
	
	/**
	 * 获取指定栏目组下的所有栏目
	 * @return
	 */
	@RequestMapping("/getTreeByGroupId")
	@ResponseBody
	public JsonResult getTreeByGroupId(){
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Tree> list=null;
		try {
			pd.put("SITEID", RequestUtils.getSiteId(this.getRequest()));
			list = columconfigService.findTopAndChildTreeByGroupId(pd);//列出栏目列表
		} catch (Exception e) {
			logger.error("get the colum tree occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}

	/**
	 * 获取所有栏目
	 * @return
	 */
	@RequestMapping("/getcolumTypeDate")
	@ResponseBody
	public JsonResult getcolumTypeDate(){
		PageData pd = new PageData();
		pd.put(Const.COLUMTYPE.COLUM_TYPE_1.getCode(),Const.COLUMTYPE.COLUM_TYPE_1.getName());
		pd.put(Const.COLUMTYPE.COLUM_TYPE_2.getCode(),Const.COLUMTYPE.COLUM_TYPE_2.getName());
		pd.put(Const.COLUMTYPE.COLUM_TYPE_3.getCode(),Const.COLUMTYPE.COLUM_TYPE_3.getName());
		pd.put(Const.COLUMTYPE.COLUM_TYPE_4.getCode(),Const.COLUMTYPE.COLUM_TYPE_4.getName());
		pd.put(Const.COLUMTYPE.COLUM_TYPE_5.getCode(),Const.COLUMTYPE.COLUM_TYPE_5.getName());
		pd.put(Const.COLUMTYPE.COLUM_TYPE_6.getCode(),Const.COLUMTYPE.COLUM_TYPE_6.getName());
		return new JsonResult(Const.HTTP_OK, "OK",pd);
	}
	/**
	 * 获取指定模板类型的栏目
	 * @param TEM_TYPE
	 * @return
	 */
	@RequestMapping("/getAssignTypeTree")
	@ResponseBody
	public JsonResult getAssignTypeTree(){
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Tree> list=null;
		try {
			pd.put("SITEID", RequestUtils.getSiteId(this.getRequest()));
			list = columconfigService.findAssignTypeTree(pd);
		} catch (Exception e) {
			logger.error("get the colum tree occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",list);
	}
	
	/**
	 * 后台接口,栏目列表
	 * @return
	 */
	@RequestMapping(value="/assignTypelist")
	@ResponseBody
	public JsonResult getAssignTypeColums(){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			List<ColumConfig> list = null;
			if(StringUtils.isNotBlank(pd.getString("COLUM_NAME"))){
				pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));
				list = columconfigService.findAssignTypeAllColums(pd);//列出栏目列表
			}else{
				pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));
				list = columconfigService.findAssignTypeTopAndChildList(pd);//列出栏目列表
			}
			map.put("list", list);
			map.put("QX", Jurisdiction.getHC());
		} catch (Exception e) {
			logger.error("get the colum list occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",map);
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
		pd.put("TITLE", "栏目图片");						//标题
		pd.put("TYPE", 6);
		pd.put("NAME", file.getOriginalFilename());			//文件名
		pd.put("IMGURL", ffile + "/" + fileName);				//路径
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		pd.put("BZ", "栏目图片");							//备注
		Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
		//picturesService.save(pd);
		imageService.save(pd);
		return fileId;
	}
	
	 /**保存视频
		 * @param
		 * @throws Exception
		 */
		private String saveVideoByName(MultipartFile file,HttpServletRequest request) throws Exception{
			//转成文件上传请求
		    MultipartHttpServletRequest murequest=(MultipartHttpServletRequest)request;
		    String videoend = FilenameUtils.getExtension(file.getOriginalFilename());
			String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {
				String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
				fileName = FileUpload.fileUp(file, videoPath, this.get32UUID());//执行上传
			}else{
				logger.error("dont access the file!");
			}
			String videoId=this.get32UUID();
			PageData pd = new PageData();
			pd.put("ID", videoId);										//主键
			pd.put("VIDEO_NAME", file.getOriginalFilename());			//文件名
			pd.put("VIDEO_TITLE",murequest.getParameter("video_title"));	//标题
			pd.put("TOURL",murequest.getParameter("tourl"));				//链接
			pd.put("VIDEO_CONTENT",murequest.getParameter("video_content"));//描述
			pd.put("VIDEO_TYPE", videoend);									//保存视频的扩展名
			pd.put("VIDEO_URL", ffile + "/" + fileName);					//路径
			pd.put("CREATETIME", Tools.date2Str(new Date()));				//创建时间
			Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
			videoService.save(pd);
			return videoId;
		}
		
		/**
		 * 获取所有栏目以及类型
		 * @return
		 */
		@RequestMapping("/getColumAndTypeTree")
		@ResponseBody
		public JsonResult getColumAndTypeTree(){
			PageData pd = new PageData();
			pd = this.getPageData();
			List<Tree> list=null;
			try {
				pd.put("SITEID", RequestUtils.getSiteId(this.getRequest()));
				list = columconfigService.findColumAndTypeTree(pd);
				if(CollectionUtils.isEmpty(list)){
					return new JsonResult(Const.HTTP_OK, "OK",null);
				}
				for(Tree group:list){
					if(CollectionUtils.isEmpty(group.getChildList())){
						continue;
					}
					this.setColumType(group.getChildList());
				}
			} catch (Exception e) {
				logger.error("get the colum tree occured error!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
			}
			return new JsonResult(Const.HTTP_OK, "OK",list);
		}
		
		public void setColumType(List<Tree> childList){
			if(CollectionUtils.isEmpty(childList)){
				return;
			}
			for(Tree tree:childList){
				try {
					//如果节点类型不为栏目则直接跳过
					if(!Const.NODE_TYPE_1.equals(tree.getType())){
						continue;
					}
					this.setType(tree);
					this.setColumType(tree.getChildList());
				} catch (Exception e) {
					logger.error("find the channel type ocurred error", e);
				}
			}
		}
		
		//查询栏目下所有的分类
		public void setType(Tree colum) throws Exception{
			List<Tree> resultTypeList=new ArrayList<Tree>();
			switch((String)colum.getAttribute().get(COLUMTYPE)){
				case Const.TEMPLATE_TYPE_2://资讯模板
					List<NewMessageType> messageTypeList=this.myMessageTypeService.
						findMessage_TypeByColumnIds(colum.getId());
					this.convertMesageTypeList(messageTypeList, resultTypeList,colum.getId());
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					List<Product_Type> productTypeList=this.productTypeService.
						findProduct_TypeByColumnIds(colum.getId());
					this.convertProductTypeList(productTypeList, resultTypeList,colum.getId());
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					List<FileType> fileTypeList=this.fileTypeService.findFileTypeByColumnIds(colum.getId());
					this.convertFileTypeList(fileTypeList, resultTypeList,colum.getId());
					break;
				default:break;
			}
			if(CollectionUtils.isEmpty(colum.getChildList())){
				colum.setChildList(new ArrayList<Tree>());
			}
			colum.getChildList().addAll(resultTypeList);
		}
		
		//转换咨询分类
		public void convertMesageTypeList(List<NewMessageType> typeList,
				List<Tree> resultList,String columId){
			if(CollectionUtils.isEmpty(typeList)){
				return;
			}
			for(NewMessageType newMessageType:typeList){
				Tree tree=new Tree();
				resultList.add(tree);
				tree.setId(newMessageType.getId());
				tree.setName(newMessageType.getType_name());
				tree.setType(Const.NODE_TYPE_2);
				tree.getAttribute().put(COLUMTYPE, Const.TEMPLATE_TYPE_2);
				tree.getAttribute().put(COLUMID, columId);
				
				if(CollectionUtils.isNotEmpty(newMessageType.getChildList())){
					tree.setChildList(new ArrayList<Tree>());
					this.convertMesageTypeList(newMessageType.getChildList(),
							tree.getChildList(),columId);
				}
			}
		}
		
		//转换产品分类
		public void convertProductTypeList(List<Product_Type> typeList,
				List<Tree> resultList,String columId){
			if(CollectionUtils.isEmpty(typeList)){
				return;
			}
			for(Product_Type type:typeList){
				Tree tree=new Tree();
				resultList.add(tree);
				tree.setId(type.getId());
				tree.setName(type.getType_name());
				tree.setType(Const.NODE_TYPE_2);
				tree.getAttribute().put(COLUMTYPE, Const.TEMPLATE_TYPE_3);
				tree.getAttribute().put(COLUMID, columId);
				if(CollectionUtils.isNotEmpty(type.getChildList())){
					tree.setChildList(new ArrayList<Tree>());
					this.convertProductTypeList(type.getChildList(), tree.getChildList(),columId);
				}
			}
		}
		//转换下载分类
		public void convertFileTypeList(List<FileType> typeList,
				List<Tree> resultList,String columId){
			if(CollectionUtils.isEmpty(typeList)){
				return;
			}
			for(FileType type:typeList){
				Tree tree=new Tree();
				resultList.add(tree);
				tree.setId(type.getDownload_id());
				tree.setName(type.getDownload_name());
				tree.setType(Const.NODE_TYPE_2);
				tree.getAttribute().put(COLUMTYPE, Const.TEMPLATE_TYPE_5);
				tree.getAttribute().put(COLUMID, columId);
				if(CollectionUtils.isNotEmpty(type.getChildList())){
					tree.setChildList(new ArrayList<Tree>());
					this.convertFileTypeList(type.getChildList(), tree.getChildList(),columId);
				}
			}
		}
		
		/**
		 * 根据栏目ID查询模板详情页信息
		 * @param TEM_TYPE
		 * @return
		 */
		@RequestMapping("/findTemplateDetailByColumId")
		@ResponseBody
		public JsonResult findTemplateDetailByColumId(@RequestParam("id") String id){
			Template template=null;
			try {
				template = columconfigService.findTemplateDetailByColumId(id);
			} catch (Exception e) {
				logger.error("find the template info by colum ID occured error!",e);
				return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
			}
			return new JsonResult(Const.HTTP_OK, "OK",template);
		}
	
	/**
	 * 根据栏目ID查询栏目详情信息
	 * @param TEM_TYPE
	 * @return
	 */
	@RequestMapping("/findColumDetailByColumId")
	@ResponseBody
	public JsonResult findColumDetailByColumId(@RequestParam("columId") String columId){
		ColumConfig columConfig=null;
		try {
			columConfig = columconfigService.findColumDetailByColumId(columId);
		} catch (Exception e) {
			logger.error("find the template info by colum ID occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",columConfig);
	}
	
	/**
	 * 查询所以栏目信息
	 * @param TEM_TYPE
	 * @return
	 */
	@RequestMapping("/findColumList")
	@ResponseBody
	public JsonResult findColumList(){
		List<ColumConfig> columConfig=null;
		try {
			columConfig = columconfigService.findColumList();
		} catch (Exception e) {
			logger.error("find the template info by colum ID occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",columConfig);
	}
	
	//翻译方法(zh:中文---en:英文)
	/*
	 * 调用的是百度翻译接口、还可以翻译更多语言**/
	public String Translate(String originalText){
		TransApi api = new TransApi(Const.APP_ID, Const.SECURITY_KEY);
        String transResult = api.getTransResult(originalText, "zh", "en");
        //将string字符串转为map集合
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(transResult, map.getClass());
        //将原文和结果的集合取出来
        ArrayList<Map> map2 = (ArrayList<Map>) map.get("trans_result");
        //将结果打印出来
        return (String) map2.get(0).get("dst");
	}
}
