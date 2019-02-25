package cn.cebest.service.system.WarrantyClaim;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author wangweijie
 * @Date 2018年7月17日
 * @company 中企高呈
 */
public interface EmailService {
	public void sendPassword(String email, String password,HttpServletRequest request) throws Exception;
	
	public void sendMail(String tomail, String content, String subject,HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException;
}
