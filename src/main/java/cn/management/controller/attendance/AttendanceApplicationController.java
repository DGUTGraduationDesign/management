package cn.management.controller.attendance;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.attendance.AttendanceApplication;
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
 * @author ZhouJiaKai
 * @date 2018-02-28
 */
@Controller
@RequestMapping("attendance/application")
public class AttendanceApplicationController extends BaseController<AttendanceApplicationService, AttendanceApplication> {

    static Logger logger = LoggerFactory.getLogger(AttendanceApplicationController.class);

    @RequestMapping("/{identity}/index")
	@ResponseBody
	public Result index(@RequestBody Map<String, Object> models, HttpServletRequest request) {
    	String identity = (String) models.get("identity");
    	//检查权限
    	SecurityUtils.getSubject().checkPermission(AttendanceIdentityEnum.getPermission(identity));
    	//拼接条件
    	Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
    	AttendanceApplication attendanceApplication = JSON.parseObject((String)models.get("application"), AttendanceApplication.class);
    	Integer page = (Integer) models.get("page");
        Example example = new Example(AttendanceApplication.class);
        Example.Criteria criteria = example.createCriteria();
        //个人
        if (AttendanceIdentityEnum.SELF.getIdentity().equals(identity)) {
        	criteria.andEqualTo("userId", loginId);
        }
        //直接上级
        if (AttendanceIdentityEnum.LEAD.getIdentity().equals(identity)) {
        	criteria.andEqualTo("leaderId", loginId);
        }
        //部门总监
        if (AttendanceIdentityEnum.HEAD.getIdentity().equals(identity)) {
        	criteria.andEqualTo("headerId", loginId);
        }
        if (StringUtils.isNotBlank(attendanceApplication.getRealName())) {
        	criteria.andLike("realName", attendanceApplication.getRealName());
        }
        if (attendanceApplication.getState() != null) {
        	criteria.andEqualTo("state", attendanceApplication.getState());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加考核申请
     * @param adminUser
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("attendanceApplication:add")
    @ResponseBody
    public Result add(@RequestBody AttendanceApplication attendanceApplication, HttpServletRequest request) throws SysException {
    	Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
    	attendanceApplication.setUserId(loginId);
    	AttendanceApplication application = service.doAdd(attendanceApplication);
        if (application == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 考勤申请审核
     * @param adminUser
     * @return
     * @throws SysException 
     */
    @RequestMapping("/audit")
    @ResponseBody
    public Result audit(@RequestBody Map<String, Object> models) throws SysException {
    	String identity = (String) models.get("identity");
    	AttendanceApplication attendanceApplication = JSON.parseObject((String)models.get("application"), AttendanceApplication.class);
        if (service.doAudit(attendanceApplication, identity)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除考勤申请
     * @param ids
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
