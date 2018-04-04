package cn.management.domain.admin.dto;

import cn.management.domain.admin.AdminUser;
import org.springframework.beans.BeanUtils;

/**
 * 修改个人信息dto
 */
public class EditProfileDto {

    private String realName;

    private String password;

    private String sex;

    private String phone;

    private String mail;

    public String getRealName() {
        return realName;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    //Dto to Entity
    public static void dtoToEntity(EditProfileDto dto, AdminUser entity) {
        BeanUtils.copyProperties(dto, entity);
    }

    @Override
    public String toString() {
        return "EditProfileDto{" +
                "realName='" + realName + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
