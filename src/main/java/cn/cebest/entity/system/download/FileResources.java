package cn.cebest.entity.system.download;

import java.util.ArrayList;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Seo;
import cn.cebest.util.ExtendFiledUtil;

public class FileResources {
	private String fileid;
	private String name;
	private String typeid;
	private String imageid;
	private String templateid;
	private String tem_name;
	private String summary;
	private String created_time;
	private String update_time;
	private String wburl;
	private String keywords;
	private String title;
	private Long download_count;
	private String imgurl;
	private String top;
	private String recommend_time;
	private String top_time;
	private String recommend;
	private String hot_time;
	private String hot;//热标识
	private String siteid;
	private String i_title;
	private List<String> columnids;
	private String[] fileids;
	private String TXT;
	private String[] typeids;
	private List<DownloadFiles> files;
	private List<Integer> filesIds;
	private String status;
	private Integer sort;
	private List<FileType> fileTypeList;
	private List<ColumConfig> columConfigList;
	private List<ExtendFiledUtil> fileds=new ArrayList<>();
	private String filedJson;
	private Seo seoInfo;
	private String seo_title;
	private String seo_description;
	private List<Image> timageList=new ArrayList<>(); //多张图(最初)
	private List<String> imageids=new ArrayList<>();//id集合
	private List<Image> pictureList= new ArrayList<>();//修改时新增的图片接的
	private String contentUrlName;
	private String columUrlName;
	private String download_id;
	private List<DownloadFiles> downloadMark = new ArrayList<>();//下载文件标记(添加存放标记值)
	private List<DownloadFiles> anotherMark = new ArrayList<>();//下载文件标记(存放修改时标记值)
	
	
	
	

	public List<DownloadFiles> getAnotherMark() {
		return anotherMark;
	}

	public void setAnotherMark(List<DownloadFiles> anotherMark) {
		this.anotherMark = anotherMark;
	}

	public List<DownloadFiles> getDownloadMark() {
		return downloadMark;
	}

	public void setDownloadMark(List<DownloadFiles> downloadMark) {
		this.downloadMark = downloadMark;
	}

	public String getDownload_id() {
		return download_id;
	}

	public void setDownload_id(String download_id) {
		this.download_id = download_id;
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
	public List<Image> getPictureList() {
		return pictureList;
	}

	public void setPictureList(List<Image> pictureList) {
		this.pictureList = pictureList;
	}

	
	

	public List<String> getImageids() {
		return imageids;
	}

	public void setImageids(List<String> imageids) {
		this.imageids = imageids;
	}

	public List<Image> getTimageList() {
		return timageList;
	}

	public void setTimageList(List<Image> timageList) {
		this.timageList = timageList;
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

	public List<String> getColumnids() {
		return columnids;
	}

	public void setColumnids(List<String> columnids) {
		this.columnids = columnids;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public String getSeo_description() {
		return seo_description;
	}

	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}

	public List<ExtendFiledUtil> getFileds() {
		return fileds;
	}

	public void setFileds(List<ExtendFiledUtil> fileds) {
		this.fileds = fileds;
	}

	public String getFiledJson() {
		return filedJson;
	}

	public void setFiledJson(String filedJson) {
		this.filedJson = filedJson;
	}

	private List<KeyValuesUtil> objKey_Value;
	
	public List<KeyValuesUtil> getObjKey_Value() {
		return objKey_Value;
	}

	public void setObjKey_Value(List<KeyValuesUtil> objKey_Value) {
		this.objKey_Value = objKey_Value;
	}

	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}

	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}

	public List<Integer> getFilesIds() {
		return filesIds;
	}

	public void setFilesIds(List<Integer> filesIds) {
		this.filesIds = filesIds;
	}

	public String getTem_name() {
		return tem_name;
	}

	public void setTem_name(String tem_name) {
		this.tem_name = tem_name;
	}

	public String getI_title() {
		return i_title;
	}

	public void setI_title(String i_title) {
		this.i_title = i_title;
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

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public List<FileType> getFileTypeList() {
		return fileTypeList;
	}

	public void setFileTypeList(List<FileType> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}

	public Long getDownload_count() {
		return download_count;
	}

	public void setDownload_count(Long download_count) {
		this.download_count = download_count;
	}

	public List<DownloadFiles> getFiles() {
		return files;
	}

	public void setFiles(List<DownloadFiles> files) {
		this.files = files;
	}

	public String[] getTypeids() {
		return typeids;
	}

	public void setTypeids(String[] typeids) {
		this.typeids = typeids;
	}

	public String getTXT() {
		return TXT;
	}

	public void setTXT(String tXT) {
		TXT = tXT;
	}

	public String[] getFileids() {
		return fileids;
	}

	public void setFileids(String[] fileids) {
		this.fileids = fileids;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getWburl() {
		return wburl;
	}

	public void setWburl(String wburl) {
		this.wburl = wburl;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
