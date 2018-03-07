package cn.management.util.test;

import java.util.ArrayList;

import org.junit.Test;

import cn.management.util.SmsUtil;
import cn.management.util.sms.SmsMultiSenderResult;

/**
 * SmsUtil测试类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class SmsUtilTest {

	/**
	 * 使用模版群发
	 */
    @Test
    public void sendMultiMessageWithParamTest() {
        SmsMultiSenderResult smsMultiSenderResult;
        //短信模版id
        int tmplId = 73016;
        //短信接收人
        ArrayList<String> phoneNumbers = new ArrayList<String>(5);
        phoneNumbers.add("13631785227");
        //模版填充内容
        ArrayList<String> params = new ArrayList<String>(5);
        //会议主题
        params.add("早会");
        //距离会议开始时间
        params.add("30");
        //会议地点
        params.add("会议室1");
        //会议时间
        params.add("2018-01-02 16:30");
        //发送短信通知
        try {
        	smsMultiSenderResult = SmsUtil.sendMultiMessageWithParam(tmplId, params, phoneNumbers);
            System.out.println(smsMultiSenderResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
