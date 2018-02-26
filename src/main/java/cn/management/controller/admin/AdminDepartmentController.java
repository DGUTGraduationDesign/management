package cn.management.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminDepartment;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminDepartmentService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 部门控制器
 * @author Admin
 * @date 2018-02-23
 */
@Controller
@RequestMapping("admin/department")
public class AdminDepartmentController extends BaseController<AdminDepartmentService, AdminDepartment> {

    static Logger logger = LoggerFactory.getLogger(AdminDepartmentController.class);

    /**
     * 查询所有部门
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public Result listAll() {
    	List<AdminDepartment> list = service.getAllItems();
    	return new Result(ResultEnum.SUCCESS.getCode(), "查询成功", list);
    }
    
    /**
     * 条件查询部门列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AdminDepartment adminDepartment = JSON.parseObject((String)models.get("department"), AdminDepartment.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminDepartment.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminDepartment.getDeptName())) {
            criteria.andLike("deptName", "%" + adminDepartment.getDeptName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加部门信息
     * @param adminDepartment
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody AdminDepartment adminDepartment) throws SysException {
    	adminDepartment.setCreateTime(new Date()); 
    	adminDepartment.setUpdateTime(new Date()); 
        if (service.addSelectiveMapper(adminDepartment) == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改部门信息
     * @param adminDepartment
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody AdminDepartment adminDepartment) throws SysException {
    	adminDepartment.setUpdateTime(new Date());
        if (service.update(adminDepartment)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除部门信息
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
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
