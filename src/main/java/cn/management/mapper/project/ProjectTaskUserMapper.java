package cn.management.mapper.project;

import cn.management.domain.project.ProjectTaskUser;
import cn.management.util.MyMapper;

/**
 * 任务对象关联表Mapper
 */
public interface ProjectTaskUserMapper extends MyMapper<ProjectTaskUser> {

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
