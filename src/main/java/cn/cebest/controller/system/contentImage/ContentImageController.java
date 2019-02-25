package cn.cebest.controller.system.contentImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.content.contentImage.ContentImageService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.FileUpload;
import cn.cebest.util.GetWeb;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;

/** 
 * 类名称：内容图片
 * 创建人：liqk
 * 创建时间：2017-10-09
 */
@Controller
@RequestMapping(value="/contentImage")
public class ContentImageController extends BaseController {
	
	@Resource(name="contentImageService")
	private ContentImageService contentImageService;
	
	/**内容图片列表
	 * @param page
	 * @return
	 * @ 
	 */
	@RequestMapping(value="/contentImageList")
	public ModelAndView contentImageList(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		try {
			String imageipport=SystemConfig.getPropertiesString("imageipport");//图片的访问ip端口地址
			pd.put("imageipport", imageipport);
			pd.put("keyword", pd.getString("keyword").trim()); //关键词
			List<PageData> varList = contentImageService.contentImageList(page);
			mv.addObject("varList", varList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("system/contentimage/contentimage_list_tc");
		mv.addObject("pd", pd);
		mv.addObject("msg", "contentImageList.do?contentId="+pd.getString("contentId")+"&keyword=");
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**新增
	 * @param file
	 * @return
	 * @
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save(
			@RequestParam(required=false) MultipartFile file
			) {
		Map<String,String> map = new HashMap<String,String>();
		String  ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile;		//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
		}else{
			System.out.println("上传失败");
		}
		String extName = file.getOriginalFilename().substring(
				file.getOriginalFilename().lastIndexOf(".")+1);
		try {
			if(extName.matches(Const.VIDEO)){
				logBefore(logger, Jurisdiction.getUsername()+"新增视频");
				pd.put("ID", this.get32UUID());						//主键
				pd.put("VIDEO_TITLE", "视频");						//标题
				pd.put("VIDEO_CONTENT", fileName);					//文件名
				pd.put("VIDEO_URL", ffile + "/" + fileName);		//路径
				pd.put("VIDEO_TYPE", "1");						    //类型
				pd.put("MASTER_ID", pd.getString("id"));			//附属与
				pd.put("CREATETIME", Tools.date2Str(new Date()));	//创建时间
//				videoService.save(pd);
			}else{
				logBefore(logger, Jurisdiction.getUsername()+"新增图片");
				pd.put("contentImageId", this.get32UUID());			//主键
				pd.put("contentImageTitle", "图片");					//标题
				pd.put("contentImageUrl", ffile + "/" + fileName);	//路径
				pd.put("bz", "图片管理处上传");							//备注
				Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
				contentImageService.save(pd);
			}
			map.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**删除
	 * @param out
	 * @ 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public JsonResult delete() {
		logBefore(logger, Jurisdiction.getUsername()+"删除图片");
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			if(Tools.notEmpty(pd.getString("imgurl").trim())){
				DelAllFile.delFolder(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pd.getString("imgurl")); //删除图片
			}
			contentImageService.delete(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult(Const.HTTP_OK,"success");
	}
	
	/**去新增页面
	 * @return
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("picturePath","/contentImage/save.do?contentId="+pd.getString("contentId")); 
		mv.setViewName("system/contentimage/contentimage_add");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 请求连接获取网页中每个图片的地址
	 * @param args
	 * @
	 */
	@RequestMapping(value="/getImagePath")
	@ResponseBody
	public Object getImagePath(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<String> imgList = new ArrayList<String>();
		String errInfo = "success";
		String serverUrl = pd.getString("serverUrl");	//网页地址
		String msg = pd.getString("msg");				//msg:save 时保存到服务器
		if (!serverUrl.startsWith("http://")){ 			//检验地址是否http://
			 errInfo = "error";							//无效地址
		 }else{
			 try {
				imgList = GetWeb.getImagePathList(serverUrl);
				if("save".equals(msg)){
					String ffile = DateUtil.getDays();
					String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile;		//文件上传路径
					for(int i=0;i<imgList.size();i++){	//把网络图片保存到服务器硬盘，并数据库记录
						String fileName = FileUpload.getHtmlPicture(imgList.get(i),filePath,null);								//下载网络图片上传到服务器上
						//保存到数据库
						pd.put("PICTURES_ID", this.get32UUID());			//主键
						pd.put("TITLE", "图片");								//标题
						pd.put("NAME", fileName);							//文件名
						pd.put("PATH", ffile + "/" + fileName);				//路径
						pd.put("CREATETIME", Tools.date2Str(new Date()));	//创建时间
						pd.put("MASTER_ID", "1");							//附属与
						pd.put("BZ", serverUrl+"爬取");						//备注
						Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
						contentImageService.save(pd);
					}
				}
			} catch (Exception e) {
				errInfo = "error";						//出错
			}
		}
		map.put("imgList", imgList);					//图片集合
		map.put("result", errInfo);						//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
