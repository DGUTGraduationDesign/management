package cn.management.service.attendance.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.attendance.AttendanceApplication;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.attendance.AttendanceApplicationMapper;
import cn.management.service.attendance.AttendanceApplicationService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

@Service
public class AttendanceApplicationServiceImpl extends BaseServiceImpl<AttendanceApplicationMapper, AttendanceApplication>
		implements AttendanceApplicationService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
        Example example = new Example(AttendanceApplication.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        AttendanceApplication attendanceApplication = new AttendanceApplication();
        attendanceApplication.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(attendanceApplication, example);
	}
}
