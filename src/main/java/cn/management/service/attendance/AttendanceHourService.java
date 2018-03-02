package cn.management.service.attendance;

import java.util.List;

import cn.management.domain.attendance.AttendanceHour;
import cn.management.domain.attendance.dto.AttendanceHourQueryDto;
import cn.management.mapper.attendance.AttendanceHourMapper;
import cn.management.service.BaseService;

/**
 * 工时统计Service层接口
 * @author Admin
 */
public interface AttendanceHourService extends BaseService<AttendanceHourMapper, AttendanceHour> {

    /**
     * 分页查询
     * @param attendanceHourQueryDto
     * @param page
     * @param pageSize
     * @return
     */
    List<AttendanceHour> getItemsByPage(AttendanceHourQueryDto attendanceHourQueryDto, int page, int pageSize);

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);
}
