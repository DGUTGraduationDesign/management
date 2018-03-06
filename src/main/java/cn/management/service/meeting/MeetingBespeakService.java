package cn.management.service.meeting;

import cn.management.domain.meeting.MeetingBespeak;
import cn.management.mapper.meeting.MeetingBespeakMapper;
import cn.management.service.BaseService;

/**
 * 会议室预约service层接口
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public interface MeetingBespeakService extends BaseService<MeetingBespeakMapper, MeetingBespeak> {

	/**
	 * 预约会议室
	 * @param meetingBespeak
	 * @return
	 */
	MeetingBespeak doBespeak(MeetingBespeak meetingBespeak);

	/**
	 * 修改会议室预约
	 * @param meetingBespeak
	 * @return
	 */
	boolean doUpdate(MeetingBespeak meetingBespeak);

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
