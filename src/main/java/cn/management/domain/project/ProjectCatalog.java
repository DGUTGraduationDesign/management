package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;

/**
 * 网盘目录/文件实体类
 */
public class ProjectCatalog extends BaseEntity<Integer> {

    /**
     * 目录名称
     */
    private String name;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 下载路径,目录为空
     */
    private String filePath;

    /**
     * 父目录id
     */
    private Integer parentId;

    /**
     * 项目组id
     */
    private Integer groupId;

    /**
     * 项目组名称
     */
    @Transient
    private String groupName;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 报给文件
     */
    @Transient
    private MultipartFile file;

    public String getName() {
        return name;
    }

    public Integer getFileType() {
        return fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ProjectCatalog{" +
                "name='" + name + '\'' +
                ", fileType=" + fileType +
                ", filePath='" + filePath + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", createBy=" + createBy +
                ", file=" + file +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
