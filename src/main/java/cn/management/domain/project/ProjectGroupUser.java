package cn.management.domain.project;

import cn.management.domain.BaseEntity;

/**
 * 分组员工关联实体类
 */
public class ProjectGroupUser extends BaseEntity<Integer> {

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 员工id
     */
    private Integer userId;

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProjectGroupUser{" +
                "groupId=" + groupId +
                ", userId=" + userId +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
