package cn.management.service.attendance.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminPosition;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.enums.ApplicationStateEnum;
import cn.management.enums.ApplicationTypeEnum;
import cn.management.enums.AttendanceIdentityEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.attendance.AttendanceApplicationMapper;
import cn.management.service.admin.AdminPositionService;
import cn.management.service.admin.AdminUserService;
import cn.management.service.attendance.AttendanceApplicationService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 考勤申请Service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-02
 */
@Service
public class AttendanceApplicationServiceImpl extends BaseServiceImpl<AttendanceApplicationMapper, AttendanceApplication>
		implements AttendanceApplicationService {

	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private AdminPositionService adminPositionService;
	
	/**
	 * 条件查询考勤申请列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<AttendanceApplication> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AttendanceApplication> list = mapper.selectByExample(example);
        //设置申请类型名称、状态名称、申请人姓名
        setName(list);
        return list;
    }
	
	/**
	 * 设置申请类型名称、状态名称、申请人姓名
	 * @param list
	 */
    private void setName(List<AttendanceApplication> list) {
		for (AttendanceApplication attendanceApplication : list) {
			//设置申请类型名称
			attendanceApplication.setTypeName(ApplicationTypeEnum.getName(attendanceApplication.getType()));
			//状态名称
			attendanceApplication.setStateName(ApplicationStateEnum.getName(attendanceApplication.getState()));
			//设置申请人姓名
			AdminUser user = adminUserService.getItemById(attendanceApplication.getId());
			if (user != null) {
				attendanceApplication.setRealName(user.getRealName());
			}
		}
	}

	/**
	 * 添加考勤申请
	 * @param attendanceApplication
	 * @return
	 * @throws SysException 
	 */
	@Override
	public AttendanceApplication doAdd(AttendanceApplication attendanceApplication) throws SysException {
		//设置直属上级
		AdminUser adminUser = adminUserService.getItemById(attendanceApplication.getUserId());
		if (adminUser.getLeaderId() != null) {
			throw new SysException("无直属上级.");
		}
		attendanceApplication.setLeaderId(adminUser.getLeaderId());
		//设置部门总监
		AdminPosition adminPosition = adminPositionService.getItemById(adminUser.getPostId());
		if (null == adminPosition.getMgrId() || 0 == adminPosition.getMgrId()) {
			throw new SysException("所在岗位无负责人.");
		}
		AdminUser headUser = new AdminUser();
		headUser.setPostId(adminPosition.getMgrId());
		List<AdminUser> header = adminUserService.getItems(headUser);
		if (null == header || 1 != header.size()) {
			throw new SysException("部门总监不唯一.");
		}
		attendanceApplication.setHeaderId(header.get(0).getId());
		//设置状态
		attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_SELF.getValue());
		//添加考勤申请
		return addSelectiveMapper(attendanceApplication);
	}
	
	/**
	 * 考勤申请审核
	 * @param attendanceApplication
	 * @return
	 * @throws SysException 
	 */
	@Override
	public boolean doAudit(AttendanceApplication attendanceApplication, String identity) {
		//检查权限
		SecurityUtils.getSubject().checkPermission(AttendanceIdentityEnum.getPermission(identity));
		//更新考核状态
		//个人提交考核申请
        if (AttendanceIdentityEnum.SELF.getIdentity().equals(identity)) {
        	attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_SELF.getValue());
        }
        //直接上级
        if (AttendanceIdentityEnum.LEAD.getIdentity().equals(identity)) {
        	attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_LEAD.getValue());
        }
        //部门总监
        if (AttendanceIdentityEnum.HEAD.getIdentity().equals(identity)) {
        	attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_HEAD.getValue());
        }
		return update(attendanceApplication);
	}
	
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
