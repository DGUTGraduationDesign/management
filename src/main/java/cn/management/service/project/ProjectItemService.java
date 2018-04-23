package cn.management.service.project;

import cn.management.domain.project.ProjectItem;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectItemMapper;
import cn.management.service.BaseService;

/**
 * 项目Service层接口
 */
public interface ProjectItemService extends BaseService<ProjectItemMapper, ProjectItem> {

    /**
     * 修改项目信息
     * @param projectItem
     * @param loginUserId
     * @return
     */
    boolean doUpdate(ProjectItem projectItem, Integer loginUserId) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
