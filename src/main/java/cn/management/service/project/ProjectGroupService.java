package cn.management.service.project;

import cn.management.domain.project.ProjectGroup;
import cn.management.mapper.project.ProjectGroupMapper;
import cn.management.service.BaseService;

/**
 * 项目组Service层接口
 */
public interface ProjectGroupService extends BaseService<ProjectGroupMapper, ProjectGroup> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
