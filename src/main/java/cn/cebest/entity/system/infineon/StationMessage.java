package cn.cebest.entity.system.infineon;

import java.io.Serializable;

/**
 * 站内消息实体类
 *
 */
public class StationMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String content;//消息内容
	
	private String toUserId;//收信人id
	
	private String isRead;//是否已读 1是   0否
	
	private String createTime;//发送时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
