package cn.management.service.project.impl;

import cn.management.domain.project.ProjectTaskUser;
import cn.management.mapper.project.ProjectTaskUserMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectTaskUserService;
import org.springframework.stereotype.Service;

/**
 * 任务对象关联表Service层实现类
 */
@Service
public class ProjectTaskUserServiceImpl extends BaseServiceImpl<ProjectTaskUserMapper, ProjectTaskUser> implements ProjectTaskUserService {

    /**
     * 统计未完成的任务对象数目
     * @param taskId
     * @return
     */
    @Override
    public int countUnComleteByTaskId(Integer taskId) {
        return mapper.countUnComleteByTaskId(taskId);
    }

    /**
     * 任务取消
     * @param taskId
     */
    @Override
    public boolean setCancelByTaskId(Integer taskId) {
        return mapper.setCancelByTaskId(taskId);
    }

    /**
     * 根据分组id删除分组员工关联信息
     * @param taskId
     */
    @Override
    public void deleteByTaskId(Integer taskId) {
        mapper.deleteByTaskId(taskId);
    }

}
