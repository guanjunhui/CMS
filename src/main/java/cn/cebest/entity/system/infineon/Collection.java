package cn.cebest.entity.system.infineon;

import java.io.Serializable;

/**
 * 收藏实体类
 *
 */
public class Collection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String userId;//用户id
	
	private String resourceId;//资源id
	
	private String resourceType;//资源类型 1解决方案   2合作伙伴信息   3视频  4文档
	
	private String createTime;//创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
