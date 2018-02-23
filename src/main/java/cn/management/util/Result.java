package cn.management.util;

import cn.management.enums.ResultEnum;

/**
 * 用于封装返回结果
 * @author ZhouJiaKai
 * @date 2018-02-23
 */
public class Result {

    /**
     * 返回状态码
     * 1000:成功
     * 1001:数据格式错误
     * 1002:常规操作失败：新增、编辑、删除失败等
     * 1003：用户未登录
     * 1004：系统异常
     * 1005：无符合数据
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    /**
     * 总记录数
     */
    private Integer count=0;

    /**
     * 页码
     */
    private Integer page = 0;

    /**
     * 每页记录数
     */
    private Integer pageSize = 0;
    
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public Result(ResultEnum resultEnum, Object data, Integer count, Integer page, Integer pageSize) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
        this.count = count;
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
