package cn.cebest.util;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{

	private static final long serialVersionUID = 7905434167533534358L;
	private String commentTitle;//评论内容
	private String commentId;//评论ID
	private int sonCount;//子评论数量
	private List<Message> sonList;
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public int getSonCount() {
		return sonCount;
	}
	public void setSonCount(int sonCount) {
		this.sonCount = sonCount;
	}
	public List<Message> getSonList() {
		return sonList;
	}
	public void setSonList(List<Message> sonList) {
		this.sonList = sonList;
	}
	
}
