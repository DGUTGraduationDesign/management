package cn.management.service.attendance;

import java.util.List;

import org.activiti.engine.task.Comment;

import cn.management.domain.attendance.AttendanceApplication;
import cn.management.exception.SysException;
import cn.management.mapper.attendance.AttendanceApplicationMapper;
import cn.management.service.BaseService;

/**
 * 考勤申请Service层接口
 * @author ZhouJiaKai
 * @date 2018-03-02
 */
public interface AttendanceApplicationService extends BaseService<AttendanceApplicationMapper, AttendanceApplication> {

	/**
	 * 使用考勤申请id，查询历史批注信息
	 * @param id
	 * @return
	 */
	List<Comment> findCommentByLeaveBillId(Integer id);

	/**
	 * 添加考勤申请
	 * @param attendanceApplication
	 * @return
	 * @throws SysException 
	 */
	boolean doAdd(AttendanceApplication attendanceApplication, Integer loginUserId) throws SysException;

	/**
	 * 考勤申请审核
	 * @param attendanceApplication
	 * @param identity
	 * @param loginUserId 
	 * @return
	 * @throws SysException 
	 */
	boolean doAudit(AttendanceApplication attendanceApplication, String identity, Integer loginUserId) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
