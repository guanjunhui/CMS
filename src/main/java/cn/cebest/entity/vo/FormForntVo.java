package cn.cebest.entity.vo;

import java.io.Serializable;
import java.util.Map;

public class FormForntVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,Object> itemsData;//表单数据
	private String code;//验证码
	private EmailSendVo emailInfo;//邮件信息
	
	public Map<String, Object> getItemsData() {
		return itemsData;
	}
	public void setItemsData(Map<String, Object> itemsData) {
		this.itemsData = itemsData;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public EmailSendVo getEmailInfo() {
		return emailInfo;
	}
	public void setEmailInfo(EmailSendVo emailInfo) {
		this.emailInfo = emailInfo;
	}
}
