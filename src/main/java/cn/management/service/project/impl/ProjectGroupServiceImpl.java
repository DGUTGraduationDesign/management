package cn.management.service.project.impl;

import cn.management.domain.project.ProjectGroup;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.project.ProjectGroupMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 项目组Service层实现类
 */
@Service
public class ProjectGroupServiceImpl extends BaseServiceImpl<ProjectGroupMapper, ProjectGroup> implements ProjectGroupService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectGroup.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectGroup projectGroup = new ProjectGroup();
        projectGroup.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectGroup, example);
    }
}
