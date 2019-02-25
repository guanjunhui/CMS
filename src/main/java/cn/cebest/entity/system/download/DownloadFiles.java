package cn.cebest.entity.system.download;

public class DownloadFiles {
	private String fileid;
	private Integer id;
	private String name;
	private String type;
	private String filepach;
	private	String mark;//标记
	private String size;//文件大小
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilepach() {
		return filepach;
	}

	public void setFilepach(String filepach) {
		this.filepach = filepach;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
