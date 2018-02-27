package cn.management.service.admin;

import cn.management.domain.admin.AdminPermission;
import cn.management.mapper.admin.AdminPermissionMapper;
import cn.management.service.BaseService;

/**
 * 权限Service层接口
 * @author Admin
 */
public interface AdminPermissionService extends BaseService<AdminPermissionMapper, AdminPermission> {

    /**
     * 删除权限
     * @param ids
     * @return
     */
	boolean doDelete(String ids);

}
