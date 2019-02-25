package cn.cebest.entity.system.newMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Seo;
import cn.cebest.entity.system.Video;
import cn.cebest.util.PageData;
/**
 * 资讯 
 * @author lwt
 *
 */
public class NewMessage implements Serializable {
private static final long serialVersionUID = -3116682341666749785L;
		private String id; //主键id
		private String site_id;//站点id
		private String type_id; //资讯类型id
		private String type_name;//资讯名称
		private String link; //外部url
		private String message_title;//资讯标题
		private String message_template; //资讯模板id
		private String message_templateName; //资讯模板名称
		private String status; //资讯状态
		private String surface_image;//保存封面图片id
		private String detail; //资讯详细描述	
		private String description; //资讯概要
		private String keyword; //资讯关键字
		private String author;//资讯的作者
		private String resource;//资讯的來源
		private String created_time; //创建时间
		private String recommend;//标记为推荐
		private String hot;//标记为热
		private String hot_time;//置顶时间
		private String[] messagetypeids;//资讯类型id数组
		private String top;//标记为置顶
		private String recommend_time;//推荐时间
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date release_time;//发布时间
		private String top_time;//置顶时间
		private String update_time;//更新时间
		private String[] columtype;//存储类型与栏目对
		private String valuetext;//保存扩展字段
		private List<MessageExtendWords> pvtlist;//保存多个扩展字段对象
		private Seo seoInfo;
		private String seo_title;
		private String seokeywords;//保存seo关键字
		private String seodescription;//保存seo描述
		private ArrayList<String> arrayList;
		private ArrayList<Image> otherImgs;
		private ArrayList<Image> eightImgs1;
		private ArrayList<Image> eightImgs2;
		private ArrayList<Image> eightImgs3;
		private List<String> listImage=new ArrayList<String>();//一个资讯保存很多图片的id
		private List<String> listVideo=new ArrayList<String>();//一个资讯保存很多录像的id
		private List<Image> pictures;//一个资讯包含很多图片
		private List<Image> addpictures;//在修改页面接收新增图片信息
		private List<Video> videos;//一个资讯包含很多录像
		private List<Video> addvideos;//在修改页面接收新增录像的信息
		private List<MultipartFile> images;//接收图片文件并上传
		private List<MultipartFile> addimages;//在修改页面,接收新增图片上传
		private List<MultipartFile> films;//接收视频文件并上传
		private List<MultipartFile> addfilms;//在修改页面接收新增视频文件并上传
		private Image image;//用来存储封面图片的详细内容
		//修改提交
		private String[] messageRelevantIdList; //相关资讯id集合
		private List<NewMessageType> messageTypeList;//相关资讯类型集合
		//为了保存栏目与资讯的关系而设计
		private String[] columnids;
		private List<ColumConfig> columConfigList;
		private List<ColumConfig> columnTypeList;//相关资讯类型集合
		//前端展示详情页显示上一篇下一篇
		private String columname;//栏目名称
		private String columid;//栏目id
		private String beforeId;//本条资讯的上一条资讯id
		private String afterId;//本条资讯的下一条资讯id
		private String beforeTitle;//本条资讯的上一条资讯的标题
		private String afterTitle;//本条资讯的下一条资讯的标题
		private String beforeUrlName;//本条资讯的上一条资讯的Url信息
		private String afterUrlName;//本条资讯的下一条资讯的Url信息
		private Integer count;
		private String video_url;
		private Integer sort;
		private PageData pd;
		private String contentUrlName;
		private String columUrlName;
		
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
		
		public String getBeforeUrlName() {
			return beforeUrlName;
		}
		public void setBeforeUrlName(String beforeUrlName) {
			this.beforeUrlName = beforeUrlName;
		}
		public String getAfterUrlName() {
			return afterUrlName;
		}
		public void setAfterUrlName(String afterUrlName) {
			this.afterUrlName = afterUrlName;
		}
		
		public ArrayList<Image> getEightImgs3() {
			return eightImgs3;
		}
		public void setEightImgs3(ArrayList<Image> eightImgs3) {
			this.eightImgs3 = eightImgs3;
		}
		public ArrayList<Image> getEightImgs1() {
			return eightImgs1;
		}
		public void setEightImgs1(ArrayList<Image> eightImgs1) {
			this.eightImgs1 = eightImgs1;
		}
		public ArrayList<Image> getEightImgs2() {
			return eightImgs2;
		}
		public void setEightImgs2(ArrayList<Image> eightImgs2) {
			this.eightImgs2 = eightImgs2;
		}
		public ArrayList<Image> getOtherImgs() {
			return otherImgs;
		}
		public void setOtherImgs(ArrayList<Image> otherImgs) {
			this.otherImgs = otherImgs;
		}
		public ArrayList<String> getArrayList() {
			return arrayList;
		}
		public void setArrayList(ArrayList<String> arrayList) {
			this.arrayList = arrayList;
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
		public PageData getPd() {
			return pd;
		}
		public void setPd(PageData pd) {
			this.pd = pd;
		}
		public Date getRelease_time() {
			return release_time;
		}
		public void setRelease_time(Date release_time) {
			this.release_time = release_time;
		}
		public List<ColumConfig> getColumnTypeList() {
			return columnTypeList;
		}
		public void setColumnTypeList(List<ColumConfig> columnTypeList) {
			this.columnTypeList = columnTypeList;
		}
		public String getHot_time() {
			return hot_time;
		}
		public void setHot_time(String hot_time) {
			this.hot_time = hot_time;
		}
		public List<ColumConfig> getColumConfigList() {
			return columConfigList;
		}
		public void setColumConfigList(List<ColumConfig> columConfigList) {
			this.columConfigList = columConfigList;
		}
		public Integer getSort() {
			return sort;
		}
		public void setSort(Integer sort) {
			this.sort = sort;
		}
		private List<Image> imageList=new ArrayList<>(); //配图
		
		public List<Image> getImageList() {
			return imageList;
		}
		public void setImageList(List<Image> imageList) {
			this.imageList = imageList;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSite_id() {
			return site_id;
		}
		public void setSite_id(String site_id) {
			this.site_id = site_id;
		}
		public String getType_id() {
			return type_id;
		}
		public void setType_id(String type_id) {
			this.type_id = type_id;
		}
		public String getLink() {
			return link;
		}


		public void setLink(String link) {
			this.link = link;
		}


		public String getMessage_title() {
			return message_title;
		}


		public void setMessage_title(String message_title) {
			this.message_title = message_title;
		}


		public String getMessage_template() {
			return message_template;
		}


		public void setMessage_template(String message_template) {
			this.message_template = message_template;
		}


		public String getStatus() {
			return status;
		}


		public String getVideo_url() {
			return video_url;
		}
		public void setVideo_url(String video_url) {
			this.video_url = video_url;
		}
		public void setStatus(String status) {
			this.status = status;
		}


		public String getSurface_image() {
			return surface_image;
		}


		public void setSurface_image(String surface_image) {
			this.surface_image = surface_image;
		}


		public String getDetail() {
			return detail;
		}


		public void setDetail(String detail) {
			this.detail = detail;
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getKeyword() {
			return keyword;
		}


		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

		public String getAuthor() {
			return author;
		}


		public void setAuthor(String author) {
			this.author = author;
		}


		public String getResource() {
			return resource;
		}


		public void setResource(String resource) {
			this.resource = resource;
		}

		public String getCreated_time() {
			return created_time;
		}


		public void setCreated_time(String created_time) {
			this.created_time = created_time;
		}
		
		public List<String> getListImage() {
			return listImage;
		}
		
		public void setListImage(List<String> listImage) {
			this.listImage = listImage;
		}
		
		public List<String> getListVideo() {
			return listVideo;
		}
		
		public void setListVideo(List<String> listVideo) {
			this.listVideo = listVideo;
		}
		
		public String[] getMessageRelevantIdList() {
			return messageRelevantIdList;
		}

		public void setMessageRelevantIdList(String[] messageRelevantIdList) {
			this.messageRelevantIdList = messageRelevantIdList;
		}
		public List<NewMessageType> getMessageTypeList() {
			return messageTypeList;
		}
		public void setMessageTypeList(List<NewMessageType> messageTypeList) {
			this.messageTypeList = messageTypeList;
		}
		
		public String getType_name() {
			return type_name;
		}
		public void setType_name(String type_name) {
			this.type_name = type_name;
		}
		public String[] getMessagetypeids() {
			return messagetypeids;
		}
		public void setMessagetypeids(String[] messagetypeids) {
			this.messagetypeids = messagetypeids;
		}
		public String getRecommend() {
			return recommend;
		}
		public void setRecommend(String recommend) {
			this.recommend = recommend;
		}
		public String getHot() {
			return hot;
		}
		public void setHot(String hot) {
			this.hot = hot;
		}
		public List<Image> getPictures() {
			return pictures;
		}
		public void setPictures(List<Image> pictures) {
			this.pictures = pictures;
		}
		public List<Video> getVideos() {
			return videos;
		}
		public void setVideos(List<Video> videos) {
			this.videos = videos;
		}
		public List<MultipartFile> getImages() {
			return images;
		}
		public void setImages(List<MultipartFile> images) {
			this.images = images;
		}
		public List<MultipartFile> getFilms() {
			return films;
		}
		public void setFilms(List<MultipartFile> films) {
			this.films = films;
		}
		public String getMessage_templateName() {
			return message_templateName;
		}
		public void setMessage_templateName(String message_templateName) {
			this.message_templateName = message_templateName;
		}
		public String getTop() {
			return top;
		}
		public void setTop(String top) {
			this.top = top;
		}
		public String getRecommend_time() {
			return recommend_time;
		}
		public void setRecommend_time(String recommend_time) {
			this.recommend_time = recommend_time;
		}
		public String getTop_time() {
			return top_time;
		}
		public void setTop_time(String top_time) {
			this.top_time = top_time;
		}
		public Image getImage() {
			return image;
		}
		public void setImage(Image image) {
			this.image = image;
		}
		public String getBeforeId() {
			return beforeId;
		}
		public void setBeforeId(String beforeId) {
			this.beforeId = beforeId;
		}
		public String getAfterId() {
			return afterId;
		}
		public void setAfterId(String afterId) {
			this.afterId = afterId;
		}
		public String getBeforeTitle() {
			return beforeTitle;
		}
		public void setBeforeTitle(String beforeTitle) {
			this.beforeTitle = beforeTitle;
		}
		public String getAfterTitle() {
			return afterTitle;
		}
		public void setAfterTitle(String afterTitle) {
			this.afterTitle = afterTitle;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		public String getUpdate_time() {
			return update_time;
		}
		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}
		public String[] getColumtype() {
			return columtype;
		}
		public void setColumtype(String[] columtype) {
			this.columtype = columtype;
		}
		public List<Image> getAddpictures() {
			return addpictures;
		}
		public void setAddpictures(List<Image> addpictures) {
			this.addpictures = addpictures;
		}
		public List<Video> getAddvideos() {
			return addvideos;
		}
		public void setAddvideos(List<Video> addvideos) {
			this.addvideos = addvideos;
		}
		public List<MultipartFile> getAddimages() {
			return addimages;
		}
		public void setAddimages(List<MultipartFile> addimages) {
			this.addimages = addimages;
		}
		public List<MultipartFile> getAddfilms() {
			return addfilms;
		}
		public void setAddfilms(List<MultipartFile> addfilms) {
			this.addfilms = addfilms;
		}
		public String getValuetext() {
			return valuetext;
		}
		public void setValuetext(String valuetext) {
			this.valuetext = valuetext;
		}
		public List<MessageExtendWords> getPvtlist() {
			return pvtlist;
		}
		public void setPvtlist(List<MessageExtendWords> pvtlist) {
			this.pvtlist = pvtlist;
		}
		public String getSeokeywords() {
			return seokeywords;
		}
		public void setSeokeywords(String seokeywords) {
			this.seokeywords = seokeywords;
		}
		public String getSeodescription() {
			return seodescription;
		}
		public void setSeodescription(String seodescription) {
			this.seodescription = seodescription;
		}
		public String[] getColumnids() {
			return columnids;
		}
		public void setColumnids(String[] columnids) {
			this.columnids = columnids;
		}
		public String getColumid() {
			return columid;
		}
		public void setColumid(String columid) {
			this.columid = columid;
		}
		public String getColumname() {
			return columname;
		}
		public void setColumname(String columname) {
			this.columname = columname;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		
}
