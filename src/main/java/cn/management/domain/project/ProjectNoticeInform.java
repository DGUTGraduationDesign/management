package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 通知知会表数据模型
 */
@Table(name = "project_notice_inform")
public class ProjectNoticeInform extends BaseEntity<Integer> {

    /**
     * 通知id
     */
    private Integer noticeId;

    /**
     * 知会人id
     */
    private Integer userId;

    /**
     * 知会人姓名
     */
    @Transient
    private String userName;

    /**
     * 是否已读，默认为0，0表示未读，1表示已读
     */
    private Integer readFlag;

    public Integer getNoticeId() {
        return noticeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    @Override
    public String toString() {
        return "ProtjectNoticeInform{" +
                "noticeId=" + noticeId +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", readFlag=" + readFlag +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
