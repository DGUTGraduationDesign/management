package cn.management.service.meeting;

import org.quartz.SchedulerException;

import cn.management.domain.meeting.MeetingBespeak;
import cn.management.exception.SysException;
import cn.management.mapper.meeting.MeetingBespeakMapper;
import cn.management.service.BaseService;

/**
 * 会议室预约service层接口
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public interface MeetingBespeakService extends BaseService<MeetingBespeakMapper, MeetingBespeak> {

	/**
	 * 查询会议室在对应时间段内是否被预约
	 * @param meetingBespeak
	 * @return
	 */
	boolean existsBespeakByIdAndTime(MeetingBespeak meetingBespeak);
	
	/**
	 * 预约会议室
	 * @param meetingBespeak
	 * @return
	 * @throws SysException 
	 */
	MeetingBespeak doBespeak(MeetingBespeak meetingBespeak) throws SysException;

	/**
	 * 修改会议室预约
	 * @param meetingBespeak
	 * @param loginUserId
	 * @return
	 * @throws SysException 
	 */
	boolean doUpdate(MeetingBespeak meetingBespeak, Integer loginUserId) throws SysException;

	/**
	 * 取消预约
	 * @param bespeakId
	 * @param loginUserId
	 * @return
	 * @throws SysException 
	 */
	boolean doCancel(Integer bespeakId, Integer loginUserId) throws SysException;

	/**
	 * 修改通知方式
	 * @param bespeakId
	 * @param loginUserId
	 * @param informWay
	 * @return
	 * @throws SysException
	 */
	boolean doChangeInformWay(Integer bespeakId, Integer loginUserId, Integer informWay) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     * @throws SysException 
     */
	void logicalDelete(String ids) throws SysException;

}
