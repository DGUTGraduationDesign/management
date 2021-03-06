package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

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
    private Integer itemState;

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
     * 按时完成任务数
     */
    private Integer completeTask;

    /**
     * 逾期完成任务数
     */
    private Integer delayTask;

    /**
     * 取消任务数
     */
    private Integer cancelTask;

    /**
     * 项目开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date beginDate;

    /**
     * 项目完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date endDate;

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

    public Integer getItemState() {
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

    public Integer getDelayTask() {
        return delayTask;
    }

    public Integer getCancelTask() {
        return cancelTask;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public void setItemState(Integer itemState) {
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

    public void setDelayTask(Integer delayask) {
        this.delayTask = delayask;
    }

    public void setCancelTask(Integer cancelask) {
        this.cancelTask = cancelask;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        return "ProjectItem{" +
                "itemName='" + itemName + '\'' +
                ", itemState=" + itemState +
                ", itemStateName='" + itemStateName + '\'' +
                ", itemTask=" + itemTask +
                ", completeTask=" + completeTask +
                ", delayTask=" + delayTask +
                ", cancelTask=" + cancelTask +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
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
