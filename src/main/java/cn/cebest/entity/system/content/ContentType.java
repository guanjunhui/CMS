package cn.cebest.entity.system.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Template;

/**
 * 内容分类
 * @author mingpinglin<mingpinglin@300.cn>
 *
 */
public class ContentType {
	
	private static final long serialVersionUID = -3118882611666749785L;
	private String id;
	private String typeName; //分类名称
	private String siteId; //站点id
	private String imageId; //分类图片id
	private String imgurl; //分类图片的路径
	private String title; //图片标题
	private String typeStatus; //分类状态
	private String typeTxt;  //分类的详情描述
	private String typeKeywords; //seo关键词
	private String typeDescription; //seo概述
	private String typeSummary; //概要描述
	private String pId; //分类父id
	private String pName; //分类父名字
	private	String typeWburl; //外部链接
	private String columnId; //栏目id
	private String templateId; //模板id
	private Integer sort; //排序
	private Date createTime; //创建时间
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间
	private String typeIp; //经纬度
	private String typePosition; //地址名称
	private Integer count; //数量
	private List<ContentType> childList; //子级分类集合
	private String [] columnids; //栏目id数组
	private Template template=new Template(); //模板实体
	private List<Image> imageList=new ArrayList<>(); //分类图片集合
	private List<ColumConfig> columConfigList=new ArrayList<>(); //栏目集合
	private Map contentTypeMap = new HashMap<>();
	private String typeUrlName;
	
	public String getTypeUrlName() {
		return typeUrlName;
	}

	public void setTypeUrlName(String typeUrlName) {
		this.typeUrlName = typeUrlName;
	}
	public String getTypeIp() {
		return typeIp;
	}
	public void setTypeIp(String typeIp) {
		this.typeIp = typeIp;
	}
	public String getTypePosition() {
		return typePosition;
	}
	public void setTypePosition(String typePosition) {
		this.typePosition = typePosition;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypeStatus() {
		return typeStatus;
	}
	public void setTypeStatus(String typeStatus) {
		this.typeStatus = typeStatus;
	}
	public String getTypeTxt() {
		return typeTxt;
	}
	public void setTypeTxt(String typeTxt) {
		this.typeTxt = typeTxt;
	}
	public String getTypeKeywords() {
		return typeKeywords;
	}
	public void setTypeKeywords(String typeKeywords) {
		this.typeKeywords = typeKeywords;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public String getTypeSummary() {
		return typeSummary;
	}
	public void setTypeSummary(String typeSummary) {
		this.typeSummary = typeSummary;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getTypeWburl() {
		return typeWburl;
	}
	public void setTypeWburl(String typeWburl) {
		this.typeWburl = typeWburl;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<ContentType> getChildList() {
		return childList;
	}
	public void setChildList(List<ContentType> childList) {
		this.childList = childList;
	}
	public String[] getColumnids() {
		return columnids;
	}
	public void setColumnids(String[] columnids) {
		this.columnids = columnids;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public List<Image> getImageList() {
		return imageList;
	}
	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}
	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}
	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}
	public Map getContentTypeMap() {
		return contentTypeMap;
	}
	public void setContentTypeMap(Map contentTypeMap) {
		this.contentTypeMap = contentTypeMap;
	}
	
}
