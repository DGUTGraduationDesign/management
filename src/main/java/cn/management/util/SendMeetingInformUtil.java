package cn.management.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import cn.management.conf.ApplicationContextHelper;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.domain.meeting.MeetingRoom;
import cn.management.domain.project.ProjectTask;
import cn.management.enums.BespeakStatusEnum;
import cn.management.enums.TaskStateEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会议通知发送工具类，用于发送会议通知
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class SendMeetingInformUtil {

    /**日期格式化*/
    private static SimpleDateFormat beginTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat endTimeFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
     * 发送会议短信通知/会议取消通知
     * @param meetingBespeak
     * @throws Exception
     */
    public static void sendMeetingInformMessage(MeetingBespeak meetingBespeak) throws Exception {
        //短信接收人
        ArrayList<String> phoneNumbers = buildPhoneNumbers(meetingBespeak.getUsers());
        //模版填充内容
        ArrayList<String> params = new ArrayList<String>(5);
        //会议主题
        params.add(meetingBespeak.getMeetingTheme());
        int tmplId = 0;
        if (BespeakStatusEnum.BESPEAK.getValue().equals(meetingBespeak.getBespeakStatus())) {
            //预约会议
        	//短信模版id
            tmplId = 71784;
            //会议地点
            MeetingRoom room = meetingBespeak.getMeetingRoom();
            params.add(room.getRoomName() + "(" + room.getRoomPlace() + ")");
            //会议时间
            String beginTime = beginTimeFormat.format(meetingBespeak.getBeginTime());
            params.add(beginTime);
            String endTime = endTimeFormat.format(meetingBespeak.getEndTime());
            params.add(endTime);
        } else if (BespeakStatusEnum.CANCEL.getValue().equals(meetingBespeak.getBespeakStatus())) {
        	//会议取消
        	//短信模版id
            tmplId = 71785;
            //会议时间
            String meetingDate = beginTimeFormat.format(meetingBespeak.getBeginTime());
            params.add(meetingDate);
        }
        
        //发送短信通知
        SmsUtil.sendMultiMessageWithParam(tmplId, params, phoneNumbers);
    }

    /**
     * 发送会议变更短信通知
     * @param meetingBespeak
     * @param formerDate
     * @throws Exception
     */
    public static void sendUpdateMeetingMessage(MeetingBespeak meetingBespeak, Date formerDate) 
        throws Exception {
        //短信模版id
        int tmplId = 72034;
        //短信接收人
        ArrayList<String> phoneNumbers = buildPhoneNumbers(meetingBespeak.getUsers());
        //模版填充内容
        ArrayList<String> params = new ArrayList<String>(5);
        //原会议时间
        String formerDateStr = beginTimeFormat.format(formerDate);
        params.add(formerDateStr);
        //会议主题
        params.add(meetingBespeak.getMeetingTheme());
        //会议地点
        MeetingRoom room = meetingBespeak.getMeetingRoom();
        params.add(room.getRoomName() + "(" + room.getRoomPlace() + ")");
        //会议时间
        String beginTime = beginTimeFormat.format(meetingBespeak.getBeginTime());
        String endTime = endTimeFormat.format(meetingBespeak.getEndTime());
        params.add(beginTime + "-" + endTime);
        //发送短信通知
        SmsUtil.sendMultiMessageWithParam(tmplId, params, phoneNumbers);
    }
    
    /**
     * 发送会前提醒短信通知
     * @param meetingBespeak
     * @throws Exception
     */
    public static void sendMeetingRemindMessage(MeetingBespeak meetingBespeak) throws Exception {
        //短信模版id
        int tmplId = 73016;
        //短信接收人
        ArrayList<String> phoneNumbers = buildPhoneNumbers(meetingBespeak.getUsers());
        //模版填充内容
        ArrayList<String> params = new ArrayList<String>(5);
        //会议主题
        String meetingTheme = meetingBespeak.getMeetingTheme();
        params.add(meetingTheme);
        //距离会议开始时间
        Date meetingTime = meetingBespeak.getBeginTime();
        Date date = new Date();
        int minutes = (int)((meetingTime.getTime() - date.getTime()) / (60 * 1000) + 1);
        params.add(String.valueOf(minutes));
        //会议地点
        MeetingRoom room = meetingBespeak.getMeetingRoom();
        params.add(room.getRoomName() + "(" + room.getRoomPlace() + ")");
        //会议时间
        String beginTime = beginTimeFormat.format(meetingBespeak.getBeginTime());
        params.add(beginTime);
        //发送短信通知
        SmsUtil.sendMultiMessageWithParam(tmplId, params, phoneNumbers);
    }
    
    /**
     * 发送会议邮件通知/会议取消通知
     * @param meetingBespeak
     * @throws SysException
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendMeetingInformMail(MeetingBespeak meetingBespeak) 
        throws SysException, MessagingException, IOException {
        //收件人地址
        List<String> toAddrs = buildToAddrs(meetingBespeak.getUsers());
        //邮件主题
        String subject = null;
        //邮件内容
        StringBuilder content = new StringBuilder();
        if (BespeakStatusEnum.BESPEAK.getValue().equals(meetingBespeak.getBespeakStatus())) {
	        //邮件主题
	        subject = "会议通知";
	        //邮件内容
	        content.append("您好，您有会议通知，具体内容如下：<br/>");
	        //会议主题
	        String meetingTheme = meetingBespeak.getMeetingTheme();
	        content.append("<b>会议主题：</b>" + meetingTheme + "<br/>");
	        //会议地点
	        MeetingRoom room = meetingBespeak.getMeetingRoom();
	        String roomPlace = room.getRoomName() + "(" + room.getRoomPlace() + ")";
	        content.append("<b>会议地点：</b>" + roomPlace + "<br/>");
	        //会议时间
	        String beginTime = beginTimeFormat.format(meetingBespeak.getBeginTime());
	        String endTime = endTimeFormat.format(meetingBespeak.getEndTime());
	        content.append("<b>会议时间：</b>" + beginTime + "-" + endTime + "<br/>");
	        //参会人员
	        content.append("<b>参会人员：</b><br/>");
	        for (AdminUser user : meetingBespeak.getUsers()) {
	            content.append(user.getRealName() + "<br/>");
	        }
        } else if(BespeakStatusEnum.CANCEL.getValue().equals(meetingBespeak.getBespeakStatus())) {
        	//邮件主题
            subject = "会议取消通知";
            //会议主题
            String meetingTheme = meetingBespeak.getMeetingTheme();
            //会议时间
            String meetingDate = beginTimeFormat.format(meetingBespeak.getBeginTime());
            //邮件内容
            content.append("您好，原定在" + meetingDate + "举办的主题为:" + meetingTheme + "的会议现已被取消。请周知。");
        }
        //发送邮件
        MailUtil.sendMails(toAddrs, subject, content.toString());
    }

    /**
     * 发送会议变更邮件通知
     * @param meetingBespeak
     * @param formerDate
     * @throws SysException
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendUpdateMeetingMail(MeetingBespeak meetingBespeak, Date formerDate) 
        throws SysException, MessagingException, IOException {
        //收件人地址
        List<String> toAddrs = buildToAddrs(meetingBespeak.getUsers());
        //邮件主题
        String subject = "会议变更通知";
        //会议主题
        String meetingTheme = meetingBespeak.getMeetingTheme();
        //原会议时间
        String formerDateStr = beginTimeFormat.format(formerDate);
        //会议地点
        MeetingRoom room = meetingBespeak.getMeetingRoom();
        String roomPlace = room.getRoomName() + "(" + room.getRoomPlace() + ")";
        //会议时间
        String beginTime = beginTimeFormat.format(meetingBespeak.getBeginTime());
        String endTime = endTimeFormat.format(meetingBespeak.getEndTime());
        //邮件内容
        StringBuilder content = new StringBuilder();
        content.append("您好，原定在" + formerDateStr + "主题为" + meetingTheme 
            + "的会议有变动，变动后的地点：" + roomPlace + "，时间：" + beginTime 
            + "-" + endTime + "，请周知。");
        //发送邮件
        MailUtil.sendMails(toAddrs, subject, content.toString());
    }
    
    /**
     * 发送会前提醒邮件通知
     * @param meetingBespeak
     * @throws SysException
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendMeetingRemindMail(MeetingBespeak meetingBespeak) 
        throws SysException, MessagingException, IOException {
        //收件人地址
        List<String> toAddrs = buildToAddrs(meetingBespeak.getUsers());
        //邮件主题
        String subject = "会议通知";
        //邮件内容
        StringBuilder content = new StringBuilder();
        //会议主题
        String meetingTheme = meetingBespeak.getMeetingTheme();
        //距离会议开始时间
        Date meetingTime = meetingBespeak.getBeginTime();
        Date date = new Date();
        int minutes = (int)((meetingTime.getTime() - date.getTime()) / (60 * 1000) + 1);
        content.append("您好，您关于主题为'" + meetingTheme + "'的会议将在" + minutes
            + "分钟后开始，请提前安排好工作时间，准时出席。<br/>会议具体内容如下：<br/>");
        content.append("<b>会议主题：</b>" + meetingTheme + "<br/>");
        //会议地点
        MeetingRoom room = meetingBespeak.getMeetingRoom();
        String roomPlace = room.getRoomName() + "(" + room.getRoomPlace() + ")";
        content.append("<b>会议地点：</b>" + roomPlace + "<br/>");
        //会议时间
        String beginTime = beginTimeFormat.format(meetingTime);
        String endTime = endTimeFormat.format(meetingBespeak.getEndTime());
        content.append("<b>会议时间：</b>" + beginTime + "-" + endTime + "<br/>");
        //参会人员
        content.append("<b>参会人员：</b><br/>");
        for (AdminUser user : meetingBespeak.getUsers()) {
            content.append(user.getRealName() + "<br/>");
        }
        //发送邮件
        MailUtil.sendMails(toAddrs, subject, content.toString());
    }

    /**
     * 发送任务邮件通知
     * @param projectTask
     * @throws SysException
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendTaskInformMail(ProjectTask projectTask) throws SysException, MessagingException, IOException {
        //从spring容器获取实例对象
        AdminUserService adminUserService =  ApplicationContextHelper.getBean(AdminUserService.class);
        //收件人地址
        AdminUser user = adminUserService.getItemById(projectTask.getUserId());
        if (null == user) {
            throw new SysException("用户不存在.");
        }
        String toAddr = user.getMail();
        //邮件主题
        String subject = "任务通知";
        //任务名称
        String taskName = projectTask.getTaskName();
        //任务内容
        String taskContent = projectTask.getContent();
        //任务创建日期
        String createTime = dateFormat.format(projectTask.getCreateTime());
        //任务截止日期
        String closingDate = dateFormat.format(projectTask.getClosingDate());
        //所属项目名称
        String itemName = projectTask.getItemName();
        //创建人姓名
        String createName = projectTask.getCreateName();
        //任务对象姓名
        String userName = projectTask.getUserName();
        //邮件内容
        StringBuilder content = new StringBuilder();
        if (null == projectTask.getId()) {
            //发送新任务通知
            content.append("您好，您有新的任务通知。<br/>");
            content.append("<b>任务名称：</b>" + taskName + "<br/>");
            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
        } else if (null != projectTask.getId() && TaskStateEnum.UNCOMPLETE.getValue().equals(projectTask.getTaskState())) {
            //发送任务更改通知
            content.append("您好，您名为'" + taskName + "'的任务有所变更。变更后信息如下：<br/>");
            content.append("<b>任务名称：</b>" + taskName + "<br/>");
            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
        } else if (TaskStateEnum.CANCEL.getValue().equals(projectTask.getTaskState())) {
            //发送任务取消通知
            content.append("您好，您名为'" + taskName + "'的任务有已取消。原任务信息：<br/>");
            content.append("<b>任务名称：</b>" + taskName + "<br/>");
            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
        } else if (TaskStateEnum.COMPLETE.getValue().equals(projectTask.getTaskState())) {
            //发送任务完成通知
            //创建人
            AdminUser create = adminUserService.getItemById(projectTask.getCreateBy());
            if (null == create) {
                throw new SysException("用户不存在.");
            }
            toAddr = create.getMail();
            content.append("您好，名为'" + taskName + "'的任务已完成。任务信息：<br/>");
            content.append("<b>任务名称：</b>" + taskName + "<br/>");
            content.append("<b>任务内容：</b>" + taskContent + "<br/>");
            content.append("<b>创建人姓名：</b>" + createName + "<br/>");
            content.append("<b>任务对象姓名：</b>" + userName + "<br/>");
            content.append("<b>所属项目名称：</b>" + itemName + "<br/>");
            content.append("<b>任务创建日期：</b>" + createTime + "<br/>");
            content.append("<b>任务截止日期：</b>" + closingDate + "<br/>");
        }
        //发送邮件
        MailUtil.sendMail(toAddr, subject, content.toString());
    }

}
