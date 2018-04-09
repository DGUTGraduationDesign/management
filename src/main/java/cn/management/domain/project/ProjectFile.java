package cn.management.domain.project;

import cn.management.domain.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;

/**
 * 文件资源实体类
 */
public class ProjectFile extends BaseEntity<Integer> {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件类型名称
     */
    @Transient
    private String fileTypeName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 所属目录id
     */
    private Integer catalogId;

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
    private MultipartFile file;

    public String getFileName() {
        return fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getCatalogId() {
        return catalogId;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ProjectFile{" +
                "fileName='" + fileName + '\'' +
                ", fileType=" + fileType +
                ", fileTypeName='" + fileTypeName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", catalogId=" + catalogId +
                ", createBy=" + createBy +
                ", createByName='" + createByName + '\'' +
                ", file=" + file +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
