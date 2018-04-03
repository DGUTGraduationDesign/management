package cn.management.domain.business;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 合同数据模型
 */
@Table(name="business_contract")
public class BusinessContract extends BaseEntity<Integer> {

    /**
     * 合同名称
     */
    private String contractName;

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
     * 对应客户id
     */
    private Integer customerId;

    /**
     * 对应客户姓名
     */
    @Transient
    private String customerName;

    public String getContractName() {
        return contractName;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "BusinessContract{" +
                "contractName='" + contractName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
