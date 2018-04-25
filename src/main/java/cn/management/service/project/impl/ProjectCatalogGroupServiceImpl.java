package cn.management.service.project.impl;

import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectCatalogGroupMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectCatalogGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectCatalogGroupServiceImpl extends BaseServiceImpl<ProjectCatalogGroupMapper, ProjectCatalogGroup> implements ProjectCatalogGroupService {

    /**
     * 根据文件目录id查询项目组id集合
     * @param catalogId
     * @return
     */
    @Override
    public List<Integer> getGroupIdsByCId(Integer catalogId) {
        return mapper.getGroupIdsByCId(catalogId);
    }

    /**
     * 根据文件目录id删除目录项目组关联信息
     * @param catalogId
     */
    @Override
    @Transactional
    public void deleteByCatalogId(Integer catalogId) {
        mapper.deleteByCatalogId(catalogId);
    }

}
