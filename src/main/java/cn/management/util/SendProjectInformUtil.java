package cn.management.util;

import cn.management.conf.ApplicationContextHelper;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectNotice;
import cn.management.domain.project.ProjectNoticeInform;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目通知发送工具类，用于发送项目通知
 * @author ZhouJiaKai
 * @date 2018-03-21
 */
public class SendProjectInformUtil {

    /**日期格式化*/
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 生成联系人手机号码数组
     * @param users
     * @return
     */
    public static ArrayList<String> buildPhoneNumbers(List<AdminUser> users) {
        ArrayList<String> phoneNumbers = new ArrayList<String>(5);
        for (AdminUser user : users) {
            //判断手机号是否为空
            String phone = user.getPhone();
            if (phone != null && !"".equals(phone)) {
                phoneNumbers.add(phone);
            }
        }
        return phoneNumbers;
    }
    
    /**
     * 生成联系人邮箱地址数组
     * @param users
     * @return
     */
    public static List<String> buildToAddrs(List<AdminUser> users) {
        List<String> toAddrs = new ArrayList<String>(5);
        for (AdminUser AdminUser : users) {
            //判断邮件是否为空
            String mail = AdminUser.getMail();
            if (mail != null && !"".equals(mail)) {
                toAddrs.add(mail);
            }
        }
        return toAddrs;
    }

    /**
     * 发送项目通知短信
     * @param projectNotice
     * @throws Exception
     */
    public static void sendProjectNoticeMessage(ProjectNotice projectNotice) throws Exception {
        //模版填充内容
        ArrayList<String> params = new ArrayList<String>(5);
        //从spring容器获取实例对象
        AdminUserService adminUserService =  ApplicationContextHelper.getBean(AdminUserService.class);
        List<ProjectNoticeInform> list = projectNotice.getInformList();
        List<AdminUser> users = new ArrayList<AdminUser>();
        for (ProjectNoticeInform projectNoticeInform : list) {
            AdminUser user = adminUserService.getItemById(projectNoticeInform.getUserId());
            if (null != user) {
                users.add(user);
            }
        }
        //短信接收人
        ArrayList<String> phoneNumbers = buildPhoneNumbers(users);
        //短信模版id
        int tmplId = 0;
        //通知标题
        params.add(projectNotice.getTitle());
        //通知内容
        params.add(projectNotice.getContent());
        if (null == projectNotice.getId()) {
            tmplId = 102701;
            //发布时间
            String createDateStr = dateFormat.format(projectNotice.getCreateTime());
            params.add(createDateStr);
        } else {
            //短信模版id
            tmplId = 102703;
            //修改时间
            String updateDateStr = dateFormat.format(projectNotice.getUpdateTime());
            params.add(updateDateStr);
        }
        //发布人
        String createName = projectNotice.getCreateName();
        params.add(createName);
        //发送短信通知
        SmsUtil.sendMultiMessageWithParam(tmplId, params, phoneNumbers);
    }

    /**
     * 发送项目通知邮件
     * @param projectNotice
     * @throws SysException
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendProjectNoticeMail(ProjectNotice projectNotice) throws SysException, MessagingException, IOException {
        //从spring容器获取实例对象
        AdminUserService adminUserService =  ApplicationContextHelper.getBean(AdminUserService.class);
        List<ProjectNoticeInform> list = projectNotice.getInformList();
        List<AdminUser> users = new ArrayList<AdminUser>();
        for (ProjectNoticeInform projectNoticeInform : list) {
            AdminUser user = adminUserService.getItemById(projectNoticeInform.getUserId());
            if (null != user) {
                users.add(user);
            }
        }
        //收件人地址
        List<String> toAddrs = buildToAddrs(users);
        //邮件主题
        String subject = "项目通知";
        //发布人
        String createName = projectNotice.getCreateName();
        //发布时间
        String createDateStr = dateFormat.format(projectNotice.getCreateTime());
        //修改时间
        String updateDateStr = dateFormat.format(projectNotice.getUpdateTime());
        //通知标题
        String title = projectNotice.getTitle();
        //通知内容
        String content = projectNotice.getContent();
        //邮件内容
        StringBuilder mailContent = new StringBuilder();
        if (null == projectNotice.getId()) {
            mailContent.append("您有新的项目通知.<br/>"
                    + "<b>标题：</b>" + title + "<br/>"
                    + "<b>内容：</b>" + content + "<br/>"
                    + "<b>发布人：</b>" + createName + "<br/>"
                    + "<b>发布时间：</b>" + createDateStr);
        } else {
            mailContent.append("您有变更的项目通知.<br/>"
                    + "<b>标题：</b>" + title + "<br/>"
                    + "<b>内容：</b>" + content + "<br/>"
                    + "<b>发布人：</b>" + createName + "<br/>"
                    + "<b>发布时间：</b>" + createDateStr + "<br/>"
                    + "<b>修改时间：</b>" + updateDateStr);
        }
        //发送邮件
        MailUtil.sendMails(toAddrs, subject, mailContent.toString());
    }

}
