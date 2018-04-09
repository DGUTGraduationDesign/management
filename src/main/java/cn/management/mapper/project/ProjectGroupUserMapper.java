package cn.management.mapper.project;

import cn.management.domain.project.ProjectGroupUser;
import cn.management.util.MyMapper;

public interface ProjectGroupUserMapper extends MyMapper<ProjectGroupUser> {

    /**
     * 根据分组id删除分组员工关联信息
     * @param groupId
     */
    void deleteByGroupId(Integer groupId);

}
