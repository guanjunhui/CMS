package cn.cebest.entity.system.survey;

import java.io.Serializable;

/**
 * 答案实体类
 * @author qiaozhipeng
 *
 */
public class Answer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer answer_id; //答案id

	private String content; //答案内容(简答题,单选题,多选题)

	private String uuid; //区分是否属于同一次参与调查的答案
	private String user_id; //创建人的用户id
	private String user_name; 
	private Integer question_id; //问题id
	private String create_time;
	private Integer survey_id; //调查id
	private Integer status; //调查id

	public Answer(String content, String uuid, String user_id, Integer question_id, String create_time,
			Integer survey_id) {
		super();
		this.content = content;
		this.uuid = uuid;
		this.user_id = user_id;
		this.question_id = question_id;
		this.create_time = create_time;
		this.survey_id = survey_id;
	}

	public Answer() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid == null ? null : uuid.trim();
	}

	public Integer getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(Integer answer_id) {
		this.answer_id = answer_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Integer getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Integer question_id) {
		this.question_id = question_id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Integer getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(Integer survey_id) {
		this.survey_id = survey_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}