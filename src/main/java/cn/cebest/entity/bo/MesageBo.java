package cn.cebest.entity.bo;

import cn.cebest.filter.startFilter;
import cn.cebest.util.SystemConfig;

public class MesageBo {
	
	private static final String templateCode;//短信模板-可在短信控制台中找到
	private static final String pwdtemplateCode; // 修改密码模板
	private static final String updtemplateCode;// 信息变更模板
	static{
		templateCode=SystemConfig.getPropertiesString("mesage.templateCode");
		pwdtemplateCode = SystemConfig.getPropertiesString("mesage.password.templateCode");
		updtemplateCode = SystemConfig.getPropertiesString("mesage.updateInfo.templateCode");
	}
	
	private static final String signName;//短信签名-可在短信控制台中找到
	static{
		signName=SystemConfig.getPropertiesString("mesage.signName");
	}
	private String phoneNumbers;//必填:待发送手机号
	private String templateParam;//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,
	private String outId;
	
	public String getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public String getTemplateParam() {
		return templateParam;
	}
	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
	public String getTemplatecode() {
		return templateCode;
	}
	public String getSignname() {
		return signName;
	}
	public static String getPwdtemplatecode() {
		return pwdtemplateCode;
	}
	public static String getUpdtemplatecode() {
		return updtemplateCode;
	}
	
	
}
