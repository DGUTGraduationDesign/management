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
    private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString() {
		return "AdminDepartment [deptName=" + deptName + ", id=" + id + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", delFlag=" + delFlag + "]";
	}

}
