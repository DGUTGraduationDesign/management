package cn.management.service.admin;

import cn.management.domain.admin.AdminRole;
import cn.management.mapper.admin.AdminRoleMapper;
import cn.management.service.BaseService;

/**
 * 角色Service层接口
 * @author Admin
 */
public interface AdminRoleService extends BaseService<AdminRoleMapper, AdminRole> {

    /**
     * 删除角色
     * @param ids
     * @return
     */
	boolean doDelete(String ids);

}
