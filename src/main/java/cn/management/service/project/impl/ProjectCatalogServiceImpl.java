package cn.management.service.project.impl;

import cn.management.domain.project.ProjectCatalog;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectCatalogMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectCatalogService;
import cn.management.service.project.ProjectGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 网盘目录Service层实现类
 */
@Service
public class ProjectCatalogServiceImpl extends BaseServiceImpl<ProjectCatalogMapper, ProjectCatalog> implements ProjectCatalogService {

    @Override
    public List<ProjectCatalog> getCatalogsByIds(Integer loginId, Integer parentId) {
        //网盘目录list
        List<ProjectCatalog> catalogList = mapper.getCatalogsByIds(loginId, parentId);
        return catalogList;
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectCatalog.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectCatalog projectItem = new ProjectCatalog();
        projectItem.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectItem, example);
    }

}
