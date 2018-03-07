package cn.management.util.test;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Test;

import cn.management.util.MailUtil;

/**
 * MailUtil测试类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class MailUtilTest {

	@Test
	public void sendMailTest() {
		String toAddr = "598959863@qq.com";
		String subject = "会议通知";
		String content = "时间：2018-03-06 08:30 - 09:30，地点：会议室2";
		try {
			MailUtil.sendMail(toAddr, subject, content);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
