package cn.management.mapper.project;

import cn.management.domain.project.ProjectTask;
import cn.management.domain.project.dto.ProjectMyTaskDto;
import cn.management.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务Mapper
 */
public interface ProjectTaskMapper extends MyMapper<ProjectTask> {

    /**
     * 查询我的任务列表
     * @param projectTask
     * @param userId
     * @return
     */
    List<ProjectMyTaskDto> selectItemByUserIdAndCond(@Param("projectTask") ProjectTask projectTask, @Param("userId") Integer userId);

}
