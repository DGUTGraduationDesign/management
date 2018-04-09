package cn.management.service.project.impl;

import cn.management.domain.project.ProjectGroupUser;
import cn.management.mapper.project.ProjectGroupUserMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectGroupUserService;
import org.springframework.stereotype.Service;

@Service
public class ProjectGroupUserServiceImpl extends BaseServiceImpl<ProjectGroupUserMapper, ProjectGroupUser> implements ProjectGroupUserService {

    /**
     * 根据分组id删除分组员工关联信息
     * @param groupId
     */
    @Override
    public void deleteByGroupId(Integer groupId) {
        mapper.deleteByGroupId(groupId);
    }

}
