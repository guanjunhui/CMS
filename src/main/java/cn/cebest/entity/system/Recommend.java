package cn.cebest.entity.system;

public class Recommend{
	
	private static final long serialVersionUID = 7251028212486675411L;
	
	private String id;
	
    private String contentId;

    private String columId;

    private String typeId;

    private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getColumId() {
		return columId;
	}

	public void setColumId(String columId) {
		this.columId = columId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}