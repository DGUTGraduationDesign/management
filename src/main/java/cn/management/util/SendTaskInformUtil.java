package cn.management.util;

import cn.management.conf.ApplicationContextHelper;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectTask;
import cn.management.enums.TaskStateEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendTaskInformUtil {

//    /**日期格式化*/
//    private static SimpleDateFormat beginTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    private static SimpleDateFormat endTimeFormat = new SimpleDateFormat("HH:mm");
//    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//    /**
//     * 生成联系人手机号码数组
//     * @param users
//     * @return
//     */
//    public static ArrayList<String> buildPhoneNumbers(List<AdminUser> users) {
//        ArrayList<String> phoneNumbers = new ArrayList<String>(5);
//        for (AdminUser user : users) {
//            //判断手机号是否为空
//            String phone = user.getPhone();
//            if (phone != null && !"".equals(phone)) {
//                phoneNumbers.add(phone);
//            }
//        }
//        return phoneNumbers;
//    }
//
//    /**
//     * 生成联系人邮箱地址数组
//     * @param users
//     * @return
//     */
//    public static List<String> buildToAddrs(List<AdminUser> users) {
//        List<String> toAddrs = new ArrayList<String>(5);
//        for (AdminUser AdminUser : users) {
//            //判断邮件是否为空
//            String mail = AdminUser.getMail();
//            if (mail != null && !"".equals(mail)) {
//                toAddrs.add(mail);
//            }
//        }
//        return toAddrs;
//    }
//
//    /**
//     * 发送任务邮件通知
//     * @param projectTask
//     * @throws SysException
//     * @throws MessagingException
//     * @throws IOException
//     */
//    public static void sendTaskInformMail(ProjectTask projectTask) throws SysException, MessagingException, IOException {
//        //从spring容器获取实例对象
//        AdminUserService adminUserService =  ApplicationContextHelper.getBean(AdminUserService.class);
//        //收件人地址
//        AdminUser user = adminUserService.getItemById(projectTask.getUserId());
//        if (null == user) {
//            throw new SysException("用户不存在.");
//        }
//        String toAddr = user.getMail();
//        //邮件主题
//        String subject = "任务通知";
//        //任务名称
//        String taskName = projectTask.getTaskName();
//        //任务内容
//        String taskContent = projectTask.getContent();
//        //任务创建日期
//        String createTime = dateFormat.format(projectTask.getCreateTime());
//        //任务截止日期
//        String closingDate = dateFormat.format(projectTask.getClosingDate());
//        //所属项目名称
//        String itemName = projectTask.getItemName();
//        //创建人姓名
//        String createName = projectTask.getCreateName();
//        //任务对象姓名
//        String userName = projectTask.getUserName();
//        //邮件内容
//        StringBuilder content = new StringBuilder();
//        if (null == projectTask.getId()) {
//            //发送新任务通知
//            content.append("您好，您有新的任务通知。<br/>");
//            content.append("<b>任务名称：</b>" + taskName + "<br/>");
//            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
//            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
//            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
//            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
//            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
//            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
//        } else if (null != projectTask.getId() && TaskStateEnum.UNCOMPLETE.getValue().equals(projectTask.getTaskState())) {
//            //发送任务更改通知
//            content.append("您好，您名为'" + taskName + "'的任务有所变更。变更后信息如下：<br/>");
//            content.append("<b>任务名称：</b>" + taskName + "<br/>");
//            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
//            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
//            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
//            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
//            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
//            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
//        } else if (TaskStateEnum.CANCEL.getValue().equals(projectTask.getTaskState())) {
//            //发送任务取消通知
//            content.append("您好，您名为'" + taskName + "'的任务有已取消。原任务信息：<br/>");
//            content.append("<b>任务名称：</b>" + taskName + "<br/>");
//            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
//            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
//            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
//            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
//            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
//            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
//        } else if (TaskStateEnum.COMPLETE.getValue().equals(projectTask.getTaskState())) {
//            //发送任务完成通知
//            //创建人
//            AdminUser create = adminUserService.getItemById(projectTask.getCreateBy());
//            if (null == create) {
//                throw new SysException("用户不存在.");
//            }
//            toAddr = create.getMail();
//            content.append("您好，名为'" + taskName + "'的任务已完成。任务信息：<br/>");
//            content.append("<b>任务名称：</b>" + taskName + "<br/>");
//            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
//            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
//            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
//            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
//            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
//            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
//        }
//        //发送邮件
//        MailUtil.sendMail(toAddr, subject, content.toString());
//    }

}
