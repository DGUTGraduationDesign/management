package cn.management.mapper.project;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectGroupUser;
import cn.management.util.MyMapper;

import java.util.List;

public interface ProjectGroupUserMapper extends MyMapper<ProjectGroupUser> {

    /**
     * 根据项目Id查找员工
     * @param itemId
     * @return
     */
    List<AdminUser> getUsersByItemId(Integer itemId);

    /**
     * 根据分组id删除分组员工关联信息
     * @param groupId
     */
    void deleteByGroupId(Integer groupId);

}
