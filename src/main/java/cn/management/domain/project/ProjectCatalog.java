package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Transient;

/**
 * 网盘目录实体类
 */
public class ProjectCatalog extends BaseEntity<Integer> {

    /**
     * 目录名称
     */
    private String catalogName;

    /**
     * 父目录id
     */
    private Integer parentId;

    /**
     * 项目组id
     */
    private Integer groupId;

    /**
     * 项目组名称
     */
    @Transient
    private String groupName;

    /**
     * 创建人id
     */
    private Integer createBy;

    public String getCatalogName() {
        return catalogName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "ProjectCatalog{" +
                "catalogName='" + catalogName + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", createBy=" + createBy +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
