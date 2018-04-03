package cn.management.service.business;

import cn.management.domain.business.BusinessCustomer;
import cn.management.mapper.business.BusinessCustomerMapper;
import cn.management.service.BaseService;

/**
 * 客户信息Service层接口
 */
public interface BusinessCustomerService extends BaseService<BusinessCustomerMapper, BusinessCustomer> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
