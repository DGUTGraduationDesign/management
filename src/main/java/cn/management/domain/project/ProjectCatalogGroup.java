package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Transient;

public class ProjectCatalogGroup extends BaseEntity<Integer> {

    /**
     * 文件目录id
     */
    private Integer catalogId;

    /**
     * 项目组id
     */
    private Integer groupId;

    /**
     * 项目组中文
     */
    @Transient
    private String groupName;

    public Integer getCatalogId() {
        return catalogId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "ProjectCatalogGroup{" +
                "catalogId=" + catalogId +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
