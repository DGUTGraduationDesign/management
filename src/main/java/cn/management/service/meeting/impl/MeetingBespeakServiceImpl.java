package cn.management.service.meeting.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.domain.meeting.MeetingRoom;
import cn.management.enums.BespeakStatusEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.InformWayEnum;
import cn.management.mapper.meeting.MeetingBespeakMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.meeting.MeetingBespeakService;
import cn.management.service.meeting.MeetingRoomService;
import tk.mybatis.mapper.entity.Example;

/**
 * 会议室预约service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
@Service
public class MeetingBespeakServiceImpl extends BaseServiceImpl<MeetingBespeakMapper, MeetingBespeak> implements MeetingBespeakService {

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private MeetingRoomService meetingRoomService;
	
	/**
	 * 条件查询员工列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<MeetingBespeak> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<MeetingBespeak> list = mapper.selectByExample(example);
        for (MeetingBespeak meetingBespeak : list) {
        	//设置预约状态、通知方式、预约人、对应的会议室
        	setName(meetingBespeak);
        }
        return list;
    }
	
	/**
	 * 查询会议室预约详情
     * @param condition
     * @return
	 */
	@Override
	public MeetingBespeak getItem(MeetingBespeak condition) {
		List<MeetingBespeak> items = getItemsByPage(condition, 1, 1);
		if (CollectionUtils.isEmpty(items)) {
            return null;
        }
		MeetingBespeak meetingBespeak = items.get(0);
    	//设置预约状态、通知方式、预约人、对应的会议室
    	setName(meetingBespeak);
    	//参会人员
    	List<AdminUser> users = new ArrayList<AdminUser>(5);
    	List<Integer> userIds = JSONObject.parseArray(meetingBespeak.getUserIds(), Integer.class);
    	for (Integer userId : userIds) {
    		AdminUser user = adminUserService.getItemById(userId);
    		if (null != user) {
    			users.add(user);
    		}
    	}
    	meetingBespeak.setUsers(users);
		return meetingBespeak;
	}
	
	/**
	 * 设置预约状态、通知方式、预约人、对应的会议室
	 * @param meetingBespeak
	 */
	private void setName(MeetingBespeak meetingBespeak) {
		//预约状态
		meetingBespeak.setBespeakStatusName(BespeakStatusEnum.getName(meetingBespeak.getBespeakStatus()));
		//通知方式
		meetingBespeak.setInformWayName(InformWayEnum.getName(meetingBespeak.getInformWay()));
		//预约人姓名
		AdminUser user = adminUserService.getItemById(meetingBespeak.getUserId());
		if (null != user) {
			meetingBespeak.setUserName(user.getRealName());
		}
		//对应会议室
		MeetingRoom meetingRoom = meetingRoomService.getItemById(meetingBespeak.getMeetingRoomId());
		if (null != meetingRoom) {
			meetingBespeak.setMeetingRoom(meetingRoom);
		}
	}

	@Override
	@Transactional
	public MeetingBespeak doBespeak(MeetingBespeak meetingBespeak) {
		return addSelectiveMapper(meetingBespeak);
	}

	@Override
	@Transactional
	public boolean doUpdate(MeetingBespeak meetingBespeak) {
		return update(meetingBespeak);
	}

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
		Example example = new Example(MeetingBespeak.class);
		example.createCriteria().andCondition("id IN(" + ids + ")");
		MeetingBespeak meetingBespeak = new MeetingBespeak();
		meetingBespeak.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(meetingBespeak, example);
	}

}
