package cn.management.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import cn.management.domain.admin.AdminUser;
import cn.management.mapper.admin.AdminUserMapper;
import cn.management.service.AdminUserService;
import cn.management.util.MD5Util;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 用户Service层实现类
 * @author ZhouJiaKai
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

	@Override
	public AdminUser doAdd(AdminUser adminUser) {
		String password = adminUser.getPassword();
		//MD5加密
		adminUser.setPassword(MD5Util.getMD5Value(password));
		//设置添加时间
		adminUser.setCreateTime(new Date());
		return addSelectiveMapper(adminUser);
	}

	@Override
	public boolean doUpdate(AdminUser adminUser) {
		//判定是否修改密码
		if (StringUtil.isNotEmpty(adminUser.getPassword())) {
			adminUser.setPassword(MD5Util.getMD5Value(adminUser.getPassword()));
		} else {
			adminUser.setPassword(null);
		}
		//设置修改时间
		adminUser.setUpdateTime(new Date());
		return update(adminUser);
	}

}
