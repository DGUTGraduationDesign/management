package cn.management.service.attendance;

import cn.management.domain.attendance.AttendanceApplication;
import cn.management.mapper.attendance.AttendanceApplicationMapper;
import cn.management.service.BaseService;

/**
 * 考勤申请Service层接口
 * @author Admin
 */
public interface AttendanceApplicationService extends BaseService<AttendanceApplicationMapper, AttendanceApplication> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
