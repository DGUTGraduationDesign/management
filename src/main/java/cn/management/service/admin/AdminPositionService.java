package cn.management.service.admin;

import cn.management.domain.admin.AdminPosition;
import cn.management.exception.SysException;
import cn.management.mapper.admin.AdminPositionMapper;
import cn.management.service.BaseService;

/**
 * 职位Service层接口
 * @author Admin
 */
public interface AdminPositionService extends BaseService<AdminPositionMapper, AdminPosition> {

	/**
	 * 添加职位
	 * @param adminPosition
	 * @return
	 * @throws SysException
	 */
	AdminPosition doAdd(AdminPosition adminPosition) throws SysException;

	/**
	 * 更改职位
	 * @param adminPosition
	 * @return
	 * @throws SysException
	 */
	boolean doUpdate(AdminPosition adminPosition) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
