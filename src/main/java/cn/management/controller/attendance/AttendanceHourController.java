package cn.management.controller.attendance;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminDepartment;
import cn.management.domain.attendance.AttendanceHour;
import cn.management.domain.attendance.dto.AttendanceHourQueryDto;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.service.attendance.AttendanceHourService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 工时统计控制器
 * @author ZhouJiaKai
 * @date 2018-02-28
 */
@Controller
@RequestMapping("attendance/hour")
public class AttendanceHourController extends BaseController<AttendanceHourService, AttendanceHour> {

    static Logger logger = LoggerFactory.getLogger(AttendanceHourController.class);

    /**
     * 条件查询工时列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AttendanceHourQueryDto attendanceHourQueryDto = JSON.parseObject((String)models.get("attendanceHourQueryDto"), AttendanceHourQueryDto.class);
        Integer page = (Integer) models.get("page");
        List<AttendanceHour> list = service.getItemsByPage(attendanceHourQueryDto, page, getPageSize());
        if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        }
        PageInfo<AttendanceHour> pageInfo = new PageInfo<AttendanceHour>(list);
        return new Result(ResultEnum.SUCCESS, pageInfo.getList(), pageInfo.getSize(), pageInfo.getPageNum(), getPageSize());
    }
    
}
