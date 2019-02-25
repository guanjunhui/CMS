package cn.cebest.entity.system.infineon;

import java.util.List;

public class SolutionCommentVO extends SolutionComment{

	private static final long serialVersionUID = 1L;
	
	private List<String> ATusers;
	
	private List<SolutionCommentVO> commentList;
	
	private String resourceName;
	
	private Integer likeNum;  //点赞数量
	
	private Integer replyNum; //回复数量
	
	private String status; //当前用户是否点赞
	
	private String username;
	
	private String toUserName;
	
	private String type;
	
	private String contentId;
	
	private String path;
	
	private String photoUrl;
	
	private String resouceTitle;
	
	private String keyWords;

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getATusers() {
		return ATusers;
	}

	public void setATusers(List<String> aTusers) {
		ATusers = aTusers;
	}

	public List<SolutionCommentVO> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<SolutionCommentVO> commentList) {
		this.commentList = commentList;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(Integer replyNum) {
		this.replyNum = replyNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getResouceTitle() {
		return resouceTitle;
	}

	public void setResouceTitle(String resouceTitle) {
		this.resouceTitle = resouceTitle;
	}
	
	
}
