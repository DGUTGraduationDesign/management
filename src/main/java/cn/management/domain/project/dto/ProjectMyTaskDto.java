package cn.management.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ProjectMyTaskDto implements Serializable {

    /**
     * 任务id
     */
    private Integer id;

    /**
     * 任务对象id
     */
    private Integer userId;

    /**
     * 任务状态，0表示未完成，1表示已完成，2表示延期完成，3表示已取消
     */
    private Integer taskState;

    /**
     * 任务状态名称
     */
    private String taskStateName;

    /**
     * 任务完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completeDate;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 任务开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 任务截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date closingDate;

    /**
     * 所属项目id
     */
    private Integer itemId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 创建人姓名
     */
    private String createName;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public String getTaskStateName() {
        return taskStateName;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getContent() {
        return content;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getClosingDate() {
        return closingDate;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    public void setTaskStateName(String taskStateName) {
        this.taskStateName = taskStateName;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public String toString() {
        return "ProjectMyTaskDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", taskState=" + taskState +
                ", taskStateName='" + taskStateName + '\'' +
                ", completeDate=" + completeDate +
                ", taskName='" + taskName + '\'' +
                ", content='" + content + '\'' +
                ", beginDate=" + beginDate +
                ", closingDate=" + closingDate +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                '}';
    }

}
