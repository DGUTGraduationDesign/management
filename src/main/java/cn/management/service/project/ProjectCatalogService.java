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
     * 根据用户id和文件目录id获取文件目录
     * @param loginId
     * @param catalogId
     * @return
     */
    ProjectCatalog getByLoginIdAndCId(Integer loginId, Integer catalogId);

    /**
     * 根据用户id和文件目录id获取文件路径
     * @param loginId
     * @param catalogIds
     * @return
     */
    List<ProjectCatalog> getByLoginIdAndCIds(Integer loginId, String catalogIds);

    /**
     * 添加文件目录信息
     * @param projectCatalog
     * @return
     */
    ProjectCatalog doAdd(ProjectCatalog projectCatalog, Integer loginId) throws SysException;

    /**
     * 修改文件目录信息
     * @param projectCatalog
     * @param loginId
     * @return
     */
    boolean doUpdate(ProjectCatalog projectCatalog, Integer loginId) throws SysException;

    /**
     * 删除文件目录
     * @param ids
     * @param loginId
     * @return
     */
    boolean doDelete(String ids, Integer loginId);

}
