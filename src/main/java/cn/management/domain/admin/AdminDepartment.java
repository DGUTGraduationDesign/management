package cn.management.domain.admin;

import java.io.Serializable;

import javax.persistence.Table;

import cn.management.domain.BaseEntity;

/**
 * 部门表实体类
 */
@Table(name = "admin_department")
public class AdminDepartment extends BaseEntity<Integer> implements Serializable {
    
    /** 
     * 部门名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AdminDepartment [name=" + name + ", id=" + id + ", createTime=" + createTime + ", updateTime="
                + updateTime + ", delFlag=" + delFlag + "]";
    }

}
