package cn.management.enums;

/**
 * 会议预约操作定时任务时相关枚举类，用于添加定、修时任务指定任务名、触发器名
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class MeetingBespeakJobEnum {
    
	/**发送会议通知任务名*/
    public static final String SEND_JOB_NAME = "send_inform_job";

	/**提起一小时发送会议通知触发器名*/
    public static final String ONE_HOUR_TIGGER_NAME = "send_before_one_hour";

    /**提前三十分钟发送会议通知触发器名*/
    public static final String THIRTY_MINUTE_TIGGER_NAME = "send_before_thirty_minute";

	/**会议预约过期自动修改预约状态任务名*/
    public static final String OVERDUE_JOB_NAME = "overdue_bespeak_job";

	/**会议预约过期自动修改预约状态触发器名*/
    public static final String OVERDUE_JOB_TIGGER_NAME = "overdue_bespeak_tigger";
    
}
