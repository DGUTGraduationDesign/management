package cn.management.service.project;

import cn.management.domain.project.ProjectTask;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectTaskMapper;
import cn.management.service.BaseService;

/**
 * 任务Service层接口
 */
public interface ProjectTaskService extends BaseService<ProjectTaskMapper, ProjectTask> {

    /**
     * 添加考勤申请
     * @param projectTask
     * @return
     * @throws SysException
     */
    ProjectTask doPublish(ProjectTask projectTask, Integer loginUserId) throws SysException;

    /**
     * 编辑任务/完成任务
     * @param projectTask
     * @param loginUserId
     * @return
     * @throws SysException
     */
    boolean doUpdate(ProjectTask projectTask, Integer loginUserId) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
