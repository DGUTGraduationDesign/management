package cn.management.service.admin;

import cn.management.domain.admin.AdminUser;
import cn.management.exception.SysException;
import cn.management.mapper.admin.AdminUserMapper;
import cn.management.service.BaseService;

import java.util.List;

/**
 * 用户Service层接口
 * @author ZhouJiaKai
 */
public interface AdminUserService extends BaseService<AdminUserMapper, AdminUser> {
    
    /**
     * 用户登录 session key
     */
    String LOGIN_SESSION_KEY = "admin_user_id_session";

    String LOGIN_USER_SESSION_KEY = "admin_user_session";
    
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
     * 查询所有员工信息
     * @return
     */
    List<AdminUser> getAllUsers();

    /**
     * 条件查询员工信息
     * @param condition
     * @return
     */
    public List<AdminUser> getByCondition(String condition);

    /**
     * 添加员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
    AdminUser doAdd(AdminUser adminUser) throws SysException;

    /**
     * 更改员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
    boolean doUpdate(AdminUser adminUser) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
