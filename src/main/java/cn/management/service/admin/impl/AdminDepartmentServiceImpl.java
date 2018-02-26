package cn.management.service.admin.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.admin.AdminDepartment;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.admin.AdminDepartmentMapper;
import cn.management.service.admin.AdminDepartmentService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 部门Service层接口实现类
 * @author Admin
 */
@Service
public class AdminDepartmentServiceImpl extends BaseServiceImpl<AdminDepartmentMapper, AdminDepartment>
		implements AdminDepartmentService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
        Example example = new Example(AdminDepartment.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        AdminDepartment adminDepartment = new AdminDepartment();
        adminDepartment.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(adminDepartment, example);
	}
	
}
