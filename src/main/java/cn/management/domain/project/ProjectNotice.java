package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 通知数据模型
 */
@Table(name = "project_notice")
public class ProjectNotice extends BaseEntity<Integer> {

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

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
     * 知会人id,json字符串
     */
    @Transient
    private String userIds;

    /**
     * 知会人
     */
    @Transient
    private List<ProjectNoticeInform> informList;

    /**
     * 是否已读
     */
    @Transient
    private String readFlagName;

    /**
     * 通知方式
     * 0 表示 "无"
     * 1 表示 "邮件通知"
     * 2 表示 "短信通知"
     * 3 表示 "邮件、短信通知"
     */
    @Transient
    private Integer informWay;

    public Integer getInformWay() {
        return informWay;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public List<ProjectNoticeInform> getInformList() { return informList; }

    public String getReadFlagName() {
        return readFlagName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public void setInformList(List<ProjectNoticeInform> informList) { this.informList = informList; }

    public void setReadFlagName(String readFlagName) {
        this.readFlagName = readFlagName;
    }

    public void setInformWay(Integer informWay) {
        this.informWay = informWay;
    }

    @Override
    public String toString() {
        return "ProjectNotice{" +
                "title='" + title + '\'' +
                "content='" + content + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", userIds=" + userIds +
                ", informList=" + informList +
                ", readFlagName='" + readFlagName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
