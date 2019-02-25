package cn.cebest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TemplateTree implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private boolean hasDefault;
	private List<TemplateTree> childList=new ArrayList<TemplateTree>();
	
	public TemplateTree(){}
	
	public TemplateTree(String id,String name){
		this.id=id;
		this.name=name;
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
	public List<TemplateTree> getChildList() {
		return childList;
	}

	public void setChildList(List<TemplateTree> childList) {
		this.childList = childList;
	}

	public boolean isHasDefault() {
		return hasDefault;
	}

	public void setHasDefault(boolean hasDefault) {
		this.hasDefault = hasDefault;
	}


}