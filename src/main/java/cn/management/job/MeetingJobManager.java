package cn.management.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.SchedulerException;

import cn.management.domain.meeting.MeetingBespeak;
import cn.management.enums.MeetingBespeakJobEnum;

/**
 * 会议定时任务管理工具类
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class MeetingJobManager {

    /**
     * 添加会前发送提醒通知定时任务
     * @param meetingBespeak
     * @throws SchedulerException
     */
    public static void addSendMeetingInformJob(MeetingBespeak meetingBespeak) throws SchedulerException {
    	Integer bespeakId = meetingBespeak.getId();
        //设置定时任务调用方法参数
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("bespeakId", bespeakId.toString());
        //任务名
        String jobName = MeetingBespeakJobEnum.SEND_JOB_NAME + bespeakId;
        //设置触发时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(meetingBespeak.getBeginTime());
        //判断距离开会时间是否小于一小时
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        if (calendar.getTime().getTime() > new Date().getTime()) {
            //触发器名
            String triggerName = MeetingBespeakJobEnum.ONE_HOUR_TIGGER_NAME + bespeakId;
            //添加定时任务
            QuartzManager.addJob(jobName, triggerName, SendMeetingInformJob.class, 
                calendar.getTime(), params);
        }
        //判断距离开会时间是否大于半小时
        calendar.add(Calendar.MINUTE, 30);
        if (calendar.getTime().getTime() > new Date().getTime()) {
            //触发器名
            String triggerName = MeetingBespeakJobEnum.THIRTY_MINUTE_TIGGER_NAME + bespeakId;
            //添加定时任务
            QuartzManager.addJob(jobName, triggerName, SendMeetingInformJob.class, 
                calendar.getTime(), params);
        }
    }

    /**
     * 添加会议过期自动修改预约状态定时任务
     * @param meetingBespeak
     * @throws SchedulerException
     */
    public static void addOverdueBespeakJob(MeetingBespeak meetingBespeak) throws SchedulerException {
        Integer bespeakId = meetingBespeak.getId();
        //设置定时任务调用方法参数
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("bespeakId", bespeakId.toString());
        //任务名
        String jobName = MeetingBespeakJobEnum.OVERDUE_JOB_NAME + bespeakId;
        //触发器名
        String tiggerName = MeetingBespeakJobEnum.OVERDUE_JOB_TIGGER_NAME + bespeakId;
        QuartzManager.addJob(jobName, tiggerName, OverdueBespeakJob.class, 
            meetingBespeak.getEndTime(), params);
    }
    
    /**
     * 修改会前发送提醒通知定时任务触发时间
     * @param meetingBespeak
     * @throws SchedulerException
     */
    public static void editSendMeetingInformJob(MeetingBespeak meetingBespeak) throws SchedulerException {
    	Integer bespeakId = meetingBespeak.getId();
        //设置定时任务调用方法参数
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("bespeakId", bespeakId.toString());
        //任务名
        String jobName = MeetingBespeakJobEnum.SEND_JOB_NAME + bespeakId;
        //会议时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(meetingBespeak.getBeginTime());
        //判断距离会议开始时间是否大于一小时
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        if (calendar.getTime().getTime() > new Date().getTime()) {
            //触发器名
            String triggerName = MeetingBespeakJobEnum.ONE_HOUR_TIGGER_NAME + bespeakId;
            //判断定时任务是否存在
            CronTrigger trigger = QuartzManager.getTriggerByName(triggerName);
            if (trigger != null) {
                //修改定时任务触发时间
                QuartzManager.modifyJobTime(triggerName, calendar.getTime());
            } else {
                //添加定时任务
                QuartzManager.addJob(jobName, triggerName, SendMeetingInformJob.class, 
                    calendar.getTime(), params);
            }
        }
        //判断距离会议开始时间是否大于半小时
        calendar.add(Calendar.MINUTE, 30);
        if (calendar.getTime().getTime() > new Date().getTime()) {
            //触发器名
            String triggerName = MeetingBespeakJobEnum.THIRTY_MINUTE_TIGGER_NAME + bespeakId;
            //修改定时任务触发时间
            QuartzManager.modifyJobTime(triggerName, calendar.getTime());
            //判断定时任务是否存在
            CronTrigger trigger = QuartzManager.getTriggerByName(triggerName);
            if (trigger != null) {
                //修改定时任务触发时间
                QuartzManager.modifyJobTime(triggerName, calendar.getTime());
            } else {
                //添加定时任务
                QuartzManager.addJob(jobName, triggerName, SendMeetingInformJob.class, 
                    calendar.getTime(), params);
            }
        }
    }

    /**
     * 移除与会议记录有关的会前通知定时任务
     * @param bespeakId
     * @throws SchedulerException 
     */
    public static void removeSendMeetingInformJob(Integer bespeakId) throws SchedulerException {
        //会前通知定时任务任务名
        String sendJobName = MeetingBespeakJobEnum.SEND_JOB_NAME + bespeakId;
        //会前通知定时任务触发器名
        String oneHourTiggerName = MeetingBespeakJobEnum.ONE_HOUR_TIGGER_NAME + bespeakId;
        String thirtyMinuteTiggerName = MeetingBespeakJobEnum.THIRTY_MINUTE_TIGGER_NAME + bespeakId;
        String[] sendJobTiggerNames = new String[]{oneHourTiggerName, thirtyMinuteTiggerName};
        //移除会前通知定时任务
        QuartzManager.removeJob(sendJobName, sendJobTiggerNames);
    }

    /**
     * 移除会议预约过期自动修改预约状态任务名
     * @param bespeakId
     * @throws SchedulerException 
     */
    public static void removeOverdueBespeakJob(Integer bespeakId) throws SchedulerException {
    	//自动修改预约状态任务名
        String overdueJobName = MeetingBespeakJobEnum.OVERDUE_JOB_NAME + bespeakId;
        //自动修改预约状态任务触发器名
        String overdueJobTriggerName = MeetingBespeakJobEnum.OVERDUE_JOB_TIGGER_NAME + bespeakId;
        String[] overdueJobTiggerNames = new String[]{overdueJobTriggerName};
        //移除自动修改预约状态任务名
        QuartzManager.removeJob(overdueJobName, overdueJobTiggerNames);
    }
    
}
