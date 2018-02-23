package cn.management.service;

import cn.management.domain.admin.AdminUser;
import cn.management.mapper.admin.AdminUserMapper;

/**
 * 用户Service层接口
 * @author ZhouJiaKai
 */
public interface AdminUserService extends BaseService<AdminUserMapper, AdminUser> {
    
    /**
     * 用户登录 session key
     */
    String LOGIN_SESSION_KEY = "admin_user_session";
    
    /**
     * 用户登录
     * @param loginName
     * @return
     */
    AdminUser login(String loginName);
    
    /**
     * 获取用户信息以及他对应的角色
     * @param user
     * @return
     */
    AdminUser getUserAndRoles(AdminUser user);

    /**
     * 添加员工信息
     * @param adminUser
     * @return
     */
    AdminUser doAdd(AdminUser adminUser);

    /**
     * 更改员工信息
     * @param adminUser
     * @return
     */
    boolean doUpdate(AdminUser adminUser);
    
}
