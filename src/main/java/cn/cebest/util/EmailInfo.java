package cn.cebest.util;

/**
 *
 * @author wangweijie
 * @Date 2018年11月29日
 * @company 中企高呈
 */
public class EmailInfo {
	
	private String host;//主机
	private String port;//端口
	private String account;//账户名
	private String password;//密码
	private String autograph;//发件人签名
	private String tomail;//发送人
	private String subject;//主题
	private String content;//内容
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAutograph() {
		return autograph;
	}
	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}
	public String getTomail() {
		return tomail;
	}
	public void setTomail(String tomail) {
		this.tomail = tomail;
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
	@Override
	public String toString() {
		return "EmailInfo [host=" + host + ", port=" + port + ", account=" + account + ", password=" + password
				+ ", autograph=" + autograph + ", tomail=" + tomail + ", subject=" + subject + ", content=" + content
				+ "]";
	}
	
	
}
