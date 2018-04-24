package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.domain.project.ProjectGroup;
import cn.management.domain.project.ProjectItem;
import cn.management.domain.project.ProjectTask;
import cn.management.domain.project.ProjectTaskUser;
import cn.management.domain.project.dto.ProjectMyTaskDto;
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
import cn.management.service.project.ProjectTaskUserService;
import cn.management.util.SendMeetingInformUtil;
import cn.management.util.SendTaskInformUtil;
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

    @Autowired
    private ProjectTaskUserService projectTaskUserService;

    /**
     * 条件查询已发布的任务列表
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
            //设置项目名称、发布人姓名、任务状态
            setName(projectTask);
        }
        return list;
    }

    /**
     * 查询我的任务列表
     * @param projectTask
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProjectMyTaskDto> getMyTaskByPage(ProjectTask projectTask, Integer loginUserId, Integer page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProjectMyTaskDto> list = mapper.selectItemByUserIdAndCond(projectTask, loginUserId);
        for (ProjectMyTaskDto projectMyTaskDto : list) {
            //任务状态
            projectMyTaskDto.setTaskStateName(TaskStateEnum.getName(projectMyTaskDto.getTaskState()));
            //对应项目
            ProjectItem projectItem = projectItemService.getItemById(projectMyTaskDto.getItemId());
            //发布人
            AdminUser create = adminUserService.getItemById(projectMyTaskDto.getCreateBy());
            if (null != projectItem) {
                projectMyTaskDto.setItemName(projectItem.getItemName());
            }
            if (null != create) {
                projectMyTaskDto.setCreateName(create.getRealName());
            }
        }
        return list;
    }

    /**
     * 查询任务详情
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
        //设置项目名称、发布人姓名、任务状态
        setName(projectTask);
        //设置任务对象关联信息
        setUsers(projectTask);
        return projectTask;
    }

    /**
     * 设置项目名称、发布人姓名、任务状态
     * @param projectTask
     */
    public void setName(ProjectTask projectTask) {
        //任务状态
        projectTask.setTaskStateName(TaskStateEnum.getName(projectTask.getTaskState()));
        //对应项目
        ProjectItem projectItem = projectItemService.getItemById(projectTask.getItemId());
        //发布人
        AdminUser create = adminUserService.getItemById(projectTask.getCreateBy());
        if (null != projectItem) {
            projectTask.setItemName(projectItem.getItemName());
        }
        if (null != create) {
            projectTask.setCreateName(create.getRealName());
        }
    }

    /**
     * 设置任务对象关联信息
     * @param projectTask
     */
    public void setUsers(ProjectTask projectTask) {
        ProjectTaskUser condition = new ProjectTaskUser();
        condition.setTaskId(projectTask.getId());
        List<ProjectTaskUser> users = projectTaskUserService.getItems(condition);
        for (ProjectTaskUser projectTaskUser : users) {
            projectTaskUser.setTaskStateName(TaskStateEnum.getName(projectTaskUser.getTaskState()));
            AdminUser user = adminUserService.getItemById(projectTaskUser.getUserId());
            projectTaskUser.setUserName(user.getRealName());
        }
        projectTask.setUserList(users);
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
        addSelectiveMapper(projectTask);
        //添加任务对象关联信息
        List<Integer> userIds = JSONObject.parseArray(projectTask.getUserIds(), Integer.class);
        for (Integer userId : userIds) {
            ProjectTaskUser projectTaskUser = new ProjectTaskUser();
            projectTaskUser.setTaskId(projectTask.getId());
            projectTaskUser.setUserId(userId);
            projectTaskUser.setCreateTime(new Date());
            projectTaskUser.setUpdateTime(new Date());
            projectTaskUserService.addSelectiveMapper(projectTaskUser);
        }
        //发送通知
        projectTask.setId(null);
        setName(projectTask);
        setUsers(projectTask);
        sendTaskInform(projectTask, projectTask.getInformWay());
        return projectTask;
    }

    /**
     * 编辑任务
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
        //是否发布者本人操作
        if (!loginUserId.equals(task.getCreateBy())) {
            throw new SysException("操作失败.非发布者本人操作.");
        }
        //任务状态
        if (!TaskStateEnum.UNCOMPLETE.getValue().equals(task.getTaskState())) {
            throw new SysException("任务已完成或失效.");
        }
        projectTask.setUpdateTime(new Date());
        boolean flag = update(projectTask);
        //发送通知
        ProjectTask task1 = getItem(projectTask);
        sendTaskInform(task1, projectTask.getInformWay());
        return flag;
    }

    /**
     * 完成任务
     * @param taskId
     * @param loginUserId
     * @return
     * @throws SysException
     */
    @Override
    @Transactional
    public boolean doComplete(Integer taskId, Integer loginUserId, Integer informWay) throws SysException {
        //任务信息
        ProjectTask taskCondition = new ProjectTask();
        taskCondition.setId(taskId);
        taskCondition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        ProjectTask task = getItem(taskCondition);
        if (null == task) {
            throw new SysException("任务不存在.");
        }
        //任务对象关联信息
        ProjectTaskUser taskUserCondition = new ProjectTaskUser();
        taskUserCondition.setTaskId(taskId);
        taskUserCondition.setUserId(loginUserId);
        ProjectTaskUser taskUser = projectTaskUserService.getItem(taskUserCondition);
        //任务状态
        if (!TaskStateEnum.UNCOMPLETE.getValue().equals(taskUser.getTaskState())) {
            throw new SysException("任务已完成或失效.");
        }
        //是否任务对象本人操作
        if (!loginUserId.equals(taskUser.getUserId())) {
            throw new SysException("操作失败.非任务对象本人操作.");
        }
        //任务完成日期
        taskUser.setCompleteDate(new Date());
        //是否延期
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        String closingDate = sd.format(task.getClosingDate());
        String completeDate = sd.format(taskUser.getCompleteDate());
        if (Integer.valueOf(completeDate) > Integer.valueOf(closingDate)) {
            taskUser.setTaskState(TaskStateEnum.DELAY.getValue());
        } else {
            taskUser.setTaskState(TaskStateEnum.COMPLETE.getValue());
        }
        taskUser.setUpdateTime(new Date());
        boolean flag = projectTaskUserService.update(taskUser);
        //如果任务对象都已经完成任务则更新任务总状态
        if (0 == projectTaskUserService.countUnComleteByTaskId(taskId)) {
            if (Integer.valueOf(completeDate) > Integer.valueOf(closingDate)) {
                task.setTaskState(TaskStateEnum.DELAY.getValue());
            } else {
                task.setTaskState(TaskStateEnum.COMPLETE.getValue());
            }
            task.setUpdateTime(new Date());
            //更新任务信息
            update(task);
            //发送通知
            task = getItem(task);
            sendTaskInform(task, informWay);
        }
        return flag;
    }

    /**
     * 取消任务
     * @param taskId
     * @param loginUserId
     * @return
     * @throws SysException
     */
    @Override
    @Transactional
    public boolean doCancel(Integer taskId, Integer loginUserId, Integer informWay) throws SysException {
        ProjectTask condition = new ProjectTask();
        condition.setId(taskId);
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        ProjectTask task = getItem(condition);
        if (null == task) {
            throw new SysException("任务不存在.");
        }
        //是否发布者本人操作
        if (!loginUserId.equals(task.getCreateBy())) {
            throw new SysException("操作失败.非发布者本人操作.");
        }
        //任务状态
        if (!TaskStateEnum.UNCOMPLETE.getValue().equals(task.getTaskState())) {
            throw new SysException("任务已完成或失效.");
        }
        //更新任务信息
        task.setTaskState(TaskStateEnum.CANCEL.getValue());
        task.setUpdateTime(new Date());
        boolean flag = update(task);
        //更新任务对象关联信息
        projectTaskUserService.setCancelByTaskId(taskId);
        //发送通知
        sendTaskInform(task, informWay);
        return flag;
    }

    /**
     * 发送任务通知
     * @param projectTask
     * @param informWay
     */
    public void sendTaskInform(ProjectTask projectTask, Integer informWay) {
        if (null != informWay && !InformWayEnum.NONE.getValue().equals(informWay)) {
            //再开启一个新的线程发送会议邮件通知、短信通知
            Thread th = new Thread(new Runnable() {
                public void run() {
                    try {
                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送邮件通知
                            SendTaskInformUtil.sendTaskInformMail(projectTask);
                        }
                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送短信通知
                            SendTaskInformUtil.sendProjectNoticeMessage(projectTask);
                        }
                    } catch (Exception e) {
                        logger.error(projectTask.toString(), e);
                    }
                }
            });
            th.start();
        }
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) throws SysException {
        String[] list = ids.split(",");
        for (String id : list) {
            ProjectTask task = getItemById(Integer.valueOf(id));
            if (TaskStateEnum.UNCOMPLETE.getValue().equals(task.getTaskState())) {
                throw new SysException("任务进行中，若想删除请先取消.");
            }
            //删除任务对象关联信息
            projectTaskUserService.deleteByTaskId(Integer.valueOf(id));
            //删除任务信息
            task.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
            update(task);
        }
        return true;
    }

}
