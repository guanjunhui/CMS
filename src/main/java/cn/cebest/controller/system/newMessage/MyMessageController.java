package cn.cebest.controller.system.newMessage;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.newMessage.MessageExtendWords;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.FileDownload;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.ObjectExcelRead;
import cn.cebest.util.ObjectExcelView;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;

/**
 * 资讯管理
 * 
 * @author lwt
 *
 */
@Controller
@RequestMapping(value = "/mymessage")
public class MyMessageController extends BaseController {
	String menuUrl = "mymessage/list.do"; // 菜单地址(权限用)

	@Resource(name = "MyMessageService")
	private MyMessageService messageService;

	@Resource(name = "fhlogService")
	private FHlogManager FHLOG;
	
	@Resource(name="seoService")
	private SeoService seoService;


	/**
	 * 入口方法
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions("mymessage:list")
	public String index(Map<String, Object> map, Page page) throws Exception {
		//获取当前的request请求方式并设置编码格式,防止乱码
		HttpServletRequest request = this.getRequest();
		request.setCharacterEncoding("UTF-8");
		// 用page传参
		PageData pd = this.getPageData();
		String message_keyword = pd.getString("message_keyword");//模板名称检索
		if(StringUtils.isNotEmpty(message_keyword)){
			String decode= URLDecoder.decode(message_keyword, "UTF-8");
			pd.put("message_keyword",decode);
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		messageService.findMessageToList(map, page);
		return "system/message/message_list";// 转发到资讯列表
	}
	
	/**
	 * 展示排序
	 * 
	 * @param map
	 * @param page
	 * @return
	 */
	@RequestMapping("sortlistPage")
	@RequiresPermissions("mymessage:sortlistPage")
	public String sortlistPage(Map<String, Object> map, Page page) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			logBefore(logger, Jurisdiction.getUsername() + "资讯排序");
			messageService.sortlistPage(map, page);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "资讯排序出现异常");
			e.printStackTrace();
		}
		return "system/message/message_list";// 转发到
	}

	/**
	 * 跳转到添加页面的方法
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getTree")
	@RequiresPermissions("mymessage:getTree")
	@ResponseBody
	public Map<String, Object> getTree(String type) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加message页面");	
		Map<String, Object> map=new HashMap<>();
		PageData pd = this.getPageData();// 此时pd是没有数据的,表结构定下以后手动填一些参数
		pd.put("TEMPLATE_TYPE_2", Const.TEMPLATE_TYPE_2);
		pd.put("type", type);
		map.put("templateList", messageService.toAddFind(pd));
		return map;// 
	}
	/**
	 * 跳转到资讯添加页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toAdd")
	@RequiresPermissions("mymessage:toAdd")
	public ModelAndView toAdd() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加message页面");
		ModelAndView modelAndView=new ModelAndView();
		PageData pd = this.getPageData();
		modelAndView.addObject("pd", pd);
		modelAndView.setViewName("system/message/message_add");
		return modelAndView;// 转发到添加页
	}
	//==========================================================================
	/**
	 * 根据产品类型查询该类型下的所有产品
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findMessageBytypeId")
	@RequiresPermissions("mymessage:findMessageBytypeId")
	@ResponseBody
	public List<NewMessage> findMessageBytypeId(String[] id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加message页面");
		return messageService.findMessageBytypeId(id);
	}
	
	/**
	 * 根据资讯类型查询该类型下的所有资讯
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findMessagetypeById")
	@RequiresPermissions("mymessage:findMessagetypeById")
	@ResponseBody
	public List<NewMessage> findMessagetypeById(String[] id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加message页面");
		return messageService.findMessageTypeById(id);
	}
	/**
	 * 添加的方法,修改可能也用这个方法
	 * 
	 * @param mess
	 * @param file
	 * @return
	 * 参数介绍:第一个参数NewMessage为咨询对象,第二个参数:file封面图片,第三个参数相关资讯的id集合,
	 * 第四个参数资讯类型id的集合
	 * 
	 */
/*	@RequestMapping(value = "/add")
	@RequiresPermissions("mymessage:add")
	@ResponseBody
	public Map<String, Object> add(NewMessage mess, MultipartFile file,
			@RequestParam(value = "ids", required = false) String[] ids,
			 String[] messagetypeids) {
		//图片进行遍历
		List<MultipartFile> images=mess.getImages();
		//对视频文件进行遍历
		List<MultipartFile> films=mess.getFilms();
		//详细视频遍历
		List<Video> videos = mess.getVideos();
		//详细图片遍历
		List<Image> pictures=mess.getPictures();
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增message");
		mess.setId(this.get32UUID());
		mess.setCreated_time(DateUtil.getTime());
		mess.setMessageRelevantIdList(ids);
		mess.setMessagetypeids(messagetypeids);
		if (mess.getHot() == null || "".equals(mess.getHot())) {
			mess.setHot("0");
		}
		if (mess.getRecommend() == null || "".equals(mess.getRecommend())) {
			mess.setRecommend("0");
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		try {
			//保存封面图片
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行封面图片上传

				logBefore(logger, Jurisdiction.getUsername() + "新增图片");
				pd.put("messageImageId", this.get32UUID()); // 主键
				mess.setSurface_image(pd.getString("messageImageId"));//保存封面图片的id
				pd.put("messageTitle", "资讯封面图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "资讯封面图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
				pd.put("flag", flag);
				messageService.saveImages(pd);
			}
			//保存上传的多张图片
			if (null != images && !images.isEmpty()) {
				int count=0;
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增资讯图片");
					pd.put("messageImageId", this.get32UUID()); // 主键
					mess.getListImage().add(pd.getString("messageImageId"));//保存多个图片的id
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("messageTitle", pictures.get(count).getTitle()); // 标题
					pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(count).getBz()); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					messageService.saveImages(pd);
					count++;
				}
			}
			//保存多个上传的视频
			if (null != films && !films.isEmpty()) {
				int num=0;
				for (MultipartFile video : films) {
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增视频");
					pd.put("messageVideoId", this.get32UUID()); // 主键
					mess.getListVideo().add(pd.getString("messageVideoId"));//保存多个视频的id
					pd.put("messageVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("messageVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					messageService.saveVideos(pd);
					num++;
				}
			}
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			mess.setSite_id(siteId);
			mess.setCreated_time(DateUtil.getTime());
			pd.put("flag", flag);
			messageService.save(mess, pd); // 执行保存资讯
			map.put("success", true);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增资讯出现异常");
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE)+ videoPath);//删除视频
			}
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}*/

	@RequestMapping(value = "/add")
	@RequiresPermissions("mymessage:add")
	@ResponseBody
	public ModelAndView add(NewMessage mess, MultipartFile file,
			@RequestParam(value = "ids", required = false) String[] ids,String[] columnids,
			 String[] messagetypeids,int is_add,String[] columtype,String[] valueText,String[] value_Type,
			 String[] names,String[] wordTypes,String type_id,String[] sort,
			 @RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId,@RequestParam("releaseTime") String releaseTime) {
		ModelAndView mav=new ModelAndView();
		
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(releaseTime != null && !"".equals(releaseTime)){
			Date date = new Date();
			try {
				date = sDateFormat.parse(releaseTime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mess.setRelease_time(date);
		}else{
			mess.setRelease_time(new Date());
		}
		//设置资讯类型id
		/*mess.setType_id(type_id);*/
		//把获得的栏目与类型之间的关系设置给mess
		mess.setColumtype(columtype);
		//只设置栏目给mess
		mess.setColumnids(columnids);
//		//图片进行遍历（禁止对象中包含IO流属性）
 		List<MultipartFile> images=mess.getImages();
//		//对视频文件进行遍历
 		List<MultipartFile> films=mess.getFilms();
		//详细视频遍历
		List<Video> videos = mess.getVideos();
		//详细图片遍历
		List<Image> pictures=mess.getPictures();
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增message");
		mess.setId(this.get32UUID());
		mess.setCreated_time(DateUtil.getTime());
		mess.setUpdate_time(DateUtil.getTime());
		mess.setMessageRelevantIdList(ids);
		mess.setMessagetypeids(messagetypeids);
		if (mess.getHot() == null || "".equals(mess.getHot())) {
			mess.setHot("0");
		}
		if (mess.getRecommend() == null || "".equals(mess.getRecommend())) {
			mess.setRecommend("0");
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		try {
			//保存封面图片
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行封面图片上传
				logBefore(logger, Jurisdiction.getUsername() + "新增图片");
				pd.put("messageImageId", this.get32UUID()); // 主键
				mess.setSurface_image(pd.getString("messageImageId"));//保存封面图片的id
				pd.put("messageTitle", "资讯封面图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				pd.put("bz", "资讯封面图片管理处上传"); // 备注
//				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				flag = true;
				pd.put("flag", flag);
				messageService.saveImages(pd);
			}
			//保存上传的多张图片
			if (null != images && images.size()>0) {
				int count=0;
				for (MultipartFile image : images) {
					pd.put("name",image.getOriginalFilename());
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增资讯图片");
					pd.put("messageImageId", this.get32UUID()); // 主键
					mess.getListImage().add(pd.getString("messageImageId"));//保存多个图片的id
					pd.put("ImageTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("messageTitle", pictures.get(count).getTitle()); // 标题
					pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(count).getBz()); // 备注
					pd.put("forder",pictures.get(count).getForder());//排序
					pd.put("tourl",pictures.get(count).getTourl());//外部链接
//					Watermark.setWatemark(PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile + "/" + fileName);// 加水印
					messageService.saveImages(pd);
					messageService.saveContentImages(pd);
					count++;
				}
			}
			//保存多个上传的视频
			if (null != films && films.size()>0) {
				int num=0;
				for (MultipartFile video : films) {
					pd.put("name",video.getOriginalFilename());
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增视频");
					pd.put("messageVideoId", this.get32UUID()); // 主键
					mess.getListVideo().add(pd.getString("messageVideoId"));//保存多个视频的id
					pd.put("messageVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("messageVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					pd.put("forder", videos.get(num).getForder());//排序
					pd.put("tourl",videos.get(num).getTourl());//外部链接
//					Watermark.setWatemark(PathUtil.getUploadPath() + Const.FILEPATHFILE + ffile + "/" + fileName);// 加水印
					messageService.saveVideos(pd);
					messageService.saveContentVideos(pd);
					num++;
				}
			}
			//保存seo关键字和seo描述
			if(mess.getSeodescription()!=null && mess.getSeokeywords()!=null){
				pd.put("ID", this.get32UUID());
				pd.put("CREATE_TIME", DateUtil.getTime());
				pd.put("MASTER_ID", mess.getId());
				pd.put("SEO_TITLE", mess.getSeo_title());
				pd.put("SEO_KEYWORDS", mess.getSeokeywords());
				pd.put("SEO_DESCRIPTION", mess.getSeodescription());
				seoService.insetSeo(pd);	
			}
			//保存seo结束
			//扩展字段处理开始=================
			List<MessageExtendWords> pvtList = new ArrayList<MessageExtendWords>();
			if (value_Type != null) {
				for (int i = 0; i < value_Type.length; i++) {
					MessageExtendWords pvt = new MessageExtendWords();
					pvt.setId(value_Type[i]);
					pvt.setName(names[i]);
					pvt.setValue(valueText[i]);
					pvt.setFieldtype(wordTypes[i]);
					if(sort[i]!=null && !"".equals(sort[i])){
						pvt.setSort(Integer.parseInt(sort[i]));
					}
					pvtList.add(pvt);
				}
			}
			MyCompartor mc = new MyCompartor();//创建比较器对象
		    Collections.sort(pvtList,mc);     //按照age升序 22，23，
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(pvtList);
			mess.setValuetext(json);
			//扩展字段处理结束====================
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			mess.setSite_id(siteId);
			pd.put("flag", flag);
			mess.setPd(pd);
			mess.setColumid(columId);
			messageService.save(mess); // 执行保存资讯
			//map.put("success", true);
			mav.addObject("result", new JsonResult(200,"添加资讯成功!"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "新增资讯出现异常");
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHFILE) + videoPath);//删除视频
			}
			//map.put("success", false);
			mav.addObject("result", new JsonResult(500,"添加资讯失败!"));
			e.printStackTrace();
		}
		if(is_add==1){
			mav.setViewName("redirect:toAdd.do?columId="+columId+"&topColumId="+topColumId);
			return mav;
		}
//		mav.setViewName("redirect:sortlistPage.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}

	/**
	 * 刪除资讯的方法
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("delete")
	@RequiresPermissions("mymessage:delete")
	public String deleteMessage(String[] id) {
		try {
			messageService.deleteMessage(id);

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return "redirect:list.do";
	}

	/**
	 * 跳转到修改页面的方法
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toEdit")
	@RequiresPermissions("mymessage:toEdit")
	public String toEdit(Map<String, Object> map, String id,String type) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加message页面");
		PageData pd = this.getPageData();// 此时pd是没有数据的,表结构定下以后手动填一些参数
		pd.put("TEMPLATE_TYPE_2", Const.TEMPLATE_TYPE_2);
		pd.put("type", type);
		messageService.toAddFind(pd);
		map.put("relevantList", messageService.findMessageRelevant(id));
		NewMessage mess = messageService.findDetailMessageById(id);
		ObjectMapper objectMapper = new ObjectMapper();
		if (mess.getValuetext() != null && !"".equals(mess.getValuetext())) {
			@SuppressWarnings("unchecked")
			List<MessageExtendWords> pvtList = objectMapper.readValue(mess.getValuetext(), List.class);
			mess.setPvtlist(pvtList);
		}		
		map.put("mess", mess);
		map.put("image", messageService.selectImageByMessageId(id));
		List<Image> selectImages = messageService.selectImages(id);
		if(selectImages.get(0)!=null){
			map.put("pictlist", selectImages);
		}
		List<Video> listvideo= messageService.selectVideosByMessageId(id);
		if(listvideo.get(0)!=null){
			map.put("videlist", listvideo);
		}
		pd.put("MASTER_ID", id);
		map.put("pd", pd);
		map.put("seo", seoService.querySeoForObject(pd));
		map.put("columns",messageService.findColumConfigByMessageId(id));
		return "system/message/message_edit";// 转发到添加页
	}

	/**
	 * 查询相同类型的资讯,点击相关产品选择按钮时接收ajax请求
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryProduct")
	@RequiresPermissions("mymessage:queryProduct")
	@ResponseBody
	public List<PageData> queryProductByCode(String id) throws Exception {
		return messageService.findMessageRelevantBycode(id);
	}

	/**
	 * 执行修改的方法
	 * 
	 * @param mess
	 * @param file
	 * @return
	 */
	/*@RequestMapping(value = "/update")
	@RequiresPermissions("mymessage:update")
	@ResponseBody
	public Map<String, Object> update(NewMessage mess, MultipartFile file,
			@RequestParam(value = "ids", required = false) String[] ids,
			 String[] messagetypeids) {
		//图片进行遍历=========================
		List<MultipartFile> images=mess.getImages();
		//对视频文件进行遍历============================
		List<MultipartFile> films=mess.getFilms();
		//详细视频遍历------------------------------
		//List<Video> videos = mess.getVideos();
		
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "修改message");
		mess.setUpdate_time(DateUtil.getTime());
		mess.setMessageRelevantIdList(ids);
		mess.setMessagetypeids(messagetypeids);
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			mess.setSite_id(siteId);
			//保存封面图片
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行封面图片上传
				flag = true;
				
				logBefore(logger, Jurisdiction.getUsername() + "修改图片");
				pd.put("messageTitle", "资讯封面图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				//pd.put("surface_image", mess.getSurface_image());//获取资讯类型id
				pd.put("bz", "封面图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
			}
			//修改上传的多张图片
			if (null != images && !images.isEmpty()) {
				int count=0;
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					flag = true;
					
					logBefore(logger, Jurisdiction.getUsername() + "修改图片"+(++count));
					pd.put("messageTitle", "资讯图片"); // 标题
					pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", "资讯图片管理处上传"); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
				}
			}
			//修改多个上传的视频
			if (null != films && !films.isEmpty()) {
				for (MultipartFile video : films) {
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					flag = true;
					
					logBefore(logger, Jurisdiction.getUsername() + "修改视频");
					pd.put("messageVideoTitle", "资讯视频"); // 标题
					pd.put("messageVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent", "资讯视频修改演示"); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile + "/" + fileName);// 加水印
				}
			}
			mess.setCreated_time(DateUtil.getTime());
			pd.put("flag", flag);
			messageService.updateMessage(mess, pd); // 执行保存资讯
			map.put("success", true);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改资讯出现异常");
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE)+ videoPath);//删除视频
			}
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}*/
	@RequestMapping(value = "/update")
	@RequiresPermissions("mymessage:update")
	@ResponseBody
	public ModelAndView update(NewMessage mess, MultipartFile file,
			@RequestParam(value = "ids", required = false) String[] ids,String[] columnids,
			 String[] messagetypeids,int is_add,String[] columtype,String[] valueText,String[] value_Type,String[] names,String[] wordTypes,
			 @RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId,@RequestParam("releaseTime") String releaseTime) {
		ModelAndView mev=new ModelAndView();
		
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(releaseTime != null && !"".equals(releaseTime)){
			Date date = new Date();
			try {
				date = sDateFormat.parse(releaseTime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mess.setRelease_time(date);
		}
		
		//把获得的栏目与类型之间的关系设置给mess
		mess.setColumtype(columtype);
		//只设置栏目给mess
		mess.setColumnids(columnids);
		//新增图片集合
		List<MultipartFile> images=mess.getAddimages();
		//新增视频集合
		List<MultipartFile> films=mess.getAddfilms();
		//新增详细视频集合
		List<Video> videos = mess.getAddvideos();
		//新增详细图片集合
		List<Image> pictures=mess.getAddpictures();
		//获得所有原有原有图片id集合
		List<String> oldimageIds = mess.getListImage();
		List<String> imageIds=new ArrayList<String>();
		if(oldimageIds!=null && oldimageIds.size()>0){
			for (String string : oldimageIds) {
				if(string!=null){
					imageIds.add(string);
				}
			}
		}
 		//获得所有原有视频的id集合
		List<String> oldvideoIds = mess.getListVideo();
		List<String> videoIds=new ArrayList<String>();
		if(oldvideoIds!=null && oldvideoIds.size()>0){
			for (String string :oldvideoIds) {
				if(string!=null){
					videoIds.add(string);
				}
			}
		}
		//删除在原有页面中不存在的图片
		List<String> arrIds=new ArrayList<String>();
		try {
			//获得所有图片的id集合
			List<Image> ima= messageService.findImagesBymessageid(mess.getId());
			if(ima!=null && ima.size()>0){
				for (Image im: ima) {					
					arrIds.add(im.getImageId());
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		//如果用户没做修改下面的不会执行,若在原来的基础上删除了一个,则会执行下面的语句
		if(arrIds!=null && arrIds.size()>0 && imageIds!=null && imageIds.size()>0){
			try {
				for (String imaId :arrIds) {
					if(!(imageIds.contains(imaId))){
						Image listImage = messageService.selectImagePathByid(imaId);// 查询图片路径
						if (listImage != null) {
							String filePath =listImage.getImgurl();
							if (filePath != null && Tools.notEmpty(filePath.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片文件
							}
						}				
						messageService.deleteImage(imaId);
					}
				}	
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除图片文件及详情时出现异常!");
			}
			
		}
		//如果用户将图片全部删除则删除该资讯下的所有图片
		if(imageIds.size()==0){
			try {
				for (String imd: arrIds) {					
					Image listImage = messageService.selectImagePathByid(imd);// 查询图片路径
					if (listImage != null) {
						String filePath =listImage.getImgurl();
						if (filePath != null && Tools.notEmpty(filePath.trim())) {
							DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片文件
						}
					}				
					messageService.deleteImage(imd);
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除全部图片文件及详情时出现异常!");
			}
		}

		
		//删除在原有页面中不存在的视频
		List<String> arrVIds=new ArrayList<String>();
		try {
			//获得所有视频的id集合
			List<Video> vid= messageService.findVideosBymessageid(mess.getId());
			if(vid!=null && vid.size()>0){
				for (Video vi: vid) {				
					arrVIds.add(vi.getId());
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		//如果用户没做修改下面的不会执行,若在原来的基础上删除了一个,则会执行下面的语句
		if(arrVIds!=null && arrVIds.size()>0 && videoIds!=null && videoIds.size()>0){
			try {
				for (String vd : arrVIds) {	
					if(!(videoIds.contains(vd))){					
						Video dvideo =messageService.selectVideoPathByid(vd);// 查询视频路径
						if (dvideo != null) {
							String filePath =dvideo.getVideo_url();
							if (filePath != null && Tools.notEmpty(filePath.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除视频文件
							}
						}
						messageService.deleteVideo(vd);			
					}				
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除视频文件及详情时出现异常!");
			}
		}
		//如果用户将视频全部删除则删除该资讯下的所有视频
		if(videoIds.size()==0){
			try {
				for (String vd : arrVIds) {
					Video dvideo =messageService.selectVideoPathByid(vd);// 查询视频路径
					if (dvideo != null) {
						String filePath =dvideo.getVideo_url();
						if (filePath != null && Tools.notEmpty(filePath.trim())) {
							DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除视频文件
						}
					}
					messageService.deleteVideo(vd);
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除全部视频文件及详情时出现异常!");
			}
		}
		

		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();

		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "修改message");
		mess.setUpdate_time(DateUtil.getTime());
		mess.setMessageRelevantIdList(ids);
		mess.setMessagetypeids(messagetypeids);
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		//更新原有的图片
		List<Image> oldpictures2 = mess.getPictures();
		List<Image> pictures2=new ArrayList<Image>();
		if(oldpictures2!=null && oldpictures2.size()>0){
			for (Image image : oldpictures2) {
				if(image.getImageId()!=null){
					pictures2.add(image);
				}
			}
		}
		List<MultipartFile> oldimages2 = mess.getImages();
		List<MultipartFile> images2=new ArrayList<MultipartFile>();
		if(oldimages2!=null && oldimages2.size()>0){
			for (MultipartFile multipartFile : oldimages2) {
				if(multipartFile!=null){
					images2.add(multipartFile);
				}
			}	
		}
		if(pictures2!=null && pictures2.size()>0){
			try {
				for (int i = 0; i < pictures2.size(); i++) {
					//上传更新文件
					if(images2.get(i)!=null && !images2.get(i).isEmpty()){
						Image dimage =messageService.selectImagePathByid(pictures2.get(i).getImageId());// 查询视频路径
						if (dimage != null) {
							String path =dimage.getImgurl();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(images2.get(i), filePath, this.get32UUID());
						pictures2.get(i).setImgurl(ffile + "/" + fileName);
						pictures2.get(i).setName(images2.get(i).getOriginalFilename());
					}
					//更新图片信息
					messageService.updateImageById(pictures2.get(i));;
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//更新原有的视频
		List<Video> oldvideos2 = mess.getVideos();
		List<Video> videos2=new ArrayList<Video>();
		if(oldvideos2!=null && oldvideos2.size()>0){			
			for (Video video : oldvideos2) {
				if(video.getId()!=null){
					videos2.add(video);
				}
			}
		}
		List<MultipartFile> oldfilms2 = mess.getFilms();
		List<MultipartFile> films2=new ArrayList<MultipartFile>();
		if(oldfilms2!=null && oldfilms2.size()>0){
			for (MultipartFile multipartFile : oldfilms2) {
				if(multipartFile!=null){
					films2.add(multipartFile);
				}
			}	
		}
		if(videos2!=null && videos2.size()>0){
			try {
				for (int i = 0; i < videos2.size(); i++) {
					//上传更新文件
					if(films2.get(i)!=null && !films2.get(i).isEmpty()){
						Video dvideo =messageService.selectVideoPathByid(videos2.get(i).getId());// 查询视频路径
						if (dvideo != null) {
							String path =dvideo.getVideo_url();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(PathUtil.getClasspath() +Const.FILEPATHFILE + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(films2.get(i), filePath, this.get32UUID());
						videos2.get(i).setVideo_url(ffile + "/" + fileName);
						videos2.get(i).setName(films2.get(i).getOriginalFilename());
					}
					//更新视频信息
					messageService.updateVideoById(videos2.get(i));
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			mess.setSite_id(siteId);
			//保存封面图片
			if (null != file && !file.isEmpty()) {
				fileName = FileUpload.fileUp(file, filePath, this.get32UUID()); // 执行封面图片上传
				flag = true;
				
				logBefore(logger, Jurisdiction.getUsername() + "修改图片");
				pd.put("messageTitle", "资讯封面图片"); // 标题
				pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
				//pd.put("surface_image", mess.getSurface_image());//获取资讯类型id
				pd.put("bz", "封面图片管理处上传"); // 备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
			}
			//保存上传的多张图片
			if (null != images && !images.isEmpty()) {
				int count=0;
				for (MultipartFile image : images) {
					pd.put("name",image.getOriginalFilename());
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增资讯图片");
					pd.put("messageImageId", this.get32UUID()); // 主键
					mess.getListImage().add(pd.getString("messageImageId"));//保存多个图片的id
					pd.put("ImageTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("messageTitle", pictures.get(count).getTitle()); // 标题
					pd.put("messageImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(count).getBz()); // 备注
					pd.put("forder",pictures.get(count).getForder());//排序
					pd.put("tourl",pictures.get(count).getTourl());//外部链接
					Watermark.setWatemark(PathUtil.getUploadPath() + Const.FILEPATHIMG + ffile + "/" + fileName);// 加水印
					messageService.saveImages(pd);
					count++;
				}
			}
			//保存多个上传的视频
			if (null != films && !films.isEmpty()) {
				int num=0;
				for (MultipartFile video : films) {
					pd.put("name",video.getOriginalFilename());
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增视频");
					pd.put("messageVideoId", this.get32UUID()); // 主键
					mess.getListVideo().add(pd.getString("messageVideoId"));//保存多个视频的id
					pd.put("messageVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("messageId", mess.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("messageVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					pd.put("forder", videos.get(num).getForder());//排序
					pd.put("tourl",videos.get(num).getTourl());//外部链接
					Watermark.setWatemark(PathUtil.getUploadPath() + Const.FILEPATHFILE + ffile + "/" + fileName);// 加水印
					messageService.saveVideos(pd);
					num++;
				}
			}
			//修改seo关键字和seo描述
			if(mess.getSeodescription()!=null && mess.getSeokeywords()!=null){
				pd.put("MASTER_ID", mess.getId());
				pd.put("SEO_TITLE", mess.getSeo_title());
				pd.put("SEO_KEYWORDS", mess.getSeokeywords());
				pd.put("SEO_DESCRIPTION", mess.getSeodescription());
				seoService.updateSeo(pd);
			}
			//修改seo结束
			//扩展字段处理开始=================
			List<MessageExtendWords> pvtList = new ArrayList<MessageExtendWords>();
			if (value_Type != null) {
				for (int i = 0; i < value_Type.length; i++) {
					MessageExtendWords pvt = new MessageExtendWords();
					pvt.setId(value_Type[i]);
					pvt.setName(names[i]);
					pvt.setValue(valueText[i]);
					pvt.setFieldtype(wordTypes[i]);
					pvtList.add(pvt);
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(pvtList);
			mess.setValuetext(json);
			//扩展字段处理结束====================
			pd.put("flag", flag);
			mess.setColumid(columId);
			messageService.updateMessage(mess, pd); // 执行保存资讯
			//map.put("success", true);
			mev.addObject("result", new JsonResult(200,"修改资讯成功!"));
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改资讯出现异常");
			if (flag) {
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除图片
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE)+ videoPath);//删除视频
			}
			//map.put("success", false);
			mev.addObject("result", new JsonResult(500,"修改资讯失败!"));
			e.printStackTrace();
		}
		if(is_add==1){
			mev.setViewName("redirect:toAdd.do?ID="+columId+"&topColumId="+topColumId);
			return mev;
		}
		mev.setViewName("redirect:/manage/columcontent_colum/managecontent.do?ID="+columId+"&topColumId="+topColumId);
		return mev;
	}
	
	/**
	 * 通过id更新资讯状态信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateStatus")
	@RequiresPermissions("mymessage:updateStatus")
	public String updateTypeStatus(){
		logBefore(logger, Jurisdiction.getUsername() + "修改资讯状态");
		/*if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {//验证权限
			return null;
		}*/
		PageData pd = this.getPageData();
		try {
			messageService.updateStatus(pd);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改资讯状态失败!");
			e.printStackTrace();
		}
		return "redirect:list.do";
	}
	//=================================================下面为导入导出excel功能================================================
	/**导出资讯全部信息到EXCEL
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("mymessage:excel")
	public ModelAndView exportExcel() throws Exception{
		FHLOG.save(Jurisdiction.getUsername(), "导出资讯信息到EXCEL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("资讯标题");			//1
			titles.add("资讯链接");			//2
			titles.add("资讯状态");		//3
			titles.add("资讯详情");	//4
			titles.add("资讯概要");	//5
			titles.add("资讯关键词");	//6
			titles.add("资讯作者");	//7
			titles.add("资讯来源");	//8
			titles.add("创建时间");	//9
			titles.add("是否推荐");	//10
			titles.add("是否为热");	//11
			titles.add("是否置顶");	//12
			dataMap.put("titles", titles);
			List<PageData> messageList = messageService.listAllMessage(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<messageList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", messageList.get(i).getString("MESSAGE_TITLE"));	//1
				vpd.put("var2", messageList.get(i).getString("LINK"));//2
				String status= messageList.get(i).getString("STATUS");
				if("1".equals(status)){
					vpd.put("var3","显示");	//3
				}else{
					vpd.put("var3","隐藏");	//3
				}
				vpd.put("var4", messageList.get(i).getString("DETAIL"));		//4
				vpd.put("var5", messageList.get(i).getString("DESCRIPTION"));		//5
				vpd.put("var6", messageList.get(i).getString("KEYWORD"));			//6
				vpd.put("var7", messageList.get(i).getString("AUTHOR"));	//7
				vpd.put("var8", messageList.get(i).getString("RESOURCE"));		//8
				vpd.put("var9", DateUtil.dateFormat((Date)messageList.get(i).get("CREATED_TIME"), "yyyy-MM-dd HH:mm:ss"));		//9
				String recommend= messageList.get(i).getString("RECOMMEND");
				if("1".equals(recommend)){
					vpd.put("var10","是");	//10
				}else{
					vpd.put("var10","否");	//10
				}
				String hot= messageList.get(i).getString("HOT");
				if("1".equals(hot)){
					vpd.put("var11","是");			//11
				}else{
					vpd.put("var11","否");			//11
				}
				String top= messageList.get(i).getString("TOP");
				if("1".equals(top)){
					vpd.put("var12","是");			//12
				}else{
					vpd.put("var12","否");			//12
				}
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
			mv = new ModelAndView(erv,dataMap);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	/**导出资讯部分信息到EXCEL
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/partexcel")
	@RequiresPermissions("mymessage:partexcel")
	public ModelAndView exportPartExcel(String[] id) throws Exception{
		FHLOG.save(Jurisdiction.getUsername(), "导出部分资讯信息到EXCEL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("资讯标题");			//1
			titles.add("资讯链接");			//2
			titles.add("资讯状态");		//3
			titles.add("资讯详情");	//4
			titles.add("资讯概要");	//5
			titles.add("资讯关键词");	//6
			titles.add("资讯作者");	//7
			titles.add("资讯来源");	//8
			titles.add("创建时间");	//9
			titles.add("是否推荐");	//10
			titles.add("是否为热");	//11
			titles.add("是否置顶");	//12
			dataMap.put("titles", titles);
			List<PageData> messageList = messageService.listPartMessage(id);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<messageList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", messageList.get(i).getString("MESSAGE_TITLE"));	//1
				vpd.put("var2", messageList.get(i).getString("LINK"));//2
				String status= messageList.get(i).getString("STATUS");
				if("1".equals(status)){
					vpd.put("var3","显示");	//3
				}else{
					vpd.put("var3","隐藏");	//3
				}
				vpd.put("var4", messageList.get(i).getString("DETAIL"));		//4
				vpd.put("var5", messageList.get(i).getString("DESCRIPTION"));		//5
				vpd.put("var6", messageList.get(i).getString("KEYWORD"));			//6
				vpd.put("var7", messageList.get(i).getString("AUTHOR"));	//7
				vpd.put("var8", messageList.get(i).getString("RESOURCE"));		//8
				vpd.put("var9", DateUtil.dateFormat((Date)messageList.get(i).get("CREATED_TIME"), "yyyy-MM-dd HH:mm:ss"));		//9
				String recommend= messageList.get(i).getString("RECOMMEND");
				if("1".equals(recommend)){
					vpd.put("var10","是");	//10
				}else{
					vpd.put("var10","否");	//10
				}
				String hot= messageList.get(i).getString("HOT");
				if("1".equals(hot)){
					vpd.put("var11","是");			//11
				}else{
					vpd.put("var11","否");			//11
				}
				String top= messageList.get(i).getString("TOP");
				if("1".equals(top)){
					vpd.put("var12","是");			//12
				}else{
					vpd.put("var12","否");			//12
				}
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();					//执行excel操作
			mv = new ModelAndView(erv,dataMap);
			
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	@RequiresPermissions("mymessage:goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/message/uploadexcel");
		return mv;
	}
	
	/**下载资讯模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	@RequiresPermissions("mymessage:downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		FileDownload.fileDownload(response, SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + "Messages.xls", "Messages.xls");
	}
	
	/**把资讯从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/readExcel")
	@RequiresPermissions("mymessage:readExcel")
	public ModelAndView readExcel(
			@RequestParam(value="excel",required=false) MultipartFile file
			) throws Exception{
		FHLOG.save(Jurisdiction.getUsername(), "从EXCEL导入到数据库");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE);								//文件上传路径
			String fileName =  FileUpload.fileUp(file, filePath, "messageexcel");							//执行上传
			List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);		//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
			for(int i=0;i<listPd.size();i++){		
				pd.put("id", this.get32UUID());										//ID
				pd.put("site_id", RequestUtils.getSite(this.getRequest()).getId()); //站点ID
				//pd.put("type_id", listPd.get(i).getString("var0"));										//类型ID
				pd.put("message_title", listPd.get(i).getString("var0"));							//咨询标题
				pd.put("link", listPd.get(i).getString("var1"));							//咨询链接
				//pd.put("message_template", listPd.get(i).getString("var3"));							//资讯模板id
				String status= listPd.get(i).getString("var2").trim();
				if("显示".equals(status)){
					pd.put("status","1");							//资讯状态
				}else{
					pd.put("status","0");							//资讯状态
				}
				//pd.put("surface_image", listPd.get(i).getString("var5"));							//封面id
				pd.put("detail", listPd.get(i).getString("var3"));							//资讯详情
				pd.put("description", listPd.get(i).getString("var4"));							//资讯描述
				pd.put("keyword", listPd.get(i).getString("var5"));							//资讯关键词
				pd.put("author", listPd.get(i).getString("var6"));							//作者
				pd.put("resource", listPd.get(i).getString("var7"));							//资讯来源
				pd.put("created_time",DateUtil.getTime());							//资讯创建时间
				String recommend= listPd.get(i).getString("var9").trim();
				if("是".equals(recommend)){
					pd.put("recommend", "1");							//资讯推荐标记
				}else{
					pd.put("recommend", "0");							//资讯推荐标记
				}
				String hot= listPd.get(i).getString("var10").trim();
				if("是".equals(hot)){
					pd.put("hot","1");						//资讯热标记
				}else{
					pd.put("hot","0");
				}
				String top= listPd.get(i).getString("var11").trim();
				if("是".equals(top)){
					pd.put("top", "1");						//资讯置顶标记
				}else{
					pd.put("top", "0");
				}
				messageService.saveExcelMessage(pd);
			}
			/*存入数据库操作======================================*/
			mv.addObject("msg","success");
		}
		//mv.setViewName("save_result");
		mv.setViewName("redirect:list.do");
		return mv;
	}
	//=============置顶推荐功能============
	/**
	 * 推荐
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataRecommend")
	@ResponseBody
	@RequiresPermissions("mymessage:updataRecommend")
	public Map<String, Object> updateRecommend(String[] ids,String recommend) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "推荐资讯");
		try {
			NewMessage message = new NewMessage();
			message.setRecommend_time(DateUtil.getTime());
			message.setMessageRelevantIdList(ids);
			message.setRecommend(recommend);
			messageService.updataRecommendAndTop(message);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "推荐资讯出现异常");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 置顶
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updataTop")
	@ResponseBody
	@RequiresPermissions("mymessage:updataTop")
	public Map<String, Object> updateTop(String[] ids,String top) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "置顶资讯");
		try {
			NewMessage message = new NewMessage();
			message.setTop_time(DateUtil.getTime());
			message.setMessageRelevantIdList(ids);
			message.setTop(top);
			messageService.updataRecommendAndTop(message);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "置顶资讯出现异常");
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping(value="/messagelistPage")
	@RequiresPermissions("mymessage:messagelistPage")
	public ModelAndView contentByColumnIdlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			List<NewMessage> messageList =messageService.selectlistPageByTypeId(page);	//列出资讯详情列表
			mv.setViewName("system/message/message_list");
			mv.addObject("messageList", messageList);
			mv.addObject("messageTypeList", messageService.findType(pd));
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 扩展字段处理
	 */
	@RequestMapping(value = "/getAllExtendWord")
	@RequiresPermissions("mymessage:getAllExtendWord")
	@ResponseBody
	public Map<String, Object> getAllExtendWord() throws Exception {
		Map<String, Object> map=new HashMap<>();
		messageService.toFindExtendWord(map);
		return map;// 
	}
	
	/*
	 * srot自定义排序
	 * **/
	class MyCompartor implements Comparator
	{
	     @Override
	     public int compare(Object o1, Object o2)
	    {		
	    	 MessageExtendWords e1 = (MessageExtendWords) o1;
	    	 MessageExtendWords e2 = (MessageExtendWords) o2;
	    	 if(e1.getSort() != null && e2.getSort() != null){
	    		 return e1.getSort().compareTo(e2.getSort());
	    	 }else{
	    		 return 0;
	    	 }
	    }
	}
}
