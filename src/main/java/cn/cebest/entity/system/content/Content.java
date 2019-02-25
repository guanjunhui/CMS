package cn.cebest.entity.system.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.cebest.entity.bo.KeyValuesBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Seo;
import cn.cebest.entity.system.Video;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;
/**
 * 内容
 * @author liqk
 *
 */
public class Content implements Serializable {
	
	private static final long serialVersionUID = -3118882611666749785L;
	
	private String id; //主键id
	private String siteId; //站点id
	private String subtitle;//副标题
	private String contentDetailId; //内容详情id
	private String contentTemplateId; //内容模板id
	private String typeId; //类型id
	private String contentTitle; //内容标题
	private String contentWbUrl; //外部url
	private String contentSummary; //内容概要
	private String contentKeyWords; //内容关键字
	private String createdTime; //创建时间
	private String updateTime; //更新时间
	private String contentStatus; //内容状态
	private String solrStatus; //内容状态
	private String source; //内容文本
	private String detailTitle; //内容详情标题
	private String releaseTime; //内容发布时间
	private Long pv; //浏览量
	private String shareCount; //分享量
	private String contentTxt; //内容文本
	private String author;//作者
	private String typeName; //类型名称
	private String typeCode; //类型编码
	private String recommend_time;
	private String recommend;
	private String top_time;
	private String top;
	private String hot_time;
	private String hot;//热标识
	private Integer sort;
	private List<ExtendFiledUtil> fileds=new ArrayList<>();
	private String [] contenttypeids;//内容类型id数组
	private List<ContentType> contentTypeList;
	private List<KeyValuesBo> objKey_Value;
	
	private PageData pd;
	private String[] columconfigIds; //栏目id
	private Set<ColumConfig> columConfigList;
	
	private String surface_imageid;
	private String surface_imageurl;
	private String surface_imageid2;
	private String surface_imageurl2;
	private String title;
	private String title2;
	private List<String> imageids=new ArrayList<>();
	private List<Image> imageList=new ArrayList<>(); //配图
	private List<Video> videoList=new ArrayList<>();
	private List<Content> contentRelevantList; //相关的内容列表
	private List<String> videoids=new ArrayList<>();
	private List<Image> timageList=new ArrayList<>(); //配图
	private List<Video> tvideoList=new ArrayList<>();
	private String filedJson;
	private Seo seoInfo;
	private String seo_title;
	private String seo_description;
	
	private String imgTitle;
	private String imgDesc;
	private String imgURL;
	private String contentUrlName;
	private String columUrlName;
	private String programme;//方案来源
	private String friend;
	
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getProgramme() {
		return programme;
	}
	public void setProgramme(String programme) {
		this.programme = programme;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getColumUrlName() {
		return columUrlName;
	}
	public void setColumUrlName(String columUrlName) {
		this.columUrlName = columUrlName;
	}
	public String getContentUrlName() {
		return contentUrlName;
	}
	public void setContentUrlName(String contentUrlName) {
		this.contentUrlName = contentUrlName;
	}
	
	
	public String getHot_time() {
		return hot_time;
	}
	public void setHot_time(String hot_time) {
		this.hot_time = hot_time;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public List<ExtendFiledUtil> getFileds() {
		return fileds;
	}
	public void setFileds(List<ExtendFiledUtil> fileds) {
		this.fileds = fileds;
	}
	public String[] getContenttypeids() {
		return contenttypeids;
	}
	public void setContenttypeids(String[] contenttypeids) {
		this.contenttypeids = contenttypeids;
	}
	public List<ContentType> getContentTypeList() {
		return contentTypeList;
	}
	public void setContentTypeList(List<ContentType> contentTypeList) {
		this.contentTypeList = contentTypeList;
	}
	public List<KeyValuesBo> getObjKey_Value() {
		return objKey_Value;
	}
	public void setObjKey_Value(List<KeyValuesBo> objKey_Value) {
		this.objKey_Value = objKey_Value;
	}
	public PageData getPd() {
		return pd;
	}
	public void setPd(PageData pd) {
		this.pd = pd;
	}
	public Seo getSeoInfo() {
		return seoInfo;
	}
	public void setSeoInfo(Seo seoInfo) {
		this.seoInfo = seoInfo;
	}
	public String getSeo_title() {
		return seo_title;
	}
	public void setSeo_title(String seo_title) {
		this.seo_title = seo_title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	public String getImgTitle() {
		return imgTitle;
	}
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	public String getImgDesc() {
		return imgDesc;
	}
	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}
	public String getSeo_description() {
		return seo_description;
	}
	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}
	public String getFiledJson() {
		return filedJson;
	}
	public void setFiledJson(String filedJson) {
		this.filedJson = filedJson;
	}
	public List<Image> getTimageList() {
		return timageList;
	}
	public void setTimageList(List<Image> timageList) {
		this.timageList = timageList;
	}
	public List<Video> getTvideoList() {
		return tvideoList;
	}
	public void setTvideoList(List<Video> tvideoList) {
		this.tvideoList = tvideoList;
	}
	public List<String> getVideoids() {
		return videoids;
	}
	public void setVideoids(List<String> videoids) {
		this.videoids = videoids;
	}
	public List<String> getImageids() {
		return imageids;
	}
	public void setImageids(List<String> imageids) {
		this.imageids = imageids;
	}
	public String getRecommend_time() {
		return recommend_time;
	}
	public void setRecommend_time(String recommend_time) {
		this.recommend_time = recommend_time;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getTop_time() {
		return top_time;
	}
	public void setTop_time(String top_time) {
		this.top_time = top_time;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public Set<ColumConfig> getColumConfigList() {
		return columConfigList;
	}
	public void setColumConfigList(Set<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	//修改提交
	private String[] contentRelevantIdList; //相关内容id集合
	
	public String getSurface_imageurl() {
		return surface_imageurl;
	}
	public void setSurface_imageurl(String surface_imageurl) {
		this.surface_imageurl = surface_imageurl;
	}
	public String getSurface_imageid() {
		return surface_imageid;
	}
	public void setSurface_imageid(String surface_imageid) {
		this.surface_imageid = surface_imageid;
	}
	public List<Video> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getContentDetailId() {
		return contentDetailId;
	}
	public void setContentDetailId(String contentDetailId) {
		this.contentDetailId = contentDetailId;
	}
	public String getContentTemplateId() {
		return contentTemplateId;
	}
	public void setContentTemplateId(String contentTemplateId) {
		this.contentTemplateId = contentTemplateId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getContentWbUrl() {
		return contentWbUrl;
	}
	public void setContentWbUrl(String contentWbUrl) {
		this.contentWbUrl = contentWbUrl;
	}
	public String getContentSummary() {
		return contentSummary;
	}
	public void setContentSummary(String contentSummary) {
		this.contentSummary = contentSummary;
	}
	public String getContentKeyWords() {
		return contentKeyWords;
	}
	public void setContentKeyWords(String contentKeyWords) {
		this.contentKeyWords = contentKeyWords;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getContentStatus() {
		return contentStatus;
	}
	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDetailTitle() {
		return detailTitle;
	}
	public void setDetailTitle(String detailTitle) {
		this.detailTitle = detailTitle;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	
	public Long getPv() {
		return pv;
	}
	public void setPv(Long pv) {
		this.pv = pv;
	}
	public String getShareCount() {
		return shareCount;
	}
	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}
	public String getContentTxt() {
		return contentTxt;
	}
	public void setContentTxt(String contentTxt) {
		this.contentTxt = contentTxt;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	public String[] getColumconfigIds() {
		return columconfigIds;
	}
	public void setColumconfigIds(String[] columconfigIds) {
		this.columconfigIds = columconfigIds;
	}
	public List<Image> getImageList() {
		return imageList;
	}
	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}
	public List<Content> getContentRelevantList() {
		return contentRelevantList;
	}
	public void setContentRelevantList(List<Content> contentRelevantList) {
		this.contentRelevantList = contentRelevantList;
	}
	public String[] getContentRelevantIdList() {
		return contentRelevantIdList;
	}
	public void setContentRelevantIdList(String[] contentRelevantIdList) {
		this.contentRelevantIdList = contentRelevantIdList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSolrStatus() {
		return solrStatus;
	}
	public void setSolrStatus(String solrStatus) {
		this.solrStatus = solrStatus;
	}
	public String getSurface_imageid2() {
		return surface_imageid2;
	}
	public void setSurface_imageid2(String surface_imageid2) {
		this.surface_imageid2 = surface_imageid2;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getSurface_imageurl2() {
		return surface_imageurl2;
	}
	public void setSurface_imageurl2(String surface_imageurl2) {
		this.surface_imageurl2 = surface_imageurl2;
	}	
	
}
