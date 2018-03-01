package cn.management.controller.attendance;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
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
	
}
