package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectGroup;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.project.ProjectGroupService;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 项目组控制器
 * @author ZhouJiaKai
 * @date 2018-03-13
 */
@Controller
@RequestMapping("project/group")
public class ProjectGroupController extends BaseController<ProjectGroupService, ProjectGroup> {

    /**
     * 条件查询项目组列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("projectGroup:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        ProjectGroup projectGroup = JSON.parseObject((String)models.get("projectGroup"), ProjectGroup.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(ProjectGroup.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(projectGroup.getGroupName())) {
            criteria.andLike("groupName", "%" + projectGroup.getGroupName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 查询项目组详情
     * @param models
     * @return
     */
    @RequestMapping("/findProjectGroupById")
    @RequiresPermissions("projectGroup:findbyId")
    @ResponseBody
    public Result findProjectGroupById(@RequestBody Map<String, Object> models) {
        Integer projectGroupId = (Integer) models.get("projectGroupId");
        ProjectGroup condition = new ProjectGroup();
        condition.setId(projectGroupId);
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        ProjectGroup projectGroup = service.getItem(condition);
        if (null == projectGroup) {
            return new Result(ResultEnum.NO_RECORDS);
        } else {
            return new Result(ResultEnum.SUCCESS, projectGroup, 1, 0, 1);
        }
    }

    /**
     * 添加项目组
     * @param projectGroup
     * @param request
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("projectGroup:add")
    @ResponseBody
    public Result add(@RequestBody ProjectGroup projectGroup, HttpServletRequest request) throws SysException {
        Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        projectGroup.setCreatBy(loginUserId);
        projectGroup.setCreateTime(new Date());
        projectGroup.setUpdateTime(new Date());
        if (null == service.doAdd(projectGroup)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改项目组
     * @param projectGroup
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("projectGroup:edit")
    @ResponseBody
    public Result edit(@RequestBody ProjectGroup projectGroup) throws SysException {
        projectGroup.setUpdateTime(new Date());
        if (service.doUpdate(projectGroup)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除项目组
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("projectGroup:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) {
        String ids = (String) models.get("ids");
        if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        if (service.logicalDelete(ids)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
