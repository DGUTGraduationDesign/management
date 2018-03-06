package cn.management.controller.meeting;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminDataDict;
import cn.management.domain.meeting.MeetingRoom;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.meeting.MeetingRoomService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 会议室控制器
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
@Controller
@RequestMapping("meeting/room")
public class MeetingRoomController extends BaseController<MeetingRoomService, MeetingRoom> {

	/**
     * 条件会议室列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("meetingRoom:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	MeetingRoom meetingRoom = JSON.parseObject((String)models.get("meetingRoom"), MeetingRoom.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminDataDict.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(meetingRoom.getRoomName())) {
            criteria.andLike("roomName", "%" + meetingRoom.getRoomName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加会议室
     * @param meetingRoom
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("meetingRoom:add")
    @ResponseBody
    public Result add(@RequestBody MeetingRoom meetingRoom) throws SysException {
    	meetingRoom.setCreateTime(new Date()); 
    	meetingRoom.setUpdateTime(new Date()); 
        if (null == service.addSelectiveMapper(meetingRoom)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改会议室
     * @param meetingRoom
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @RequiresPermissions("meetingRoom:edit")
    @ResponseBody
    public Result edit(@RequestBody MeetingRoom meetingRoom) throws SysException {
    	meetingRoom.setUpdateTime(new Date());
        if (service.update(meetingRoom)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除会议室
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("meetingRoom:delete")
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
