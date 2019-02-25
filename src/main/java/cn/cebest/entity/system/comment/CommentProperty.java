package cn.cebest.entity.system.comment;

import java.io.Serializable;

/**
 * 评论属性
 * @author liqk
 *
 */
public class CommentProperty implements Serializable {

	private static final long serialVersionUID = 6070100790903344517L;

	private String id;
	private int commentType;
	private String dictionariesId;//分类id(数据字典维护)
	private String relevanceId;//关联的id(机构id)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCommentType() {
		return commentType;
	}
	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}
	public String getDictionariesId() {
		return dictionariesId;
	}
	public void setDictionariesId(String dictionariesId) {
		this.dictionariesId = dictionariesId;
	}
	public String getRelevanceId() {
		return relevanceId;
	}
	public void setRelevanceId(String relevanceId) {
		this.relevanceId = relevanceId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
