package cn.management.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import cn.management.conf.ApplicationContextHelper;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.enums.BespeakStatusEnum;
import cn.management.service.meeting.MeetingBespeakService;

/**
 * 会议过期定时任务
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class OverdueBespeakJob implements Job {
	
	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(OverdueBespeakJob.class);
	
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	//从spring容器获取实例对象
    	MeetingBespeakService meetingBespeakService =  ApplicationContextHelper.getBean(MeetingBespeakService.class);
    	//将会议预约记录标记为已过期
        Integer bespeakId = Integer.valueOf(jobExecutionContext
            .getJobDetail().getJobDataMap().getString("bespeakId"));
        MeetingBespeak meetingBespeak = meetingBespeakService.getItemById(bespeakId);
        meetingBespeak.setBespeakStatus(BespeakStatusEnum.OVERDUE.getValue());
        meetingBespeakService.update(meetingBespeak);
    }

}
