package cn.management.service.project;

import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.mapper.project.ProjectCatalogGroupMapper;
import cn.management.service.BaseService;

import java.util.List;

public interface ProjectCatalogGroupService extends BaseService<ProjectCatalogGroupMapper, ProjectCatalogGroup> {

    /**
     * 根据文件目录id查询项目组id集合
     * @param catalogId
     * @return
     */
    List<Integer> getGroupIdsByCId(Integer catalogId);

    /**
     * 根据文件目录id删除目录项目组关联信息
     * @param catalogId
     */
    void deleteByCatalogId(Integer catalogId);

}
