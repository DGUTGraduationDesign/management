package cn.management.service.project;

import cn.management.domain.project.ProjectCatalog;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectCatalogMapper;
import cn.management.service.BaseService;

import java.util.List;

/**
 * 网盘目录Service层接口
 */
public interface ProjectCatalogService extends BaseService<ProjectCatalogMapper, ProjectCatalog> {

    /**
     * 查询网盘目录和文件
     * @param loginId
     * @return
     */
    List<ProjectCatalog> getCatalogsByIds(Integer loginId, Integer parentId);

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
