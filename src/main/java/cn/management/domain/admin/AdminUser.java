package cn.management.domain.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;

import cn.management.domain.BaseEntity;

/**
 * 用户表实体类
 */
@Table(name = "admin_user")
public class AdminUser extends BaseEntity<Integer> implements Serializable {

    /**
     * 用户名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;
    
    /**
     * 用户真实姓名
     */
    private String realName;
    
    /**
     * 性别
     */
    private String sex;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 工号
     */
    private String number;
    
    /**
     * 邮箱
     */
    private String mail;

    /**
     * 直属上司id
     */
    private Integer leaderId;

    /**
     * 直属上司名称
     */
    @Transient
    private String leaderName;

    /**
     * 个性化签名图片路径
     */
    private String uploadPath;
    
    /**
     * 职位id
     */
    private Integer postId;

    /**
     * 职位名称
     */
    @Transient
    private String postName;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 部门名称
     */
    @Transient
    private String deptName;

    /**
     * 角色用户角色ID集合，json字符串
     */
    private String roleIds;
    
    /**
     * 角色集合
     */
    @Transient
    private List<AdminRole> roleList;
    
    /**
     * 以 List 格式返回角色ID
     * @return
     */
    public List<String> getRoleIdsList() {
        try {
        	return JSONObject.parseArray(this.getRoleIds(), String.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 设置角色ID
     * @param roleIds
     * @throws JsonProcessingException
     */
    public void setRoleIdsList(List<String> roleIds) throws JsonProcessingException {
        this.roleIds = JSON.toJSONString(roleIds);
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getNumber() {
        return number;
    }

    public String getMail() {
        return mail;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getPostName() {
        return postName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getRoleIds() {
        return roleIds;
    }

	public List<AdminRole> getRoleList() {
		return roleList;
	}

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

	public void setRoleList(List<AdminRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return "AdminUser [loginName=" + loginName + ", password=" + password + ", realName=" + realName + ", sex="
				+ sex + ", phone=" + phone + ", number=" + number + ", mail=" + mail + ", leaderId=" + leaderId
				+ ", leaderName=" + leaderName + ", uploadPath=" + uploadPath + ", postId=" + postId + ", postName="
				+ postName + ", deptId=" + deptId + ", deptName=" + deptName + ", roleIds=" + roleIds + ", id=" + id
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
	}

}
