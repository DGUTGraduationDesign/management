package cn.management.service.attendance.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.management.domain.attendance.AttendanceHour;
import cn.management.domain.attendance.dto.AttendanceHourQueryDto;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.attendance.AttendanceHourMapper;
import cn.management.service.attendance.AttendanceHourService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

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

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
		Example example = new Example(AttendanceHour.class);
		example.createCriteria().andCondition("id IN(" + ids + ")");
		AttendanceHour attendanceHour = new AttendanceHour();
		attendanceHour.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(attendanceHour, example);
	}

}
