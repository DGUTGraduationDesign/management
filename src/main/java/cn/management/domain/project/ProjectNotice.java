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
     * 知会人
     */
    @Transient
    private List<ProjectNoticeInform> informList;

    public String getContent() {
        return content;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public List<ProjectNoticeInform> getInformList() { return informList; }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setInformList(List<ProjectNoticeInform> informList) { this.informList = informList; }

    @Override
    public String toString() {
        return "ProjectNotice{" +
                "content='" + content + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", informList=" + informList +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
