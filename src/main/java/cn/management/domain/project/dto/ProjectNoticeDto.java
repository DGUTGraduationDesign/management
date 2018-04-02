package cn.management.domain.project.dto;

import cn.management.domain.BaseEntity;
import cn.management.domain.project.ProjectNoticeInform;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 条件查询通知Dto
 */
public class ProjectNoticeDto {

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 知会人id
     */
    private Integer userId;

    /**
     * 是否已读，默认为0，0表示未读，1表示已读
     */
    private Integer readFlag;

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    @Override
    public String toString() {
        return "ProjectNoticeDto{" +
                "createBy=" + createBy +
                ", userId=" + userId +
                ", readFlag=" + readFlag +
                '}';
    }

}
