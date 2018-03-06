package cn.management.service.meeting;

import cn.management.domain.meeting.MeetingRoom;
import cn.management.mapper.meeting.MeetingRoomMapper;
import cn.management.service.BaseService;

/**
 * 会议室service层接口
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public interface MeetingRoomService extends BaseService<MeetingRoomMapper, MeetingRoom> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
