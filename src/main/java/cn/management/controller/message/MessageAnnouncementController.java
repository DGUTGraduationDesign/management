package cn.management.controller.message;

import cn.management.controller.BaseController;
import cn.management.domain.message.MessageAnnouncement;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.message.MessageAnnouncementService;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 企业公告控制器
 */
@Controller
@RequestMapping("message/announcement")
public class MessageAnnouncementController extends BaseController<MessageAnnouncementService, MessageAnnouncement> {

    /**
     * 条件查询企业公告列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("messageAnnouncement:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        MessageAnnouncement messageAnnouncement = JSON.parseObject((String)models.get("messageAnnouncement"), MessageAnnouncement.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(MessageAnnouncement.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(messageAnnouncement.getTitle())) {
            criteria.andLike("title", "%" + messageAnnouncement.getTitle() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 添加企业公告
     * @param messageAnnouncement
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("messageAnnouncement:add")
    @ResponseBody
    public Result add(@RequestBody MessageAnnouncement messageAnnouncement, HttpServletRequest request) throws SysException {
        Integer createBy = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        messageAnnouncement.setCreateBy(createBy);
        messageAnnouncement.setCreateTime(new Date());
        messageAnnouncement.setUpdateTime(new Date());
        if (null == service.addSelectiveMapper(messageAnnouncement)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改企业公告
     * @param messageAnnouncement
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("messageAnnouncement:edit")
    @ResponseBody
    public Result edit(@RequestBody MessageAnnouncement messageAnnouncement) throws SysException {
        messageAnnouncement.setUpdateTime(new Date());
        if (service.update(messageAnnouncement)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除企业公告
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("messageAnnouncement:delete")
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
