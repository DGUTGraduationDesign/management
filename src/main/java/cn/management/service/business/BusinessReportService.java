package cn.management.service.business;

import cn.management.domain.business.BusinessReport;
import cn.management.mapper.business.BusinessReportMapper;
import cn.management.service.BaseService;

/**
 * 报告Service层接口
 */
public interface BusinessReportService extends BaseService<BusinessReportMapper, BusinessReport> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
