package cn.management.controller.meeting;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminDataDict;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.meeting.MeetingBespeakService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 会议室预约控制器
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
@Controller
@RequestMapping("meeting/bespeak")
public class MeetingBespeakController extends BaseController<MeetingBespeakService, MeetingBespeak> {

	/**
     * 条件会议室预约列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("meetingBespeak:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	MeetingBespeak meetingBespeak = JSON.parseObject((String)models.get("meetingBespeak"), MeetingBespeak.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminDataDict.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(meetingBespeak.getMeetingTheme())) {
            criteria.andLike("meetingTheme", "%" + meetingBespeak.getMeetingTheme() + "%");
        }
        if (null != meetingBespeak.getBespeakStatus()) {
            criteria.andEqualTo("bespeakStatus", meetingBespeak.getBespeakStatus());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
	 * 查询会议室预约详情
	 * @param models
	 * @return
	 */
	@RequestMapping("/findMeetingBespeakById")
	@RequiresPermissions("meetingBespeak:findbyId")
	@ResponseBody
	public Result findApplicationById(@RequestBody Map<String, Object> models) {
		Integer meetingBespeakId = (Integer) models.get("meetingBespeakId");
		MeetingBespeak condition = new MeetingBespeak();
		condition.setId(meetingBespeakId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		MeetingBespeak meetingBespeak = service.getItem(condition);
		if (null == meetingBespeak) {
			return new Result(ResultEnum.NO_RECORDS);
		} else {
			return new Result(ResultEnum.SUCCESS, meetingBespeak, 1, 0, 1);
		}
	}

    /**
     * 查询会议室是否已被预约
     * @param meetingBespeak
     * @return
     */
    @RequestMapping("/exists")
    @ResponseBody
    public Result exists(@RequestBody MeetingBespeak meetingBespeak) {
        if (service.existsBespeakByIdAndTime(meetingBespeak)) {
            return new Result(ResultEnum.NO_RECORDS.getCode(), "会议室可以预约.");
        } else {
            return new Result(ResultEnum.SUCCESS.getCode(), "会议室已被预约.");
        }
    }
    
    /**
     * 添加会议室预约
     * @param meetingBespeak
     * @return
     * @throws SysException 
     * @throws SchedulerException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("meetingBespeak:add")
    @ResponseBody
    public Result add(@RequestBody MeetingBespeak meetingBespeak, HttpServletRequest request) throws SysException, SchedulerException {
    	Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
    	meetingBespeak.setUserId(loginUserId);
        meetingBespeak.setCreateTime(new Date());
    	meetingBespeak.setUpdateTime(new Date());
        if (null == service.doBespeak(meetingBespeak)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改会议室预约
     * @param meetingBespeak
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @RequiresPermissions("meetingBespeak:edit")
    @ResponseBody
    public Result edit(@RequestBody MeetingBespeak meetingBespeak, HttpServletRequest request) throws SysException {
    	Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
    	meetingBespeak.setUpdateTime(new Date());
        if (service.doUpdate(meetingBespeak, loginUserId)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 取消会议室预约
     * @param models
     * @return
     * @throws SysException 
     */
    @RequestMapping("/cancel")
    @RequiresPermissions("meetingBespeak:cancel")
    @ResponseBody
    public Result cancel(@RequestBody Map<String, Object> models, HttpServletRequest request) throws SysException {
    	Integer bespeakId = (Integer) models.get("bespeakId");
    	Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        if (service.doCancel(bespeakId, loginUserId)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 修改预约通知方式
     * @param models
     * @return
     * @throws SysException 
     */
    @RequestMapping("/changeWay")
    @RequiresPermissions("meetingBespeak:edit")
    @ResponseBody
    public Result changeWay(@RequestBody Map<String, Object> models, HttpServletRequest request) throws SysException {
    	Integer bespeakId = (Integer) models.get("bespeakId");
    	Integer informWay = (Integer) models.get("informWay");
    	Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        if (service.doChangeInformWay(bespeakId, loginUserId, informWay)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除会议室预约信息
     * @param models
     * @return
     * @throws SysException 
     */
    @RequestMapping("/delete")
    @RequiresPermissions("meetingBespeak:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) throws SysException {
    	String ids = (String) models.get("ids");
    	if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        service.logicalDelete(ids);
        return new Result(ResultEnum.SUCCESS);
    }
    
}
