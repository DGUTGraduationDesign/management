package cn.management.domain.business;

import cn.management.domain.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;

/**
 * 报告数据模型
 */
@Table(name="business_report")
public class BusinessReport extends BaseEntity<Integer> {

    /**
     * 报给名称
     */
    private String reportName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

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
     * 报给文件
     */
    @Transient
    private MultipartFile file;

    public String getReportName() {
        return reportName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "BusinessReport{" +
                "reportName='" + reportName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", file='" + file + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
