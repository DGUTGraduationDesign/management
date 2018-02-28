package cn.management.mapper.attendance;

import java.util.List;

import cn.management.domain.attendance.AttendanceHour;
import cn.management.domain.attendance.dto.AttendanceHourQueryDto;
import cn.management.util.MyMapper;

/**
 * 工时统计Mapper
 */
public interface AttendanceHourMapper extends MyMapper<AttendanceHour> {
	
	/**
	 * 条件查询工时信息
	 * @param attendanceHourQueryDto
	 * @return
	 */
	List<AttendanceHour> getItemsByCondition(AttendanceHourQueryDto attendanceHourQueryDto);

}
