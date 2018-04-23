package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 任务对象表数据模型
 */
@Table(name = "project_task_user")
public class ProjectTaskUser extends BaseEntity<Integer> {

    /**
     * 任务id
     */
    private Integer taskId;

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
     * 任务状态，0表示未完成，1表示已完成，2表示延期完成，3表示已取消
     */
    private Integer taskState;

    /**
     * 任务状态名称
     */
    @Transient
    private String taskStateName;

    /**
     * 任务完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completeDate;

    public Integer getTaskId() {
        return taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
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

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "ProjectTaskUser{" +
                "taskId=" + taskId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", taskState=" + taskState +
                ", taskStateName='" + taskStateName + '\'' +
                ", completeDate=" + completeDate +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
