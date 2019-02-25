package cn.cebest.entity.system.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 问题实体类
 * @author Qiaozhipeng
 *
 */
public class Question implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer question_id; //问题id

	private String question_name; //问题名称
	private Integer question_type; //问题类型:0-单选,1-多选,2-简答
	private Integer survey_id; 
	private String options; //问题选项(单选和多选)
	/*----------------用于接收用户提交的结果数据-----------------------*/
	private List<Answer> answerList = new ArrayList<Answer>(); //答案集合
	public Integer getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Integer question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_name() {
		return question_name;
	}

	public void setQuestion_name(String question_name) {
		this.question_name = question_name;
	}

	public Integer getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(Integer question_type) {
		this.question_type = question_type;
	}


	public String getOptions() {
		return options;
	}

	//==============设置特殊操作-开始===================================
	public void setOptions(String options) {
		if(this.options==null){
			this.options = "";
		}
		//1.先将所有的换行替换为逗号
		String temp = options.replaceAll("\r\n",",");
		
		//2.在循环中将连着的两个逗号替换为一个逗号
		while(temp.contains(",,")){
			temp = temp.replaceAll(",,",",");
		}
		//3.去掉字符串中前后有可能出现的逗号
		temp = removeComma(temp,","); 
		this.options = temp ;
	}
	
	public String getOptionsForEdit(){ //${question.optionsForEdit}
		if(this.options==null){
			return "" ;
		}
		return this.options.replace(",", "\r\n");
	}
	
	public String[] getOptionsArr(){
		if(this.options==null){
			return null ;
		}
		return this.options.split(",");
	}

	
	//去掉字符串中前后有可能出现的逗号
	private static String removeComma(String temp,String operator) {
		if(temp==null || temp.length()==0){
			return temp ;
		}
		
		//判断当前字符串中是否以逗号开头
		if(temp.startsWith(operator)){
			temp = temp.substring(1);
		}
		
		//判断当前字符串中是否以逗号结尾
		if(temp.endsWith(operator)){
			temp = temp.substring(0, temp.length()-1);//半开，半闭
		}
		return temp;
	}

	public Integer getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(Integer survey_id) {
		this.survey_id = survey_id;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}
}