package cn.management.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminPermission;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminPermissionService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 权限控制器
 * @author Admin
 * @date 2018-02-27
 */
@Controller
@RequestMapping("admin/permission")
public class AdminPermissionController extends BaseController<AdminPermissionService, AdminPermission> {

    static Logger logger = LoggerFactory.getLogger(AdminPermissionController.class);

    /**
     * 查询所有权限
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public Result listAll() {
    	List<AdminPermission> list = service.getAllItems();
    	return new Result(ResultEnum.SUCCESS.getCode(), "查询成功", list);
    }
    
    /**
     * 条件查询权限列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("adminPermission:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AdminPermission adminPermission = JSON.parseObject((String)models.get("permission"), AdminPermission.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminPermission.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminPermission.getName())) {
            criteria.andLike("name", "%" + adminPermission.getName() + "%");
        }
        if (StringUtils.isNotBlank(adminPermission.getKey())) {
            criteria.andLike("key", "%" + adminPermission.getKey() + "%");
        }
        if (StringUtils.isNotBlank(adminPermission.getGroup())) {
            criteria.andEqualTo("group", adminPermission.getGroup());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        //设置排序
        example.setOrderByClause("id ASC");
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加权限
     * @param adminPermission
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("adminPermission:add")
    @ResponseBody
    public Result add(@RequestBody AdminPermission adminPermission) throws SysException {
    	adminPermission.setCreateTime(new Date()); 
    	adminPermission.setUpdateTime(new Date()); 
        if (service.addSelectiveMapper(adminPermission) == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改权限
     * @param adminPermission
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @RequiresPermissions("adminPermission:edit")
    @ResponseBody
    public Result edit(@RequestBody AdminPermission adminPermission) throws SysException {
    	adminPermission.setUpdateTime(new Date());
        if (service.update(adminPermission)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除权限
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("adminPermission:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) {
    	String ids = (String) models.get("ids");
    	if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        if (service.doDelete(ids)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
}
