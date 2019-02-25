package cn.cebest.entity.system.comment;

import java.io.Serializable;

/**
 * 评论
 * @author liqk
 *
 */
public class Comment implements Serializable {

	private static final long serialVersionUID = -3204318227759554234L;

	private String id;
	private String commentPropertyId;
	private String userId;
	private int readStatus;
	private int commentScore;
	private String email;
	private int clientType;
	private String createTime;
	private int status;
	private String commentContent;
	private CommentProperty commentProperty;
	
	/**
	 * 未映射数据库字段的属性
	 * 
	 */
	private String memNick;		//昵称
	private String memImgPath;		//头像路径
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCommentPropertyId() {
		return commentPropertyId;
	}
	public void setCommentPropertyId(String commentPropertyId) {
		this.commentPropertyId = commentPropertyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}
	public int getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(int commentScore) {
		this.commentScore = commentScore;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public CommentProperty getCommentProperty() {
		return commentProperty;
	}
	public void setCommentProperty(CommentProperty commentProperty) {
		this.commentProperty = commentProperty;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getMemNick() {
		return memNick;
	}
	public void setMemNick(String memNick) {
		this.memNick = memNick;
	}
	public String getMemImgPath() {
		return memImgPath;
	}
	public void setMemImgPath(String memImgPath) {
		this.memImgPath = memImgPath;
	}
	
}
