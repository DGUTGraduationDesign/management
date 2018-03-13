package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 项目数据模型
 */
@Table(name = "project_item")
public class ProjectItem extends BaseEntity<Integer> {

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目状态，默认为0，0表示未完成，1表示已完成
     */
    private String itemState;

    /**
     * 项目状态名称
     */
    @Transient
    private String itemStateName;

    /**
     * 项目任务总数
     */
    private Integer itemTask;

    /**
     * 已完成任务数
     */
    private Integer completeTask;

    /**
     * 负责人id
     */
    private Integer mainId;

    /**
     * 负责人姓名
     */
    @Transient
    private String mainName;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 创建人姓名
     */
    @Transient
    private String createName;

    public String getItemName() {
        return itemName;
    }

    public String getItemState() {
        return itemState;
    }

    public String getItemStateName() {
        return itemStateName;
    }

    public Integer getItemTask() {
        return itemTask;
    }

    public Integer getCompleteTask() {
        return completeTask;
    }

    public Integer getMainId() {
        return mainId;
    }

    public String getMainName() {
        return mainName;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemState(String itemState) {
        this.itemState = itemState;
    }

    public void setItemStateName(String itemStateName) {
        this.itemStateName = itemStateName;
    }

    public void setItemTask(Integer itemTask) {
        this.itemTask = itemTask;
    }

    public void setCompleteTask(Integer completeTask) {
        this.completeTask = completeTask;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public String toString() {
        return "ProtjectItem{" +
                "itemName='" + itemName + '\'' +
                ", itemState='" + itemState + '\'' +
                ", itemStateName='" + itemStateName + '\'' +
                ", itemTask=" + itemTask +
                ", completeTask=" + completeTask +
                ", mainId=" + mainId +
                ", mainName='" + mainName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }
}
