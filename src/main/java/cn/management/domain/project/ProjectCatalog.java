package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 网盘目录/文件实体类
 */
@Table(name = "project_catalog")
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
     * 文件大小,单位KB
     */
    private Integer fileSize;

    /**
     * 下载路径,目录为空
     */
    private String filePath;

    /**
     * 父目录id
     */
    private Integer parentId;

    /**
     * 项目组id,用json字符串表示
     */
    @Transient
    private String groupIds;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 创建人姓名
     */
    @Transient
    private String createByName;

    /**
     * 报给文件
     */
    @Transient
    private MultipartFile[] files;

    /**
     * 文件目录项目组关联信息
     */
    @Transient
    private List<ProjectCatalogGroup> catalogGroups;

    public Integer getFileSize() {
        return fileSize;
    }

    public String getCreateByName() {
        return createByName;
    }

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

    public Integer getCreateBy() {
        return createBy;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public List<ProjectCatalogGroup> getCatalogGroups() {
        return catalogGroups;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
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

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public void setCatalogGroups(List<ProjectCatalogGroup> catalogGroups) {
        this.catalogGroups = catalogGroups;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    @Override
    public String toString() {
        return "ProjectCatalog{" +
                "name='" + name + '\'' +
                ", fileType=" + fileType +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", parentId=" + parentId +
                ", groupIds='" + groupIds + '\'' +
                ", createBy=" + createBy +
                ", createByName='" + createByName + '\'' +
                ", files=" + files +
                ", catalogGroups=" + catalogGroups +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
