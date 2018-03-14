package cn.management.domain.project;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 通知数据模型
 */
@Table(name = "protject_notice")
public class ProtjectNotice extends BaseEntity<Integer> {

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

    public String getContent() {
        return content;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
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
        return "ProtjectNotice{" +
                "content='" + content + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
