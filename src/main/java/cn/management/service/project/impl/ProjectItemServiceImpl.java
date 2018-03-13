package cn.management.service.project.impl;

import cn.management.domain.project.ProjectItem;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.project.ProjectItemMapper;
import cn.management.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 项目Service层实现类
 */
@Service
public class ProjectItemServiceImpl extends BaseServiceImpl<ProjectItemMapper, ProjectItem> implements cn.management.service.project.ProjectItemService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectItem.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectItem projectItem = new ProjectItem();
        projectItem.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectItem, example);
    }

}
