package cn.cebest.entity.vo;

import java.io.Serializable;

public class RecommendVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String type;
	private String columType;
	private String columId;
	private String contentId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumType() {
		return columType;
	}
	public void setColumType(String columType) {
		this.columType = columType;
	}
	public String getColumId() {
		return columId;
	}
	public void setColumId(String columId) {
		this.columId = columId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	
}
