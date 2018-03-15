package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 任务数据模型
 */
@Table(name = "project_task")
public class ProjectTask extends BaseEntity<Integer> {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 任务状态，0表示未完成，1表示已完成，2表示延期完成，3表示已取消
     */
    private Integer taskState;

    /**
     * 任务状态名称
     */
    @Transient
    private String taskStateName;

    /**
     * 任务开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 任务截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date closingDate;

    /**
     * 任务完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completeDate;

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
     * 任务对象id
     */
    private Integer userId;

    /**
     * 任务对象姓名
     */
    @Transient
    private String userName;

    /**
     * 通知方式
     */
    @Transient
    private Integer informWay;

    public String getTaskName() {
        return taskName;
    }

    public String getContent() {
        return content;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public String getTaskStateName() {
        return taskStateName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public Date getCompleteDate() {
        return completeDate;
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

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getInformWay() {
        return informWay;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    public void setTaskStateName(String taskStateName) {
        this.taskStateName = taskStateName;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setInformWay(Integer informWay) {
        this.informWay = informWay;
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "taskName='" + taskName + '\'' +
                ", content='" + content + '\'' +
                ", taskState=" + taskState +
                ", taskStateName='" + taskStateName + '\'' +
                ", beginDate=" + beginDate +
                ", closingDate=" + closingDate +
                ", completeDate=" + completeDate +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", informWay=" + informWay +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
