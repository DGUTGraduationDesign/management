package cn.management.mapper.meeting;

import cn.management.domain.meeting.MeetingBespeak;
import cn.management.util.MyMapper;

import java.util.List;

/**
 * 会议室预约Mapper
 */
public interface MeetingBespeakMapper extends MyMapper<MeetingBespeak> {

    /**
     * 条件查询预约记录
     * @param meetingBespeak
     * @return
     */
	List<MeetingBespeak> getBespeakByCondition(MeetingBespeak meetingBespeak);

	/**
	 * 查询会议室在对应时间段内是否被预约
	 * @param meetingBespeak
	 * @return
	 */
	int countBespeakByIdAndTime(MeetingBespeak meetingBespeak);

}
