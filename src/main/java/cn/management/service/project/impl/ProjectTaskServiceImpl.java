package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.domain.project.ProjectGroup;
import cn.management.domain.project.ProjectItem;
import cn.management.domain.project.ProjectTask;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.InformWayEnum;
import cn.management.enums.TaskIdentityEnum;
import cn.management.enums.TaskStateEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectGroupMapper;
import cn.management.mapper.project.ProjectTaskMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectGroupService;
import cn.management.service.project.ProjectItemService;
import cn.management.service.project.ProjectTaskService;
import cn.management.util.SendMeetingInformUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目组Service层实现类
 */
@Service
public class ProjectTaskServiceImpl extends BaseServiceImpl<ProjectTaskMapper, ProjectTask> implements ProjectTaskService {

    private final static Logger logger =  LoggerFactory.getLogger(ProjectTaskServiceImpl.class);

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ProjectItemService projectItemService;

    /**
     * 条件查询任务列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProjectTask> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProjectTask> list = mapper.selectByExample(example);
        for (ProjectTask projectTask : list) {
            //设置任务对象姓名、项目名称、发布人姓名、任务状态
            setName(projectTask);
        }
        return list;
    }

    /**
     * 查询项目组详情
     * @param condition
     * @return
     */
    @Override
    public ProjectTask getItem(ProjectTask condition) {
        List<ProjectTask> items = getItemsByPage(condition, 1, 1);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        ProjectTask projectTask = items.get(0);
        //设置任务对象姓名、项目名称、发布人姓名、任务状态
        setName(projectTask);
        return projectTask;
    }

    /**
     * 设置任务对象姓名、项目名称、发布人姓名、任务状态
     * @param projectTask
     */
    public void setName(ProjectTask projectTask) {
        //任务状态
        projectTask.setTaskStateName(TaskStateEnum.getName(projectTask.getTaskState()));
        //对应项目
        ProjectItem projectItem = projectItemService.getItemById(projectTask.getItemId());
        //任务对象
        AdminUser user = adminUserService.getItemById(projectTask.getUserId());
        //发布人
        AdminUser create = adminUserService.getItemById(projectTask.getCreateBy());
        if (null != projectItem) {
            projectTask.setItemName(projectItem.getItemName());
        }
        if (null != user) {
            projectTask.setUserName(user.getRealName());
        }
        if (null != create) {
            projectTask.setCreateName(create.getRealName());
        }
    }

    /**
     * 发布任务
     * @param projectTask
     * @return
     * @throws SysException
     */
    @Override
    @Transactional
    public ProjectTask doPublish(ProjectTask projectTask, Integer loginUserId) throws SysException {
        //发布人id
        projectTask.setCreateBy(loginUserId);
        //任务对象是否存在
        if (null == adminUserService.getItemById(projectTask.getUserId())) {
            throw new SysException("任务对象不存在.");
        }
        addSelectiveMapper(projectTask);
        //发送通知
        Integer informWay = projectTask.getInformWay();
        if (null != informWay && !InformWayEnum.NONE.getValue().equals(informWay)) {
            //再开启一个新的线程发送会议邮件通知、短信通知
            Thread th = new Thread(new Runnable() {
                public void run() {
                    try {
                        projectTask.setId(null);
                        setName(projectTask);
                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送邮件通知
                            SendMeetingInformUtil.sendTaskInformMail(projectTask);
                        }
                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送短信通知
                            //SendMeetingInformUtil.sendTaskInformMessage(projectTask);
                        }
                    } catch (Exception e) {
                        logger.error(projectTask.toString(), e);
                    }
                }
            });
            th.start();
        }
        return projectTask;
    }

    /**
     * 编辑任务/完成任务
     * @param projectTask
     * @return
     * @throws SysException
     */
    @Override
    @Transactional
    public boolean doUpdate(ProjectTask projectTask, Integer loginUserId) throws SysException {
        ProjectTask condition = new ProjectTask();
        condition.setId(projectTask.getId());
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        ProjectTask task = getItem(condition);
        if (null == task) {
            throw new SysException("任务不存在.");
        }
        if (!TaskStateEnum.UNCOMPLETE.getValue().equals(task.getTaskState())) {
            throw new SysException("任务已完成或失效.");
        }
        //完成任务
        if (TaskStateEnum.COMPLETE.getValue().equals(projectTask.getTaskState())) {
            projectTask.setCompleteDate(new Date());
            //是否任务对象本人操作
            if (!loginUserId.equals(task.getUserId())) {
                throw new SysException("操作失败.非任务对象本人操作.");
            }
            //是否延期
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
            String closingDate = sd.format(task.getClosingDate());
            String completeDate = sd.format(projectTask.getCompleteDate());
            if (Integer.valueOf(completeDate) > Integer.valueOf(closingDate)) {
                projectTask.setTaskState(TaskStateEnum.DELAY.getValue());
            }
        } else { //编辑任务
            //是否发布者本人操作
            if (!loginUserId.equals(task.getUserId())) {
                throw new SysException("操作失败.非发布者本人操作.");
            }
        }
        projectTask.setUpdateTime(new Date());
        boolean flag = update(projectTask);
        //发送通知
        Integer informWay = projectTask.getInformWay();
        if (null != informWay && !InformWayEnum.NONE.getValue().equals(informWay)) {
            //再开启一个新的线程发送会议邮件通知、短信通知
            Thread th = new Thread(new Runnable() {
                public void run() {
                    ProjectTask task1 = getItemById(projectTask.getId());
                    try {
                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送邮件通知
                            SendMeetingInformUtil.sendTaskInformMail(task1);
                        }
                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送短信通知
                            //SendMeetingInformUtil.sendTaskUpdateMessage(task1);
                        }
                    } catch (Exception e) {
                        logger.error(projectTask.toString(), e);
                    }
                }
            });
            th.start();
        }
        return flag;
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectGroup.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectTask projectTask = new ProjectTask();
        projectTask.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectTask, example);
    }

}
