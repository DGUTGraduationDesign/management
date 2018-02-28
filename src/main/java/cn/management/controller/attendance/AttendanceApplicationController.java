package cn.management.controller.attendance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.management.controller.BaseController;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.service.attendance.AttendanceApplicationService;

/**
 * 工时统计控制器
 * @author ZhouJiaKai
 * @date 2018-02-28
 */
@Controller
@RequestMapping("attendance/application")
public class AttendanceApplicationController extends BaseController<AttendanceApplicationService, AttendanceApplication> {

}
