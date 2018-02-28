package cn.management.service.attendance.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.management.domain.attendance.AttendanceHour;
import cn.management.domain.attendance.dto.AttendanceHourQueryDto;
import cn.management.mapper.attendance.AttendanceHourMapper;
import cn.management.service.attendance.AttendanceHourService;
import cn.management.service.impl.BaseServiceImpl;

@Service
public class AttendanceHourServiceImpl extends BaseServiceImpl<AttendanceHourMapper, AttendanceHour>
		implements AttendanceHourService {

    /**
     * 分页查询
     * @param attendanceHourQueryDto
     * @param page
     * @param pageSize
     * @return
     */
	@Override
	public List<AttendanceHour> getItemsByPage(AttendanceHourQueryDto attendanceHourQueryDto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mapper.getItemsByCondition(attendanceHourQueryDto);
	}

}
