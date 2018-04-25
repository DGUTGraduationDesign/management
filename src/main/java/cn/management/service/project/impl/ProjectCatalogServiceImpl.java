package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectCatalog;
import cn.management.domain.project.ProjectCatalogGroup;
import cn.management.domain.project.ProjectGroup;
import cn.management.enums.CatalogTypeEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectCatalogMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectCatalogGroupService;
import cn.management.service.project.ProjectCatalogService;
import cn.management.service.project.ProjectGroupService;
import cn.management.util.Commons;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 网盘目录Service层实现类
 */
@Service
public class ProjectCatalogServiceImpl extends BaseServiceImpl<ProjectCatalogMapper, ProjectCatalog> implements ProjectCatalogService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ProjectCatalogGroupService projectCatalogGroupService;

    @Autowired
    private ProjectGroupService projectGroupService;

    @Override
    public List<ProjectCatalog> getCatalogsByIds(Integer loginId, Integer parentId) {
        //网盘目录list
        List<ProjectCatalog> catalogList = mapper.getCatalogsByIds(loginId, parentId);
        for (ProjectCatalog projectCatalog : catalogList) {
            //创建人姓名
            AdminUser user = adminUserService.getItemById(projectCatalog.getCreateBy());
            projectCatalog.setCreateByName(user.getRealName());
        }
        return catalogList;
    }

    @Override
    public ProjectCatalog getByLoginIdAndCId(Integer loginId, Integer catalogId) {
        return mapper.getByLoginIdAndCId(loginId, catalogId);
    }

    @Override
    public ProjectCatalog getItemById(Object catalogId) {
        ProjectCatalog projectCatalog = mapper.getById((Integer) catalogId);
        if (null != projectCatalog) {
            //设置项目名、创建人姓名
            setName(projectCatalog);
        }
        return projectCatalog;
    }

    /**
     * 设置项目名、创建人姓名
     * @param projectCatalog
     */
    public void setName(ProjectCatalog projectCatalog) {
        //创建人姓名
        AdminUser user = adminUserService.getItemById(projectCatalog.getCreateBy());
        projectCatalog.setCreateByName(user.getRealName());
        //项目组关联信息
        List<ProjectCatalogGroup> list = projectCatalog.getCatalogGroups();
        for (ProjectCatalogGroup projectCatalogGroup : list) {
            ProjectGroup projectGroup = projectGroupService.getItemById(projectCatalogGroup.getGroupId());
            projectCatalogGroup.setGroupName(projectGroup.getGroupName());
        }
    }

    /**
     * 添加文件目录信息
     * @param projectCatalog
     * @return
     */
    @Override
    @Transactional
    public ProjectCatalog doAdd(ProjectCatalog projectCatalog, Integer loginId) throws SysException {
        List<Integer> groupIds = new ArrayList<Integer>(5);
        //判断父目录是否为空
        if (null != projectCatalog.getParentId()) {
            ProjectCatalog parentCatalog = getByLoginIdAndCId(loginId, projectCatalog.getParentId());
            if (null == parentCatalog) {
                throw new SysException("父目录不存在.");
            }
            groupIds = projectCatalogGroupService.getGroupIdsByCId(parentCatalog.getId());
        } else {
            if (!StringUtils.isNotBlank(projectCatalog.getGroupIds())) {
                throw new SysException("项目组不能为空.");
            }
            groupIds = JSONObject.parseArray(projectCatalog.getGroupIds(), Integer.class);
        }
        //添加文件目录信息
        addSelectiveMapper(projectCatalog);
        //添加文件目录项目组关联信息
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
     * @param loginId
     * @return
     */
    @Override
    @Transactional
    public boolean doUpdate(ProjectCatalog projectCatalog, Integer loginId) throws SysException {
        ProjectCatalog findCatalog = mapper.getByLoginIdAndCId(loginId, projectCatalog.getId());
        if (null == findCatalog) {
            throw new SysException("文件目录不存在.");
        }
        //判断是否是根目录
        if (null == findCatalog.getParentId()) {
            //判断有无修改关联的项目组
            if (StringUtils.isNotBlank(projectCatalog.getGroupIds())) {
                projectCatalogGroupService.deleteByCatalogId(projectCatalog.getId());
                List<Integer> groupIds = JSONObject.parseArray(projectCatalog.getGroupIds(), Integer.class);
                updateCatalogGroup(projectCatalog.getId(), groupIds);
            }
        }
        //修改文件目录信息
        return update(projectCatalog);
    }

    /**
     * 递归修改文件目录项目组关联信息
     * @param catalogId
     * @param groupIds
     */
    public void updateCatalogGroup(Integer catalogId, List<Integer> groupIds) {
        //查询子目录
        ProjectCatalog condition = new ProjectCatalog();
        condition.setParentId(catalogId);
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        List<ProjectCatalog> childs = getItems(condition);
        //修改子目录的关联信息
        if (null != childs && 0 != childs.size()) {
            for (ProjectCatalog projectCatalog : childs) {
                updateCatalogGroup(projectCatalog.getId(), groupIds);
            }
        }
        //修改文件目录项目组关联信息
        for (Integer groupId : groupIds) {
            ProjectCatalogGroup projectCatalogGroup = new ProjectCatalogGroup();
            projectCatalogGroup.setCatalogId(catalogId);
            projectCatalogGroup.setGroupId(groupId);
            projectCatalogGroup.setCreateTime(new Date());
            projectCatalogGroup.setUpdateTime(new Date());
            projectCatalogGroupService.addSelectiveMapper(projectCatalogGroup);
        }
    }

    /**
     * 删除文件目录
     * @param ids
     * @param loginId
     * @return
     */
    @Override
    @Transactional
    public boolean doDelete(String ids, Integer loginId) {
        String[] list = ids.split(",");
        for (String id : list) {
            //判断文件是否存在以及是否在对应的项目组
            if (null != mapper.getByLoginIdAndCId(loginId, Integer.valueOf(id))) {
                deleteCatalog(Integer.valueOf(id));
            }
        }
        return true;
    }

    /**
     * 递归删除文件目录
     * @param id
     */
    public void deleteCatalog(Integer id) {
        //查询子目录
        ProjectCatalog condition = new ProjectCatalog();
        condition.setParentId(id);
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        List<ProjectCatalog> childs = getItems(condition);
        //删除子目录
        if (null != childs && 0 != childs.size()) {
            for (ProjectCatalog projectCatalog : childs) {
                deleteCatalog(projectCatalog.getId());
            }
        }
        //删除文件目录项目组关联信息
        projectCatalogGroupService.deleteByCatalogId(id);
        //删除文件目录信息
        ProjectCatalog projectCatalog = mapper.selectByPrimaryKey(id);
        //如果是文件要删除源文件
        if (!CatalogTypeEnum.DIR.getValue().equals(projectCatalog.getFileType())) {
            String path = Commons.FILE_HOST + projectCatalog.getFilePath();
            //创建jersey服务器，进行跨服务器下载
            Client client = new Client();
            //把文件关联到远程服务器
            WebResource resource = client.resource(path);
            //删除
            resource.delete();
        }
        mapper.deleteByPrimaryKey(id);
    }

}
