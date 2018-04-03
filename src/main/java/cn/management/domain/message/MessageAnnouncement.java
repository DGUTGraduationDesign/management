package cn.management.domain.message;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 企业公告实数据模型
 */
@Table(name="message_announcement")
public class MessageAnnouncement extends BaseEntity<Integer> {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
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

    public String getCreateName() {
        return createName;
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

    @Override
    public String toString() {
        return "MessageAnnouncement{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createBy=" + createBy +
                ", createName=" + createName +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
