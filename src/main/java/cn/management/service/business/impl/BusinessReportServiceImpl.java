package cn.management.service.business.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.business.BusinessReport;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.business.BusinessReportMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.business.BusinessReportService;
import cn.management.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 报告Service层实现类
 */
@Service
public class BusinessReportServiceImpl extends BaseServiceImpl<BusinessReportMapper, BusinessReport> implements BusinessReportService {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 条件查询报告列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<BusinessReport> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<BusinessReport> list = mapper.selectByExample(example);
        //设置创建人姓名
        for (BusinessReport businessReport : list) {
            AdminUser createUser = adminUserService.getItemById(businessReport.getCreateBy());
            if (null != createUser) {
                businessReport.setCreateName(createUser.getRealName());
            }
        }
        return list;
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(BusinessReport.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        BusinessReport businessReport = new BusinessReport();
        businessReport.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(businessReport, example);
    }

}
