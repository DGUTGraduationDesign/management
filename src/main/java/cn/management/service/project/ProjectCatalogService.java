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
     * 添加文件目录信息
     * @param projectCatalog
     * @return
     */
    ProjectCatalog doAdd(ProjectCatalog projectCatalog);

    /**
     * 修改文件目录信息
     * @param projectCatalog
     * @return
     */
    boolean doUpdate(ProjectCatalog projectCatalog);

    /**
     * 删除文件目录
     * @param ids
     * @return
     */
    boolean doDelete(String ids);

}
