package cn.management.util;

import java.util.ArrayList;

import cn.management.util.sms.SmsMultiSender;
import cn.management.util.sms.SmsMultiSenderResult;
import cn.management.util.sms.SmsSingleSender;
import cn.management.util.sms.SmsSingleSenderResult;

/**
 * 短信发送工具
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class SmsUtil {

	/**腾讯云短信应用id*/
    private static int appid;
    
    /**腾讯云短信应用密码*/
    private static String appkey;
    
    static {
        appid = 1400054270;
        appkey = "97af94e43ac19d061a917b2dadad4c98";
    }
    
    /**
     * 指定模板单发
     * 设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
     * @param tmplId 模版id
     * @param params 模版填充内容
     * @param phone 接收人手机号
     * @return
     * @throws Exception
     */
    public static SmsSingleSenderResult sendSingleMessageWithParam(int tmplId, ArrayList<String> params, 
        String phone) throws Exception {
        SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult singleSenderResult;
        singleSenderResult = singleSender.sendWithParam("86", phone, tmplId, params, "", "", "");
        return singleSenderResult;
    }
    
    /**
     * 指定模板群发
     * 设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
     * @param tmplId 模版id
     * @param params 模版填充内容
     * @param phoneNumbers 接收人手机号
     * @return
     * @throws Exception
     */
    public static SmsMultiSenderResult sendMultiMessageWithParam(int tmplId, ArrayList<String> params, 
        ArrayList<String> phoneNumbers) throws Exception {
        SmsMultiSender multiSender = new SmsMultiSender(appid, appkey);
        SmsMultiSenderResult multiSenderResult;
        multiSenderResult = multiSender.sendWithParam("86", phoneNumbers, tmplId, params, "", "", "");
        return multiSenderResult;
    }
    
}
