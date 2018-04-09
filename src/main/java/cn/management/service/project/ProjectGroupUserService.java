package cn.management.service.project;

import cn.management.domain.project.ProjectGroupUser;
import cn.management.mapper.project.ProjectGroupUserMapper;
import cn.management.service.BaseService;

import java.util.List;

public interface ProjectGroupUserService extends BaseService<ProjectGroupUserMapper, ProjectGroupUser> {

    /**
     * 根据分组id删除分组员工关联信息
     * @param groupId
     */
    void deleteByGroupId(Integer groupId);

}
