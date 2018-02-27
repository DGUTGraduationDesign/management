package cn.management.domain.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;

import cn.management.domain.BaseEntity;

/**
 * 角色表实体类
 */
@Table(name = "admin_role")
public class AdminRole extends BaseEntity<Integer> implements Serializable {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限，json字符串
     */
    private String permissions;

    /**
     * 获取权限list
     * @return
     */
    public List<String> getPermissionList() {
        try {
            return JSONObject.parseArray(this.permissions, String.class);
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    /**
     * 设置权限，传入权限数组["user::add", "user:edit"]
     * @param permissions
     * @return
     */
    public void setPermissionList(String[] permissions) throws JsonProcessingException {
        this.permissions = JSON.toJSONString(permissions);
    }
    
    public String getPermissions() {
        return permissions;
    }

    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "AdminRole [roleName=" + roleName + ", permissions=" + permissions + ", id=" + id + ", createTime=" + createTime
                + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
    }

}