package cn.management.service.admin.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminPermission;
import cn.management.enums.PermissionGroupEnum;
import cn.management.mapper.admin.AdminPermissionMapper;
import cn.management.service.admin.AdminPermissionService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 权限Service层接口实现类
 * @author Admin
 */
@Service
public class AdminPermissionServiceImpl extends BaseServiceImpl<AdminPermissionMapper, AdminPermission>
		implements AdminPermissionService {

	/**
	 * 条件查询权限列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<AdminPermission> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminPermission> list = mapper.selectByExample(example);
        //设置分组名
        setName(list);
        return list;
    }
	
	/**
	 * 设置分组名
	 * @param list
	 */
    private void setName(List<AdminPermission> list) {
        for (AdminPermission adminPermission : list) {
        	adminPermission.setGroupName(PermissionGroupEnum.getName(adminPermission.getGroup()));
        }
	}

	/**
     * 删除权限
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
