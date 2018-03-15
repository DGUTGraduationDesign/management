package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 通知知会表数据模型
 */
@Table(name = "project_notice_inform")
public class ProjectNoticeInform extends BaseEntity<Integer> {

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

    /**
     * 是否已读
     */
    @Transient
    private String readFlagName;

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public String getReadFlagName() {
        return readFlagName;
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

    public void setReadFlagName(String readFlagName) {
        this.readFlagName = readFlagName;
    }

    @Override
    public String toString() {
        return "ProtjectNoticeInform{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", readFlag=" + readFlag +
                ", readFlagName='" + readFlagName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
