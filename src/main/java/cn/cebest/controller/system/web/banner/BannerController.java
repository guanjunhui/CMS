package cn.cebest.controller.system.web.banner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.web.Banner;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.web.banner.BannerManagerService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
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
 * 说明： 广告位接口
 * 创建人：lwt
 * @version
 */
@Controller
@RequestMapping(value="/banner")
public class BannerController extends BaseController {
	
	String menuUrl = "banner/list.do"; //菜单地址(权限用)
	@Resource(name="bannerService")
	private BannerManagerService bannerService;
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;//栏目
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(@ModelAttribute(value="columId") String columId,
			@ModelAttribute(value="topColumId") String topColumId)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//栏目列表
		try {
			List<PageData> columconfigList = columconfigService.columconfigAllList(pd);
			mv.addObject("columconfigList", columconfigList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//mv.addObject("picturePath","/image/save.do"); 发送保存路径请求
		mv.setViewName("web/banner/add");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
//=============================终结显示列表开始=============================
	/**
	 * banner入口方法
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@RequiresPermissions("banner:list")
	public String index(Map<String, Object> map, Page page) throws Exception {
		// 思路:
		// 1 用page传参
		PageData pd = this.getPageData();
		String banner_name = pd.getString("banner_name");
		if(StringUtils.isNotEmpty(banner_name)){
			String decode= URLDecoder.decode(banner_name, "UTF-8");
			pd.put("banner_name",decode);
		}
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		map.put("type", bannerService.findColum(pd));
		bannerService.findBannerToList(map, page);
		return "web/banner/bannerList";// 转发到banner显示页面
		}
	//================================终结显示列表结束===================================
		
	@RequestMapping(value="/bannerByColumnIdlistPage")
	@RequiresPermissions("banner:bannerByColumnIdlistPage")
	public ModelAndView bannerByColumnIdlistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			List<Banner> bannerList = bannerService.selectlistPageByColumID(page);	//列出banner详情列表
			mv.setViewName("web/banner/bannerList");
			mv.addObject("bannerList", bannerList);
			mv.addObject("type", bannerService.findColum(pd));
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 添加banner的方法,修改可能也用这个方法
	 * @param mess
	 * @param file
	 * @return
	 * 参数介绍:第一个参数bann为广告对象,第二个参数为选中栏目的id集合,
	 * 第三个参数资讯类型id的集合
	 * 
	 */
	/*	@RequestMapping(value = "/add")
	@RequiresPermissions("banner:add")
	@ResponseBody
	public Map<String, Object> add(Banner bann, 
			@RequestParam(value = "ids", required = false) String[] ids) {	
		HttpServletRequest request = this.getRequest();//获取当前的request请求
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			logBefore(logger, Jurisdiction.getUsername() + "设置get请求编码出现异常");
			e1.printStackTrace();
		}
		//图片进行遍历
		List<MultipartFile> images=bann.getImages();
		//对视频文件进行遍历
		List<MultipartFile> films=bann.getFilms();
		//详细视频遍历
		List<Video> videos = bann.getVideos();
		//设置视频数量
		Integer countvideo=0;
		if(films!=null && films.size()!=0){
			bann.setCountvideo(films.size());
			countvideo = bann.getCountvideo();
		}
		//详细图片遍历
		List<Image> pictures=bann.getPictures();
		
		//设置图片数量
		Integer countimage=0;
		if(images!=null && images.size()!=0){
			bann.setCountimage(images.size());
			countimage = bann.getCountimage();
		}
		//设置图片和视频的总的数量
		if(countimage!=0 || countvideo!=0){
			bann.setCountiv(countimage+countvideo);
		}
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();	
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增banner");
		bann.setId(this.get32UUID());
		bann.setCreate_time(DateUtil.getTime());
		//处理栏目的id交给banner
		if(ids!=null && ids.length!=0){
			for (String str: ids) {
				pd.put("bannerid", bann.getId());
				pd.put("columnid", str);
				try {
					bannerService.saveBannerColumn(pd);
				} catch (Exception e) {
					logBefore(logger, Jurisdiction.getUsername() + "新增栏目与banner关系时出现异常");
					e.printStackTrace();
				}
			}
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		try {
			String siteId = RequestUtils.getSite(request).getId();// 站点id
			bann.setSite_id(siteId);
			//保存上传的多张图片
			if (null != images && !images.isEmpty()) {
				int count=0;
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增banner图片");
					pd.put("bannerImageId", this.get32UUID()); // 主键
					pd.put("bannerId", bann.getId());//保存相关banner的id
					pd.put("bannerTitle", pictures.get(count).getTitle()); // 标题
					pd.put("bannerImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(count).getBz()); // 备注
					pd.put("create_time", DateUtil.getTime());//保存创建时间
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveImages(pd);
					count++;
				}
			}
			//保存多个上传的视频
			if (null != films && !films.isEmpty()) {
				int num=0;
				for (MultipartFile video : films) {
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增banner视频");
					pd.put("bannerVideoId", this.get32UUID()); // 主键
					pd.put("bannerVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("bannerId", bann.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("bannerVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile + "/" + fileName);// 加水印
					flag= true;
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveVideos(pd);
					num++;
				}
			}
			pd.put("flag", flag);
			
			Map<String, Object> BM = new HashMap<>();
			String bannerName = bann.getBanner_name();
			BM.put("bannerName", bannerName);
			Long count = bannerService.selectBannerByName(BM);
			if(count == 1){
				map.put("count", count);
			}else{
				bannerService.saveBanner(bann, pd); // 执行保存资讯
				map.put("success", true);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "保存banner出现异常");
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}*/
	
	/**
	 * 添加banner的方法,修改可能也用这个方法
	 * @param mess
	 * @param file
	 * @return
	 * 参数介绍:第一个参数bann为广告对象,第二个参数为选中栏目的id集合,
	 * 第三个参数资讯类型id的集合
	 * 
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("banner:add")
	@ResponseBody
	public ModelAndView add(Banner bann, 
			@RequestParam(value = "ids", required = false) String[] ids,int is_add,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {	
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
		//对视频文件进行遍历
		List<MultipartFile> films=bann.getFilms();
		//详细视频遍历
		List<Video> videos = bann.getVideos();
		//设置视频数量
		Integer countvideo=0;
		if(films!=null && films.size()!=0){
			bann.setCountvideo(films.size());
			countvideo = bann.getCountvideo();
		}
		//详细图片遍历
		List<Image> pictures=bann.getPictures();
		
		//设置图片数量
		Integer countimage=0;
		if(images!=null && images.size()!=0){
			bann.setCountimage(images.size());
			countimage = bann.getCountimage();
		}
		//设置图片和视频的总的数量
		if(countimage!=0 || countvideo!=0){
			bann.setCountiv(countimage+countvideo);
		}
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = this.getPageData();	
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "新增banner");
		bann.setId(this.get32UUID());
		bann.setCreate_time(DateUtil.getTime());
		//处理栏目的id交给banner
		if(ids!=null && ids.length!=0){
			for (String str: ids) {
				pd.put("bannerid", bann.getId());
				pd.put("columnid", str);
				try {
					bannerService.saveBannerColumn(pd);
				} catch (Exception e) {
					logBefore(logger, Jurisdiction.getUsername() + "新增栏目与banner关系时出现异常");
					e.printStackTrace();
				}
			}
		}else if(columId != null){
			pd.put("bannerid", bann.getId());
			pd.put("columnid", columId);
			try {
				bannerService.saveBannerColumn(pd);
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "新增栏目与banner关系时出现异常");
				e.printStackTrace();
			}
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		try {
			String siteId = RequestUtils.getSite(request).getId();// 站点id
			bann.setSite_id(siteId);
			//保存上传的多张图片
			if (null != images && !images.isEmpty()) {
				int count=0;
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增banner图片");
					pd.put("bannerImageId", this.get32UUID()); // 主键
					pd.put("bannerId", bann.getId());//保存相关banner的id
					pd.put("bannerTitle", pictures.get(count).getTitle()); // 标题
					pd.put("bannerImageUrl", ffile + "/" + fileName); // 路径
					pd.put("subhead",pictures.get(count).getSubhead());//副标题
					pd.put("imgToUrl",pictures.get(count).getImgurl());//副标题
					pd.put("bz", pictures.get(count).getBz()); // 备注
					pd.put("forder", pictures.get(count).getForder()); // 备注
					pd.put("create_time", DateUtil.getTime());//保存创建时间
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveImages(pd);
					count++;
				}
			}
			//保存多个上传的视频
			if (null != films && !films.isEmpty()) {
				int num=0;
				for (MultipartFile video : films) {
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增banner视频");
					pd.put("bannerVideoId", this.get32UUID()); // 主键
					pd.put("bannerVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("bannerId", bann.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("bannerVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile + "/" + fileName);// 加水印
					flag= true;
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveVideos(pd);
					num++;
				}
			}
			pd.put("flag", flag);
			
			Map<String, Object> BM = new HashMap<>();
			String bannerName = bann.getBanner_name();
			BM.put("bannerName", bannerName);
			Long count = bannerService.selectBannerByName(BM);
			if(count == 1){
				map.put("count", count);
			}else{
				bannerService.saveBanner(bann, pd); // 执行保存资讯
				//map.put("success", true);
				mav.addObject("result", new JsonResult(200,"添加成功!"));
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "保存banner出现异常");
			//map.put("success", false);
			mav.addObject("result", new JsonResult(500,"添加失败!"));
			e.printStackTrace();
		}
		if(is_add==1){
			//mav.setViewName("redirect:goAdd.do");
			mav.setViewName("redirect:goAdd.do?columId="+columId+"&topColumId="+topColumId);
			return mav;
		}
		//mav.setViewName("redirect:list.do");
		mav.setViewName("redirect:/manage/columcontent_colum/managebanner.do?ID="+columId+"&topColumId="+topColumId);
		return mav;
	}
	/**
	 * 刪除banner的方法
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("banner:delete")
	public String deleteProduct(String[] id) {
		try {
			if(id!=null && id.length!=0){
				bannerService.deleteBanner(id);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除banner异常");
			e.printStackTrace();
		}

		return "redirect:list.do";
	}
	/**
	 * 跳转到修改banner页面的方法
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toEdit")
	@RequiresPermissions("banner:toEdit")
	public String toEdit(Map<String, Object> map, String id) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "跳转添加banner页面");
		//通过id找出其相关的栏目并展示在banner编辑的页面中
		PageData pd=this.getPageData();
		pd.put("MASTER_ID", id);
		Banner banner= bannerService.findColumnByBannerId(id);
		map.put("banner", banner);
		map.put("pd", pd);
		return "web/banner/bannerEdit";// 转发到添加页
	}
	/**
	 * 执行修改banner的方法
	 * 
	 * @param product
	 * @param file
	 * @return
	 */
	/*
	@RequestMapping(value = "/update")
	@RequiresPermissions("banner:update")
	@ResponseBody
	public Map<String, Object> updateBanner(Banner bann, 
			@RequestParam(value = "ids", required = false) String[] ids) {	
		//新增图片集合
		List<MultipartFile> images=bann.getAddimages();
		//新增视频集合
		List<MultipartFile> films=bann.getAddfilms();
		//新增详细视频集合
		List<Video> videos = bann.getAddvideos();
		//新增详细图片集合
		List<Image> pictures=bann.getAddpictures();
		//获得所有原有原有图片id集合
		List<String> oldimageIds = bann.getListImage();
		List<String> imageIds=new ArrayList<String>();
		if(oldimageIds!=null && oldimageIds.size()>0){
			for (String string : oldimageIds) {
				if(string!=null){
					imageIds.add(string);
				}
			}
		}
 		//获得所有原有视频的id集合
		List<String> oldvideoIds = bann.getListVideo();
		List<String> videoIds=new ArrayList<String>();
		if(oldvideoIds!=null && oldvideoIds.size()>0){
			for (String string :oldvideoIds) {
				if(string!=null){
					videoIds.add(string);
				}
			}
		}
		int countImages=0;
		int countVideos=0;
		int image1=0;
		int image2=0;
		int video1=0;
		int video2=0;
		if(images!=null){
			image1=images.size();
		}	
		if(imageIds!=null){	
			image2=imageIds.size();
		}		
		//设置图片的数量
		if(image1>0 || image2>0){
			bann.setCountimage(image1+image2);
			countImages=image1+image2;
		}
		if(films!=null){
			video1=films.size();
			
		}
		if(videoIds!=null){
			video2=videoIds.size();	
		}
		//设置视频的数量
		if(video1>0 || video2>0){
			bann.setCountvideo(video1+video2);
			countVideos=video1+video2;
		}
		//设置图片和视频的总的数量
		if(countImages>0 || countVideos>0){
			bann.setCountiv(countImages+countVideos);
		}
		//删除在原有页面中不存在的图片
		List<String> arrIds=new ArrayList<String>();
		try {
			//获得所有图片的id集合
			List<Image> ima= bannerService.findImagesBybannerid(bann.getId());
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
						Image listImage = bannerService.selectImagePathByid(imaId);// 查询图片路径
						if (listImage != null) {
							String path =listImage.getImgurl();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + path); // 删除图片文件
							}
						}				
						bannerService.deleteImage(imaId);
					}
				}	
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除图片文件及详情时出现异常!");
			}
			
		}	
		//删除在原有页面中不存在的视频
		List<String> arrVIds=new ArrayList<String>();
		try {
			//获得所有图片的id集合
			List<Video> vid= bannerService.findVideosBybannerid(bann.getId());
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
						Video dvideo =bannerService.selectVideoPathByid(vd);// 查询视频路径
						if (dvideo != null) {
							String filePath =dvideo.getVideo_url();
							if (filePath != null && Tools.notEmpty(filePath.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除视频文件
							}
						}
						bannerService.deleteVideo(vd);			
					}				
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除视频文件及详情时出现异常!");
			}
		}
			
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "修改banner");
		//处理栏目的id交给banner
		if(ids!=null && ids.length!=0){
			try {
				//处理栏目之前,首先删除先前的栏目与banner的关系
				bannerService.deleteRelationByBannerId(bann.getId());
				for (String str: ids) {
					pd.put("bannerid", bann.getId());
					pd.put("columnid", str);
					try {
						bannerService.saveBannerColumn(pd);
					} catch (Exception e) {
						logBefore(logger, Jurisdiction.getUsername() + "保存栏目与banner关系时出现异常");
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除栏目与banner关系时出现异常");
				e.printStackTrace();
			}
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		//更新原有的图片
		List<Image> oldpictures2 = bann.getPictures();
		List<Image> pictures2=new ArrayList<Image>();
		if(oldpictures2!=null && oldpictures2.size()>0){
			for (Image image : oldpictures2) {
				if(image.getImageId()!=null){
					pictures2.add(image);
				}
			}
		}
		List<MultipartFile> oldimages2 = bann.getImages();
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
						Image dimage =bannerService.selectImagePathByid(pictures2.get(i).getImageId());// 查询视频路径
						if (dimage != null) {
							String path =dimage.getImgurl();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(images2.get(i), filePath, this.get32UUID());
						pictures2.get(i).setImgurl(ffile + "/" + fileName);
					}
					//更新图片信息
					bannerService.updateImageById(pictures2.get(i));;
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//更新原有的视频
		List<Video> oldvideos2 = bann.getVideos();
		List<Video> videos2=new ArrayList<Video>();
		if(oldvideos2!=null && oldvideos2.size()>0){			
			for (Video video : oldvideos2) {
				if(video.getId()!=null){
					videos2.add(video);
				}
			}
		}
		List<MultipartFile> oldfilms2 = bann.getFilms();
		List<MultipartFile> films2=new ArrayList<MultipartFile>();
		if(oldfilms2!=null && oldfilms2.size()>0){
			for (MultipartFile multipartFile : oldfilms2) {
				if(multipartFile!=null){
					films2.add(multipartFile);
				}
			}	
		}
		if(videos!=null && videos.size()>0){
			try {
				for (int i = 0; i < videos2.size(); i++) {
					//上传更新文件
					if(films2.get(i)!=null && !films2.get(i).isEmpty()){
						Video dvideo =bannerService.selectVideoPathByid(videos2.get(i).getId());// 查询视频路径
						if (dvideo != null) {
							String path =dvideo.getVideo_url();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(PathUtil.getClasspath() +Const.FILEPATHFILE + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(films2.get(i), filePath, this.get32UUID());
						videos2.get(i).setVideo_url(ffile + "/" + fileName);
					}
					//更新视频信息
					bannerService.updateVideoById(videos2.get(i));
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (null != images && !images.isEmpty()) {
				//保存新增的图片
				int counts=0;
				for (MultipartFile image : images) {
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增banner图片");
					pd.put("bannerImageId", this.get32UUID()); // 主键
					pd.put("bannerId", bann.getId());//保存相关banner的id
					pd.put("bannerTitle", pictures.get(counts).getTitle()); // 标题
					pd.put("bannerImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(counts).getBz()); // 备注
					pd.put("create_time", DateUtil.getTime());//保存创建时间
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					bannerService.saveImages(pd);
					counts++;
				}
			}
			if (null != films && !films.isEmpty()) {
				//保存新增的视频
				int num=0;
				for (MultipartFile video : films) {
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增banner视频");
					pd.put("bannerVideoId", this.get32UUID()); // 主键
					videoIds.add(pd.getString("bannerVideoId"));//保存多个视频的id
					pd.put("bannerVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("bannerId", bann.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("bannerVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
					Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveVideos(pd);
					num++;
				}
			}
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			bann.setSite_id(siteId);
			pd.put("flag", flag);
			Map<String, Object> BM = new HashMap<>();
			String bannerName = bann.getBanner_name();
			BM.put("bannerName", bannerName);
			BM.put("id", bann.getId());
			Long count = bannerService.selectBannerByName(BM);
			if(count == 1){
				map.put("count", count);
			}else{
				bannerService.updateBanner(bann, pd); 
				map.put("success", true);
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改banner出现异常");
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}*/
	
	/**
	 * 执行修改banner的方法
	 * 
	 * @param product
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/update")
	@RequiresPermissions("banner:update")
	@ResponseBody
	public ModelAndView updateBanner(Banner bann,
			@RequestParam(value = "ids", required = false) String[] ids,
			@RequestParam("columId") String columId,@RequestParam("topColumId") String topColumId) {	
		ModelAndView mev=new ModelAndView();
		//新增图片集合
		List<MultipartFile> imagesNew=bann.getAddimages();
		//新增视频集合
		List<MultipartFile> filmsNew=bann.getAddfilms();
		//新增详细视频集合
		List<Video> videos = bann.getAddvideos();
		//新增详细图片集合
		List<Image> pictures=bann.getAddpictures();
		//获得所有原有原有图片id集合
		List<String> oldimageIds = bann.getListImage();
		List<String> imageIds=new ArrayList<String>();
		if(oldimageIds!=null && oldimageIds.size()>0){
			for (String string : oldimageIds) {
				if(string!=null){
					imageIds.add(string);
				}
			}
		}
 		//获得所有原有视频的id集合
		List<String> oldvideoIds = bann.getListVideo();
		List<String> videoIds=new ArrayList<String>();
		if(oldvideoIds!=null && oldvideoIds.size()>0){
			for (String string :oldvideoIds) {
				if(string!=null){
					videoIds.add(string);
				}
			}
		}
		int countImages=0;
		int countVideos=0;
		int image1=0;
		int image2=0;
		int video1=0;
		int video2=0;
		if(imagesNew!=null){
			image1=imagesNew.size();
		}	
		if(imageIds!=null){	
			image2=imageIds.size();
		}		
		//设置图片的数量
		if(image1>0 || image2>0){
			bann.setCountimage(image1+image2);
			countImages=image1+image2;
		}
		if(filmsNew!=null){
			video1=filmsNew.size();
			
		}
		if(videoIds!=null){
			video2=videoIds.size();	
		}
		//设置视频的数量
		if(video1>0 || video2>0){
			bann.setCountvideo(video1+video2);
			countVideos=video1+video2;
		}
		//设置图片和视频的总的数量
		if(countImages>0 || countVideos>0){
			bann.setCountiv(countImages+countVideos);
		}
		//删除在原有页面中不存在的图片
		List<String> arrIds=new ArrayList<String>();
		try {
			//获得所有图片的id集合
			List<Image> ima= bannerService.findImagesBybannerid(bann.getId());
			if(ima!=null && ima.size()>0){
				for (Image im: ima) {					
					arrIds.add(im.getImageId());
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		//如果用户没做修改下面的不会执行,若在原来的基础上删除了一个,则会执行下面的语句
		if(arrIds!=null && arrIds.get(0)!=null && imageIds!=null && imageIds.get(0)!=null){
			try {
				for (String imaId :arrIds) {
					if(imaId!=null){
						if(!(imageIds.contains(imaId))){
							Image listImage = bannerService.selectImagePathByid(imaId);// 查询图片路径
							if (listImage != null) {
								String path =listImage.getImgurl();
								if (path != null && Tools.notEmpty(path.trim())) {
									DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除图片文件
								}
							}				
							bannerService.deleteImage(imaId);
						}
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
				logBefore(logger, Jurisdiction.getUsername() + "删除图片文件及详情时出现异常!");
			}
			
		}	
		//删除在原有页面中不存在的视频
		List<String> arrVIds=new ArrayList<String>();
		try {
			//获得所有图片的id集合
			List<Video> vid= bannerService.findVideosBybannerid(bann.getId());
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
					if(vd!=null){
						if(!(videoIds.contains(vd))){					
							Video dvideo =bannerService.selectVideoPathByid(vd);// 查询视频路径
							if (dvideo != null) {
								String path =dvideo.getVideo_url();
								if (path != null && Tools.notEmpty(path.trim())) {
									DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除视频文件
								}
							}
							bannerService.deleteVideo(vd);			
						}				
					}
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除视频文件及详情时出现异常!");
			}
		}
		if(videoIds!=null && videoIds.size()==0 && arrVIds!=null && arrVIds.size()>0){
			try {
				for (String vd : arrVIds) {	
						Video dvideo =bannerService.selectVideoPathByid(vd);// 查询视频路径
						if (dvideo != null) {
							String path =dvideo.getVideo_url();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除视频文件
							}
						}
						bannerService.deleteVideo(vd);			
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除视频文件及详情时出现异常!");
				e.printStackTrace();
			}
		}	
		Map<String, Object> map = new HashMap<>();
		String ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		// 添加
		logBefore(logger, Jurisdiction.getUsername() + "修改banner");
		//处理栏目的id交给banner
		if(columId != null){
			try {
				//处理栏目之前,首先删除先前的栏目与banner的关系
				bannerService.deleteRelationByBannerId(bann.getId());
				pd.put("bannerid", bann.getId());
				pd.put("columnid", columId);
				try {
					bannerService.saveBannerColumn(pd);
				} catch (Exception e) {
					logBefore(logger, Jurisdiction.getUsername() + "保存栏目与banner关系时出现异常");
					e.printStackTrace();
				}
			} catch (Exception e) {
				logBefore(logger, Jurisdiction.getUsername() + "删除栏目与banner关系时出现异常");
				e.printStackTrace();
			}
		}
		Boolean flag = false;
		String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 图片文件上传路径
		String videoPath = SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + ffile; // 视频文件上传路径
		//更新原有的图片
		List<Image> oldpictures2 = bann.getPictures();
		List<Image> pictures2=new ArrayList<Image>();
		if(oldpictures2!=null && oldpictures2.size()>0){
			for (Image image : oldpictures2) {
				pictures2.add(image);
			}
		}
		List<MultipartFile> oldimages2 = bann.getImages();
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
						Image dimage =bannerService.selectImagePathByid(pictures2.get(i).getImageId());// 查询视频路径
						if (dimage != null) {
							String path =dimage.getImgurl();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(images2.get(i), filePath, this.get32UUID());
						pictures2.get(i).setImgurl(ffile + "/" + fileName);
						//pictures2.get(i).setName(images2.get(i).getOriginalFilename());
					}
					//更新图片信息
					bannerService.updateImageById(pictures2.get(i));
					/*if(pictures2.get(i).getImgurl() != null && !"".equals(pictures2.get(i).getImgurl())){
						
					}*/
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//更新原有的视频
		List<Video> oldvideos2 = bann.getVideos();
		List<Video> videos2=new ArrayList<Video>();
		if(oldvideos2!=null && oldvideos2.size()>0){			
			for (Video video : oldvideos2) {
				if(video.getId()!=null){
					videos2.add(video);
				}
			}
		}
		List<MultipartFile> oldfilms2 = bann.getFilms();
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
						Video dvideo =bannerService.selectVideoPathByid(videos2.get(i).getId());// 查询视频路径
						if (dvideo != null) {
							String path =dvideo.getVideo_url();
							if (path != null && Tools.notEmpty(path.trim())) {
								DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除视频文件
							}
						}
						fileName = FileUpload.fileUp(films2.get(i), filePath, this.get32UUID());
						videos2.get(i).setVideo_url(ffile + "/" + fileName);
						//videos2.get(i).setName(films2.get(i).getOriginalFilename());
					}
					//更新视频信息
					bannerService.updateVideoById(videos2.get(i));
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (null != imagesNew && imagesNew.size()>0) {
				//保存新增的图片
				int counts=0;
				for (MultipartFile image : imagesNew) {
					pd.put("name",image.getOriginalFilename());
					fileName = FileUpload.fileUp(image, filePath, this.get32UUID()); // 执行多张图片上传	
					logBefore(logger, Jurisdiction.getUsername() + "新增banner图片");
					pd.put("bannerImageId", this.get32UUID()); // 主键
					pd.put("bannerId", bann.getId());//保存相关banner的id
					pd.put("bannerTitle", pictures.get(counts).getTitle()); // 标题
					pd.put("subhead",pictures.get(counts).getSubhead());//副标题
					pd.put("bannerImageUrl", ffile + "/" + fileName); // 路径
					pd.put("bz", pictures.get(counts).getBz()); // 备注
					pd.put("create_time", DateUtil.getTime());//保存创建时间
					pd.put("forder", pictures.get(counts).getForder());//排序
//					Watermark.setWatemark(resourceLocation + ffile + "/" + fileName);// 加水印
					flag = true;
					pd.put("flag", flag);
					bannerService.saveImages(pd);
					counts++;
				}
			}
			if (null != filmsNew && filmsNew.size()>0) {
				//保存新增的视频
				int num=0;
				for (MultipartFile video : filmsNew) {
					pd.put("name",video.getOriginalFilename());
					fileName = FileUpload.fileUp(video, videoPath, this.get32UUID()); // 执行多个视频上传
					String videoend = FilenameUtils.getExtension(video.getOriginalFilename());
					logBefore(logger, Jurisdiction.getUsername() + "新增banner视频");
					pd.put("bannerVideoId", this.get32UUID()); // 主键
					videoIds.add(pd.getString("bannerVideoId"));//保存多个视频的id
					pd.put("bannerVideoTitle", videos.get(num).getVideo_title()); // 标题
					pd.put("bannerId", bann.getId());//保存相关资讯的id
					pd.put("extensionName", videoend);//保存视频的扩展名
					pd.put("videoTime", DateUtil.getTime());//保存视频的上传时间
					pd.put("bannerVideoUrl", ffile + "/" + fileName); // 路径
					pd.put("videoContent",videos.get(num).getVideo_content()); // 备注
//					Watermark.setWatemark(resourceLocation + ffile + "/" + fileName);// 加水印
					flag = true;
					//pd.put("forder", videos.get(num).getForder());//排序
					pd.put("flag", flag);
					pd.put("CREAT_TIME", new Date());
					bannerService.saveVideos(pd);
					num++;
				}
			}
			String siteId = RequestUtils.getSite(this.getRequest()).getId();// 获取站点id
			bann.setSite_id(siteId);
			pd.put("flag", flag);
			Map<String, Object> BM = new HashMap<>();
			String bannerName = bann.getBanner_name();
			BM.put("bannerName", bannerName);
			BM.put("id", bann.getId());
			Long count = bannerService.selectBannerByName(BM);
			if(count == 1){
				map.put("count", count);
			}else{
				bann.setPd(pd);
				bannerService.updateBanner(bann); 
				//map.put("success", true);
				mev.addObject("result", new JsonResult(200,"修改成功!"));
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "修改banner出现异常");
			//map.put("success", false);
			mev.addObject("result", new JsonResult(500,"修改失败!"));
			e.printStackTrace();
		}
		mev.setViewName("redirect:/manage/columcontent_colum/managebanner.do?ID="+columId+"&topColumId="+topColumId);
		return mev;
	}
	
	
	/**
	 * 对应栏目的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/columnDetail")
	@RequiresPermissions("banner:columnDetail")
	public String columnDetail(Map<String, Object> map,String id){
		logBefore(logger, Jurisdiction.getUsername() + "查询栏目的详细信息!");
		try {
			bannerService.selectColumnDetail(map,id);
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询栏目的详细信息失败!");
			e.printStackTrace();
		}
		return "web/banner/columnDetail";
	}
	
	@RequestMapping("updateSort")
	@ResponseBody
	public JsonResult updateSort(String id,String forder){
		PageData pd = new PageData();
		pd.put("bannerId", id);
		pd.put("forder", forder);
		try {
			bannerService.updateSort(pd);
		} catch (Exception e) {
			logger.error("update the forder occured error!", e);
            return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
}
