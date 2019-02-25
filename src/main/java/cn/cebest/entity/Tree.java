package cn.cebest.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String pid;
	private String name;
	private String text;
	private String groupId;
	private int childLevel;//该节点处于第几层
	private List<Tree> childList;
	private String type;//节点类型
	private String url;//节点地址
	
	//扩展属性
	private Map<String,Object> attribute = new HashMap<String,Object>();
		
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Tree(){}
	
	public Tree(String id,String name,String pid){
		this.id=id;
		this.name=name;
		this.pid=pid;
	}
	
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getChildLevel() {
		return childLevel;
	}

	public void setChildLevel(int childLevel) {
		this.childLevel = childLevel;
	}

	public List<Tree> getChildList() {
		return childList;
	}
	public void setChildList(List<Tree> childList) {
		this.childList = childList;
	}

}