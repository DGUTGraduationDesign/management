package cn.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;

import cn.management.domain.BaseEntity;
import cn.management.enums.ResultEnum;
import cn.management.service.BaseService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

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
        //分页信息
        String pageMenu = getPagination(list, page);
        return new Result(ResultEnum.SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageNum(), getPageSize(), pageMenu);
    }

    /**
     * 打印分页
     * @param list
     * @param page
     * @return
     */
    protected String getPagination(List list, int page) {
        PageInfo pageInfo =  new PageInfo<>(list);
        if ( pageInfo.getTotal() > 0 ) {
            //获取列表的页码
            int[] pageNums = pageInfo.getNavigatepageNums();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<li class=\"am-disabled\"><a href=\"javascript:void(0);\">总计:" + pageInfo.getTotal() + "</a></li>");
            if (pageInfo.isIsFirstPage()) {
                stringBuilder.append("<li><a href=\"javascript:void(0);\">&laquo;</a></li>");
            } else {
                stringBuilder.append("<li class=\"active\"><a href=\"javascript:void(0);\">&laquo;</a></li>");
            }

            for(int i = 0; i < pageNums.length; i++) {
                if (page == pageNums[i]) {
                    stringBuilder.append("<li class=\"active\"><a href=\"javascript:void(0);\">" + page + "</a></li>");
                } else {
                    stringBuilder.append("<li><a href=\"javascript:void(0);\">" + pageNums[i] + "</a></li>");
                }
            }
            if (pageInfo.isIsLastPage()) {
                stringBuilder.append("<li><a href=\"javascript:void(0);\">&raquo;</a></li>");
            } else {
                stringBuilder.append("<li class=\"active\"><a href=\"javascript:void(0);\">&raquo;</a></li>");
            }
            return stringBuilder.toString();
        }
        return null;
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
