package cn.management.service.admin.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.admin.AdminRole;
import cn.management.mapper.admin.AdminRoleMapper;
import cn.management.service.admin.AdminRoleService;
import cn.management.service.impl.BaseServiceImpl;

/**
 * 角色Service层接口实现类
 * @author Admin
 */
@Service
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleMapper, AdminRole>
		implements AdminRoleService {
	
    /**
     * 删除角色
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean doDelete(String ids) {
		boolean flag = true;
		String[] idstr = ids.split(",");
		for (String id : idstr) {
			if (!deleteById(Integer.valueOf(id))) {
				flag = false;
			}
		}
		return flag;
	}
	
}
