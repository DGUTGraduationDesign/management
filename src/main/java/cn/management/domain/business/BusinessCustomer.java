package cn.management.domain.business;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 客户信息数据模型
 */
@Table(name="business_customer")
public class BusinessCustomer extends BaseEntity<Integer> {

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 客户邮箱
     */
    private String mail;

    /**
     * 客户电话
     */
    private String phone;

    /**
     * 对接人id
     */
    private Integer userId;

    /**
     * 对接人姓名
     */
    @Transient
    private String userName;

    /**
     * 创建人id
     */
    private Integer createBy;

    /**
     * 创建人姓名
     */
    @Transient
    private String createName;

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public String toString() {
        return "BusinessCustomer{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", createBy=" + createBy +
                ", createName='" + createName + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }

}
