package cn.management.service.project.impl;

import cn.management.domain.project.ProjectCatalog;
import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.TaskStateEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectCatalogMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectCatalogGroupService;
import cn.management.service.project.ProjectCatalogService;
import cn.management.service.project.ProjectGroupService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 网盘目录Service层实现类
 */
@Service
public class ProjectCatalogServiceImpl extends BaseServiceImpl<ProjectCatalogMapper, ProjectCatalog> implements ProjectCatalogService {

    @Autowired
    private ProjectCatalogGroupService projectCatalogGroupService;

    @Override
    public List<ProjectCatalog> getCatalogsByIds(Integer loginId, Integer parentId) {
        //网盘目录list
        List<ProjectCatalog> catalogList = mapper.getCatalogsByIds(loginId, parentId);
        return catalogList;
    }

    /**
     * 添加文件目录信息
     * @param projectCatalog
     * @return
     */
    @Override
    @Transactional
    public ProjectCatalog doAdd(ProjectCatalog projectCatalog) {
        //添加文件目录信息
        addSelectiveMapper(projectCatalog);
        //添加文件目录项目组关联信息
        List<Integer> groupIds = JSONObject.parseArray(projectCatalog.getGroupIds(), Integer.class);
        for (Integer groupId : groupIds) {
            ProjectCatalogGroup projectCatalogGroup = new ProjectCatalogGroup();
            projectCatalogGroup.setCatalogId(projectCatalog.getId());
            projectCatalogGroup.setGroupId(groupId);
            projectCatalogGroup.setCreateTime(new Date());
            projectCatalogGroup.setUpdateTime(new Date());
            projectCatalogGroupService.addSelectiveMapper(projectCatalogGroup);
        }
        return projectCatalog;
    }

    /**
     * 修改文件目录信息
     * @param projectCatalog
     * @return
     */
    @Override
    @Transactional
    public boolean doUpdate(ProjectCatalog projectCatalog) {
        //修改文件目录项目组关联信息
        projectCatalogGroupService.deleteByCatalogId(projectCatalog.getId());
        List<Integer> groupIds = JSONObject.parseArray(projectCatalog.getGroupIds(), Integer.class);
        for (Integer groupId : groupIds) {
            ProjectCatalogGroup projectCatalogGroup = new ProjectCatalogGroup();
            projectCatalogGroup.setCatalogId(projectCatalog.getId());
            projectCatalogGroup.setGroupId(groupId);
            projectCatalogGroup.setCreateTime(new Date());
            projectCatalogGroup.setUpdateTime(new Date());
            projectCatalogGroupService.addSelectiveMapper(projectCatalogGroup);
        }
        //修改文件目录信息
        return update(projectCatalog);
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        String[] list = ids.split(",");
        for (String id : list) {
            //这里要递归查询所有子文件子目录id然后再删除
            //修改文件目录项目组关联信息
//            projectCatalogGroupService.deleteByCatalogId(Integer.valueOf(id));
            //删除文件目录信息
//            ProjectCatalog projectCatalog = getItemById(Integer.valueOf(id));
//            projectCatalog.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
//            update(projectCatalog);
        }
        return true;
    }

}
