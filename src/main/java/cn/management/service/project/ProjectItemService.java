package cn.management.service.project;

import cn.management.domain.project.ProjectItem;
import cn.management.mapper.project.ProjectItemMapper;
import cn.management.service.BaseService;

/**
 * 项目Service层接口
 */
public interface ProjectItemService extends BaseService<ProjectItemMapper, ProjectItem> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
