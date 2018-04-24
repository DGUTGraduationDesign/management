package cn.management.mapper.project;

import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.util.MyMapper;

public interface ProjectCatalogGroupMapper extends MyMapper<ProjectCatalogGroup> {

    /**
     * 根据文件目录id删除目录项目组关联信息
     * @param catalogId
     */
    void deleteByCatalogId(Integer catalogId);

}
