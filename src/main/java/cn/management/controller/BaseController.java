package cn.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;

import cn.management.domain.BaseEntity;
import cn.management.enums.ResultEnum;
import cn.management.service.BaseService;
import cn.management.util.Result;
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
    
    /**
     * 获取数据列表
     * @param page
     * @return
     */
    protected Result list(Integer page) {
        if (null == getExample().getOrderByClause()) {
            getExample().setOrderByClause("id DESC");   //设置默认排序方式
        };
        List<M> list = service.getItemsByPage(getExample(), page, getPageSize());
        if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        }
        PageInfo<M> pageInfo = new PageInfo<M>(list);
        return new Result(ResultEnum.SUCCESS, pageInfo.getList(), pageInfo.getSize(), pageInfo.getPageNum(), getPageSize());
    }
    
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
