package cn.management.service.meeting.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.meeting.MeetingRoom;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.meeting.MeetingRoomMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.meeting.MeetingRoomService;
import tk.mybatis.mapper.entity.Example;

/**
 * 会议室service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
@Service
public class MeetingRoomServiceImpl extends BaseServiceImpl<MeetingRoomMapper, MeetingRoom> implements MeetingRoomService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
		Example example = new Example(MeetingRoom.class);
		example.createCriteria().andCondition("id IN(" + ids + ")");
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(meetingRoom, example);
	}

}
