package cn.management.service.admin;

import cn.management.domain.admin.AdminDepartment;
import cn.management.mapper.admin.AdminDepartmentMapper;
import cn.management.service.BaseService;

/**
 * 部门Service层接口
 * @author Admin
 */
public interface AdminDepartmentService extends BaseService<AdminDepartmentMapper, AdminDepartment> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
