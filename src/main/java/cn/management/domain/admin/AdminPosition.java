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
    private String postName;
    
    /** 
     * 所属部门id
     */
    private Integer deptId;
    
    /** 
     * 部门名称
     */
    @Transient
    private String deptName;
    
    /** 
     * 上级职位id
     */
    private Integer mgrId;

    /** 
     * 上级职位名称
     */
    @Transient
    private String mgrName;
    
    public String getPostName() {
        return postName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

	public Integer getMgrId() {
		return mgrId;
	}

	public String getMgrName() {
		return mgrName;
	}

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

	public void setMgrId(Integer mgrId) {
		this.mgrId = mgrId;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	@Override
	public String toString() {
		return "AdminPosition [postName=" + postName + ", deptId=" + deptId + ", deptName=" + deptName + ", mgrId="
				+ mgrId + ", mgrName=" + mgrName + ", id=" + id + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", delFlag=" + delFlag + "]";
	}

}
