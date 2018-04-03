package cn.management.service.business;

import cn.management.domain.business.BusinessContract;
import cn.management.mapper.business.BusinessContractMapper;
import cn.management.service.BaseService;

/**
 * 合同Service层接口
 */
public interface BusinessContractService extends BaseService<BusinessContractMapper, BusinessContract> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
