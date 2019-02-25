package cn.cebest.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author wangweijie
 * @Date 2018年7月25日
 * @company 中企高呈
 */
public class EmailUtil {
	
	private String protocol="SMTP";
	
	public void sendMail(final EmailInfo info)
				throws UnsupportedEncodingException, javax.mail.MessagingException {

	 	Properties properties = new Properties();
	 	properties.put("mail.smtp.ssl.enable", "false");
		properties.setProperty("mail.smtp.host", info.getHost());// 发件人的邮箱SMTP服务器地址
		properties.setProperty("mail.smtp.port", info.getPort());
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.transport.protocol", protocol);// 使用协议
		 Session session = Session.getDefaultInstance(properties, new Authenticator(){
			 @Override
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication(info.getAccount(), info.getPassword());
			 }});


		 MimeMessage message = new MimeMessage(session);
			// from 发送人
			message.setFrom(new InternetAddress(info.getAccount(), info.getAutograph(), "UTF-8"));

			// to 收信人
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(info.getTomail(),"admin", "UTF-8"));

			// 主题
			message.setSubject(info.getSubject(), "UTF-8");

			// 正文
			message.setContent(info.getContent(), "text/html;charset=utf-8");

			message.setSentDate(new Date());

			message.saveChanges();

			// 发送邮箱
			
			Transport.send(message);
		}
}
