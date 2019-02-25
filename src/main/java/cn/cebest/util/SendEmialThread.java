package cn.cebest.util;

import cn.cebest.util.EmailUtil;
import java.util.Map;

/**
 * @ClassName SendEmialThread
 * @Description TODO
 * @Author Roger
 * @Date 2018/8/27 19:58
 * @Company 中企高呈
 **/
public class SendEmialThread extends Thread{
	protected Logger logger = Logger.getLogger(this.getClass()); 
    private EmailInfo info;
    public SendEmialThread(){

    }
    public SendEmialThread(EmailInfo info){
        this.info = info;
    }
    @Override
    public void run() {
        EmailUtil emailUtil = new EmailUtil();
        try {
            emailUtil.sendMail(info);
        }catch (Exception e){
        	logger.error("发送邮件出错：",e);
        }
        super.run();
    }
}
