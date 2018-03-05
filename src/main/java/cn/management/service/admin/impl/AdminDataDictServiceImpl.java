package cn.management.service.admin.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.management.domain.admin.AdminDataDict;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.admin.AdminDataDictMapper;
import cn.management.service.admin.AdminDataDictService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 数据字典Service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
@Service
public class AdminDataDictServiceImpl extends BaseServiceImpl<AdminDataDictMapper, AdminDataDict>
		implements AdminDataDictService {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
		Example example = new Example(AdminDataDict.class);
		example.createCriteria().andCondition("id IN(" + ids + ")");
		AdminDataDict adminDataDict = new AdminDataDict();
		adminDataDict.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(adminDataDict, example);
	}

}
