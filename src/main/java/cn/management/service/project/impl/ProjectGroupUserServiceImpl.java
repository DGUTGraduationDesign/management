package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectGroupUser;
import cn.management.mapper.project.ProjectGroupUserMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectGroupUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectGroupUserServiceImpl extends BaseServiceImpl<ProjectGroupUserMapper, ProjectGroupUser> implements ProjectGroupUserService {

    /**
     * 根据项目Id查找员工
     * @param itemId
     * @return
     */
    public List<AdminUser> getUsersByItemId(Integer itemId) {
        return mapper.getUsersByItemId(itemId);
    }

    /**
     * 根据分组id删除分组员工关联信息
     * @param groupId
     */
    @Override
    public void deleteByGroupId(Integer groupId) {
        mapper.deleteByGroupId(groupId);
    }

}
