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
    private Integer item_id;

    /**
     * 项目名称
     */
    @Transient
    private String itemName;

    /**
     * 创建人id
     */
    private Integer creat_by;

    /**
     * 创建人姓名
     */
    @Transient
    private String createName;

    /**
     * 项目组成员ID集合，使用json存储
     */
    private String user_ids;

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

    public Integer getItem_id() {
        return item_id;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getCreat_by() {
        return creat_by;
    }

    public String getCreateName() {
        return createName;
    }

    public String getUser_ids() {
        return user_ids;
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

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCreat_by(Integer creat_by) {
        this.creat_by = creat_by;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setUser_ids(String user_ids) {
        this.user_ids = user_ids;
    }

    public void setUsers(List<AdminUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ProtjectGroup{" +
                "groupName='" + groupName + '\'' +
                ", headId=" + headId +
                ", headName='" + headName + '\'' +
                ", item_id=" + item_id +
                ", itemName='" + itemName + '\'' +
                ", creat_by=" + creat_by +
                ", createName='" + createName + '\'' +
                ", user_ids='" + user_ids + '\'' +
                ", users=" + users +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
