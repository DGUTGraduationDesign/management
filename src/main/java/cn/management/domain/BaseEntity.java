package cn.management.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 通用数据模型
 */
public class BaseEntity<T> {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    /**
     * 添加时间
     */
    @Column(name="create_time") protected Date createTime;

    /**
     * 更新时间
     */
    @Column(name="update_time") protected Date updateTime;

    /**
     * 删除标识1-未删除0-已删除
     */
    @Column(name="del_flag") protected Integer delFlag;

    public T getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setId(T id) {
        this.id = id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
