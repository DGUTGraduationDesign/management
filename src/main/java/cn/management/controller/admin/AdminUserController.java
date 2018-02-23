package cn.management.controller.admin;

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
import com.github.pagehelper.PageInfo;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminUser;
import cn.management.enums.ResultEnum;
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
    @RequestMapping("list")
    @ResponseBody
    public Result list(@RequestBody Map<String, Object> models) {
    	AdminUser adminUser = JSON.parseObject((String)models.get("user"), AdminUser.class);
    	Integer page = (Integer) models.get("page");
    	Example example = new Example(AdminUser.class);
    	Example.Criteria criteria = example.createCriteria();
    	if (StringUtils.isNotBlank(adminUser.getRealName())) {
    		criteria.andLike("real_name", "%" + adminUser.getRealName() + "%");
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
    	setExample(example);
    	List<AdminUser> list = service.getItemsByPage(getExample(), page, pageSize);
    	if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
    	}
		PageInfo<AdminUser> pageInfo = new PageInfo<AdminUser>(list);
    	return new Result(ResultEnum.SUCCESS, pageInfo.getList(), pageInfo.getSize(), pageInfo.getPageNum(), getPageSize());
    }
    
    /**
     * 添加员工信息
     * @param adminUser
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public Result add(@RequestBody AdminUser adminUser) {
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
     */
    @RequestMapping("edit")
    @ResponseBody
    public Result edit(@RequestBody AdminUser adminUser) {
		if (service.doUpdate(adminUser)) {
	        return new Result(ResultEnum.FAIL);
		} else {
	        return new Result(ResultEnum.SUCCESS);
		}
    }
	
    /**
     * 批量删除员工信息
     * @param ids
     * @return
     */
    public Result delete(@RequestBody String ids) {
    	if (ids == null) {
    		return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
    	}
    	Example example = new Example(AdminUser.class);
    	example.createCriteria().andCondition("id IN(" + ids + ")");
    	if (service.delele(example)) {
            return new Result(ResultEnum.SUCCESS);
    	} else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    
}
