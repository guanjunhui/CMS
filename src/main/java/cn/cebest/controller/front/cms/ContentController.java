package cn.cebest.controller.front.cms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.content.Content;
import cn.cebest.interceptor.FrontSiteInterceptor;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.StringUtil;
import cn.cebest.util.SystemConfig;

@RestController
@RequestMapping(value = "/content")
public class ContentController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ContentController.class);

	@Autowired
	private ContentService contentService;

	// @Autowired
	// private SaveJson saveJson;
	//
	// @RequestMapping(value = "/test")
	// public void test(){
	// JsonParser parse =new JsonParser(); //创建json解析器
	// try {
	// JsonObject json=(JsonObject) parse.parse(new
	// FileReader("C:\\Users\\lenovo\\Documents\\WeChat
	// Files\\wzdong12\\Files\\address(1).json")); //创建jsonObject对象
	// JsonArray array=json.get("京瓷").getAsJsonArray(); //将json数据转为为String型的数据
	// for(int i=0;i<array.size();i++){
	// PageData pd=new PageData();
	// JsonObject reust=array.get(i).getAsJsonObject();
	// pd.put("MY_NAME", reust.get("name").getAsString().trim());
	// pd.put("TEL", reust.get("tel").getAsString().trim());
	// pd.put("ADDRESS", reust.get("address").getAsString().trim());
	// pd.put("CITY", reust.get("city").getAsString().trim());
	// pd.put("PROVINCE", reust.get("province").getAsString().trim());
	// pd.put("BRAND", "京瓷");
	// pd.put("ID",UuidUtil.get32UUID());
	// //入库
	// saveJson.save(pd);
	// }
	//
	// JsonArray array1=json.get("京滟").getAsJsonArray(); //将json数据转为为String型的数据
	// for(int i=0;i<array1.size();i++){
	// PageData pd=new PageData();
	// JsonObject reust=array1.get(i).getAsJsonObject();
	// pd.put("MY_NAME", reust.get("name").getAsString().trim());
	// pd.put("TEL", reust.get("tel").getAsString().trim());
	// pd.put("ADDRESS", reust.get("address").getAsString().trim());
	// pd.put("CITY", reust.get("city").getAsString().trim());
	// pd.put("PROVINCE", reust.get("province").getAsString().trim());
	// pd.put("BRAND", "京滟");
	// pd.put("ID",UuidUtil.get32UUID());
	// //入库
	// saveJson.save(pd);
	// }
	//
	// JsonArray array2=json.get("槟榔").getAsJsonArray(); //将json数据转为为String型的数据
	// for(int i=0;i<array2.size();i++){
	// PageData pd=new PageData();
	// JsonObject reust=array2.get(i).getAsJsonObject();
	// pd.put("MY_NAME", reust.get("name").getAsString().trim());
	// pd.put("TEL", reust.get("tel").getAsString().trim());
	// pd.put("ADDRESS", reust.get("address").getAsString().trim());
	// pd.put("CITY", reust.get("city").getAsString().trim());
	// pd.put("PROVINCE", reust.get("province").getAsString().trim());
	// pd.put("BRAND", "槟榔");
	// pd.put("ID",UuidUtil.get32UUID());
	// //入库
	// saveJson.save(pd);
	// }
	//
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	
	@ResponseBody
	@RequestMapping(value = "/findAllList")
	public Object findAllList(String currentPage,Page page,String queryName) throws Exception{
		PageData pd = this.getPageData();
		String currentId = (String) pd.get("currentId");
		String[] idArray = currentId.split(",");
		pd.put("colum_id", idArray);
		pd.put("queryName", queryName);
		page.setPd(pd);
		Integer currentPageTemp = 1;
		if(currentPage != null) {
			currentPageTemp = Integer.valueOf(currentPage);
			if( currentPageTemp<= 0) {
				currentPageTemp = 1;
			}
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		List<String> ids = contentService.fingAllList(page);
		pd.put("colum_id", ids);
		pd.put("currentPage", currentPageTemp);
		page = (Page) contentService.findIdsList(page);
		resultMap.put("list", page.getPd().get("contentList"));
		page.setCurrentPage(currentPageTemp);
		resultMap.put("page", page);
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}

	@ResponseBody
	@RequestMapping(value = "/findCultureList")
	public Object findCultureList(String currentId,String sort,String queryName,Page page) throws Exception{
		PageData pd = new PageData();
		pd.put("colum_id", currentId);
		pd.put("queryName", queryName);
		pd.put("sort", sort);
		page.setPd(pd);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", contentService.selectlistPageByColumID(page));
		resultMap.put("page", page);
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	@RequestMapping(value = "/contentList")
	@ResponseBody
	public MappingJacksonValue cultureActivityList(Page page, String currentId, String typeid, String jsonpCallback) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("colum_id", currentId);
		pd.put("TYPEID", typeid);
		page.setPd(pd);
		List<Content> list = null;
		try {
			list = contentService.selectlistPageByColumID(page);
			String scheme = getRequest().getScheme();
			String serverName = getRequest().getServerName();
			int port = getRequest().getServerPort();
			String path = getRequest().getContextPath();
			String basePath = scheme + FrontSiteInterceptor.SCHEM + serverName + Const.SPLIT_COLON + port + path;
			String imgDomain = SystemConfig.getPropertiesString(FrontSiteInterceptor.IMG_DOMAIN, basePath);
			String fileDomain = SystemConfig.getPropertiesString(FrontSiteInterceptor.FILE_DOMAIN, basePath);
			for (Content content : list) {
				if (StringUtil.isNotBlank(content.getSurface_imageurl())) {
					content.setSurface_imageurl(imgDomain + "/" + content.getSurface_imageurl());
				}
				if (content.getImageList() != null && content.getImageList().size() > 0) {
					for (Image image : content.getImageList()) {
						if (StringUtil.isNotBlank(image.getImgurl())) {
							image.setImgurl(imgDomain + "/" + image.getImgurl());
						}
					}
				}
				if (content.getVideoList() != null && content.getVideoList().size() > 0) {
					for (Video video : content.getVideoList()) {
						if (StringUtil.isNotBlank(video.getVideo_url())) {
							video.setVideo_url(imgDomain + "/" + video.getVideo_url());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
			list = new ArrayList<Content>();
		}
		Map<String,Object> map=new HashMap<>();
		map.put("page", page);
		map.put("list", list);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new JsonResult(Const.HTTP_OK, "OK", map));
		mappingJacksonValue.setJsonpFunction(jsonpCallback);
		return mappingJacksonValue;
	}
	
	@RequestMapping(value = "/contentListByType")
	@ResponseBody
	public Map<String,Object> contentListByType(Page page, String[] typeIds,String columnId,String keyword) {
		PageData pd = this.getPageData();
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		pd.put("colum_id", columnId);
		pd.put("TYPEIDS", typeIds);
		if(StringUtils.isNotBlank(keyword))
		keyword=new String(Base64Utils.decodeFromString(keyword));
		pd.put("keyword", keyword);
		page.setPd(pd);
		List<Content> list = null;
		try {
			list = contentService.selectlistPageByTypeIds(page);
			String scheme = getRequest().getScheme();
			String serverName = getRequest().getServerName();
			int port = getRequest().getServerPort();
			String path = getRequest().getContextPath();
			String basePath = scheme + FrontSiteInterceptor.SCHEM + serverName + Const.SPLIT_COLON + port + path;
			String imgDomain = SystemConfig.getPropertiesString(FrontSiteInterceptor.IMG_DOMAIN, basePath);
			String fileDomain = SystemConfig.getPropertiesString(FrontSiteInterceptor.FILE_DOMAIN, basePath);
			if(CollectionUtils.isNotEmpty(list)){
				for (Content content : list) {
					if (StringUtil.isNotBlank(content.getSurface_imageurl())) {
						content.setSurface_imageurl(imgDomain + "/" + content.getSurface_imageurl());
					}
					if (content.getImageList() != null && content.getImageList().size() > 0) {
						for (Image image : content.getImageList()) {
							if (StringUtil.isNotBlank(image.getImgurl())) {
								image.setImgurl(imgDomain + "/" + image.getImgurl());
							}
						}
					}
					if (content.getVideoList() != null && content.getVideoList().size() > 0) {
						for (Video video : content.getVideoList()) {
							if (StringUtil.isNotBlank(video.getVideo_url())) {
								video.setVideo_url(imgDomain + "/" + video.getVideo_url());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
		Map<String,Object> map=new HashMap<>();
		map.put("page", page);
		map.put("list", list);
		return map;
	}

}
