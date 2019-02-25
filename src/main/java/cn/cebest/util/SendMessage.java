package cn.cebest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import cn.cebest.entity.bo.MesageBo;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 */
public class SendMessage {

	private static Logger logger = LoggerFactory.getLogger(SendMessage.class);

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId;
    
    static{
    	accessKeyId=SystemConfig.getPropertiesString("mesage.accessKeyId");
    }
    static final String accessKeySecret;
    static{
    	accessKeySecret=SystemConfig.getPropertiesString("mesage.accessKeySecret");
    }

    public static SendSmsResponse sendSms(SendSmsRequest request) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId,String phoneNumber) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    /**
     * 
     * @param mesageBo
     * @param templateType 默认 :用户注册 , 2 :修改密码  , 3 : 信息变更
     * @throws ClientException
     * @throws InterruptedException
     */
    public static void sendMessage(MesageBo mesageBo,Integer templateType) throws ClientException, InterruptedException {

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mesageBo.getPhoneNumbers());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(mesageBo.getSignname());
        //必填:短信模板-可在短信控制台中找到
        switch (templateType) {
		case 2:
			request.setTemplateCode(mesageBo.getPwdtemplatecode());
			break;
		case 3:
			request.setTemplateCode(mesageBo.getUpdtemplatecode());
			break;	
		default:
			request.setTemplateCode(mesageBo.getTemplatecode());
			break;
		}
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"
        request.setTemplateParam(mesageBo.getTemplateParam());
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(mesageBo.getOutId());

        //发短信
        SendSmsResponse response = sendSms(request);
        logger.info("短信接口返回的数据----------------");
        logger.info("Code=" + response.getCode());
        logger.info("Message=" + response.getMessage());
        logger.info("RequestId=" + response.getRequestId());
        logger.info("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId(),mesageBo.getPhoneNumbers());
            logger.info("短信明细查询接口返回数据----------------");
            logger.info("Code=" + querySendDetailsResponse.getCode());
            logger.info("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
            	logger.info("SmsSendDetailDTO["+i+"]:");
            	logger.info("Content=" + smsSendDetailDTO.getContent());
            	logger.info("ErrCode=" + smsSendDetailDTO.getErrCode());
            	logger.info("OutId=" + smsSendDetailDTO.getOutId());
            	logger.info("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
            	logger.info("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
            	logger.info("SendDate=" + smsSendDetailDTO.getSendDate());
            	logger.info("SendStatus=" + smsSendDetailDTO.getSendStatus());
            	logger.info("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            logger.info("TotalCount=" + querySendDetailsResponse.getTotalCount());
            logger.info("RequestId=" + querySendDetailsResponse.getRequestId());
        }

    }

    
    
}
