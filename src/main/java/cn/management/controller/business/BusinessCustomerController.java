package cn.management.controller.business;

import cn.management.controller.BaseController;
import cn.management.domain.business.BusinessCustomer;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.business.BusinessCustomerService;
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
 * 客户信息控制器
 */
@Controller
@RequestMapping("business/customer")
public class BusinessCustomerController extends BaseController<BusinessCustomerService, BusinessCustomer> {

    /**
     * 条件查询客户信息列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("businessCustomer:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        BusinessCustomer businessCustomer = JSON.parseObject((String)models.get("businessCustomer"), BusinessCustomer.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(BusinessCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(businessCustomer.getName())) {
            criteria.andLike("name", "%" + businessCustomer.getName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 添加客户信息
     * @param businessCustomer
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("businessCustomer:add")
    @ResponseBody
    public Result add(@RequestBody BusinessCustomer businessCustomer, HttpServletRequest request) throws SysException {
        Integer createBy = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        businessCustomer.setCreateBy(createBy);
        businessCustomer.setCreateTime(new Date());
        businessCustomer.setUpdateTime(new Date());
        if (null == service.addSelectiveMapper(businessCustomer)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改客户信息
     * @param businessCustomer
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("businessCustomer:edit")
    @ResponseBody
    public Result edit(@RequestBody BusinessCustomer businessCustomer) throws SysException {
        businessCustomer.setUpdateTime(new Date());
        if (service.update(businessCustomer)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除客户信息
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("businessCustomer:delete")
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