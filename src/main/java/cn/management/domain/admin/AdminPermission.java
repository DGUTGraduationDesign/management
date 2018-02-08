package cn.management.domain.admin;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.management.domain.BaseEntity;

/**
 * 权限表实体类
 */
@Table(name = "admin_permissions")
public class AdminPermission extends BaseEntity<Integer> {

    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限标志
     */
    @Column(name = "pkey", unique = true)
    private String key;

    /**
     * 模块(分组)
     */
    @Column(name = "pgroup")
    private String group;

    /**
     * 分组名称
     */
    @Transient
    private String groupName;

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getGroup() {
		return group;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "AdminPermission [name=" + name + ", key=" + key + ", group=" + group + ", groupName=" + groupName
				+ ", id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag
				+ "]";
	}

}