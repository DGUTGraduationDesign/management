package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

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
     * 任务总状态，0表示未完成，1表示已完成，2表示延期完成，3表示已取消
     */
    private Integer taskState;

    /**
     * 任务总状态名称
     */
    @Transient
    private String taskStateName;

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
     * 任务对象id,json字符串
     */
    @Transient
    private String userIds;

    /**
     * 通知方式
     */
    @Transient
    private Integer informWay;

    /**
     * 任务对象集合
     */
    @Transient
    private List<ProjectTaskUser> userList;

    public Integer getTaskState() {
        return taskState;
    }

    public String getTaskStateName() {
        return taskStateName;
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

    public Integer getInformWay() {
        return informWay;
    }

    public String getUserIds() {
        return userIds;
    }

    public List<ProjectTaskUser> getUserList() {
        return userList;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    public void setTaskStateName(String taskStateName) {
        this.taskStateName = taskStateName;
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

    public void setInformWay(Integer informWay) {
        this.informWay = informWay;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public void setUserList(List<ProjectTaskUser> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "taskName='" + taskName + '\'' +
                ", content='" + content + '\'' +
                ", beginDate=" + beginDate +
                ", closingDate=" + closingDate +
                ", taskState=" + taskState +
                ", taskStateName='" + taskStateName + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", userIds='" + userIds + '\'' +
                ", informWay=" + informWay +
                ", userList=" + userList +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
