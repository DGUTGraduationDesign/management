package cn.management.mapper.project;

import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.util.MyMapper;

import java.util.List;

public interface ProjectCatalogGroupMapper extends MyMapper<ProjectCatalogGroup> {

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
