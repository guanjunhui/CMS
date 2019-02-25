package cn.cebest.controller.system.columcontent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Recommend;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.vo.RecommendVo;
import cn.cebest.entity.web.Banner;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.content.contentType.ContentTypeService;
import cn.cebest.service.system.contentresolve.ColumContentResolveService;
import cn.cebest.service.system.contentresolve.ColumTypeResolveService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.service.system.recommend.RecommendService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

/**
 * 栏目管理
 * @author qichangxin
 */
@Controller
@RequestMapping(value = "/columcontent_colum")
public class ColumController extends BaseController {
	
	public static final String COLUMTYPE = "columType";
	public static final String COLUMID = "columId";
	
	@Autowired
	private ColumconfigService columconfigClient;
	@Autowired
	private ProductTypeService productTypeClient;
	@Autowired
	private ContentService contentClient;
	@Autowired
	private ContentTypeService contentTypeClient;
	@Autowired
	private MyMessageTypeService myMessageTypeClient;
	@Autowired
	private EmployService employClient;
	@Autowired
	private FileTypeService fileTypeClient;
	
	@Autowired
	private ProductService productClient;
	@Autowired
	private MyMessageService mymessageClient;
	@Autowired
	private FileResourceService fileResourceClient;
	@Autowired
	private ColumContentResolveService columContentResolveClient;
	@Autowired
	private ColumTypeResolveService columTypeResolveClient;

    @Autowired
    private RecommendService recommendService;

	
	@Value("${upload.resourceLocation}")
	private String resourceLocation;

	/**
	 * 后台接口,栏目列表
	 * @return
	 */
	@RequestMapping(value="/golist")
	public ModelAndView golist(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/cloulmcontent/colum_list");
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
			pd.put("siteid", RequestUtils.getSiteId(this.getRequest()));

			if(StringUtils.isNotBlank(pd.getString("COLUM_NAME")) 
					|| StringUtils.isNotBlank(pd.getString("TEM_TYPE"))){
				list = columconfigClient.findAllListByAssignedColum(pd);//列出栏目列表
			}else{
				list = columconfigClient.findSelfAndChildList(pd);//列出栏目列表
			}
			if(list==null)
				return new JsonResult(Const.HTTP_ERROR_400,null);
			map.put("list", list);
		} catch (Exception e) {
			logger.error("get the colum list occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",map);
	}
	
	/**
	 * 跳转到banner维护界面
	 * @return
	 */
	
	@RequestMapping(value="/managebanner")
    public ModelAndView manageCanner(Page<List<Banner>> page){ 
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String id = pd.getString("columId");
		if(id != null){
			pd.put("ID", id);
		}
		page.setPd(pd);
		//查询栏目下所有的内容
		//List<Banner> bannerList=null;
		ColumConfig columConfig = null;
		try {
			columConfig = (ColumConfig)columconfigClient.findColumconfigPoById(pd);
			//contentList=this.columContentResolveClient.findContentlistPageByColumID(page,columConfig.getColumType();
            page=this.columContentResolveClient.findBannerlistPageByColumID(page,columConfig.getColumType());
		} catch (Exception e) {
			logger.error("select the content list by columId occured error!", e);
		}
		mv.setViewName("system/cloulmcontent/banner_list");
		mv.addObject("pd", pd);
        mv.addObject("page", page);
		mv.addObject("columType", columConfig.getColumType());
		mv.addObject("columName", columConfig.getColumName());
		return mv;
	}
	
	
	/**
	 * 跳转到内容维护界面
	 * @return
	 */
	@RequestMapping(value="/managecontent")
    public ModelAndView manageContent(Page<List<ContentInfoBo>> page){ 
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String id = pd.getString("columId");
		if(id != null){
			pd.put("ID", id);
		}
		//lwt增加模糊查询
		/*try {
			String message_keyword = pd.getString("keyword");//模板名称检索
			if(StringUtils.isNotEmpty(message_keyword)){
				String decode;
					decode = URLDecoder.decode(message_keyword, "UTF-8");
				pd.put("keyword",decode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		page.setPd(pd);
		//查询栏目下所有的内容
		ColumConfig columConfig = null;
		try {
			columConfig = (ColumConfig)columconfigClient.findColumconfigPoById(pd);
			//contentList=this.columContentResolveClient.findContentlistPageByColumID(page,columConfig.getColumType();
            page=this.columContentResolveClient.findContentlistPageByColumID(page,columConfig.getColumType());
		} catch (Exception e) {
			logger.error("select the content list by columId occured error!", e);
		}
		mv.setViewName("system/cloulmcontent/content_list");
		mv.addObject("pd", pd);
        mv.addObject("page", page);
		mv.addObject("columType", columConfig.getColumType());
		return mv;
	}
	
	/**
	 * 查询内容列表
	 * @return
	 */
	@RequestMapping(value="/manageContentList")
	@ResponseBody
    public Map manageContentList(Page<List<ContentInfoBo>> page){ 
		ModelAndView mv = this.getModelAndView();
		Map<String,Object> map = new HashMap<>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String id = pd.getString("columId");
		if(id != null){
			pd.put("ID", id);
		}
		page.setPd(pd);
		//查询栏目下所有的内容
		ColumConfig columConfig = null;
		try {
			columConfig = (ColumConfig)columconfigClient.findColumconfigPoById(pd);
			page.setShowCount(1000);
			//contentList=this.columContentResolveClient.findContentlistPageByColumID(page,columConfig.getColumType();
            page=this.columContentResolveClient.findContentlistPageByColumID(page,columConfig.getColumType());
		} catch (Exception e) {
			logger.error("select the content list by columId occured error!", e);
		}
		map.put("pd", pd);
		map.put("page", page);
		map.put("columType", columConfig.getColumType());
		return map;
	}
	
	/**
	 * 跳转到分类维护界面
	 * @return
	 */
	@RequestMapping(value = "/getmanageType")
	public String getmanageType(String ID ,String topColumId){
		Map map=new HashMap<>();
		PageData pd = new PageData();
		pd = this.getPageData();
		map.put("pd", pd);
		return "system/cloulmcontent/contentType_list";
	}
	
	/**
	 * 分类维护界面
	 * @return
	 */
	@RequestMapping(value="/managecontentType")
	public ModelAndView manageContentType(Page<List<TypeInfoBo>> page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		//查询栏目下所有的分类
		ColumConfig columConfig = null;
 		try {
 			columConfig = (ColumConfig)columconfigClient.findColumconfigPoById(pd);
 			//typeList=(List<TypeInfoBo>) this.columTypeResolveClient.findTypelistPageByColumID(page,columConfig.getColumType());
 		} catch (Exception e) {
 			logger.error("select the type list by columId ocuured error!", e);
 		}
		mv.setViewName("system/cloulmcontent/contentType_list");
		if(!pd.containsKey("columId")) pd.put("columId", pd.getString("ID"));
		mv.addObject("pd", pd);
		mv.addObject("page", page);
		mv.addObject("columType", columConfig.getColumType());
		return mv;
	}
	
	/**
	 * 分类界面（分页）
	 * @return
	 */
	@RequestMapping(value="/manageContentTypelistPage")
	@ResponseBody
	public Map manageContentTypelistPage(Page<List<TypeInfoBo>> page){
		Map<String,Object> map = new HashMap<>();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		//查询栏目下所有的分类
		ColumConfig columConfig = null;
 		try {
 			columConfig = (ColumConfig)columconfigClient.findColumconfigPoById(pd);
 			page=this.columTypeResolveClient.findTypelistPageByColumID(page,columConfig.getColumType());
 			page.setAjaxPageStr(page.getAjaxPageStr());
 		} catch (Exception e) {
 			logger.error("select the type list by columId ocuured error!", e);
 		}
		if(!pd.containsKey("columId")){
			pd.put("columId", pd.getString("ID"));
		}
		map.put("pd", pd);
		map.put("page", page);
		map.put("columType", columConfig.getColumType());
		return map;
	}
	
	/**
	 * 获取栏目所属分类
	 * @param TEM_TYPE
	 * @return
	 */
	@RequestMapping("/getTypeTree")
	@ResponseBody
	public JsonResult getTypeTree(@RequestParam("columId") String columId,
			@RequestParam("columType") String columType){
		List<TypeInfoBo> typeList = null;
		try {
			typeList = this.columTypeResolveClient.findTypeByColumID(columId,columType);
		} catch (Exception e) {
			logger.error("select the type list by columId ocuured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage(),null);
		}
		return new JsonResult(Const.HTTP_OK, "OK",typeList);
	}

    /**获取推荐设置的关联关系
     * @return
     */
    @RequestMapping(value="/getRecommendRelation")
    @ResponseBody
    public JsonResult getRoleIdsByUserId(@RequestParam(value="contentId") String contentId){
        List<Recommend> list=null;
        try{
            list=recommendService.selectByContentId(contentId);
        } catch(Exception e){
            logger.error("get the recommend setting relation occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
        }
        return new JsonResult(Const.HTTP_OK, "OK",list);
    }
    
    /**
     * 保存推荐设置
     * @param RecommendVo recommendVo
     * @return
     */
    @RequestMapping("saveRecommend/{contentId}")
    @ResponseBody
    public JsonResult saveRecommend(@RequestBody RecommendVo[] recommendVo,
            @PathVariable("contentId") String contentId) {
        try {
            recommendService.deleteByContentId(contentId);
        } catch (Exception e) {
            logger.error("delete the product recommend setting data before save new setting occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
        }
        
        if(recommendVo!=null&&recommendVo.length>0){
            List<Recommend> recommendList = new ArrayList<Recommend>();
            for(RecommendVo vo:recommendVo){
                Recommend recommend = new Recommend();
                recommendList.add(recommend);
                recommend.setId(this.get32UUID());
                if(Const.NODE_TYPE_1.equals(vo.getType())){//栏目
                    recommend.setContentId(vo.getContentId());
                    recommend.setType(vo.getColumType());
                    recommend.setTypeId(StringUtils.EMPTY);
                    recommend.setColumId(vo.getId());
                }else if(Const.NODE_TYPE_2.equals(vo.getType())){//分类
                    recommend.setContentId(vo.getContentId());
                    recommend.setType(vo.getColumType());
                    recommend.setTypeId(vo.getId());
                    recommend.setColumId(vo.getColumId());
                }
            }
            //批量插入
            try {
                recommendService.saveBatch(recommendList);
            } catch (Exception e) {
                logger.error("save the product recommend data occured error!", e);
                return new JsonResult(Const.HTTP_ERROR, e.getMessage());
            }
        }
        return new JsonResult(Const.HTTP_OK, "OK");
    }
	
	@RequestMapping(value = "/updataStatus")
	@ResponseBody
	public JsonResult updateStatus(String[] ids, String status,String columType) {
		PageData pd = new PageData();
		pd.put("ids", ids);
		pd.put("status", status);
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					this.contentClient.updateStatusByIds(pd);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					this.mymessageClient.updateStatus(pd);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					productClient.updataStatus(pd);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					employClient.updateStatusByIds(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					this.fileResourceClient.updateStatusByIds(pd);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("update the content status occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	@RequestMapping(value = "/updateTypeStatus")
	@ResponseBody
	public JsonResult updateTypeStatus(String[] ids, String status,String columType) {
		PageData pd = new PageData();
		pd.put("ids", ids);
		pd.put("status", status);
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					this.contentTypeClient.updateTypeStatusByIds(pd);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					this.myMessageTypeClient.updateStatus(pd);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					productTypeClient.updataTypeStatus(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					this.fileTypeClient.updateStatusByIds(pd);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("update the content status occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 刪除内容
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("deleteType")
	@ResponseBody
	public JsonResult deleteType(String[] ids,String columType) {
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					contentTypeClient.delete(ids);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					myMessageTypeClient.delete(ids);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					productTypeClient.delete(ids);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					this.fileTypeClient.deleteFileType(ids);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("delete the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	/**
	 * 置顶
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateTop")
	@ResponseBody
	public JsonResult updataTop(String[] ids, String top,String columType) {
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					Content content = new Content();
					content.setTop_time(Const.YES.equals(top)?DateUtil.getTime():"");//取消置顶时，将置顶时间设置为空
					content.setContentRelevantIdList(ids);
					content.setTop(top);
					content.setUpdateTime(DateUtil.getTime());
					contentClient.updataRecommendAndTopAndHot(content);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					NewMessage message = new NewMessage();
					message.setTop_time(Const.YES.equals(top)?DateUtil.getTime():null);//取消置顶时，将置顶时间设置为空
					message.setMessageRelevantIdList(ids);
					if("0".equals(top)){
						message.setTop(top);
					}else{
						message.setTop(top);
					}
					mymessageClient.updataRecommendAndTopAndHot(message);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					Product product = new Product();
					product.setTop_time(Const.YES.equals(top)?DateUtil.getTime():"");//取消置顶时，将置顶时间设置为空
					product.setProductRelevantIdList(ids);
					product.setTop(top);
					productClient.updataRecommendAndTopAndHot(product);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					PageData pd = new PageData();
					pd = this.getPageData();
					pd.put("IFTOP", top);
					pd.put("ids", ids);
					if(Const.YES.equals(top)) pd.put("TOP_TIME", DateUtil.getTime());
					employClient.top(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					FileResources file=new FileResources();
					file.setTop_time(Const.YES.equals(top)?DateUtil.getTime():"");//取消置顶时，将置顶时间设置为空
					file.setFileids(ids);
					file.setTop(top);
					file.setUpdate_time(DateUtil.getTime());// 修改时间
					this.fileResourceClient.updataRecommendAndTopAndHot(file);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("top the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 热设置
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateHot")
	@ResponseBody
	public JsonResult updateHot(String[] ids, String hot,String columType) {
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					Content content = new Content();
					content.setHot_time(DateUtil.getTime());
					content.setContentRelevantIdList(ids);
					content.setHot(hot);
					content.setUpdateTime(DateUtil.getTime());
					contentClient.updataRecommendAndTopAndHot(content);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					NewMessage message = new NewMessage();
					if("0".equals(hot)){
						message.setHot_time(null);
						message.setHot(hot);
					}else{
						message.setHot_time(DateUtil.getTime());
						message.setHot(hot);
					}
					message.setMessageRelevantIdList(ids);
					mymessageClient.updataRecommendAndTopAndHot(message);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					Product product = new Product();
					product.setHot_time(DateUtil.getTime());
					product.setProductRelevantIdList(ids);
					product.setHot(hot);
					productClient.updataRecommendAndTopAndHot(product);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					PageData pd = new PageData();
					pd = this.getPageData();
					pd.put("IFHOT", hot);
					pd.put("ids", ids);
					if(Const.YES.equals(hot)) pd.put("HOT_TIME", DateUtil.getTime());
					employClient.hot(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					FileResources file=new FileResources();
					file.setHot_time(DateUtil.getTime());
					file.setFileids(ids);
					file.setHot(hot);
					file.setUpdate_time(DateUtil.getTime());// 修改时间
					this.fileResourceClient.updataRecommendAndTopAndHot(file);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("top the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	
	
	/**
	 * 推荐
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateRecommend")
	@ResponseBody
	public JsonResult updateRecommend(String[] ids, String recommend,String columType) {
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					Content content = new Content();
					content.setRecommend_time(DateUtil.getTime());
					content.setContentRelevantIdList(ids);
					content.setRecommend(recommend);
					content.setUpdateTime(DateUtil.getTime());
					contentClient.updataRecommendAndTopAndHot(content);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					NewMessage message = new NewMessage();
					if("0".equals(recommend)){
						message.setRecommend_time(null);
						message.setRecommend(recommend);
					}else{
						message.setRecommend_time(DateUtil.getTime());
						message.setRecommend(recommend);
					}
					message.setMessageRelevantIdList(ids);
					mymessageClient.updataRecommendAndTopAndHot(message);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					Product product = new Product();
					product.setRecommend_time(DateUtil.getTime());
					product.setProductRelevantIdList(ids);
					product.setRecommend(recommend);
					productClient.updataRecommendAndTopAndHot(product);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					PageData pd = new PageData();
					pd = this.getPageData();
					pd.put("IFRECOMMEND", recommend);
					pd.put("ids", ids);
					if(Const.YES.equals(recommend)) pd.put("RECOMMEND_TIME", DateUtil.getTime());
					employClient.recommend(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					FileResources file=new FileResources();
					file.setRecommend_time(DateUtil.getTime());
					file.setFileids(ids);
					file.setRecommend(recommend);
					file.setUpdate_time(DateUtil.getTime());// 修改时间
					this.fileResourceClient.updataRecommendAndTopAndHot(file);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("top the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 排序值
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateSort")
	@ResponseBody
	public JsonResult updateSort(String id, Integer sort,String columType) {
		PageData pd = new PageData();
		pd.put("id", id);
		pd.put("sort", sort);
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					contentClient.updateSort(pd);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					mymessageClient.updateSort(pd);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					productClient.updateSort(pd);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					employClient.updateSort(pd);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					this.fileResourceClient.updateSort(pd);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("top the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 刪除内容
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete(String[] ids,String columType) {
		try {
			switch(columType){
				case Const.TEMPLATE_TYPE_1://内容模板
					contentClient.deleteAll(ids);
					break;
				case Const.TEMPLATE_TYPE_2://资讯模板
					mymessageClient.deleteMessage(ids);
					break;
				case Const.TEMPLATE_TYPE_3://产品模板
					productClient.deleteProduct(ids);
					break;
				case Const.TEMPLATE_TYPE_4://招聘模板
					employClient.delAll(ids);
					break;
				case Const.TEMPLATE_TYPE_5://下载模板
					this.fileResourceClient.deleteFile(ids);
					break;
				default:break;
			}
		} catch (Exception e) {
            logger.error("delete the content occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
        return new JsonResult(Const.HTTP_OK, "OK");
	}

}
