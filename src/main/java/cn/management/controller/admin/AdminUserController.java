package cn.management.controller.admin;

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
import cn.management.domain.admin.AdminUser;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.AdminUserService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 员工控制器
 * @author ZhouJiaKai
 * @date 2018-02-23
 */
@Controller
@RequestMapping("admin/manager")
public class AdminUserController extends BaseController<AdminUserService, AdminUser> {

    static Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    /**
     * 条件查询员工列表
     * @param adminUser
     * @return
     */
    @RequestMapping("/index")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        AdminUser adminUser = JSON.parseObject((String)models.get("user"), AdminUser.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminUser.getRealName())) {
            criteria.andLike("realName", "%" + adminUser.getRealName() + "%");
        }
        if (StringUtils.isNotBlank(adminUser.getNumber())) {
            criteria.andEqualTo("number", adminUser.getNumber());
        }
        if (null != adminUser.getDeptId() && 0 != adminUser.getDeptId()) {
            criteria.andEqualTo("deptId", adminUser.getDeptId());
        }
        if (null != adminUser.getPostId() && 0 != adminUser.getPostId()) {
            criteria.andEqualTo("postId", adminUser.getPostId());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody AdminUser adminUser) throws SysException {
        AdminUser user = service.doAdd(adminUser);
        if (user == null) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody AdminUser adminUser) throws SysException {
        if (service.doUpdate(adminUser)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除员工信息
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
    
    /**
     * 检查用户名是否被注册
     * @param loginName
     * @return
     */
    @RequestMapping("/exists")
    @ResponseBody
    public Result exists(@RequestBody Map<String, Object> models) {
    	String loginName = (String) models.get("loginName");
    	Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName", loginName);
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        if (service.countByCondition(example) == 0) {
            return new Result(ResultEnum.NO_RECORDS.getCode(), "用户名未被注册！");
        } else {
            return new Result(ResultEnum.SUCCESS.getCode(), "用户名已被注册！");
        }
    }
    
}
