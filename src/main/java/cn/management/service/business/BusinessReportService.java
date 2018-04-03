package cn.management.service.business;

import cn.management.domain.business.BusinessReport;
import cn.management.mapper.business.BusinessReportMapper;
import cn.management.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * 报告Service层接口
 */
public interface BusinessReportService extends BaseService<BusinessReportMapper, BusinessReport> {

    /**
     * 逻辑删除，更新表中del_flag字段为1，并删除文件
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}
