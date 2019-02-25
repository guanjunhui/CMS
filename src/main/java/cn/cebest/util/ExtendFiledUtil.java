package cn.cebest.util;

import java.io.Serializable;

public class ExtendFiledUtil implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String value;
	private String pid;
	private String fieldtype;
	private String type;
	private Integer sort;
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
