package cn.cebest.entity.vo;

import java.io.Serializable;

/**
 * 邮件发送信息
 * */
public class EmailSendVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String toEmail;//邮件接收者
	private String subject;//邮件主题
	private String content;//邮件内容
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
