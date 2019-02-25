package cn.cebest.entity.system.newMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageExtendWords implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//主键id
	private String name;//扩展字段的名字
	private String type;//栏目类型
	private String pid;//扩展字段的上一级
	private String fieldtype;//扩展字段的类型
	private String value;//存储动态文本框的值
	private Integer sort;//排序值
	private List<MessageExtendWords> childList=new ArrayList<>();
	
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public List<MessageExtendWords> getChildList() {
		return childList;
	}
	public void setChildList(List<MessageExtendWords> childList) {
		this.childList = childList;
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
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
