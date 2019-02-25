package cn.cebest.entity.system.infineon;

import java.io.Serializable;

public class KeyWords implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String keyWords;//关键词
	
	private String status;//是否启用
	
	private String createTime;//创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
