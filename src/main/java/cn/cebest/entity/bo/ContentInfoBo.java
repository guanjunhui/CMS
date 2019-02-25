package cn.cebest.entity.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
/**
 * 
 */
public class ContentInfoBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;

	private String fileUrl;
	
	private String creatTime;
	
	private String columId;//内容所属栏目或者类别ID
	
	private Collection<ColumConfig> columList;//内容所属栏目name集合
	private Collection<TypeInfoBo> typeList;//内容所属分类name集合
	
	private String type;
	
	private String description;//概要描述
	
	private String imageUrl;//图片（可存封面图片）
	
	private String updateTime;
	
	private String topTime;
	
	private String top;//是否置顶
	
	private String recommendTime;
	
	private String recommend;//推荐标识

	private String hotTime;
	
	private String hot;//热标识
	
	private String status;//内容状态（内容状态(0:启用,1:停用)）
	
	private Integer sort;//排序值
	
	private Long download_count;
	
	private String tourl;
	
	private List<Image> imageList;
	
	

	

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public String getTourl() {
		return tourl;
	}

	public void setTourl(String tourl) {
		this.tourl = tourl;
	}

	public Long getDownload_count() {
		return download_count;
	}

	public void setDownload_count(Long download_count) {
		this.download_count = download_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getColumId() {
		return columId;
	}

	public void setColumId(String columId) {
		this.columId = columId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getTopTime() {
		return topTime;
	}

	public void setTopTime(String topTime) {
		this.topTime = topTime;
	}

	public Collection<ColumConfig> getColumList() {
		return columList;
	}

	public void setColumList(Collection<ColumConfig> columList) {
		this.columList = columList;
	}

	public Collection<TypeInfoBo> getTypeList() {
		return typeList;
	}

	public void setTypeList(Collection<TypeInfoBo> typeList) {
		this.typeList = typeList;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getHotTime() {
		return hotTime;
	}

	public void setHotTime(String hotTime) {
		this.hotTime = hotTime;
	}

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
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
	
}
