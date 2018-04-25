package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectGroup;
import cn.management.domain.project.ProjectGroupUser;
import cn.management.domain.project.ProjectItem;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.project.ProjectGroupMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectGroupService;
import cn.management.service.project.ProjectGroupUserService;
import cn.management.service.project.ProjectItemService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目组Service层实现类
 */
@Service
public class ProjectGroupServiceImpl extends BaseServiceImpl<ProjectGroupMapper, ProjectGroup> implements ProjectGroupService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ProjectItemService projectItemService;

    @Autowired
    private ProjectGroupUserService projectGroupUserService;

    /**
     * 条件查询项目列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProjectGroup> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProjectGroup> list = mapper.selectByExample(example);
        for (ProjectGroup projectGroup : list) {
            //设置项目名称、项目组组长、创建人姓名
            setName(projectGroup);
        }
        return list;
    }

    /**
     * 查询项目组详情
     * @param condition
     * @return
     */
    @Override
    public ProjectGroup getItem(ProjectGroup condition) {
        List<ProjectGroup> items = getItemsByPage(condition, 1, 1);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        ProjectGroup projectGroup = items.get(0);
        //设置项目名称、项目组组长、创建人姓名
        setName(projectGroup);
        //参会人员
        setUserList(projectGroup);
        return projectGroup;
    }

    /**
     * 条件查询项目组
     * @param condition
     * @return
     */
    @Override
    public List<ProjectGroup> getItems(ProjectGroup condition) {
        List<ProjectGroup> items = mapper.select(condition);
        for (ProjectGroup projectGroup : items) {
            //设置项目名称、项目组组长、创建人姓名
            setName(projectGroup);
        }
        return items;
    }

    /**
     * 设置项目名称、项目组组长、创建人姓名
     * @param projectGroup
     */
    public void setName(ProjectGroup projectGroup) {
        //项目
        ProjectItem projectItem = projectItemService.getItemById(projectGroup.getItemId());
        //项目组组长
        AdminUser header = adminUserService.getItemById(projectGroup.getHeadId());
        //创建人
        AdminUser createUser = adminUserService.getItemById(projectGroup.getCreateBy());
        if (null != projectItem) {
            projectGroup.setItemName(projectItem.getItemName());
        }
        if (null != header) {
            projectGroup.setHeadName(header.getRealName());
        }
        if (null != createUser) {
            projectGroup.setCreateName(createUser.getRealName());
        }
    }

    /**
     * 设置项目组成员
     * @param projectGroup
     */
    public void setUserList(ProjectGroup projectGroup) {
        //项目组成员
        List<AdminUser> users = new ArrayList<AdminUser>();
        List<Integer> userIds = JSONObject.parseArray(projectGroup.getUserIds(), Integer.class);
        for (Integer id : userIds) {
            AdminUser user = adminUserService.getItemById(id);
            users.add(user);
        }
        projectGroup.setUsers(users);
    }

    /**
     * 添加项目组
     * @param projectGroup
     * @return
     */
    @Override
    public Object doAdd(ProjectGroup projectGroup) {
        //添加项目组信息
        addSelectiveMapper(projectGroup);
        //添加项目组员工关联信息
        List<Integer> userIds = JSONObject.parseArray(projectGroup.getUserIds(), Integer.class);
        for (Integer userId : userIds) {
            ProjectGroupUser projectGroupUser = new ProjectGroupUser();
            projectGroupUser.setGroupId(projectGroup.getId());
            projectGroupUser.setUserId(userId);
            projectGroupUser.setCreateTime(new Date());
            projectGroupUser.setUpdateTime(new Date());
            projectGroupUserService.addSelectiveMapper(projectGroupUser);
        }
        return projectGroup;
    }

    @Override
    public boolean doUpdate(ProjectGroup projectGroup) {
        //修改项目组员工关联信息
        projectGroupUserService.deleteByGroupId(projectGroup.getId());
        List<Integer> userIds = JSONObject.parseArray(projectGroup.getUserIds(), Integer.class);
        for (Integer userId : userIds) {
            ProjectGroupUser projectGroupUser = new ProjectGroupUser();
            projectGroupUser.setGroupId(projectGroup.getId());
            projectGroupUser.setUserId(userId);
            projectGroupUser.setCreateTime(new Date());
            projectGroupUser.setUpdateTime(new Date());
            projectGroupUserService.addSelectiveMapper(projectGroupUser);
        }
        //修改项目组信息
        return update(projectGroup);
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        //删除项目组员工关联信息
        String[] list = ids.split(",");
        for (String id : list) {
            projectGroupUserService.deleteByGroupId(Integer.valueOf(id));
        }
        //删除项目组信息
        Example example = new Example(ProjectGroup.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectGroup projectGroup = new ProjectGroup();
        projectGroup.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectGroup, example);
    }

}
