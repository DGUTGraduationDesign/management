package cn.management.mapper.project;

import cn.management.domain.project.ProjectCatalog;
import cn.management.util.MyMapper;

import java.util.List;

/**
 * 网盘目录Mapper
 */
public interface ProjectCatalogMapper extends MyMapper<ProjectCatalog> {

    /**
     * 根据用户id获取网盘目录
     * @param loginId
     * @return
     */
    List<ProjectCatalog> getCatalogsByLoginId(Integer loginId);

}
