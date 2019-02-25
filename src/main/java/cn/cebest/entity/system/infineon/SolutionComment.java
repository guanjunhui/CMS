package cn.cebest.entity.system.infineon;

import java.io.Serializable;
import java.util.List;

/**
 * 评论实体类
 *
 */
public class SolutionComment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String resourceId;//资源id
	
	private String parentId;//父id
	
	private String userId;//评论者id
	
	private String content;//评论内容
	
	private String toUserId;//被评论者ID
	
	private String createTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

}
