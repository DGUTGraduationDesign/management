package cn.management.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务管理类
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class QuartzManager {
    
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    
    /**
     * 添加定时任务
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass 要执行的任务类
     * @param date 触发时间
     * @param params 定时任务调用方法参数
     * @throws SchedulerException 
     */
    public static void addJob(String jobName, String triggerName, Class jobClass, 
        Date date, Map<String, String> params) throws SchedulerException {
        //获取任务调度容器
        Scheduler sched = schedulerFactory.getScheduler();
        //根据任务名获取任务对象
        JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName));
        if (jobDetail == null) {
            //新建任务，设置任务组名、任务名、对应的任务类
            jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName).storeDurably(true).build();
            //定时任务调用方法参数设置
            //params.forEach((key, value) -> jobDetail.getJobDataMap().put(key, value));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
            }
            //将任务对象添加到调度容器
            sched.addJob(jobDetail, false);
        }
        //设置触发器名
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(triggerName);
        //设置触发器对应的任务对象
        triggerBuilder.forJob(jobDetail);
        triggerBuilder.startNow();
        //拼接时间表达式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String cronExpression = "0 " + calendar.get(Calendar.MINUTE) + " " 
            + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.DAY_OF_MONTH) 
            + " " + (calendar.get(Calendar.MONTH) + 1) + " ? " 
            + calendar.get(Calendar.YEAR);
        //设置触发时间
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
        //创建触发器
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();
        //将触发器添加到调度容器
        sched.scheduleJob(trigger);
        //启动
        sched.start();
    }
    
    /**
     * 根据触发器名称获取触发器
     * @param triggerName
     * @return
     * @throws SchedulerException
     */
    public static CronTrigger getTriggerByName(String triggerName) throws SchedulerException {
        //获取任务调度容器
        Scheduler sched = schedulerFactory.getScheduler();
        //根据触发器名查找触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
        CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
        return trigger;
    }
    
    /**
     * 修改定时任务触发时间
     * @param triggerName 触发器名
     * @param date 时间表达式
     * @throws SchedulerException 
     */
    public static void modifyJobTime(String triggerName, Date date) 
        throws SchedulerException {
        //获取任务调度容器
        Scheduler sched = schedulerFactory.getScheduler();
        //根据触发器名查找触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
        CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }
        //判断触发时间有无更改
        String oldTime = trigger.getCronExpression();
        //拼接时间表达式
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String cronExpression = "0 " + calendar.get(Calendar.MINUTE) + " " 
            + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.DAY_OF_MONTH) 
            + " " + (calendar.get(Calendar.MONTH) + 1) + " ? " 
            + calendar.get(Calendar.YEAR);
        //时间有变更才修改触发时间
        if (!oldTime.equals(cronExpression)) {
            //设置触发器名
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName);
            triggerBuilder.startNow();
            //设置触发时间
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
            //创建触发器
            trigger = (CronTrigger) triggerBuilder.build();
            //修改触发容器中对应的触发器
            sched.rescheduleJob(triggerKey, trigger);
        }
    }
    
    /**
     * 删除任务
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @throws SchedulerException 
     */
    public static void removeJob(String jobName, String[] triggerNames) throws SchedulerException {
        //获取任务调度容器
        Scheduler sched = schedulerFactory.getScheduler();
        for(String triggerName : triggerNames) {
            //根据触发器名查找触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
            if (triggerName != null) {
                //停止触发器
                sched.pauseTrigger(triggerKey);
                //移除触发器
                sched.unscheduleJob(triggerKey);
            }
        }
        //根据任务名查找任务
        JobKey jobKey = JobKey.jobKey(jobName);
        if (jobKey != null) {
            //删除任务
            sched.deleteJob(jobKey);
        }
    }
    
}
