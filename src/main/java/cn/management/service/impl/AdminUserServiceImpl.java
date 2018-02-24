package cn.management.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.admin.AdminUser;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.admin.AdminUserMapper;
import cn.management.service.AdminUserService;
import cn.management.util.MD5Util;
import tk.mybatis.mapper.entity.Example;
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
