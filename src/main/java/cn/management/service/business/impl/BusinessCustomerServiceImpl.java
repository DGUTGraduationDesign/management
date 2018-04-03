package cn.management.service.business.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.business.BusinessCustomer;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.business.BusinessCustomerMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.business.BusinessCustomerService;
import cn.management.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 客户信息Service层实现类
 */
@Service
public class BusinessCustomerServiceImpl extends BaseServiceImpl<BusinessCustomerMapper, BusinessCustomer> implements BusinessCustomerService {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 条件查询客户信息列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<BusinessCustomer> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<BusinessCustomer> list = mapper.selectByExample(example);
        //设置创建人姓名、对接人姓名
        for (BusinessCustomer businessCustomer : list) {
            AdminUser createUser = adminUserService.getItemById(businessCustomer.getCreateBy());
            if (null != createUser) {
                businessCustomer.setCreateName(createUser.getRealName());
            }
            AdminUser user = adminUserService.getItemById(businessCustomer.getUserId());
            if (null != user) {
                businessCustomer.setUserName(user.getRealName());
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
        Example example = new Example(BusinessCustomer.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        BusinessCustomer businessCustomer = new BusinessCustomer();
        businessCustomer.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(businessCustomer, example);
    }

}
