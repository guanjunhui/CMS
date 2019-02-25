package cn.cebest.entity.bo;

import java.io.Serializable;
import java.util.List;

public class TypeInfoBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String typeNameSub;
	
	private Integer sort;
	
	private String temPlatePath;

	private String type;

	private String columId;
	
	private String columName;

	private String summary;
	
	private String type_status; //状态没用着:改为字体颜色选择 1:黑色,2白色
	
	private String status;//0:隐藏、1:显示、3:删除
	
	private String type_txt;
	
	private String imageUrl;
	
	private String pid;
	
	private long count;//包含内容数量
	
	private List<TypeInfoBo> childList;
	
	private String typeUrlName;
	
	public String getTypeUrlName() {
		return typeUrlName;
	}

	public void setTypeUrlName(String typeUrlName) {
		this.typeUrlName = typeUrlName;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public String getTypeNameSub() {
		return typeNameSub;
	}

	public void setTypeNameSub(String typeNameSub) {
		this.typeNameSub = typeNameSub;
	}

	public String getColumName() {
		return columName;
	}

	public void setColumName(String columName) {
		this.columName = columName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public List<TypeInfoBo> getChildList() {
		return childList;
	}

	public void setChildList(List<TypeInfoBo> childList) {
		this.childList = childList;
	}

	public String getTemPlatePath() {
		return temPlatePath;
	}

	public void setTemPlatePath(String temPlatePath) {
		this.temPlatePath = temPlatePath;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColumId() {
		return columId;
	}

	public void setColumId(String columId) {
		this.columId = columId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getType_txt() {
		return type_txt;
	}

	public void setType_txt(String type_txt) {
		this.type_txt = type_txt;
	}

	public String getType_status() {
		return type_status;
	}

	public void setType_status(String type_status) {
		this.type_status = type_status;
	}
	
}
