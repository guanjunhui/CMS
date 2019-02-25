package cn.cebest.entity.system.solr;

public class Search {
	
	private String id;
	private String name;					
	private String des;
	private String columnId;
	private String type;
	private String time;
	private String typeId;
	private String columParentId;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Search [id=" + id + ", name=" + name + ", des=" + des + ", columnId=" + columnId + ", type=" + type
				+ "]";
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getColumParentId() {
		return columParentId;
	}
	public void setColumParentId(String columParentId) {
		this.columParentId = columParentId;
	}
	
	
	
	
}
