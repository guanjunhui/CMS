package cn.cebest.entity.system;

import java.io.Serializable;

public class EmployField implements Serializable{
	
	private static final long serialVersionUID = -1L;
	
	//主键ID 自增长类型
	private int id;
	
	//默认名称
	private String default_name;
	
	//显示名称
	private String display_name;
	
	//获取对象的关键字
	private String keyword;	
	
	//是否显示该字段 0为隐藏 1为显示
	private int isdisplay;
	
	//字段排序的值
	private int sort;
	
	//类型（可用于显示中英文不同内容）
	private int type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDefault_name() {
		return default_name;
	}
	public void setDefault_name(String default_name) {
		this.default_name = default_name;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getIsdisplay() {
		return isdisplay;
	}
	public void setIsdisplay(int isdisplay) {
		this.isdisplay = isdisplay;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
