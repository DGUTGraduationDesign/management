package cn.management.controller.attendance;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.domain.attendance.vo.ApplicationCommentVo;
import cn.management.enums.ApplicationStateEnum;
import cn.management.enums.AttendanceIdentityEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.attendance.AttendanceApplicationService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 工时统计控制器
 * 
 * @author ZhouJiaKai
 * @date 2018-02-28
 */
@Controller
@RequestMapping("attendance/application")
public class AttendanceApplicationController
		extends BaseController<AttendanceApplicationService, AttendanceApplication> {

	static Logger logger = LoggerFactory.getLogger(AttendanceApplicationController.class);

	@RequestMapping("/{identity}/index")
	@ResponseBody
	public Result index(@RequestBody Map<String, Object> models, HttpServletRequest request,
			@PathVariable String identity) {
		// 检查权限
		SecurityUtils.getSubject().checkPermission(AttendanceIdentityEnum.getPermission(identity));
		// 拼接条件
		Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		AttendanceApplication attendanceApplication = JSON.parseObject((String) models.get("application"),
				AttendanceApplication.class);
		Integer page = (Integer) models.get("page");
		Example example = new Example(AttendanceApplication.class);
		Example.Criteria criteria = example.createCriteria();
		// 个人
		if (AttendanceIdentityEnum.SELF.getIdentity().equals(identity)) {
			criteria.andEqualTo("userId", loginId);
		}
		// 直接上级
		if (AttendanceIdentityEnum.LEAD.getIdentity().equals(identity)) {
			criteria.andEqualTo("leaderId", loginId);
			criteria.andGreaterThanOrEqualTo("state", ApplicationStateEnum.STATUS_COMMIT_SELF.getValue());
			if (null != attendanceApplication.getUserId()) {
				criteria.andEqualTo("userId", attendanceApplication.getUserId());
			}
		}
		// 部门总监
		if (AttendanceIdentityEnum.HEAD.getIdentity().equals(identity)) {
			criteria.andEqualTo("headerId", loginId);
			criteria.andGreaterThanOrEqualTo("state", ApplicationStateEnum.STATUS_COMMIT_LEAD.getValue());
			if (null != attendanceApplication.getUserId()) {
				criteria.andEqualTo("userId", attendanceApplication.getUserId());
			}
		}
        // 汇总
        if (AttendanceIdentityEnum.HR.getIdentity().equals(identity)) {
            if (null != attendanceApplication.getUserId()) {
                criteria.andEqualTo("userId", attendanceApplication.getUserId());
            }
        }
		if (null != attendanceApplication.getType()) {
			criteria.andEqualTo("type", attendanceApplication.getType());
		}
		if (null != attendanceApplication.getState()) {
			criteria.andEqualTo("state", attendanceApplication.getState());
		}
		if (null != attendanceApplication.getStartDate()) {
            criteria.andGreaterThanOrEqualTo("startDate", attendanceApplication.getStartDate());
        }
		if (null != attendanceApplication.getEndDate()) {
			Date date = attendanceApplication.getEndDate();
			Calendar ct = Calendar.getInstance();
			ct.setTime(date);
			ct.add(Calendar.DATE, +1);
			Date endDate = ct.getTime();
			criteria.andLessThan("startDate", endDate);
		}
		criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
		setExample(example);
		return list(page);
	}
	
	/**
	 * 查询考勤申请详情
	 * @param models
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping("/findApplicationById")
	@RequiresPermissions("attendanceApplication:findbyId")
	@ResponseBody
	public Result findApplicationById(@RequestBody Map<String, Object> models) {
		Integer applicationId = (Integer) models.get("applicationId");
		AttendanceApplication condition = new AttendanceApplication();
		condition.setId(applicationId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		AttendanceApplication attendanceApplication = service.getItem(condition);
		if (null == attendanceApplication) {
			return new Result(ResultEnum.NO_RECORDS);
		} else {
			List<ApplicationCommentVo> commentList = service.findCommentByLeaveBillId(applicationId);
			Map<String, Object> data = new HashMap<String, Object>(1);
			data.put("application", attendanceApplication);
			data.put("commentList", commentList);
			return new Result(ResultEnum.SUCCESS, data, 1, 0, 1);
		}
	}

	/**
	 * 添加考核申请
	 * 
	 * @param attendanceApplication
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/add")
	@RequiresPermissions("attendanceApplication:add")
	@ResponseBody
	public Result add(@RequestBody AttendanceApplication attendanceApplication, HttpServletRequest request)
			throws SysException {
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		if (service.doAdd(attendanceApplication, loginUserId)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

	/**
	 * 考勤申请审核
	 * 
	 * @param attendanceApplication
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/{identity}/audit")
	@ResponseBody
	public Result audit(@RequestBody AttendanceApplication attendanceApplication, HttpServletRequest request,
			@PathVariable String identity) throws SysException {
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		if (service.doAudit(attendanceApplication, identity, loginUserId)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

	/**
	 * 考勤申请取消
	 * 
	 * @param models
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/cancel")
	@RequiresPermissions("attendanceApplication:cancel")
	@ResponseBody
	public Result cancel(@RequestBody Map<String, Object> models, HttpServletRequest request) throws SysException {
		Integer applicationId  = (Integer) models.get("applicationId");
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		if (service.doCancel(applicationId, loginUserId)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}
	
	/**
	 * 批量删除考勤申请
	 * 
	 * @param models
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("attendanceApplication:delete")
	@ResponseBody
	public Result delete(@RequestBody Map<String, Object> models) {
		String ids = (String) models.get("ids");
		if (!StringUtils.isNotBlank(ids)) {
			return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
		}
		if (service.logicalDelete(ids)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

}
