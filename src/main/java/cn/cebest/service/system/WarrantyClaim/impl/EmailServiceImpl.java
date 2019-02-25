package cn.cebest.service.system.WarrantyClaim.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;


import cn.cebest.service.system.WarrantyClaim.EmailService;
import cn.cebest.util.MD5;
import cn.cebest.util.redis.RedisUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年7月17日
 * @company 中企高呈
 */
@Service
public class EmailServiceImpl implements EmailService {

	private String myAccount = "service@315job.com";

	private String myEmailPassword="Dfhj123456";

	//private String protocol="smtp";

	private String host="smtp.mxhichina.com";
	
	private String port="465";

	/*@Autowired
	private RedisService redisService;*/

	private String mail_autograph = "shaoyin";
	
	
	private String acz = "/user/forgetPassword";
	
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory"; 
	
	
	public void sendMail(String tomail, String content, String subject,HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.enable", "true");
		//properties.setProperty("mail.transport.protocol", protocol);// 使用协议
		properties.setProperty("mail.smtp.host", host);// 发件人的邮箱SMTP服务器地址
		properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);  
		properties.put("mail.smtp.socketFactory.fallback", "false");  
		properties.setProperty("mail.smtp.port", port);
		properties.put("mail.smtp.socketFactory.port", port);
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(properties, new Authenticator(){  
            @Override  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(myAccount, myEmailPassword);  
            }});  
		
		MimeMessage message = new MimeMessage(session);
		// from 发送人
		message.setFrom(new InternetAddress(myAccount, mail_autograph, "UTF-8"));

		// to 收信人
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(tomail, "CC", "UTF-8"));

		// 主题
		message.setSubject(subject, "UTF-8");

		// 正文
		message.setContent(content, "text/html;charset=utf-8");

		message.setSentDate(new Date());

		message.saveChanges();

		// 发送邮箱
		
		Transport.send(message);
	}
	
	@Override
	public void sendPassword(String email, String password,HttpServletRequest request) throws Exception{
			String scheme = request.getScheme();
			String serverName = request.getServerName();
			int _port = request.getServerPort();
			String mail_callback_retrieve = scheme + "://" + serverName + ":" + _port + acz;
		
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("email", email);
			map.put("password", password);
			
			
			// 生成token
			String token = MD5.md5(email);
			// 将token保存到缓存
			//redisService.setEx(Constants.RETRIEVE_PASSWORD_KEY + token, DateUtil.SECONDS_ONE_DAY, JSON.toJSONString(map));
			RedisUtil.setString(token, email);
			//设置token失效性
			RedisUtil redisUtil = new RedisUtil();
			redisUtil.expire(token, 24*60*60);
			
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("点击下面链接重设密码，24小时生效，链接只能使用一次，请尽快修改！</br>");
			sBuffer.append(String.format("<a href=\"%s?token=", mail_callback_retrieve));
			sBuffer.append(token);
			sBuffer.append(String.format("\">%s?token=", mail_callback_retrieve));
			sBuffer.append(token);
			sBuffer.append("</a>");
			// 发送邮件
			sendMail(email, sBuffer.toString(), "邵音找回密码邮件",request);
	}
}
