package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectItem;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.project.ProjectItemService;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Map;

/**
 * 项目控制器
 * @author ZhouJiaKai
 * @date 2018-03-13
 */
@Controller
@RequestMapping("project/item")
public class ProjectItemController extends BaseController<ProjectItemService, ProjectItem> {

    /**
     * 条件查询项目列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("projectItem:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        ProjectItem projectItem = JSON.parseObject((String)models.get("projectItem"), ProjectItem.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(ProjectItem.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(projectItem.getItemName())) {
            criteria.andLike("itemName", "%" + projectItem.getItemName() + "%");
        }
        if (null != projectItem.getItemState()) {
            criteria.andEqualTo("itemState", projectItem.getItemState());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 添加项目
     * @param projectItem
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("projectItem:add")
    @ResponseBody
    public Result add(@RequestBody ProjectItem projectItem) throws SysException {
        projectItem.setCreateTime(new Date());
        projectItem.setUpdateTime(new Date());
        if (null == service.addSelectiveMapper(projectItem)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改项目组
     * @param projectItem
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("projectItem:edit")
    @ResponseBody
    public Result edit(@RequestBody ProjectItem projectItem) throws SysException {
        projectItem.setUpdateTime(new Date());
        if (service.update(projectItem)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除项目
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("projectItem:delete")
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
