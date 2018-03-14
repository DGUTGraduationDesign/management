package cn.management.service.admin.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminDepartment;
import cn.management.domain.admin.AdminPosition;
import cn.management.domain.admin.AdminUser;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.admin.AdminUserMapper;
import cn.management.service.admin.AdminDepartmentService;
import cn.management.service.admin.AdminPositionService;
import cn.management.service.admin.AdminRoleService;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.util.MD5Util;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 用户Service层实现类
 * @author ZhouJiaKai
 */
@Service
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

	@Autowired
	private AdminDepartmentService adminDepartmentService;

	@Autowired
	private AdminPositionService adminPositionService;
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	/**
	 * 条件查询员工列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<AdminUser> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminUser> list = mapper.selectByExample(example);
        for (AdminUser adminUser : list) {
        	//设置部门岗位名
        	setName(adminUser);
        	//设置直属上级姓名
        	if (adminUser.getLeaderId() != null) {
        		AdminUser leader = getItemById(adminUser.getLeaderId());
        		if (leader != null) {
            		adminUser.setLeaderName(leader.getRealName());
        		} 
        	}
        }
        return list;
    }

	/**
	 * 根据id查询员工信息
	 * @param id
	 * @return
	 */
	public AdminUser getItemById(Object id) {
		AdminUser adminUser = mapper.selectByPrimaryKey(id);
		//设置部门岗位名
		setName(adminUser);
		return adminUser;
	}

	/**
     * 用户登录
     * @param loginName
     * @return
     */
	@Override
	public AdminUser login(String loginName) {
		AdminUser findUser = new AdminUser();
		findUser.setLoginName(loginName);
		return getUserAndRoles(findUser);
	}

    /**
     * 获取用户信息以及他对应的角色
     * @param user
     * @return
     */
	@Override
	public AdminUser getUserAndRoles(AdminUser user) {
		AdminUser adminUser = this.getItem(user);
		if (adminUser != null) {
			//设置岗位职位中文名称
			setName(adminUser);
			adminUser.setRoleList(adminRoleService.getItemsByIds(adminUser.getRoleIdsList()));
		}
		return adminUser;
	}

	/**
	 * 设置部门岗位中文
	 * @param adminUser
	 */
	private void setName(AdminUser adminUser) {
		AdminDepartment department = adminDepartmentService.getItemById(adminUser.getDeptId());
		AdminPosition position = adminPositionService.getItemById(adminUser.getPostId());
		if (department != null) {
			adminUser.setDeptName(department.getDeptName());
		}
		if (position != null) {
			adminUser.setPostName(position.getPostName());
		}
	}

    /**
     * 添加员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
	@Override
	@Transactional
	public AdminUser doAdd(AdminUser adminUser) throws SysException {
		//校验用户名是否已存在
    	Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName", adminUser.getLoginName());
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        if (this.countByCondition(example) > 0) {
            throw new SysException("用户名已被注册！");
        }
		String password = adminUser.getPassword();
		//MD5加密
		adminUser.setPassword(MD5Util.getMD5Value(password));
		//设置添加时间
		adminUser.setCreateTime(new Date());
		return addSelectiveMapper(adminUser);
	}

    /**
     * 更改员工信息
     * @param adminUser
     * @return
     * @throws SysException 
     */
	@Override
	@Transactional
	public boolean doUpdate(AdminUser adminUser) throws SysException {
		//判断要编辑的数据是否存在
		AdminUser condition = new AdminUser();
		condition.setId(adminUser.getId());
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		AdminUser findUser = getItem(condition);
		if (findUser == null) {
			throw new SysException("所编辑数据不存在！");
		}
		//用户名不可更改
		adminUser.setLoginName(null);
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

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
        Example example = new Example(AdminUser.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
	    AdminUser adminUser = new AdminUser();
	    adminUser.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(adminUser, example);
	}
	
}
