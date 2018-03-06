package cn.management.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送工具
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class MailUtil {
    
    private static Properties props = new Properties();
    private static Session session;
    private static MimeMessage message; 
    
    static {
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(props);
        session.setDebug(true);
        message = new MimeMessage(session);
    }

    /**
     * 邮件单发
     * @param toAddrs
     * @param subject
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendMail(String toAddr, String subject, String content) throws MessagingException, IOException {
        //From: 发件人
        message.setFrom(new InternetAddress("13631785227@163.com"));
        //To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toAddr, "USER_CC", "UTF-8"));
        //Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        //Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");
        //设置显示的发件时间
        message.setSentDate(new Date());
        //保存前面的设置
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.163.com", "13631785227@163.com", "zjk955957");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 邮件群发
     * @param toAddrs
     * @param subject
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendMails(List<String> toAddrs, String subject, String content) throws MessagingException, IOException {
        //From: 发件人
        message.setFrom(new InternetAddress("13631785227@163.com"));
        //To: 收件人
        List<InternetAddress> toAddrList = new ArrayList<InternetAddress>(5);
        for(String toAddr : toAddrs){
            toAddrList.add(new InternetAddress(toAddr, "USER_CC", "UTF-8"));
        }
        message.setRecipients(MimeMessage.RecipientType.TO, toAddrList.toArray(new InternetAddress[toAddrList.size()]));
        //Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        //Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");
        //设置显示的发件时间
        message.setSentDate(new Date());
        //保存前面的设置
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.163.com", "13631785227@163.com", "zjk955957");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}
