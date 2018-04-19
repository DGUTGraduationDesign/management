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
import cn.management.domain.admin.AdminRole;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminRoleService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 角色控制器
 * @author Admin
 * @date 2018-02-27
 */
@Controller
@RequestMapping("admin/role")
public class AdminRoleController extends BaseController<AdminRoleService, AdminRole> {

    static Logger logger = LoggerFactory.getLogger(AdminRoleController.class);

    /**
     * 查询所有角色
     * @return
     */
    @RequestMapping("/listAll")
    @RequiresPermissions("adminRole:list")
    @ResponseBody
    public Result listAll() {
    	List<AdminRole> list = service.getAllItems();
    	return new Result(ResultEnum.SUCCESS.getCode(), "查询成功", list);
    }
    
    /**
     * 条件查询角色列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("adminRole:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AdminRole adminRole = JSON.parseObject((String)models.get("role"), AdminRole.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminRole.getRoleName())) {
            criteria.andLike("roleName", "%" + adminRole.getRoleName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        //设置排序
        example.setOrderByClause("id ASC");
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加角色
     * @param adminRole
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("adminRole:add")
    @ResponseBody
    public Result add(@RequestBody AdminRole adminRole) throws SysException {
    	adminRole.setCreateTime(new Date()); 
    	adminRole.setUpdateTime(new Date()); 
        if (service.addSelectiveMapper(adminRole) == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改角色
     * @param adminRole
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @RequiresPermissions("adminRole:edit")
    @ResponseBody
    public Result edit(@RequestBody AdminRole adminRole) throws SysException {
    	adminRole.setUpdateTime(new Date());
        if (service.update(adminRole)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除角色
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("adminRole:delete")
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
