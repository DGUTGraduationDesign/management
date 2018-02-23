package cn.management.controller;

import org.springframework.beans.factory.annotation.Autowired;

import cn.management.domain.BaseEntity;
import cn.management.service.BaseService;
import tk.mybatis.mapper.entity.Example;

/**
 * 通用Controller
 */
public class BaseController<SERVICE extends BaseService, M extends BaseEntity> {

    @Autowired
    protected SERVICE service;
    
    protected int pageSize = 10;
    
    /**
     * 添加查询
     */
    private Example example;

    public Example getExample() {
        return example;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setExample(Example example) {
        this.example = example;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
}
