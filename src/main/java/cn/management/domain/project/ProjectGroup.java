package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import cn.management.domain.admin.AdminUser;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 项目组数据模型
 */
@Table(name = "project_group")
public class ProjectGroup extends BaseEntity<Integer> {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 小组负责人id
     */
    private Integer headId;

    /**
     * 小组负责人姓名
     */
    @Transient
    private String headName;

    /**
     * 所属项目id
     */
    private Integer itemId;

    /**
     * 项目名称
     */
    @Transient
    private String itemName;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 创建人姓名
     */
    @Transient
    private String createName;

    /**
     * 项目组成员ID集合，使用json存储
     */
    private String userIds;

    /**
     * 项目组成员
     */
    @Transient
    private List<AdminUser> users;

    public String getGroupName() {
        return groupName;
    }

    public Integer getHeadId() {
        return headId;
    }

    public String getHeadName() {
        return headName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public String getUserIds() {
        return userIds;
    }

    public List<AdminUser> getUsers() {
        return users;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCreatBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public void setUsers(List<AdminUser> users) {
        this.users = users;
    }

    public void setCreateBy(Integer createBy) { this.createBy = createBy; }

    @Override
    public String toString() {
        return "ProtjectGroup{" +
                "groupName='" + groupName + '\'' +
                ", headId=" + headId +
                ", headName='" + headName + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", userIds='" + userIds + '\'' +
                ", users=" + users +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
