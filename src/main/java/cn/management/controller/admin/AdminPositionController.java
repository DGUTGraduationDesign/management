package cn.management.controller.admin;

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
import cn.management.domain.admin.AdminPosition;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminPositionService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 职位控制器
 * @author Admin
 * @date 2018-02-23
 */
@Controller
@RequestMapping("admin/position")
public class AdminPositionController extends BaseController<AdminPositionService, AdminPosition> {

    static Logger logger = LoggerFactory.getLogger(AdminPositionController.class);

    /**
     * 查询所有职位
     * @return
     */
    @RequestMapping("/listAll")
    @RequiresPermissions("adminPosition:list")
    @ResponseBody
    public Result listAll() {
        AdminPosition condition = new AdminPosition();
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        List<AdminPosition> list = service.getItems(condition);
        return new Result(ResultEnum.SUCCESS, list);
    }

    /**
     * 根据部门id查询职位
     * @param models
     * @return
     */
    @RequestMapping("/findPostByDeptId")
    @ResponseBody
    public Result findPostByDeptId(@RequestBody Map<String, Object> models) {
        Integer deptId = (Integer) models.get("deptId");
        AdminPosition condition = new AdminPosition();
        condition.setDeptId(deptId);
        condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
        List<AdminPosition> list = service.getItems(condition);
        if (null != list && 0 != list.size()) {
            return new Result(ResultEnum.SUCCESS, list);
        } else {
            return new Result(ResultEnum.NO_RECORDS);
        }
    }
    
    /**
     * 条件查询职位列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("adminPosition:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AdminPosition adminPosition = JSON.parseObject((String)models.get("position"), AdminPosition.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminPosition.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminPosition.getPostName())) {
            criteria.andLike("postName", "%" + adminPosition.getPostName() + "%");
        }
        if (null != adminPosition.getDeptId() && 0 != adminPosition.getDeptId()) {
            criteria.andEqualTo("deptId", adminPosition.getDeptId());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加职位信息
     * @param adminPosition
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @RequiresPermissions("adminPosition:add")
    @ResponseBody
    public Result add(@RequestBody AdminPosition adminPosition) throws SysException {
    	AdminPosition position = service.doAdd(adminPosition);
        if (position == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改职位信息
     * @param adminPosition
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @RequiresPermissions("adminPosition:edit")
    @ResponseBody
    public Result edit(@RequestBody AdminPosition adminPosition) throws SysException {
        if (service.doUpdate(adminPosition)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除职位信息
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("adminPosition:delete")
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
