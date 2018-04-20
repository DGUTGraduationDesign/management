package cn.management.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import cn.management.conf.ApplicationContextHelper;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.enums.InformWayEnum;
import cn.management.service.meeting.MeetingBespeakService;
import cn.management.util.SendMeetingInformUtil;

/**
 * 会前发送提醒通知定时任务类
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class SendMeetingInformJob implements Job {

	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(SendMeetingInformJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	//从spring容器获取实例对象
    	MeetingBespeakService meetingBespeakService =  ApplicationContextHelper.getBean(MeetingBespeakService.class);

    	//查询会议预约记录
        Integer bespeakId = Integer.valueOf(jobExecutionContext
            .getJobDetail().getJobDataMap().getString("bespeakId"));
        MeetingBespeak meetingBespeak = meetingBespeakService.getItemById(bespeakId);
        
        //判断是否发送邮件通知和短信通知
        Integer informWay = meetingBespeak.getInformWay();
        if (!InformWayEnum.NONE.getValue().equals(informWay)) {
            try {
                if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                    //发送会前提醒邮件通知
                    SendMeetingInformUtil.sendMeetingRemindMail(meetingBespeak);
                }
                if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                    //发送短信通知
                    SendMeetingInformUtil.sendMeetingRemindMessage(meetingBespeak);
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
            }
        }
    }
    
}
