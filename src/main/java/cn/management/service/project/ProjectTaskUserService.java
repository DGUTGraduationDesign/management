package cn.management.service.project;

import cn.management.domain.project.ProjectTaskUser;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectTaskUserMapper;
import cn.management.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务对象关联表Service层
 */
public interface ProjectTaskUserService extends BaseService<ProjectTaskUserMapper, ProjectTaskUser> {

    /**
     * 统计未完成的任务对象数目
     * @param taskId
     * @return
     */
    int countUnComleteByTaskId(Integer taskId);

    /**
     * 任务取消
     * @param taskId
     */
    boolean setCancelByTaskId(Integer taskId);

    /**
     * 根据任务id删除任务对象关联信息，逻辑删除
     * @param taskId
     */
    void deleteByTaskId(Integer taskId);

}
