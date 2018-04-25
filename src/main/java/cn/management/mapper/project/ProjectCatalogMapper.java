package cn.management.mapper.project;

import cn.management.domain.project.ProjectCatalog;
import cn.management.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网盘目录Mapper
 */
public interface ProjectCatalogMapper extends MyMapper<ProjectCatalog> {

    /**
     * 根据用户id和父目录id获取网盘目录
     * @param loginId
     * @return
     */
    List<ProjectCatalog> getCatalogsByIds(@Param("loginId") Integer loginId, @Param("parentId") Integer parentId);

    /**
     * 根据用户id和文件目录id获取文件目录
     * @param loginId
     * @param catalogId
     * @return
     */
    ProjectCatalog getByLoginIdAndCId(@Param("loginId") Integer loginId, @Param("catalogId") Integer catalogId);

    /**
     * 根据id查询目录详细信息
     * @param catalogId
     * @return
     */
    ProjectCatalog getById(Integer catalogId);

}
