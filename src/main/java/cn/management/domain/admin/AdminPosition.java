package cn.management.domain.admin;

import java.io.Serializable;

import javax.persistence.Table;
import javax.persistence.Transient;

import cn.management.domain.BaseEntity;

/**
 * 职位表实体
 */
@Table(name = "admin_position")
public class AdminPosition extends BaseEntity<Integer>  implements Serializable {
    
    /** 
     * 职位名称
     */
    private String name;
    
    /** 
     * 所属部门id
     */
    private Integer deptId;
    
    /** 
     * 部门名称
     */
    @Transient
    private String deptName;

    public String getName() {
        return name;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "AdminPosition [name=" + name + ", deptId=" + deptId + ", deptName=" + deptName + ", id=" + id
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
    }
    
}
