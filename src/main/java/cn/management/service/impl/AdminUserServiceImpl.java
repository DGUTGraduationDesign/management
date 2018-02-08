package cn.management.service.impl;

import org.springframework.stereotype.Service;

import cn.management.domain.admin.AdminUser;
import cn.management.mapper.admin.AdminUserMapper;
import cn.management.service.AdminUserService;

/**
 * 用户Service层实现类
 */
@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

	@Override
	public AdminUser login(String loginName) {
		AdminUser findUser = new AdminUser();
		findUser.setLoginName(loginName);
		return getUserAndRoles(findUser);
	}

	@Override
	public AdminUser getUserAndRoles(AdminUser user) {
		AdminUser adminUser = this.getItem(user);
		if (adminUser != null) {
			//设置岗位职位中文名称
			//setName(adminUser);
			//获取用户角色
            //admin.setRoleList(roleService.getItemsByIds(admin.getRoleIdsList()));
		}
		return adminUser;
	}

}
