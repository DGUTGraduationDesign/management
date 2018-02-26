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
import cn.management.mapper.admin.AdminPositionMapper;
import cn.management.service.admin.AdminDepartmentService;
import cn.management.service.admin.AdminPositionService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 职位Service层接口实现类
 * @author Admin
 */
@Service
public class AdminPositionServiceImpl extends BaseServiceImpl<AdminPositionMapper, AdminPosition>
		implements AdminPositionService {

	@Autowired
	private AdminDepartmentService adminDepartmentService;
	
	/**
	 * 条件查询职位列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<AdminPosition> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AdminPosition> list = mapper.selectByExample(example);
        for (AdminPosition adminPosition : list) {
        	//设置部门名
        	setName(adminPosition);
        	if (adminPosition.getMgrId() != null) {
        		AdminPosition mgrPosition = getItemById(adminPosition.getMgrId());
        		if (mgrPosition != null) {
        			adminPosition.setMgrName(mgrPosition.getPostName());
        		}
        	}
        }
        return list;
    }
	
	private void setName(AdminPosition adminPosition) {
		AdminDepartment adminDepartment = adminDepartmentService.getItemById(adminPosition.getDeptId());
		if (adminDepartment != null) {
			adminPosition.setDeptName(adminDepartment.getDeptName());
		}
	}

	@Override
	@Transactional
	public AdminPosition doAdd(AdminPosition adminPosition) throws SysException {
		//判断所选部门是否存在
		Integer deptId = adminPosition.getDeptId();
		AdminDepartment condition = new AdminDepartment();
		condition.setId(deptId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		if (deptId == null || adminDepartmentService.getItem(condition) == null) {
			throw new SysException("所选职位不存在！");
		}
		//判断所选上级职位是否存在
		Integer postId = adminPosition.getMgrId();
		AdminPosition positionCondition = new AdminPosition();
		positionCondition.setId(postId);
		positionCondition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		if (postId == null || getItem(positionCondition) == null) {
			throw new SysException("所选上级职位不存在！");
		}
		adminPosition.setCreateTime(new Date());
		adminPosition.setUpdateTime(new Date());
		return addSelectiveMapper(adminPosition);
	}

	@Override
	@Transactional
	public boolean doUpdate(AdminPosition adminPosition) throws SysException {
		//判断所选部门是否存在
		Integer deptId = adminPosition.getDeptId();
		AdminDepartment condition = new AdminDepartment();
		condition.setId(deptId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		if (adminDepartmentService.getItem(condition) == null) {
			throw new SysException("所选职位不存在！");
		}
		adminPosition.setUpdateTime(new Date());
		return update(adminPosition);
	}
	
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
        Example example = new Example(AdminPosition.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        AdminPosition adminPosition = new AdminPosition();
        adminPosition.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(adminPosition, example);
	}

}
