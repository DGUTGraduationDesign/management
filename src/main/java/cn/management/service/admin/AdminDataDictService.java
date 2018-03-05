package cn.management.service.admin;

import cn.management.domain.admin.AdminDataDict;
import cn.management.mapper.admin.AdminDataDictMapper;
import cn.management.service.BaseService;

/**
 * 数据字典Service层接口
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
public interface AdminDataDictService extends BaseService<AdminDataDictMapper, AdminDataDict> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	boolean logicalDelete(String ids);

}
