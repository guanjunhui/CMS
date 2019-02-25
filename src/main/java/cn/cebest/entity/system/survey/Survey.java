package cn.cebest.entity.system.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 调查实体类
 * @author qiaozhipeng
 *
 */
public class Survey implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer survey_id; //调查 id

	private String survey_name;//调查名称

	private String user_id; //创建人的用户id
	private String creat_time;
	private Integer status;
	private List<Question> questionList = new ArrayList<Question>(); //问题集合

	public Survey() {
		super();
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	public Integer getSurvey_id() {
		return survey_id;
	}


	public void setSurvey_id(Integer survey_id) {
		this.survey_id = survey_id;
	}


	public String getSurvey_name() {
		return survey_name;
	}


	public void setSurvey_name(String survey_name) {
		this.survey_name = survey_name;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getCreat_time() {
		return creat_time;
	}


	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}

}