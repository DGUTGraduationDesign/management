package cn.management.service.business.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.business.BusinessContract;
import cn.management.domain.business.BusinessCustomer;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.business.BusinessContractMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.business.BusinessContractService;
import cn.management.service.business.BusinessCustomerService;
import cn.management.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 合同Service层实现类
 */
@Service
public class BusinessContractServiceImpl extends BaseServiceImpl<BusinessContractMapper, BusinessContract> implements BusinessContractService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private BusinessCustomerService businessCustomerService;

    /**
     * 条件查询合同列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<BusinessContract> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<BusinessContract> list = mapper.selectByExample(example);
        //设置创建人姓名、客户姓名
        for (BusinessContract businessContract : list) {
            AdminUser createUser = adminUserService.getItemById(businessContract.getCreateBy());
            if (null != createUser) {
                businessContract.setCreateName(createUser.getRealName());
            }
            BusinessCustomer businessCustomer = businessCustomerService.getItemById(businessContract.getCustomerId());
            if (null != businessContract) {
                businessContract.setCustomerName(businessCustomer.getName());
            }
        }
        return list;
    }

    @Override
    public Object doAdd(BusinessContract businessContract) throws SysException {
        BusinessCustomer businessCustomer = businessCustomerService.getItemById(businessContract.getCustomerId());
        if (null == businessCustomer) {
            throw new SysException("所选客户不存在.");
        }
        businessContract.setCreateTime(new Date());
        businessContract.setUpdateTime(new Date());
        return addSelectiveMapper(businessContract);
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(BusinessContract.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        BusinessContract businessContract = new BusinessContract();
        businessContract.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(businessContract, example);
    }

}
