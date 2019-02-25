package cn.cebest.entity.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.util.PageData;

public class Banner implements Serializable {
	//基本属性字段
	private String id;//banner主键id
	private String banner_name;//banner名称
	private String banner_typeid;//广告属于什么类型
	private String banner_status;//banner状态 0锁定 1启用
	private String site_id;//广告所属站点id
	private String target_connection;//连接目标0原窗口 1新窗口
	private Integer forder;//排序
	private String columconfigid;//栏目id
	private String create_time;//banner的创建时间
	
	private Integer countimage;//记录图片数量
	private Integer countvideo;//记录视品数量
	private Integer countiv;//记录视频和图片的总量
	//图片和视频的处理字段
	private List<String> listImage=new ArrayList<String>();//一个banner保存很多图片的id
	private List<String> listVideo=new ArrayList<String>();//一个banner保存很多录像的id
	private List<Image> pictures;//一个banner包含很多图片
	private List<Image> addpictures;//在banner修改页面接收新增图片的信息
	private List<Video> videos;//一个banner包含很多录像
	private List<Video> addvideos;//在banner修改页面接收新增录像的信息
	private List<MultipartFile> images;//接收图片文件并上传
	private List<MultipartFile> addimages;//在修改页面,接收新增图片上传
	private List<MultipartFile> films;//接收视频文件并上传
	private List<MultipartFile> addfilms;//在修改页面接收新增视频文件并上传
	private String videotitle;
	private String videourl;
	
	//关系属性字段
	private List<ColumConfig> cols;//一个banner可能属于多个栏目	
	
	private String imageUrl;
	private String toUrl;
	private String title;
	private String subhead;
	private String bz;
	
	private PageData pd;
	
	public PageData getPd() {
		return pd;
	}
	public void setPd(PageData pd) {
		this.pd = pd;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	//属性的get/set方法
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBanner_name() {
		return banner_name;
	}
	public void setBanner_name(String banner_name) {
		this.banner_name = banner_name;
	}
	public String getBanner_typeid() {
		return banner_typeid;
	}
	public void setBanner_typeid(String banner_typeid) {
		this.banner_typeid = banner_typeid;
	}
	public String getBanner_status() {
		return banner_status;
	}
	public void setBanner_status(String banner_status) {
		this.banner_status = banner_status;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getTarget_connection() {
		return target_connection;
	}
	public void setTarget_connection(String target_connection) {
		this.target_connection = target_connection;
	}
	public Integer getForder() {
		return forder;
	}
	public void setForder(Integer forder) {
		this.forder = forder;
	}
	public String getColumconfigid() {
		return columconfigid;
	}
	public void setColumconfigid(String columconfigid) {
		this.columconfigid = columconfigid;
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
	public Integer getCountimage() {
		return countimage;
	}
	public void setCountimage(Integer countimage) {
		this.countimage = countimage;
	}
	public Integer getCountvideo() {
		return countvideo;
	}
	public void setCountvideo(Integer countvideo) {
		this.countvideo = countvideo;
	}
	public Integer getCountiv() {
		return countiv;
	}
	public void setCountiv(Integer countiv) {
		this.countiv = countiv;
	}
	public List<ColumConfig> getCols() {
		return cols;
	}
	public void setCols(List<ColumConfig> cols) {
		this.cols = cols;
	}
	public String getToUrl() {
		return toUrl;
	}
	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubhead() {
		return subhead;
	}
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getVideotitle() {
		return videotitle;
	}
	public void setVideotitle(String videotitle) {
		this.videotitle = videotitle;
	}
	public String getVideourl() {
		return videourl;
	}
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}
	
}
