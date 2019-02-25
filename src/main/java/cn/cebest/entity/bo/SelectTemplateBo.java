package cn.cebest.entity.bo;

import java.io.Serializable;

public class SelectTemplateBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String ifColumOrType;//1:栏目 0：分类

	private String temPlatePath;
	
	private String columId;//被选中的栏目ID或者分类所属栏目ID
	
	private String columName;
	
	private String columParentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemPlatePath() {
		return temPlatePath;
	}

	public void setTemPlatePath(String temPlatePath) {
		this.temPlatePath = temPlatePath;
	}

	public String getColumId() {
		return columId;
	}

	public void setColumId(String columId) {
		this.columId = columId;
	}

	public String getColumParentId() {
		return columParentId;
	}

	public void setColumParentId(String columParentId) {
		this.columParentId = columParentId;
	}

	public String getColumName() {
		return columName;
	}

	public void setColumName(String columName) {
		this.columName = columName;
	}

	public String getIfColumOrType() {
		return ifColumOrType;
	}

	public void setIfColumOrType(String ifColumOrType) {
		this.ifColumOrType = ifColumOrType;
	}
	
}
